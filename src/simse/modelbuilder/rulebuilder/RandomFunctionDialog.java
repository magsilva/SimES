/* This class defines the dialog for choosing the arguments for the random function for the effect rule info form */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

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

public class RandomFunctionDialog extends JDialog implements ActionListener
{
	private JTextField fieldInFocus; // text field to edit
	private JTextArea echoedField; // echoed text field in the button pad gui
	private boolean trimWhitespace; // whether or not to trim the trailing whitespace in the text field b4 appending to it

	private JTextField minTextField; // for entering min value
	private JTextField maxTextField; // for entering max value
	private JButton okButton;
	private JButton cancelButton;

	public RandomFunctionDialog(JDialog owner, JTextField tField, JTextArea echoedTField, boolean trim)
	{
		super(owner, true);
		setTitle("Random(min, max)");

		fieldInFocus = tField;
		echoedField = echoedTField;
		trimWhitespace = trim;

		// main pane:
		Box mainPane = Box.createVerticalBox();

		// top pane:
		JPanel topPane = new JPanel();
		topPane.add(new JLabel("Enter values:"));

		// middle pane:
		JPanel middlePane = new JPanel();
		middlePane.add(new JLabel("min:"));
		minTextField = new JTextField(10);
		middlePane.add(minTextField);
		middlePane.add(new JLabel("  max:"));
		maxTextField = new JTextField(10);
		middlePane.add(maxTextField);

		// bottom pane
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
		Object source = evt.getSource();
		if(source == cancelButton)
		{
			setVisible(false);
			dispose();
		}

		else if(source == okButton)
		{
			ok();
		}
	}


	private void ok()
	{
		String minStr = minTextField.getText();
		String maxStr = maxTextField.getText();
		boolean valid = true;
		try
		{
			int minVal = Integer.parseInt(minStr);
			int maxVal = Integer.parseInt(maxStr);
			if(minVal > maxVal)
			{
				JOptionPane.showMessageDialog(null, ("Max value must be greater than or equal to min value"), "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
			else
			{
				// set the text field text:
				if(trimWhitespace)
				{
					setFocusedTextFieldText(fieldInFocus.getText().trim().concat("random:" + minVal + "," + maxVal + " "));
				}
				else
				{
					setFocusedTextFieldText(fieldInFocus.getText().concat("random:" + minVal + "," + maxVal + " "));
				}
			}
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, ("Please enter valid integers in both fields"), "Invalid Input",
				JOptionPane.ERROR_MESSAGE);
			valid = false;
		}
		if(valid)
		{
			setVisible(false);
			dispose();
		}
	}
	
	
	private void setFocusedTextFieldText(String text) // sets both the text fields passed in to the specified text
	{
		fieldInFocus.setText(text);
		echoedField.setText(text);
	}		
}
