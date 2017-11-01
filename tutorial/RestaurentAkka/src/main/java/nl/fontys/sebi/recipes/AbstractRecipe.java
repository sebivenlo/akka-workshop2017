/*
 * Copyright (C) 2017 lukeelten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package nl.fontys.sebi.recipes;

/**
 *
 * @author lukeelten
 */
abstract class AbstractRecipe implements Recipe {
    
    @Override
    public int hashCode() {
        return Integer.hashCode(getCookingTime())
                + Integer.hashCode(getEatingTime())
                + Boolean.hashCode(this.requireAttention());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Recipe) {
            Recipe rhs = (Recipe) o;
            
            return rhs.hashCode() == this.hashCode();
        }
        
        return false;
    }
    
}
