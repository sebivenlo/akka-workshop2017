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
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import nl.fontys.sebi.messages.CustomerEntered;
import nl.fontys.sebi.messages.EatingFinished;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.OpeningMessage;
import nl.fontys.sebi.messages.PoisonPill;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.recipes.BakedPotatos;
import nl.fontys.sebi.recipes.PastaAlaMax;
import nl.fontys.sebi.recipes.Recipe;
import nl.fontys.sebi.recipes.Steak;
import org.jboss.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author lukeelten
 */
public class Restaurant extends AbstractActor {
    
    private Map<Integer, ActorRef> customers;
    private Queue<Integer> freeTables;
    private final List<Class<? extends Recipe>> menu;
    
    private ActorRef waiter;
    private ActorRef chef;
    
    public Restaurant() {
        this(20);
    }
    
    public Restaurant(int tables) {
        customers = new HashMap<>(tables);
        freeTables = new LinkedList<>();
        
        for (int i = 1; i <= tables; i++) {
            freeTables.add(i);
        }
        
        List<Class<? extends Recipe>> menu = new ArrayList<>();
        menu.add(BakedPotatos.class);
        menu.add(PastaAlaMax.class);
        menu.add(Steak.class);
        
        this.menu = Collections.unmodifiableList(menu);
    }
    
    public void open() {
        waiter = getContext().actorOf(Props.create(Waiter.class), "waiter");
        System.out.println("Created actor: " + waiter.path().toString());
        
        //chef = getContext().actorOf(Props.create(Chef.class), "chef");
        chef = getContext().actorOf(new RoundRobinPool(5).props(Props.create(Chef.class)), "chef");
        System.out.println("Created actor: " + chef.path().toString());
        
        enterRestaurant("Klaus");
        enterRestaurant("Horst");
    }
    
    public void close() {
        waiter.tell(new PoisonPill(), getSelf());
        chef.tell(new PoisonPill(), getSelf());
    }

    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OpeningMessage.class, om -> { open(); })
                .match(PoisonPill.class, pp -> { close(); })
                .match(PreparedMeal.class, pm -> {
                    waiter.tell(pm, getSelf());
                })
                .match(CompleteOrder.class, fo -> {
                    chef.tell(fo, getSelf());
                }).match(EatingFinished.class, ef -> {
                    ActorRef customer = getSender();
                    Integer table = -1;
                    
                    for (Entry<Integer, ActorRef> entry : customers.entrySet()) {
                        if (entry.getValue().equals(customer)) {
                            table = entry.getKey();
                        }
                    }
                    
                    if (table > 0) {
                        customers.remove(table);
                        freeTables.add(table);
                    }
                    
                    System.out.println("Table " + table + " finished.");
                }).build();
    }
    
    
    
    
    public void enterRestaurant(final String name) {
        if (name == null) throw new NullPointerException();
        if (name.isEmpty()) throw new IllegalArgumentException();
        
        if (freeTables.peek() != null) {
            Props props = Props.create(Customer.class, name, menu);
            Integer table = freeTables.poll();
            
            ActorRef customer = getContext().actorOf(props, ("customer" + table));
            customers.put(table, customer);
            
            waiter.tell(new CustomerEntered(customer), getSelf());
            return;
        }
        
        throw new RuntimeException("Restaurant is full.");
    }
}
