# The Actor model
The actor model is a concurrecny model which describes a system of blackboxes communicating through immutable messages. A good way to think in this: How do I implement this using multiple computers?

## Messages



## Actors
An actor represents a worker thread for the system. You can start actors as much as you like or need.
Different actors can respond to different messages, you can simply send your messages to all actors hoping someone will handle it. This is obviously not very efficient. Maybe you will get multiple results or - even worse - none at all.




# Basic Concepts of Akka

### Actor System

```java
ActorSystem system = ActorSystem.create();
system.terminate();
```

### Actors
```java
ActorRef chef = getContext().actorOf(Props.create(Chef.class), "chef");
```


### Actor Path
Every actor has an unique address which can be used to address them. This addresses auses the concepts of [URI](https://en.wikipedia.org/wiki/Uniform_Resource_Identifier) which are also used to identifiy resources on the internet or on file systems.
The biggest advantages of this concept is, that it does not mattter on which machine it lives, if you can address it uniquely, you can use it. 
Addresses can be absoulte, just like in filesystems or internet addresses, or can be relative to the actor you performs the query.
You can simple query a an actor by address:
```java
context.actorSelection("/user/chef")
```


### Running bulk-task using load balancing
```java
getContext().actorOf(new RoundRobinPool(5).props(Props.create(Chef.class)), "chef");
```
