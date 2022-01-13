package projects.neldermead;

import java.util.Random;

public class Simplex {

    private final HeightMap MAP = NelderMead.map;
    private final double ORIGIN_X = NelderMead.ORIGIN_X;
    private final double ORIGIN_Y = NelderMead.ORIGIN_Y;
    private final double CANVAS_WIDTH = NelderMead.CANVAS_WIDTH;
    private final double CANVAS_HEIGHT = NelderMead.CANVAS_HEIGHT;
    private final int RESET_CODE = NelderMead.RESET_CODE;
    private final int RECONFIGURE_CODE = NelderMead.RECONFIGURE_CODE;
    private final int TILE_WIDTH = NelderMead.TILE_WIDTH;
    private final int TILE_HEIGHT = NelderMead.TILE_HEIGHT;
    private final Point INIT_ONE, INIT_TWO, INIT_THREE;
    private Point one, two, three;

    /**
     * Empty Constructor
     */
    public Simplex() {
        this.one = new Point();
        this.two = new Point();
        this.three = new Point();
        this.INIT_ONE = one;
        this.INIT_TWO = two;
        this.INIT_THREE = three;
    }

    /**
     * Full Constructor
     * @param one a vertex of the Simplex
     * @param two a vertex of the Simplex
     * @param three a vertex of the Simplex
     */
    public Simplex(Point one, Point two, Point three) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.INIT_ONE = one;
        this.INIT_TWO = two;
        this.INIT_THREE = three;
    }

    /**
     * Initializes the simplex by setting each vertex's (x, y) coordinate
     * Depends upon the context of the initialization
     * @param code the integer to determine the context
     */
    public void initialize(int code) {
        double map_width = CANVAS_WIDTH / TILE_WIDTH;
        double map_height = CANVAS_HEIGHT / TILE_HEIGHT;
        //Sends the Simplex to its original coordinates
        if (code == RESET_CODE) {
            one = INIT_ONE;
            one.setTileWidth(TILE_WIDTH);
            one.setTileHeight(TILE_HEIGHT);
            two = INIT_TWO;
            two.setTileWidth(TILE_WIDTH);
            two.setTileHeight(TILE_HEIGHT);
            three = INIT_THREE;
            three.setTileWidth(TILE_WIDTH);
            three.setTileHeight(TILE_HEIGHT);
        /*
        Sends the Simplex to new coordinates.
        Vertex one is randomly generated new coordinates
        Vertex two always has the same y-coordinate as one, but its x-coordinate is closer to the middle of the canvas than one's x-coordinate by 1/8 of the map's width
        Vertex three always has the same x-coordinate as one, but its y-coordinate is closer to the middle of the canvas than one's y-coordinate by 1/8 of the map's height
         */
        } else if (code == RECONFIGURE_CODE) {
            Random r = new Random();
            double one_x = HeightMap.lerp(r.nextDouble(), ORIGIN_X, map_width);
            double one_y = HeightMap.lerp(r.nextDouble(), ORIGIN_Y, map_height);
            one.setX(Math.floor(one_x));
            one.setY(Math.floor(one_y));
            one.setZ(MAP.getPoint(((int)Math.floor(one.getX())) * one.getTileWidth(), ((int)Math.floor(one.getY())) * one.getTileHeight()).getZ());
            one.setTileWidth(TILE_WIDTH);
            one.setTileHeight(TILE_HEIGHT);
            two.setTileWidth(TILE_WIDTH);
            two.setTileHeight(TILE_HEIGHT);
            three.setTileWidth(TILE_WIDTH);
            three.setTileHeight(TILE_HEIGHT);

            if (one_x > map_width / 2 && one_y <= map_height / 2) { //Quadrant 1
                two.setX(Math.floor(one_x - map_width / 8));
                two.setY(Math.floor(one_y));
                three.setX(Math.floor(one_x));
                three.setY(Math.floor(one_y + map_height / 8));
            } else if (one_x <= map_width / 2 && one_y <= map_height / 2) { //Quadrant 2
                two.setX(Math.floor(one_x + map_width / 8));
                two.setY(Math.floor(one_y));
                three.setX(Math.floor(one_x));
                three.setY(Math.floor(one_y + map_height / 8));
            } else if (one_x <= map_width / 2 && one_y > map_height / 2) { //Quadrant 3
                two.setX(Math.floor(one_x + map_width / 8));
                two.setY(Math.floor(one_y));
                three.setX(Math.floor(one_x));
                three.setY(Math.floor(one_y - map_height / 8));
            } else if (one_x > map_width / 2 && one_y > map_height / 2) { //Quadrant 4
                two.setX(Math.floor(one_x - map_width / 8));
                two.setY(Math.floor(one_y));
                three.setX(Math.floor(one_x));
                three.setY(Math.floor(one_y - map_height / 8));
            }

            two.setZ(MAP.getPoint(((int)Math.floor(two.getX())) * two.getTileWidth(), ((int)Math.floor(two.getY())) * two.getTileHeight()).getZ());
            three.setZ(MAP.getPoint(((int)Math.floor(three.getX())) * three.getTileWidth(), ((int)Math.floor(three.getY())) * three.getTileHeight()).getZ());
        }
    }

    public Point getOne() {
        return one;
    }

    public void setOne(Point p) {
        this.one = p;
    }

    public Point getTwo() {
        return two;
    }

    public void setTwo(Point p) {
        this.two = p;
    }

    public Point getThree() {
        return three;
    }

    public void setThree(Point p) {
        this.three = p;
    }
}
