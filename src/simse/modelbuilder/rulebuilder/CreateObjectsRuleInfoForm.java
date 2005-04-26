/* This class defines the window through which information about a Create Objects Rule can be added/edited */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;


public class CreateObjectsRuleInfoForm extends JDialog implements ActionListener
{
	private CreateObjectsRule ruleInFocus; // copy of rule being edited
	private CreateObjectsRule actualRule; // actual rule being edited
	private ActionType actionInFocus; // action to whom this rule belongs
	private DefinedObjectTypes objects; // object types defined
	private DefinedActionTypes actions; // existing defined actions
	private boolean newRule; // whether this is a newly created rule (true) or editing an existing one (false)
	
	private JComboBox objTypeList; // for choosing a type of object to create
	private JButton okCreateObjectButton; // for ok'ing the creation of an object
	private JList createObjsList; // lists the objects this rule creates
	private JButton viewEditButton; // for viewing/editing a create object
	private JButton removeObjectButton; // for removing an object that this rule creates
	private ButtonGroup timingButtonGroup; // for radio buttons for choosing the timing of the rule
	private JRadioButton continuousButton; // for denoting a continuous rule
	private JRadioButton triggerButton; // for denoting a trigger rule
	private JRadioButton destroyerButton; // for denoting a destroyer rule
	private JButton okButton; // for ok'ing the changes made in this form
	private JButton cancelButton; // for cancelling the changes made in this form
	
	public CreateObjectsRuleInfoForm(JFrame owner, CreateObjectsRule rule, ActionType act, DefinedActionTypes acts,
		DefinedObjectTypes objs, boolean newOrEdit)
	{
		super(owner, true);
		newRule = newOrEdit;
		actualRule = rule;
		actionInFocus = act;
		if(!newRule) // editing existing rule
		{
			ruleInFocus = (CreateObjectsRule)(actualRule.clone()); // make a copy of the rule for editing
		}
		else // new rule
		{
			ruleInFocus = rule;
		}
		objects = objs;
		actions = acts;
		
		// Set window title:
		setTitle(ruleInFocus.getName() + " Rule Information - SimSE");
		
		// Create main panel (box):
		Box mainPane = Box.createVerticalBox();
		
		// Create top pane:
		JPanel topPane = new JPanel();
		topPane.add(new JLabel("Create:"));
		// object type combo box:
		objTypeList = new JComboBox();
		Vector allObjs = objects.getAllObjectTypes();
		// go through each object type and add its type and name to the list:
		for(int i=0; i<allObjs.size(); i++)
		{
			SimSEObjectType tempType = (SimSEObjectType)allObjs.elementAt(i);
			objTypeList.addItem(new String(tempType.getName() + " " + SimSEObjectTypeTypes.getText(tempType.getType())));
		}
		topPane.add(objTypeList);
		// ok button for creating an object
		okCreateObjectButton = new JButton("OK");
		okCreateObjectButton.addActionListener(this);
		topPane.add(okCreateObjectButton);
		
		// Create middle pane:
		JPanel middlePane = new JPanel();
		middlePane.add(new JLabel("Created objects:"));
		// list of created objects:
		createObjsList = new JList();
		createObjsList.setVisibleRowCount(10); // make 10 items visible at a time
		createObjsList.setFixedCellWidth(250);
		createObjsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		JScrollPane createObjsListPane = new JScrollPane(createObjsList);
		middlePane.add(createObjsListPane);
		refreshCreateObjsList();
		setupCreateObjsListSelectionListenerStuff();
		// buttons pane:
		Box buttonsPane = Box.createVerticalBox();
		viewEditButton = new JButton("View/Edit Starting Values");
		viewEditButton.addActionListener(this);
		viewEditButton.setEnabled(false);
		buttonsPane.add(viewEditButton);
		removeObjectButton = new JButton("Remove Object                  ");
		removeObjectButton.addActionListener(this);
		removeObjectButton.setEnabled(false);
		buttonsPane.add(removeObjectButton);
		middlePane.add(buttonsPane);
		
		// Create middle bottom pane:
		JPanel middleBottomPane = new JPanel();
		middleBottomPane.add(new JLabel("Timing of Rule:"));		
		timingButtonGroup = new ButtonGroup();
		continuousButton = new JRadioButton("Continuous Rule");
		continuousButton.setSelected(true);
		middleBottomPane.add(continuousButton);
		triggerButton = new JRadioButton("Trigger Rule");
		middleBottomPane.add(triggerButton);
		destroyerButton = new JRadioButton("Destroyer Rule");
		middleBottomPane.add(destroyerButton);
		timingButtonGroup.add(continuousButton);
		timingButtonGroup.add(triggerButton);		
		timingButtonGroup.add(destroyerButton);
		refreshTimingButtons();
		
		// Create bottom pane:
		JPanel bottomPane = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		bottomPane.add(okButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPane.add(cancelButton);
		
		// Add panes to main pane:
		mainPane.add(topPane);
		mainPane.add(middlePane);
		JSeparator separator1 = new JSeparator();
		separator1.setMaximumSize(new Dimension(900, 1));
		mainPane.add(separator1);
		mainPane.add(middleBottomPane);
		JSeparator separator2 = new JSeparator();
		separator2.setMaximumSize(new Dimension(900, 1));		
		mainPane.add(separator2);
		mainPane.add(bottomPane);
		
		// Set main window frame properties:
		setBackground(Color.black);
		setContentPane(mainPane);
		validate();
		pack();
		repaint();
		toFront();
		// Make it show up in the center of the screen:
		Point ownerLoc = owner.getLocationOnScreen();
		Point thisLoc = new Point();
		thisLoc.setLocation((ownerLoc.getX() + (owner.getWidth() / 2) - (this.getWidth() / 2)),
			(ownerLoc.getY() + (owner.getHeight() / 2) - (this.getHeight() / 2)));
		setLocation(thisLoc);
		setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent evt) // handles user actions
	{
		
		Object source = evt.getSource(); // get which component the action came from
		if(source == okCreateObjectButton)
		{
			createObject();
		}
		
		else if(source == cancelButton) // cancel button has been pressed
		{
			// Close window:
			setVisible(false);
			dispose();
		}
		
		else if(source == viewEditButton)
		{
			editObject();
		}
		
		else if(source == removeObjectButton)
		{
			removeObject();
		}
		
		else if(source == okButton) // okButton has been pressed
		{
			// set timing:
			if(continuousButton.isSelected())
			{
				ruleInFocus.setTiming(RuleTiming.CONTINUOUS);
			}
			else if(triggerButton.isSelected())
			{
				ruleInFocus.setTiming(RuleTiming.TRIGGER);
			}
			else if(destroyerButton.isSelected())
			{
				ruleInFocus.setTiming(RuleTiming.DESTROYER);
			}
			
			if(!newRule) // existing rule being edited
			{
				int indexOfRule = actionInFocus.getAllRules().indexOf(actualRule);
				actionInFocus.removeRule(actualRule.getName()); // remove old version of rule
				if(indexOfRule != -1) // object already exists in the rule
				{
					actionInFocus.addRule(ruleInFocus, indexOfRule); // add edited rule at its old location
				}
			}
			else // newly created rule
			{
				actionInFocus.addRule(ruleInFocus); // add new rule
			}
			
			// close window:
			setVisible(false);
			dispose();
		}
	}
	
	
	private void refreshCreateObjsList()
	{
		Vector objNamesAndKeys = new Vector();
		Vector objs = ruleInFocus.getAllSimSEObjects();
		for(int i=0; i<objs.size(); i++)
		{
			SimSEObject tempObj = (SimSEObject)objs.elementAt(i);
			String keyVal = new String();
			if(tempObj.getKey().isInstantiated())
			{
				keyVal = tempObj.getKey().getValue().toString();
			}
			// Add each object's name, type, and key value:
			objNamesAndKeys.add(new String(tempObj.getName() + " " + SimSEObjectTypeTypes.getText(tempObj.getSimSEObjectType().getType())
				+ " (" + keyVal + ")"));
		}
		createObjsList.setListData(objNamesAndKeys);
	}
	
	
	private void refreshTimingButtons()
	{
		int timing = ruleInFocus.getTiming();
		if(timing == RuleTiming.CONTINUOUS)
		{
			continuousButton.setSelected(true);
			triggerButton.setSelected(false);
			destroyerButton.setSelected(false);
		}
		else if(timing == RuleTiming.TRIGGER)
		{
			triggerButton.setSelected(true);
			continuousButton.setSelected(false);
			destroyerButton.setSelected(false);
		}
		else if(timing == RuleTiming.DESTROYER)
		{
			destroyerButton.setSelected(true);
			continuousButton.setSelected(false);
			triggerButton.setSelected(false);
		}
	}	
	
	
	private void setupCreateObjsListSelectionListenerStuff() // enables view/edit and remove object buttons whenever a list item (object)
		// is selected
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = createObjsList.getSelectionModel();
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
	
	
	private void createObject()
	{
		String selectedItem = (String)(objTypeList.getSelectedItem()); // get the object type and name to create
		int type = 0;
		String name = new String();
		if(selectedItem.endsWith(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.EMPLOYEE))) // employee object type
		{
			type = SimSEObjectTypeTypes.EMPLOYEE;
			name = selectedItem.substring(0, (selectedItem.length() - SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.EMPLOYEE).length() - 1));
			// parse object name from selected item
		}
		else if(selectedItem.endsWith(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.ARTIFACT))) // artifact object type
		{
			type = SimSEObjectTypeTypes.ARTIFACT;
			name = selectedItem.substring(0, (selectedItem.length() - SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.ARTIFACT).length() - 1));
			// parse object name from selected item
		}
		else if(selectedItem.endsWith(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.TOOL))) // tool object type
		{
			type = SimSEObjectTypeTypes.TOOL;
			name = selectedItem.substring(0, (selectedItem.length() - SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.TOOL).length() - 1));
			// parse object name from selected item
		}
		else if(selectedItem.endsWith(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.PROJECT))) // project object type
		{
			type = SimSEObjectTypeTypes.PROJECT;
			name = selectedItem.substring(0, (selectedItem.length() - SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.PROJECT).length() - 1));
			// parse object name from selected item
		}
		else if(selectedItem.endsWith(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.CUSTOMER))) // customer object type
		{
			type = SimSEObjectTypeTypes.CUSTOMER;
			name = selectedItem.substring(0, (selectedItem.length() - SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.CUSTOMER).length() - 1));
			// parse object name from selected item
		}
		
		SimSEObject newObj = new SimSEObject(objects.getObjectType(type, name)); // create new object
		CreateObjectsRuleStartingValForm svForm = new CreateObjectsRuleStartingValForm(this, ruleInFocus, newObj, actions);
		
		// Makes it so this gui will refresh itself after this rule starting val form closes:
		WindowFocusListener l = new WindowFocusListener ()
		{
			public void windowLostFocus (WindowEvent ev)
			{
				refreshCreateObjsList();
				viewEditButton.setEnabled(false);
				removeObjectButton.setEnabled(false);
			}
			public void windowGainedFocus(WindowEvent ev)
			{}
		};
		svForm.addWindowFocusListener(l);
	}
	
	
	private void editObject()
	{
		if(createObjsList.getSelectedIndex() >= 0) // an object is selected
		{
			SimSEObject tempObj = (SimSEObject)(ruleInFocus.getAllSimSEObjects().elementAt(createObjsList.getSelectedIndex()));
			// get the selected object
			CreateObjectsRuleStartingValForm svForm = new CreateObjectsRuleStartingValForm(this, ruleInFocus, tempObj, actions);
			
			// Makes it so this gui will refresh itself after this rule starting val form closes:
			WindowFocusListener l = new WindowFocusListener ()
			{
				public void windowLostFocus (WindowEvent ev)
				{
					refreshCreateObjsList();
					viewEditButton.setEnabled(false);
					removeObjectButton.setEnabled(false);
				}
				public void windowGainedFocus(WindowEvent ev)
				{}
			};
			svForm.addWindowFocusListener(l);
		}
	}
	
	
	private void removeObject()
	{
		if(createObjsList.getSelectedIndex() >= 0) // an item (object) is selected
		{
			// get the selected object:
			SimSEObject tempObj = (SimSEObject)(ruleInFocus.getAllSimSEObjects().elementAt(createObjsList.getSelectedIndex()));
			
			String keyVal = new String();
			if(tempObj.getKey().isInstantiated())
			{
				keyVal = tempObj.getKey().getValue().toString();
			}
			int choice = JOptionPane.showConfirmDialog(null, ("Really remove " + tempObj.getName() + " "
				+ SimSEObjectTypeTypes.getText(tempObj.getSimSEObjectType().getType()) + " (" + keyVal
				+ ") object?"), "Confirm Object Removal", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION)
			{
				ruleInFocus.removeSimSEObject(tempObj);
				viewEditButton.setEnabled(false);
				removeObjectButton.setEnabled(false);
				refreshCreateObjsList();
			}
			else // choice == JOptionPane.NO_OPTION
			{
			}
		}
	}
}
