/*
 * Copyright (C) 2017 Tobias Derksen <tobias.derksen@student.fontys.nl>
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
package nl.fontys.sebi.messages;

import akka.actor.ActorRef;
import java.util.List;
import nl.fontys.sebi.recipes.Recipe;

/**
 *
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class CompleteOrder {
    
    private final List<Class<? extends Recipe>> recipes;
    private final ActorRef customer;

    public CompleteOrder(ActorRef customer, List<Class<? extends Recipe>> recipes) {
        this.customer = customer;
        this.recipes = recipes;
    }

    public List<Class<? extends Recipe>> getRecipes() {
        return recipes;
    }

    public ActorRef getCustomer() {
        return customer;
    }
}
