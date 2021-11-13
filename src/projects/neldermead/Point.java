package projects.neldermead;

public class Point {

    private double x, y, z;

    /**
     * Empty Constructor
     */
    public Point() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    /**
     * Full Constructor
     * @param x the x-coordinate of the Point
     * @param y the y-coordinate of the Point
     * @param z the z-coordinate of the Point
     */
    public Point(double x, double y, double z) {
       this.x = x;
       this.y = y;
       this.z = z;
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
}
