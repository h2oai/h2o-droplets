/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package water.droplets

import hex.tree.gbm.GBMModel
import org.apache.spark.h2o.H2OContext
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{HashingTF, IDF, RegexTokenizer, StopWordsRemover}
import org.apache.spark.ml.h2o.features.ColumnPruner
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import _root_.hex.grid.GridSearch
import hex.ModelMetrics
import hex.genmodel.algos.gbm.GbmMojoModel

import scala.collection.JavaConverters._

/**
  * Example of Sparkling Water based application.
  */
object H2OSWMixedAPIDroplet {

  def main(args: Array[String]) {

    // Create Spark Context
    val conf = configure("H2O-SW mixed API Droplet")
    val spark = SparkSession.builder.config(conf).getOrCreate()
    import spark.implicits._

    // Create H2O Context
    val hc = H2OContext.getOrCreate(spark)
    import hc.implicits._

    // Load data from file
    val dataDF = spark.read
      .option("delimiter", "\t")
      .schema(StructType(Array(
        StructField("label", StringType, nullable = false),
        StructField("text", StringType, nullable = false),
        StructField("weight", IntegerType, nullable = false),
        StructField("fold", IntegerType, nullable = false))))
      .csv("data/smsData.txt")

    val Array(trainingDF, testingDF) = dataDF.randomSplit(Array(0.9, 0.1))

    // Perform pre-processing and feature engineering with Apache Spark
    val preProcessingPipeline = sparkPreprocessingPipeline(spark).fit(trainingDF)

    val preProcessedTrainingDF = preProcessingPipeline.transform(trainingDF)
    val trainingH2OFrame = hc.asH2OFrame(preProcessedTrainingDF)

    val preProcessedTestingDF = preProcessingPipeline.transform(testingDF)
    val testingH2OFrame = hc.asH2OFrame(preProcessedTestingDF)


    // Setup hyper-parameter search space
    val hyperParams = Map[String, Array[AnyRef]](
      "_ntrees" -> Array[Integer](5, 10, 15).asInstanceOf[Array[AnyRef]],
      "_max_depth" -> Array[Integer](2, 5).asInstanceOf[Array[AnyRef]],
      "_learn_rate" -> Array[java.lang.Double](0.01, 0.1, 0.3).asInstanceOf[Array[AnyRef]]
    ).asJava

    // Prepare training dataset to be passed to a H2O algorithm
    val params = new GBMModel.GBMParameters
    params._train = trainingH2OFrame.key
    params._response_column = "label"
    params._ignored_columns = Array("text")
    params._weights_column = "weight"
    params._fold_column = "fold"

    trainingH2OFrame.replace(0, trainingH2OFrame.vec("label").toCategoricalVec)

    // Fire off a grid search
    val gs = GridSearch.startGridSearch(null, params, hyperParams)
    val grid = gs.get()

    // Get the best model from the grid based on AUC metric
    val model = grid.getModels.sortBy(_.auc())(Ordering[Double].reverse).head

    // Get the corresponding GBM MOJO model
    val mojoModel = model.toMojo.asInstanceOf[GbmMojoModel]

    // Print values of hyper-parameters for a selected model (e.g. 'ntrees')
    println(s"NTreeGroups: ${mojoModel.getNTreeGroups}")

    // Print AUC metric for the model based on training datasets (k-fold cross validation)
    println(s"Training AUC: ${model._output._training_metrics.auc_obj()._auc}")

    // Print AUC metric for the model based on validation datasets (k-fold cross validation)
    println(s"Validation AUC: ${model.auc()}")

    // Make prediction on the best model
    val h2oPrediction = model
        .score(testingH2OFrame)
        .add(testingH2OFrame)

    // Print AUC metric for the model based on the testing dataset
    println(s"Testing AUC: ${ModelMetrics.getFromDKV(model, testingH2OFrame).auc_obj()._auc}")

    // Convert testing frame with predictions back to Spark and show results
    val predictionDF = hc.asDataFrame(h2oPrediction)
    predictionDF
      .select($"text", $"label", $"predict" as "prediction", $"ham", $"spam")
      .show()

    // Shutdown application
    hc.stop(true)
  }

  def sparkPreprocessingPipeline(spark: SparkSession): Pipeline = {

    // Tokenize messages and split sentences into words.
    val tokenizer = new RegexTokenizer()
      .setInputCol("text")
      .setOutputCol("words")
      .setMinTokenLength(3)
      .setGaps(false)
      .setPattern("[a-zA-Z]+")

    // Remove words that do not bring much value to the model
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("filtered")
      .setStopWords(Array("the", "a", "", "in", "on", "at", "as", "not", "for"))
      .setCaseSensitive(false)

    // Create hashes for the observed words.
    val hashingTF = new HashingTF()
      .setNumFeatures(1 << 10)
      .setInputCol(stopWordsRemover.getOutputCol)
      .setOutputCol("wordToIndex")

    // Create an IDF model. This creates a numerical representation of how much information a given word provides in the whole message.
    val idf = new IDF()
      .setMinDocFreq(4)
      .setInputCol(hashingTF.getOutputCol)
      .setOutputCol("tf_idf_features")

    // Remove unnecessary columns
    val colPruner = new ColumnPruner()
      .setColumns(Array[String](hashingTF.getOutputCol, stopWordsRemover.getOutputCol, tokenizer.getOutputCol))

    // Assemble stages
    new Pipeline()
      .setStages(Array(tokenizer, stopWordsRemover, hashingTF, idf, colPruner))
  }


  def configure(appName: String): SparkConf = {
    new SparkConf()
      .setAppName(appName)
      .setIfMissing("spark.master", sys.env.getOrElse("spark.master", "local"))
      .set("spark.sql.autoBroadcastJoinThreshold", "-1")
  }
}
