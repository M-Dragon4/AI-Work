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
    private final int TILE_SIZE = NelderMead.TILE_SIZE;
    private Point one, two, three, initOne, initTwo, initThree;

    /**
     * Empty Constructor
     */
    public Simplex() {
        this.one = new Point();
        this.two = new Point();
        this.three = new Point();
        this.initOne = one;
        this.initTwo = two;
        this.initThree = three;
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
        this.initOne = one;
        this.initTwo = two;
        this.initThree = three;
    }

    /**
     * Initializes the simplex by setting each vertex's (x,y) coordinate
     * Depends upon the context of the initialization
     * @param code the integer to determine the context
     */
    public void initialize(int code) {
        double map_width = CANVAS_WIDTH / TILE_SIZE;
        double map_height = CANVAS_HEIGHT / TILE_SIZE;
        switch(code) {
            //Sends the Simplex to its original coordinates
            case RESET_CODE: {
                one = initOne;
                one.setTileSize(TILE_SIZE);
                two = initTwo;
                two.setTileSize(TILE_SIZE);
                three = initThree;
                three.setTileSize(TILE_SIZE);
            } break;
            /*
            Sends the Simplex to new coordinates.
            Vertex one is randomly generated new coordinates
            Vertex two always has the same y-coordinate as one, but its x-coordinate is closer to the middle of the canvas than one's x-coordinate by 1/8 of the map's width
            Vertex three always has the same x-coordinate as one, but its y-coordinate is closer to the middle of the canvas than one's y-coordinate by 1/8 of the map's height
            */
            case RECONFIGURE_CODE: {
                Random r = new Random();
                double one_x = HeightMap.lerp(r.nextDouble(), ORIGIN_X, map_width);
                double one_y = HeightMap.lerp(r.nextDouble(), ORIGIN_Y, map_height);
                one.setX(Math.floor(one_x));
                one.setY(Math.floor(one_y));
                one.setZ(MAP.getPoint((int)Math.floor(one.getX()), (int)Math.floor(one.getY())).getZ());
                one.setTileSize(TILE_SIZE);
                two.setTileSize(TILE_SIZE);
                three.setTileSize(TILE_SIZE);

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

                two.setZ(MAP.getPoint((int)Math.floor(two.getX()), (int)Math.floor(two.getY())).getZ());
                three.setZ(MAP.getPoint((int)Math.floor(three.getX()), (int)Math.floor(three.getY())).getZ());

                initOne = one;
                initTwo = two;
                initThree = three;
            } break;
        }
    }

    public Point getPoint(int index) {
        switch(index) {
            case 1: {
                return one;
            }
            case 2: {
                return two;
            }
            case 3: {
                return three;
            }
            default: return null;
        }
    }

    public void setPoint(int index, Point p) {
        switch(index) {
            case 1: {
                this.one = p;
                break;
            }
            case 2: {
                this.two = p;
                break;
            }
            case 3: {
                this.three = p;
                break;
            }
            default: break;
        }
    }
}
