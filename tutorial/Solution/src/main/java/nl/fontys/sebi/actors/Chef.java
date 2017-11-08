package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    
    private Recipe prepareMeal(Class<? extends Recipe> recipe) {
        try {
            System.out.println("Chef prepares: " + recipe.getSimpleName());
            Recipe meal = recipe.newInstance();
            
            if (meal.requireAttention()) {
                long end = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(meal.getCookingTime());
                
                while (System.currentTimeMillis() < end) {
                    System.nanoTime();
                    System.currentTimeMillis();
                }
            } else {
                Thread.sleep(meal.getCookingTime());
            }
            
            return meal;
        } catch (InstantiationException | IllegalAccessException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CompleteOrder.class, order -> {
                    List<Class<? extends Recipe>> recipes = order.getRecipes();
                    
                    for (Class<? extends Recipe> recipe : recipes) {
                        Recipe meal = prepareMeal(recipe);
                        PreparedMeal prepared = new PreparedMeal(order.getCustomer(), meal);
                        getSender().tell(prepared, getSelf());
                    }
                }).build();
    }
    
}
