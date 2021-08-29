package projects;

import util.RandomGenerator;
import util.Writer;

import java.io.IOException;
import java.util.Scanner;

/**
 * Tester class for the Random Generators to see how random they really are
 */
public class RandomGeneratorTester {
    private static final String VALUE_PATH = "C:/Users/Maxwell/Desktop/AI-Work/src/files/value.txt";
    private static final String RANGE_PATH = "C:/Users/Maxwell/Desktop/AI-Work/src/files/range.txt";
    private static final String VALUE = "value";
    private static final String RANGE = "range";
    private static final String INTEGER = "int";
    private static final String FLOATING = "float";
    private static long numVal = 0;
    private static double max = 0;
    private static double min = 0;

    public static void main(String[] args) {
        RandomGenerator rand = new RandomGenerator();

        @SuppressWarnings("resource")
        Scanner in = new Scanner(System.in);
        System.out.println("Type 'value' for one value or 'range' for a range of numbers: ");
        if (in.next().equals(VALUE)) {
            try {
                Writer.CreateFile(VALUE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Number of values: ");
            numVal = in.nextLong();
            System.out.println("Type 'int' for integers or 'float' for floating-point numbers: ");
            if (in.next().equals(INTEGER)) {
                System.out.println("Maximum: ");
                max = (int) in.nextDouble();
                System.out.println("Minimum: ");
                min = (int) in.nextDouble();
                for (long i = 0; i < numVal; i++) {
                    rand.makeValue(max, min, false);
                    int value = (int) rand.getValue();
                    Writer.Write(VALUE_PATH, Integer.toString(value));
                }
            } else if (in.next().equals(FLOATING)) {
                System.out.println("Maximum: ");
                max = in.nextDouble();
                System.out.println("Minimum: ");
                min = in.nextDouble();
                for (long i = 0; i < numVal; i++) {
                    rand.makeValue(max, min, true);
                    double value = rand.getValue();
                    Writer.Write(VALUE_PATH, Double.toString(value));
                }
            }
            System.out.println("See /src/files/value.txt for generated values.");
        } else if (in.next().equals(RANGE)) {
            try {
                Writer.CreateFile(RANGE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Number of values: ");
            numVal = in.nextInt();
            System.out.println("Maximum: ");
            max = in.nextInt();
            System.out.println("Minimum: ");
            min = in.nextInt();
            System.out.println("Type 'int' for integers or 'float' for floating-point numbers: ");
            if (in.next().equals(INTEGER)) {
                rand.makeRange(max, min, numVal, false);
                double range[] = rand.getRange();
                //print range to text file
            } else if (in.next().equals(FLOATING)) {
                rand.makeRange(max, min, numVal, true);
                double range[] = rand.getRange();
                //print range to text file
            }
        }
    }
}
