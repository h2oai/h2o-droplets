# Spark and H2O Pojo

This is a simple example project to start coding with H2O Pojo.

## Dependencies
  - Spark 1.5

For more details see [build.gradle](build.gradle).

## Project structure
 
```
├─ gradle/        - Gradle definition files
├─ lib/           - Location for h2o-genmodel.jar
├─ src/           - Source code
│  ├─ main/       - Main implementation code 
│  │  ├─ scala/
│  │  ├─ pojo/    - Pojo code
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

## Creating and Running Spark Application

Create application assembly which can be directly submitted to Spark cluster:
```
./gradlew shadowJar
```

The command creates jar file `build/libs/cap1-assembler-all.jar` containing all necessary classes to run application on top of Spark cluster.

Submit application to Spark cluster (in this case, local cluster of 6nodes is used):
```
export MASTER='local-cluster[6,2,1024]'
$SPARK_HOME/bin/spark-submit --class examples.PojoExample build/libs/cap1-assembler-all.jar
```

