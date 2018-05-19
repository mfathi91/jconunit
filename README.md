[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mfathi91/jconunit/badge.svg)](http://search.maven.org/#search|ga|1|com.github.mfathi91/jconunit)

# JConUnit

JConUnit is a tiny library for unit testing in multithreaded environment. As so far JUnit has not added concurrent execution to its 
framework, this library can be used to facilitate concurrent unit testing.

## Instructions

### Maven
Include the following to your dependency list:
```xml
<dependency>
    <groupId>com.github.mfathi91</groupId>
    <artifactId>jconunit</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

### Usage
For instance one may use JConUnit like the following code:
```java
@Tese
public void junitTestMethod(){
    Runnable task = () -> System.out.println("MyCustomRunnable");
    Executable executable = Executable.of(task, 10);  // set task and number of threads to run the task
    JConUnit.assertTimeout(Duration.ofMillis(100), executable);
}
```
### Requirements
This version of Persian Date Time requires:
 * Java SE 8

## License
This library is released under MIT license.
