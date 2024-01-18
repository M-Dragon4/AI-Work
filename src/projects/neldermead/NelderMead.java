package projects.neldermead;

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
 * TODO: Fix oscillatory response bug
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
    private final int VICTORY_DISTANCE = 7; //In Tiles
    private final double ELEVATION_MAX = 100.0; //Arbitrary
    private final double ELEVATION_MIN = 0.0; //Arbitrary
    private boolean running = false;
    private final Set<String> pointHistory = new HashSet<>(); //Set of all worst Points the algorithm has found
    private int isH, isS;
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
//                        System.out.println("h_: " + h.getX() + " " + h.getY() + " " + h.getZ());
//                        System.out.println("s_: " + s.getX() + " " + s.getY() + " " + s.getZ());
//                        System.out.println("l_: " + l.getX() + " " + l.getY() + " " + l.getZ());
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
//        System.out.println("z1: " + canvas.getSimplex().getPoint(1).getZ());
//        System.out.println("z2: " + canvas.getSimplex().getPoint(2).getZ());
//        System.out.println("z3: " + canvas.getSimplex().getPoint(3).getZ());
        if (canvas.getSimplex().getPoint(1).getZ() < canvas.getSimplex().getPoint(2).getZ() && canvas.getSimplex().getPoint(1).getZ() < canvas.getSimplex().getPoint(3).getZ()) {
            h = canvas.getSimplex().getPoint(1);
            isH = 1;
            if (canvas.getSimplex().getPoint(2).getZ() < canvas.getSimplex().getPoint(3).getZ()) {
                s = canvas.getSimplex().getPoint(2);
                isS = 2;
                l = canvas.getSimplex().getPoint(3);
            } else {
                s = canvas.getSimplex().getPoint(3);
                isS = 3;
                l = canvas.getSimplex().getPoint(2);
            }
        } else if (canvas.getSimplex().getPoint(2).getZ() < canvas.getSimplex().getPoint(1).getZ() && canvas.getSimplex().getPoint(2).getZ() < canvas.getSimplex().getPoint(3).getZ()) {
            h = canvas.getSimplex().getPoint(2);
            isH = 2;
            if (canvas.getSimplex().getPoint(1).getZ() < canvas.getSimplex().getPoint(3).getZ()) {
                s = canvas.getSimplex().getPoint(1);
                isS = 1;
                l = canvas.getSimplex().getPoint(3);
            } else {
                s = canvas.getSimplex().getPoint(3);
                isS = 3;
                l = canvas.getSimplex().getPoint(1);
            }
        } else {
            h = canvas.getSimplex().getPoint(3);
            isH = 3;
            if (canvas.getSimplex().getPoint(1).getZ() < canvas.getSimplex().getPoint(2).getZ()) {
                s = canvas.getSimplex().getPoint(1);
                isS = 1;
                l = canvas.getSimplex().getPoint(2);
            } else {
                s = canvas.getSimplex().getPoint(2);
                isS = 2;
                l = canvas.getSimplex().getPoint(1);
            }
        }
        System.out.println("h: " + h.getX() + " " + h.getY() + " " + h.getZ());
        System.out.println("s: " + s.getX() + " " + s.getY() + " " + s.getZ());
        System.out.println("l: " + l.getX() + " " + l.getY() + " " + l.getZ());

        String h_key = generateKey(h);
        //String s_key = generateKey(s);
        //String l_key = generateKey(l);
        if(!pointHistory.contains(h_key)) pointHistory.add(h_key);
        //if(!pointHistory.contains(s_key)) pointHistory.add(s_key);
        //if(!pointHistory.contains(l_key)) pointHistory.add(l_key);
    }

    /**
     * Reflects Point h about the midpoint (m) of the line drawn from Points l and s
     * @return true if the reflected point has a better (greater) z-coordinate than h
     */
    public boolean reflection() {
        double r_x = 2*((l.getX() + s.getX()) / 2) - h.getX();
        double r_y = 2*((l.getY() + s.getY()) / 2) - h.getY();

        r = new Point(new double[]{r_x, r_y, canvas.getHeightMap().getPoint((int)r_x, (int)r_y).getZ()}, TILE_SIZE);
//        System.out.println("r: " + r.getX() + " " + r.getY() + " " + r.getZ());
        return r.getZ() > h.getZ();
    }

    /**
     * If the reflection is successful, make r twice as far from m as it was
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

        Point e = new Point(new double[]{e_x, e_y, canvas.getHeightMap().getPoint((int)e_x, (int)e_y).getZ()}, TILE_SIZE);

        if (e.getZ() > 0.0) {
            switch(isH) {
                case 1: {
                    canvas.getSimplex().setPoint(1, e);
                    break;
                }
                case 2: {
                    canvas.getSimplex().setPoint(2, e);
                    break;
                }
                case 3: {
                    canvas.getSimplex().setPoint(3, e);
                    break;
                }
            }
        } else {
            switch(isH) {
                case 1: {
                    canvas.getSimplex().setPoint(1, r);
                    break;
                }
                case 2: {
                    canvas.getSimplex().setPoint(2, r);
                    break;
                }
                case 3: {
                    canvas.getSimplex().setPoint(3, r);
                    break;
                }
            }
        }
    }

    /**
     * If the reflection is unsuccessful, make r the midpoint of h and m
     */
    public void contract() {
        double m_x = (l.getX() + s.getX()) / 2;
        double m_y = (l.getY() + s.getY()) / 2;
        double c_x = (m_x + h.getX()) / 2;
        double c_y = (m_y + h.getY()) / 2;

        Point c = new Point(new double[]{c_x, c_y, canvas.getHeightMap().getPoint((int)c_x, (int)c_y).getZ()}, TILE_SIZE);

        String c_key = generateKey(c);
        if(!pointHistory.contains(c_key)) {
            System.out.println("pointHistory does NOT contain " + c_key);
            pointHistory.add(c_key);

            switch(isH) {
                case 1: {
                    canvas.getSimplex().setPoint(1, c);
                    break;
                }
                case 2: {
                    canvas.getSimplex().setPoint(2, c);
                    break;
                }
                case 3: {
                    canvas.getSimplex().setPoint(3, c);
                    break;
                }
            }
        } else {
            System.out.println("pointHistory contains " + c_key);
            double c_prime_x = 2*((l.getX() + s.getX()) / 2) - c.getX();
            double c_prime_y = 2*((l.getY() + s.getY()) / 2) - c.getY();

            Point c_prime = new Point(new double[]{c_prime_x, c_prime_y, canvas.getHeightMap().getPoint((int)c_prime_x, (int)c_prime_y).getZ()}, TILE_SIZE);

            switch(isH) {
                case 1: {
                    canvas.getSimplex().setPoint(1, c_prime);
                    break;
                }
                case 2: {
                    canvas.getSimplex().setPoint(2, c_prime);
                    break;
                }
                case 3: {
                    canvas.getSimplex().setPoint(3, c_prime);
                    break;
                }
            }
        }
    }

    /**
     * Checks the current iteration to see if the best guess has been reached (re: the Simplex is quite small)
     * The three tile condition is arbitrary
     * @return true if the Simplex's vertices are fewer than three tiles apart
     */
    public boolean checkIteration() {
        //annotate();

        double ls = Math.sqrt((s.getX() - l.getX())*(s.getX() - l.getX()) + (s.getY() - l.getY())*(s.getY() - l.getY()));
        double sh = Math.sqrt((h.getX() - s.getX())*(h.getX() - s.getX()) + (h.getY() - s.getY())*(h.getY() - s.getY()));
        double hl = Math.sqrt((l.getX() - h.getX())*(l.getX() - h.getX()) + (l.getY() - h.getY())*(l.getY() - h.getY()));

        return ls < VICTORY_DISTANCE && sh < VICTORY_DISTANCE && hl < VICTORY_DISTANCE;
    }

    /**
     * Generates a key for a given point based upon that point's (x,y,z) coordinates
     * @param p the point
     * @return the String coordinates of the point for the pointHistory HashSet
     */
    public String generateKey(Point p) {
        DecimalFormat df = new DecimalFormat("#.####");

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