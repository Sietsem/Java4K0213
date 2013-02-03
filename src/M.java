import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class M extends Applet implements Runnable {
    private boolean g = true;
    private boolean[] k = new boolean[256];
    
    public void start() {
        new Thread(this).start();
    }
    public void stop() {
        g = false;
    }

    public void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED)
            k[e.getKeyCode()] = true;
        else
            k[e.getKeyCode()] = false;
    }
    
    public void run() {
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        long lastTime = System.currentTimeMillis();
        long lastFPS = lastTime;
        int fps = 0;
        long up = 0;
        final BufferedImage imageBuffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = imageBuffer.createGraphics();
        Graphics2D g2 = null;
        float po[][] = new float[1][3];
        while(this.g) {
            long now = System.currentTimeMillis();
            long delta = (now - lastTime);
            lastTime = now;
            up += delta * 10000;
            boolean a = false;
            while (up > 166667) {
                //-----TICK
                if (k[KeyEvent.VK_LEFT])
                    po[0][0] -= 1;
                if (k[KeyEvent.VK_RIGHT])
                    po[0][0] += 1;
                if (k[KeyEvent.VK_UP])
                    po[0][1] -= 1;
                if (k[KeyEvent.VK_DOWN])
                    po[0][1] += 1;
                a = true;
                up -= 166667;
            }
            if ((now - lastFPS) > 1000) {
                lastFPS = now;
                System.out.println("FPS: " + fps);
                fps = 0;
            }
            if (a) {
                //-----RENDER
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 800, 600);
                for (int i = 0; i < po.length; i++) {
                    g.setColor(Color.RED);
                    if (i == 0) {
                        g.setColor(Color.GREEN);
                    }
                    g.fillRect((int)po[i][0], (int)po[i][1], 32, 32);
                }
                if (g2 == null) {
                    g2 = (Graphics2D)getGraphics();
                }
                g2.drawImage(imageBuffer, 0, 0, null);
                fps++;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) throws Throwable {
        JFrame frame = new JFrame("4K");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        M m = new M();
        m.setPreferredSize(new Dimension(800, 600));
        frame.setContentPane(m);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Thread.sleep(250);
        m.start();
        m.requestFocus();
    }
}
