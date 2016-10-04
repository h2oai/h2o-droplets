import sbt._
import Keys._

object Dependencies {
  val sparkVersion = "2.0.0"
  // TODO change this before merging!!!
  val sparklingWaterVersion = "2.0.99999-SNAPSHOT"

  val sparklinwatercore      = "ai.h2o" %% "sparkling-water-core"     % sparklingWaterVersion
  val sparklingwaterexamples = "ai.h2o" %% "sparkling-water-examples" % sparklingWaterVersion  

  val scalatest = "org.scalatest" %% "scalatest" % "2.2.1"
}
