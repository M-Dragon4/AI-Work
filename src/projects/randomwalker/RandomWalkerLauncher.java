package projects.randomwalker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO: Have buttons and walker in different threads
 */
public class RandomWalkerLauncher {

    public static RandomWalker walker = new RandomWalker();

    public static void main(String[] args) {
        Thread t1 = new Thread(walker);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(t1);
    }
}
