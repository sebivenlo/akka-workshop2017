package nl.fontys.sebi.messages;

/**
 * Someone is entering the restaurant. 
 * He clearly needs a free table.
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class EnteringMessage {
    private final String name;

    public EnteringMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EnteringMessage) {
            EnteringMessage obj = (EnteringMessage) o;
            return (name == obj.name || (name != null && name.equals(obj.name)));
        }
        
        return false;
    }
    
}
