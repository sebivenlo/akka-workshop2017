package nl.fontys.sebi;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Terminated;
import java.io.IOException;
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
            // TODO Create restaurant actor 
            ActorRef restaurant = null;
            
            restaurant.tell(new OpeningMessage(5, 3), ActorRef.noSender());
            
            // TODO there should be some customers or not?
            
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
