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
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.routing.RoundRobinPool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import nl.fontys.sebi.messages.ClosingMessage;
import nl.fontys.sebi.messages.CustomerEntered;
import nl.fontys.sebi.messages.EatingFinished;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.OpeningMessage;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.recipes.BakedPotatos;
import nl.fontys.sebi.recipes.PastaAlaMax;
import nl.fontys.sebi.recipes.Recipe;
import nl.fontys.sebi.recipes.Steak;

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
    
    private void open() {
        waiter = getContext().actorOf(Props.create(Waiter.class), "waiter");
        getContext().watch(waiter);
        System.out.println("Created actor: " + waiter.path().toString());
        
        //chef = getContext().actorOf(Props.create(Chef.class), "chef");
        chef = getContext().actorOf(new RoundRobinPool(5).props(Props.create(Chef.class)), "chef");
        getContext().watch(chef);
        System.out.println("Created actor: " + chef.path().toString());
        
        enterRestaurant("Klaus");
        enterRestaurant("Horst");
    }
    
    private void close() {
        System.out.println("Restaurant is closing");
        if (!customers.isEmpty()) {
            customers.entrySet().stream().forEach(entry -> {
                System.out.println("Kicking out customer at table " + entry.getKey());
                entry.getValue().tell(PoisonPill.getInstance(), getSelf());
            });
        }
        
        waiter.tell(PoisonPill.getInstance(), getSelf());
        chef.tell(PoisonPill.getInstance(), getSelf());
    }
    
    private void eatingFinished(ActorRef customer) {
        Integer table = -1;
        
        for (Entry<Integer, ActorRef> entry : customers.entrySet()) {
            if (entry.getValue().equals(customer)) {
                table = entry.getKey();
                break;
            }
        }
        
        if (table > 0) {
            customers.remove(table);
            freeTables.add(table);
        }
        
        customer.tell(PoisonPill.getInstance(), getSelf());
        System.out.println("Table " + table + " finished.");
        
        if (customers.isEmpty()) {
            getSelf().tell(new ClosingMessage(), ActorRef.noSender());
        }
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OpeningMessage.class, om -> { open(); })
                .match(ClosingMessage.class, pp -> { close(); })
                .match(Terminated.class, t -> t.actor().equals(chef), t -> {
                    chef = null; 
                    System.out.println("Chef had stopped working.");
                    
                    if (waiter == null) {
                        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                    }
                })
                .match(Terminated.class, t -> t.actor().equals(waiter), t -> {
                    waiter = null; 
                    System.out.println("Waiter has stopped working");
                    
                    if (chef == null) {
                        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
                    }
                })
                .match(PreparedMeal.class, pm -> {
                    waiter.tell(pm, getSelf());
                })
                .match(CompleteOrder.class, fo -> {
                    chef.tell(fo, getSelf());
                }).match(EatingFinished.class, ef -> {
                    ActorRef customer = getSender();
                    eatingFinished(customer);
                }).build();
    }
        
    private void enterRestaurant(final String name) {
        if (name == null) throw new NullPointerException();
        if (name.isEmpty()) throw new IllegalArgumentException();
        
        if (freeTables.peek() != null) {
            Props props = Props.create(Customer.class, name, menu);
            Integer table = freeTables.poll();
            
            ActorRef customer = getContext().actorOf(props, ("customer" + table));
            customers.put(table, customer);
            
            waiter.tell(new CustomerEntered(customer), getSelf());
            System.out.println("Customer entered: " + name + " on table " + table);
            return;
        }
        
        throw new RuntimeException("Restaurant is full.");
    }
}
