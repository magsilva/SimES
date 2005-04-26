/* This class is responsible for generating all of the code for the engine component of the simulation */

package simse.codegenerator.enginegenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class EngineGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to generate into
	private CreatedObjects createdObjs; // start state objects

	private StartingNarrativeDialogGenerator sndg;

	public EngineGenerator(CreatedObjects cObjs, File dir)
	{
		directory = dir;
		createdObjs = cObjs;
		sndg = new StartingNarrativeDialogGenerator(createdObjs, directory);
	}


	public void generate() // causes the engine component to be generated
	{
		// generate starting narrative dialog:
		sndg.generate();

		// generate Engine:
		File engineFile = new File(directory, ("simse\\engine\\Engine.java"));
		if(engineFile.exists())
		{
			engineFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(engineFile);
			writer.write("package simse.engine;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.logic.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.gui.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);


			writer.write("public class Engine implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private Logic logic;");
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private SimSEGUI gui;");
			writer.write(NEWLINE);
			writer.write("private static int numSteps;");
			writer.write(NEWLINE);
			writer.write("private boolean stopClock;");
			writer.write(NEWLINE);
			writer.write("private boolean stopAtEvents;");
			writer.write(NEWLINE);
			writer.write("private javax.swing.Timer timer;");
			writer.write(NEWLINE);



			// constructor:
			writer.write("public Engine(Logic l, State s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("logic = l;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			// startup script: go through each objects in the start state, create it, and add it to the simulation:
			Vector objs = createdObjs.getAllObjects();
			for(int i=0; i<objs.size(); i++)
			{
				StringBuffer strToWrite = new StringBuffer();
				SimSEObject tempObj = (SimSEObject)objs.elementAt(i);
				String objTypeName = getUpperCaseLeading(tempObj.getSimSEObjectType().getName());
				strToWrite.append(objTypeName + " a" + i + " = new " + objTypeName + "(");
				Vector atts = tempObj.getSimSEObjectType().getAllAttributes();
				if(atts.size() == tempObj.getAllAttributes().size()) // all attributes are instantiated
				{
					boolean validObj = true;
					// go through all attributes:
					for(int j=0; j<atts.size(); j++)
					{
						Attribute att = (Attribute)atts.elementAt(j);
						InstantiatedAttribute instAtt = tempObj.getAttribute(att.getName()); // get the corresponding instantiated attribute
						if(instAtt == null) // no corresponding instantiated attribute
						{
							validObj = false;
							break;
						}
						if(instAtt.isInstantiated()) // attribute has a value
						{
							if(instAtt.getAttribute().getType() == AttributeTypes.STRING)
							{
								strToWrite.append("\"" + instAtt.getValue() + "\"");
							}
							else // boolean, int, or double
							{
								strToWrite.append(instAtt.getValue().toString());
							}
							if(j < (atts.size() - 1)) // not on last element
							{
								strToWrite.append(", ");
							}
						}
						else // attribute does not have a value -- invalidates entire object
						{
							validObj = false;
							break;
						}
					}
					if(validObj) // if valid, finish writing:
					{
						writer.write(strToWrite + ");");
						writer.write(NEWLINE);
						writer.write("state.get" + SimSEObjectTypeTypes.getText(tempObj.getSimSEObjectType().getType()) + "StateRepository().get"
							+ objTypeName + "StateRepository().add(a" + i + ");");
						writer.write(NEWLINE);
					}
				}
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// methods:

			// giveGUI method:
			writer.write("public void giveGUI(SimSEGUI g)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("gui = g;");
			writer.write(NEWLINE);
			writer.write("StartingNarrativeDialog snd = new StartingNarrativeDialog(gui);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// actionPerformed method:
			writer.write("public void actionPerformed(ActionEvent ae)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(isRunning())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("ClockPanel.setAdvClockImage();");
			writer.write(NEWLINE);
			writer.write("if(state.getClock().isStopped())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("numSteps = 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("gui.getAttributePanel().setGUIChanged();");
			writer.write(NEWLINE);
			writer.write("logic.update(gui);");
			writer.write(NEWLINE);
			writer.write("gui.update();");
			writer.write(NEWLINE);
			writer.write("numSteps--;");
			writer.write(NEWLINE);
			writer.write("if(stopAtEvents && gui.getWorld().overheadTextDisplayed())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("stopClock = true;");
			writer.write(NEWLINE);
			writer.write("numSteps = 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("ClockPanel.resetAdvClockImage();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// isRunning method
			writer.write("public boolean isRunning()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return numSteps > 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// setStopAtEvents method
			writer.write("public void setStopAtEvents(boolean t)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("stopClock = false;");
			writer.write(NEWLINE);
			writer.write("stopAtEvents = t;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// setSteps method
			writer.write("public void setSteps(int ns)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(timer == null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("timer = new javax.swing.Timer(50,this);");
			writer.write(NEWLINE);
			writer.write("timer.start();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("numSteps += ns;");
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// stop method
			writer.write("public void stop()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("numSteps = 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// stopClock method
			writer.write("public boolean stopClock()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return stopClock;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + engineFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
