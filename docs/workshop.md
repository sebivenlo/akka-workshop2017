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
5. There are some tests but not everything is covered.
6. Improve the program with one of the ideas below or come up with your own improvement

There are also questions at certain locations. Try to answer them, this will help you to understands the concepts.

### Task Overview
Do you remember the restaurant task from the [JAVA3](https://java3.fontysvenlo.org) course?
([Original assignment description](https://java3.fontysvenlo.org/material/clab-1_en.pdf))

One of the most challenging part of this assignment is the synchronization between the restaurant manager, the chef who cooks the meals and the waiter who collects orders and serves meals.
There are a lot of different possibilities to solve the problems, some of them works with the some Java features which are available now for a long time (see [wait-notify](https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html)).
You can also make use of the advanced [Java Executor Framework](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)

For our workshop task we will try to implement the a restaurant using **Akka Actors**.

### Structure

__Actors__
The program consists of four actors: 
* The restaurant is responsible to manage the basic tasks and to "employ the staff"
* Customers should order meals and eat them when they are served
* Chefs are responsible to cook customer´s order
* Waiter are responsible to record the orders and serve the prepared meals

__Messages__
* OpeningMessage - _The restaurant opens, staff should be employed_
* EnteringMessage - _A customer enters the restaurant_
* WhatsYourOrder - _A waiter asks for the customer´s order_
* CompleteOrder - _Complete order of a customer_
* PreparedMeal - _One prepared meal ready to serve_
* EatingFinished - _A customer has finished eating, the whole order has been served_
* ClosingMessage - _The restaurant closes. Kick out customers and fire staff_

__Recipes__
The assignment already consists of a set of recipes. Feel free to add your own ones.

### Improvements?

The task offers a lot of opportunities to improve the implementation.
* Increase number of recipes
* Create customer-actors outside of restaurant
* Make customer order not random
* Direct communication between Waiter and customer using ActorSelections
* Direct communication between Chef and Waiter using ActorSelection
* Is everything for free? Shouldn't the customer pay for the meals?

You can also think of your own improvements. Maybe restructuring the whole application. 
Or pulling out some actors to run somewhere else and connecting them using networks (see [Akka Networking](https://doc.akka.io/docs/akka/2.5/java/index-network.html))

[Prev](concepts.md)
