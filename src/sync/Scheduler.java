/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sync;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anik
 */
public class Scheduler implements Runnable {
    private int interval;
    private TaskExecuter executer;
    private boolean isRunning = true;

    public Scheduler(TaskExecuter executer , int interval) {
        this.executer = executer;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            executer.execute();
        }
    }
    
    public void stop(){
        isRunning = false;
        stop();
    }
}
