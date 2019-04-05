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

import java.io.File

import hex.tree.gbm.GBM
import hex.tree.gbm.GBMModel.GBMParameters
import org.apache.spark.h2o.{StringHolder, H2OContext}
import org.apache.spark.{SparkFiles, SparkConf}
import org.apache.spark.sql.SparkSession
import water.fvec.H2OFrame


/**
  * Example of Sparkling Water based application.
  */
object SparklingWaterDroplet {

  def main(args: Array[String]) {

    // Create Spark Context
    val conf = configure("Sparkling Water Droplet")
    val spark = SparkSession.builder.config(conf).getOrCreate()

    // Create H2O Context
    val h2oContext = H2OContext.getOrCreate(spark)
    import h2oContext.implicits._

    // Register file to be available on all nodes
    spark.sparkContext.addFile(new File("data/iris.csv").getAbsolutePath)

    // Load data and parse it via h2o parser
    val irisTable = new H2OFrame(new File(SparkFiles.get("iris.csv")))

    // Build GBM model
    val gbmParams = new GBMParameters()
    gbmParams._train = irisTable
    gbmParams._response_column = "class"
    gbmParams._ntrees = 5

    val gbm = new GBM(gbmParams)
    val gbmModel = gbm.trainModel.get

    // Make prediction on train data
    val predict = gbmModel.score(irisTable).subframe(Array("predict"))

    // Compute number of mispredictions with help of Spark API
    val trainRDD = h2oContext.asRDD[StringHolder](irisTable.subframe(Array("class")))
    val predictRDD = h2oContext.asRDD[StringHolder](predict)

    // Make sure that both RDDs has the same number of elements
    assert(trainRDD.count() == predictRDD.count)
    val numMispredictions = trainRDD.zip(predictRDD).filter(i => {
      val act = i._1
      val pred = i._2
      act.result != pred.result
    }).collect()

    println(
      s"""
         |Number of mispredictions: ${numMispredictions.length}
         |
         |Mispredictions:
         |
         |actual X predicted
         |------------------
         |${numMispredictions.map(i => i._1.result.get + " X " + i._2.result.get).mkString("\n")}
       """.stripMargin)

    // Shutdown application
    h2oContext.stop(true)
  }

  def configure(appName: String = "Sparkling Water Demo"): SparkConf = {
    new SparkConf()
      .setAppName(appName)
      .setIfMissing("spark.master", sys.env.getOrElse("spark.master", "local"))
      .set("spark.sql.autoBroadcastJoinThreshold", "-1")
  }
}
