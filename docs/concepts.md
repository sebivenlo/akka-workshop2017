# Basic Concepts of Akka

## The Actor Model
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

## Define Actor Classes
Defining your own actor is very simple. You only need to extend from AbstractActor provided by Akka and implements one method.
The createReceive() method returns a setup of (lambda-)functions which are called when a certain message is received.
```java
public class Actor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Message.class, msg -> {
                // Do something
             }).build();
    }
}
```

## Actor System
Before you can start an actor, you need t0 create an Actor System which will supervise all actors.
```java
ActorSystem system = ActorSystem.create();

// System´s termination will stop all actors
system.terminate();
```

## Creating Actors
```java
ActorRef restaurant = system.actorOf(Props.create(Restaurant.class), "restaurant");
ActorRef chef = getContext().actorOf(Props.create(Chef.class), "chef");
```

## Sending Messages
Sending Messages is very easy, just "tell" the actor what you want to send. Important is to think about who is the sender of this message. Normally, the actor reponse to this "sender". In some cases, this is not a trivial problem.
You can send any object you like as message but remember: __Message must be immutable__.
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
context.actorSelection("/user/actor"); // select specific actor
context.actorSelection("/user/parent/child"); // Select child of some parent actor
context.actorSelection("../brother"); // Select actor relative to current actor
context.actorSelection("akka.tcp://user@host:2552/user/actor"); // Select remote actor
```
_Be aware that actor selection says nothing whether there is an actual actor with this name. You need to do extra steps to find out whether there is an actor matching your path. Otherwise your messages will be lost._

## Stopping Actors
Stopping actors is inevitable at some point in time. Actors uses a considerable amount of memory, if you fail to stop them properly, you will run out of memory.

You can stop actors by telling them directly to stop. If you stop an actor which is the parent of one or more child actor, the parent actor will stop the children first and stop themself after the last child has been stopped.
```java
// Stop child actors
getContext().stop(child);

// Stop self
getContext().stop(getSelf());
```

If you want to stop the actor after processing all previous messages, you can add up a PoisonPill message in the message queue.
The actor will stop after processing this message.
```java
// Lineup poison pill in message queue
actor.tell(PoisonPill.getInstance(), ActorRef.noSender());
```

Terminating the Actor System will automatically stop all actors.
```java
system.terminate();
```

## Create Actor pool
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

## Advanced Patterns
Akka provides some helpers for commonly used patterns.
For example you can "ask" something which means that you send a message to an actor and wait for an answer.
You can also pipe the answer directly to another actor.

```java
CompletionStage<Object> cs = PatternsCS.ask(actor, new Message(), 3000); // Returns a Completion Stage which allows to handle the actor`s answer asynchronously when it occures or give it to another actor.
PatternsCS.pipe(cs, getContext().dispatcher()).to(actor2);
```

## Setup Akka Project

__Maven Dependencies__ <br>
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

# Other Parts of Akka

### [Akka Networking](https://doc.akka.io/docs/akka/current/java/index-network.html)
Akka subsystem which allows to communicate with actor via TCP or UDP.

### [Akka Streams](https://doc.akka.io/docs/akka/current/java/stream/index.html)
An intuitive and safe way to do asynchronous, non-blocking backpressured stream processing.

### [Akka HTTP](https://doc.akka.io/docs/akka-http/current/java/http/index.html)
Asynchronous, modern and fast HTTP server and client.

### [Akka Persistence](https://doc.akka.io/docs/akka/current/java/persistence.html)
Preserve the state of an actor so it can reach the same state after restart.

<br><br>

[Prev](concepts.md) | [Next](workshop.md)
