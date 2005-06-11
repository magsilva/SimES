/* This class defines the window through which trigger rules can be put in order for priority of execution*/

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.actionbuilder.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;


public class TriggerRulePrioritizer extends JDialog implements ActionListener
{
	private ActionType action; // action type whose trigger rules are being prioritized
	private Vector nonPrioritizedRules; // vector of rules that haven't been prioritized yet
	private Vector prioritizedRules; // vector of rules that have been prioritized
	
	private JList nonPrioritizedRuleList; // JList for non-prioritized rules
	private JList prioritizedRuleList; // JList for prioritized rules
	private JButton rightArrowButton; // for moving rules to the right
	private JButton leftArrowButton; // for moving rules to the left
	private JButton moveUpButton; // for moving rules up
	private JButton moveDownButton; // for moving rules down
	private JButton okButton; // for ok'ing the changes made in this form
	private JButton cancelButton; // for cancelling the changes made in this form
	
	public TriggerRulePrioritizer(JFrame owner, ActionType act)
	{
		super(owner, true);
		action = act;
		
		// Set window title:
		setTitle(action.getName() + " Trigger Rule Prioritizer -- SimSE");
		
		// initialize lists:
		nonPrioritizedRules = new Vector();
		prioritizedRules = new Vector();
		
		Vector rules = action.getAllTriggerRules();
		// go through each rule and add them to the correct list:
		for(int j=0; j<rules.size(); j++)
		{
			Rule tempRule = (Rule)rules.elementAt(j);
			int priority = tempRule.getPriority();
			if(priority == -1) // rule is not prioritized yet
			{
				nonPrioritizedRules.addElement(tempRule.getName());
			}
			else // priority >= 0
			{
				if(prioritizedRules.size() == 0) // no elements have been added yet to the prioritized rule list
				{
					prioritizedRules.add(tempRule.getName());
				}
				else
				{
					// find the correct position to insert the rule at:
					for(int k=0; k<prioritizedRules.size(); k++)
					{
						String tempRuleName = (String)prioritizedRules.elementAt(k);
						Rule tempR = action.getRule(tempRuleName);
						if(priority <= tempR.getPriority())
						{
							prioritizedRules.insertElementAt(tempRule.getName(), k); // insert the rule name
							break;
						}
						else if(k == (prioritizedRules.size() - 1)) // on the last element
						{
							prioritizedRules.add(tempRule.getName()); // add the rule name to the end of the list
							break;
						}
					}
				}
			}
		}
		
		nonPrioritizedRuleList = new JList(nonPrioritizedRules);
		nonPrioritizedRuleList.setVisibleRowCount(10); // make 10 items visible at a time
		nonPrioritizedRuleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		prioritizedRuleList = new JList(prioritizedRules);
		prioritizedRuleList.setVisibleRowCount(10); // make 10 items visible at a time
		prioritizedRuleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		setupListSelectionListeners();
		
		// Create main panel:
		Box mainPane = Box.createVerticalBox();
		
		// Create top panel:
		JPanel topPane = new JPanel();
		
		// Create topPaneA and components:
		Box topPaneA = Box.createVerticalBox();
		topPaneA.add(new JLabel("Non-Prioritized Rules:"));
		JScrollPane nonPriListPane = new JScrollPane(nonPrioritizedRuleList);
		nonPriListPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		topPaneA.add(nonPriListPane);
		topPane.add(topPaneA);
		
		// Create topPaneB and components:
		Box topPaneB = Box.createVerticalBox();
		rightArrowButton = new JButton("->");
		rightArrowButton.addActionListener(this);
		rightArrowButton.setEnabled(false);
		topPaneB.add(rightArrowButton);
		leftArrowButton = new JButton("<-");
		leftArrowButton.addActionListener(this);
		leftArrowButton.setEnabled(false);
		topPaneB.add(leftArrowButton);
		topPane.add(topPaneB);
		
		// Create topPaneC and components:
		Box topPaneC = Box.createVerticalBox();
		topPaneC.add(new JLabel("Prioritized Rules:"));
		JScrollPane priListPane = new JScrollPane(prioritizedRuleList);
		priListPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		topPaneC.add(priListPane);
		topPane.add(topPaneC);
		
		// Create topPaneD and components:
		Box topPaneD = Box.createVerticalBox();
		moveUpButton = new JButton("Move Up      ");
		moveUpButton.addActionListener(this);
		moveUpButton.setEnabled(false);
		topPaneD.add(moveUpButton);
		moveDownButton = new JButton("Move Down");
		moveDownButton.addActionListener(this);
		moveDownButton.setEnabled(false);
		topPaneD.add(moveDownButton);
		topPane.add(topPaneD);
		
		// Create bottom pane and components:
		JPanel bottomPane = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		bottomPane.add(okButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPane.add(cancelButton);
		
		// Add panes to main pane:
		mainPane.add(topPane);
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
		
		if(source == cancelButton) // cancel button has been pressed
		{
			// Close window:
			setVisible(false);
			dispose();
		}
		
		else if(source == rightArrowButton)
		{
			int selIndex = nonPrioritizedRuleList.getSelectedIndex();
			String tempStr = (String)(nonPrioritizedRules.elementAt(selIndex)); 
			nonPrioritizedRules.remove(selIndex); // remove rule from lefthand list
			prioritizedRules.add(tempStr); // add to righthand list
			String tempSelectedVal = ((String)(prioritizedRuleList.getSelectedValue()));
			// reset list data:
			prioritizedRuleList.setListData(prioritizedRules);
			// reset selected value:
			if((tempSelectedVal != null) && (tempSelectedVal.length() > 0)) // selection before resetting list data was non-empty
			{
				prioritizedRuleList.setSelectedValue(tempSelectedVal, true);
			}
			nonPrioritizedRuleList.repaint();
			if((nonPrioritizedRules.size() == 0) || (nonPrioritizedRuleList.getSelectedIndex() > (nonPrioritizedRules.size() - 1))) // no
				// item is selected (had to use nonPrioritizedRules.size() - 1 because it doesn't work using selected index = -1 or
				// selected value == null!
			{
				// disable button:
				rightArrowButton.setEnabled(false);
			}		
		}
		
		else if(source == leftArrowButton)
		{
			int selIndex = prioritizedRuleList.getSelectedIndex();
			String tempStr = (String)(prioritizedRules.elementAt(selIndex)); // remove rule from righthand list
			prioritizedRules.remove(selIndex); // remove rule from righthand list
			nonPrioritizedRules.add(tempStr); // add to lefthand list
			String tempSelectedVal = ((String)(nonPrioritizedRuleList.getSelectedValue()));			
			// reset list data:
			nonPrioritizedRuleList.setListData(nonPrioritizedRules);
			// reset selected value:
			if((tempSelectedVal != null) && (tempSelectedVal.length() > 0)) // selection before resetting list data was non-empty
			{
				nonPrioritizedRuleList.setSelectedValue(tempSelectedVal, true);
			}			
			prioritizedRuleList.repaint();
			if((prioritizedRules.size() == 0) || (prioritizedRuleList.getSelectedIndex() > (prioritizedRules.size() - 1))) // no
				// item is selected (had to use prioritizedRules.size() - 1 because it doesn't work using selected index = -1 or
				// selected value == null!
			{
				// disable butons:
				leftArrowButton.setEnabled(false);
				moveUpButton.setEnabled(false);
				moveDownButton.setEnabled(false);
			}
		}
		
		else if(source == moveUpButton)
		{
			int selIndex = prioritizedRuleList.getSelectedIndex();
			if(selIndex > 0) // first list element wasn't chosen
			{
				String tempRule = (String)(prioritizedRules.remove(selIndex)); // remove rule
				prioritizedRules.insertElementAt(tempRule, (selIndex - 1)); // move up
				prioritizedRuleList.setSelectedIndex(selIndex - 1);
				prioritizedRuleList.repaint();
				if((prioritizedRules.size() == 0) || (prioritizedRuleList.isSelectionEmpty())) // no item is selected
				{
					// disable butons:
					leftArrowButton.setEnabled(false);
					moveUpButton.setEnabled(false);
					moveDownButton.setEnabled(false);
				}
			}
		}
		
		else if(source == moveDownButton)
		{
			int selIndex = prioritizedRuleList.getSelectedIndex();
			if(selIndex < (prioritizedRules.size() - 1)) // last list element wasn't chosen
			{
				String tempRule = (String)(prioritizedRules.remove(selIndex)); // remove rule
				prioritizedRules.insertElementAt(tempRule, (selIndex + 1)); // move down
				prioritizedRuleList.setSelectedIndex(selIndex + 1);
				prioritizedRuleList.repaint();
				if((prioritizedRules.size() == 0) || (prioritizedRuleList.isSelectionEmpty())) // no item is selected
				{
					// disable butons:
					leftArrowButton.setEnabled(false);
					moveUpButton.setEnabled(false);
					moveDownButton.setEnabled(false);
				}
			}
		}
		
		else if(source == okButton) // okButton has been pressed
		{
			// update non-prioritized rules:
			for(int i=0; i<nonPrioritizedRules.size(); i++)
			{
				String tempRuleName = (String)nonPrioritizedRules.elementAt(i);
				Rule tempR = action.getRule(tempRuleName);
				tempR.setPriority(-1);
			}
			
			// update prioritized rules:
			for(int i=0; i<prioritizedRules.size(); i++)
			{
				String tempRuleName = (String)prioritizedRules.elementAt(i);
				Rule tempR = action.getRule(tempRuleName);
				tempR.setPriority(i);
			}
			
			// Close window:
			setVisible(false);
			dispose();
		}
	}
	
	
	private void setupListSelectionListeners() // enables/disables buttons according to list selections
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = nonPrioritizedRuleList.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty() == false)
				{
					rightArrowButton.setEnabled(true);
				}
			}
		});
		
		ListSelectionModel rowSM2 = prioritizedRuleList.getSelectionModel();
		rowSM2.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty() == false)
				{
					leftArrowButton.setEnabled(true);
					moveUpButton.setEnabled(true);
					moveDownButton.setEnabled(true);
				}
			}
		});
	}
}
