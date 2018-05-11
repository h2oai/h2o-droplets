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

import org.apache.spark.SparkConf
import org.apache.spark.ml.h2o.models.H2OMOJOModel
import org.apache.spark.sql.SparkSession


/**
 * Example of Sparkling Water based application.
 */
object SparklingWaterScorerDroplet {

  // This can be HDFS URI
  val DATA_FILE = "data/prostate"

  // This can be HDFS URI
  val MOJO_MODEL = "data/prostate.mojo"

  def main(args: Array[String]) {

    val inputFile = if (args.length > 0) args(0) else DATA_FILE
    val mojoFile = if (args.length > 1) args(1) else MOJO_MODEL

    println(
      s"""
        |Using:
        |  input file:  ${inputFile}
        |  mojo file :  ${mojoFile}
      """.stripMargin)

    // Create Spark Context
    banner("Staring Spark Session")
    val conf = configure("Sparkling Water Scorer Droplet").set("spark.ext.h2o.repl.enabled", "false")
    val spark = SparkSession.builder.config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    
    // Load data to spark DataFrame
    banner(s"Loading input data from '${inputFile}'")
    val prostateDf = spark.read.parquet(DATA_FILE)
    banner(s"Input data schema")
    prostateDf.printSchema()

    // Load H2O MOJO
    banner(s"Loading MOJO from '${inputFile}'")
    val mojo = H2OMOJOModel.createFromMojo(mojoFile)

    // Make prediction
    banner(s"Making prediction with MOJO")
    val prediction = mojo.transform(prostateDf)
    banner("Prediction data frame has the following schema")
    prediction.printSchema()

    // Show prediction in pretty form
    banner("Actual content of prediction data frame")
    import org.apache.spark.sql.functions.{col, udf}
    val prettyFunc = (pred: Double) => {
      f"${pred}%.4f"
    }
    val prettyUdf = udf(prettyFunc)
    prediction.withColumn("prediction_output", prettyUdf(col("prediction_output.value"))).show()

    // Shutdown application
    spark.stop()
  }

  def configure(appName:String = "Sparkling Water Demo"):SparkConf = {
    val conf = new SparkConf()
      .setAppName(appName)
    conf.setIfMissing("spark.master", sys.env.getOrElse("spark.master", "local"))
    conf
  }

  def banner(msg:String) = println(s"\n*** ${msg} ****")
}
