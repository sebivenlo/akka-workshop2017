# Workshop

### Getting Started
Use NetBeans and Maven to fulfill this workshop.


Create a new Maven Project and add `akka-actor` as dependency:
```xml
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-actor_2.11</artifactId>
    <version>2.5.4</version>
    <type>jar</type>
</dependency>
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-testkit_2.11</artifactId>
    <version>2.5.4</version>
    <scope>test</scope>
    <type>jar</type>
</dependency>
```
