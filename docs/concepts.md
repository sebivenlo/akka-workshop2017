# Basic Concepts of Akka

## The Actor model
The actor model is a concurrecny model which describes a system of blackboxes communicating through immutable messages. A good way to think in this: How do I implement this using multiple computers?

## Messages

Messages are the basic communication concept inside Akka Actors.
By convention, all messages have to be **IMMUTABLE**. Which means, that the state of the Message **and of all attributes** cannot change after construction. 
Immutable objects cannot create data races, because to change them, you need to create a whole new object. The original one - which has been shared among the threads - will stay the same.

One of the easiest way to achieve immutability is, when your class _does not have a state at all_.
If you really need to transport data, make sure that you cannot change the attributes and that your attributes are immuatable as well.

Example:
```java
public class NotImmutable {
  private final List<Object> list;
  public List<Object> getList() {
    return list; // List ca be altered outside
  }
}

public class NotImmutable2 {
  private final List<Object> list;
  public NotImmutable(final List<Object> list) {
    // List can be changed after given to the constructor
    this.list = list;
  }
  public List<Object> getList() {
    // List cannot be altered outside
    return Collections.unmodifiableList(list);
  }
}

public class Immutable {
  // final primitives are always immutable
  private final int number;
  // Same applies to objects of primitives 
  private final Double ratio;
  // String is an immutable type
  private final String name;
}
```

Small hint: If you, for some reasons, need to throw an exception inside the constructor, make sure your object is in a well-defined state.

## Actors
An actor represents a worker thread for the system. You can start actors as much as you like or need.
Different actors can respond to different messages, you can simply send your messages to all actors hoping someone will handle it. This is obviously not very efficient. Maybe you will get multiple results or - even worse - none at all.



# Examples

## Actor System

```java
ActorSystem system = ActorSystem.create();
system.terminate();
```

## Actors
```java
ActorRef restaurant = system.actorOf(Props.create(Restaurant.class), "restaurant");
ActorRef chef = getContext().actorOf(Props.create(Chef.class), "chef");
```

## Sending Messages
```java
restaurant.tell(new DoSomethingMessage(), ActorRef.noSender());
chef.tell(new DoSomethingMessage(), getSelf());
```

## Actor Path
Every actor has an unique address which can be used to address them. This addresses auses the concepts of [URI](https://en.wikipedia.org/wiki/Uniform_Resource_Identifier) which are also used to identifiy resources on the internet or on file systems.
The biggest advantages of this concept is, that it does not mattter on which machine it lives, if you can address it uniquely, you can use it. 
Addresses can be absoulte, just like in filesystems or internet addresses, or can be relative to the actor you performs the query.
You can simple query a an actor by address:
```java
context.actorSelection("/user/chef")
```

## Create actor pool
If you want to create multiple actors of the same type, Akka will make it really easy.
You can use a message-routing strategy (pool) of your choice annd Akka creates all the actors and sending the messages according to the chosen strategy. For your implementation there is no difference between using a single actor or multiple actors with a message-routing strategy.

```java
// Create 5 actor instances
getContext().actorOf(new RoundRobinPool(5).props(Props.create(Chef.class)), "chefs");

// Dynamicly create instances
DefaultResizer resizer = new DefaultResizer(2, 15);
getContext().actorOf(new RoundRobinPool(5).withResizer(resizer).props(Props.create(Waiter.class)), "waiters");
```

Different types of pools:
* Round Robin Pool
* Balancing Pool
* Random Pool
* SmallestMailbox Pool
* Broadcast Pool
* Some other types for special scenarios


[Prev](concepts.md) | [Next](workshop.md)
