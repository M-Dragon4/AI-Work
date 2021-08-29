package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	/**
	 * Writes to the next open line of a file
	 * @param path the path to the file
	 * @param toWrite the String to be written to the file
	 */
	public static void Write(String path, String toWrite) {
		File file = new File(path);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.write(toWrite);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new file at the designated path
	 * @param path the path to the file
	 */
	public static void CreateFile(String path) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
	}
}
