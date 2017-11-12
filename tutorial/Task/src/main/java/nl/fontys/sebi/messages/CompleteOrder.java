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
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CompleteOrder) {
            CompleteOrder obj = (CompleteOrder) o;

            return (recipes == obj.recipes || (recipes != null && recipes.equals(obj.recipes)))
                    && (customer == obj.customer || (customer != null && customer.equals(obj.customer)));
        }

        return false;
    }
}
