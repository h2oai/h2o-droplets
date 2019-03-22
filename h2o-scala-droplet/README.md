# H2O Example Project

This is a simple example project to start with H<sub>2</sub>O.

## Project dependencies

The project is built on top of:
  - H2O 3.22.1.6 release

For more details, please, see [build file](build.gradle).


## Project structure
 
```
├─ gradle/        - Gradle definition files
├─ src/           - Source code
│  ├─ main/       - Main implementation code 
│  │  ├─ java/
│  │  ├─ scala/
│  ├─ test/       - Test code
│  │  ├─ scala/
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
For running a simple application:
```
./gradlew run
```

## Run tests

To run tests, please, run:
```
./gradlew test
```

# Checking code style

To check codestyle:
```
./gradlew scalaStyle
```






