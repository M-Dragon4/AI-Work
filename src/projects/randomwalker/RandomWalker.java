package projects.randomwalker;

import javax.swing.*;
import java.awt.*;

/**
 * This is a Random Walker project to go along with The Coding Train's Nature of Code video series, but adapted for Java.
 * TODO: Make some fun pathfinding techniques for the Walker
 */
public class RandomWalker implements Runnable //, ActionListener
{

    private boolean running = false;
    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 0;
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;
    public static final int BUTTON_WIDTH = 640;
    public static final int BUTTON_HEIGHT = 36;
    public static final Dimension CANVAS_SIZE = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
    public static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    private final int WAIT = 100; //In milliseconds
    private Thread buttonThread = null;
    public Walker prevWalker;
    public JFrame frame;
    public JPanel container;
    public Canvas canvas;
    public JPanel buttonPanel;
    public JButton walkButton;
    public JButton stopButton;

    public void init() {
        frame = new JFrame("Random Walker");
        container = new JPanel();

        walkButton = new JButton("Walk");
        stopButton = new JButton("Stop");

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(BUTTON_SIZE);
        buttonPanel.add(walkButton);
        buttonPanel.add(stopButton);
        canvas = new Canvas();
        canvas.setPreferredSize(CANVAS_SIZE);

        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.add(buttonPanel);
        container.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(container);
        frame.pack();
        frame.setVisible(true);
        prevWalker = new Walker();
        canvas.addWalker(prevWalker);
    }

    public void run() {
        init();

        walkButton.addActionListener(event -> {
            buttonThread = new Thread(() -> {
                running = true;
                int i = 0;
                try {
                    while (running) {
                        Walker walker = new Walker(prevWalker.getWalkerX(), prevWalker.getWalkerY());
                        walker.walk();
                        canvas.addWalker(walker);
                        prevWalker = walker;
                        canvas.repaint();
                        System.out.println(canvas.getWalker(i).getWalkerX() + " | " + canvas.getWalker(i).getWalkerY());
                        Thread.sleep(WAIT);
                        i++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            buttonThread.start();
        });
        stopButton.addActionListener(event -> {
            running = false;
            buttonThread.interrupt();
        });
    }
}
