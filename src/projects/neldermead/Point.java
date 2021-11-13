package projects.neldermead;

public class Point {

    private double x, y, z;
    private int tileWidth, tileHeight;

    /**
     * Empty Constructor
     */
    public Point() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.tileWidth = 0;
        this.tileHeight = 0;
    }

    /**
     * Full Constructor
     * @param x the x-coordinate of the Point
     * @param y the y-coordinate of the Point
     * @param z the z-coordinate of the Point
     * @param tileWidth the width of the Point
     * @param tileHeight the height of the Point
     */
    public Point(double x, double y, double z, int tileWidth, int tileHeight) {
       this.x = x;
       this.y = y;
       this.z = z;
       this.tileWidth = tileWidth;
       this.tileHeight = tileHeight;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }
}
