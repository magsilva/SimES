/* This class is responsible for generating all of the code for the logic's ChooseActionToDestroyDialog component */

package simse.codegenerator.logicgenerator.dialoggenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;
import simse.codegenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ChooseActionToDestroyDialogGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	*/

	private File directory; // directory to generate into
	private File catddFile; // file to generate
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file

	public ChooseActionToDestroyDialogGenerator(DefinedActionTypes acts, File dir)
	{
		directory = dir;
		actTypes = acts;
	}


	public void generate()
	{
		try
		{
			catddFile = new File(directory, ("simse\\logic\\dialogs\\ChooseActionToDestroyDialog.java"));
			if(catddFile.exists())
			{
				catddFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(catddFile);
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
			writer.write("import simse.logic.*;");
			writer.write(NEWLINE);
			writer.write("import simse.gui.*;");
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
			writer.write("public class ChooseActionToDestroyDialog extends JDialog implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// member variables:
			writer.write("private Vector actions;");
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private RuleExecutor ruleExec;");
			writer.write(NEWLINE);
			writer.write("private Employee emp;");
			writer.write(NEWLINE);
			writer.write("private String menuText;");
			writer.write(NEWLINE);
			writer.write("private JFrame gui;");
			writer.write(NEWLINE);
			writer.write("private Vector checkBoxes;");
			writer.write(NEWLINE);
			writer.write("private JButton okButton;");
			writer.write(NEWLINE);
			writer.write("private JButton cancelButton;");
			writer.write(NEWLINE);
			// constructor:
			writer.write("public ChooseActionToDestroyDialog(JFrame owner, Vector acts, State s, Employee e, RuleExecutor r, String mText)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("super(owner, true);");
			writer.write(NEWLINE);
			writer.write("actions = acts;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("ruleExec = r;");
			writer.write(NEWLINE);
			writer.write("gui = owner;");
			writer.write(NEWLINE);
			writer.write("emp = e;");
			writer.write(NEWLINE);
			writer.write("menuText = mText;");
			writer.write(NEWLINE);
			writer.write("checkBoxes = new Vector();");
			writer.write(NEWLINE);
			writer.write("setTitle(\"Stop Action(s)\");");
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

			// make a Vector of all the action types with user destroyers:
			Vector userDestActs = new Vector();
			Vector allActs = actTypes.getAllActionTypes();
			for(int j=0; j<allActs.size(); j++)
			{
				ActionType userAct = (ActionType)allActs.elementAt(j);
				Vector allDests = userAct.getAllDestroyers();
				for(int k=0; k<allDests.size(); k++)
				{
					ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(k);
					if(tempDest instanceof UserActionTypeDestroyer)
					{
						userDestActs.add(userAct);
						break;
					}
				}
			}
			// go through each action type and generate code for it:
			for(int j=0; j<userDestActs.size(); j++)
			{
				ActionType act = (ActionType)userDestActs.elementAt(j);
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
			writer.write("topPane.add(new JLabel(\"Choose which \" + actionName + \" Action to stop:\"));");
			writer.write(NEWLINE);
			writer.write("JPanel middlePane = new JPanel(new GridLayout(0, 1));");
			writer.write(NEWLINE);
			for(int j=0; j<userDestActs.size(); j++)
			{
				ActionType act = (ActionType)userDestActs.elementAt(j);
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
				writer.write("if(act.getAllParticipants().contains(emp))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
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
							+ tempType.getKey().getName() + "() + \")\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				writer.write("JPanel tempPane = new JPanel(new BorderLayout());");
				writer.write(NEWLINE);
				writer.write("JCheckBox tempCheckBox = new JCheckBox(label.toString());");
				writer.write(NEWLINE);
				writer.write("tempPane.add(tempCheckBox, BorderLayout.WEST);");
				writer.write(NEWLINE);
				writer.write("checkBoxes.add(tempCheckBox);");
				writer.write(NEWLINE);
				writer.write("middlePane.add(tempPane);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
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
			writer.write("setVisible(true);");
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
			writer.write("int numChecked = 0;");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<checkBoxes.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JCheckBox tempCBox = (JCheckBox)checkBoxes.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(tempCBox.isSelected())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("numChecked++;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(numChecked == 0)");
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
			writer.write("for(int i=0; i<checkBoxes.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JCheckBox cBox = (JCheckBox)checkBoxes.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(cBox.isSelected())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)actions.elementAt(i);");
			writer.write(NEWLINE);
			// go through all action types w/ user destroyers:
			for(int i=0; i<userDestActs.size(); i++)
			{
				ActionType tempAct = (ActionType)userDestActs.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(tempAct instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(tempAct.getName()) + "Action " + tempAct.getName().toLowerCase() + "Act = (" + getUpperCaseLeading(tempAct.getName())
					+ "Action)tempAct;");
				writer.write(NEWLINE);
				// go through all participants:
				Vector parts = tempAct.getAllParticipants();
				for(int j=0; j<parts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
					if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
						writer.write(tempAct.getName().toLowerCase() + "Act.remove" + part.getName() + "(emp);");
						writer.write(NEWLINE);
					}
				}

				// go through all of the action's user destroyers:
				Vector dests = tempAct.getAllDestroyers();
				boolean putElse = false;
				for(int j=0; j<dests.size(); j++)
				{
					ActionTypeDestroyer tempDest = (ActionTypeDestroyer)dests.elementAt(j);
					if((tempDest instanceof UserActionTypeDestroyer) && (tempDest.getDestroyerText() != null)
						&& (tempDest.getDestroyerText().length() > 0))
					{
						if(putElse)
						{
							writer.write("else ");
						}
						else
						{
							putElse = true;
						}
						writer.write("if(menuText.equals(\"" + ((UserActionTypeDestroyer)tempDest).getMenuText() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("emp.setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}

				// go through all parts again:
				for(int j=0; j<parts.size(); j++)
				{
					if(j > 0) // not on first element
					{
						writer.write("else ");
					}
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
					writer.write("if(" + tempAct.getName().toLowerCase() + "Act.getAll" + part.getName() + "s().size() < ");
					if(part.getQuantity().isMinValBoundless())
					{
						writer.write("0");
					}
					else
					{
						writer.write(part.getQuantity().getMinVal().toString());
					}
					writer.write(")");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("Vector c = " + tempAct.getName().toLowerCase() + "Act.getAllParticipants();");
					writer.write(NEWLINE);
					writer.write("for(int j=0; j<c.size(); j++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("SSObject d = (SSObject)c.elementAt(j);");
					writer.write(NEWLINE);
					writer.write("if(d instanceof Employee)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);

					// go through all user destroyers again:
					boolean putElse2 = false;
					for(int k=0; k<dests.size(); k++)
					{
						ActionTypeDestroyer tempDest = (ActionTypeDestroyer)dests.elementAt(k);
						if((tempDest instanceof UserActionTypeDestroyer) && (tempDest.getDestroyerText() != null)
							&& (tempDest.getDestroyerText().length() > 0))
						{
							if(putElse2)
							{
								writer.write("else ");
								writer.write(NEWLINE);
							}
							else
							{
								putElse2 = true;
							}
							writer.write("if(menuText.equals(\"" + ((UserActionTypeDestroyer)tempDest).getMenuText() + "\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("((Employee)d).setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write("else if(d instanceof Customer)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);

					// go through all user destroyers again:
					boolean putElse3 = false;
					for(int k=0; k<dests.size(); k++)
					{
						ActionTypeDestroyer tempDest = (ActionTypeDestroyer)dests.elementAt(k);
						if((tempDest instanceof UserActionTypeDestroyer) && (tempDest.getDestroyerText() != null)
							&& (tempDest.getDestroyerText().length() > 0))
						{
							if(putElse3)
							{
								writer.write("else ");
								writer.write(NEWLINE);
							}
							else
							{
								putElse3 = true;
							}
							writer.write("if(menuText.equals(\"" + ((UserActionTypeDestroyer)tempDest).getMenuText() + "\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("((Customer)d).setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);

					// execute all destroyer rules:
					Vector destRules = tempAct.getAllDestroyerRules();
					for(int k=0; k<destRules.size(); k++)
					{
						Rule dRule = (Rule)destRules.elementAt(k);
						writer.write("ruleExec.update(gui, RuleExecutor.UPDATE_ONE, \"" + dRule.getName() + "\", " + tempAct.getName().toLowerCase()
							+ "Act);");
						writer.write(NEWLINE);
					}
					writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(tempAct.getName()) + "ActionStateRepository().remove("
						+ tempAct.getName().toLowerCase() + "Act);");
					writer.write(NEWLINE);

					// game-ending:
					if(tempAct.hasGameEndingDestroyer())
					{
						// find all game-ending user destroyers:
						Vector allDests = tempAct.getAllDestroyers();
						boolean putElse711 = false;
						for(int k=0; k<allDests.size(); k++)
						{
							ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(k);
							if((tempDest instanceof UserActionTypeDestroyer) && (tempDest.isGameEndingDestroyer()))
							{
								if(putElse711)
								{
									writer.write("else ");
								}
								else
								{
									putElse711 = true;
								}
								writer.write("if(menuText.equals(\"" + ((UserActionTypeDestroyer)tempDest).getMenuText() + "\"))");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								writer.write("// stop game and give score:");
								writer.write(NEWLINE);
								writer.write(getUpperCaseLeading(tempAct.getName()) + "Action t111 = (" + getUpperCaseLeading(tempAct.getName())
									+ "Action)" + tempAct.getName().toLowerCase() + "Act;");
								writer.write(NEWLINE);
								// find the scoring attribute:
								ActionTypeParticipantDestroyer scoringPartDest = null;
								ActionTypeParticipantConstraint scoringPartConst = null;
								ActionTypeParticipantAttributeConstraint scoringAttConst = null;
								Vector partDests = tempDest.getAllParticipantDestroyers();
								for(int m=0; m<partDests.size(); m++)
								{
									ActionTypeParticipantDestroyer partDest = (ActionTypeParticipantDestroyer)partDests.elementAt(m);
									Vector partConsts = partDest.getAllConstraints();
									for(int n=0; n<partConsts.size(); n++)
									{
										ActionTypeParticipantConstraint partConst = (ActionTypeParticipantConstraint)partConsts.elementAt(n);
										ActionTypeParticipantAttributeConstraint[] attConsts = partConst.getAllAttributeConstraints();
										for(int p=0; p<attConsts.length; p++)
										{
											if(attConsts[p].isScoringAttribute())
											{
												scoringAttConst = attConsts[p];
												scoringPartConst = partConst;
												scoringPartDest = partDest;
												break;
											}
										}
									}
								}
								if((scoringAttConst != null) && (scoringPartConst != null) && (scoringPartDest != null))
								{
									writer.write("if(t111.getAll" + scoringPartDest.getParticipant().getName() + "s().size() > 0)");
									writer.write(NEWLINE);
									writer.write(OPEN_BRACK);
									writer.write(NEWLINE);
									writer.write(getUpperCaseLeading(scoringPartConst.getSimSEObjectType().getName()) + " t = ("
									+ getUpperCaseLeading(scoringPartConst.getSimSEObjectType().getName()) + ")(t111.getAll"
									+ scoringPartDest.getParticipant().getName() + "s().elementAt(0));");
									writer.write(NEWLINE);
									writer.write("if(t != null)");
									writer.write(NEWLINE);
									writer.write(OPEN_BRACK);
									writer.write(NEWLINE);
									if(scoringAttConst.getAttribute().getType() == AttributeTypes.INTEGER)
									{
										writer.write("int");
									}
									else if(scoringAttConst.getAttribute().getType() == AttributeTypes.DOUBLE)
									{
										writer.write("double");
									}
									else if(scoringAttConst.getAttribute().getType() == AttributeTypes.STRING)
									{
										writer.write("String");
									}
									else if(scoringAttConst.getAttribute().getType() == AttributeTypes.BOOLEAN)
									{
										writer.write("boolean");
									}
									writer.write(" v = t.get" + scoringAttConst.getAttribute().getName() + "();");
									writer.write(NEWLINE);
									writer.write("state.getClock().stop();");
									writer.write(NEWLINE);
									writer.write("((SimSEGUI)gui).update();");
									writer.write(NEWLINE);
									writer.write("JOptionPane.showMessageDialog(null, (\"Your score is \" + v), \"Game over!\", JOptionPane.INFORMATION_MESSAGE);");
									writer.write(NEWLINE);
									writer.write(CLOSED_BRACK);
									writer.write(NEWLINE);
									writer.write(CLOSED_BRACK);
									writer.write(NEWLINE);
								}
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
						}
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("setVisible(false);");
			writer.write(NEWLINE);
			writer.write("dispose();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
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
			JOptionPane.showMessageDialog(null, ("Error writing file " + catddFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
