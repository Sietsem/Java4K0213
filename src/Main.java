import java.applet.Applet;


public class Main extends Applet implements Runnable {
    private boolean going = true;
    
    public void start() {
        new Thread(this).start();
        System.out.println("STARTED");
    }
    public void init() {
        System.out.println("INITIALIZED");
    }
    public void stop() {
        going = false;
        System.out.println("STOPPED");
    }

    public void run() {
        while(going) {
            System.out.println("TICK");
        }
    }
}
