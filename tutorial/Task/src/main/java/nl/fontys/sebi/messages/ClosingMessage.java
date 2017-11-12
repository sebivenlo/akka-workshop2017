package nl.fontys.sebi.messages;

/**
 * Indicates that the restaurant should close
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class ClosingMessage {
    
    @Override
    public boolean equals(Object o) {
        // Class is stateless, only type checking
        return (o != null && o.getClass().equals(this.getClass()));
    }
}
