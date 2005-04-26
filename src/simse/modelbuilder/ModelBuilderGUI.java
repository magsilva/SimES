/* This class defines the main GUI for the model builder */

package simse.modelbuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;
import simse.modelbuilder.graphicsbuilder.*;
import simse.modelbuilder.mapeditor.*;
import simse.codegenerator.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.awt.Color;
import java.io.*;

public class ModelBuilderGUI extends JFrame implements ActionListener, ChangeListener, MenuListener
{
	private File openFile; // file currently open
	private boolean fileModSinceLastSave; // denotes whether the file has been modified since the last save
	
	private DefinedObjectTypes objectTypes;
	private CreatedObjects objects;
	private DefinedActionTypes actionTypes;
	private ArrayList userDatas; // list of UserData objects for each employee that is displayed in the map
	private TileData[][] map; // map
	private ObjectBuilderGUI objectBuilder;
	private StartStateBuilderGUI startStateBuilder;
	private ActionBuilderGUI actionBuilder;
	private RuleBuilderGUI ruleBuilder;
	private GraphicsBuilderGUI graphicsBuilder;
	private MapEditorGUI mapEditor;
	
	private ModelFileManipulator fileManip;
	private JFileChooser fileChooser;

	private JTabbedPane mainPane;
	private JMenuBar menuBar; // menu bar at top of window
	
	// File menu:
	private JMenu fileMenu; // file menu
	private JMenuItem newItem; // menu item in "File" menu
	private JMenuItem openItem; // menu item in "File" menu
	private JMenuItem closeItem; // menu item in "File" menu
	private JMenuItem saveItem; // menu item in "File" menu
	private JMenuItem saveAsItem; // menu item in "File" menu
	private JMenuItem exitItem; // menu item in "File" menu
	
	// Narratives menu:
	private JMenu narrativesMenu; // narratives menu
	private JMenuItem startNarrItem; // menu item in "Narratives" menu
	
	// Prioritize menu:
	private JMenu prioritizeMenu;
	private JMenuItem triggerItem; // menu item in "Prioritize" menu for prioritizing triggers
	private JMenuItem destroyerItem; // menu item in "Prioritize" menu for prioritizing destroyers
	// rules submenu:
	private JMenu rulesMenu;
	private JMenuItem continuousItem; // menu item in "rules" sub-menu for prioritizing continuous rules
	private JMenu triggerRulesMenu; // sub-menu in "rules" sub-menu for prioritizing trigger rules
	private JMenu destroyerRulesMenu; // sub-menu in "rules" sub-menu for prioritizing destroyer rules	
	
	// Generate menu:
	private JMenu generateMenu; // generate menu
	private JMenuItem generateSimItem; // menu item in "Generate" menu
	

	public ModelBuilderGUI()
	{
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new MDLFileFilter()); // make it so it only displays .mdl files
		
		fileModSinceLastSave = false;

		// Set window title:
		setTitle("SimSE Model Builder");

		// Create main panel:
		mainPane = new JTabbedPane();
		mainPane.setPreferredSize(new Dimension(1024, 710));
		SingleSelectionModel model = mainPane.getModel();
		model.addChangeListener(this);

		
		objectBuilder = new ObjectBuilderGUI(this);
		mainPane.addTab("Object Types", objectBuilder);
		
		objectTypes = objectBuilder.getDefinedObjectTypes();
		
		startStateBuilder = new StartStateBuilderGUI(this, objectTypes);
		mainPane.addTab("Start State", startStateBuilder);
		
		objects = startStateBuilder.getCreatedObjects();
		
		actionBuilder = new ActionBuilderGUI(this, objectTypes);
		mainPane.addTab("Actions", actionBuilder);
		
		actionTypes = actionBuilder.getDefinedActionTypes();
		
		ruleBuilder = new RuleBuilderGUI(this, objectTypes, actionTypes);
		mainPane.addTab("Rules", ruleBuilder);
		
		graphicsBuilder = new GraphicsBuilderGUI(this, objectTypes, objects, actionTypes);
		mainPane.addTab("Graphics", graphicsBuilder);
		
		mapEditor = new MapEditorGUI(this, objectTypes, objects, actionTypes, graphicsBuilder.getImageDirectory(),
			graphicsBuilder.getStartStateObjsToImages(), graphicsBuilder.getRuleObjsToImages());
		mainPane.addTab("Map", mapEditor);
		
		mainPane.setOpaque(true);

		userDatas = mapEditor.getUserDatas();
		map = mapEditor.getMap();		
		
		fileManip = new ModelFileManipulator(objectTypes, actionTypes, objects, userDatas, map);//stsObjsToImages, ruleObjsToImages, userDatas, map);		

		// Create menu bar and menus:
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File"); // "File" menu
		// "New" menu item:
		newItem = new JMenuItem("New");
		fileMenu.add(newItem);
		newItem.addActionListener(this);
		// "Open" menu item:
		openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		openItem.addActionListener(this);
		// "Close" menu item:
		closeItem = new JMenuItem("Close");
		fileMenu.add(closeItem);
		closeItem.addActionListener(this);
		// (Add separator):
		fileMenu.addSeparator();
		// "Save" menu item:
		saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		saveItem.addActionListener(this);
		// "Save As..." menu item:
		saveAsItem = new JMenuItem("Save As...");
		fileMenu.add(saveAsItem);
		saveAsItem.addActionListener(this);
		// (Add separtor):
		fileMenu.addSeparator();
		// "Exit" menu item:
		exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		exitItem.addActionListener(this);

		menuBar.add(fileMenu);
		
		// Narrative menu:
		narrativesMenu = new JMenu("Narratives"); // "Narratives" menu
		narrativesMenu.setEnabled(false); // disable menu
		startNarrItem = new JMenuItem("Starting narrative");
		narrativesMenu.add(startNarrItem);
		startNarrItem.addActionListener(this);
		
		menuBar.add(narrativesMenu);		
		
		// Prioritize menu:
		prioritizeMenu = new JMenu("Prioritize"); // "Prioritize" menu
		prioritizeMenu.setEnabled(false); // disable menu
		prioritizeMenu.addMenuListener(this);
		triggerItem = new JMenuItem("Triggers");
		prioritizeMenu.add(triggerItem);
		triggerItem.addActionListener(this);
		destroyerItem = new JMenuItem("Destroyers");
		prioritizeMenu.add(destroyerItem);
		destroyerItem.addActionListener(this);		
		menuBar.add(prioritizeMenu);	
		
		// Rules sub-menu:
		rulesMenu = new JMenu("Rules"); // "Rules" sub-menu
		continuousItem = new JMenuItem("Continuous Rules");
		rulesMenu.add(continuousItem);
		continuousItem.addActionListener(this);
		triggerRulesMenu = new JMenu("Trigger Rules"); // "Trigger Rules" menu
		rulesMenu.add(triggerRulesMenu);
		destroyerRulesMenu = new JMenu("Destroyer Rules"); // "Destroyer rules" menu
		rulesMenu.add(destroyerRulesMenu);
		prioritizeMenu.add(rulesMenu);
		
		// Generate menu:
		generateMenu = new JMenu("Generate"); // "Generate" menu
		generateMenu.setEnabled(false); // disable menu
		generateSimItem = new JMenuItem("Generate Simulation");
		generateMenu.add(generateSimItem);
		generateSimItem.addActionListener(this);
		menuBar.add(generateMenu);

		// Add menu bar to this frame:
		this.setJMenuBar(menuBar);

		// make it so no file is open to begin with:
		setNoOpenFile();

		// Set main window frame properties:
		addWindowListener(new ExitListener());
		setContentPane(mainPane);
		setVisible(true);
		setSize(getLayout().preferredLayoutSize(this));
		// Make it show up in the center of the screen:
		setLocationRelativeTo(null);
		validate();
		pack();
		repaint();
	}
	
	
	public void setFileModSinceLastSave() // sets this variable to "true"
	{
		fileModSinceLastSave = true;
	}


	public void actionPerformed(ActionEvent evt) // handles user actions
	{

		Object source = evt.getSource(); // get which component the action came from

		if(source == newItem)
		{
			newFile();
		}

		else if(source == openItem)
		{
			openFile();
		}

		else if(source == closeItem)
		{
			if(openFile != null) // a file is open
			{
				closeFile();
			}
		}

		if(source == saveItem)
		{
			if(openFile != null) // a file is open
			{
				if(openFile.exists()) // file has already been saved before
				{
					fileManip.generateFile(openFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					fileModSinceLastSave = false;
				}
				else // file has not been saved before
				{
					saveAs();
				}
			}
		}
		else if(source == saveAsItem)
		{
			if(openFile != null) // a file is open
			{
				saveAs();
			}
		}
		else if(source == exitItem)
		{
			if(closeFile()) // if current file is successfully closed, exit
			{
				System.exit(0);
			}
		}
		else if(source == startNarrItem)
		{
			if(openFile != null) // a file is open
			{
				// bring up starting narrative dialog:
				NarrativeDialog nd = new NarrativeDialog(this, objects, "Starting Narrative");
				fileModSinceLastSave = true;
			}
		}		
		else if(source == triggerItem)
		{
			TriggerPrioritizer tp = new TriggerPrioritizer(this, actionTypes);
			fileModSinceLastSave = true;
		}
		
		else if(source == destroyerItem)
		{
			DestroyerPrioritizer dp = new DestroyerPrioritizer(this, actionTypes);
			fileModSinceLastSave = true;
		}
		else if(source == continuousItem)
		{
			ContinuousRulePrioritizer rp = new ContinuousRulePrioritizer(this, actionTypes);
			fileModSinceLastSave = true;
		}
		else if(source == generateSimItem)
		{
			if(fileModSinceLastSave)
			{
				// must save first
				int choice = JOptionPane.showConfirmDialog(null, ("You must save this model before generating the simulation. Save now?"),
					"Confirm Save",	JOptionPane.YES_NO_CANCEL_OPTION);
				if(choice != JOptionPane.CANCEL_OPTION)
				{
					if(choice == JOptionPane.YES_OPTION)
					{
						if(openFile.exists()) // file has already been saved before
						{
							fileManip.generateFile(openFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
							graphicsBuilder.getRuleObjsToImages());
						}
						else // file has not been saved before
						{
							saveAs();
						}
					}				
				}
			}
			
			// get directory to generate code in:
			// Bring up a file chooser to choose a directory:
			JFileChooser dirFileChooser = new JFileChooser();
			dirFileChooser.addChoosableFileFilter(new DirectoryFileFilter()); // make it so it only displays directories
			// bring up open file chooser:
			dirFileChooser.setSelectedFile(new File(""));
			dirFileChooser.setDialogTitle("Please select a destination directory (to generate code into):");
			dirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = dirFileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File f = dirFileChooser.getSelectedFile();
				if(f.isDirectory()) // valid
				{
					// ask for the image directory for the simulation images:
					dirFileChooser.setSelectedFile(new File(""));
					dirFileChooser.setDialogTitle("Please locate the \"images\" directory containing the simulation images:");
					int returnVal2 = dirFileChooser.showOpenDialog(this);
					if(returnVal2 == JFileChooser.APPROVE_OPTION)
					{
						File imgDir = dirFileChooser.getSelectedFile();
						if(imgDir.isDirectory() && imgDir.getName().equals("images")) // valid
						{
							// generate code:
							CodeGenerator codeGen = new CodeGenerator(objectTypes, objects, actionTypes, graphicsBuilder.getImageDirectory(),
								graphicsBuilder.getStartStateObjsToImages(), graphicsBuilder.getRuleObjsToImages(), map, userDatas, imgDir, f);//openFile, imgDir, f);
							codeGen.generate();
						}
						else
						{
							JOptionPane.showMessageDialog(null, "You must choose a directory named \"images\"", "Invalid Selection",
								JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You must choose a directory", "Invalid Selection",
						JOptionPane.WARNING_MESSAGE);
				}
			}	
		}
		else if(source instanceof JMenuItem)
		{
			JMenuItem mItem = (JMenuItem)source;
			if(mItem.getText().endsWith("Trigger"))
			{
				ActionType tempAct = actionTypes.getActionType(mItem.getText().substring(0, mItem.getText().indexOf(' ')));
				TriggerRulePrioritizer rp = new TriggerRulePrioritizer(this, tempAct);
				fileModSinceLastSave = true;
			}
			else if(mItem.getText().endsWith("Destroyer"))
			{
				ActionType tempAct = actionTypes.getActionType(mItem.getText().substring(0, mItem.getText().indexOf(' ')));
				DestroyerRulePrioritizer rp = new DestroyerRulePrioritizer(this, tempAct);
				fileModSinceLastSave = true;
			}			
		}		
	}
	
	
	public void stateChanged(ChangeEvent e)
	{
		if(mainPane.getSelectedComponent() == startStateBuilder)
		{
			if(openFile != null) // there is a file currently open
			{
				startStateBuilder.getCreatedObjects().updateAllInstantiatedAttributes();
				try
				{
					File tempFile = File.createTempFile(openFile.getName(), ".mdl"); // create a temporary file
					tempFile.deleteOnExit(); // make sure it's deleted on exit
					fileManip.generateFile(tempFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					startStateBuilder.reload(tempFile);
					actionBuilder.reload(tempFile);
					ruleBuilder.reload(tempFile);
					graphicsBuilder.reload(tempFile);	
					tempFile.delete();
				}
				catch(IOException i)
				{
					System.out.println("File I/O error creating temp file for " + openFile.getName() + ": " + i.toString());
				}
			}
		}
		else if(mainPane.getSelectedComponent() == actionBuilder)
		{
			if(openFile != null) // there is a file currently open
			{
				startStateBuilder.getCreatedObjects().updateAllInstantiatedAttributes();
				try
				{
					File tempFile = File.createTempFile(openFile.getName(), ".mdl"); // create a temporary file
					tempFile.deleteOnExit(); // make sure it's deleted on exit
					fileManip.generateFile(tempFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					startStateBuilder.reload(tempFile);
					actionBuilder.reload(tempFile);
					ruleBuilder.reload(tempFile);
					graphicsBuilder.reload(tempFile);	
					tempFile.delete();
				}
				catch(IOException i)
				{
					System.out.println("File I/O error creating temp file for " + openFile.getName() + ": " + i.toString());
				}
			}		
		}
		else if(mainPane.getSelectedComponent() == ruleBuilder)
		{
			if(openFile != null) // there is a file currently open
			{
				startStateBuilder.getCreatedObjects().updateAllInstantiatedAttributes();
				try
				{
					File tempFile = File.createTempFile(openFile.getName(), ".mdl"); // create a temporary file
					tempFile.deleteOnExit(); // make sure it's deleted on exit
					fileManip.generateFile(tempFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					startStateBuilder.reload(tempFile);
					actionBuilder.reload(tempFile);
					ruleBuilder.reload(tempFile);
					graphicsBuilder.reload(tempFile);	
					tempFile.delete();
				}
				catch(IOException i)
				{
					System.out.println("File I/O error creating temp file for " + openFile.getName() + ": " + i.toString());
				}
			}	
		}
		else if(mainPane.getSelectedComponent() == graphicsBuilder)
		{
			if(openFile != null) // there is a file currently open
			{
				startStateBuilder.getCreatedObjects().updateAllInstantiatedAttributes();
				try
				{
					File tempFile = File.createTempFile(openFile.getName(), ".mdl"); // create a temporary file
					tempFile.deleteOnExit(); // make sure it's deleted on exit
					fileManip.generateFile(tempFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					// have to reload both rules & start state as well as graphics since graphics uses the data structures of rules and start state					
					startStateBuilder.reload(tempFile);
					actionBuilder.reload(tempFile);
					ruleBuilder.reload(tempFile);
					graphicsBuilder.reload(tempFile);	
					tempFile.delete();
				}
				catch(IOException i)
				{
					System.out.println("File I/O error creating temp file for " + openFile.getName() + ": " + i.toString());
				}
			}
		}		
		else if(mainPane.getSelectedComponent() == mapEditor)
		{
			if(openFile != null) // there is a file currently open
			{
				startStateBuilder.getCreatedObjects().updateAllInstantiatedAttributes();
				try
				{
					File tempFile = File.createTempFile(openFile.getName(), ".mdl"); // create a temporary file
					tempFile.deleteOnExit(); // make sure it's deleted on exit
					fileManip.generateFile(tempFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					// have to reload both rules & start state as well as graphics since graphics uses the data structures of rules and start state					
					startStateBuilder.reload(tempFile);
					actionBuilder.reload(tempFile);
					ruleBuilder.reload(tempFile);
					graphicsBuilder.reload(tempFile);	
					mapEditor.reload(tempFile, graphicsBuilder.getImageDirectory());
					tempFile.delete();
				}
				catch(IOException i)
				{
					System.out.println("File I/O error creating temp file for " + openFile.getName() + ": " + i.toString());
				}
			}
		}		
	}
	
	
	public void menuSelected(MenuEvent e)
	{
		if(e.getSource() == prioritizeMenu)
		{
			resetRulesMenu();
		}
	}
	
	
	public void menuCanceled(MenuEvent e) {}
	public void menuDeselected(MenuEvent e) {}
	
	
	private boolean saveAs()
	{
		// bring up save file chooser:
		fileChooser.setSelectedFile(openFile);
		int returnVal = fileChooser.showSaveDialog(this);
	  if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			if(isMDLFile(selectedFile))
			{
				// generate file:
				fileManip.generateFile(selectedFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
				openFile = selectedFile;
				resetWindowTitle();
				fileModSinceLastSave = false;
				return true;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "File not saved! File must have extension \".mdl\"", "Unexpected File Format", 
					JOptionPane.WARNING_MESSAGE);
				saveAs(); // try again
				return false;
			}
		}
		else // "cancel" button chosen
		{
			return false;
		}
	}

	private void setNewOpenFile(File file)
	{
		openFile = file;
		prioritizeMenu.setEnabled(true);
		narrativesMenu.setEnabled(true);
		generateMenu.setEnabled(true);
		resetWindowTitle();
		objectBuilder.setNewOpenFile(openFile);
		startStateBuilder.setNewOpenFile(openFile);
		actionBuilder.setNewOpenFile(openFile);
		ruleBuilder.setNewOpenFile(openFile);
		graphicsBuilder.setNewOpenFile(openFile);
		mapEditor.setNewOpenFile(openFile, graphicsBuilder.getImageDirectory());		
	}


	private void setNoOpenFile() // makes it so there's no open file in the GUI
	{
		openFile = null;
		setTitle("SimSE Model Builder");
		fileModSinceLastSave = false;
		objectBuilder.setNoOpenFile();
		startStateBuilder.setNoOpenFile();
		actionBuilder.setNoOpenFile();
		ruleBuilder.setNoOpenFile();
		graphicsBuilder.setNoOpenFile();
		mapEditor.setNoOpenFile();
		// disable UI components:
		prioritizeMenu.setEnabled(false);
		narrativesMenu.setEnabled(false);
		generateMenu.setEnabled(false);
	}


	private void resetWindowTitle() // resets the window title to reflect the current file open
	{
		setTitle(("SimSE Model Builder - [") + openFile.getName() + ("]"));
	}


	private void newFile() // creates a new action file
	{
		if(closeFile()) // if currently-open file is successfuly closed, continue
		{
			setNewOpenFile(new File("NewFile.mdl"));
			fileModSinceLastSave = false;
		}
	}


	private void openFile() // allows the user to open a file
	{
		if(closeFile()) // attempt to close currently opened file, if it works, continue:
		{
			// bring up open file chooser:
			fileChooser.setSelectedFile(new File(""));
			int returnVal = fileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				// open file:
				File f = fileChooser.getSelectedFile();
				if(isMDLFile(f)) // valid file format
				{
					setNewOpenFile(f);
					fileModSinceLastSave = false;
				}
				else
				{
					JOptionPane.showMessageDialog(null, "File not opened! File must have extension \".mdl\"", "Unexpected File Format", JOptionPane.WARNING_MESSAGE);
					openFile(); // try again
				}
			}
		}
	}


	private boolean closeFile() // closes the file currently open (returns true if file is closed, false otherwise (i.e., user cancels
		// operation))
	{
		if(fileModSinceLastSave) // file has been modified since last save
		{
			int choice = JOptionPane.showConfirmDialog(null, ("Save changes to " + openFile.getName() + "?"), "SimSE Model Builder",
				JOptionPane.YES_NO_CANCEL_OPTION);
			if(choice != JOptionPane.CANCEL_OPTION)
			{
				if(choice == JOptionPane.YES_OPTION)
				{
					if(openFile.exists()) // file has already been saved before
					{
						fileManip.generateFile(openFile, graphicsBuilder.getImageDirectory(), graphicsBuilder.getStartStateObjsToImages(),
						graphicsBuilder.getRuleObjsToImages());
					}
					else // file has not been saved before
					{
						if(saveAs() == false)
						{
							return false; // file not saved, action cancelled
						}
					}
				}
				setNoOpenFile();
				return true;
			}
			return false;
		}
		setNoOpenFile();
		return true;
	}
	
	
	public void resetRulesMenu() // reset the trigger/destroyer prioritize menus to reflect the rules currently in the file
	{
		// trigger rules menu:
		triggerRulesMenu.removeAll();
		Vector acts = actionTypes.getAllActionTypes();
		// go through all action types and add them to the menu:
		for(int i=0; i<acts.size(); i++)
		{
			ActionType tempAct = (ActionType)acts.elementAt(i);
			if(tempAct.hasTriggerRules())
			{
				JMenuItem tempItem = new JMenuItem(tempAct.getName() + " Trigger");
				triggerRulesMenu.add(tempItem);
				tempItem.addActionListener(this);
			}
		}
		
		// destroyer rules menu:
		destroyerRulesMenu.removeAll();
		// go through all action types and add them to the menu:
		for(int i=0; i<acts.size(); i++)
		{
			ActionType tempAct = (ActionType)acts.elementAt(i);
			if(tempAct.hasDestroyerRules())
			{
				JMenuItem tempItem = new JMenuItem(tempAct.getName() + " Destroyer");
				destroyerRulesMenu.add(tempItem);
				tempItem.addActionListener(this);				
			}
		}		
	}	


	private boolean isMDLFile(File f)
	{
		String extension = f.getName().substring(f.getName().length() - 4);
		if(extension.equalsIgnoreCase(".mdl"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public static void main(String[] args)
	{
		ModelBuilderGUI mbg = new ModelBuilderGUI();
	}


	public class ExitListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			closeFile();
			System.exit(0);
		}
	}
}
