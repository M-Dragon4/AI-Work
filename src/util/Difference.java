package util;

import java.util.Scanner;

public class Difference
{
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the observed value: ");
		double numOne = in.nextDouble();
		System.out.println("Enter the expected value: ");
		double numTwo = in.nextDouble();
		
		double x = difference(numOne, numTwo);
		
		System.out.println(x + "%");
	}

	/**
	 * Method to find the percent difference between two numbers, commonly an observed and expected value.
	 * @param numOne the observed value
	 * @param numTwo the expected value
	 * @return the percent difference between the two numbers entered.
	 */
	private static double difference(double numOne, double numTwo)
	{
		Math.abs(numOne); Math.abs(numTwo);
		
		double dif = numOne - numTwo;
		double av = (numOne + numTwo)/2;
		
		double quo = dif/av;
		double per = Math.abs(quo * 100);
		
		return per;
	}
}
