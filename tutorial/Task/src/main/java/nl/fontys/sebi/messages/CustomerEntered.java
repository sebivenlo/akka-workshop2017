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
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomerEntered) {
            CustomerEntered obj = (CustomerEntered) o;

            return (customer == obj.customer || (customer != null && customer.equals(obj.customer)));
        }

        return false;
    }
}
