package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Reader
{
	/**
	 * Reads a file and returns the text within it
	 * @param path
	 * @return text : the text of the file
	 * @throws IOException
	 */
	public static String Read(String path) throws IOException
	{
		File file = new File(path);
		
		FileReader reader = new FileReader(file);
		@SuppressWarnings("resource")
		BufferedReader bReader = new BufferedReader(reader);
		
		String text = "";
		String line = bReader.readLine();
		
		while (line != null)
		{
			text += line;
			line = bReader.readLine();
		}
		
		return text;
	}
	
	/**
	 * Reads a specific line of a file
	 * @param path, lineNumber
	 * @return text : the text of the line specified
	 * @throws IOException
	 */
	public static String ReadLine(String path, int lineNumber) throws IOException
	{
		File file = new File(path);
		
		FileReader reader = new FileReader(file);
		@SuppressWarnings("resource")
		BufferedReader bReader = new BufferedReader(reader);
		
		for(int i=0; i < lineNumber; i++)
		{
			bReader.readLine();
		}
		
		String text = bReader.readLine();
		
		return text;
	}
	
	/**
	 * Counts the number of lines of a file
	 * @param path
	 * @return lineCount : the number of lines
	 * @throws IOException
	 */
	public static int Counter(String path) throws IOException
	{
		File file = new File(path);
		
		@SuppressWarnings("resource")
		LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		while (lnr.skip(Long.MAX_VALUE) > 0);
		int lineCount = lnr.getLineNumber();
		
		return lineCount;
	}

}
