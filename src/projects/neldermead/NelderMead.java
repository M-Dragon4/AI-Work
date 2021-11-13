package projects.neldermead;

import javax.swing.*;
import java.awt.*;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topology map)
 * TODO: Draw the map better and implement Nelder Mead
 */
public class NelderMead implements Runnable {
    public static HeightMap map;
    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 0;
    public final int CANVAS_WIDTH = 640;
    public final int CANVAS_HEIGHT = 480;
    public final int BUTTON_WIDTH = CANVAS_WIDTH;
    public final int BUTTON_HEIGHT = CANVAS_HEIGHT / 12;
    public final Dimension CANVAS_SIZE = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
    public final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    public Canvas canvas;
    public JFrame frame;
    public JPanel container;
    public JPanel buttonPanel;
    public JButton solveButton;
    public JButton resetButton;
    public JButton reconfigureButton;
    public JButton regenerateButton;

    private final int TILE_WIDTH = 16;
    private final int TILE_HEIGHT = 16;
    private final int WAIT = 100; //In milliseconds
    private final double ELEVATION_MAX = 100.0;
    private final double ELEVATION_MIN = 0.0;
    private boolean running = false;
    private Thread buttonThread = null;

    public void init() {
        frame = new JFrame("Nelder Mead");
        container = new JPanel();

        solveButton = new JButton("Solve");
        solveButton.setToolTipText("Solves the given Height Map using the Nelder Mead algorithm.");
        resetButton = new JButton("Reset");
        resetButton.setToolTipText("Resets the simplex to its initial position.");
        reconfigureButton = new JButton("Reconfigure");
        reconfigureButton.setToolTipText("Reconfigures the simplex's initial position.");
        regenerateButton = new JButton("Regenerate");
        regenerateButton.setToolTipText("Generates a new Height Map and a new simplex.");

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(BUTTON_SIZE);
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(reconfigureButton);
        buttonPanel.add(regenerateButton);

        canvas = new Canvas(new HeightMap(CANVAS_WIDTH, CANVAS_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ELEVATION_MAX, ELEVATION_MIN));
        canvas.setPreferredSize(CANVAS_SIZE);

        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.add(buttonPanel);
        container.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(container);
        frame.pack();
        frame.setVisible(true);
    }

    public void run() {
        init();
    }
}