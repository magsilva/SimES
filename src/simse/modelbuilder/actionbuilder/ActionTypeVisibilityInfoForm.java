/* This class defines the window through which information about an action's visibility can be edited */

package simse.modelbuilder.actionbuilder;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.border.*;
import java.util.*;


public class ActionTypeVisibilityInfoForm extends JDialog implements ActionListener
{
	private ActionType actionInFocus; // action type whose attributes are being edited

	private JComboBox visibleList; // for choosing whether the action is visible
	private JLabel descriptionLabel; // label for description field
	private JTextField descriptionTextField; // for entering the description of the action
	private JButton okButton; // for ok'ing the creating/editing of a new attribute
	private JButton cancelButton; // for canceling the creating/editing of a new attribute


	public ActionTypeVisibilityInfoForm(JFrame owner, ActionType action)
	{
		super(owner, true);
		actionInFocus = action;

		// Set window title:
		setTitle("Action Visibility");

		// Create main panel (box):
		Box mainPane = Box.createVerticalBox();

		// Create top pane:
		JPanel topPane = new JPanel();
		topPane.add(new JLabel("Visibility:"));
		visibleList = new JComboBox();
		visibleList.addItem("true");
		visibleList.addItem("false");
		visibleList.setToolTipText("Whether or not this action type is visible in the simulation's user interface");
		visibleList.addActionListener(this);
		topPane.add(visibleList);
		
		// Create middle pane:
		JPanel middlePane = new JPanel();
		descriptionLabel = new JLabel("Description:");
		middlePane.add(descriptionLabel);
		descriptionTextField = new JTextField(10);
		descriptionTextField.setToolTipText("Description of this action type to appear in the simulation's user interface");
		middlePane.add(descriptionTextField);
		
		// Create bottom pane and buttons:
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
		mainPane.add(bottomPane);

		initializeForm(); // initialize values to reflect action being edited

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
		
		if(source == visibleList)
		{
			if(((String)visibleList.getSelectedItem()).equals("true"))
			{
				descriptionLabel.setEnabled(true);
				descriptionTextField.setEnabled(true);
			}
			else
			{
				descriptionLabel.setEnabled(false);
				descriptionTextField.setEnabled(false);
			}
		}

		else if(source == cancelButton) // cancel button has been pressed
		{
			// Close window:
			setVisible(false);
			dispose();
		}

		else if(source == okButton) // okButton has been pressed
		{
			if(((String)visibleList.getSelectedItem()).equals("true"))
			{
				// check if description is valid:
				String des = descriptionTextField.getText();
				if((des.equals(null)) || (des.length() == 0)) // nothing entered
				{
					JOptionPane.showMessageDialog(null, "Please enter a description", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				else // valid input
				{
					actionInFocus.setVisibility(true);
					actionInFocus.setDescription(des);
					setVisible(false);
					dispose();					
				}
			}
			else // false
			{
				actionInFocus.setVisibility(false);
				actionInFocus.setDescription(null);
				setVisible(false);
				dispose();				
			}
		}
	}


	private void initializeForm() // initializes the form to reflect the action being edited
	{
		if(actionInFocus.isVisible())
		{
			visibleList.setSelectedIndex(0);
		}
		else // not visible
		{
			visibleList.setSelectedIndex(1);
		}
		if((actionInFocus.getDescription() != null) && (actionInFocus.getDescription().length() > 0)) // has a description
		{
			descriptionTextField.setText(actionInFocus.getDescription());
		}
	}
}
