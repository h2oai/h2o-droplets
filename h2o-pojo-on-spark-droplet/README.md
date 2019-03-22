# Spark and H2O Pojo

This is a simple example project to start with H<sub>2</sub>O Pojo. 

Recommendation: If you plan to use H2O POJO/MOJO in Spark environment, we sugget to have a look at Sparkling Water
project.

## Dependencies
  - Spark 2.4

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

## Starting with Idea

To open this project in InteliJ, import it as a Gradle project
via _New > Project From Existing Sources > and select Gradle_
    
> Note: To clean up Idea project files please launch `./gradlew cleanIdea`

## Starting with Eclipse
  1. Generate Eclipse project files via `./gradlew eclipse`
  2. Open project in Eclipse via _File > Import > Existing Projects into Workspace_

> Note: To clean up Eclipse project files please launch `./gradlew cleanEclipse`

## Project building

For building, please, use encapsulated `gradlew` command:
```
./gradlew build
```

### Run demo
For running a simple application on top of Spark:
```
./gradlew run
```

## Run tests

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

Submit application to a local Spark cluster:
```
export MASTER='local[*]'
$SPARK_HOME/bin/spark-submit --class examples.PojoExample build/libs/cap1-assembler-all.jar
```

