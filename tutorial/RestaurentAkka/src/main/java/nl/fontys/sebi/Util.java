package nl.fontys.sebi;

import java.util.concurrent.TimeUnit;

/**
 * Utility class provides some common methods
 * 
 * @author Tobias Derksen <tobias.derksen@student.fontys.nl>
 */
public class Util {
    
    /**
     * This methods waits for a certain amount of time.
     * You can indicate whether the thread should sleep or keeping busy.
     * For sleeping, Thread.sleep is invoked. Otherwise a simple loop is 
     * running until the time is up.
     * 
     * @param seconds Seconds to wait
     * @param sleep Whether the thread should sleep or keep busy.
     * @throws InterruptedException 
     */
    public static void wait(int seconds, boolean sleep) throws InterruptedException {
        long milliSeconds = TimeUnit.SECONDS.toMillis(seconds);
        
        if (sleep) {
            Thread.sleep(milliSeconds);
        } else {
            long end = System.currentTimeMillis() + milliSeconds;

            while (System.currentTimeMillis() < end) {
                System.nanoTime();
                System.currentTimeMillis();
            }
        }
    }
}
