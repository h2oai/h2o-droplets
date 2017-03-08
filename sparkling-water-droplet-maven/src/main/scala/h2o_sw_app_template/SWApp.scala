package h2o_sw_app_template
import org.apache.spark.SparkContext
import org.apache.spark.h2o.H2OContext
import org.apache.spark.sql.SQLContext
import water.support.SparkContextSupport

object SWApp extends SparkContextSupport {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(configure("h2o app name"))
    val sqlContext = new SQLContext(sc)
    val h2oContext = H2OContext.getOrCreate(sc)
  }
}
