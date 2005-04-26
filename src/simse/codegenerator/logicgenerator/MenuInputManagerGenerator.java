/* This class is responsible for generating all of the code for the logic's MenuInputManager component */

package simse.codegenerator.logicgenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class MenuInputManagerGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	
	private File directory; // directory to generate into
	private File mimFile; // file to generate
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file
	private Vector vectors; // for keeping track of which vectors are being used in generated code so that you don't generate the
	// same ones more than once -- e.g., Vector programmers =
	// state.getEmployeeStateRepository().getProgrammerStateRepository().getAll() will be generated more than once if you
	// don't keep track of this.
	
	public MenuInputManagerGenerator(DefinedActionTypes acts, File dir)
	{
		directory = dir;
		actTypes = acts;
		vectors = new Vector();
	}
	
	
	public void generate()
	{
		try
		{
			mimFile = new File(directory, ("simse\\logic\\MenuInputManager.java"));
			if(mimFile.exists())
			{
				mimFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(mimFile);
			writer.write("package simse.logic;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.gui.*;");
			writer.write(NEWLINE);			
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.actions.*;");
			writer.write(NEWLINE);
			writer.write("import simse.logic.dialogs.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("public class MenuInputManager");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private TriggerChecker trigChecker;");
			writer.write(NEWLINE);
			writer.write("private DestroyerChecker destChecker;");
			writer.write(NEWLINE);
			writer.write("private RuleExecutor ruleExec;");
			writer.write(NEWLINE);
			writer.write("public MenuInputManager(State s, TriggerChecker t, DestroyerChecker d, RuleExecutor r)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("trigChecker = t;");
			writer.write(NEWLINE);
			writer.write("destChecker = d;");
			writer.write(NEWLINE);
			writer.write("ruleExec = r;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// menuItemSelected method:
			writer.write("public void menuItemSelected(Employee e, String s, JFrame parent)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("boolean hasStr = false;");
			writer.write(NEWLINE);
			writer.write("Vector menu = e.getMenu();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<menu.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String menuItem = (String)menu.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(menuItem.equals(s))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("hasStr = true;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(!hasStr)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(s.equals(\"Everyone stop what you're doing\"))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("int choice = JOptionPane.showConfirmDialog(null, (\"Are you sure you want everyone to stop what they're doing?\"), \"Confirm Activities Ending\", JOptionPane.YES_NO_OPTION);");
			writer.write(NEWLINE);
			writer.write("if(choice == JOptionPane.YES_OPTION)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);			
			writer.write("Vector allEmps = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("for(int z=0; z<allEmps.size(); z++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Employee emp = (Employee)allEmps.elementAt(z);");
			writer.write(NEWLINE);
			
			// user destroyers:
			// make a Vector of all the user destroyers:
			Vector userDests = new Vector();
			Vector actions = actTypes.getAllActionTypes();
			for(int j=0; j<actions.size(); j++)
			{
				ActionType act = (ActionType)actions.elementAt(j);
				Vector allDests = act.getAllDestroyers();
				for(int k=0; k<allDests.size(); k++)
				{
					ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(k);
					if(tempDest instanceof UserActionTypeDestroyer)
					{
						userDests.add(tempDest);
					}
				}
			}		
			
			// go through each destroyer and generate code for it:
			for(int j=0; j<userDests.size(); j++)
			{
				ActionTypeDestroyer outerDest = (ActionTypeDestroyer)userDests.elementAt(j);
				ActionType act = outerDest.getActionType();
				writer.write("// " + ((UserActionTypeDestroyer)outerDest).getMenuText() + ":");
				writer.write(NEWLINE);
				writer.write("Vector allActions" + j + " = state.getActionStateRepository().get" + getUpperCaseLeading(act.getName()) 
					+ "ActionStateRepository().getAllActions();");
				writer.write(NEWLINE);
				writer.write("int a" + j + " = 0;");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<allActions" + j + ".size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action b" + j + " = (" + getUpperCaseLeading(act.getName()) 
					+ "Action)allActions" + j + ".elementAt(i);");
				writer.write(NEWLINE);
				writer.write("if(b" + j + ".getAllParticipants().contains(emp))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("a" + j + "++;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("if(a" + j + " == 1)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<allActions" + j + ".size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action b" + j + " = (" + getUpperCaseLeading(act.getName()) 
					+ "Action)allActions" + j + ".elementAt(i);");
				writer.write(NEWLINE);
				// go through all participants:
				Vector parts = act.getAllParticipants();
				for(int k=0; k<parts.size(); k++)
				{
					ActionTypeParticipant tempPart = (ActionTypeParticipant)parts.elementAt(k);
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // participant is of employee type
					{
						writer.write("if(b" + j + ".getAll" + tempPart.getName() + "s().contains(emp))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						
						// execute all destroyer rules:
						Vector destRules = act.getAllDestroyerRules();
						for(int i=0; i<destRules.size(); i++)
						{
							Rule dRule = (Rule)destRules.elementAt(i);
							writer.write("ruleExec.update(parent, RuleExecutor.UPDATE_ONE, \"" + dRule.getName() + "\", b" + j + ");");
							writer.write(NEWLINE);
						}
						
						writer.write("b" + j + ".remove" + tempPart.getName() + "(emp);");
						writer.write(NEWLINE);
						if((outerDest.getDestroyerText() != null) && (outerDest.getDestroyerText().length() > 0))
						{						
							writer.write("emp.setOverheadText(\"" + ((UserActionTypeDestroyer)outerDest).getDestroyerText() + "\");");
							writer.write(NEWLINE);
						}
						writer.write("if(b" + j + ".getAll" + tempPart.getName() + "s().size() < ");
						if(tempPart.getQuantity().isMinValBoundless()) // no minimum
						{
							writer.write("0)");
						}
						else // has a minimum
						{
							writer.write(tempPart.getQuantity().getMinVal().intValue() + ")");
						}
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("Vector c" + j + " = b" + j + ".getAllParticipants();");
						writer.write(NEWLINE);
						writer.write("for(int j=0; j<c" + j + ".size(); j++)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("SSObject d" + j + " = (SSObject)c" + j + ".elementAt(j);");
						writer.write(NEWLINE);
						writer.write("if(d" + j + " instanceof Employee)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						if((outerDest.getDestroyerText() != null) && (outerDest.getDestroyerText().length() > 0))
						{						
							writer.write("((Employee)d" + j + ").setOverheadText(\"" + ((UserActionTypeDestroyer)outerDest).getDestroyerText() + "\");");
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("else if(d" + j + " instanceof Customer)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						if((outerDest.getDestroyerText() != null) && (outerDest.getDestroyerText().length() > 0))
						{						
							writer.write("((Customer)d" + j + ").setOverheadText(\"" + ((UserActionTypeDestroyer)outerDest).getDestroyerText() + "\");");
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);

						// remove action from repository:
						writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(act.getName()) 
							+ "ActionStateRepository().remove(b" + j + ");");
						writer.write(NEWLINE);
						
						// game-ending:
						if(outerDest.isGameEndingDestroyer())
						{
							writer.write("// stop game and give score:");
							writer.write(NEWLINE);
							writer.write(getUpperCaseLeading(act.getName()) + "Action t111" + j + " = (" + getUpperCaseLeading(act.getName()) 
								+ "Action)b" + j + ";");
							writer.write(NEWLINE);
							// find the scoring attribute:
							ActionTypeParticipantDestroyer scoringPartDest = null;
							ActionTypeParticipantConstraint scoringPartConst = null;
							ActionTypeParticipantAttributeConstraint scoringAttConst = null;
							Vector partDests = outerDest.getAllParticipantDestroyers();
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
								writer.write("if(t111" + j + ".getAll" + scoringPartDest.getParticipant().getName() + "s().size() > 0)");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);								
								writer.write(getUpperCaseLeading(scoringPartConst.getSimSEObjectType().getName()) + " t" + j + " = ("
									+ getUpperCaseLeading(scoringPartConst.getSimSEObjectType().getName()) + ")(t111" + j + ".getAll" 
									+ scoringPartDest.getParticipant().getName() + "s().elementAt(0));");
								writer.write(NEWLINE);
								writer.write("if(t" + j + " != null)");
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
								writer.write(" v" + j + " = t" + j + ".get" + scoringAttConst.getAttribute().getName() + "();");						
								writer.write(NEWLINE);
								writer.write("state.getClock().stop();");
								writer.write(NEWLINE);
								writer.write("((SimSEGUI)parent).update();");
								writer.write(NEWLINE);
								writer.write("JOptionPane.showMessageDialog(null, (\"Your score is \" + v" + j + "), \"Game over!\", JOptionPane.INFORMATION_MESSAGE);");
								writer.write(NEWLINE);								
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
						}						
						
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("else if(a" + j + " > 1)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector b" + j + " = new Vector();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<allActions" + j + ".size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action c" + j + " = (" + getUpperCaseLeading(act.getName()) + "Action)allActions" + j + ".elementAt(i);");
				writer.write(NEWLINE);
				// go through all participants:
				for(int k=0; k<parts.size(); k++)
				{
					ActionTypeParticipant tempPart = (ActionTypeParticipant)parts.elementAt(k);	
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
						writer.write("if((c" + j + ".getAll" + tempPart.getName() + "s().contains(emp)) && (!(b" + j + ".contains(c" + j + "))))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("b" + j + ".add(c" + j + ");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("ChooseActionToDestroyDialog z" + j + " = new ChooseActionToDestroyDialog(parent, b" + j + ", state, emp, ruleExec, s);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			
			// make a Vector of all the user triggers:
			Vector userTrigs = new Vector();
			for(int i=0; i<actions.size(); i++)
			{
				ActionType act = (ActionType)actions.elementAt(i);
				Vector allTrigs = act.getAllTriggers();
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					if(tempTrig instanceof UserActionTypeTrigger)
					{
						userTrigs.add(tempTrig);
					}
				}
			}
			// go through each trigger and generate code for it:
			for(int i=0; i<userTrigs.size(); i++)
			{
				vectors.removeAllElements(); // clear vector
				ActionTypeTrigger outerTrig = (ActionTypeTrigger)userTrigs.elementAt(i);
				ActionType act = outerTrig.getActionType();
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(s.equals(\"" + ((UserActionTypeTrigger)outerTrig).getMenuText() + "\"))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// game-ending triggers:
				if(outerTrig.isGameEndingTrigger())
				{
					writer.write("int choice = JOptionPane.showConfirmDialog(null, (\"Are you sure you want to end the game?\"), \"Confirm Game Ending\", JOptionPane.YES_NO_OPTION);");
					writer.write(NEWLINE);
					writer.write("if(choice == JOptionPane.YES_OPTION)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
				}
				
				Vector triggers = outerTrig.getAllParticipantTriggers();
				for(int j=0; j<triggers.size(); j++)
				{
					ActionTypeParticipantTrigger trig = (ActionTypeParticipantTrigger)triggers.elementAt(j);
					writer.write("Vector " + trig.getParticipant().getName().toLowerCase() + "s" + j + " = new Vector();");
					writer.write(NEWLINE);
					Vector constraints = trig.getAllConstraints();
					for(int k=0; k<constraints.size(); k++)
					{
						ActionTypeParticipantConstraint constraint = (ActionTypeParticipantConstraint)constraints.elementAt(k);
						String objTypeName = constraint.getSimSEObjectType().getName();
						if(vectorContainsString(vectors, (objTypeName.toLowerCase() + "s")) == false) // this vector has not been generated already
						{
							writer.write("Vector " + objTypeName.toLowerCase() + "s = state.get"
								+ getUpperCaseLeading(SimSEObjectTypeTypes.getText(constraint.getSimSEObjectType().getType()))
								+ "StateRepository().get" + getUpperCaseLeading(objTypeName) + "StateRepository().getAll();");
							// generate it
							vectors.add(objTypeName.toLowerCase() + "s"); // add it to the list
							writer.write(NEWLINE);
						}										
						writer.write("for(int i=0; i<" + objTypeName.toLowerCase() + "s.size(); i++)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(getUpperCaseLeading(objTypeName) + " a = (" + getUpperCaseLeading(objTypeName) + ")"
							+ objTypeName.toLowerCase() + "s.elementAt(i);");
						writer.write(NEWLINE);
						writer.write("boolean alreadyInAction = false;");
						writer.write(NEWLINE);							
						if((trig.getParticipant().getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
							|| (trig.getParticipant().getSimSEObjectTypeType() == SimSEObjectTypeTypes.ARTIFACT)) // employees and artifacts can
								//only be in one of these actions in this role at a time
						{
							writer.write("Vector allActions = state.getActionStateRepository().get"
								+ getUpperCaseLeading(act.getName())
								+ "ActionStateRepository().getAllActions(a);");
							writer.write(NEWLINE);
							writer.write("for(int j=0; j<allActions.size(); j++)");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write(getUpperCaseLeading(act.getName()) + "Action b = (" + getUpperCaseLeading(act.getName())
								+ "Action)allActions.elementAt(j);");
							writer.write(NEWLINE);
							writer.write("if(b.getAll" + trig.getParticipant().getName() + "s().contains(a))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("alreadyInAction = true;");
							writer.write(NEWLINE);
							writer.write("break;");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
						writer.write("if((alreadyInAction == false)");
						ActionTypeParticipantAttributeConstraint[] attConstraints = constraint.getAllAttributeConstraints();
						for(int m=0; m<attConstraints.length; m++)
						{
							ActionTypeParticipantAttributeConstraint attConst = attConstraints[m];
							if(attConst.isConstrained())
							{
								writer.write(" && (a.get" + attConst.getAttribute().getName() + "() ");
								if(attConst.getGuard().equals(AttributeGuard.EQUALS))
								{
									writer.write("== ");
								}
								else
								{
									writer.write(attConst.getGuard() + " ");
								}
								if(attConst.getAttribute().getType() == AttributeTypes.STRING)
								{
									writer.write("\"" + attConst.getValue().toString() + "\"");
								}
								else
								{
									writer.write(attConst.getValue().toString());
								}
								writer.write(")");
							}
						}
						writer.write(")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(trig.getParticipant().getName().toLowerCase() + "s" + j + ".add(a);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}	
				writer.write("if(");
				Vector parts = act.getAllParticipants();
				for(int j=0; j<parts.size(); j++)
				{
					if(j > 0) // not on first element
					{
						writer.write(" && ");
					}
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
					writer.write("(" + part.getName().toLowerCase() + "s" + j + ".size() ");
					if(part.getQuantity().isMinValBoundless() == false)
					{
						writer.write(">= " + part.getQuantity().getMinVal() + ")");
					}
					else // min val boundless
					{
						writer.write(">= 0)");
					}
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector c = new Vector();");
				writer.write(NEWLINE);
				
				// NOTE: this following stuff was commented out because it wasn't working right:
				//boolean moreThan1RoleForSameEmployeeType = false;
				for(int j=0; j<parts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
					writer.write("c.add(\"" + part.getName() + "\");");
					writer.write(NEWLINE);
					/*if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
					Vector partTypes = part.getAllSimSEObjectTypes();
					// go through the participant types:
					for(int k=0; k<partTypes.size(); k++)
					{
					SimSEObjectType tempType = (SimSEObjectType)partTypes.elementAt(k);	
					// go through the rest of the participants:
					for(int m=0; m<parts.size(); m++)
					{
					ActionTypeParticipant part2 = (ActionTypeParticipant)parts.elementAt(m);
					if((part2 != part) && (part2.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)) // Employee participant role but
					// not the same participant
					{
					// go through their allowable types:
					Vector partTypes2 = part2.getAllSimSEObjectTypes();
					for(int n=0; n<partTypes2.size(); n++)
					{
					SimSEObjectType tempType2 = (SimSEObjectType)partTypes2.elementAt(n);
					if(tempType2.getName().equals(tempType.getName())) // same type!
					{
					moreThan1RoleForSameEmployeeType = true;
					break;
					}
					}
					}
					}
					}
					}*/
				}
				/*if(!moreThan1RoleForSameEmployeeType) // NOT > 1 role for the same employee SimSEObjectType
				{
				boolean putElse3 = false;
				// go through participants:
				for(int j=0; j<parts.size(); j++)
				{
				ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
				if(part.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
				{
				if(putElse3)
				{
				writer.write("else ");
				}
				else
				{
				putElse3 = true;
				}
				writer.write("if(");
				// go through all SimSEObjectTypes:
				Vector allowableTypes = part.getAllSimSEObjectTypes();
				for(int k=0; k<allowableTypes.size(); k++)
				{
				if(k > 0) // not on first element
				{
				writer.write(" || ");
				}
				SimSEObjectType ssType = (SimSEObjectType)allowableTypes.elementAt(k);
				writer.write("(e instanceof " + getUpperCaseLeading(ssType.getName()) + ")");
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(part.getName().toLowerCase() + "s" + j + ".remove(e);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				}
				}
				}		*/			
				writer.write("Vector d = new Vector();");
				writer.write(NEWLINE);
				
				for(int j=0; j<parts.size(); j++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
					writer.write("d.add(" + part.getName().toLowerCase() + "s" + j + ");");
					writer.write(NEWLINE);
				}
				writer.write(getUpperCaseLeading(act.getName()) + "Action f = new " + getUpperCaseLeading(act.getName()) + "Action();");
				writer.write(NEWLINE);
				writer.write("ParticipantSelectionDialogsDriver g = new ParticipantSelectionDialogsDriver(parent, c, d, f, state, ruleExec, e, s);");
				/*if(!moreThan1RoleForSameEmployeeType) // NOT more than one participant role for the same employee SimSEObjectType
				{
				writer.write("e");
				}
				else // more than one employee participant role for the same SimSEObjectType
				{
				writer.write("null");
				}
				writer.write(");");*/
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				if(outerTrig.isGameEndingTrigger()) // add extra closed brack
				{
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// JOINING existing actions:
				writer.write("else if(s.equals(\"JOIN " + ((UserActionTypeTrigger)outerTrig).getMenuText() + "\"))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector a = state.getActionStateRepository().get" + getUpperCaseLeading(act.getName()) + "ActionStateRepository().getAllActions();");
				writer.write(NEWLINE);
				writer.write("Vector b = new Vector();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<a.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action c = (" + getUpperCaseLeading(act.getName()) + "Action)a.elementAt(i);");
				writer.write(NEWLINE);
				// go through all participants:
				for(int j=0; j<parts.size(); j++)
				{
					ActionTypeParticipant tempPart = (ActionTypeParticipant)parts.elementAt(j);
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
						writer.write("if((c.getAll" + tempPart.getName() + "s().contains(e) == false) && (b.contains(c) == false))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("b.add(c);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("ChooseActionToJoinDialog catjd = new ChooseActionToJoinDialog(parent, b, e, state, \"" + ((UserActionTypeTrigger)outerTrig).getMenuText() 
					+ "\");");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			
			// go through each destroyer and generate code for it:
			for(int j=0; j<userDests.size(); j++)
			{
				ActionTypeDestroyer outerDest = (ActionTypeDestroyer)userDests.elementAt(j);
				ActionType act = outerDest.getActionType();
				writer.write("else if(s.equals(\"" + ((UserActionTypeDestroyer)outerDest).getMenuText() + "\"))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);				
				writer.write("Vector allActions = state.getActionStateRepository().get" + getUpperCaseLeading(act.getName()) + "ActionStateRepository().getAllActions();");
				writer.write(NEWLINE);
				writer.write("int a = 0;");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<allActions.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action b = (" + getUpperCaseLeading(act.getName()) + "Action)allActions.elementAt(i);");
				writer.write(NEWLINE);
				writer.write("if(b.getAllParticipants().contains(e))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("a++;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("if(a == 1)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<allActions.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action b = (" + getUpperCaseLeading(act.getName()) + "Action)allActions.elementAt(i);");
				writer.write(NEWLINE);
				// go through all participants:
				Vector parts = act.getAllParticipants();
				for(int k=0; k<parts.size(); k++)
				{
					ActionTypeParticipant tempPart = (ActionTypeParticipant)parts.elementAt(k);
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE) // participant is of employee type
					{
						writer.write("if(b.getAll" + tempPart.getName() + "s().contains(e))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						
						// execute all destroyer rules:
						Vector destRules = act.getAllDestroyerRules();
						for(int i=0; i<destRules.size(); i++)
						{
							Rule dRule = (Rule)destRules.elementAt(i);
							writer.write("ruleExec.update(parent, RuleExecutor.UPDATE_ONE, \"" + dRule.getName() + "\", b);");
							writer.write(NEWLINE);
						}
						
						writer.write("b.remove" + tempPart.getName() + "(e);");
						writer.write(NEWLINE);
						if((outerDest.getDestroyerText() != null) && (outerDest.getDestroyerText().length() > 0))
						{						
							writer.write("e.setOverheadText(\"" + ((UserActionTypeDestroyer)outerDest).getDestroyerText() + "\");");
							writer.write(NEWLINE);
						}
						writer.write("if(b.getAll" + tempPart.getName() + "s().size() < ");
						if(tempPart.getQuantity().isMinValBoundless()) // no minimum
						{
							writer.write("0)");
						}
						else // has a minimum
						{
							writer.write(tempPart.getQuantity().getMinVal().intValue() + ")");
						}
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("Vector c = b.getAllParticipants();");
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
						if((outerDest.getDestroyerText() != null) && (outerDest.getDestroyerText().length() > 0))
						{						
							writer.write("((Employee)d).setOverheadText(\"" + ((UserActionTypeDestroyer)outerDest).getDestroyerText() + "\");");
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("else if(d instanceof Customer)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						if((outerDest.getDestroyerText() != null) && (outerDest.getDestroyerText().length() > 0))
						{						
							writer.write("((Customer)d).setOverheadText(\"" + ((UserActionTypeDestroyer)outerDest).getDestroyerText() + "\");");
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						
						// remove action from repository:
						writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(act.getName()) 
							+ "ActionStateRepository().remove(b);");
						writer.write(NEWLINE);
						
						// game-ending:
						if(outerDest.isGameEndingDestroyer())
						{
							writer.write("// stop game and give score:");
							writer.write(NEWLINE);
							writer.write(getUpperCaseLeading(act.getName()) + "Action t111 = (" + getUpperCaseLeading(act.getName()) 
								+ "Action)b;");
							writer.write(NEWLINE);
							// find the scoring attribute:
							ActionTypeParticipantDestroyer scoringPartDest = null;
							ActionTypeParticipantConstraint scoringPartConst = null;
							ActionTypeParticipantAttributeConstraint scoringAttConst = null;
							Vector partDests = outerDest.getAllParticipantDestroyers();
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
								writer.write("((SimSEGUI)parent).update();");
								writer.write(NEWLINE);
								writer.write("JOptionPane.showMessageDialog(null, (\"Your score is \" + v), \"Game over!\", JOptionPane.INFORMATION_MESSAGE);");
								writer.write(NEWLINE);								
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
						}						
						
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("else");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector b = new Vector();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<allActions.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action c = (" + getUpperCaseLeading(act.getName()) + "Action)allActions.elementAt(i);");
				writer.write(NEWLINE);
				// go through all participants:
				for(int k=0; k<parts.size(); k++)
				{
					ActionTypeParticipant tempPart = (ActionTypeParticipant)parts.elementAt(k);	
					if(tempPart.getSimSEObjectTypeType() == SimSEObjectTypeTypes.EMPLOYEE)
					{
						writer.write("if((c.getAll" + tempPart.getName() + "s().contains(e)) && (!(b.contains(c))))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("b.add(c);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("ChooseActionToDestroyDialog z = new ChooseActionToDestroyDialog(parent, b, state, e, ruleExec, s);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);				
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("// update all employees' menus:");
			writer.write(NEWLINE);
			writer.write("Vector allEmps = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<allEmps.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("((Employee)allEmps.elementAt(i)).clearMenu();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("// update trigger checker:");
			writer.write(NEWLINE);
			writer.write("trigChecker.update(true, parent);");
			writer.write(NEWLINE);
			writer.write("// update destroyer checker:");
			writer.write(NEWLINE);
			writer.write("destChecker.update(true, parent);");
			writer.write(NEWLINE);	
			writer.write("// update gui:");
			writer.write(NEWLINE);
			writer.write("((SimSEGUI)parent).update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + mimFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
	
	
	private boolean vectorContainsString(Vector v, String s)
	{
		for(int i=0; i<v.size(); i++)
		{
			String temp = (String)v.elementAt(i);
			if(temp.equals(s))
			{
				return true;
			}
		}
		return false;
	}	
}
