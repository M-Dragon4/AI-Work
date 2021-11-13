package projects.neldermead;

import javax.swing.*;
import java.awt.*;
import java.net.JarURLConnection;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topology map)
 * TODO: Draw the map and implement Nelder Mead
 */
public class NelderMead implements Runnable {
    public final int ORIGIN_X = 0;
    public final int ORIGIN_Y = 0;
    public final int CANVAS_WIDTH = 640;
    public final int CANVAS_HEIGHT = 480;
    public final int BUTTON_WIDTH = CANVAS_WIDTH;
    public final int BUTTON_HEIGHT = CANVAS_HEIGHT / 12;
    public final Dimension CANVAS_SIZE = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
    public final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    public JFrame frame;
    public JPanel container;
    public JPanel buttonPanel;
    public JButton solveButton;
    public JButton resetButton;
    public JButton reconfigureButton;
    public JButton regenerateButton;

    //Colors to represent at what percentage between the minimum and maximum elevation that the Point lies
    private final Color ZERO = new Color(75, 128, 38);
    private final Color FIVE = new Color(103, 143, 54);
    private final Color TEN = new Color(141, 164, 68);
    private final Color FIFTEEN = new Color(153, 181, 79);
    private final Color TWENTY = new Color(177, 195, 93);
    private final Color TWENTYFIVE = new Color(200, 211, 107);
    private final Color THIRTY = new Color(220, 223, 110);
    private final Color THIRTYFIVE = new Color(234, 228, 118);
    private final Color FORTY = new Color(237, 223, 124);
    private final Color FORTYFIVE = new Color(239, 220, 115);
    private final Color FIFTY = new Color(237, 212, 109);
    private final Color FIFTYFIVE = new Color(237, 205, 102);
    private final Color SIXTY = new Color(235, 196, 103);
    private final Color SIXTYFIVE = new Color(232, 188, 99);
    private final Color SEVENTY = new Color(230, 180, 93);
    private final Color SEVENTYFIVE = new Color(220, 165, 101);
    private final Color EIGHTY = new Color(210, 149, 95);
    private final Color EIGHTYFIVE = new Color(187, 125, 78);
    private final Color NINETY = new Color(186, 114, 70);
    private final Color NINETYFIVE = new Color(177, 102, 71);
    private final Color HUNDRED = new Color(161, 89, 67);

    private final int MAP_WIDTH = 640;
    private final int MAP_HEIGHT = 480;
    private final int TILE_WIDTH = 4;
    private final int TILE_HEIGHT = 4;
    private final int ELEVATION_MAX = 100;
    private final int ELEVATION_MIN = 0;
    private boolean running = false;
    private Thread buttonThread = null;
    private HeightMap map;

    public void init() {
        map = new HeightMap(MAP_WIDTH, MAP_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ELEVATION_MAX, ELEVATION_MIN);

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

        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.add(buttonPanel);

        frame.setSize(MAP_WIDTH, MAP_HEIGHT);
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