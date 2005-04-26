/* This class is responsible for generating all of the code for the state's clock component */

package simse.codegenerator.stategenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ClockGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to generate into

	public ClockGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File clockFile = new File(directory, ("simse\\state\\Clock.java"));
		if(clockFile.exists())
		{
			clockFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(clockFile);
			writer.write("package simse.state;");
			writer.write(NEWLINE);
			writer.write("public class Clock");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private int time;");
			writer.write(NEWLINE);
			writer.write("private boolean stopped;");
			writer.write(NEWLINE);

			// constructor:
			writer.write("public Clock()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("time = 0;");
			writer.write(NEWLINE);
			writer.write("stopped = false;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			

			// methods:
			// "incrementTime" method:
			writer.write("public void incrementTime()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("time++;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "getTime" method:
			writer.write("public int getTime()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return time;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "isStopped" method:
			writer.write("public boolean isStopped()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return stopped;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "stop" method:
			writer.write("public void stop()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("stopped = true;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + clockFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
