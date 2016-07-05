import sbt._
import Keys._

object Dependencies {
  val sparkVersion = "1.6"
  val sparklingWaterVersion = s"${sparkVersion}.4"

  val sparklinwatercore      = "ai.h2o" %% "sparkling-water-core"     % sparklingWaterVersion
  val sparklingwaterexamples = "ai.h2o" %% "sparkling-water-examples" % sparklingWaterVersion  

  val scalatest = "org.scalatest" %% "scalatest" % "2.2.1"
}
