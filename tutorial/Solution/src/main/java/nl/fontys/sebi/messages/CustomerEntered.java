package nl.fontys.sebi.messages;

import akka.actor.ActorRef;

/**
 * A new customer entered the restaurant.
 * Someone should collect his order.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class CustomerEntered {
    private final ActorRef customer;

    public CustomerEntered(ActorRef customer) {
        this.customer = customer;
    }

    public ActorRef getCustomer() {
        return customer;
    }
}
