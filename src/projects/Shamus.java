package projects;

import util.Writer;
import util.Reader;
import util.QuantNormal;
import java.util.Scanner;


/**
 * This program is designed to predict when my friend Shamus will arrive during D-Block to pay a visit to my 
 * teacher (in minutes from the beginning of the period), and to test out my Normalization functions.
 * 
 * @author max4b
 *
 */
public class Shamus
{
	private static double normH = 1;
	private static double normL = 0;
	private static double datH = 84;
	private static double datL = 0;
	private static String path = "C:/Users/Maxwell/Desktop/AI-Work/src/files/shamus.txt";
	private static String[] timeString;
	private static double[] time;
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Time: ");
		String tArrival = in.next();
		
		Writer.Write(path, tArrival);
		try
		{
			timeString = new String[Reader.Counter(path)];
			for (int i=0; i < timeString.length; i++)
			{
				timeString[i] = Reader.ReadLine(path, i);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		double avg = 0;
		double sum = 0;
		time = new double[timeString.length];
		for (int i=0; i < timeString.length; i++)
		{
			time[i] = Double.parseDouble(timeString[i]);
			time[i] = QuantNormal.quantNormalizer(datH, datL, normH, normL, time[i]);
			
			sum += time[i];
		}
		
		avg = sum / time.length;
		
		System.out.println("Shamus will arrive approximately " + QuantNormal.quantDenormalizer(datH, datL, normH, normL, avg) + " minutes into D-Block.");
	}

}
