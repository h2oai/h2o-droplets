# Example Project Mixing Sparkling Water and H2O-3 API

This is a simple Scala project that demonstrates how to combine Sparkling Water API with H20-3 internal Java API.

## Dependencies
This droplet uses Sparkling Water 2.4.9 which integrates:
  - Spark 2.4
  - H2O 3.24.0.1

For more details see [build.gradle](build.gradle).

## Project structure
 
```
├─ gradle/        - Gradle definition files
├─ src/           - Source code
│  ├─ main/       - Main implementation code 
│  │  ├─ scala/
├─ build.gradle   - Build file for this project
├─ gradlew        - Gradle wrapper 
```

## Project building

For building, please, use provided `gradlew` command:
```
./gradlew build
```

### Run demo
For running a simple application:
```
./gradlew run
```

## Starting with Idea

To open this project in InteliJ, import it as a Gradle project
via _New > Project From Existing Sources > and select Gradle_
    
## Starting with Eclipse
  1. Generate Eclipse project files via `./gradlew eclipse`
  2. Open project in Eclipse via _File > Import > Existing Projects into Workspace_


## Running tests

To run tests, please, run:
```
./gradlew test
```

# Checking code style

To check codestyle:
```
./gradlew scalaStyle
```

## Creating and Running Spark Application

Create application assembly which can be directly submitted to Spark cluster:
```
./gradlew shadowJar
```
The command creates jar file `build/libs/h2o-sw-mixed-api-droplet-app.jar` containing all necessary classes to run application on top of Spark cluster.

Submit application to Spark cluster (in this case, local cluster is used):
```
export MASTER="local[*]"
$SPARK_HOME/bin/spark-submit --class water.droplets.H2OSWMixedAPIDroplet build/libs/h2o-sw-mixed-api-droplet-app.jar
```




