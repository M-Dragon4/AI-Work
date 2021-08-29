package projects.randomwalker;

import util.RandomGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This is a Random Walker project to go along with The Coding Train's Nature of Code video series, but adapted for Java.
 * TODO: Make some fun pathfinding techniques for the Walker
 */
public class RandomWalker {

    private static boolean running = false;
    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 0;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final Dimension canvasSize = new Dimension(WIDTH, HEIGHT);
    public static Walker prevWalker;
    public static JFrame frame;
    public static Canvas canvas;

    public static void init() {
        frame = new JFrame("Random Walker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        canvas = new Canvas();
        canvas.setPreferredSize(canvasSize);
        frame.add(canvas);
        frame.pack();
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

    static class Walker {
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
            this.randG.makeUniformValue(3, 0, false);
            System.out.println(this.randG.getValue());
            int choice = (int) this.randG.getValue();

            if (isAtTopEdge()) y += WALKER_HEIGHT;
            else if (isAtBottomEdge()) y -= WALKER_HEIGHT;
            else if (isAtRightEdge()) x -= WALKER_WIDTH;
            else if (isAtLeftEdge()) x += WALKER_WIDTH;
            else {
                if(choice == 0) {
                    x += WALKER_WIDTH;
                }
                else if (choice == 1) {
                    x -= WALKER_WIDTH;
                }
                else if (choice == 2) {
                    y += WALKER_HEIGHT;
                }
                else {
                    y -= WALKER_HEIGHT;
                }
            }
        }

        public boolean isAtTopEdge() {
            if (y - WALKER_HEIGHT < ORIGIN_Y) return true;
            else return false;
        }

        public boolean isAtBottomEdge() {
            if (y + WALKER_HEIGHT > HEIGHT - WALKER_HEIGHT) return true;
            else return false;
        }

        public boolean isAtRightEdge() {
            if (x + WALKER_WIDTH > WIDTH - WALKER_WIDTH) return true;
            else return false;
        }

        public boolean isAtLeftEdge() {
            if (x - WALKER_WIDTH < ORIGIN_X) return true;
            else return false;
        }

        public int getWalkerX() {
            return x;
        }
        public int getWalkerY() {
            return y;
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
