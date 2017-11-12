package nl.fontys.sebi.messages;

/**
 * Ask the customer what he wants to order.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class WhatsYourOrder {

    @Override
    public boolean equals(Object o) {
        // Class is stateless, only type checking
        return (o != null && o.getClass().equals(this.getClass()));
    }
}
