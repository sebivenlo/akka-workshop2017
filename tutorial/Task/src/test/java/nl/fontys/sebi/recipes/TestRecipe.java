package nl.fontys.sebi.recipes;

/**
 * Test Recipe with no waiting time.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class TestRecipe extends AbstractRecipe {

    @Override
    public int getCookingTime() {
        return 0;
    }

    @Override
    public int getServingTime() {
        return 0;
    }

    @Override
    public int getEatingTime() {
        return 0;
    }

    @Override
    public boolean requireAttention() {
        return true;
    }
}
