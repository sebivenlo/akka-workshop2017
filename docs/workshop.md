# Workshop


### Maven Dependencies
If you want to start a new project using akka, you need the following maven dependencies:
```xml
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-actor_2.12</artifactId>
    <version>2.5.6</version>
    <type>jar</type>
</dependency>
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-testkit_2.12</artifactId>
    <version>2.5.6</version>
    <scope>test</scope>
    <type>jar</type>
</dependency>
```

## Begin

1. Download prepared workshop task: [Akka Task](https://rawgit.com/sebivenlo/akka-workshop2017/master/tutorial/Task.zip)
2. Open Project with your IDE - perferably NetBeans
3. Look at TODOs in the comments (On NetBeans Ctrl + 6)
4. Try to implement the missing functionality

There are also questions at certain locations. Try to answer them, this will help you to understands the concepts.

### Task Overview
Do you remember the restaurant task from the [JAVA3](https://java3.fontysvenlo.org) course?
([Original assignment description](https://java3.fontysvenlo.org/material/clab-1_en.pdf))

One of the most challenging part of this assignment is the synchronization between the restaurant manager, the chef who cooks the meals and the waiter who collects orders and serves meals.
There are a lot of different possibilities to solve the problems, some of them works with the some Java features which are available now for a long time (see [wait-notify](https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html)).
You can also make use of the advanced [Java Executor Framework](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)

For our workshop task we will try to implement the a restaurant using **Akka Actors**.


### Structure


### Parts to implement



### Improvements?

The task offers a lot of opportunities to improve the implementation.
* Increase number of recipes
* Create customer-actors outside of restaurant
* Make customer order not random
* Direct communication between Waiter and customer using ActorSelections
* Direct communication between Chef and Waiter using ActorSelection

You can also think of your own improvements. Maybe restructuring the whole application. 
Or pulling out some actors to run somewhere else and connecting them using networks (see [Akka Networking](https://doc.akka.io/docs/akka/2.5/java/index-network.html))
