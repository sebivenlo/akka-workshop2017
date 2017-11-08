package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import nl.fontys.sebi.Util;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.messages.WhatsYourOrder;
import nl.fontys.sebi.recipes.Recipe;

/**
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Customer extends AbstractActor {
    
    private final Random random;
    private final String name;
    private final List<Class<? extends Recipe>> menu;
    
    private final List<Class<? extends Recipe>> order;
    
    public Customer(String name, List<Class<? extends Recipe>> menu) {
        if (name == null || menu == null) throw new NullPointerException();
        if (menu.isEmpty()) throw new RuntimeException();
        
        random = new Random(System.currentTimeMillis());
        
        this.name = name;
        this.menu = menu;
        order = new ArrayList<>(6);
    }

    public String getName() {
        return name;
    }
    
    private CompleteOrder getOrder() {
        int meals = random.nextInt(3) + 1;
        
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
        
        System.out.println(name + " received Meal: " + recipe.getSimpleName() + " - still " + (order.size() - 1) + " to go");
        
        try {
            Util.wait(meal.getMeal().getEatingTime(), true);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        
        int index = order.indexOf(recipe);
        order.remove(index);
        
        // TODO if all orders has been served, tell restaurant we are finished (EatingFinished)
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
