package projects.neldermead;

import java.util.Random;

/**
 * TODO: Play around with STRETCH_FACTOR to find the best value for it
 */
public class HeightMap {

    private final double STRETCH_FACTOR = 32.0;
    private Point[] map;
    private double elevationMax, elevationMin;

    private final int[] P = new int[512], PERMUTATION = new int[256];

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
        permute();

        this.elevationMax = elevationMax;
        this.elevationMin = elevationMin;

        for (int i=0; i < 256 ; i++) {
            P[256+i] = P[i] = PERMUTATION[i];
        }

        int numTilesX = canvasWidth / tileSize;
        int numTilesY = canvasHeight / tileSize;

        this.map = new Point[numTilesX * numTilesY];

        Random r = new Random();
        for (int t = 0; t < numTilesX * numTilesY; t++) {
            double x = t % numTilesX;
            double y = (t - x) / numTilesX;
            x += r.nextDouble();
            y += r.nextDouble();

            this.map[t] = new Point(new double[]{x, y, lerp(noise(x, y, STRETCH_FACTOR), (elevationMin + elevationMax) / 2, elevationMax)}, tileSize);
        }
    }

    /**
     * Fills the PERMUTATION array with the numbers 0 to 255 in a random order without repetition
     */
    public void permute() {
        for (int i = 0; i < PERMUTATION.length; i++) {
            PERMUTATION[i] = i;
        }

        Random r = new Random();
        int rIndex, rIndexVal;
        for (int i = 0; i < PERMUTATION.length; i++) {
            rIndex = r.nextInt(PERMUTATION.length);
            rIndexVal = PERMUTATION[rIndex];
            PERMUTATION[rIndex] = PERMUTATION[i];
            PERMUTATION[i] = rIndexVal;
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
