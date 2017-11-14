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
import nl.fontys.sebi.messages.EnteringMessage;
import nl.fontys.sebi.messages.OpeningMessage;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.recipes.BakedPotatos;
import nl.fontys.sebi.recipes.ChilliCheeseKebab;
import nl.fontys.sebi.recipes.CurryChicken;
import nl.fontys.sebi.recipes.Hamburger;
import nl.fontys.sebi.recipes.PastaAlaMax;
import nl.fontys.sebi.recipes.Recipe;
import nl.fontys.sebi.recipes.Steak;

/**
 * Restaurant Actor.
 * Organized staff and tables at the restaurant.
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
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
        
        // What are we serving at all?
        List<Class<? extends Recipe>> menu = new ArrayList<>(20);
        menu.add(BakedPotatos.class);
        menu.add(PastaAlaMax.class);
        menu.add(Steak.class);
        menu.add(CurryChicken.class);
        menu.add(Hamburger.class);
        menu.add(ChilliCheeseKebab.class);
        
        // TODO Q4 Why an unmodifiable collection?
        this.menu = Collections.unmodifiableList(menu);
    }
    
    /**
     * The restaurant is opening.
     * We should employ some staff.
     * 
     * @param msg 
     */
    void open(OpeningMessage msg) {
        // TODO create actor for waiter
        getContext().watch(waiter);
        System.out.println("Created actor: " + waiter.path().toString());
        
        // TODO create actor for chef
        getContext().watch(chef);
        System.out.println("Created actor: " + chef.path().toString());
    }
    
    /**
     * Closes the restaurant.
     * Kick out all customers and fire the staff.
     */
    void close() {
        System.out.println("Restaurant is closing");
        if (!customers.isEmpty()) {
            customers.entrySet().stream().forEach(entry -> {
                System.out.println("Kicking out customer at table " + entry.getKey());
                entry.getValue().tell(PoisonPill.getInstance(), getSelf());
            });
        }
        
        // TODO fire the staff
    }
    
    /**
     * A customer has finished eating.
     * Kick him out so the table is free again.
     * 
     * @param customer 
     */
    void eatingFinished(ActorRef customer) {
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
        
        // TODO the restaurant should close when there no customers anymore
    }
    
    @Override
    public Receive createReceive() {
        // TODO Restaurant should also handle the following messages:
        // EatingFinished
        // OpeningMessage
        // EnteringMessage
        
        return receiveBuilder()
                .match(ClosingMessage.class, pp -> { close(); })
                .match(Terminated.class, t -> {
                    System.out.println(t.actor().path() + " has stopped working");
                    
                    if (t.actor().equals(waiter)) {
                        waiter = null;
                    } else if(t.actor().equals(chef)) {
                        chef = null;
                    }
                    
                    if (waiter == null && chef == null) {
                        getContext().getSystem().terminate();
                    }
                })
                .match(PreparedMeal.class, pm -> {
                    waiter.tell(pm, getSelf());
                })
                .match(CompleteOrder.class, fo -> {
                    chef.tell(fo, getSelf());
                }).build();
    }
     
    /**
     * Someone entered the restaurant.
     * We should provide him a free table and send a waiter
     * 
     * @param name 
     * @throws RuntimeException If there is no free table left
     */
    void enterRestaurant(final String name) {
        if (name == null) throw new NullPointerException();
        if (name.isEmpty()) throw new IllegalArgumentException();
        
        if (freeTables.peek() != null) {
            Props props = Props.create(Customer.class, name, menu);
            Integer table = freeTables.poll();
            
            ActorRef customer = getContext().actorOf(props);
            customers.put(table, customer);
            
            waiter.tell(new CustomerEntered(customer), getSelf());
            System.out.println("Customer entered: " + name + " on table " + table);
            return;
        }
        
        throw new RuntimeException("Restaurant is full.");
    }
}
