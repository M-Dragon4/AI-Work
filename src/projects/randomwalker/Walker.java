package projects.randomwalker;

import util.RandomGenerator;

public class Walker {
    private int x, y;
    private final int WALKER_WIDTH = 4;
    private final int WALKER_HEIGHT = 4;
    private final int ORIGIN_X = RandomWalker.ORIGIN_X;
    private final int ORIGIN_Y = RandomWalker.ORIGIN_Y;
    private final int CANVAS_WIDTH = RandomWalker.CANVAS_WIDTH;
    private final int CANVAS_HEIGHT = RandomWalker.CANVAS_HEIGHT;
    private final int CENTER_X = CANVAS_WIDTH / 2;
    private final int CENTER_Y = CANVAS_HEIGHT / 2;
    private RandomGenerator randG;

    public Walker() {
        this.x = CENTER_X;
        this.y = CENTER_Y;
        this.randG = new RandomGenerator();
    }

    public Walker(int x, int y) {
        this.x = x;
        this.y = y;
        this.randG = new RandomGenerator();
    }

    public void walk() {
        this.randG.makeUniformValue(3, 0, false);
        System.out.println(this.randG.getValue());
        int choice = (int) this.randG.getValue();

        if (isAtTopEdge()) y += WALKER_HEIGHT;
        else if (isAtBottomEdge()) y -= WALKER_HEIGHT;
        else if (isAtRightEdge()) x -= WALKER_WIDTH;
        else if (isAtLeftEdge()) x += WALKER_WIDTH;
        else {
            if(choice == 0) {
                x += WALKER_WIDTH;
            }
            else if (choice == 1) {
                x -= WALKER_WIDTH;
            }
            else if (choice == 2) {
                y += WALKER_HEIGHT;
            }
            else {
                y -= WALKER_HEIGHT;
            }
        }
    }

    public boolean isAtTopEdge() {
        if (y - WALKER_HEIGHT < ORIGIN_Y) return true;
        else return false;
    }

    public boolean isAtBottomEdge() {
        if (y + WALKER_HEIGHT > CANVAS_HEIGHT - WALKER_HEIGHT) return true;
        else return false;
    }

    public boolean isAtRightEdge() {
        if (x + WALKER_WIDTH > CANVAS_WIDTH - WALKER_WIDTH) return true;
        else return false;
    }

    public boolean isAtLeftEdge() {
        if (x - WALKER_WIDTH < ORIGIN_X) return true;
        else return false;
    }

    public int getWalkerX() {
        return x;
    }
    public int getWalkerY() {
        return y;
    }

    public int getWalkerWidth() {
        return WALKER_WIDTH;
    }

    public int getWalkerHeight() {
        return WALKER_HEIGHT;
    }

    public Walker getWalker() {
        return this;
    }
}
