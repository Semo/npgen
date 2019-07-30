package dev.semo.npgen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class UtilBase64File {

	public static String encodeFromFile(String imagePath) {
		File file = new File(imagePath);
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			String base64ImageString = "";
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64ImageString = Base64.getEncoder().encodeToString(imageData);
			return base64ImageString;
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return null;
	}

}
