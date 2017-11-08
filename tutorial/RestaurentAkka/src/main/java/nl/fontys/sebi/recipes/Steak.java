package nl.fontys.sebi.recipes;

/**
 * Quite hard to do properly. 
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Steak extends AbstractRecipe {

    @Override
    public int getCookingTime() {
        return 5;
    }

    /**
     * Needs a lot of attention when cooking.
     * 
     * @return 
     */
    @Override
    public boolean requireAttention() {
        return true;
    }
}
