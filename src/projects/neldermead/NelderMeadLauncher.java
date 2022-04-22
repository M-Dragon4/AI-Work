package projects.neldermead;

/**
 * This is a visual implementation of the Nelder Mead Algorithm for 2D. The objective of this implementation is to find
 * the highest region on a randomly generated surface (visualized as a topographic map).
 * Theoretically, this algorithm can be applied to an N-dimensional data set to find the region of best solution.
 * However, it is important to note that the region of best solution may only be local--there may exist a region of the
 * data that the algorithm was not led to that is better than the solution set it finds.
 */
public class NelderMeadLauncher {

    public static NelderMead nelder = new NelderMead();

    public static void main(String[] args) {
        Thread thread = new Thread(nelder);
        thread.start();
    }
}
