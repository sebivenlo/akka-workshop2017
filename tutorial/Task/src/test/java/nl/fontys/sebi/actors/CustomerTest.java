package nl.fontys.sebi.actors;

import akka.actor.Props;
import nl.fontys.sebi.ActorTestBase;

/**
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class CustomerTest extends ActorTestBase<Customer> {

    @Override
    protected Props getProps() {
        return Props.create(Customer.class);
    }
    
    /**
     * TODO Implement tests
     */
}
