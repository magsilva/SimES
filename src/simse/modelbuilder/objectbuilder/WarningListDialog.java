/* This class defines a warning dialog that simply displays a JList of warning messages  */

package simse.modelbuilder.objectbuilder;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;


public class WarningListDialog extends JFrame implements ActionListener
{
	private Vector warnings; // vector of strings, each one a warning message

	private JList warningList; // list of warnings
	private JButton okButton; // for ok'ing the creating/editing of a new attribute


	public WarningListDialog(Vector warn, String title)
	{
		warnings = warn;

		// Set window title:
		setTitle(title);

		// Create main panel (box):
		Box mainPane = Box.createVerticalBox();

		// Create top pane:
		warningList = new JList();
		warningList.setListData(warnings);
		warningList.setVisibleRowCount(10); // make 10 items visible at a time
		JScrollPane topPane = new JScrollPane(warningList);
		topPane.setPreferredSize(new Dimension(900, 250));

		// Create bottom pane:
		JPanel bottomPane = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		bottomPane.add(okButton);

		// Add panes to main pane:
		mainPane.add(topPane);
		mainPane.add(bottomPane);

		// Set main window frame properties:
		setBackground(Color.black);
		setContentPane(mainPane);
		validate();
		pack();
		repaint();
		// Make it show up in the center of the screen:
		setLocationRelativeTo(null);
		setVisible(true);
		//show();
		toFront();
		requestFocus();		
	}


	public void actionPerformed(ActionEvent evt) // handles user actions
	{
		Object source = evt.getSource(); // get which component the action came from

		if(source == okButton) // ok button has been pressed
		{
			// Close window:
			setVisible(false);
			dispose();
		}
	}
}
