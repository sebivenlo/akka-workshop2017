package nl.fontys.sebi.recipes;

/**
 * How is a certain meals cooked?
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public interface Recipe {
    
    int getCookingTime();
    
    default int getServingTime() {
        return 2;
    }
    
    default int getEatingTime() {
        return 1;
    }
    
    default boolean requireAttention() {
        return false;
    }
}
