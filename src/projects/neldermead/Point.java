package projects.neldermead;

public class Point {

    private int x, y, z;

    /**
     * Empty Constructor
     */
    public Point() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Full Constructor
     * @param x the x-coordinate of the Point
     * @param y the y-coordinate of the Point
     * @param z the z-coordinate of the Point
     */
    public Point(int x, int y, int z) {
       this.x = x;
       this.y = y;
       this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
