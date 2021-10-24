package util;

import java.util.Random;

public class RandomGenerator {

	private Random random = new Random();
	private double[] range;
	private double value;

	/**
	 * Empty Constructor
	 */
	public RandomGenerator() {
		this.range = null;
		this.value = 0;
	}

	/**
	 * Constructor for just generating a range
	 * @param range the array to store the generated numbers
	 */
	public RandomGenerator(double[] range) {
		this.range = range;
	}

	/**
	 * Constructor for just generating a value
	 * @param value the variable to store the generated number
	 */
	public RandomGenerator(double value) {
		this.value = value;
	}

	/**
	 * Constructor for generating a range and a value
	 * @param range the array to store the generated numbers
	 * @param value value the variable to store the generated number
	 */
	public RandomGenerator(double[] range, double value) {
		this.range = range;
		this.value = value;
	}

	/**
	 * Generates a 1D array of pseudo-random numbers in a given range
	 * @param rH       the maximum range value
	 * @param rL       the minimum range value
	 * @param n        the amount of numbers you want to print
	 * @param floating true if you want an array of floating point numbers; set to false if you want an array of integers
	 */
	public void makeRange(double rH, double rL, long n, boolean floating) {
		range = new double[(int) n];
		if (floating) {
			for (int i = 0; i < n; i++) {
				range[i] = random.nextDouble() * (rH - rL) + rL;
			}
		} else {
			for (int i = 0; i < n; i++) {
				range[i] = Math.round(random.nextDouble() * (rH - rL) + rL);
			}
		}
	}

	/**
	 * Generates a pseudo-random number from a given range across a Semi-Uniform Distribution
	 * NOTE: This method has the disadvantage of the Java RNG: The extrema are quite unlikely to be rolled; Use makeUniformValue instead
	 * @param rH		the maximum range value
	 * @param rL		the minimum range value
	 * @param floating	true if you want a floating point number; set to false if you want an integer
	 */
	public void makeValue(double rH, double rL, boolean floating) {
		value = 0;
		if (floating) {
				value = random.nextDouble() * (rH - rL) + rL;
		} else {
				value = Math.round(random.nextDouble() * (rH - rL) + rL);
		}
	}

	/**
	 * Generates a pseudo-random number from a given range across a Uniform Distribution; extends the range to account for LCG bias
	 * @param rH		the maximum range value
	 * @param rL		the minimum range value
	 * @param floating	true if you want a floating point number; set to false if you want an integer
	 */
	public void makeUniformValue(double rH, double rL, boolean floating) {
		boolean inRange = false;
		value = 0;
		if (floating) {
			while (!inRange) {
				value = random.nextDouble() * ((rH + 1) - (rL - 1)) + (rL - 1);
				if (value >= rL && value <= rH) {
					break;
				}
			}
		} else {
			while (!inRange) {
				value = Math.round(random.nextDouble() * ((rH + 1) - (rL - 1)) + (rL - 1));
				if (value >= rL && value <= rH) {
					break;
				}
			}
		}
	}

	/**
	 * Generates a pseudo-random number from a given Gaussian Distribution
	 * @param stddev	the standard deviation of the distribution
	 * @param mean		the mean of the distribution
	 * @param floating	true if you want a floating point number; set to false if you want an integer
	 */
	public void makeGaussianValue(double stddev, double mean, boolean floating) {
		value = 0;
		if (floating) {
			value = random.nextGaussian();
			value *= stddev;
			value += mean;
		} else {
			value = random.nextGaussian();
			value *= stddev;
			value += mean;
			value = Math.round(value);
		}
	}

	public double[] getRange() {
		return range;
	}

	public double getValue() {
		return value;
	}
}