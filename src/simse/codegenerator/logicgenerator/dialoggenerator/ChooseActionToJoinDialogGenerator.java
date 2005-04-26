/* This class is responsible for generating all of the code for the logic's ChooseActionToDestroyJoinDialog component */

package simse.codegenerator.logicgenerator.dialoggenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.codegenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ChooseActionToJoinDialogGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	*/

	private File directory; // directory to generate into
	private File catjdFile; // file to generate
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file

	public ChooseActionToJoinDialogGenerator(DefinedActionTypes acts, File dir)
	{
		directory = dir;
		actTypes = acts;
	}


	public void generate()
	{
		try
		{
			catjdFile = new File(directory, ("simse\\logic\\dialogs\\ChooseActionToJoinDialog.java"));
			if(catjdFile.exists())
			{
				catjdFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(catjdFile);
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
			writer.write("public class ChooseActionToJoinDialog extends JDialog implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// member variables:
			writer.write("private Vector actions;");
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Employee emp;");
			writer.write(NEWLINE);
			writer.write("private Vector radioButtons;");
			writer.write(NEWLINE);
			writer.write("private ButtonGroup radioButtonGroup;");
			writer.write(NEWLINE);
			writer.write("private JButton okButton;");
			writer.write(NEWLINE);
			writer.write("private JButton cancelButton;");
			writer.write(NEWLINE);
			writer.write("private String menuText;");
			writer.write(NEWLINE);

			// constructor:
			writer.write("public ChooseActionToJoinDialog(JFrame owner, Vector acts, Employee e, State s, String menText)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("super(owner, true);");
			writer.write(NEWLINE);
			writer.write("actions = acts;");
			writer.write(NEWLINE);
			writer.write("emp = e;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("menuText = menText;");
			writer.write(NEWLINE);
			writer.write("radioButtons = new Vector();");
			writer.write(NEWLINE);
			writer.write("radioButtonGroup = new ButtonGroup();");
			writer.write(NEWLINE);
			writer.write("setTitle(\"Join Action\");");
			writer.write(NEWLINE);
			// main pane:
			writer.write("Box mainPane = Box.createVerticalBox();");
			writer.write(NEWLINE);
			// top pane:
			writer.write("JPanel topPane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("String actionName = new String();");
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)actions.elementAt(0);");
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
			// go through each action type and generate code for it:
			for(int j=0; j<userTrigActs.size(); j++)
			{
				ActionType act = (ActionType)userTrigActs.elementAt(j);
				if(j > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(tempAct instanceof " + getUpperCaseLeading(act.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("actionName = \"" + getUpperCaseLeading(act.getName()) + "\";");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("topPane.add(new JLabel(\"Choose which \" + actionName + \" Action to join:\"));");
			writer.write(NEWLINE);
			writer.write("JPanel middlePane = new JPanel(new GridLayout(0, 1));");
			writer.write(NEWLINE);
			for(int j=0; j<userTrigActs.size(); j++)
			{
				ActionType act = (ActionType)userTrigActs.elementAt(j);
				if(j > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(tempAct instanceof " + getUpperCaseLeading(act.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<actions.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action act = (" + getUpperCaseLeading(act.getName())
					+ "Action)actions.elementAt(i);");
				writer.write(NEWLINE);
				writer.write("StringBuffer label = new StringBuffer();");
				writer.write(NEWLINE);
				// go through all participants:
				Vector parts = act.getAllParticipants();
				for(int k=0; k<parts.size(); k++)
				{
					if(k > 0) // not on first element
					{
						writer.write("label.append(\"; \");");
						writer.write(NEWLINE);
					}
					ActionTypeParticipant tempPart = (ActionTypeParticipant)parts.elementAt(k);
					writer.write("label.append(\"" + tempPart.getName() + "(s); \");");
					writer.write(NEWLINE);
					writer.write("Vector all" + tempPart.getName() + "s = act.getAll" + tempPart.getName() + "s();");
					writer.write(NEWLINE);
					writer.write("for(int j=0; j<all" + tempPart.getName() + "s.size(); j++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("if(j > 0)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("label.append(\", \");");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write(SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " a = ("
						+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + ")all" + tempPart.getName() + "s.elementAt(j);");
					writer.write(NEWLINE);
					// go through all allowable SimSEObjectTypes for this participant:
					Vector ssObjTypes = tempPart.getAllSimSEObjectTypes();
					for(int m=0; m<ssObjTypes.size(); m++)
					{
						SimSEObjectType tempType = (SimSEObjectType)ssObjTypes.elementAt(m);
						if(m > 0) // not on first element
						{
							writer.write("else ");
						}
						writer.write("if(a instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("label.append(\"" + tempType.getName() + "(\" + ((" + getUpperCaseLeading(tempType.getName()) + ")a).get"
							+ getUpperCaseLeading(tempType.getKey().getName()) + "() + \")\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				writer.write("JPanel tempPane = new JPanel(new BorderLayout());");
				writer.write(NEWLINE);
				writer.write("JRadioButton tempRadioButton = new JRadioButton(label.toString());");
				writer.write(NEWLINE);
				writer.write("radioButtonGroup.add(tempRadioButton);");
				writer.write(NEWLINE);
				writer.write("tempPane.add(tempRadioButton, BorderLayout.WEST);");
				writer.write(NEWLINE);
				writer.write("radioButtons.add(tempRadioButton);");
				writer.write(NEWLINE);
				writer.write("middlePane.add(tempPane);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}

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
			writer.write("if(radioButtons.size() == 1)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("onlyOneChoice(owner);");
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
			writer.write("boolean anySelected = false;");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<radioButtons.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JRadioButton tempRButt = (JRadioButton)radioButtons.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(tempRButt.isSelected())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("anySelected = true;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(!anySelected)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JOptionPane.showMessageDialog(null, (\"You must choose at least one action\"), \"Invalid Input\", JOptionPane.ERROR_MESSAGE);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<radioButtons.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JRadioButton rButt  = (JRadioButton)radioButtons.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(rButt.isSelected())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)actions.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("Vector participantNames = new Vector();");
			writer.write(NEWLINE);

			// go through all action types w/ user triggers:
			for(int i=0; i<userTrigActs.size(); i++)
			{
				ActionType tempAct = (ActionType)userTrigActs.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(tempAct instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);

				// go through all employee participants:
				Vector allParts = tempAct.getAllParticipants();
				for(int j=0; j<allParts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)allParts.elementAt(j);
					if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // employee participant
					{
						writer.write("Vector all" + part.getName() + "s = ((" + getUpperCaseLeading(tempAct.getName()) + "Action)tempAct).getAll" + part.getName() + "s();");
						writer.write(NEWLINE);
						writer.write("if((all" + part.getName() + "s.contains(emp) == false) && (all" + part.getName() + "s.size() < ");
						if(part.getQuantity().isMaxValBoundless())
						{
							writer.write("999999");
						}
						else // max val has a value
						{
							writer.write(part.getQuantity().getMaxVal().toString());
						}
						writer.write("))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);

						// collect all the user triggers for this action:
						Vector userTrigsTemp = new Vector();
						Vector allTriggers = tempAct.getAllTriggers();
						for(int k=0; k<allTriggers.size(); k++)
						{
							ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTriggers.elementAt(k);
							if(tempTrig instanceof UserActionTypeTrigger)
							{
								userTrigsTemp.add(tempTrig);
							}
						}

						// go through all of the user triggers:
						for(int k=0; k<userTrigsTemp.size(); k++)
						{
							UserActionTypeTrigger userTrig = (UserActionTypeTrigger)userTrigsTemp.elementAt(k);
							if(k > 0) // not on first element
							{
								writer.write("else ");
							}
							writer.write("if(menuText.equals(\"" + userTrig.getMenuText() + "\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);

							// go through all allowable types:
							Vector types = part.getAllSimSEObjectTypes();
							for(int m=0; m<types.size(); m++)
							{
								SimSEObjectType type = (SimSEObjectType)types.elementAt(m);
								if(m > 0) // not on first element
								{
									writer.write("else ");
								}
								writer.write("if((emp instanceof " + getUpperCaseLeading(type.getName()) + ")");

								// go through all attribute constraints:
								ActionTypeParticipantAttributeConstraint[] attConstraints =
									userTrig.getParticipantTrigger(part.getName()).getConstraint(type.getName()).getAllAttributeConstraints();
								for(int n=0; n<attConstraints.length; n++)
								{
									ActionTypeParticipantAttributeConstraint attConst = attConstraints[n];
									if(attConst.isConstrained())
									{
										writer.write(" && (((" + getUpperCaseLeading(type.getName()) + ")emp).get"
											+ attConst.getAttribute().getName() + "() ");
										if(attConst.getAttribute().getType() == AttributeTypes.STRING)
										{
											writer.write(".equals(" + "\"" + attConst.getValue().toString() + "\")");
										}
										else
										{
											if(attConst.getGuard().equals(AttributeGuard.EQUALS))
											{
												writer.write("== ");
											}
											else
											{
												writer.write(attConst.getGuard() + " ");
											}
											writer.write(attConst.getValue().toString());
										}
										writer.write(")");
									}
								}
								writer.write(")");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								writer.write("participantNames.add(\"" + getUpperCaseLeading(part.getName()) + "\");");
								writer.write(NEWLINE);
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("ChooseRoleToPlayDialog crtpd = new ChooseRoleToPlayDialog(this, participantNames, emp, tempAct, menuText);");
			writer.write(NEWLINE);
			writer.write("setVisible(false);");
			writer.write(NEWLINE);
			writer.write("dispose();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// onlyOneChoice function:
			writer.write("private void onlyOneChoice(JFrame owner)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<radioButtons.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JRadioButton rButt = (JRadioButton)radioButtons.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)actions.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("Vector participantNames = new Vector();");
			writer.write(NEWLINE);

			// go through all action types w/ user triggers:
			for(int i=0; i<userTrigActs.size(); i++)
			{
				ActionType tempAct = (ActionType)userTrigActs.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(tempAct instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);

				// go through all employee participants:
				Vector allParts = tempAct.getAllParticipants();
				for(int j=0; j<allParts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)allParts.elementAt(j);
					if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // employee participant
					{
						writer.write("Vector all" + part.getName() + "s = ((" + getUpperCaseLeading(tempAct.getName()) + "Action)tempAct).getAll" + part.getName() + "s();");
						writer.write(NEWLINE);
						writer.write("if((all" + part.getName() + "s.contains(emp) == false) && (all" + part.getName() + "s.size() < ");
						if(part.getQuantity().isMaxValBoundless())
						{
							writer.write("999999");
						}
						else // max val has a value
						{
							writer.write(part.getQuantity().getMaxVal().toString());
						}
						writer.write("))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);

						// collect all the user triggers for this action:
						Vector userTrigsTemp = new Vector();
						Vector allTriggers = tempAct.getAllTriggers();
						for(int k=0; k<allTriggers.size(); k++)
						{
							ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTriggers.elementAt(k);
							if(tempTrig instanceof UserActionTypeTrigger)
							{
								userTrigsTemp.add(tempTrig);
							}
						}

						// go through all of the user triggers:
						for(int k=0; k<userTrigsTemp.size(); k++)
						{
							UserActionTypeTrigger userTrig = (UserActionTypeTrigger)userTrigsTemp.elementAt(k);
							if(k > 0) // not on first element
							{
								writer.write("else ");
							}
							writer.write("if(menuText.equals(\"" + userTrig.getMenuText() + "\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);

							// go through all allowable types:
							Vector types = part.getAllSimSEObjectTypes();
							for(int m=0; m<types.size(); m++)
							{
								SimSEObjectType type = (SimSEObjectType)types.elementAt(m);
								if(m > 0) // not on first element
								{
									writer.write("else ");
								}
								writer.write("if((emp instanceof " + getUpperCaseLeading(type.getName()) + ")");

								// go through all attribute constraints:
								ActionTypeParticipantAttributeConstraint[] attConstraints =
									userTrig.getParticipantTrigger(part.getName()).getConstraint(type.getName()).getAllAttributeConstraints();
								for(int n=0; n<attConstraints.length; n++)
								{
									ActionTypeParticipantAttributeConstraint attConst = attConstraints[n];
									if(attConst.isConstrained())
									{
										writer.write(" && (((" + getUpperCaseLeading(type.getName()) + ")emp).get"
											+ attConst.getAttribute().getName() + "() ");
										if(attConst.getAttribute().getType() == AttributeTypes.STRING)
										{
											writer.write(".equals(" + "\"" + attConst.getValue().toString() + "\")");
										}
										else
										{
											if(attConst.getGuard().equals(AttributeGuard.EQUALS))
											{
												writer.write("== ");
											}
											else
											{
												writer.write(attConst.getGuard() + " ");
											}
											writer.write(attConst.getValue().toString());
										}
										writer.write(")");
									}
								}
								writer.write(")");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								writer.write("participantNames.add(\"" + getUpperCaseLeading(part.getName()) + "\");");
								writer.write(NEWLINE);
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("ChooseRoleToPlayDialog crtpd = new ChooseRoleToPlayDialog(owner, participantNames, emp, tempAct, menuText);");
			writer.write(NEWLINE);
			writer.write("dispose();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + catjdFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
