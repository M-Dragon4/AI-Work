package projects.neldermead;

public class Point {

    private double[] coordinates = new double[3];
    private int tileSize;

    /**
     * Empty Constructor
     */
    public Point() {
        for (int i = 0; i < coordinates.length; i++) {
            this.coordinates[i] = 0.0;
        }
        this.tileSize = 0;
    }

    /**
     * Full Constructor
     * @param coordinates the coordinates of the Point within the Canvas
     * @param tileSize the height and width of the Point on the Canvas
     */
    public Point(double[] coordinates, int tileSize) {
       this.coordinates = coordinates;
       this.tileSize = tileSize;
    }

    public double getX() {
        return coordinates[0];
    }

    public void setX(double x) {
        this.coordinates[0] = x;
    }

    public double getY() {
        return coordinates[1];
    }

    public void setY(double y) {
        this.coordinates[1] = y;
    }

    public double getZ() {
        return coordinates[2];
    }

    public void setZ(double z) {
        this.coordinates[2] = z;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
}
