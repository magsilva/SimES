/* This class is responsible for generating all of the code for the state component of the simulation */

package simse.codegenerator.stategenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.startstatebuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class StateGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private ADTGenerator adtGen; // generates ADTs
	private RepositoryGenerator repGen; // generates state repositories
	private ClockGenerator clockGen; // generates clock
	private File directory; // directory to generate into

	public StateGenerator(DefinedObjectTypes objTypes, DefinedActionTypes actTypes, File dir)
	{
		directory = dir;
		adtGen = new ADTGenerator(objTypes, actTypes, directory);
		repGen = new RepositoryGenerator(objTypes, actTypes, directory);
		clockGen = new ClockGenerator(directory);
	}


	public void generate() // causes all of this component's sub-components to generate code
	{
		adtGen.generate();
		repGen.generate();
		clockGen.generate();

		// generate outer state component:
		File stateFile = new File(directory, ("simse\\state\\State.java"));
		if(stateFile.exists())
		{
			stateFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(stateFile);
			writer.write("package simse.state;");
			writer.write(NEWLINE);
			writer.write("public class State");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private EmployeeStateRepository esr;");
			writer.write(NEWLINE);
			writer.write("private ArtifactStateRepository asr;");
			writer.write(NEWLINE);
			writer.write("private ToolStateRepository tsr;");
			writer.write(NEWLINE);
			writer.write("private ProjectStateRepository psr;");
			writer.write(NEWLINE);
			writer.write("private CustomerStateRepository csr;");
			writer.write(NEWLINE);
			writer.write("private ActionStateRepository actsr;");
			writer.write(NEWLINE);
			writer.write("private Clock clock;");
			writer.write(NEWLINE);

			// constructor:
			writer.write("public State()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("esr = new EmployeeStateRepository();");
			writer.write(NEWLINE);
			writer.write("asr = new ArtifactStateRepository();");
			writer.write(NEWLINE);
			writer.write("tsr = new ToolStateRepository();");
			writer.write(NEWLINE);
			writer.write("psr = new ProjectStateRepository();");
			writer.write(NEWLINE);
			writer.write("csr = new CustomerStateRepository();");
			writer.write(NEWLINE);
			writer.write("actsr = new ActionStateRepository();");
			writer.write(NEWLINE);
			writer.write("clock = new Clock();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// methods:

			// getEmployeeStateRepository() method:
			writer.write("public EmployeeStateRepository getEmployeeStateRepository()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return esr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// getArtifactStateRepository() method:
			writer.write("public ArtifactStateRepository getArtifactStateRepository()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return asr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// getToolStateRepository() method:
			writer.write("public ToolStateRepository getToolStateRepository()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return tsr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// getProjectStateRepository() method:
			writer.write("public ProjectStateRepository getProjectStateRepository()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return psr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// getCustomerStateRepository() method:
			writer.write("public CustomerStateRepository getCustomerStateRepository()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return csr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// getActionStateRepository() method:
			writer.write("public ActionStateRepository getActionStateRepository()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return actsr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// getClock() method:
			writer.write("public Clock getClock()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return clock;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + stateFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
