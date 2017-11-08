package nl.fontys.sebi;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import java.io.IOException;
import nl.fontys.sebi.actors.Restaurant;
import nl.fontys.sebi.messages.EnteringMessage;
import nl.fontys.sebi.messages.OpeningMessage;
import scala.concurrent.Future;

/**
 * Main class, simply runs the app
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Main {
       
    public static void main(String[] args) throws IOException, InterruptedException {
       
        ActorSystem system = ActorSystem.create();
        try {
            ActorRef restaurant = system.actorOf(Props.create(Restaurant.class, 10), "restaurant");
            
            restaurant.tell(new OpeningMessage(5, 3), ActorRef.noSender());
            restaurant.tell(new EnteringMessage("Heinz"), restaurant);
            restaurant.tell(new EnteringMessage("Hans"), restaurant);
            restaurant.tell(new EnteringMessage("Klaus"), restaurant);
            restaurant.tell(new EnteringMessage("Horst"), restaurant);
            Future<Terminated> termination = system.whenTerminated();
            
            while (!termination.isCompleted()) {
                // TODO Q1 Will Thread.sleep at this position influence performance? 
                Thread.sleep(100);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
