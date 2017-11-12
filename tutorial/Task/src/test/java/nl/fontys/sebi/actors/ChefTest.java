package nl.fontys.sebi.actors;


import akka.actor.ActorRef;
import akka.actor.Props;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.fontys.sebi.ActorTestBase;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.recipes.Recipe;
import nl.fontys.sebi.recipes.TestRecipe;
import org.junit.Test;

/**
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class ChefTest extends ActorTestBase<Chef> {

    @Override
    protected Props getProps() {
        return Props.create(Chef.class);
    }
    
    @Test
    public void testPreparingMeals() {
        List<Class<? extends Recipe>> list = new ArrayList<>();
        list.add(TestRecipe.class);
        
        CompleteOrder order = new CompleteOrder(ActorRef.noSender(), Collections.unmodifiableList(list));
        PreparedMeal expected = new PreparedMeal(ActorRef.noSender(), new TestRecipe());
        
        testMessage(order, expected);
    }
    
}
