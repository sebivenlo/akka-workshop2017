/*
 * Copyright (C) 2017 lukeelten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import java.util.concurrent.TimeUnit;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.recipes.Recipe;

/**
 *
 * @author lukeelten
 */
public class Chef extends AbstractActor {
    
    public Chef() {
        System.out.println("Creating Chef");
    }
    
    private Recipe prepareMeal(Class<? extends Recipe> recipe) {
        try {
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
                    for (Class<? extends Recipe> recipe : order.getRecipes()) {
                        Recipe meal = prepareMeal(recipe);
                        getSender().tell(new PreparedMeal(order.getCustomer(), meal), getSelf());
                    }
                    
                }).build();
    }
    
}
