/* This class defines a form for entering info about constraints on an action participant */

package simse.modelbuilder.actionbuilder;

import simse.modelbuilder.objectbuilder.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.border.*;
import java.util.*;
import java.lang.Math;

public class ActionTypeParticipantConstraintsInfoForm extends JFrame implements ActionListener
{
	private ActionTypeParticipantTrigger triggerInFocus; // temporary copy of trigger whose values are being edited
	private ActionTypeParticipantTrigger actualTrigger;
	private ActionTypeParticipant participantInFocus; // participant that these constraints are on
	private ActionTypeParticipantConstraint constraintInFocus; // particular constraint of the trigger being edited
	private Vector attributes; // Attributes of the constraint currently in focus
	private Vector guardComboBoxes; // JComboBoxes of guards for each attribute of the constraint currently in focus
	private Vector values; // JTextFields (for non-boolean attributes) and JComboBoxes (for boolean attributes) of values for each
		// attribute of the constraint currently in focus
	private Vector typeNames; // names of the SimSEObjectTypes to be put in the JList

	private Box middlePane;
	private JLabel middlePaneTitleLabel;
	private JList typeList; // list of types to define constraints for
	private JLabel constraintsTitleLabel;
	private JButton okButton; // for ok'ing the info entered in this form
	private JButton cancelButton; // for canceling the info entered in this form

	public ActionTypeParticipantConstraintsInfoForm(ActionTypeParticipantTrigger trig)
	{
		actualTrigger = trig;
		triggerInFocus = (ActionTypeParticipantTrigger)(actualTrigger.clone());
		participantInFocus = trig.getParticipant();

		// Set window title:
		setTitle("Action Type Participant Trigger Constraints");

		// Create main panel (box):
		Box mainPane = Box.createVerticalBox();

		// Create top pane and components:
		JPanel topPane = new JPanel();
		// title pane and label:
		JPanel titlePane = new JPanel();
		titlePane.add(new JLabel("Allowable Types:"));
		topPane.add(titlePane);

		// type list:
		typeList = new JList();
		Vector types = participantInFocus.getAllSimSEObjectTypes();
		typeNames = new Vector();
		for(int i=0; i<types.size(); i++)
		{
			typeNames.add(((SimSEObjectType)(types.elementAt(i))).getName()); // add the name of each type to the list
		}
		typeList.setListData(typeNames);
		typeList.setVisibleRowCount(10); // make 10 items visible at a time
		typeList.setFixedCellWidth(250);
		typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only allow the user to select one item at a time
		typeList.setSelectedIndex(0); // make 1st item selected initially
		JScrollPane typeListPane = new JScrollPane(typeList);
		topPane.add(typeListPane);
		setupTypeListSelectionListenerStuff();

		// Create middle pane:
		JPanel middlePaneTitlePane = new JPanel();
		middlePaneTitleLabel = new JLabel(" ");
		middlePaneTitlePane.add(middlePaneTitleLabel);
		middlePaneTitlePane.add(new JLabel("(leave blank if no constraints):"));
		middlePane = Box.createHorizontalBox();
		guardComboBoxes = new Vector();
		values = new Vector();
		setConstraintInFocus();

		// Create bottom pane & buttons:
		JPanel bottomPane = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		bottomPane.add(okButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPane.add(cancelButton);

		// set up tool tips:
		setUpToolTips();

		// Add panes to main pane:
		mainPane.add(topPane);
		mainPane.add(middlePaneTitlePane);
		mainPane.add(middlePane);
		mainPane.add(bottomPane);

		// Set main window frame properties:
		setBackground(Color.black);
		setContentPane(mainPane);
		setVisible(true);
		setSize(getLayout().preferredLayoutSize(this));
		validate();
		repaint();
		toFront();
	}


	public void actionPerformed(ActionEvent evt) // handles user actions
	{
		Object source = evt.getSource(); // get which component the action came from

		if(source == okButton) // okButton has been pressed
		{
			Vector errors = validateInput(); // check validity of input of constraint currently in focus
			if(errors.size() == 0) // input valid
			{
				setConstraintInFocusDataFromForm();
				// set the values to the actual trigger from the copy whose values have been edited:
				actualTrigger.setConstraints(triggerInFocus.getAllConstraints());

				// close window:
				setVisible(false);
				dispose();
			}
			else // input not valid
			{
				for(int i=0; i<errors.size(); i++)
				{
					JOptionPane.showMessageDialog(null, ((String)errors.elementAt(i)), "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		else if(source == cancelButton) // cancel button has been pressed
		{
			// Close window:
			setVisible(false);
			dispose();
		}
	}


	private void setUpToolTips()
	{

	}


	private void setupTypeListSelectionListenerStuff() // sets bottom panel to reflect selected type whenever a type is selected
	{
		// Copied from a Java tutorial:
		ListSelectionModel rowSM = typeList.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if ((lsm.isSelectionEmpty() == false)
					&& (constraintInFocus.getSimSEObjectType().getName().equals((String)(typeList.getSelectedValue())) == false))
						// a choice has been selected and it is not the same selection that was already selected
				{
					Vector errors = validateInput();
					if(errors.size() > 0)
					{
						for(int i=0; i<errors.size(); i++)
						{
							JOptionPane.showMessageDialog(null, ((String)errors.elementAt(i)), "Invalid Input",
								JOptionPane.ERROR_MESSAGE);
							// reset selected index of JList to previously selected item:
							typeList.setSelectedIndex(typeNames.indexOf(constraintInFocus.getSimSEObjectType().getName()));
						}
					}
					else // no errors
					{
						setConstraintInFocusDataFromForm();
						setConstraintInFocus();
					}
				}
			}
		});
	}


	private Vector validateInput() // returns a vector of error messages (if any)
	{
		Vector messages = new Vector();
		for(int i=0; i<values.size(); i++)
		{
			if(values.elementAt(i) instanceof JTextField) // text field (non-boolean) input
			{
				Attribute tempAtt = (Attribute)(attributes.elementAt(i)); // get the corresponding attribute to the text field
				String value = ((JTextField)(values.elementAt(i))).getText();
				if(tempAtt.getType() == AttributeTypes.STRING) // string attribute
				{
					char[] cArray = value.toCharArray();
					// Check for length constraints:
					if(cArray.length > 100) // user has entered a string longer than 100 chars
					{
						messages.add(new String(tempAtt.getName() + " attribute string must be less than 100 characters long"));
					}
				}

				else if(tempAtt.getType() == AttributeTypes.DOUBLE) // double attribute
				{
					if((value != null) && (value.length() > 0)) // field is not blank
					{
						try
						{
							double doubleVal = Double.parseDouble(value); // parse the string into a double
							if(((((NumericalAttribute)(tempAtt)).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt)).isMaxBoundless()) == false)) // has both a min and a max
								// value
							{
								if((doubleVal < ((NumericalAttribute)(tempAtt)).getMinValue().doubleValue()) ||
									(doubleVal > ((NumericalAttribute)(tempAtt)).getMaxValue().doubleValue())) // it's outside of the range
								{
									messages.add(new String(tempAtt.getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt)).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt)).isMaxBoundless())== true)) // has only a min value
							{
								if(doubleVal < ((NumericalAttribute)(tempAtt)).getMinValue().doubleValue()) // below the minimum value
								{
									messages.add(new String(tempAtt.getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt)).isMinBoundless()) == true) &&
								((((NumericalAttribute)(tempAtt)).isMaxBoundless()) == false)) // has only a max value
							{
								if(doubleVal > ((NumericalAttribute)(tempAtt)).getMaxValue().doubleValue()) // above the maximum value
								{
									messages.add(new String(tempAtt.getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
						}
						catch(NumberFormatException e)
						{
							messages.add(new String(tempAtt.getName() + " attribute value must be a valid double number"));
						}
					}
				}

				else if(tempAtt.getType() == AttributeTypes.INTEGER) // integer attribute
				{
					if((value != null) && (value.length() > 0)) // field is not blank
					{
						try
						{
							int intVal = Integer.parseInt(value); // parse the string into an int
							if(((((NumericalAttribute)(tempAtt)).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt)).isMaxBoundless()) == false)) // has both a min and a max
								// value
							{
								if((intVal < ((NumericalAttribute)(tempAtt)).getMinValue().intValue()) ||
									(intVal > ((NumericalAttribute)(tempAtt)).getMaxValue().intValue())) // it's outside of the range
								{
									messages.add(new String(tempAtt.getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt)).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt)).isMaxBoundless())== true)) // has only a min value
							{
								if(intVal < ((NumericalAttribute)(tempAtt)).getMinValue().intValue()) // below the minimum value
								{
									messages.add(new String(tempAtt.getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt)).isMinBoundless()) == true) &&
								((((NumericalAttribute)(tempAtt)).isMaxBoundless()) == false)) // has only a max value
							{
								if(intVal > ((NumericalAttribute)(tempAtt)).getMaxValue().intValue()) // above the maximum value
								{
									messages.add(new String(tempAtt.getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
						}
						catch(NumberFormatException e)
						{
							messages.add(new String(tempAtt.getName() + " attribute value must be a valid integer"));
						}
					}
				}
			}
		}
		return messages;
	}


	private void setConstraintInFocusDataFromForm() // takes what's currently in the form for the constraint in focus and sets the
		// current temporary copy of that constraint with those values
	{
		for(int i=0; i<attributes.size(); i++)
		{
			// get the attribute constraint for the attribute:
			Attribute tempAtt = (Attribute)(attributes.elementAt(i));
			ActionTypeParticipantAttributeConstraint attConst = constraintInFocus.getAttributeConstraint(tempAtt.getName());
			// get the selected guard:
			String guardStr = (String)(((JComboBox)(guardComboBoxes.elementAt(i))).getSelectedItem());
			attConst.setGuard(guardStr); // set the guard
			// get the value:
			if(tempAtt.getType() == AttributeTypes.BOOLEAN) // boolean attribute
			{
				// get the selected value:
				String valString = (String)(((JComboBox)(values.elementAt(i))).getSelectedItem());
				if(valString.equals("True"))
				{
					attConst.setValue(new Boolean(true));
				}
				else if(valString.equals("False"))
				{
					attConst.setValue(new Boolean(false));
				}
				else if(valString.equals(""))
				{
					attConst.setValue(null);
				}
			}
			else // non-boolean attribute
			{
				// get the selected value:
				String valString = (String)(((JTextField)(values.elementAt(i))).getText());
				if(tempAtt.getType() == AttributeTypes.STRING) // string attribute
				{
					attConst.setValue(valString);
				}
				else if(tempAtt.getType() == AttributeTypes.INTEGER) // integer attribute
				{
					try
					{
						int intVal = Integer.parseInt(valString);
						attConst.setValue(new Integer(intVal));
					}
					catch(NumberFormatException e)
					{
						System.out.println(e.getMessage()); // note: validateInput() should have already been called immediately
							// before calling this method, so a NumberFormatException should never be thrown here.
					}
				}
				else if(tempAtt.getType() == AttributeTypes.DOUBLE) // double attribute
				{
					try
					{
						double doubleVal = Double.parseDouble(valString);
						attConst.setValue(new Double(doubleVal));
					}
					catch(NumberFormatException e)
					{
						System.out.println(e.getMessage()); // note: validateInput() should have already been called immediately
							// before calling this method, so a NumberFormatException should never be thrown here.
					}
				}
			}
		}
	}


	private void setConstraintInFocus()
	{
		// clear data structures:
		guardComboBoxes.removeAllElements();
		values.removeAllElements();
		middlePane.removeAll();
		// get selected constraint:
		constraintInFocus = triggerInFocus.getConstraint((String)(typeList.getSelectedValue()));
		// set title:
		middlePaneTitleLabel.setText(constraintInFocus.getSimSEObjectType().getName() + " Constraints");
		// set attributes:
		attributes = constraintInFocus.getSimSEObjectType().getAllAttributes();
		JPanel namePanel = new JPanel(new GridLayout(0, 1));
		JPanel guardPanel = new JPanel(new GridLayout(0, 1));
		JPanel valPanel = new JPanel(new GridLayout(0, 1));
		for(int i=0; i<attributes.size(); i++)
		{
			Attribute att = (Attribute)(attributes.elementAt(i));
			StringBuffer attLabel = new StringBuffer(att.getName() + " (" + AttributeTypes.getText(att.getType())); // attribute name & type
			if((att.getType() == AttributeTypes.INTEGER) || (att.getType() == AttributeTypes.DOUBLE)) // double or integer attribute
			{
				if(((NumericalAttribute)att).isMinBoundless() == false)
				{
					attLabel.append(", min value = " + ((NumericalAttribute)att).getMinValue().toString());
				}
				if(((NumericalAttribute)att).isMaxBoundless() == false)
				{
					attLabel.append(", max value = " + ((NumericalAttribute)att).getMaxValue().toString());
				}
			}
			attLabel.append(")");
			namePanel.add(new JLabel(attLabel.toString()));
			// create guard list:
			JComboBox guardList = new JComboBox();
			guardList.addItem(AttributeGuard.EQUALS);
			if((att.getType() == AttributeTypes.INTEGER) || (att.getType() == AttributeTypes.DOUBLE)) // double or integer
				// attribute
			{
				guardList.addItem(AttributeGuard.LESS_THAN);
				guardList.addItem(AttributeGuard.GREATER_THAN);
				guardList.addItem(AttributeGuard.LESS_THAN_OR_EQUAL_TO);
				guardList.addItem(AttributeGuard.GREATER_THAN_OR_EQUAL_TO);
			}
			ActionTypeParticipantAttributeConstraint attConst = constraintInFocus.getAttributeConstraint(att.getName());
			// set the guard list to the correct selected item:
			guardList.setSelectedItem(attConst.getGuard());
			guardPanel.add(guardList);
			guardComboBoxes.add(guardList);

			// create value text field or combo box:
			if(att.getType() == AttributeTypes.BOOLEAN) // boolean attribute
			{
				JComboBox valList = new JComboBox();
				valList.addItem("");
				valList.addItem("True");
				valList.addItem("False");
				// set the list to the correct selected item:
				Boolean boolVal = (Boolean)(attConst.getValue());
				if(boolVal == null)
				{
					valList.setSelectedItem("");
				}
				else if(boolVal.booleanValue() == true)
				{
					valList.setSelectedItem("True");
				}
				else if(boolVal.booleanValue() == false)
				{
					valList.setSelectedItem("False");
				}
				valPanel.add(valList);
				values.add(valList);
			}
			else // non-boolean attribute
			{
				JTextField valTextField = new JTextField(10);
				// set the text field to the correct value:
				Object val = attConst.getValue();
				if(val != null)
				{
					valTextField.setText(attConst.getValue().toString());
				}
				valPanel.add(valTextField);
				values.add(valTextField);
			}
			middlePane.add(namePanel);
			middlePane.add(guardPanel);
			middlePane.add(valPanel);
		}
		pack();
		validate();
		repaint();
	}
}
