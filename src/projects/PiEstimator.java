package projects;

import java.util.Random;
import java.util.Scanner;

public class PiEstimator
{

	public static void main(String[] args)
	{
		Random rand = new Random();
		double tries = 0;
		double success = 0;
		long iterations = 0;
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Enter number of iterations: ");
		{
			iterations = in.nextLong();
		}
		for(int i = 0; i < iterations; i++)
		{
			double x = rand.nextDouble();
			double y = rand.nextDouble();
			tries += 1;
			if((x*x + y*y) <= 1)
			{
				success++;
			}
		}
		double pi = 4 * success / tries;
		System.out.println("Successes: " + success);
		System.out.println("Tries: " + tries);
		System.out.println(pi);
	}

}
