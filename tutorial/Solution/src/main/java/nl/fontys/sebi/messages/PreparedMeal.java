package nl.fontys.sebi.messages;

import akka.actor.ActorRef;
import nl.fontys.sebi.recipes.Recipe;

/**
 * A meal which is served to the customer by a waiter.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class PreparedMeal {
    
    private final Recipe meal;
    private final ActorRef customer;

    public PreparedMeal(ActorRef customer, Recipe meal) {
        this.customer = customer;
        this.meal = meal;
    }

    public Recipe getMeal() {
        return meal;
    }

    public ActorRef getCustomer() {
        return customer;
    }
}
