package projects.neldermead;

import javax.swing.*;
import java.util.ArrayList;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topology map)
 */
public class NelderMead {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private static ArrayList<Point> list = new ArrayList<>();
    private static MapGenerator map = new MapGenerator(list);

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(WIDTH, HEIGHT);
        f.setTitle("Nelder Mead");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        map.generateMap(WIDTH, HEIGHT);
    }
}