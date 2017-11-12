package nl.fontys.sebi.messages;

/**
 * A customer finished eating after all meals has been served.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class EatingFinished {
    
    @Override
    public boolean equals(Object o) {
        // Class is stateless, only type checking
        return (o != null && o.getClass().equals(this.getClass()));
    }
}
