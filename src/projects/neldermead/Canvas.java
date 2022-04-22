package projects.neldermead;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {

    //Colors to represent at what percentage between the minimum and maximum elevation that the Point lies
    //Rounded down to the closest least multiple of five
    private final Color ZERO = new Color(75, 128, 38);
    private final Color FIVE = new Color(103, 143, 54);
    private final Color TEN = new Color(141, 164, 68);
    private final Color FIFTEEN = new Color(153, 181, 79);
    private final Color TWENTY = new Color(177, 195, 93);
    private final Color TWENTYFIVE = new Color(200, 211, 107);
    private final Color THIRTY = new Color(220, 223, 110);
    private final Color THIRTYFIVE = new Color(234, 228, 118);
    private final Color FORTY = new Color(237, 223, 124);
    private final Color FORTYFIVE = new Color(239, 220, 115);
    private final Color FIFTY = new Color(237, 212, 109);
    private final Color FIFTYFIVE = new Color(237, 205, 102);
    private final Color SIXTY = new Color(235, 196, 103);
    private final Color SIXTYFIVE = new Color(232, 188, 99);
    private final Color SEVENTY = new Color(230, 180, 93);
    private final Color SEVENTYFIVE = new Color(220, 165, 101);
    private final Color EIGHTY = new Color(210, 149, 95);
    private final Color EIGHTYFIVE = new Color(187, 125, 78);
    private final Color NINETY = new Color(186, 114, 70);
    private final Color NINETYFIVE = new Color(177, 102, 71);
    private final Color HUNDRED = new Color(161, 89, 67);

    private final int RESET_CODE = NelderMead.RESET_CODE;
    private final int RECONFIGURE_CODE = NelderMead.RECONFIGURE_CODE;
    private HeightMap map;
    private Simplex simplex;

    public Canvas() {
        this.map = new HeightMap();
        this.simplex = new Simplex();
        this.setBackground(Color.WHITE);
    }

    /**
     * Full Constructor
     * @param map the Height Map to be drawn
     * @param simplex the Simplex to be drawn
     */
    public Canvas(HeightMap map, Simplex simplex) {
        this.map = map;
        this.simplex = simplex;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(map.getPoints() == null) return;
        for(Point p : map.getPoints()) {
            int elevation = (int) Math.floor(100 * ((p.getZ() - map.getElevationMin()) / (map.getElevationMax() - map.getElevationMin())));
            elevation -= elevation % 5;

            switch (elevation) {
                case 0: g2d.setColor(ZERO);
                    break;
                case 5: g2d.setColor(FIVE);
                    break;
                case 10: g2d.setColor(TEN);
                    break;
                case 15: g2d.setColor(FIFTEEN);
                    break;
                case 20: g2d.setColor(TWENTY);
                    break;
                case 25: g2d.setColor(TWENTYFIVE);
                    break;
                case 30: g2d.setColor(THIRTY);
                    break;
                case 35: g2d.setColor(THIRTYFIVE);
                    break;
                case 40: g2d.setColor(FORTY);
                    break;
                case 45: g2d.setColor(FORTYFIVE);
                    break;
                case 50: g2d.setColor(FIFTY);
                    break;
                case 55: g2d.setColor(FIFTYFIVE);
                    break;
                case 60: g2d.setColor(SIXTY);
                    break;
                case 65: g2d.setColor(SIXTYFIVE);
                    break;
                case 70: g2d.setColor(SEVENTY);
                    break;
                case 75: g2d.setColor(SEVENTYFIVE);
                    break;
                case 80: g2d.setColor(EIGHTY);
                    break;
                case 85: g2d.setColor(EIGHTYFIVE);
                    break;
                case 90: g2d.setColor(NINETY);
                    break;
                case 95: g2d.setColor(NINETYFIVE);
                    break;
                case 100: g2d.setColor(HUNDRED);
                    break;
                default: g2d.setColor(Color.WHITE); //Should never get called
                    break;
            }

            g2d.fillRect(((int)Math.floor(p.getX())) * p.getTileSize(), ((int)Math.floor(p.getY())) * p.getTileSize(), p.getTileSize(), p.getTileSize());
        }
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine((int)simplex.getPoint(3).getX() * simplex.getPoint(3).getTileSize(), (int)simplex.getPoint(3).getY() * simplex.getPoint(3).getTileSize(),
                (int)simplex.getPoint(2).getX() * simplex.getPoint(2).getTileSize(), (int)simplex.getPoint(2).getY() * simplex.getPoint(2).getTileSize());
        g2d.drawLine((int)simplex.getPoint(2).getX() * simplex.getPoint(2).getTileSize(), (int)simplex.getPoint(2).getY() * simplex.getPoint(2).getTileSize(),
                (int)simplex.getPoint(1).getX() * simplex.getPoint(1).getTileSize(), (int)simplex.getPoint(1).getY() * simplex.getPoint(1).getTileSize());
        g2d.drawLine((int)simplex.getPoint(1).getX() * simplex.getPoint(1).getTileSize(), (int)simplex.getPoint(1).getY() * simplex.getPoint(1).getTileSize(),
                (int)simplex.getPoint(3).getX() * simplex.getPoint(3).getTileSize(), (int)simplex.getPoint(3).getY() * simplex.getPoint(3).getTileSize());
    }

    /**
     * Resets the Simplex to the initial position of this iteration
     */
    public void reset() {
        this.simplex.initialize(RESET_CODE);
        this.repaint();
    }

    /**
     * Reconfigures the Simplex's initial position to new coordinates
     */
    public void reconfigure() {
        this.simplex.initialize(RECONFIGURE_CODE);
        this.repaint();
    }

    /**
     * Regenerates the Height Map and draws a new Simplex onto it
     * @param map the new Height Map
     */
    public void regenerate(HeightMap map) {
        this.map = map;
        this.simplex.initialize(RECONFIGURE_CODE);
        this.repaint();
    }

    public HeightMap getHeightMap() {
        return map;
    }

    public void setHeightMap(HeightMap map) {
        this.map = map;
    }

    public Simplex getSimplex() {
        return simplex;
    }

    public void setSimplex(Simplex simplex) {
        this.simplex = simplex;
    }
}
