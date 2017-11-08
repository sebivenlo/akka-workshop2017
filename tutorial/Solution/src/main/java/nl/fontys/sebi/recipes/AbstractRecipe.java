package nl.fontys.sebi.recipes;

/**
 * Abstract base class for shared methods.
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
abstract class AbstractRecipe implements Recipe {
    
    // TODO Q3 Why do we need these two methods?
    
    /**
     * @return Integer representation of this object
     */
    @Override
    public int hashCode() {
        return 31 * (Integer.hashCode(getCookingTime())
                + Integer.hashCode(getEatingTime())
                + Integer.hashCode(getServingTime())
                + Boolean.hashCode(requireAttention()));
    }

    /**
     * @param o
     * @return True if o and this are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Recipe) {
            Recipe rhs = (Recipe) o;
            
            return rhs.hashCode() == this.hashCode();
        }
        
        return false;
    }
    
}
