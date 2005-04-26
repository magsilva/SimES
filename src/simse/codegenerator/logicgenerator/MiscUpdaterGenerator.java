/* This class is responsible for generating all of the code for the logic's MiscUpdater component, which is responsible for doing
 	various updating tasks like clearing employee menus and overhead texts, and incrementing action times */

package simse.codegenerator.logicgenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.codegenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class MiscUpdaterGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	*/

	private File directory; // directory to generate into
	private File muFile; // file to generate

	public MiscUpdaterGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		try
		{
			muFile = new File(directory, ("simse\\logic\\MiscUpdater.java"));
			if(muFile.exists())
			{
				muFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(muFile);
			writer.write("package simse.logic;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.actions.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("public class MiscUpdater");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("public MiscUpdater(State s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("// clear menus and overhead texts:");
			writer.write(NEWLINE);
			writer.write("Vector employees = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<employees.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("((Employee)employees.elementAt(i)).clearOverheadText();");
			writer.write(NEWLINE);
			writer.write("((Employee)employees.elementAt(i)).clearMenu();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// update actions' time elapsed:");
			writer.write(NEWLINE);
			writer.write("Vector actions = state.getActionStateRepository().getAllActions();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<actions.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action act = (simse.adts.actions.Action)actions.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("act.incrementTimeElapsed();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("// update clock:");
			writer.write(NEWLINE);
			writer.write("state.getClock().incrementTime();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + muFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
