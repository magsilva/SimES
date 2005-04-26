/* This class is responsible for generating all of the code for the PopupListener class for the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class PopupListenerGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into

	public PopupListenerGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File pulFile = new File(directory, ("simse\\gui\\PopupListener.java"));
		if(pulFile.exists())
		{
			pulFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(pulFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.*;");
			writer.write(NEWLINE);;
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("public class PopupListener extends MouseAdapter");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);				

			// member variables:
			writer.write("JPopupMenu popup;");
			writer.write(NEWLINE);
			writer.write("boolean enabled;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// constructor:
			writer.write("public PopupListener(JPopupMenu popupMenu)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("popup = popupMenu;");
			writer.write(NEWLINE);			
			writer.write("enabled = true;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);		
			
			// isEnabled function:
			writer.write("public boolean isEnabled()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return enabled;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setEnabled function:
			writer.write("public void setEnabled(boolean e)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("enabled = e;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mousePressed function:
			writer.write("public void mousePressed(MouseEvent e)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("maybeShowPopup(e);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mouseReleased function:
			writer.write("public void mouseReleased(MouseEvent e)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("maybeShowPopup(e);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// maybeShowPopup function:
			writer.write("private void maybeShowPopup(MouseEvent e)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(e.isPopupTrigger() && isEnabled())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("popup.show(e.getComponent(), e.getX(), e.getY());");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write(CLOSED_BRACK);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + pulFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
