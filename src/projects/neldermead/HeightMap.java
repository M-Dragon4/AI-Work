package projects.neldermead;

import java.util.Random;

/**
 * TODO: Fix lack of uniqueness across generations (Is a different noise function required? It's not a product of which
 *  tile is chosen as the initial tile...)
 */
public class HeightMap {

    private final double STRETCH_FACTOR = 28.0;
    private Point[] map;
    private double elevationMax, elevationMin;

    private final int[] P = new int[512], PERMUTATION = { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };

    /**
     * Empty Constructor
     */
    public HeightMap() {
        this.map = null;
        this.elevationMax = 0.0;
        this.elevationMin = 0.0;
    }

    /**
     * Full Constructor
     * @param map the Height Map to draw to the Canvas
     */
    public HeightMap(HeightMap map) {
        this.map = map.getPoints();
        this.elevationMax = map.getElevationMax();
        this.elevationMin = map.getElevationMin();
    }

    /**
     * Generates a Height Map that is, along an imaginary z-axis, centered around the median elevation, given the following parameters:
     * @param canvasWidth the width [x] of the canvas
     * @param canvasHeight the height [y] of the canvas
     * @param tileSize the height and width of each tile, or Point
     * @param elevationMax the maximum elevation [z] of the map
     * @param elevationMin the minimum elevation [z] of the map
     */
    public HeightMap(int canvasWidth, int canvasHeight, int tileSize, double elevationMax, double elevationMin) {
        this.elevationMax = elevationMax;
        this.elevationMin = elevationMin;

        for (int i=0; i < 256 ; i++) {
            P[256+i] = P[i] = PERMUTATION[i];
        }

        int numTilesX = canvasWidth / tileSize;
        int numTilesY = canvasHeight / tileSize;

        this.map = new Point[numTilesX * numTilesY];

//        for (int t = 0; t < numTilesX * numTilesY; t++) {
//            double x = t % numTilesX;
//            double y = (t - x) / numTilesX;
//            Random r = new Random();
//            x += r.nextDouble();
//            y += r.nextDouble();
//
//            this.map[t] = new Point(new double[]{x, y, lerp(noise(x, y, STRETCH_FACTOR), (elevationMin + elevationMax) / 2, elevationMax)}, tileSize);
//        }

        Random r1 = new Random();
        int i = r1.nextInt(numTilesX * numTilesY);
        System.out.println(i + " " + numTilesX * numTilesY);
        for (int t = 0; t < i; t++) {
            double x = t % numTilesX;
            double y = (t - x) / numTilesX;
            Random r2 = new Random();
            x += r2.nextDouble();
            y += r2.nextDouble();

            this.map[t] = new Point(new double[]{x, y, lerp(noise(x, y, STRETCH_FACTOR), (elevationMin + elevationMax) / 2, elevationMax)}, tileSize);
        }
        for (int t = i; t < numTilesX * numTilesY; t++) {
            double x = t % numTilesX;
            double y = (t - x) / numTilesX;
            Random r2 = new Random();
            x += r2.nextDouble();
            y += r2.nextDouble();

            this.map[t] = new Point(new double[]{x, y, lerp(noise(x, y, STRETCH_FACTOR), (elevationMin + elevationMax) / 2, elevationMax)}, tileSize);
        }
    }

    /**
     * Ken Perlin's Improved Noise Java Implementation (https://mrl.cs.nyu.edu/~perlin/noise/)
     * Adapted for 2D
     * @param x the x-coordinate on the map
     * @param y the y-coordinate on the map
     * @param stretch the factor by which adjacent points are smoothed
     * @return a value between -1.0 and 1.0 to represent the height of the terrain at (x, y)
     */
    private double noise(double x, double y, double stretch) {
        x /= stretch;
        y /= stretch;
        int X = (int)Math.floor(x) & 255, Y = (int)Math.floor(y) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);

        double u = fade(x),
               v = fade(y);

        int AA = P[P[X    ] + Y    ],
            AB = P[P[X    ] + Y + 1],
            BA = P[P[X + 1] + Y    ],
            BB = P[P[X + 1] + Y + 1];

        return lerp(v, lerp(u, grad(P[AA], x, y), grad(P[BA], x - 1, y)), lerp(u, grad(P[AB], x, y - 1), grad(P[BB], x - 1, y - 1)));
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    public static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    //Riven's Optimization (http://riven8192.blogspot.com/2010/08/calculate-perlinnoise-twice-as-fast.html)
    private double grad(int hash, double x, double y) {
        switch(hash & 0xF)
        {
            case 0x0:
            case 0x8:
                return  x + y;
            case 0x1:
            case 0x9:
                return -x + y;
            case 0x2:
            case 0xA:
                return  x - y;
            case 0x3:
            case 0xB:
                return -x - y;
            case 0x4:
            case 0xC:
                return  y + x;
            case 0x5:
            case 0xD:
                return -y + x;
            case 0x6:
            case 0xE:
                return  y - x;
            case 0x7:
            case 0xF:
                return -y - x;
            default: return 0; // never happens
        }
    }

    public Point[] getPoints() {
        return map;
    }

    public Point getPoint(int x, int y) {
        for (Point p : map) {
            if ((((int)Math.floor(p.getX())) * p.getTileSize() == x) && (((int)Math.floor(p.getY())) * p.getTileSize() == y)) {
                return p;
            }
        }
        return new Point();
    }

    public double getElevationMax() {
        return elevationMax;
    }

    public double getElevationMin() {
        return elevationMin;
    }
}
