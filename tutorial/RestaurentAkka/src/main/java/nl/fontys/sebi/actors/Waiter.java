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
package nl.fontys.sebi.actors;

import akka.actor.AbstractActor;
import nl.fontys.sebi.messages.CompleteOrder;
import nl.fontys.sebi.messages.CustomerEntered;
import nl.fontys.sebi.messages.PreparedMeal;
import nl.fontys.sebi.messages.WhatsYourOrder;


/**
 *
 * @author lukeelten
 */
public class Waiter extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CustomerEntered.class, ce -> {
                    ce.getCustomer().tell(new WhatsYourOrder(), getSelf());
                }).match(CompleteOrder.class, co -> {
                    getContext().getParent().tell(co, getSelf());
                }).match(PreparedMeal.class, pm -> {
                    pm.getCustomer().tell(pm, getSelf());
                }).build();
    }
}
