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
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.EatingFinished;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.messages.WhatsYourOrder;
import nl.fontys.sebi.recipes.Recipe;

/**
 *
 * @author lukeelten
 */
public class Customer extends AbstractActor {
    private final static Random random = new Random(System.currentTimeMillis());
    
    private final String name;
    private final List<Class<? extends Recipe>> menu;
    
    private final List<Class<? extends Recipe>> order;
    
    public Customer(String name, List<Class<? extends Recipe>> menu) {
        if (name == null || menu == null) throw new NullPointerException();
        if (menu.isEmpty()) throw new RuntimeException();
        
        this.name = name;
        this.menu = menu;
        order = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    
    private CompleteOrder getOrder() {
        int meals = random.nextInt(10) + 1;
        
        for (int i = 0; i < meals; i++) {
            int menuIndex = abs(random.nextInt()) % menu.size();
            
            Class<? extends Recipe> recipe = menu.get(menuIndex);
            order.add(recipe); 
        }
        
        List<Class<? extends Recipe>> tmp = order.stream().collect(Collectors.toList());
        System.out.println("Ordering " + tmp.size() + " meals");
        
        return new CompleteOrder(getSelf(), Collections.unmodifiableList(tmp));
    }
    
    private void receiveMeal(PreparedMeal meal) {
        Class<? extends Recipe> recipe = meal.getMeal().getClass();
        int index = order.indexOf(recipe);
        order.remove(index);
        
        System.out.println("Received Meal: " + recipe.getSimpleName() + " - " + order.size() + " to go");
        
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(meal.getMeal().getEatingTime()));
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        
        if (order.isEmpty()) {
            getContext().getParent().tell(new EatingFinished(), getSelf());
        }
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + order.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Customer) {
            Customer rhs = (Customer) o;
            return (rhs.name.equals(name) && menu.equals(rhs.menu)) && order.equals(rhs.order);
        }
        return false;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WhatsYourOrder.class, wyo -> {
                    getSender().tell(getOrder(), getSelf());
                }).match(PreparedMeal.class, pm -> {
                    receiveMeal(pm);
                }).build();
    }
}
