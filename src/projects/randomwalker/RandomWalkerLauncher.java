package projects.randomwalker;

/**
 * This is a Random Walker project to go along with The Coding Train's Nature of Code video series, but adapted for Java.
 */
public class RandomWalkerLauncher {

    public static RandomWalker walker = new RandomWalker();

    public static void main(String[] args) {
        Thread t1 = new Thread(walker);
        t1.start();
    }
}
