/* This class defines the intial GUI for managing the action triggers of an action type */

package simse.modelbuilder.actionbuilder;

import simse.modelbuilder.objectbuilder.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.Color;

public class ActionTypeTriggerManagementForm extends JDialog implements ActionListener
{
	private ActionType actionInFocus; // temporary copy of action being edited
	private ActionType originalAction; // original action
	private DefinedActionTypes allActions; // all of the currently defined actions (for passing into the trigger info form)
	private Vector triggerNames; // for the JList

	private JList triggerList; // for choosing a trigger to edit
	private JButton viewEditButton; // for viewing/editing triggers
	private JButton removeButton; // for removing triggers
	private JButton newTriggerButton; // for adding a new trigger
	private JButton okButton;
	private JButton cancelButton;
	
	
	public ActionTypeTriggerManagementForm(JFrame owner, ActionType action, DefinedActionTypes acts)
	{
		super(owner, true);
		originalAction = action; // store pointer to original
		actionInFocus = (ActionType)action.clone(); // make a copy of the action for temporary editing
		allActions = acts;
		
		// Set window title:
		setTitle("Trigger Management");
		
		// Create main panel (box):
		Box mainPane = Box.createVerticalBox();
		
		// Create topPane and label:
		JPanel topLabelPane = new JPanel();
		topLabelPane.add(new JLabel("Existing Triggers:"));
		
		// Create top pane and trigger list:
		triggerList = new JList();
		triggerList.setVisibleRowCount(10); // make 10 items visible at a time
		triggerList.setFixedCellWidth(200);
		triggerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		JScrollPane topPane = new JScrollPane(triggerList);
		// initialize trigger names:
		triggerNames = new Vector();
		refreshTriggerList();
		setUpTriggerListActionListenerStuff();
		
		// Create middle pane and buttons:
		JPanel middlePane = new JPanel();
		viewEditButton = new JButton("View/Edit");
		viewEditButton.addActionListener(this);
		viewEditButton.setEnabled(false);
		middlePane.add(viewEditButton);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		removeButton.setEnabled(false);
		middlePane.add(removeButton);		
		newTriggerButton = new JButton("Add New Trigger");
		newTriggerButton.addActionListener(this);
		middlePane.add(newTriggerButton);
		
		// Create bottom pane and buttons:
		JPanel bottomPane = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPane.add(okButton);
		bottomPane.add(cancelButton);
		
		// Add panes and separators to main pane:
		mainPane.add(topLabelPane);
		mainPane.add(topPane);
		mainPane.add(middlePane);
		JSeparator separator3 = new JSeparator();
		separator3.setMaximumSize(new Dimension(450, 1));
		mainPane.add(separator3);
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
		
		if(source == viewEditButton)
		{
			if(triggerList.isSelectionEmpty() == false) // a trigger is selected
			{
				// Bring up form for viewing/editing trigger:
				String selectedStr = (String)triggerList.getSelectedValue();
				ActionTypeTrigger trig = actionInFocus.getTrigger(selectedStr.substring(0, selectedStr.indexOf(' ')));
				ActionTypeTriggerInfoForm trigInfoForm = new ActionTypeTriggerInfoForm(this, actionInFocus, trig, allActions);
				removeButton.setEnabled(false);
				viewEditButton.setEnabled(false);				
				refreshTriggerList();
			}
		}
		
		else if(source == removeButton)
		{
			if(triggerList.isSelectionEmpty() == false) // a trigger is selected
			{
				String selectedStr = (String)triggerList.getSelectedValue();
				int choice = JOptionPane.showConfirmDialog(null, ("Really remove " + selectedStr.substring(0, selectedStr.indexOf(' '))
					+ " trigger?"), "Confirm Trigger Removal", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION)
				{
					// Remove trigger:
					actionInFocus.removeTrigger(selectedStr.substring(0, selectedStr.indexOf(' ')));
					removeButton.setEnabled(false);
					viewEditButton.setEnabled(false);
					refreshTriggerList();
				}
				else // choice == JOptionPane.NO_OPTION
				{
				}	
			}
		}
		
		else if(source == newTriggerButton)
		{
			// Create a new trigger:
			AutonomousActionTypeTrigger newTrig = new AutonomousActionTypeTrigger("", actionInFocus);
			// add a participant trigger for each participant:
			Vector allParts = actionInFocus.getAllParticipants();
			for(int i=0; i<allParts.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)allParts.elementAt(i);
				newTrig.addEmptyTrigger(tempPart);
				// add an empty constraint to the participant trigger for each allowable SimSEObjectType:
				Vector allTypes = tempPart.getAllSimSEObjectTypes();
				for(int j=0; j<allTypes.size(); j++)
				{
					newTrig.getParticipantTrigger(tempPart.getName()).addEmptyConstraint((SimSEObjectType)allTypes.elementAt(j));
				}
			}
			// Bring up form for adding a new trigger:
			ActionTypeTriggerInfoForm trigInfoForm = new ActionTypeTriggerInfoForm(this, actionInFocus, newTrig, allActions);
			refreshTriggerList();
		}
		
		else if(source == okButton)
		{
			originalAction.setTriggers(actionInFocus.getAllTriggers()); // set permanent action's triggers to the edited one
			setVisible(false);
			dispose();			
		}
		
		else if (source == cancelButton)
		{
			setVisible(false);
			dispose();
		}
	}
	
	
	private void refreshTriggerList()
	{
		triggerNames.removeAllElements();
		Vector trigs = actionInFocus.getAllTriggers();
		for(int i=0; i<trigs.size(); i++)
		{
			ActionTypeTrigger tempTrig = (ActionTypeTrigger)trigs.elementAt(i);
			String trigInfo = new String(tempTrig.getName() + " (");
			if(tempTrig instanceof AutonomousActionTypeTrigger)
			{
				trigInfo = trigInfo.concat(ActionTypeTrigger.AUTO);
			}
			else if(tempTrig instanceof RandomActionTypeTrigger)
			{
				trigInfo = trigInfo.concat(ActionTypeTrigger.RANDOM);
			}
			else if(tempTrig instanceof UserActionTypeTrigger)
			{
				trigInfo = trigInfo.concat(ActionTypeTrigger.USER);
			}
			trigInfo = trigInfo.concat(")");
			triggerNames.add(trigInfo);
		}
		triggerList.setListData(triggerNames);
	}
	
	
	private void setUpTriggerListActionListenerStuff()  // enables view/edit and remove buttons whenever a list item (trigger) is selected
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = triggerList.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty() == false)
				{
					viewEditButton.setEnabled(true);
					removeButton.setEnabled(true);
				}
			}
		});
	}
	
	
	public class ExitListener extends WindowAdapter
	{
	  public void windowClosing(WindowEvent event)
		{
			{
				setVisible(false);
				dispose();
			}
	  }
	}
}
