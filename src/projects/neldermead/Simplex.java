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
    private final Point INIT_H, INIT_S, INIT_L;
    private Point h, s, l;

    /**
     * Empty Constructor
     */
    public Simplex() {
        this.h = new Point();
        this.s = new Point();
        this.l = new Point();
        this.INIT_H = h;
        this.INIT_S = s;
        this.INIT_L = l;
    }

    /**
     * Full Constructor
     * @param h a vertex of the Simplex
     * @param s a vertex of the Simplex
     * @param l a vertex of the Simplex (used as the initial guess)
     */
    public Simplex(Point h, Point s, Point l) {
        this.h = h;
        this.s = s;
        this.l = l;
        this.INIT_H = h;
        this.INIT_S = s;
        this.INIT_L = l;
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
            h = INIT_H;
            h.setTileWidth(TILE_WIDTH);
            h.setTileHeight(TILE_HEIGHT);
            s = INIT_S;
            s.setTileWidth(TILE_WIDTH);
            s.setTileHeight(TILE_HEIGHT);
            l = INIT_L;
            l.setTileWidth(TILE_WIDTH);
            l.setTileHeight(TILE_HEIGHT);
        /*
        Sends the Simplex to new coordinates.
        Vertex l is randomly generated new coordinates
        Vertex s always has the same y-coordinate as l, but its x-coordinate is closer to the middle of the canvas than l's x-coordinate by 1/8 of the map's width
        Vertex h always has the same x-coordinate as l, but its y-coordinate is closer to the middle of the canvas than l's y-coordinate by 1/8 of the map's height
         */
        } else if (code == RECONFIGURE_CODE) {
            Random r = new Random();
            double l_x = HeightMap.lerp(r.nextDouble(), ORIGIN_X, map_width);
            double l_y = HeightMap.lerp(r.nextDouble(), ORIGIN_Y, map_height);
            l.setX(Math.floor(l_x));
            l.setY(Math.floor(l_y));
            l.setZ(MAP.getPoint(((int)Math.floor(l.getX())) * l.getTileWidth(), ((int)Math.floor(l.getY())) * l.getTileHeight()).getZ());
            l.setTileWidth(TILE_WIDTH);
            l.setTileHeight(TILE_HEIGHT);
            s.setTileWidth(TILE_WIDTH);
            s.setTileHeight(TILE_HEIGHT);
            h.setTileWidth(TILE_WIDTH);
            h.setTileHeight(TILE_HEIGHT);

            if (l_x > map_width / 2 && l_y <= map_height / 2) { //Quadrant 1
                s.setX(Math.floor(l_x - map_width / 8));
                s.setY(Math.floor(l_y));
                h.setX(Math.floor(l_x));
                h.setY(Math.floor(l_y + map_height / 8));
            } else if (l_x <= map_width / 2 && l_y <= map_height / 2) { //Quadrant 2
                s.setX(Math.floor(l_x + map_width / 8));
                s.setY(Math.floor(l_y));
                h.setX(Math.floor(l_x));
                h.setY(Math.floor(l_y + map_height / 8));
            } else if (l_x <= map_width / 2 && l_y > map_height / 2) { //Quadrant 3
                s.setX(Math.floor(l_x + map_width / 8));
                s.setY(Math.floor(l_y));
                h.setX(Math.floor(l_x));
                h.setY(Math.floor(l_y - map_height / 8));
            } else if (l_x > map_width / 2 && l_y > map_height / 2) { //Quadrant 4
                s.setX(Math.floor(l_x - map_width / 8));
                s.setY(Math.floor(l_y));
                h.setX(Math.floor(l_x));
                h.setY(Math.floor(l_y - map_height / 8));
            }

            s.setZ(MAP.getPoint(((int)Math.floor(s.getX())) * s.getTileWidth(), ((int)Math.floor(s.getY())) * s.getTileHeight()).getZ());
            h.setZ(MAP.getPoint(((int)Math.floor(h.getX())) * h.getTileWidth(), ((int)Math.floor(h.getY())) * h.getTileHeight()).getZ());
        }
    }

    public Point getH() {
        return h;
    }

    public void setH(Point p) {
        this.h = p;
    }

    public Point getS() {
        return s;
    }

    public void setS(Point p) {
        this.s = p;
    }

    public Point getL() {
        return l;
    }

    public void setL(Point p) {
        this.l = p;
    }
}
