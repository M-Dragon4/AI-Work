package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer
{
	/**
	 * Writes to the next open line of a file
	 * @param path, toWrite
	 */
	public static void Write(String path, String toWrite)
	{
		File file = new File(path);
		
		try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true)))
		{
			bWriter.write(toWrite + "\n");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
