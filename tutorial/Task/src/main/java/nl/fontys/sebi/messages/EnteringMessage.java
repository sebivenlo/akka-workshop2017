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
}
