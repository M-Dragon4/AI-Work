package util;

public class QuantNormal
{
	
	/**
	 * Normalizes a value based upon an estimation range
	 * @param datHigh
	 * @param datLow
	 * @param normHigh
	 * @param normLow
	 * @param value
	 * @return the normalized value
	 */
	public static double quantNormalizer(double datHigh, double datLow, double normHigh, double normLow, double value)
	{
		value = ((value-datLow)*(normHigh-normLow)) / (datHigh-datLow) + normLow;
		
		return value;
	}
	
	/**
	 * Denormalizes a normalized value based upon a given estimation range
	 * @param datHigh
	 * @param datLow
	 * @param normHigh
	 * @param normLow
	 * @param value
	 * @return the original value
	 */
	public static double quantDenormalizer(double datHigh, double datLow, double normHigh, double normLow, double value)
	{
		value = ((datLow-datHigh)*value - (normHigh*datLow) + (datHigh*normLow)) / (normLow-normHigh);
		
		return value;
	}
}
