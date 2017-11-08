package nl.fontys.sebi.messages;

import akka.actor.ActorRef;
import java.util.List;
import nl.fontys.sebi.recipes.Recipe;

/**
 * Contains the complete order of a customer
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class CompleteOrder {
    
    private final List<Class<? extends Recipe>> recipes;
    private final ActorRef customer;

    public CompleteOrder(ActorRef customer, List<Class<? extends Recipe>> recipes) {
        this.customer = customer;
        this.recipes = recipes;
    }

    public List<Class<? extends Recipe>> getRecipes() {
        return recipes;
    }

    public ActorRef getCustomer() {
        return customer;
    }
}
