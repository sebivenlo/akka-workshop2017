package nl.fontys.sebi.messages;

/**
 * The restaurant opens. Should we employ some staff?
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class OpeningMessage {
    
    private final int neededChefs;
    private final int neededWaiters;

    public OpeningMessage() {
        this(5, 3);
    }
    
    public OpeningMessage(int neededChefs, int neededWaiters) {
        this.neededChefs = neededChefs;
        this.neededWaiters = neededWaiters;
    }
    
    public int neededChefs() {
        return neededChefs;
    }
    
    public int neededWaiters() {
        return neededWaiters;
    }
   
}
