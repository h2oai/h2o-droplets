package water.droplets

import hex.genmodel.GenModel
import hex.genmodel.easy.{EasyPredictModelWrapper, RowData}
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession

/**
  * Simple example showing how to use POJO directly from Spark.
  */
object UsePojoDroplet extends Logging {

  def main(args: Array[String]): Unit = {
    logInfo("Starting Spark Session")
    val conf = new SparkConf().setAppName("Sparkling Water Droplet").setMaster("local")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    logInfo(s"Loading data from data/iris.csv into DataFrame")
    val testData = spark.read.option("header", "true").option("inferSchema", "true").csv("data/iris.csv")

    logInfo("Loading POJO by its class name: 'gbm_aa57ff38_927d_40ba_973b_4b0f5e281d03'")
    // We use classloading here instead of referencing pojo directly.
    // In normal case, you should use here a dedicated URI classloader to load
    // POJO from given URI.
    val genModel: GenModel = Class.forName("gbm_aa57ff38_927d_40ba_973b_4b0f5e281d03")
      .newInstance().asInstanceOf[GenModel]
    // Create EasyWrapper for generated model
    val easyModel = new EasyPredictModelWrapper(genModel)
    val header = genModel.getNames

    logInfo("Making prediction using loaded POJO...")
    // Now we hava pojo ready and can generate prediction for each row
    // It is a categorical model with 3 output categories
    logInfo("  - pojo type:          " + easyModel.getModelCategory)
    logInfo("  - pojo input columns: " + header.mkString(","))
    logInfo("  - pojo prediction categories: " + easyModel.getResponseDomainValues.mkString(","))

    val predictionRdd = testData.rdd.map(row => {
      val r = new RowData
      // This is simple use that header of POJO is matching
      header.indices.foreach(idx => r.put(header(idx), row.getDouble(idx).asInstanceOf[AnyRef]))
      val prediction = easyModel.predictMultinomial(r)
      prediction
    })

    // Collect predictions and print them
    predictionRdd.collect().foreach { prediction =>
      logInfo(s"Prediction: ${prediction.label}: ${prediction.classProbabilities.mkString(",")}")
    }

    spark.stop()
  }
}
