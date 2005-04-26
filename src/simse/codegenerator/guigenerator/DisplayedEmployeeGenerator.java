/* This class is responsible for generating all of the code for the DisplayedEmployee class for the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class DisplayedEmployeeGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into

	public DisplayedEmployeeGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File deFile = new File(directory, ("simse\\gui\\DisplayedEmployee.java"));
		if(deFile.exists())
		{
			deFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(deFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);			
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Image;");
			writer.write(NEWLINE);;
			writer.write("import javax.swing.JMenuItem;");
			writer.write(NEWLINE);
			writer.write("public class DisplayedEmployee");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);				

			// member variables:
			writer.write("private Employee userObject;");
			writer.write(NEWLINE);
			writer.write("private String userIconLocation;");
			writer.write(NEWLINE);
			writer.write("private Image userIcon;");
			writer.write(NEWLINE);
			writer.write("private boolean userDisplayed; // is this employee displayed on screen");
			writer.write(NEWLINE);
			writer.write("private boolean activated; // if this employee is a start state object, this is automatically true; if created by a rule, false");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// grid locations of the SimSEObject");
			writer.write(NEWLINE);
			writer.write("// <xLocation, yLocation> where <0, 0> is the top left tile");
			writer.write(NEWLINE);
			writer.write("private int xLocation;");
			writer.write(NEWLINE);
			writer.write("private int yLocation;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JMenuItem userMenu;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// constructor:
			writer.write("public DisplayedEmployee(Employee emp, String il, ActionListener a, boolean d, boolean ra, int x, int y)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("userObject = emp;");
			writer.write(NEWLINE);			
			writer.write("userIconLocation = il;");
			writer.write(NEWLINE);
			writer.write("if(userIconLocation != null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("userIcon = ImageLoader.getImageFromURL(il);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("activated = ra;");
			writer.write(NEWLINE);
			writer.write("setDisplayed(d);");
			writer.write(NEWLINE);
			writer.write("setXYLocations(x, y);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);		
			
			// getUserIcon function:
			writer.write("public Image getUserIcon()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return userIcon;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// setUserIcon function:
			writer.write("public void setUserIcon(String imageLocation)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("userIconLocation = imageLocation;");
			writer.write(NEWLINE);
			writer.write("userIcon = ImageLoader.getImageFromURL(userIconLocation);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);					

			// getXLocation function:
			writer.write("public int getXLocation()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return xLocation;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getYLocation function:
			writer.write("public int getYLocation()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return yLocation;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// isActivated function:
			writer.write("public boolean isActivated()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return activated;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// isDisplayed function:
			writer.write("public boolean isDisplayed()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return userDisplayed;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// setActivated function:
			writer.write("public void setActivated(boolean a)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("activated = a;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// setDisplayed function:
			writer.write("public void setDisplayed(boolean b)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("userDisplayed = b;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// setXYLocations function:
			writer.write("public void setXYLocations(int x, int y)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("xLocation = x;");
			writer.write(NEWLINE);
			writer.write("yLocation = y;");
			writer.write(NEWLINE);			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);	
			writer.write(NEWLINE);
			
			// checkXYLocations function:
			writer.write("public boolean checkXYLocations(int x, int y)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return xLocation == x && yLocation == y;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// getEmployee function:
			writer.write("// returns the Employee object associated with this DisplayedEmployee");
			writer.write(NEWLINE);
			writer.write("public Employee getEmployee()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return userObject;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			
			writer.write(CLOSED_BRACK);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + deFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
