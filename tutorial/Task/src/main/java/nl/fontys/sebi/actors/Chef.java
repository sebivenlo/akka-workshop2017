package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import nl.fontys.sebi.Util;
import nl.fontys.sebi.recipes.Recipe;

/**
 * Someone has to prepare the meals.
 * Otherwise the restaurant would be closed very soon.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Chef extends AbstractActor {
    
    private Recipe prepareMeal(Class<? extends Recipe> recipe) {
        try {
            System.out.println("Chef prepares: " + recipe.getSimpleName());
            Recipe meal = recipe.newInstance();
            
            Util.wait(meal.getCookingTime(), !meal.requireAttention());
            
            return meal;
        } catch (InstantiationException | IllegalAccessException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public Receive createReceive() {
        // TODO chef should handle the orders (CompleteOrder) and send the meal back to sender (getSender)
        
        return receiveBuilder().build();
    }
    
}
