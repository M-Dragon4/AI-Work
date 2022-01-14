package projects.neldermead;

public class Point {

    private double[] coordinates = new double[3];
    private int tileWidth, tileHeight;

    /**
     * Empty Constructor
     */
    public Point() {
        for (int i = 0; i < coordinates.length; i++) {
            this.coordinates[i] = 0.0;
        }
        this.tileWidth = 0;
        this.tileHeight = 0;
    }

    /**
     * Full Constructor
     * @param coordinates the coordinates of the Point within the Canvas
     * @param tileWidth the width of the Point on the Canvas
     * @param tileHeight the height of the Point on the Canvas
     */
    public Point(double[] coordinates, int tileWidth, int tileHeight) {
       this.coordinates = coordinates;
       this.tileWidth = tileWidth;
       this.tileHeight = tileHeight;
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
