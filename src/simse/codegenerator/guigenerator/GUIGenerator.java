/* This class is responsible for generating all of the code for the SimSEGUI component of the simulation */

package simse.codegenerator.guigenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.graphicsbuilder.*;
import simse.modelbuilder.mapeditor.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class GUIGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private ImageLoaderGenerator imageLoaderGen; // generates the image loader
	private ClockPanelGenerator clockPanelGen; // generates the clock panel
	private TabPanelGenerator tabPanelGen; // generates the tab panel
	private LogoPanelGenerator logoPanelGen; // generates the logo panel
	private AttributePanelGenerator attPanelGen; // generates the attribute panel
	private ActionPanelGenerator actPanelGen; // generates the action panel
	private PopupListenerGenerator popupListGen; // generates the PopupListener class
	private DisplayedEmployeeGenerator dispEmpGen; // generates the DisplayedEmployee class
	private MapDataGenerator mapDataGen; // generates the MapData class
	private SimSEMapGenerator ssmGen; // generates the SimSEMap class
	private WorldGenerator worldGen; // generates the world
	private AtAGlanceFramesGenerator glanceFramesGen; // generates the At-A-Glance frames for each object meta-type
	private AtAGlanceTableModelGenerator glanceTblModGen; // generates the At-A-Glance tables for each object type
	private SimSEAboutDialogGenerator aboutDialogGen; // generates the About Dialog when you click the SimSE logo
	private File directory; // directory to generate into
	String panelsImageDir; // directory of images for panels
	String worldImageDir; // directory of images for world

	public GUIGenerator(DefinedObjectTypes objTypes, CreatedObjects objs, DefinedActionTypes acts, File iconDir, Hashtable stsObjs,
		Hashtable ruleObjs, TileData[][] map, ArrayList userDatas, File imgDir,	File dir)
	{
		directory = dir;
		worldImageDir = imgDir.getPath();
		Hashtable allObjsToImages = new Hashtable();
		allObjsToImages.putAll(stsObjs);
		allObjsToImages.putAll(ruleObjs);
		panelsImageDir = iconDir.getPath();
		imageLoaderGen = new ImageLoaderGenerator(dir);
		tabPanelGen = new TabPanelGenerator(objTypes, allObjsToImages, dir);
		clockPanelGen = new ClockPanelGenerator(dir);
		logoPanelGen = new LogoPanelGenerator(dir);
		attPanelGen = new AttributePanelGenerator(objTypes, dir);
		actPanelGen = new ActionPanelGenerator(objTypes, acts, dir);
		popupListGen = new PopupListenerGenerator(dir);
		dispEmpGen = new DisplayedEmployeeGenerator(dir);
		mapDataGen = new MapDataGenerator(dir);
		ssmGen = new SimSEMapGenerator(objTypes, objs, allObjsToImages, map, userDatas, dir);
		worldGen = new WorldGenerator(dir);
		glanceFramesGen = new AtAGlanceFramesGenerator(objTypes, dir);
		glanceTblModGen = new AtAGlanceTableModelGenerator(objTypes, dir);
		aboutDialogGen = new SimSEAboutDialogGenerator(dir);
	}


	public void generate() // causes all of this component's sub-components to generate code
	{
		copyDir(panelsImageDir, (directory.getPath() + "\\simse\\gui\\" + (new File(panelsImageDir)).getName()));
		copyDir(worldImageDir, (directory.getPath() + "\\simse\\gui\\" + (new File(panelsImageDir)).getName()));
		imageLoaderGen.generate();
		clockPanelGen.generate();
		tabPanelGen.generate();
		logoPanelGen.generate();
		attPanelGen.generate();
		actPanelGen.generate();
		popupListGen.generate();
		dispEmpGen.generate();
		mapDataGen.generate();
		ssmGen.generate();
		worldGen.generate();
		glanceFramesGen.generate();
		glanceTblModGen.generate();
		aboutDialogGen.generate();
		generateMainGUI();
	}


	private void generateMainGUI() // generates the SimSEGUI class
	{
		File mainGUIFile = new File(directory, ("simse\\gui\\SimSEGUI.java"));
		if(mainGUIFile.exists())
		{
			mainGUIFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(mainGUIFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.logic.*;");
			writer.write(NEWLINE);
			writer.write("import simse.engine.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Dimension;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.text.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Color;");
			writer.write(NEWLINE);
			writer.write("import java.io.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public class SimSEGUI extends JFrame");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private TabPanel tabPanel;");
			writer.write(NEWLINE);
			writer.write("private ClockPanel clockPanel;");
			writer.write(NEWLINE);
			writer.write("private AttributePanel attribPanel;");
			writer.write(NEWLINE);
			writer.write("private ActionPanel actionPanel;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Logic logic;");
			writer.write(NEWLINE);
			writer.write("private Engine engine;");
			writer.write(NEWLINE);
			writer.write("private World world;");
			writer.write(NEWLINE);

			// constructor:
			writer.write("public SimSEGUI(Engine e, State s, Logic l)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("logic = l;");
			writer.write(NEWLINE);
			writer.write("engine = e;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("attribPanel = new AttributePanel(this, state, engine);");
			writer.write(NEWLINE);
			writer.write("tabPanel = new TabPanel(state, attribPanel);");
			writer.write(NEWLINE);
			writer.write("actionPanel = new ActionPanel(state, logic, this);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Set window title:");
			writer.write(NEWLINE);
			writer.write("setTitle(\"SimSE\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Create main panel:");
			writer.write(NEWLINE);
			writer.write("JPanel mainPane = new JPanel(new BorderLayout());");
			writer.write(NEWLINE);
			writer.write("mainPane.setPreferredSize(new Dimension(1024, 710));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("mainPane.add(tabPanel, BorderLayout.NORTH);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(attribPanel, BorderLayout.SOUTH);");
			writer.write(NEWLINE);
			writer.write("world = new World(state, logic, this);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(world, BorderLayout.CENTER);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(actionPanel, BorderLayout.EAST);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Set main window frame properties:");
			writer.write(NEWLINE);
			writer.write("mainPane.setBackground(Color.white);");
			writer.write(NEWLINE);
			writer.write("addWindowListener(new ExitListener());");
			writer.write(NEWLINE);
			writer.write("setContentPane(mainPane);");
			writer.write(NEWLINE);
			writer.write("setVisible(true);");
			writer.write(NEWLINE);
			writer.write("setSize(getLayout().preferredLayoutSize(this));");
			writer.write(NEWLINE);
			writer.write("// Make it show up in the center of the screen:");
			writer.write(NEWLINE);
			writer.write("setLocationRelativeTo(null);");
			writer.write(NEWLINE);
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// getEngine function:
			writer.write("public Engine getEngine()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return engine;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);



			// getWorld function:
			writer.write("public World getWorld()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return world;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// getAttributePanel function:
			writer.write("public AttributePanel getAttributePanel()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return attribPanel;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// getTabPanel function:
			writer.write("public TabPanel getTabPanel()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return tabPanel;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// update function:
			writer.write("// Update the GUI to reflect the current state:");
			writer.write(NEWLINE);
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("tabPanel.update();");
			writer.write(NEWLINE);
			writer.write("attribPanel.update();");
			writer.write(NEWLINE);
			writer.write("world.update();");
			writer.write(NEWLINE);
			writer.write("actionPanel.update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// ExitListener class:
			writer.write("public class ExitListener extends WindowAdapter");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("public void windowClosing(WindowEvent event)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("System.exit(0);");
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
			JOptionPane.showMessageDialog(null, ("Error writing file " + mainGUIFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}



	private void copyDir(String dirToCopyPath, String destinationDirPath) // copies the directory whose path is dirToCopyPath, along with all its files (not directories),
		// to the directory whose path is destinationDirPath
	{
		File dirToCopy = new File(dirToCopyPath);
		File destinationDir = new File(destinationDirPath);
		destinationDir.mkdir();

		String[] files = dirToCopy.list();
		try
		{
			// go through each file:
			for(int i=0; i<files.length; i++)
			{
				File inFile = new File(dirToCopyPath, files[i]);
				if(inFile.isFile()) // not a directory
				{
					File outFile = new File(destinationDir, files[i]);
					if(outFile.exists())
					{
						outFile.delete();
					}
					FileInputStream inStream = new FileInputStream(inFile);
					FileOutputStream outStream = new FileOutputStream(outFile);
					boolean eof = false;
					while(!eof) // not end of file
					{
						int b = inStream.read();
						if(b == -1)
						{
							eof = true;
						}
						else
						{
							outStream.write(b); // write it to the other file
						}
					}
					inStream.close();
					outStream.close();
				}
				else // is a directory
				{
					// recurse:
					copyDir(inFile.getPath(), new String(destinationDirPath + "\\" + inFile.getName()));
				}
			}
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, ("Cannot find directory to copy: *" + dirToCopyPath +"*"), "File Not Found",
				JOptionPane.WARNING_MESSAGE);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error reading file: " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
