package projects.neldermead;

import javax.swing.*;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topology map)
 * TODO: Draw the map and implement Nelder Mead
 */
public class NelderMead implements Runnable {

    private final int MAP_WIDTH = 640;
    private final int MAP_HEIGHT = 480;
    private final int TILE_WIDTH = 4;
    private final int TILE_HEIGHT = 4;
    private final int ELEVATION_MAX = 100;
    private final int ELEVATION_MIN = 0;
    private HeightMap map;

    public void init() {
        map = new HeightMap(MAP_WIDTH, MAP_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ELEVATION_MAX, ELEVATION_MIN);

        JFrame f = new JFrame();
        f.setSize(MAP_WIDTH, MAP_HEIGHT);
        f.setTitle("Nelder Mead");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void run() {
        init();
    }
}