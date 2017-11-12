package nl.fontys.sebi.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import java.util.ArrayList;
import nl.fontys.sebi.ActorTestBase;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.CustomerEntered;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.messages.WhatsYourOrder;
import nl.fontys.sebi.recipes.TestRecipe;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class WaiterTest extends ActorTestBase<Waiter> {
    
    @Override
    protected Props getProps() {
        return Props.create(Waiter.class);
    }
    
    @Test
    public void testCustomerEntered() throws InterruptedException {
        TestActorRef<ActorMock> mock = getActorMock();
        actor.tell(new CustomerEntered(mock), TestActorRef.noSender());

        Thread.sleep(10); // Wait 10 ms to handle message; Should in real be much faster
        
        assertTrue(mock.underlyingActor().hasMessages());
        assertEquals(new WhatsYourOrder(), mock.underlyingActor().nextMessage());
    }
    
    @Test
    public void testCompleteOrderDispatch() throws InterruptedException {
        // Need name for selection
        TestActorRef<ActorMock> mock = getActorMock("restaurant");
        
        CompleteOrder order = new CompleteOrder(TestActorRef.noSender(), new ArrayList<>(0));
        
        actor.tell(order, TestActorRef.noSender());
        Thread.sleep(10); // Wait 10 ms to handle message; Should in real be much faster

        assertTrue(mock.underlyingActor().hasMessages());
        assertSame(order, mock.underlyingActor().nextMessage());
    }
    
    @Test
    public void testServingMeals() throws InterruptedException {
        TestActorRef<ActorMock> mock = getActorMock();
        
        PreparedMeal message = new PreparedMeal(mock, new TestRecipe());
        
        actor.tell(message, ActorRef.noSender());
        Thread.sleep(10); // Wait 10 ms to handle message; Should in real be much faster

        assertTrue(mock.underlyingActor().hasMessages());
        assertSame(message, mock.underlyingActor().nextMessage());
    }
}
