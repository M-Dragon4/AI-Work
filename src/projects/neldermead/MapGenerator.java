package projects.neldermead;

import util.RandomGenerator;
import java.util.ArrayList;

//TODO: The map has a real probability of being jagged, not contoured, find algorithm for smoother map generation
public class MapGenerator {

    private final int MAX_HEIGHT = 100; //Arbitrarily defined
    private final int MIN_HEIGHT = 10; //Arbitrarily defined, though should not be zero
    private ArrayList<Point> map;
    private Point max;
    private RandomGenerator rand;

    public MapGenerator(ArrayList<Point> map) {
        this.map = map;
    }

    public void generateMap(int width, int height) {
        rand = new RandomGenerator(max.getZ());
        rand.makeValue(MAX_HEIGHT, MIN_HEIGHT, false);
        max.setZ((int) rand.getValue());
        rand = new RandomGenerator(max.getX());
        rand.makeValue(NelderMead.WIDTH - 1, 1, false);
        max.setX((int) rand.getValue());
        rand = new RandomGenerator(max.getY());
        rand.makeValue(NelderMead.HEIGHT - 1, 1, false);
        max.setY((int) rand.getValue());

        int k = 0;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                rand = new RandomGenerator(k);
                rand.makeValue(MAX_HEIGHT - 1, 0, false);
                k = (int) rand.getValue();
                Point p = new Point(i, j, k);
                map.add(p);
            }
        }
    }

    public ArrayList<Point> getMap() {
        return map;
    }
}
