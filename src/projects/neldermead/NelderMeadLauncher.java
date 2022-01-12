package projects.neldermead;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topographic map)
 */
public class NelderMeadLauncher {

    public static NelderMead nelder = new NelderMead();

    public static void main(String[] args) {
        Thread thread = new Thread(nelder);
        thread.start();
    }
}
