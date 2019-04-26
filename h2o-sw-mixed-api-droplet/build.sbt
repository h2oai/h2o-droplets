import Dependencies._

//
// Common configuration
//
lazy val commonSettings = Seq(
  organization := "ai.h2o",
  version := "0.1.0",
  // set the Scala version used for the project
  scalaVersion := "2.11.12"
)

// 
// Common dependencies
// 
lazy val commonDeps = Seq(
  sparklingwaterexamples,
  scalatest % Test
) 

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    // set the name of the project
    name := "h2o-sw-mixed-api-droplet",

    // setup dependencies
    libraryDependencies ++= commonDeps,

    // setup resolvers
    // resolvers := oracleResolvers
    
    // append -deprecation to the options passed to the Scala compiler
    scalacOptions += "-deprecation",

    // set the main class for packaging the main jar
    // 'run' will still auto-detect and prompt
    // change Compile to Test to set it for the test jar
    mainClass in (Compile, packageBin) := Some("water.droplets.H2OSWMixedAPIDroplet"),

    // set the main class for the main 'run' task
    // change Compile to Test to set it for 'test:run'
    mainClass in (Compile, run) := Some("water.droplets.H2OSWMixedAPIDroplet"),

    // disable updating dynamic revisions (including -SNAPSHOT versions)
    offline := true,

    // fork a new JVM for 'run' and 'test:run'
    fork := true,

    // fork a new JVM for 'test:run', but not 'run'
    fork in Test := true,

    // add a JVM option to use when forking a JVM for 'run'
    javaOptions += "-Xmx2G",

    // Execute tests in the current project serially
    //   Tests from other projects may still run concurrently.
    parallelExecution in Test := false,

    // only show warnings and errors on the screen for compilations.
    //  this applies to both test:compile and compile and is Info by default
    logLevel in compile := Level.Warn,

    // only show 10 lines of stack traces
    traceLevel := 10,

    // only show stack traces up to the first sbt stack frame
    traceLevel := 0
  )

