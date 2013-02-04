import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

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
        k[e.getKeyCode()] = false;
        if (e.getID() == KeyEvent.KEY_PRESSED)
            k[e.getKeyCode()] = true;
    }
    
    public void run() {
        setSize(800, 600);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        long lastTime = System.currentTimeMillis();
        long lastFPS = lastTime;
        int fps = 0;
        long up = 0;
        Graphics2D g2 = null;
        int[][] po = new int[1][3];
        po[0][2] = 4; //Player sprite is 4
        byte[][] map = new byte[64][64];
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                map[x][y] = 0;
                if (Math.random() > 0.9)
                    map[x][y] = 1;
            }
        }
        final BufferedImage imageBuffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        //-----CREATE SPRITES
        final BufferedImage[] spr = new BufferedImage[5];
        final Graphics2D[] gs = new Graphics2D[5];
        for (int i = 0; i < 5; i++) {
            spr[i] = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
            gs[i] = spr[i].createGraphics();
        }
        gs[4].setColor(new Color(0xFF0000));
        gs[4].fillRect(0, 0, 32, 32);
        gs[4].setColor(new Color(0xCC0000));
        gs[4].fillRect(2, 2, 28, 28);
        
        gs[0].setColor(new Color(0x0000FF));
        gs[0].fillRect(0, 0, 32, 32);
        gs[1].setColor(new Color(0x00FFFF));
        gs[1].fillRect(0, 0, 32, 32);
        gs[2].setColor(new Color(0xFFFF00));
        gs[2].fillRect(0, 0, 32, 32);
        while(this.g) {
            long now = System.currentTimeMillis();
            up += (now - lastTime) * 10000;
            lastTime = now;
            boolean a = true;
            //while (up > 166667) {
            while (up > 166667) {
                //-----TICK
                if (k[KeyEvent.VK_LEFT] && map[(po[0][0]>>5)-1][po[0][1]>>5] == 0)  po[0][0] -= 32;
                if (k[KeyEvent.VK_RIGHT] && map[(po[0][0]>>5)+1][po[0][1]>>5] == 0) po[0][0] += 32;
                if (k[KeyEvent.VK_UP] && map[po[0][0]>>5][(po[0][1]>>5)-1] == 0)    po[0][1] -= 32;
                if (k[KeyEvent.VK_DOWN] && map[po[0][0]>>5][(po[0][1]>>5)+1] == 0)  po[0][1] += 32;
                a = true;
                up -= 166667;
            }
            if ((now - lastFPS) > 1000) {
                lastFPS = now;
                System.out.println("FPS: " + fps);
                fps = 0;
            }
            if (a) {
                final Graphics2D g = imageBuffer.createGraphics();
                //-----RENDER
                //-----RENDER BACKGROUND
                g.translate(400-po[0][0], 300-po[0][1]);
                //-----RENDER WORLD
                for (int x = (po[0][0] - 400)>>5; x <= (po[0][0] + 400)>>5; x++) {
                    for (int y = (po[0][1] - 300)>>5; y <= (po[0][1] + 300)>>5; y++) {
                        if ((x & 0xFFFFFFC0) != 0 || (y & 0xFFFFFFC0) != 0) //if (x < 0 || x >= 64 || y < 0 || y >= 64) 
                            g.drawImage(spr[2], x<<5, y<<5, null);
                        else
                            g.drawImage(spr[map[x][y]], x<<5, y<<5, null);
                    }
                }
                //-----RENDER OBJECTS
                for (int i = 0; i < po.length; i++) {
                    g.drawImage(spr[po[i][2]], po[i][0], po[i][1], null);
                }
                if (g2 == null) {
                    g2 = (Graphics2D)getGraphics();
                } else {
                    g2.drawImage(imageBuffer, 0, 0, null);
                    fps++;
                    g.dispose();
                }
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*public static void main(String[] args) throws Throwable {
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
    }*/
}
