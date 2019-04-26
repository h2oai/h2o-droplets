import sbt._
import Keys._

object Dependencies {
  val sparkVersion = "2.4"
  val sparklingWaterVersion = s"${sparkVersion}.7"

  val sparklinwatercore      = "ai.h2o" %% "sparkling-water-core"     % sparklingWaterVersion
  val sparklingwaterexamples = "ai.h2o" %% "sparkling-water-examples" % sparklingWaterVersion
  val sparklinwatercore      = "ai.h2o" %% "sparkling-water-repl"     % sparklingWaterVersion
  val sparklingwaterexamples = "ai.h2o" %% "sparkling-water-ml"       % sparklingWaterVersion

  val scalatest = "org.scalatest" %% "scalatest" % "2.2.1"
}
