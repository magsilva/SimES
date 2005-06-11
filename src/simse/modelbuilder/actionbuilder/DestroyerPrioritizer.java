/* This class defines the window through which destroyers can be put in order for priority of execution*/

package simse.modelbuilder.actionbuilder;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;


public class DestroyerPrioritizer extends JDialog implements ActionListener
{
	private DefinedActionTypes actions; // existing defined action types
	private Vector nonPrioritizedDestroyers; // vector of destroyers that haven't been prioritized yet
	private Vector prioritizedDestroyers; // vector of destroyers that have been prioritized
	
	private JList nonPrioritizedDestroyerList; // JList for non-prioritized destroyers
	private JList prioritizedDestroyerList; // JList for prioritized destroyers
	private JButton rightArrowButton; // for moving destroyers to the right
	private JButton leftArrowButton; // for moving destroyers to the left
	private JButton moveUpButton; // for moving destroyers up
	private JButton moveDownButton; // for moving destroyers down
	private JButton okButton; // for ok'ing the changes made in this form
	private JButton cancelButton; // for cancelling the changes made in this form
	
	public DestroyerPrioritizer(JFrame owner, DefinedActionTypes acts)
	{
		super(owner, true);
		actions = acts;
		
		// Set window title:
		setTitle("Destroyer Prioritizer -- SimSE");
		
		// initialize lists:
		nonPrioritizedDestroyers = new Vector();
		prioritizedDestroyers = new Vector();
		Vector allActions = actions.getAllActionTypes();
		// go through all action types and add their destroyers to the correct list:
		for(int i=0; i<allActions.size(); i++)
		{
			ActionType tempAct = (ActionType)allActions.elementAt(i);
			Vector allDests = tempAct.getAllDestroyers();
			for(int j=0; j<allDests.size(); j++)
			{
				ActionTypeDestroyer dest = (ActionTypeDestroyer)allDests.elementAt(j);
				int priority = dest.getPriority();
				String destroyerInfo = new String(dest.getName() + " - " + tempAct.getName() + " (");
				if(dest instanceof AutonomousActionTypeDestroyer)
				{
					destroyerInfo = destroyerInfo.concat(ActionTypeDestroyer.AUTO + ")");
				}
				else if(dest instanceof UserActionTypeDestroyer)
				{
					destroyerInfo = destroyerInfo.concat(ActionTypeDestroyer.USER + ")");
				}
				else if(dest instanceof RandomActionTypeDestroyer)
				{
					destroyerInfo = destroyerInfo.concat(ActionTypeDestroyer.RANDOM + ")");
				}
				else if(dest instanceof TimedActionTypeDestroyer)
				{
					destroyerInfo = destroyerInfo.concat(ActionTypeDestroyer.TIMED + ")");
				}				
				if(priority == -1) // destroyer is not prioritized yet
				{
					nonPrioritizedDestroyers.addElement(destroyerInfo);
				}
				else // priority >= 0
				{
					if(prioritizedDestroyers.size() == 0) // no elements have been added yet to the prioritized destroyer list
					{
						prioritizedDestroyers.add(destroyerInfo);
					}
					else
					{
						// find the correct position to insert the destroyer at:
						for(int k=0; k<prioritizedDestroyers.size(); k++)
						{
							String tempDestInfo = (String)prioritizedDestroyers.elementAt(k);
							String tempDestName = tempDestInfo.substring(0, tempDestInfo.indexOf(' '));
							String tempActName = tempDestInfo.substring((tempDestInfo.indexOf('-') + 2), (tempDestInfo.indexOf('(') - 1));
							ActionType tempActType = actions.getActionType(tempActName);
							ActionTypeDestroyer tempDest = tempActType.getDestroyer(tempDestName);
							if(priority <= tempDest.getPriority())
							{
								prioritizedDestroyers.insertElementAt(destroyerInfo, k); // insert the destroyer
								break;
							}
							else if(k == (prioritizedDestroyers.size() - 1)) // on the last element
							{
								prioritizedDestroyers.add(destroyerInfo); // add the destroyer to the end of the list
								break;
							}
						}
					}
				}
			}
		}
		
		nonPrioritizedDestroyerList = new JList(nonPrioritizedDestroyers);
		nonPrioritizedDestroyerList.setVisibleRowCount(10); // make 10 items visible at a time
		nonPrioritizedDestroyerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		prioritizedDestroyerList = new JList(prioritizedDestroyers);
		prioritizedDestroyerList.setVisibleRowCount(10); // make 10 items visible at a time
		prioritizedDestroyerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		setupListSelectionListeners();
		
		// Create main panel:
		Box mainPane = Box.createVerticalBox();
		
		// Create top panel:
		JPanel topPane = new JPanel();
		
		// Create topPaneA and components:
		Box topPaneA = Box.createVerticalBox();
		topPaneA.add(new JLabel("Non-Prioritized Destroyers:"));
		JScrollPane nonPriListPane = new JScrollPane(nonPrioritizedDestroyerList);
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
		topPaneC.add(new JLabel("Prioritized Destroyers:"));
		JScrollPane priListPane = new JScrollPane(prioritizedDestroyerList);
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
			int selIndex = nonPrioritizedDestroyerList.getSelectedIndex();
			String tempStr = (String)(nonPrioritizedDestroyers.elementAt(selIndex)); 
			nonPrioritizedDestroyers.remove(selIndex); // remove destroyer from lefthand list
			prioritizedDestroyers.add(tempStr); // add to righthand list
			String tempSelectedVal = ((String)(prioritizedDestroyerList.getSelectedValue()));
			// reset list data:
			prioritizedDestroyerList.setListData(prioritizedDestroyers);
			// reset selected value:
			if((tempSelectedVal != null) && (tempSelectedVal.length() > 0)) // selection before resetting list data was non-empty
			{
				prioritizedDestroyerList.setSelectedValue(tempSelectedVal, true);
			}
			nonPrioritizedDestroyerList.repaint();
			if((nonPrioritizedDestroyers.size() == 0) || (nonPrioritizedDestroyerList.getSelectedIndex() > (nonPrioritizedDestroyers.size() - 1))) // no
				// item is selected (had to use nonPrioritizedDestroyers.size() - 1 because it doesn't work using selected index = -1 or
				// selected value == null!
			{
				// disable button:
				rightArrowButton.setEnabled(false);
			}		
		}
		
		else if(source == leftArrowButton)
		{
			int selIndex = prioritizedDestroyerList.getSelectedIndex();
			String tempStr = (String)(prioritizedDestroyers.elementAt(selIndex)); // remove destroyer from righthand list
			prioritizedDestroyers.remove(selIndex); // remove destroyer from righthand list
			nonPrioritizedDestroyers.add(tempStr); // add to lefthand list
			String tempSelectedVal = ((String)(nonPrioritizedDestroyerList.getSelectedValue()));			
			// reset list data:
			nonPrioritizedDestroyerList.setListData(nonPrioritizedDestroyers);
			// reset selected value:
			if((tempSelectedVal != null) && (tempSelectedVal.length() > 0)) // selection before resetting list data was non-empty
			{
				nonPrioritizedDestroyerList.setSelectedValue(tempSelectedVal, true);
			}			
			prioritizedDestroyerList.repaint();
			if((prioritizedDestroyers.size() == 0) || (prioritizedDestroyerList.getSelectedIndex() > (prioritizedDestroyers.size() - 1))) // no
				// item is selected (had to use prioritizedDestroyers.size() - 1 because it doesn't work using selected index = -1 or
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
			int selIndex = prioritizedDestroyerList.getSelectedIndex();
			if(selIndex > 0) // first list element wasn't chosen
			{
				String tempAct = (String)(prioritizedDestroyers.remove(selIndex)); // remove destroyer
				prioritizedDestroyers.insertElementAt(tempAct, (selIndex - 1)); // move up
				prioritizedDestroyerList.setSelectedIndex(selIndex - 1);
				prioritizedDestroyerList.repaint();
				if((prioritizedDestroyers.size() == 0) || (prioritizedDestroyerList.isSelectionEmpty())) // no item is selected
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
			int selIndex = prioritizedDestroyerList.getSelectedIndex();
			if(selIndex < (prioritizedDestroyers.size() - 1)) // last list element wasn't chosen
			{
				String tempAct = (String)(prioritizedDestroyers.remove(selIndex)); // remove destroyer
				prioritizedDestroyers.insertElementAt(tempAct, (selIndex + 1)); // move down
				prioritizedDestroyerList.setSelectedIndex(selIndex + 1);
				prioritizedDestroyerList.repaint();
				if((prioritizedDestroyers.size() == 0) || (prioritizedDestroyerList.isSelectionEmpty())) // no item is selected
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
			// update non-prioritized destroyers:
			for(int i=0; i<nonPrioritizedDestroyers.size(); i++)
			{
				String tempDestInfo = (String)nonPrioritizedDestroyers.elementAt(i);
				String tempDestName = tempDestInfo.substring(0, tempDestInfo.indexOf(' '));
				String tempActName = tempDestInfo.substring((tempDestInfo.indexOf('-') + 2), (tempDestInfo.indexOf('(') - 1));
				ActionType tempActType = actions.getActionType(tempActName);
				ActionTypeDestroyer tempDest = tempActType.getDestroyer(tempDestName);				
				tempDest.setPriority(-1);
			}
			
			// update prioritized destroyers:
			for(int i=0; i<prioritizedDestroyers.size(); i++)
			{
				String tempDestInfo = (String)prioritizedDestroyers.elementAt(i);
				String tempDestName = tempDestInfo.substring(0, tempDestInfo.indexOf(' '));
				String tempActName = tempDestInfo.substring((tempDestInfo.indexOf('-') + 2), (tempDestInfo.indexOf('(') - 1));
				ActionType tempActType = actions.getActionType(tempActName);
				ActionTypeDestroyer tempDest = tempActType.getDestroyer(tempDestName);			
				tempDest.setPriority(i);
			}
			
			// Close window:
			setVisible(false);
			dispose();
		}
	}
	
	
	private void setupListSelectionListeners() // enables/disables buttons according to list selections
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = nonPrioritizedDestroyerList.getSelectionModel();
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
		
		ListSelectionModel rowSM2 = prioritizedDestroyerList.getSelectionModel();
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
