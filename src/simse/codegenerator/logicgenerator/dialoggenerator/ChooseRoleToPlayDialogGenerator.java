/* This class is responsible for generating all of the code for the logic's ChooseRoleToPlayDialog component */

package simse.codegenerator.logicgenerator.dialoggenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ChooseRoleToPlayDialogGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to generate into
	private File crtpdFile; // file to generate
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file

	public ChooseRoleToPlayDialogGenerator(DefinedActionTypes acts, File dir)
	{
		directory = dir;
		actTypes = acts;
	}


	public void generate()
	{
		try
		{
			crtpdFile = new File(directory, ("simse\\logic\\dialogs\\ChooseRoleToPlayDialog.java"));
			if(crtpdFile.exists())
			{
				crtpdFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(crtpdFile);
			// package statement:
			writer.write("package simse.logic.dialogs;");
			writer.write(NEWLINE);
			// imports:
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.actions.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.border.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.event.*;");
			writer.write(NEWLINE);
			writer.write("public class ChooseRoleToPlayDialog extends JDialog implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// member variables:
			writer.write("private Employee emp;");
			writer.write(NEWLINE);
			writer.write("private simse.adts.actions.Action action;");
			writer.write(NEWLINE);
			writer.write("private String menuText;");
			writer.write(NEWLINE);
			writer.write("private JComboBox partNameList;");
			writer.write(NEWLINE);
			writer.write("private JButton okButton;");
			writer.write(NEWLINE);
			writer.write("private JButton cancelButton;");
			writer.write(NEWLINE);
			// constructor:
			writer.write("public ChooseRoleToPlayDialog(JDialog owner, Vector partNames, Employee e, simse.adts.actions.Action act, String menText)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("super(owner, true);");
			writer.write(NEWLINE);
			writer.write("emp = e;");
			writer.write(NEWLINE);
			writer.write("action = act;");
			writer.write(NEWLINE);
			writer.write("menuText = menText;");
			writer.write(NEWLINE);
			writer.write("setTitle(\"Choose Action Role\");");
			writer.write(NEWLINE);
			// main pane:
			writer.write("Box mainPane = Box.createVerticalBox();");
			writer.write(NEWLINE);
			// top pane:
			writer.write("JPanel topPane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("topPane.add(new JLabel(\"Choose role to play:\"));");
			writer.write(NEWLINE);
			writer.write("JPanel middlePane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("partNameList = new JComboBox(partNames);");
			writer.write(NEWLINE);
			writer.write("middlePane.add(partNameList);");
			writer.write(NEWLINE);
			
			// bottom pane:
			writer.write("JPanel bottomPane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("okButton = new JButton(\"OK\");");
			writer.write(NEWLINE);
			writer.write("okButton.addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("bottomPane.add(okButton);");
			writer.write(NEWLINE);
			writer.write("cancelButton = new JButton(\"Cancel\");");
			writer.write(NEWLINE);
			writer.write("cancelButton.addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("bottomPane.add(cancelButton);");
			writer.write(NEWLINE);
			
			// add panes to main pane:
			writer.write("mainPane.add(topPane);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(middlePane);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(bottomPane);");
			writer.write(NEWLINE);
			
			// Set main window frame properties:
			writer.write("setContentPane(mainPane);");
			writer.write(NEWLINE);
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write("pack();");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write("toFront();");
			writer.write(NEWLINE);
			writer.write("Point ownerLoc = owner.getLocationOnScreen();");
			writer.write(NEWLINE);
			writer.write("Point thisLoc = new Point();");
			writer.write(NEWLINE);
			writer.write("thisLoc.setLocation((ownerLoc.getX() + (owner.getWidth() / 2) - (this.getWidth() / 2)), (ownerLoc.getY() + (owner.getHeight() / 2) - (this.getHeight() / 2)));");
			writer.write(NEWLINE);
			writer.write("setLocation(thisLoc);");
			writer.write(NEWLINE);
			writer.write("if(partNames.size() == 1)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("onlyOneRole();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setVisible(true);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// other constructor:
			writer.write("public ChooseRoleToPlayDialog(JFrame owner, Vector partNames, Employee e, simse.adts.actions.Action act, String menText)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("super(owner, true);");
			writer.write(NEWLINE);
			writer.write("emp = e;");
			writer.write(NEWLINE);
			writer.write("action = act;");
			writer.write(NEWLINE);
			writer.write("menuText = menText;");
			writer.write(NEWLINE);
			writer.write("setTitle(\"Choose Action Role\");");
			writer.write(NEWLINE);
			// main pane:
			writer.write("Box mainPane = Box.createVerticalBox();");
			writer.write(NEWLINE);
			// top pane:
			writer.write("JPanel topPane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("topPane.add(new JLabel(\"Choose role to play:\"));");
			writer.write(NEWLINE);
			writer.write("JPanel middlePane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("partNameList = new JComboBox(partNames);");
			writer.write(NEWLINE);
			writer.write("middlePane.add(partNameList);");
			writer.write(NEWLINE);
			
			// bottom pane:
			writer.write("JPanel bottomPane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("okButton = new JButton(\"OK\");");
			writer.write(NEWLINE);
			writer.write("okButton.addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("bottomPane.add(okButton);");
			writer.write(NEWLINE);
			writer.write("cancelButton = new JButton(\"Cancel\");");
			writer.write(NEWLINE);
			writer.write("cancelButton.addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("bottomPane.add(cancelButton);");
			writer.write(NEWLINE);
			
			// add panes to main pane:
			writer.write("mainPane.add(topPane);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(middlePane);");
			writer.write(NEWLINE);
			writer.write("mainPane.add(bottomPane);");
			writer.write(NEWLINE);
			
			// Set main window frame properties:
			writer.write("setContentPane(mainPane);");
			writer.write(NEWLINE);
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write("pack();");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write("toFront();");
			writer.write(NEWLINE);
			writer.write("Point ownerLoc = owner.getLocationOnScreen();");
			writer.write(NEWLINE);
			writer.write("Point thisLoc = new Point();");
			writer.write(NEWLINE);
			writer.write("thisLoc.setLocation((ownerLoc.getX() + (owner.getWidth() / 2) - (this.getWidth() / 2)), (ownerLoc.getY() + (owner.getHeight() / 2) - (this.getHeight() / 2)));");
			writer.write(NEWLINE);
			writer.write("setLocation(thisLoc);");
			writer.write(NEWLINE);
			writer.write("if(partNames.size() == 1)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("onlyOneRole();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setVisible(true);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			
			// actionPerformed function:
			writer.write("public void actionPerformed(ActionEvent evt)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = evt.getSource();");
			writer.write(NEWLINE);
			writer.write("if(source == cancelButton)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setVisible(false);");
			writer.write(NEWLINE);
			writer.write("dispose();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if(source == okButton)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String partName = (String)(partNameList.getSelectedItem());");
			writer.write(NEWLINE);
			
			// make a Vector of all the action types with user triggers:
			Vector userTrigActs = new Vector();
			Vector allActs = actTypes.getAllActionTypes();
			for(int j=0; j<allActs.size(); j++)
			{
				ActionType userAct = (ActionType)allActs.elementAt(j);
				Vector allTrigs = userAct.getAllTriggers();
				for(int k=0; k<allTrigs.size(); k++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTrigs.elementAt(k);
					if(tempTrig instanceof UserActionTypeTrigger)
					{
						userTrigActs.add(userAct);
						break;
					}
				}
			}			
			
			// go through all the actions with user triggers:
			for(int i=0; i<userTrigActs.size(); i++)
			{
				ActionType tempAct = (ActionType)userTrigActs.elementAt(i);
				writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// go through all the action's user triggers:
				boolean putElse = false;
				Vector allTrigs = tempAct.getAllTriggers();
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger outerTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					if((outerTrig instanceof UserActionTypeTrigger) && (outerTrig.getTriggerText() != null) 
						&& (outerTrig.getTriggerText().length() > 0))
					{
						if(putElse)
						{
							writer.write("else ");
						}
						else
						{
							putElse = true;
						}				
						writer.write("if(menuText.equals(\"" + ((UserActionTypeTrigger)outerTrig).getMenuText() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("emp.setOverheadText(\"" + ((UserActionTypeTrigger)outerTrig).getTriggerText() + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				
				// go through all employee participants:
				Vector allParts = tempAct.getAllParticipants();
				int numEmpParts = 0;
				for(int j=0; j<allParts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)allParts.elementAt(j);
					if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // employee participant
					{
						if(numEmpParts > 0) // not on first element
						{
							writer.write("else ");
						}
						numEmpParts++;
						writer.write("if(partName.equals(\"" + part.getName() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("((" + getUpperCaseLeading(tempAct.getName()) + "Action)action).add" + part.getName() + "(emp);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("setVisible(false);");
			writer.write(NEWLINE);
			writer.write("dispose();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// onlyOneRole function:
			writer.write("private void onlyOneRole()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String partName = (String)(partNameList.getItemAt(0));");
			writer.write(NEWLINE);			
			
			// go through all action types w/ user triggers:
			for(int i=0; i<userTrigActs.size(); i++)
			{
				ActionType tempAct = (ActionType)userTrigActs.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// go through all the actions user triggers:
				boolean putElse = false;
				Vector allTrigs = tempAct.getAllTriggers();
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger outerTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					if((outerTrig instanceof UserActionTypeTrigger) && (outerTrig.getTriggerText() != null)
						&& (outerTrig.getTriggerText().length() > 0))
					{
						if(putElse)
						{
							writer.write("else ");
						}
						else
						{
							putElse = true;
						}
						writer.write("if(menuText.equals(\"" + ((UserActionTypeTrigger)outerTrig).getMenuText() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("emp.setOverheadText(\"" + outerTrig.getTriggerText() + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				
				// go through all employee participants:
				Vector allParts = tempAct.getAllParticipants();
				int numEmpParts = 0;
				for(int j=0; j<allParts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)allParts.elementAt(j);
					if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // employee participant
					{
						if(numEmpParts > 0) // not on first element
						{
							writer.write("else ");
						}
						numEmpParts++;
						writer.write("if(partName.equals(\"" + part.getName() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("((" + getUpperCaseLeading(tempAct.getName()) + "Action)action).add" + part.getName() + "(emp);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("setVisible(false);");
			writer.write(NEWLINE);
			writer.write("dispose();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + crtpdFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
