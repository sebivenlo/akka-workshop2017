package nl.fontys.sebi;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;


/**
 * Base class for actor tests. Provides basic setups
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 * @param <T> Actor Class to test
 */
public abstract class ActorTestBase<T extends Actor> {
    
    /**
     * Default timeout applies to waiting for answers
     */
    protected static final Timeout DEFAULT_TIMEOUT = new Timeout(3, TimeUnit.SECONDS);
    
    protected static ActorSystem system;
    
    /**
     * Instance of current actor ref to test
     */
    protected TestActorRef<T> actor;
    
    /**
     * Creates props to create the actor to test.
     * Will be used for TestActorRef.create(system, props)
     * 
     * @return Instance of Props
     */
    protected abstract Props getProps();
    
    /**
     * Initializes Actor System
     */
    @BeforeClass
    public static void setupClass() {
        system = ActorSystem.create();
    }
    
    /**
     * Shutdown actor system
     */
    @AfterClass
    public static void tearDownClass() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    
    @Before
    public void setupBase() {
        actor = TestActorRef.create(system, getProps());
    }
    
    @After
    public void tearDownBase() {
        actor = null;
    }
    
    /**
     * Creates mock actor which simply collects all messages received.
     * 
     * @see ActorMock
     * @return Mock actor
     */
    public TestActorRef<ActorMock> getActorMock() {
       return TestActorRef.create(system, Props.create(ActorMock.class));
    }
    
    /**
     * Creates mock actor which simply collects all messages received.
     * 
     * @see ActorMock
     * @see #getActorMock() 
     * @param name Name of the actor in path
     * @return Mock actor
     */
    public TestActorRef<ActorMock> getActorMock(String name) {
        return TestActorRef.create(system, Props.create(ActorMock.class), name);
    }
    
    /**
     * Check if actor answers which correct message
     * IMPORTANT: Message will never be the same reference therefore the message class MUST IMPLEMENT the equals method.
     * 
     * @see #testMessage(java.lang.Object, java.util.function.Consumer) 
     * @param message Message to send
     * @param expected Expected Message
     */
    public void testMessage(Object message, Object expected) {
        testMessage(message, (answer) -> {
            assertEquals(expected, answer);
        });
    }
    
    /**
     * Test sending message and receive an answer. Answer will be given to consumer.
     * 
     * Important: This method will not work if the actor does NOT reply using getSender()
     * 
     * @param message Message to send
     * @param consumer Logic to apply on answer
     */
    public void testMessage (Object message, Consumer<Object> consumer) {
        final CompletableFuture<Object> future = PatternsCS.ask(actor, message, DEFAULT_TIMEOUT).toCompletableFuture();
        assertTrue(future.isDone());
        try {
            consumer.accept(future.get());
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Mock class is used when an actor does not answer to getSender() but to some other actor ref.
     * @see #getActorMock() 
     * @see #getActorMock(java.lang.String) 
     */
    protected static class ActorMock extends AbstractActor {
        
        private Queue<Object> messages = new LinkedList<>();

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny(o -> {
                messages.add(o);
            }).build();
        }
        
        public boolean hasMessages() {
            return messages.peek() != null;
        }
        
        public Object nextMessage() {
            return messages.poll();
        }
    }
}
