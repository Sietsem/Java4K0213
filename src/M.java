public class M extends java.applet.Applet implements Runnable {
    private boolean going = true;
    
    public void start() {
        new Thread(this).start();
    }
    public void stop() {
        going = false;
    }

    public void run() {
        long lastTime = System.currentTimeMillis();
        long lastFPS = lastTime;
        int fps = 0;
        long unprocessed = 0;
        final java.awt.image.BufferedImage imageBuffer = new java.awt.image.BufferedImage(800, 600, java.awt.image.BufferedImage.TYPE_INT_RGB);
        final java.awt.Graphics2D g = imageBuffer.createGraphics();
        java.awt.Graphics2D g2 = null;
        while(going) {
            long now = System.currentTimeMillis();
            long delta = (now - lastTime);
            lastTime = now;
            unprocessed += delta * 10000;
            boolean a = false;
            while (unprocessed > 166667) {
                //-----TICK
                a = true;
                unprocessed -= 166667;
            }
            if ((now - lastFPS) > 1000) {
                lastFPS = now;
                System.out.println("FPS: " + fps);
                fps = 0;
            }
            if (a) {
                //-----RENDER
                g.setColor(new java.awt.Color((int) (Math.random() * 0xFFFFFF)));
                g.fillRect(0, 0, 800, 600);
                if (g2 == null) {
                    g2 = (java.awt.Graphics2D)getGraphics();
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
        javax.swing.JFrame frame = new javax.swing.JFrame("4K");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        M m = new M();
        m.setPreferredSize(new java.awt.Dimension(800, 600));
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
