package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import nl.fontys.sebi.Util;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.CustomerEntered;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.messages.WhatsYourOrder;


/**
 * Waiter asks customers for their order and brings the prepared meals afterwards.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Waiter extends AbstractActor {

    private final ActorSelection restaurant;
    
    public Waiter() {
        // TODO Q4 What is it for? Why do we need that?
        restaurant = getContext().actorSelection("akka://default/user/restaurant");
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CustomerEntered.class, ce -> {
                    ce.getCustomer().tell(new WhatsYourOrder(), getSelf());
                }).match(CompleteOrder.class, co -> {
                    restaurant.tell(co, getSelf());
                }).match(PreparedMeal.class, pm -> {
                    Util.wait(pm.getMeal().getServingTime(), false);
                    pm.getCustomer().tell(pm, getSelf());
                }).build();
    }
}
