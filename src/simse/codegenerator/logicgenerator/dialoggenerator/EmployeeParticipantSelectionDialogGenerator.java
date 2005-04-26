/* This class is responsible for generating all of the code for the logic's EmployeeParticipantSelectionDialog component */

package simse.codegenerator.logicgenerator.dialoggenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class EmployeeParticipantSelectionDialogGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to generate into
	private File psdFile; // file to generate
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file
	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file

	public EmployeeParticipantSelectionDialogGenerator(DefinedActionTypes acts, DefinedObjectTypes objs, File dir)
	{
		directory = dir;
		actTypes = acts;
		objTypes = objs;
	}


	public void generate()
	{
		try
		{
			psdFile = new File(directory, ("simse\\logic\\dialogs\\EmployeeParticipantSelectionDialog.java"));
			if(psdFile.exists())
			{
				psdFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(psdFile);
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
			writer.write("public class EmployeeParticipantSelectionDialog extends JDialog implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// member variables:
			writer.write("private String partName;");
			writer.write(NEWLINE);
			writer.write("private Vector participants;");
			writer.write(NEWLINE);
			writer.write("private simse.adts.actions.Action action;");
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Employee selectedEmp;");
			writer.write(NEWLINE);
			writer.write("private int minNumParts;");
			writer.write(NEWLINE);
			writer.write("private int maxNumParts;");
			writer.write(NEWLINE);
			writer.write("private Vector checkBoxes;");
			writer.write(NEWLINE);
			writer.write("private JButton okButton;");
			writer.write(NEWLINE);
			writer.write("private JButton cancelButton;");
			writer.write(NEWLINE);
			writer.write("private boolean actionCancelled;");
			writer.write(NEWLINE);
			// constructor:
			writer.write("public EmployeeParticipantSelectionDialog(JFrame owner, String pName, Vector parts, simse.adts.actions.Action act, State s, Employee emp)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("super(owner, true);");
			writer.write(NEWLINE);
			writer.write("partName = pName;");
			writer.write(NEWLINE);
			writer.write("participants = parts;");
			writer.write(NEWLINE);
			writer.write("action = act;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("selectedEmp = emp;");
			writer.write(NEWLINE);
			writer.write("actionCancelled = false;");
			writer.write(NEWLINE);
			writer.write("setMinAndMax();");
			writer.write(NEWLINE);
			writer.write("if(((selectedEmp != null) && (maxNumParts > 0) && (participants.size() > 0)) || ((selectedEmp == null) && (participants.size() > minNumParts)))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("checkBoxes = new Vector();");
			writer.write(NEWLINE);
			writer.write("setTitle(\"Participant Selection\");");
			writer.write(NEWLINE);
			// main pane:
			writer.write("Box mainPane = Box.createVerticalBox();");
			writer.write(NEWLINE);
			// top pane:
			writer.write("JPanel topPane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("String title = \"Choose \";");
			writer.write(NEWLINE);
			writer.write("if(selectedEmp != null) // selected emp already added in this participant role");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("title = title.concat(\"other \");");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("title = title.concat(partName + \" participant(s) (\");");
			writer.write(NEWLINE);
			writer.write("if(minNumParts == maxNumParts)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("title = title.concat(\"exactly \" + minNumParts);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("title = title.concat(\"at least \" + minNumParts);");
			writer.write(NEWLINE);
			writer.write("if(maxNumParts < 999999) // not boundless");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("title = title.concat(\", at most \" + maxNumParts);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("title = title.concat(\"):\");");
			writer.write(NEWLINE);
			writer.write("topPane.add(new JLabel(title));");
			writer.write(NEWLINE);
			// middle pane:
			writer.write("JPanel middlePane = new JPanel(new GridLayout(0, 1));");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<participants.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("SSObject tempObj = (SSObject)participants.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("String label = new String();");
			writer.write(NEWLINE);
			// go through each object type:
			Vector objs = objTypes.getAllObjectTypes();
			for(int i=0; i<objs.size(); i++)
			{
				SimSEObjectType tempType = (SimSEObjectType)objs.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(tempObj instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("label = (\"" + getUpperCaseLeading(tempType.getName()) + " (\" + ((" + getUpperCaseLeading(tempType.getName()) 
					+ ")tempObj).get" + tempType.getKey().getName() + "() + \")\");");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("JPanel tempPane = new JPanel(new BorderLayout());");
			writer.write(NEWLINE);
			writer.write("JCheckBox tempCheckBox = new JCheckBox(label);");
			writer.write(NEWLINE);
			writer.write("tempPane.add(tempCheckBox, BorderLayout.WEST);");
			writer.write(NEWLINE);
			writer.write("checkBoxes.add(tempCheckBox);");
			writer.write(NEWLINE);
			writer.write("middlePane.add(tempPane);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
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
			writer.write("setVisible(true);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(selectedEmp != null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// go through each action type:
			Vector acts = actTypes.getAllActionTypes();
			boolean putElse = false;
			for(int j=0; j<acts.size(); j++)
			{
				ActionType tempAct = (ActionType)acts.elementAt(j);
				Vector trigs = tempAct.getAllTriggers();
				// only generate code for actions w/ user triggers:
				for(int k=0; k<trigs.size(); k++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)trigs.elementAt(k);
					if(tempTrig instanceof UserActionTypeTrigger)
					{			
						if(putElse) // not on first element
						{
							writer.write("else ");
						}
						else
						{
							putElse = true;
						}
						writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						// go through all participants:
						Vector participants = tempAct.getAllParticipants();
						boolean nextOneWriteElse = false;
						for(int m=0; m<participants.size(); m++)
						{
							ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(m);
							if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // Employee participant
							{
								if(nextOneWriteElse) // not on first element
								{
									writer.write("else ");
								}
								writer.write("if(partName.equals(\"" + tempPart.getName() + "\"))");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								writer.write("((" + getUpperCaseLeading(tempAct.getName()) + "Action)action).add" + tempPart.getName()
									+ "(selectedEmp);");
								writer.write(NEWLINE);
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
								nextOneWriteElse = true;
							}
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write("if(participants.size() == 1)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Employee e = (Employee)participants.elementAt(0);");
			writer.write(NEWLINE);
			boolean putElse2 = false;
			for(int j=0; j<acts.size(); j++)
			{
				ActionType tempAct = (ActionType)acts.elementAt(j);
				Vector trigs = tempAct.getAllTriggers();
				// only generate code for actions w/ user triggers:
				for(int k=0; k<trigs.size(); k++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)trigs.elementAt(k);
					if(tempTrig instanceof UserActionTypeTrigger)
					{
						if(putElse2) // not on first element
						{
							writer.write("else ");
						}
						else
						{
							putElse2 = true;
						}
						writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						// go through all participants:
						Vector participants = tempAct.getAllParticipants();
						boolean nextOneWriteElse = false;
						for(int m=0; m<participants.size(); m++)
						{
							ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(m);
							if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // Employee participant
							{
								if(nextOneWriteElse) // not on first element
								{
									writer.write("else ");
								}
								writer.write("if(partName.equals(\"" + tempPart.getName() + "\"))");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								writer.write("((" + getUpperCaseLeading(tempAct.getName()) + "Action)action).add" + tempPart.getName()
									+ "(e);");
								writer.write(NEWLINE);
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
								nextOneWriteElse = true;
							}
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						break;
					}
				}
			}				
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
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
			writer.write("actionCancelled = true;");
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
			writer.write("Vector checkedBoxes = new Vector();");
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
			writer.write("checkedBoxes.add(tempCBox);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(checkedBoxes.size() < minNumParts)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JOptionPane.showMessageDialog(null, (\"You must choose at least \" + minNumParts + \" participants\"), \"Invalid Input\", JOptionPane.ERROR_MESSAGE);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if(checkedBoxes.size() > maxNumParts)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JOptionPane.showMessageDialog(null, (\"You may only choose at most \" + maxNumParts + \" participants\"), \"Invalid Input\", JOptionPane.ERROR_MESSAGE);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<checkedBoxes.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JCheckBox checkedBox = (JCheckBox)checkedBoxes.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("String cBoxText = checkedBox.getText();");
			writer.write(NEWLINE);
			writer.write("String objTypeName = cBoxText.substring(0, (cBoxText.indexOf('(') - 1));");
			writer.write(NEWLINE);
			writer.write("String keyValStr = cBoxText.substring((cBoxText.indexOf('(') + 1), cBoxText.lastIndexOf(')'));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("addParticipant(objTypeName, keyValStr);");
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
			writer.write(NEWLINE);
			
			// addParticipant function:
			writer.write("private void addParticipant(String objTypeName, String keyValStr)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);	
			boolean putElse9 = false;
			// go through each object type:
			for(int i=0; i<objs.size(); i++)
			{
				SimSEObjectType tempType = (SimSEObjectType)objs.elementAt(i);
				if(tempType.getType() == SimSEObjectTypeTypes.EMPLOYEE)
				{
					if(putElse9)
					{
						writer.write("else ");
					}
					else
					{
						putElse9 = true;
					}
					writer.write("if(objTypeName.equals(\"" + getUpperCaseLeading(tempType.getName()) + "\"))");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if((tempType.getKey().getType() == AttributeTypes.DOUBLE) || (tempType.getKey().getType() == AttributeTypes.INTEGER))
					{
						writer.write("try");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
					}
					writer.write(getUpperCaseLeading(tempType.getName()) + " a = state.get" + SimSEObjectTypeTypes.getText(tempType.getType())
						+ "StateRepository().get" + getUpperCaseLeading(tempType.getName()) + "StateRepository().get(");
					if(tempType.getKey().getType() == AttributeTypes.STRING)
					{
						writer.write("keyValStr);");
					}
					else if(tempType.getKey().getType() == AttributeTypes.BOOLEAN)
					{
						writer.write("(new Boolean(keyValStr)).booleanValue());");
					}
					else if(tempType.getKey().getType() == AttributeTypes.INTEGER)
					{
						writer.write("(new Integer(keyValStr)).intValue());");
					}
					else if(tempType.getKey().getType() == AttributeTypes.DOUBLE)
					{
						writer.write("(new Double(keyValStr)).doubleValue());");
					}
					writer.write(NEWLINE);
					writer.write("if(a != null)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					// go through each action type:
					boolean putElse88 = false;
					for(int j=0; j<acts.size(); j++)
					{
						ActionType tempAct = (ActionType)acts.elementAt(j);
						Vector trigs = tempAct.getAllTriggers();
						// only generate code for actions w/ user triggers:
						for(int k=0; k<trigs.size(); k++)
						{
							ActionTypeTrigger tempTrig = (ActionTypeTrigger)trigs.elementAt(k);
							if(tempTrig instanceof UserActionTypeTrigger)
							{					
								if(putElse88) // not on first element
								{
									writer.write("else ");
								}
								else
								{
									putElse88 = true;
								}
								writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								// go through all participants:
								Vector participants = tempAct.getAllParticipants();
								boolean nextOneWriteElse = false;
								for(int m=0; m<participants.size(); m++)
								{
									ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(m);
									if(tempPart.getSimSEObjectType(tempType.getName()) != null) // this SimSEObjectType is an allowable type for this participant
									{
										if(nextOneWriteElse) // not on first element
										{
											writer.write("else ");
										}
										writer.write("if(partName.equals(\"" + tempPart.getName() + "\"))");
										writer.write(NEWLINE);
										writer.write(OPEN_BRACK);
										writer.write(NEWLINE);
										writer.write("((" + getUpperCaseLeading(tempAct.getName()) + "Action)action).add" + tempPart.getName()
											+ "((" + SimSEObjectTypeTypes.getText(tempType.getType()) + ")a);");
										writer.write(NEWLINE);
										writer.write(CLOSED_BRACK);
										writer.write(NEWLINE);
										nextOneWriteElse = true;
									}
								}
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
						}
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					if((tempType.getKey().getType() == AttributeTypes.INTEGER) || (tempType.getKey().getType() == AttributeTypes.DOUBLE))
					{
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("catch(NumberFormatException e)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("System.out.println(e);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
			}	
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);			
			
			// setMinandMax function:
			writer.write("private void setMinAndMax()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// go through each action:
			for(int i=0; i<acts.size(); i++)
			{
				ActionType tempAct = (ActionType)acts.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				// go through each participant:
				Vector participants = tempAct.getAllParticipants();
				for(int j=0; j<participants.size(); j++)
				{
					if(j > 0) // not on first element
					{
						writer.write("else ");
					}
					ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(j);
					writer.write("if(partName.equals(\"" + tempPart.getName() + "\"))");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
						writer.write("if(selectedEmp == null)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
					}
					writer.write("minNumParts = ");
					if(tempPart.getQuantity().isMinValBoundless())
					{
						writer.write("0;");
					}
					else // min val has a value
					{
						writer.write(tempPart.getQuantity().getMinVal().toString() + ";");
					}
					writer.write(NEWLINE);
					writer.write("maxNumParts = ");
					if(tempPart.getQuantity().isMaxValBoundless())
					{
						writer.write("999999;");
					}
					else // max val has a value
					{
						writer.write(tempPart.getQuantity().getMaxVal().toString() + ";");
					}
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("else");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("minNumParts = ");
						if(tempPart.getQuantity().isMinValBoundless())
						{
							writer.write("0;");
						}
						else // min val has a value
						{
							writer.write(tempPart.getQuantity().getMinVal().toString() + " - 1;");
						}
						writer.write(NEWLINE);
						writer.write("maxNumParts = ");
						if(tempPart.getQuantity().isMaxValBoundless())
						{
							writer.write("999999;");
						}
						else // max val has a value
						{
							writer.write(tempPart.getQuantity().getMaxVal().toString() + " - 1;");
						}
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// actionCancelled function:
			writer.write("public boolean actionCancelled()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return actionCancelled;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);		
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + psdFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
