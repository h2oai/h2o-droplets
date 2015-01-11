# Sparkling Water Example Project

This is a simple example project to start coding with Sparkling Water.

## Project structure
 
```
├─ gradle/        - Gradle definition files
├─ src/           - Source code
│  ├─ main/       - Main implementation code 
│  │  ├─ scala/
│  ├─ test/       - Test code
│  │  ├─ scala/
├─ build.gradle   - Build file for this project
├─ gradlew        - Gradle wrapper 
```

## Project building

For building, please, use provided `gradlew` command:
```
./gradlew build
```

## Starting with Idea

There are two ways to open this project in Idea

  * Using Gradle build file directly
    1. Open project's `build.gradle` in Idea via _File > Open_ 
    
or
  
  Using Gradle generated project files
    1. Generate Idea configuration files via
      ```
      ./gradlew idea
      ```
    2. and open project in Idea via _File > Open_
    
> Note: To clean up Idea project files please launch `./gradlew cleanIdea`

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
The command creates jar file `build/libs/sparkling-water-droplet-app.jar` containing all necessary classes to run application on top of Spark cluster.

Submit application to Spark cluster (in this case, local cluster is used):
```
export MASTER='local-cluster[3,2,1024]'
$SPARK_HOME/bin/spark-submit --class water.droplets.SparklingWaterDroplet build/libs/sparkling-water-droplet-app.jar
```




