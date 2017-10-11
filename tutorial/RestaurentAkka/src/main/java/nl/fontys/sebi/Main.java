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


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.io.IOException;
import nl.fontys.sebi.actors.Restaurant;
import nl.fontys.sebi.messages.OpeningMessage;
import nl.fontys.sebi.messages.PoisonPill;



/**
 *
 * @author lukeelten
 */
public class Main {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        ActorSystem system = ActorSystem.create();
        
        try {
            ActorRef restaurant = system.actorOf(Props.create(Restaurant.class), "restaurant");
            
            restaurant.tell(new OpeningMessage(), restaurant);
            
            System.out.println("Press ENTER to exit the system");
            System.in.read();
            
            restaurant.tell(new PoisonPill(), restaurant);
            Thread.sleep(1);
        } finally {
            system.terminate();
        }
    }
    
}
