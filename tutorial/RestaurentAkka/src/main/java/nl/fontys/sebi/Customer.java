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
package nl.fontys.sebi;

import akka.actor.AbstractActor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nl.fontys.sebi.messages.MealPrepared;
import nl.fontys.sebi.messages.NoMoreOrders;
import nl.fontys.sebi.messages.Order;
import nl.fontys.sebi.messages.WhatsYourOrder;
import nl.fontys.sebi.recipes.Recipe;

/**
 *
 * @author lukeelten
 */
public class Customer extends AbstractActor {
    private final static Random random = new Random(System.currentTimeMillis());
    
    private final String name;
    private final int table;
    
    private int orders;
    private final List<Class<? extends Recipe>> menu;
    
    private final List<Order> ordered;
    
    public Customer(String name, int table, List<Class<? extends Recipe>> menu) {
        if (name == null || menu == null) throw new NullPointerException();
        if (table <= 0) throw new IllegalArgumentException();
        if (menu.isEmpty()) throw new RuntimeException();
        
        this.name = name;
        this.table = table;
        this.menu = menu;
        
        orders = random.nextInt(4) + 1;
        ordered = new ArrayList<>(orders);
    }

    public String getName() {
        return name;
    }

    public int getTable() {
        return table;
    }
    
    public Order getNextOrder() {
        if (!hasNextOrder()) return null;
        
        Class<? extends Recipe> recipe = menu.get((random.nextInt() % menu.size()));
        
        Order order = new Order(recipe, random.nextInt(4));
        ordered.add(order);
        return order;
    }
    
    public boolean hasNextOrder() {
        return orders > ordered.size();
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + 17 * table;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Customer) {
            Customer rhs = (Customer) o;
            return (rhs.name.equals(name) && table == rhs.table && menu.equals(rhs.menu));
        }
        
        return false;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(WhatsYourOrder.class, wyo -> {
            System.out.println("Table " + table + ": What´s your order?");
            if (hasNextOrder()) {
                Order order = getNextOrder();
                System.out.println("Table " + table + ": Ordering " + order.getRecipe().getSimpleName());
                getSender().tell(order, getSelf());
            } else {
                System.out.println("Table " + table + ": No More Orders");
                getSender().tell(new NoMoreOrders(), getSelf());
            }
        }).match(MealPrepared.class, msg -> {
            
        }).build();
    }
}
