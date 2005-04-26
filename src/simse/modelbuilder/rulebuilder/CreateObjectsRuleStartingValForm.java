/* This class defines the window through which starting values for attributes of objects that are created by a Create Objects Rule can be
	added/edited */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import java.util.*;


public class CreateObjectsRuleStartingValForm extends JDialog implements ActionListener
{
	private CreateObjectsRule ruleInFocus; // rule being edited
	private SimSEObject objectInFocus; // SimSEObject being assigned starting vals for its attributes
	private DefinedActionTypes actions; // existing defined action types

	private Vector values; // JTextFields (for non-boolean attributes) and JComboBoxes (for boolean attributes) of starting values for
		// each attribute of the object in focus
	private JButton okButton; // for ok'ing the changes made in this form
	private JButton cancelButton; // for cancelling the changes made in this form

	public CreateObjectsRuleStartingValForm(JDialog owner, CreateObjectsRule rule, SimSEObject obj, DefinedActionTypes acts)
	{
		super(owner, true);
		ruleInFocus = rule;
		objectInFocus = obj;
		actions = acts;

		values = new Vector();

		// Set window title:
		setTitle("Attribute Starting Values");

		// Create main panel:
		Box mainPane = Box.createVerticalBox();

		// Create top pane:
		JPanel topPane = new JPanel();
		topPane.add(new JLabel("(* indicates key attribute)"));

		// Create middle pane:
		Box middlePane = Box.createHorizontalBox();
		// Create left pane (attribute labels):
		JPanel leftPane = new JPanel(new GridLayout(0, 1));
		// Create right pane (attribute values):
		JPanel rightPane = new JPanel(new GridLayout(0, 1));
		Vector attributes = objectInFocus.getAllAttributes(); // get all the object's attributes
		if(attributes.size() == 0) // new object, not an existing one being edited
		{
			// instantiate instantiated attributes:
			Vector uninstantAtts = objectInFocus.getSimSEObjectType().getAllAttributes();
			for(int i=0; i<uninstantAtts.size(); i++)
			{
				InstantiatedAttribute instAtt = new InstantiatedAttribute((Attribute)uninstantAtts.elementAt(i));
				objectInFocus.addAttribute(instAtt);
			}
		}
		for(int i=0; i<attributes.size(); i++)
		{
			InstantiatedAttribute att = (InstantiatedAttribute)attributes.elementAt(i);

			// Attribute labels:
			StringBuffer attLabel = new StringBuffer();
			if(att.getAttribute().isKey())
			{
				attLabel.append("*"); // indicate this is key attribute
			}
			attLabel.append(att.getAttribute().getName() + " ("  + AttributeTypes.getText(att.getAttribute().getType())); // attribute name & 
				// type
			if((att.getAttribute().getType() == AttributeTypes.INTEGER) || (att.getAttribute().getType() == AttributeTypes.DOUBLE)) // double
				// or integer attribute
			{
				if(((NumericalAttribute)(att.getAttribute())).isMinBoundless() == false)
				{
					attLabel.append(", min value = " + ((NumericalAttribute)(att.getAttribute())).getMinValue().toString());
				}
				if(((NumericalAttribute)(att.getAttribute())).isMaxBoundless() == false)
				{
					attLabel.append(", max value = " + ((NumericalAttribute)(att.getAttribute())).getMaxValue().toString());
				}
			}
			attLabel.append(")");
			leftPane.add(new JLabel(attLabel.toString()));

			// Attribute value text fields/combo boxes:
			if(att.getAttribute().getType() == AttributeTypes.BOOLEAN) // boolean attribute
			{
				JComboBox valList = new JComboBox();
				valList.addItem("True");
				valList.addItem("False");
				// set the list to the correct selected item:
				if(att.isInstantiated()) // attribute has a value
				{
					Boolean boolVal = (Boolean)(att.getValue());
					if(boolVal.booleanValue() == true)
					{
						valList.setSelectedItem("True");
					}
					else if(boolVal.booleanValue() == false)
					{
						valList.setSelectedItem("False");
					}
				}
				rightPane.add(valList);
				values.add(valList);
			}
			else // non-boolean attribute
			{
				JTextField valTextField = new JTextField(10);
				// set the text field to the correct value:
				if(att.isInstantiated()) // attribute has a value
				{
					Object val = att.getValue();
					valTextField.setText(val.toString());
				}
				rightPane.add(valTextField);
				values.add(valTextField);
			}
		}
		middlePane.add(leftPane);
		middlePane.add(rightPane);

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

		else if(source == okButton) // okButton has been pressed
		{
			Vector errors = validateInput(); // check validity of input

			if(errors.size() == 0) // input valid
			{
				addEditObject();
			}
			else // input not valid
			{
				for(int i=0; i<errors.size(); i++)
				{
					JOptionPane.showMessageDialog(null, ((String)errors.elementAt(i)), "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}


	private Vector validateInput() // returns a vector of error messages (if any)
	{
		Vector messages = new Vector();
		Vector attributes = objectInFocus.getAllAttributes(); // get all instantiated attributes of the object
		for(int i=0; i<values.size(); i++)
		{
			if(values.elementAt(i) instanceof JTextField) // text field (non-boolean) input
			{
				InstantiatedAttribute tempAtt = (InstantiatedAttribute)(attributes.elementAt(i)); // get the corresponding attribute
					// to the text field
				String value = ((JTextField)(values.elementAt(i))).getText();
				if((value == null) || (value.length() == 0)) // blank entry
				{
					messages.add("Please enter a starting value for the " + tempAtt.getAttribute().getName() + " attribute");
				}
				else // not-blank
				{
					if(tempAtt.getAttribute().getType() == AttributeTypes.STRING) // string attribute
					{
						char[] cArray = value.toCharArray();
						// Check for length constraints:
						if(cArray.length > 100) // user has entered a string longer than 100 chars
						{
							messages.add(new String(tempAtt.getAttribute().getName() + " attribute string must be less than 100 characters long"));
						}
						if(tempAtt.getAttribute().isKey()) // key attribute
						{
							if(keyValueIsUnique(value) == false) // key value is not unique
							{
								messages.add(new String("There is already a " + objectInFocus.getName() + " "
									+ SimSEObjectTypeTypes.getText(objectInFocus.getSimSEObjectType().getType())
										+ " with that key attribute value being generated by a rule"));
							}
						}
					}

					else if(tempAtt.getAttribute().getType() == AttributeTypes.DOUBLE) // double attribute
					{
						try
						{
							double doubleVal = Double.parseDouble(value); // parse the string into a double
							if(((((NumericalAttribute)(tempAtt.getAttribute())).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt.getAttribute())).isMaxBoundless()) == false)) // has both a min and a max
								// value
							{
								if((doubleVal < ((NumericalAttribute)(tempAtt.getAttribute())).getMinValue().doubleValue()) ||
									(doubleVal > ((NumericalAttribute)(tempAtt.getAttribute())).getMaxValue().doubleValue())) // it's
										// outside of the range
								{
									messages.add(new String(tempAtt.getAttribute().getName() +
										" attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt.getAttribute())).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt.getAttribute())).isMaxBoundless())== true)) // has only a min value
							{
								if(doubleVal < ((NumericalAttribute)(tempAtt.getAttribute())).getMinValue().doubleValue()) // below the
									// minimum value
								{
									messages.add(new String(tempAtt.getAttribute().getName() +
										" attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt.getAttribute())).isMinBoundless()) == true) &&
								((((NumericalAttribute)(tempAtt.getAttribute())).isMaxBoundless()) == false)) // has only a max value
							{
								if(doubleVal > ((NumericalAttribute)(tempAtt.getAttribute())).getMaxValue().doubleValue()) // above the
									// maximum value
								{
									messages.add(new String(tempAtt.getAttribute().getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							if(tempAtt.getAttribute().isKey()) // key attribute
							{
								if(keyValueIsUnique(new Double(doubleVal)) == false) // key value is not unique
								{
									messages.add(new String("There is already a " + objectInFocus.getName() + " "
										+ SimSEObjectTypeTypes.getText(objectInFocus.getSimSEObjectType().getType())
											+ " with that key attribute value being generated by a rule"));
								}
							}
						}
						catch(NumberFormatException e)
						{
							messages.add(new String(tempAtt.getAttribute().getName() + " attribute value must be a valid double number"));
						}
					}

					else if(tempAtt.getAttribute().getType() == AttributeTypes.INTEGER) // integer attribute
					{
						try
						{
							int intVal = Integer.parseInt(value); // parse the string into an int
							if(((((NumericalAttribute)(tempAtt.getAttribute())).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt.getAttribute())).isMaxBoundless()) == false)) // has both a min and a max
								// value
							{
								if((intVal < ((NumericalAttribute)(tempAtt.getAttribute())).getMinValue().intValue()) ||
									(intVal > ((NumericalAttribute)(tempAtt.getAttribute())).getMaxValue().intValue())) // it's outside
										// of the range
								{
									messages.add(new String(tempAtt.getAttribute().getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt.getAttribute())).isMinBoundless()) == false) &&
								((((NumericalAttribute)(tempAtt.getAttribute())).isMaxBoundless())== true)) // has only a min value
							{
								if(intVal < ((NumericalAttribute)(tempAtt.getAttribute())).getMinValue().intValue()) // below the minimum
									// value
								{
									messages.add(new String(tempAtt.getAttribute().getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							else if(((((NumericalAttribute)(tempAtt.getAttribute())).isMinBoundless()) == true) &&
								((((NumericalAttribute)(tempAtt.getAttribute())).isMaxBoundless()) == false)) // has only a max value
							{
								if(intVal > ((NumericalAttribute)(tempAtt.getAttribute())).getMaxValue().intValue()) // above the maximum
									// value
								{
									messages.add(new String(tempAtt.getAttribute().getName() + " attribute value is not within minimum/maximum value ranges"));
								}
							}
							if(tempAtt.getAttribute().isKey()) // key attribute
							{
								if(keyValueIsUnique(new Integer(intVal)) == false) // key value is not unique
								{
									messages.add(new String("There is already a " + objectInFocus.getName() + " "
										+ SimSEObjectTypeTypes.getText(objectInFocus.getSimSEObjectType().getType())
											+ " with that key attribute value being generated by a rule"));
								}
							}
						}
						catch(NumberFormatException e)
						{
							messages.add(new String(tempAtt.getAttribute().getName() + " attribute value must be a valid integer"));
						}
					}
				}
			}
			else if(values.elementAt(i) instanceof JComboBox) // combo box (boolean) input
			{
				InstantiatedAttribute tempAtt = (InstantiatedAttribute)(attributes.elementAt(i)); // get the corresponding attribute
					// to the combo box
				if(tempAtt.getAttribute().isKey()) // key attribute
				{
					String value = (String)(((JComboBox)(values.elementAt(i))).getSelectedItem());
					if(value.equals("True"))
					{
						if(keyValueIsUnique(new Boolean(true)) == false) // key value is not unique
						{
							messages.add(new String("There is already a " + objectInFocus.getName() + " "
								+ SimSEObjectTypeTypes.getText(objectInFocus.getSimSEObjectType().getType())
									+ " with that key attribute value being generated by a rule"));
						}
					}
					else if(value.equals("False"))
					{
						if(keyValueIsUnique(new Boolean(false)) == false) // key value is not unique
						{
							messages.add(new String("There is already a " + objectInFocus.getName() + " "
								+ SimSEObjectTypeTypes.getText(objectInFocus.getSimSEObjectType().getType())
									+ " with that key attribute value being generated by a rule"));
						}
					}
				}
			}
		}
		return messages;
	}


	private boolean keyValueIsUnique(Object value) // returns true if there is no other object created by this or another rule with the
		// same object type as the object in focus and key attribute value as the one that is being passed into this function
	{
		if(((objectInFocus.getKey().getValue() != null) && (objectInFocus.getKey().getValue().equals(value) == false)) ||
			(objectInFocus.getKey().getValue() == null))
			// the new value that the user is trying to set this attribute to is different than its previous value
		{
			Vector actionTypes = actions.getAllActionTypes();
			// go through all action types and check their rules:
			for(int i=0; i<actionTypes.size(); i++)
			{
				Vector alreadyCreatedRules = ((ActionType)actionTypes.elementAt(i)).getAllCreateObjectsRules(); // get all already
					// created create objects rules of this ActionType
				for(int j=0; j<alreadyCreatedRules.size(); j++)
				{
					CreateObjectsRule tempRule = (CreateObjectsRule)alreadyCreatedRules.elementAt(j);
					Vector createObjs = tempRule.getAllSimSEObjects(); // get all objects this rule creates
					for(int k=0; k<createObjs.size(); k++)
					{
						SimSEObject tempObj = (SimSEObject)createObjs.elementAt(k);
						if((tempObj.getKey().isInstantiated()) && (tempObj.getKey().getValue().equals(value)) &&
							(tempObj.getSimSEObjectType().getName().equals(objectInFocus.getSimSEObjectType().getName()))
							&& (tempObj.getSimSEObjectType().getType() == objectInFocus.getSimSEObjectType().getType())) // found a match
						{
							return false;
						}
					}
				}
			}

			// now check the objects created by this rule in particular:
			Vector objs = ruleInFocus.getAllSimSEObjects();
			for(int i=0; i<objs.size(); i++)
			{
				SimSEObject obj = (SimSEObject)objs.elementAt(i);
				if(obj.getKey().isInstantiated() && (obj.getKey().getValue().equals(value)) &&
					(obj.getSimSEObjectType().getName().equals(objectInFocus.getSimSEObjectType().getName()))
					&& (obj.getSimSEObjectType().getType() == objectInFocus.getSimSEObjectType().getType())) // found a match
				{
					return false;
				}
			}
		}
		return true;
	}


	private void addEditObject() // sets the attributes of the SimSEObject in focus from the existing info in the form and adds it to the
		// rule in focus. Note: validateInput() should be called before calling this function to ensure that you're adding a valid object
	{
		int indexOfObj = ruleInFocus.getAllSimSEObjects().indexOf(objectInFocus);
		if(indexOfObj != -1) // object already exists in the rule
		{
			ruleInFocus.removeSimSEObject(objectInFocus); // remove old version of object
		}
		Vector attributes = objectInFocus.getAllAttributes(); // get all instantiated attributes of the object
		for(int i=0; i<values.size(); i++)
		{
			InstantiatedAttribute tempAtt = (InstantiatedAttribute)(attributes.elementAt(i)); // get the corresponding attribute
				// to the text field or combo box
			if(values.elementAt(i) instanceof JTextField) // text field (non-boolean) input
			{
				String value = ((JTextField)(values.elementAt(i))).getText();
				if(tempAtt.getAttribute().getType() == AttributeTypes.STRING) // string attribute
				{
					tempAtt.setValue(value); // set the value to the string in the text field
				}

				else if(tempAtt.getAttribute().getType() == AttributeTypes.DOUBLE) // double attribute
				{
					try
					{
						double doubleVal = Double.parseDouble(value); // parse the string into a double
						tempAtt.setValue(new Double(doubleVal)); // set the value
					}
					catch(NumberFormatException e)
					{
						System.out.println(e.getMessage()); // NOTE: this exception should never be thrown since validateInput() should
							// have been called immediately before this to ensure that the input is valid
					}
				}

				else if(tempAtt.getAttribute().getType() == AttributeTypes.INTEGER) // integer attribute
				{
					try
					{
						int intVal = Integer.parseInt(value); // parse the string into an int
						tempAtt.setValue(new Integer(intVal)); // set the value
					}
					catch(NumberFormatException e)
					{
						System.out.println(e.getMessage()); // NOTE: this exception should never be thrown since validateInput() should
							// have been called immediately before this to ensure that the input is valid
					}
				}
			}

			else if(values.elementAt(i) instanceof JComboBox) // combo box (boolean) input
			{

				String value = (String)(((JComboBox)(values.elementAt(i))).getSelectedItem());
				if(value.equals("True"))
				{
					tempAtt.setValue(new Boolean(true));
				}
				else if(value.equals("False"))
				{
					tempAtt.setValue(new Boolean(false));
				}
			}
		}
		if(indexOfObj != -1) // existing object being edited
		{
			ruleInFocus.addSimSEObject(objectInFocus, indexOfObj);
		}
		else // new object being added
		{
			ruleInFocus.addSimSEObject(objectInFocus);
		}
		setVisible(false);
		dispose();
	}
}
