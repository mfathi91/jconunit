[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mfathi91/jconunit/badge.svg)](http://search.maven.org/#search|ga|1|com.github.mfathi91/jconunit)
<a href="https://scan.coverity.com/projects/mfathi91-jconunit">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/21151/badge.svg"/>
</a>

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
    <version>3.0.0</version>
    <scope>test</scope>
</dependency>
```

### Usage
For instance one may use JConUnit like the following codes:
```java
@Tese
public void junitTestMethod(){
    ThreadSafeClass threadSafe = new ThreadSafeClass();
    List<Runnable> runnables = Collections.nCopy(10, threadSafe::foo);
    // 'foo' method is designed to not throw any exception in multithreaded environment
    JConUnit.assertDoesNotThrowException(runnables);
}
```
or
```java
@Tese
public void junitTestMethod(){
    ThreadSafeClass threadSafe = new ThreadSafeClass();
    List<Runnable> runnables = Collections.nCopy(2, threadSafe::bar);
    // 'bar' method is designed to throw IllegalStateException when it is 
    // accessed from multiple threads
    JConUnit.assertThrows(IllegalStateException.class, runnables);
}
```
### Requirements
This version of Persian Date Time requires:
 * Java SE 8

## License
This library is released under MIT license.
