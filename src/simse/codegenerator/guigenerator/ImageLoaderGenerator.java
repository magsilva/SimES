/* This class is responsible for generating all of the code for the ImageLoader class for the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ImageLoaderGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into

	public ImageLoaderGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File ilFile = new File(directory, ("simse\\gui\\ImageLoader.java"));
		if(ilFile.exists())
		{
			ilFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(ilFile);
			writer.write("// ImageLoader is a utility class that fetches images and returns them given a URL");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import java.awt.Image;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Toolkit;");
			writer.write(NEWLINE);
			writer.write("import java.net.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public class ImageLoader");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);				
			writer.write(NEWLINE);
			
			// getImageFromURL function:
			writer.write("public static Image getImageFromURL(String url)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("URL imgURL = ImageLoader.class.getResource(url);");
			writer.write(NEWLINE);
			writer.write("Image img = Toolkit.getDefaultToolkit().getImage(imgURL);");
			writer.write(NEWLINE);
			writer.write("return(img);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write(CLOSED_BRACK);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + ilFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
