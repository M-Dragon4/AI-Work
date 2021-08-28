package projects.randomwalker;

import util.RandomGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This is a Random Walker project to go along with The Coding Train's Nature of Code video series, but adapted for Java.
 * TODO: Prevent the Walker from going off-screen; make some fun pathfinding techniques for the Walker (check random generator)
 */
public class RandomWalker {

    private static boolean running = false;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static Walker prevWalker;
    public static JFrame frame;
    public static Canvas canvas;

    public static void init() {
        frame = new JFrame("Random Walker");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        canvas = new Canvas();
        frame.add(canvas);
        prevWalker = new Walker();
        canvas.addWalker(prevWalker);
    }

    public static void main(String[] args) throws InterruptedException {
        running = true;
        init();
        int i = 0;
        while(running) {
            Walker walker = new Walker(prevWalker.getWalkerX(), prevWalker.getWalkerY());
            walker.walk();
            canvas.addWalker(walker);
            prevWalker = walker;
            canvas.repaint();
            System.out.println(canvas.walkers.get(i).getWalkerX() + " | " + canvas.walkers.get(i).getWalkerY());
            TimeUnit.MILLISECONDS.sleep(100);
            i++;
        }
    }

    static class Walker extends JPanel {
        private int x, y;
        private final int WALKER_WIDTH = 4;
        private final int WALKER_HEIGHT = 4;
        private RandomGenerator randG;

        public Walker() {
            this.x = 320;
            this.y = 240;
            this.randG = new RandomGenerator();
        }

        public Walker(int x, int y) {
            this.x = x;
            this.y = y;
            this.randG = new RandomGenerator();
        }

        public void walk() {
            this.randG.makeValue(3, 0, false);
            System.out.println(this.randG.getValue());
            int choice = (int) this.randG.getValue();
            if(choice == 0) this.x += WALKER_WIDTH;
            else if (choice == 1) this.x -= WALKER_WIDTH;
            else if (choice == 2) this.y += WALKER_HEIGHT;
            else this.y -= WALKER_HEIGHT;
        }

        public int getWalkerX() {
            return this.x;
        }
        public int getWalkerY() {
            return this.y;
        }

        public int getWalkerWidth() {
            return WALKER_WIDTH;
        }

        public int getWalkerHeight() {
            return WALKER_HEIGHT;
        }

        public Walker getWalker() {
            return this;
        }
    }

    static class Canvas extends JPanel {
        private ArrayList<Walker> walkers = new ArrayList<Walker>();

        public Canvas() {
            this.setBackground(Color.WHITE);
        }

        public void addWalker(Walker w) {
            walkers.add(w);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            for(Walker w : walkers) {
                g2d.fillRect(w.getWalkerX(), w.getWalkerY(), w.getWalkerWidth(), w.getWalkerHeight());
            }
        }
    }
}
