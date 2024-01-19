package projects.neldermead;

import util.CastBoolean;
import util.RandomGenerator;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.HashSet;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topographic map).
 * Theoretically, this algorithm can be applied to an N-dimensional data set to find the region of best solution.
 * However, it is important to note that the region of best solution may only be local--there may exist a region of the
 * data that the algorithm was not led to that is better than the solution set it finds.
 * TODO: Fix oscillatory response bug (annotation is correct, but thin strips of terrain can cause h to be
 *  forever flipped across the strip); fix RESET; allow for N-vertex Simplexes
 */
public class NelderMead implements Runnable {

    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 0;
    public static final int RESET_CODE = 0;
    public static final int RECONFIGURE_CODE = 1;
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;
    public static final int TILE_SIZE = 8;
    public final int BUTTON_WIDTH = CANVAS_WIDTH;
    public final int BUTTON_HEIGHT = CANVAS_HEIGHT / 12;
    public final Dimension CANVAS_SIZE = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
    public final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    public static Canvas canvas;
    public static HeightMap map;
    public static Simplex simplex;

    public JFrame frame;
    public JPanel container;
    public JPanel buttonPanel;
    public JButton solveButton;
    public JButton resetButton;
    public JButton reconfigureButton;
    public JButton regenerateButton;

    private final int WAIT = 1000; //milliseconds
    private final int VICTORY_DISTANCE = 6; //In Tiles; technically arbitrary: smaller value implies greater precision
    private final double ELEVATION_MAX = 100.0; //Arbitrary
    private final double ELEVATION_MIN = 0.0; //Arbitrary
    private boolean running = false;
    private final Set<String> pointHistory = new HashSet<>(); //Set of all worst Points the algorithm has found
    private Point h, s, l, r;
    private Thread buttonThread = null;

    public void init() {
        frame = new JFrame("Nelder Mead");
        container = new JPanel();

        solveButton = new JButton("Solve");
        solveButton.setToolTipText("Solves the given Height Map using the Nelder Mead algorithm.");
        resetButton = new JButton("Reset");
        resetButton.setToolTipText("Resets the Simplex to its initial position.");
        reconfigureButton = new JButton("Reconfigure");
        reconfigureButton.setToolTipText("Reconfigures the Simplex's initial position.");
        regenerateButton = new JButton("Regenerate");
        regenerateButton.setToolTipText("Generates a new Height Map and reconfigures the Simplex's initial position.");

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(BUTTON_SIZE);
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(reconfigureButton);
        buttonPanel.add(regenerateButton);

        map = new HeightMap(CANVAS_WIDTH, CANVAS_HEIGHT, TILE_SIZE, ELEVATION_MAX, ELEVATION_MIN);
        simplex = new Simplex();
        simplex.initialize(RECONFIGURE_CODE);
        canvas = new Canvas(map, simplex);
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

        solveButton.addActionListener(actionEvent -> {
            buttonThread = new Thread(() -> {
                running = true;
                try {
                    while (running) {
                        //Nelder Mead
                        simplex = canvas.getSimplex();
                        annotate();
                        if (reflection()) {
                            System.out.println("Expanding!");
                            expand();
                        } else {
                            System.out.println("Contracting!");
                            contract();
                        }
                        if (checkIteration()) {
                            Point centroid = centroid(canvas.getSimplex());
                            System.out.println("The best guess for the highest elevation is " + centroid.getZ());
                            pointHistory.clear();
                            running = false;
                            continue;
                        }
                        Thread.sleep(WAIT);
                        canvas.repaint();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            buttonThread.start();
        });
        resetButton.addActionListener(actionEvent -> {
            running = false;
            canvas.reset();
        });
        reconfigureButton.addActionListener(actionEvent -> {
            running = false;
            canvas.reconfigure();
        });
        regenerateButton.addActionListener(event -> {
            running = false;
            map = new HeightMap(CANVAS_WIDTH, CANVAS_HEIGHT, TILE_SIZE, ELEVATION_MAX, ELEVATION_MIN);
            canvas.regenerate(map);
        });
    }

    /**
     * Sorts the vertices of the Canvas's Simplex by the Simplex's vertex z-coordinate
     * l denotes best (greatest) z-coordinate
     * s denotes second-best z-coordinate
     * h denotes worst (least) z-coordinate
     */
    public void annotate() {
        RandomGenerator rand = new RandomGenerator();
        rand.makeUniformValue(1,0,false);
        boolean coin = CastBoolean.doubleToBoolean(rand.getValue());

        Point v1 = simplex.getPoint(1);
        Point v2 = simplex.getPoint(2);
        Point v3 = simplex.getPoint(3);

        System.out.println("[PRE] v1: " + v1.getX() + " " + v1.getY() + " " + v1.getZ() + " " + v1.getIndex());
        System.out.println("[PRE] v2: " + v2.getX() + " " + v2.getY() + " " + v2.getZ() + " " + v2.getIndex());
        System.out.println("[PRE] v3: " + v3.getX() + " " + v3.getY() + " " + v3.getZ() + " " + v3.getIndex());

        if (v1.getZ() < v2.getZ() && v1.getZ() < v3.getZ()) {
            System.out.println("V1 IS H");
            h = v1;
            h.setIndex(v1.getIndex());
            if (v2.getZ() < v3.getZ()) {
                s = v2;
                s.setIndex(v2.getIndex());
                l = v3;
                l.setIndex(v3.getIndex());
            } else if (v3.getZ() < v2.getZ()) {
                s = v3;
                s.setIndex(v3.getIndex());
                l = v2;
                l.setIndex(v2.getIndex());
            } else {
                if (coin) {
                    s = v2;
                    s.setIndex(v2.getIndex());
                    l = v3;
                    l.setIndex(v3.getIndex());
                } else {
                    s = v3;
                    s.setIndex(v3.getIndex());
                    l = v2;
                    l.setIndex(v2.getIndex());
                }
            }
        } else if (v2.getZ() < v1.getZ() && v2.getZ() < v3.getZ()) {
            System.out.println("V2 IS H");
            h = v2;
            h.setIndex(v2.getIndex());
            if (v1.getZ() < v3.getZ()) {
                s = v1;
                s.setIndex(v1.getIndex());
                l = v3;
                l.setIndex(v3.getIndex());
            } else if (v3.getZ() < v1.getZ()) {
                s = v3;
                s.setIndex(v3.getIndex());
                l = v1;
                l.setIndex(v1.getIndex());
            } else {
                if (coin) {
                    s = v1;
                    s.setIndex(v1.getIndex());
                    l = v3;
                    l.setIndex(v3.getIndex());
                } else {
                    s = v3;
                    s.setIndex(v3.getIndex());
                    l = v1;
                    l.setIndex(v1.getIndex());
                }
            }
        } else if (v3.getZ() < v1.getZ() && v3.getZ() < v2.getZ()) {
            System.out.println("V3 IS H");
            h = v3;
            h.setIndex(v3.getIndex());
            if (v1.getZ() < v2.getZ()) {
                s = v1;
                s.setIndex(v1.getIndex());
                l = v2;
                l.setIndex(v2.getIndex());
            } else if (v2.getZ() < v1.getZ()) {
                s = v2;
                s.setIndex(v2.getIndex());
                l = v1;
                l.setIndex(v1.getIndex());
            } else {
                if (coin) {
                    s = v1;
                    s.setIndex(v1.getIndex());
                    l = v2;
                    l.setIndex(v2.getIndex());
                } else {
                    s = v2;
                    s.setIndex(v2.getIndex());
                    l = v1;
                    l.setIndex(v1.getIndex());
                }
            }
        }

        System.out.println("[SIMPLEX] v1: " + v1.getX() + " " + v1.getY() + " " + v1.getZ() + " " + v1.getIndex());
        System.out.println("[SIMPLEX] v2: " + v2.getX() + " " + v2.getY() + " " + v2.getZ() + " " + v2.getIndex());
        System.out.println("[SIMPLEX] v3: " + v3.getX() + " " + v3.getY() + " " + v3.getZ() + " " + v3.getIndex());

        System.out.println("[POST] h: " + h.getX() + " " + h.getY() + " " + h.getZ() + " " + h.getIndex());
        System.out.println("[POST] s: " + s.getX() + " " + s.getY() + " " + s.getZ() + " " + s.getIndex());
        System.out.println("[POST] l: " + l.getX() + " " + l.getY() + " " + l.getZ() + " " + l.getIndex());

        String h_key = generateKey(h);
        if(!pointHistory.contains(h_key)) pointHistory.add(h_key);
    }

    /**
     * Reflects Point h about the midpoint (m) of the line drawn from Points l and s
     * @return true if the reflected point has a better (greater) z-coordinate than h
     */
    public boolean reflection() {
        double r_x = 2*((l.getX() + s.getX()) / 2) - h.getX();
        double r_y = 2*((l.getY() + s.getY()) / 2) - h.getY();

        r = new Point(new double[]{r_x, r_y, canvas.getHeightMap().getPoint((int)r_x, (int)r_y).getZ()}, TILE_SIZE, h.getIndex());
        System.out.println("[R]: " + r.getZ());
        return r.getZ() > h.getZ();
    }

    /**
     * If the reflection is successful, make r twice as far from m as it was (Point e), but if e is out-of-bounds then
     * keep r where it is
     */
    public void expand() {
        double e_x, e_y;
        double m_x = (l.getX() + s.getX()) / 2;
        double m_y = (l.getY() + s.getY()) / 2;
        double x = h.getX() - m_x;
        double y = h.getY() - m_y;

        double theta = Math.atan(y / x);
        if (x < 0 && y > 0) theta *= -1;
        else if (x > 0 && y > 0 || x > 0 && y < 0) theta = Math.PI - theta;
        else theta = 2 * Math.PI - theta;

        if (0 <= theta && theta < Math.PI / 2) {
            e_x = m_x + (2 * Math.abs(x));
            e_y = m_y - (2 * Math.abs(y));
        } else if (Math.PI / 2 <= theta && theta < Math.PI) {
            e_x = m_x - (2 * Math.abs(x));
            e_y = m_y - (2 * Math.abs(y));
        } else if (Math.PI <= theta && theta < (3 * Math.PI) / 2) {
            e_x = m_x - (2 * Math.abs(x));
            e_y = m_y + (2 * Math.abs(y));
        } else {
            e_x = m_x + (2 * Math.abs(x));
            e_y = m_y + (2 * Math.abs(y));
        }

        Point e = new Point(new double[]{e_x, e_y, canvas.getHeightMap().getPoint((int)e_x, (int)e_y).getZ()}, TILE_SIZE, h.getIndex());

        System.out.println("EX_REPLACE H @ " + h.getIndex() + " " + h.getZ());
        if (e.getZ() > 0.0) {
            simplex.setPoint(h.getIndex(), e);
            System.out.println("H IS NOW @ e " + e.getIndex() + " " + e.getZ());
        } else {
            simplex.setPoint(h.getIndex(), r);
            System.out.println("H IS NOW @ r " + r.getIndex() + " " + r.getZ());
        }
    }

    /**
     * If the reflection is unsuccessful, make r the midpoint of h and m (Point c), but if that point has already been a
     * worst-point, then make r 2/3 the distance from h to m
     */
    public void contract() {
        double m_x = (l.getX() + s.getX()) / 2;
        double m_y = (l.getY() + s.getY()) / 2;
        double c_x = (m_x + h.getX()) / 2;
        double c_y = (m_y + h.getY()) / 2;

        Point c = new Point(new double[]{c_x, c_y, canvas.getHeightMap().getPoint((int)c_x, (int)c_y).getZ()}, TILE_SIZE, h.getIndex());

        System.out.println("CON_REPLACE H @ " + h.getIndex() + " " + h.getZ());
        String c_key = generateKey(c);
        if(!pointHistory.contains(c_key)) {
            pointHistory.add(c_key);
            simplex.setPoint(h.getIndex(), c);
            System.out.println("H IS NOW @ c " + c.getIndex() + " " + c.getZ());
        } else {
            double c_prime_x = (m_x + 2*h.getX()) / 3;
            double c_prime_y = (m_y + 2*h.getY()) / 3;

            Point c_prime = new Point(new double[]{c_prime_x, c_prime_y, canvas.getHeightMap().getPoint((int)c_prime_x, (int)c_prime_y).getZ()}, TILE_SIZE, h.getIndex());

            simplex.setPoint(h.getIndex(), c_prime);
            System.out.println("H IS NOW @ c_prime " + c_prime.getIndex() + " " + c_prime.getZ());
        }
    }

    /**
     * Checks the current iteration to see if the best guess has been reached (re: the Simplex is quite small)
     * @return true if the Simplex's edges are less than VICTORY_DISTANCE tiles apart
     */
    public boolean checkIteration() {
        double ls = Math.sqrt((s.getX() - l.getX())*(s.getX() - l.getX()) + (s.getY() - l.getY())*(s.getY() - l.getY()));
        double sh = Math.sqrt((h.getX() - s.getX())*(h.getX() - s.getX()) + (h.getY() - s.getY())*(h.getY() - s.getY()));
        double hl = Math.sqrt((l.getX() - h.getX())*(l.getX() - h.getX()) + (l.getY() - h.getY())*(l.getY() - h.getY()));

        return ls < VICTORY_DISTANCE && sh < VICTORY_DISTANCE && hl < VICTORY_DISTANCE;
    }

    /**
     * Generates a key for a given point based upon that point's (x,y,z) coordinates
     * The key resolution cannot be so low that it repeats points frequently, but also cannot be so high that it begins
     * to ignore the gradient of the terrain--both cause oscillation
     * @param p the point
     * @return the String coordinates of the point for the pointHistory HashSet
     */
    public String generateKey(Point p) {
        DecimalFormat df = new DecimalFormat("#.##"); //2 decimal places seems to work well

        return df.format(p.getX()) + "_" + df.format(p.getY()) + "_" + df.format(p.getZ());
    }

    /**
     * Calculates the centroid of the given Simplex and maps that to the equivalent Point
     * @param s the Simplex
     * @return the Point of the centroid
     */
    public Point centroid(Simplex s) {
        int x = (int)((s.getPoint(1).getX() + s.getPoint(2).getX() + s.getPoint(3).getX()) / 3);
        int y = (int)((s.getPoint(1).getY() + s.getPoint(2).getY() + s.getPoint(3).getY()) / 3);

        return map.getPoint(x, y);
    }
}