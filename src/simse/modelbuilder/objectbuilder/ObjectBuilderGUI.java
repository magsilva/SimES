/* This class defines the GUI for building objects with */

package simse.modelbuilder.objectbuilder;

import simse.modelbuilder.*;

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

public class ObjectBuilderGUI extends JPanel implements ActionListener
{
	private ModelBuilderGUI mainGUI;
	private DefinedObjectTypes objects; // data structure for holding all of the created SimSE object types
	private ObjectFileManipulator fileManip; // for generating/loading files
	private File openFile; // file currently open
	private JFileChooser fileChooser; // for saving/opening files
	private JComboBox defineObjectList; // drop-down list of objects to define
	private JButton okDefineObjectButton; // button to "okay" choosing a new object to define
	private JLabel attributeTableTitle; // label for title of attribute table
	private JScrollPane attributeTablePane; // pane for attribute table
	private JTable attributeTable; // table for displaying attributes of an object
	private ObjectBuilderAttributeTableModel attTblMod; // model for above table
	private JButton addNewAttributeButton; // button for adding a new attribute
	private JButton editAttributeButton; // button for editing an attribute
	private JButton removeAttributeButton; // button for deleting an attribute
	private JList definedObjectsList; // JList of already defined objects
	private JButton viewEditButton; // button for viewing/editing an already defined object
	private JButton removeObjectButton; // button for removing an already defined object
	private AttributeInfoForm aInfo; // form for entering attribute info
	
	
	public ObjectBuilderGUI(ModelBuilderGUI owner)
	{
		mainGUI = owner;
		objects = new DefinedObjectTypes();
		fileManip = new ObjectFileManipulator(objects);
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new SSOFileFilter()); // make it so it only displays .sso files
		
		// Create main panel (box):
		Box mainPane = Box.createVerticalBox();
		mainPane.setPreferredSize(new Dimension(1024, 650));
		
		// Create "define object" pane:
		JPanel defineObjectPane = new JPanel();
		defineObjectPane.add(new JLabel("Define New Object Type:"));
		
		// Create and add "define object list":
		defineObjectList = new JComboBox();
		defineObjectList.addItem("Employee");
		defineObjectList.addItem("Artifact");
		defineObjectList.addItem("Tool");
		defineObjectList.addItem("Project");
		defineObjectList.addItem("Customer");
		defineObjectPane.add(defineObjectList);
		
		// Create and add "ok" button for choosing object to define:
		okDefineObjectButton = new JButton("OK");
		okDefineObjectButton.addActionListener(this);
		defineObjectPane.add(okDefineObjectButton);
		
		// Create attribute table title label and pane:
		JPanel attributeTableTitlePane = new JPanel();
		attributeTableTitle = new JLabel("No object type selected");
		attributeTableTitlePane.add(attributeTableTitle);
		
		// Create "attribute table" pane:
		attTblMod = new ObjectBuilderAttributeTableModel();
		attributeTable = new JTable(attTblMod);
		attributeTablePane = new JScrollPane(attributeTable);
		attributeTablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		attributeTablePane.setPreferredSize(new Dimension(900, 350));
		setupAttributeTableRenderers();
		setupAttributeTableSelectionListenerStuff();
		
		// Create attribute button pane and buttons:
		JPanel attributeButtonPane = new JPanel();
		addNewAttributeButton = new JButton("Add New Attribute");
		addNewAttributeButton.addActionListener(this);
		editAttributeButton = new JButton("Edit Attribute");
		editAttributeButton.addActionListener(this);
		removeAttributeButton = new JButton("Remove Attribute");
		removeAttributeButton.addActionListener(this);
		attributeButtonPane.add(addNewAttributeButton);
		attributeButtonPane.add(editAttributeButton);
		attributeButtonPane.add(removeAttributeButton);
		addNewAttributeButton.setEnabled(false);
		editAttributeButton.setEnabled(false);
		removeAttributeButton.setEnabled(false);
		
		// Create "defined objects" pane:
		JPanel definedObjectsPane = new JPanel();
		definedObjectsPane.add(new JLabel("Object Types Already Defined:"));
		
		// Create and add "already defined" objects list to a scroll pane:
		definedObjectsList = new JList();
		definedObjectsList.setVisibleRowCount(10); // make 10 items visible at a time
		definedObjectsList.setFixedCellWidth(500);
		definedObjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		JScrollPane definedObjectsListPane = new JScrollPane(definedObjectsList);
		definedObjectsPane.add(definedObjectsListPane);
		updateDefinedObjectsList();
		setupDefinedObjectsListSelectionListenerStuff();
		
		// Create and add "view/edit" button, "remove" button, and pane for these buttons::
		Box definedObjectsButtonPane = Box.createVerticalBox();
		viewEditButton = new JButton("View/Edit");
		definedObjectsButtonPane.add(viewEditButton);
		viewEditButton.addActionListener(this);
		viewEditButton.setEnabled(false);
		removeObjectButton = new JButton("Remove  ");
		definedObjectsButtonPane.add(removeObjectButton);
		removeObjectButton.addActionListener(this);
		removeObjectButton.setEnabled(false);
		definedObjectsPane.add(definedObjectsButtonPane);
		
		// Add panes and separators to main pane:
		mainPane.add(defineObjectPane);
		JSeparator separator1 = new JSeparator();
		separator1.setMaximumSize(new Dimension(2900, 1));
		mainPane.add(separator1);
		mainPane.add(attributeTableTitlePane);
		mainPane.add(attributeTablePane);
		mainPane.add(attributeButtonPane);
		JSeparator separator2 = new JSeparator();
		separator2.setMaximumSize(new Dimension(2900, 1));
		mainPane.add(separator2);
		mainPane.add(definedObjectsPane);
		add(mainPane);

		validate();
		repaint();
	}
	
	
	public DefinedObjectTypes getDefinedObjectTypes()
	{
		return objects;
	}
	
	
	public void actionPerformed(ActionEvent evt) // handles user actions
	{
		Object source = evt.getSource(); // get which component the action came from
		if(source == okDefineObjectButton) // User has ok'ed the creation of a new object
		{
			createObject((String)defineObjectList.getSelectedItem());
		}
		
		else if(source == addNewAttributeButton) // user has requested to add a new attribute
		{
			addNewAttribute();
			editAttributeButton.setEnabled(false);
			removeAttributeButton.setEnabled(false);
		}
		
		else if(source == editAttributeButton)
		{
			if(attributeTable.getSelectedRow() >= 0) // a row is selected
			{
				Attribute tempAttr = attTblMod.getObjectInFocus().getAttribute((String)(attTblMod.getValueAt(attributeTable.getSelectedRow(), 0)));
				editAttribute(tempAttr);
				editAttributeButton.setEnabled(false);
				removeAttributeButton.setEnabled(false);
			}
		}
		
		else if(source == removeAttributeButton)
		{
			if(attributeTable.getSelectedRow() >= 0) // a row is selected
			{
				Attribute tempAttr = attTblMod.getObjectInFocus().getAttribute((String)(attTblMod.getValueAt(attributeTable.getSelectedRow(), 0)));
				removeAttribute(tempAttr);
			}
		}
		
		else if(source == viewEditButton)
		{
			if(definedObjectsList.getSelectedIndex() >= 0) // an item (object) is selected
			{
				SimSEObjectType tempObj = (SimSEObjectType)(objects.getAllObjectTypes().elementAt(definedObjectsList.getSelectedIndex()));
				// get the selected object type
				setObjectInFocus(tempObj);
			}
		}
		
		else if(source == removeObjectButton)
		{
			if(definedObjectsList.getSelectedIndex() >= 0) // an item (object) is selected
			{
				SimSEObjectType tempObj = (SimSEObjectType)(objects.getAllObjectTypes().elementAt(definedObjectsList.getSelectedIndex()));
				// get the selected object
				removeObject(tempObj);
			}
		}
	}
	
	
	private void createObject(String selectedItem) // creates a new SimSE object of selectedItem type and adds it to the data structure
	{
		if((attTblMod.getObjectInFocus() != null) && (attTblMod.getObjectInFocus().getNumAttributes() == 0)) // current object in focus doesn't have any attributes
		{
			JOptionPane.showMessageDialog(null, "You must add at least one attribute to this " + attTblMod.getObjectInFocus().getName()
				+ " " + SimSEObjectTypeTypes.getText(attTblMod.getObjectInFocus().getType()) + " before continuing.",
				"Warning", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			String response = JOptionPane.showInputDialog(null, "Enter the name for this " + selectedItem + " type:",
				"Enter Object Type Name", JOptionPane.QUESTION_MESSAGE); // Show input dialog
			if(response != null)
			{
				if(objectNameInputValid(response) == false) // input is invalid
				{
					JOptionPane.showMessageDialog(null, "Please enter a unique name, between 2 and 40 alphabetic characters, and no spaces", "Invalid Input",
						JOptionPane.WARNING_MESSAGE); // warn user to enter a valid number
					createObject(selectedItem); // try again
				}
				else // user has entered valid input
				{
					SimSEObjectType newObj = new SimSEObjectType((SimSEObjectTypeTypes.getIntRepresentation(selectedItem)), response); // create new object
					objects.addObjectType(newObj); // add new object type to the data structure
					mainGUI.setFileModSinceLastSave();
					
					setObjectInFocus(newObj); // set newly created object to be the focus of the GUI
					updateDefinedObjectsList();
				}
			}
		}
	}
	
	
	private boolean objectNameInputValid(String input) // returns true if input is a valid object name, false if not
	{
		if((input.equalsIgnoreCase(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.EMPLOYEE)))
			|| (input.equalsIgnoreCase(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.ARTIFACT)))
				|| (input.equalsIgnoreCase(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.TOOL)))
				|| (input.equalsIgnoreCase(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.CUSTOMER)))
				|| (input.equalsIgnoreCase(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.PROJECT)))
				|| (input.equalsIgnoreCase("action")) || (input.equalsIgnoreCase("compiler"))) // System-wide keywords
		{
			return false;
		}
		
		char[] cArray = input.toCharArray();
		
		// Check for length constraints:
		if((cArray.length < 2) || (cArray.length > 40)) // user has entered a string shorter than 2 chars or longer than 40 chars
		{
			return false;
		}
		
		// Check for invalid characters:
		for(int i=0; i<cArray.length; i++)
		{
			if((Character.isLetter(cArray[i])) == false) // character is not a letter (hence, invalid)
			{
				return false;
			}
		}
		
		// Check for uniqueness of name:
		Vector existingObjects = objects.getAllObjectTypes();
		for(int i=0; i<existingObjects.size(); i++)
		{
			SimSEObjectType tempObj = ((SimSEObjectType)existingObjects.elementAt(i));
			if(tempObj.getName().equalsIgnoreCase(input)) // name entered is not unique (there is already another object defined with the
				// same name (hence, invalid))
			{
				return false;
			}
		}
		
		return true; // none of the invalid conditions exist
	}
	
	
	private void addNewAttribute()
	{
		aInfo = new AttributeInfoForm(mainGUI, attTblMod.getObjectInFocus(), null);
		// Makes it so attribute info form window holds focus exclusively:
		WindowFocusListener l = new WindowFocusListener ()
		{
			public void windowLostFocus (WindowEvent ev)
			{
				aInfo.requestFocus();
				attTblMod.refreshData();
				editAttributeButton.setEnabled(false);
				removeAttributeButton.setEnabled(false);
			}
			public void windowGainedFocus(WindowEvent ev)
			{}
		};
		aInfo.addWindowFocusListener(l);
		mainGUI.setFileModSinceLastSave();
	}
	
	
	private void editAttribute(Attribute a)
	{
		aInfo = new AttributeInfoForm(mainGUI, attTblMod.getObjectInFocus(), a);
		// Makes it so attribute info form window holds focus exclusively:
		WindowFocusListener l = new WindowFocusListener ()
		{
			public void windowLostFocus (WindowEvent ev)
			{
				aInfo.requestFocus();
				attTblMod.refreshData();
			}
			public void windowGainedFocus(WindowEvent ev)
			{}
		};
		aInfo.addWindowFocusListener(l);
		mainGUI.setFileModSinceLastSave();
	}
	
	
	private void removeAttribute(Attribute a)
	{
		int choice = JOptionPane.showConfirmDialog(null, ("Really remove " + a.getName() + " attribute?"),
			"Confirm Attribute Removal", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
		{
			if(a.isKey()) // this attribute is the key
			{
				JOptionPane.showMessageDialog(null, "You must first make another attribute the key before you can remove this one.",
					"Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
			else // this attribute not the key
			{
				// Remove attribute:
				attTblMod.getObjectInFocus().removeAttribute(a.getName());
				attTblMod.refreshData();
				removeAttributeButton.setEnabled(false);
				editAttributeButton.setEnabled(false);
				mainGUI.setFileModSinceLastSave();
				//mainGUI.resetObjectTypes();
			}
		}
		else // choice == JOptionPane.NO_OPTION
		{
		}
	}
	
	
	private void setupAttributeTableRenderers()
	{
		// Set up alignment in columns:
		DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();
		renderer1.setHorizontalAlignment(JLabel.RIGHT);
		attributeTable.getColumnModel().getColumn(3).setCellRenderer(renderer1);
		
		DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
		renderer2.setHorizontalAlignment(JLabel.RIGHT);
		attributeTable.getColumnModel().getColumn(4).setCellRenderer(renderer2);
		
		// Set selction mode to only one row at a time:
		attributeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	
	private void setupAttributeTableSelectionListenerStuff() // enables edit and remove attribute buttons whenever a row (attribute) is selected
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = attributeTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty() == false)
				{
					editAttributeButton.setEnabled(true);
					removeAttributeButton.setEnabled(true);
				}
			}
		});
	}
	
	
	private void setupDefinedObjectsListSelectionListenerStuff() // enables view/edit button whenever a list item (object) is selected
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = definedObjectsList.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty() == false)
				{
					viewEditButton.setEnabled(true);
					removeObjectButton.setEnabled(true);
				}
			}
		});
	}
	
	
	private void updateDefinedObjectsList()
	{
		Vector objectNamesAndTypes = new Vector();
		Vector currentObjs = objects.getAllObjectTypes();
		for(int i=0; i<currentObjs.size(); i++)
		{
			SimSEObjectType tempObj = (SimSEObjectType)(currentObjs.elementAt(i));
			objectNamesAndTypes.add(new String((tempObj.getName()) + " (" + (SimSEObjectTypeTypes.getText(tempObj.getType())) + ")"));
		}
		definedObjectsList.setListData(objectNamesAndTypes);
	}
	
	
	private void setObjectInFocus(SimSEObjectType newObj) // sets the given object as the focus of this GUI
	{
		if((attTblMod.getObjectInFocus() != null) && (attTblMod.getObjectInFocus().getNumAttributes() == 0)) // current object in focus doesn't have any attributes
		{
			JOptionPane.showMessageDialog(null, "You must add at least one attribute to this " + attTblMod.getObjectInFocus().getName()
				+ " " + SimSEObjectTypeTypes.getText(attTblMod.getObjectInFocus().getType()) + " before continuing.",
				"Warning", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			attTblMod.setObjectInFocus(newObj); // set focus of attribute table to new object
			attributeTableTitle.setText((newObj.getName()) + " " + (SimSEObjectTypeTypes.getText(newObj.getType())) + " Attributes:"); // change title of table to reflect new object
			// enable buttons:
			addNewAttributeButton.setEnabled(true);
			editAttributeButton.setEnabled(false);
			removeAttributeButton.setEnabled(false);
		}
	}
	
	
	private void removeObject(SimSEObjectType obj) // removes this object from the data structure
	{
		int choice = JOptionPane.showConfirmDialog(null, ("Really remove " + obj.getName() + " " + (SimSEObjectTypeTypes.getText(obj.getType())) + " object type?"),
			"Confirm Object Removal", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
		{
			if((attTblMod.getObjectInFocus() != null) && (attTblMod.getObjectInFocus().getName() == obj.getName()) &&
				(attTblMod.getObjectInFocus().getType() == obj.getType())) // removing object currently in focus
			{
				clearObjectInFocus(); // set it so that there's no object in focus
			}
			objects.removeObjectType(obj.getType(), obj.getName());
			viewEditButton.setEnabled(false);
			removeObjectButton.setEnabled(false);
			updateDefinedObjectsList();
			mainGUI.setFileModSinceLastSave();
		}
		else // choice == JOptionPane.NO_OPTION
		{
		}
	}
	
	
	private void clearObjectInFocus() // clears the GUI so that it doesn't have an object in focus
	{
		attTblMod.clearObjectInFocus(); // clear the attribute table
		attributeTableTitle.setText("No object type selected");
		// disable button:
		addNewAttributeButton.setEnabled(false);
		editAttributeButton.setEnabled(false);
		removeAttributeButton.setEnabled(false);
	}

	
	public void setNoOpenFile()
	{
		clearObjectInFocus();
		objects.clearAll();
		updateDefinedObjectsList();
		defineObjectList.setEnabled(false);
		okDefineObjectButton.setEnabled(false);
	}


	public void setNewOpenFile(File f)
	{
		clearObjectInFocus();
		objects.clearAll();
		if(f.exists()) // file has been saved before
		{
			fileManip.loadFile(f);
		}
		updateDefinedObjectsList();
		defineObjectList.setEnabled(true);
		okDefineObjectButton.setEnabled(true);		
	}
}
