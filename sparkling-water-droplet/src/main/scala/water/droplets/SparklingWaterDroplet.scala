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

import ai.h2o.sparkling.ml.algos.H2OGBM
import org.apache.spark.SparkConf
import org.apache.spark.h2o._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
 * Example of Sparkling Water based application.
 */
object SparklingWaterDroplet {

  def main(args: Array[String]) {
    // Create Spark Session
    val conf = new SparkConf()
      .setAppName("Sparkling Water Droplet")
      .setMaster("local")
    val spark = SparkSession
      .builder
      .config(conf)
      .getOrCreate()

    // Create H2O Context
    val h2oContext = H2OContext.getOrCreate()

    val irisTable = spark.read.option("header", "true").option("inferSchema", "true").csv("data/iris.csv")

    // Build GBM model
    val model = new H2OGBM()
      .setLabelCol("class")
      .setNtrees(5)
      .fit(irisTable)

    // Make prediction on train data
    val predictions = model.transform(irisTable)

    // Compute number of miss-predictions with help of Spark API

    // Make sure that both DataFrames has the same number of elements
    assert(irisTable.count() == predictions.count)
    val irisTableWithId = irisTable.select("class").withColumn("id", monotonically_increasing_id())
    val predictionsWithId = predictions.select("prediction").withColumn("id", monotonically_increasing_id())
    val df = irisTableWithId.join(predictionsWithId, "id").drop("id")
    val missPredictions = df.filter(df("class")=!=df("prediction"))
    val numMissPredictions = missPredictions.count()

    println(
      s"""
         |Number of miss-predictions: $numMissPredictions
         |
         |Miss-predictions:
         |
         |actual X predicted
         |------------------
         |${missPredictions.collect().mkString("\n")}
       """.stripMargin)

    // Shutdown application
    h2oContext.stop(true)
  }
}
