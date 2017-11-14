package nl.fontys.sebi.actors;

import akka.actor.Props;
import nl.fontys.sebi.ActorTestBase;

/**
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class RestaurantTest extends ActorTestBase<Restaurant> {
    
    @Override
    protected Props getProps() {
        return Props.create(Restaurant.class);
    }

    /**
     * TODO Implement tests
     */
}
