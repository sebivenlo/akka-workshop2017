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
package nl.fontys.sebi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import nl.fontys.sebi.recipes.BakedPotatos;
import nl.fontys.sebi.recipes.PastaAlaMax;
import nl.fontys.sebi.recipes.Recipe;
import nl.fontys.sebi.recipes.Steak;

/**
 *
 * @author lukeelten
 */
public class Restaurant {
    
    private List<Customer> customers;
    private Queue<Integer> freeTables;
    
    private final List<Class<? extends Recipe>> menu;
    
    public Restaurant() {
        this(20);
    }
    
    public Restaurant(int tables) {
        customers = new ArrayList<>(tables);
        freeTables = new LinkedList<>();
        
        
        for (int i = 1; i <= tables; i++) {
            freeTables.add(i);
        }
        List<Class<? extends Recipe>> menu = new ArrayList<>();
        menu.add(BakedPotatos.class);
        menu.add(PastaAlaMax.class);
        menu.add(Steak.class);
        
        this.menu = Collections.unmodifiableList(menu);
    }
    
    public boolean open() {
        return true;
    }
    
    public void close() {
        
    }
    
    
    public Customer enterRestaurant(final String name) {
        if (name == null) throw new NullPointerException();
        if (name.isEmpty()) throw new IllegalArgumentException();
        
        if (freeTables.peek() != null) {
            Customer customer = new Customer(name, freeTables.poll(), menu);
            customers.add(customer);
            return customer;
        }
        
        throw new RuntimeException("Restaurant is full.");
    }
    
    public void leaveRestaurant(final Customer customer) {
        if (!customers.contains(customer)) throw new IllegalArgumentException();
        
        customers.remove(customer);
        freeTables.add(customer.getTable());
    }
}
