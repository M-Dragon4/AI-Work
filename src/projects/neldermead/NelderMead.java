package projects.neldermead;

import javax.swing.*;
import java.awt.*;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topographic map)
 * TODO: Fix oscillatory response bug (ensure h, s, l are annotated correctly; ensure expansion/contraction appropriately
 *  overwrite h and only h; why is h_ the same as h?); Fix e existence conditional; Check expanion()'s theta; Fix
 *  incorrect reflection() evaluation
 *
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
    private int isH;
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
                        annotate();
                        if (reflection()) {
                            System.out.println("Expanding!");
                            expand();
                        } else {
                            System.out.println("Contracting!");
                            contract();
                        }
                        System.out.println("h_: " + h.getX() + " " + h.getY() + " " + h.getZ());
                        System.out.println("s_: " + s.getX() + " " + s.getY() + " " + s.getZ());
                        System.out.println("l_: " + l.getX() + " " + l.getY() + " " + l.getZ());
                        if (checkIteration()) {
                            System.out.println("The best guess for the highest elevation is " + l.getZ());
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
        System.out.println("z1: " + canvas.getSimplex().getOne().getZ());
        System.out.println("z2: " + canvas.getSimplex().getTwo().getZ());
        System.out.println("z3: " + canvas.getSimplex().getThree().getZ());
        if (canvas.getSimplex().getOne().getZ() < canvas.getSimplex().getTwo().getZ() && canvas.getSimplex().getOne().getZ() < canvas.getSimplex().getThree().getZ()) {
            h = canvas.getSimplex().getOne();
            isH = 1;
            if (canvas.getSimplex().getTwo().getZ() < canvas.getSimplex().getThree().getZ()) {
                s = canvas.getSimplex().getTwo();
                l = canvas.getSimplex().getThree();
            } else {
                s = canvas.getSimplex().getThree();
                l = canvas.getSimplex().getTwo();
            }
        } else if (canvas.getSimplex().getTwo().getZ() < canvas.getSimplex().getOne().getZ() && canvas.getSimplex().getTwo().getZ() < canvas.getSimplex().getThree().getZ()) {
            h = canvas.getSimplex().getTwo();
            isH = 2;
            if (canvas.getSimplex().getOne().getZ() < canvas.getSimplex().getThree().getZ()) {
                s = canvas.getSimplex().getOne();
                l = canvas.getSimplex().getThree();
            } else {
                s = canvas.getSimplex().getThree();
                l = canvas.getSimplex().getOne();
            }
        } else {
            h = canvas.getSimplex().getThree();
            isH = 3;
            if (canvas.getSimplex().getOne().getZ() < canvas.getSimplex().getTwo().getZ()) {
                s = canvas.getSimplex().getOne();
                l = canvas.getSimplex().getTwo();
            } else {
                s = canvas.getSimplex().getTwo();
                l = canvas.getSimplex().getOne();
            }
        }
        System.out.println("h: " + h.getX() + " " + h.getY() + " " + h.getZ());
        System.out.println("s: " + s.getX() + " " + s.getY() + " " + s.getZ());
        System.out.println("l: " + l.getX() + " " + l.getY() + " " + l.getZ());
    }

    /**
     * Reflects Point h about the midpoint (c) of the line drawn from Points l and s
     * @return true if the reflected point has a better (greater) z-coordinate than h
     */
    public boolean reflection() {
        double r_x = 2*((l.getX() + s.getX()) / 2) - h.getX();
        double r_y = 2*((l.getY() + s.getY()) / 2) - h.getY();

        r = new Point(new double[]{r_x, r_y, canvas.getHeightMap().getPoint((int)r_x, (int)r_y).getZ()}, TILE_SIZE);
        System.out.println("r: " + r.getX() + " " + r.getY() + " " + r.getZ());
        return r.getZ() > h.getZ();
    }

    /**
     * If the reflection is successful, make r twice as far from c as it was
     */
    public void expand() {
        double e_x, e_y;
        double m_x = (l.getX() + s.getX()) / 2;
        double m_y = (l.getY() + s.getY()) / 2;
        double x = h.getX() - m_x;
        double y = h.getY() - m_y;

        double theta = Math.atan(y / x);
        if (theta < 0 && x > 0) theta += (2 * Math.PI);
        else if ((theta > 0 && x < 0) || (theta < 0 && x < 0)) theta += Math.PI;

        if (0 <= theta && theta < Math.PI / 2) {
            e_x = m_x - (2 * x);
            e_y = m_y - (2 * y);
        } else if (Math.PI / 2 <= theta && theta < Math.PI) {
            e_x = m_x + (2 * x);
            e_y = m_y - (2 * y);
        } else if (Math.PI <= theta && theta < (3 * Math.PI) / 2) {
            e_x = m_x + (2 * x);
            e_y = m_y + (2 * y);
        } else {
            e_x = m_x - (2 * x);
            e_y = m_y + (2 * y);
        }

        Point e = new Point(new double[]{e_x, e_y, canvas.getHeightMap().getPoint((int)e_x, (int)e_y).getZ()}, TILE_SIZE);

        if (e.getZ() != 0.0) {
            switch(isH) {
                case 1: {
                    canvas.getSimplex().setOne(e);
                    break;
                }
                case 2: {
                    canvas.getSimplex().setTwo(e);
                    break;
                }
                case 3: {
                    canvas.getSimplex().setThree(e);
                    break;
                }
            }
        } else {
            switch(isH) {
                case 1: {
                    canvas.getSimplex().setOne(r);
                    break;
                }
                case 2: {
                    canvas.getSimplex().setTwo(r);
                    break;
                }
                case 3: {
                    canvas.getSimplex().setThree(r);
                    break;
                }
            }
        }
    }

    /**
     * If the reflection is unsuccessful, make r the midpoint of h and c
     */
    public void contract() {
        double m_x = (l.getX() + s.getX()) / 2;
        double m_y = (l.getY() + s.getY()) / 2;
        double c_x = (m_x + h.getX()) / 2;
        double c_y = (m_y + h.getY()) / 2;

        Point c = new Point(new double[]{c_x, c_y, canvas.getHeightMap().getPoint((int)c_x, (int)c_y).getZ()}, TILE_SIZE);

        switch(isH) {
            case 1: {
                canvas.getSimplex().setOne(c);
                break;
            }
            case 2: {
                canvas.getSimplex().setTwo(c);
                break;
            }
            case 3: {
                canvas.getSimplex().setThree(c);
                break;
            }
        }
    }

    /**
     * Checks the current iteration to see if the best guess has been reached (re: the Simplex is quite small)
     * The three tile condition is arbitrary
     * @return true if the Simplex's vertices are fewer than three tiles apart
     */
    public boolean checkIteration() {
        double ls = Math.sqrt((s.getX() - l.getX())*(s.getX() - l.getX()) + (s.getY() - l.getY())*(s.getY() - l.getY()));
        System.out.println("ls: " + ls);
        double sh = Math.sqrt((h.getX() - s.getX())*(h.getX() - s.getX()) + (h.getY() - s.getY())*(h.getY() - s.getY()));
        System.out.println("sh: " + sh);
        double hl = Math.sqrt((l.getX() - h.getX())*(l.getX() - h.getX()) + (l.getY() - h.getY())*(l.getY() - h.getY()));
        System.out.println("hl: " + hl);

        return ls < VICTORY_DISTANCE && sh < VICTORY_DISTANCE && hl < VICTORY_DISTANCE;
    }

    /**
     * Theoretically, l is annotated as the best point, so this method shouldn't need to exist.
     *
     * Once the Simplex is small enough, the best guess is the Point with the greatest z-coordinate
     * @return the Point with the best (greatest) z-coordinate
     */
//    public Point bestPoint() {
//        if (canvas.getSimplex().getOne().getZ() > canvas.getSimplex().getTwo().getZ() && canvas.getSimplex().getOne().getZ() > canvas.getSimplex().getThree().getZ()) return canvas.getSimplex().getOne();
//        else if (canvas.getSimplex().getTwo().getZ() > canvas.getSimplex().getThree().getZ()) return canvas.getSimplex().getTwo();
//        else return canvas.getSimplex().getThree();
//    }
}