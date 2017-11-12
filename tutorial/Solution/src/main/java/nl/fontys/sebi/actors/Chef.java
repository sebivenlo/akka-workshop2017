package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import java.util.List;
import nl.fontys.sebi.Util;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.recipes.Recipe;

/**
 * Someone has to prepare the meals.
 * Otherwise the restaurant would be closed very soon.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Chef extends AbstractActor {
    
    Recipe prepareMeal(Class<? extends Recipe> recipe) {
        try {
            System.out.println("Chef prepares: " + recipe.getSimpleName());
            Recipe meal = recipe.newInstance();

            Util.wait(meal.getCookingTime(), !meal.requireAttention());

            return meal;
        } catch (InstantiationException | IllegalAccessException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    void receiveCompleteOrder(CompleteOrder msg) {
        List<Class<? extends Recipe>> recipes = msg.getRecipes();

        for (Class<? extends Recipe> recipe : recipes) {
            Recipe meal = prepareMeal(recipe);
            PreparedMeal prepared = new PreparedMeal(msg.getCustomer(), meal);
            getSender().tell(prepared, getSelf());
        }
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CompleteOrder.class, order -> {
                    receiveCompleteOrder(order);
                }).build();
    }
    
}
