# H2O Example Project

This is a simple example project to start with H<sub>2</sub>O.

## Project dependencies

The project is built on top of:
  - H2O 3.16.0.4 Wheeler release

For more details, please, see [build file](build.gradle).

## Project structure
 
```
├─ gradle/        - Gradle definition files
├─ src/           - Source code
│  ├─ main/       - Main implementation code 
│  │  ├─ java/
│  ├─ test/       - Test code
│  │  ├─ java/
├─ build.gradle   - Build file for this project
├─ gradlew        - Gradle wrapper 
```

## Project building

For building, please, use encapsulated `gradlew` command:
```
./gradlew build
```

## Starting with Idea

There are two ways to open this project in Idea

  * Using Gradle build file directly
    1. Open project's `build.gradle` in Idea via _File > Open_ 
    
or
  
  * Using Gradle generated project files
    1. Generate Idea configuration files via
      ```
      ./gradlew idea
      ```
    2. and open project in Idea via _File > Open_
    
> Note: To clean up Idea project files please launch `./gradlew cleanIdea`

## Starting with Eclipse
  1. Generate Eclipse project files via `./gradlew eclipse`
  2. Open project in Eclipse via _File > Import > Existing Projects into Workspace_


> Note: To clean up Eclipse project files please launch `./gradlew cleanEclipse`

## Run tests

To run tests, please, run:
```
./gradlew test
```






