/* This class is responsible for generating all of the code for the logic component of the simulation */

package simse.codegenerator.logicgenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.codegenerator.logicgenerator.dialoggenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class LogicGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private MiscUpdaterGenerator miscUGen; // generates MiscUpdater component
	private TriggerCheckerGenerator trigGen; // generates trigger checker component
	private DestroyerCheckerGenerator destGen; // generates destroyer checker component
	private MenuInputManagerGenerator menuGen; // generates menu input manager component
	private EmployeeParticipantSelectionDialogGenerator epsdGen; // generates employee participant selection dialogs
	private NonEmployeeParticipantSelectionDialogGenerator nepsdGen; // generates non-employee participant selection dialogs
	private ParticipantSelectionDialogsDriverGenerator psdDriverGen; // generates participant selection dialogs driver
	private ChooseActionToDestroyDialogGenerator catddGen; // generates choose action to destroy dialog
	private ChooseActionToJoinDialogGenerator catjdGen; // generates choose action to join dialog
	private ChooseRoleToPlayDialogGenerator crtpdGen; // generates choose role to play dialog
	private RuleExecutorGenerator ruleGen; // generates rule executor component
	private File directory; // directory to generate into

	public LogicGenerator(DefinedObjectTypes objTypes, DefinedActionTypes actTypes, File dir)
	{
		directory = dir;
		miscUGen = new MiscUpdaterGenerator(directory);
		trigGen = new TriggerCheckerGenerator(actTypes, directory);
		destGen = new DestroyerCheckerGenerator(actTypes, directory);
		menuGen = new MenuInputManagerGenerator(actTypes, directory);
		epsdGen = new EmployeeParticipantSelectionDialogGenerator(actTypes, objTypes, directory);
		nepsdGen = new NonEmployeeParticipantSelectionDialogGenerator(actTypes, objTypes, directory);
		psdDriverGen = new ParticipantSelectionDialogsDriverGenerator(actTypes, directory);
		catddGen = new ChooseActionToDestroyDialogGenerator(actTypes, directory);
		catjdGen = new ChooseActionToJoinDialogGenerator(actTypes, directory);
		crtpdGen = new ChooseRoleToPlayDialogGenerator(actTypes, directory);
		ruleGen = new RuleExecutorGenerator(actTypes, directory);
	}


	public void generate() // causes all of this component's sub-components to generate code
	{
		miscUGen.generate();
		trigGen.generate();
		destGen.generate();
		menuGen.generate();
		epsdGen.generate();
		nepsdGen.generate();
		psdDriverGen.generate();
		catddGen.generate();
		catjdGen.generate();
		crtpdGen.generate();
		ruleGen.generate();

		// generate outer logic component:
		File logicFile = new File(directory, ("simse\\logic\\Logic.java"));
		if(logicFile.exists())
		{
			logicFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(logicFile);
			writer.write("package simse.logic;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("public class Logic");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private MiscUpdater updater;");
			writer.write(NEWLINE);
			writer.write("private TriggerChecker trigChecker;");
			writer.write(NEWLINE);
			writer.write("private DestroyerChecker destChecker;");
			writer.write(NEWLINE);
			writer.write("private MenuInputManager menInputMgr;");
			writer.write(NEWLINE);
			writer.write("private RuleExecutor ruleEx;");
			writer.write(NEWLINE);			

			// constructor:
			writer.write("public Logic(State s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("updater = new MiscUpdater(state);");
			writer.write(NEWLINE);
			writer.write("ruleEx = new RuleExecutor(state);");
			writer.write(NEWLINE);			
			writer.write("trigChecker = new TriggerChecker(state, ruleEx);");
			writer.write(NEWLINE);
			writer.write("destChecker = new DestroyerChecker(state, ruleEx, trigChecker);");
			writer.write(NEWLINE);
			writer.write("menInputMgr = new MenuInputManager(state, trigChecker, destChecker, ruleEx);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// methods:

			// getMenuInputManager() method:
			writer.write("public MenuInputManager getMenuInputManager()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return menInputMgr;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// getTriggerChecker() method:
			writer.write("public TriggerChecker getTriggerChecker()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return trigChecker;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			

			// update() method:
			writer.write("public void update(JFrame mainGUI)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("updater.update();");
			writer.write(NEWLINE);
			writer.write("trigChecker.update(false, mainGUI);");
			writer.write(NEWLINE);
			writer.write("ruleEx.update(mainGUI, RuleExecutor.UPDATE_ALL_CONTINUOUS, null, null);");
			writer.write(NEWLINE);
			writer.write("destChecker.update(false, mainGUI);");
			writer.write(NEWLINE);			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + logicFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
