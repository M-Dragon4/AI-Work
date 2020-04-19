package projects.neldermead;

import util.RandomGenerator;

import java.util.ArrayList;

public class MapGenerator {

    private final int MAX_HEIGHT = 100; //Arbitrarily defined
    private final int MIN_HEIGHT = 10; //Arbitrarily defined, though should not be zero
    private ArrayList<Point> map;
    private Point z_max;
    private RandomGenerator rand;

    public MapGenerator(ArrayList<Point> map) {
        this.map = map;
    }

    public void generateMap(int width, int height) {
        rand = new RandomGenerator(z_max.getZ());
        rand.makeValue(MAX_HEIGHT, MIN_HEIGHT, false);
        z_max.setZ((int) rand.getValue());
        rand = new RandomGenerator(z_max.getX());
        rand.makeValue(NelderMead.WIDTH, NelderMead.HEIGHT, false);
        z_max.setX((int) rand.getValue());
        rand = new RandomGenerator(z_max.getY());
        rand.makeValue(NelderMead.WIDTH, NelderMead.HEIGHT, false);
        z_max.setY((int) rand.getValue());

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {

            }
        }
    }

    public ArrayList<Point> getMap() {
        return map;
    }
}
