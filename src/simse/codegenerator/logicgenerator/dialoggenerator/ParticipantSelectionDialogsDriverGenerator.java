/* This class is responsible for generating all of the code for the logic's ParticipantSelectionDialogsDriver component */

package simse.codegenerator.logicgenerator.dialoggenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ParticipantSelectionDialogsDriverGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to generate into
	private File psddFile; // file to generate
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file

	public ParticipantSelectionDialogsDriverGenerator(DefinedActionTypes acts, File dir)
	{
		directory = dir;
		actTypes = acts;
	}


	public void generate()
	{
		try
		{
			psddFile = new File(directory, ("simse\\logic\\dialogs\\ParticipantSelectionDialogsDriver.java"));
			if(psddFile.exists())
			{
				psddFile.delete(); // delete old version of file
			}
			FileWriter writer = new FileWriter(psddFile);
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
			writer.write("public class ParticipantSelectionDialogsDriver");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// member variables:
			writer.write("private Vector partNames;");
			writer.write(NEWLINE);
			writer.write("private Vector partsVector;");
			writer.write(NEWLINE);
			writer.write("private simse.adts.actions.Action action;");
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Employee selectedEmp;");
			writer.write(NEWLINE);
			writer.write("private RuleExecutor ruleExec;");
			writer.write(NEWLINE);
			writer.write("private String menuText;");
			writer.write(NEWLINE);
			// constructor:
			writer.write("public ParticipantSelectionDialogsDriver(JFrame gui, Vector pNames, Vector parts, simse.adts.actions.Action act, State s, RuleExecutor re, Employee emp, String mText)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("partNames = pNames;");
			writer.write(NEWLINE);
			writer.write("partsVector = parts;");
			writer.write(NEWLINE);
			writer.write("action = act;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("selectedEmp = emp;");
			writer.write(NEWLINE);
			writer.write("ruleExec = re;");
			writer.write(NEWLINE);
			writer.write("menuText = mText;");
			writer.write(NEWLINE);
			writer.write("boolean actionValid = true;");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<partNames.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String participantName = (String)partNames.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("Vector participants = (Vector)partsVector.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("// check to see if any of these possible participants have already been added to the action in a different role:");
			writer.write(NEWLINE);
			writer.write("Vector allParts = action.getAllParticipants();");
			writer.write(NEWLINE);
			writer.write("Enumeration participantsEnum = participants.elements();");
			writer.write(NEWLINE);
			writer.write("while(participantsEnum.hasMoreElements())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("SSObject tempObj = (SSObject)participantsEnum.nextElement();");
			writer.write(NEWLINE);
			writer.write("for(int k=0; k<allParts.size(); k++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("SSObject tempObj2 = (SSObject)allParts.elementAt(k);");
			writer.write(NEWLINE);
			writer.write("if(tempObj == tempObj2)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("participants.remove(tempObj);");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write("if((participants.size() == 0) || ((SSObject)participants.elementAt(0) instanceof Employee))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(int j=0; j<allParts.size(); j++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("SSObject tempObj = (SSObject)allParts.elementAt(j);");
			writer.write(NEWLINE);
			writer.write("if((selectedEmp != null) && (tempObj == selectedEmp))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("selectedEmp = null;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("boolean participantsContainsSelEmp = false;");
			writer.write(NEWLINE);
			writer.write("Iterator participantsIterator = participants.iterator();");
			writer.write(NEWLINE);
			writer.write("while(participantsIterator.hasNext())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("SSObject tempObj = (SSObject)participantsIterator.next();");
			writer.write(NEWLINE);
			writer.write("if(tempObj == selectedEmp)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("participantsContainsSelEmp = true;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			writer.write("if((selectedEmp != null) && (participantsContainsSelEmp)) // selectedEmp needs to be added to the action as one of these participants");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("participants.remove(selectedEmp);");
			writer.write(NEWLINE);
			writer.write("EmployeeParticipantSelectionDialog psd = new EmployeeParticipantSelectionDialog(gui, participantName, participants, action, state, selectedEmp);");
			writer.write(NEWLINE);
			writer.write("if(psd.actionCancelled())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("actionValid = false;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else // pass null in instead of selectedEmp");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("EmployeeParticipantSelectionDialog psd = new EmployeeParticipantSelectionDialog(gui, participantName, participants, action, state, null);");
			writer.write(NEWLINE);
			writer.write("if(psd.actionCancelled())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("actionValid = false;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);		
			writer.write("NonEmployeeParticipantSelectionDialog psd = new NonEmployeeParticipantSelectionDialog(gui, participantName, participants, action, state);");			
			writer.write(NEWLINE);
			writer.write("if(psd.actionCancelled())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("actionValid = false;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(actionValid)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// gather all of the action types w/ user triggers:
			Vector acts = actTypes.getAllActionTypes();
			Vector userActs = new Vector();
			for(int i=0; i<acts.size(); i++)
			{
				ActionType act = (ActionType)acts.elementAt(i);
				Vector allTrigs = act.getAllTriggers();
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					if(tempTrig instanceof UserActionTypeTrigger)
					{
						userActs.add(act);
						break;
					}
				}
			}
			
			// go through each action type:
			for(int i=0; i<userActs.size(); i++)
			{
				ActionType tempAct = (ActionType)userActs.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(action instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector participants = action.getAllParticipants();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<participants.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("SSObject obj = (SSObject)participants.elementAt(i);");
				writer.write(NEWLINE);
				writer.write("if(obj instanceof Employee)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// generate conditions for each user trigger:
				Vector allTrigs = tempAct.getAllTriggers();
				boolean putElse9 = false;
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					if((tempTrig instanceof UserActionTypeTrigger) && (tempTrig.getTriggerText() != null) 
						&& (tempTrig.getTriggerText().length() > 0))
					{				
						if(putElse9)
						{
							writer.write("else ");
						}
						else
						{
							putElse9 = true;
						}
						writer.write("if(menuText.equals(\"" + ((UserActionTypeTrigger)tempTrig).getMenuText() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("((Employee)obj).setOverheadText(\"" + tempTrig.getTriggerText() + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("else if(obj instanceof Customer)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// generate conditions for each user trigger:
				boolean putElse8 = false;
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					if((tempTrig instanceof UserActionTypeTrigger) && (tempTrig.getTriggerText() != null) 
						&& (tempTrig.getTriggerText().length() > 0))
					{				
						if(putElse8)
						{
							writer.write("else ");
						}
						else
						{
							putElse8 = true;
						}
						writer.write("if(menuText.equals(\"" + ((UserActionTypeTrigger)tempTrig).getMenuText() + "\"))");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("((Customer)obj).setOverheadText(\"" + tempTrig.getTriggerText() + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}				
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(tempAct.getName()) + "ActionStateRepository().add(("
					+ getUpperCaseLeading(tempAct.getName()) + "Action)action);");
				writer.write(NEWLINE);
				// execute all trigger rules:
				Vector trigRules = tempAct.getAllTriggerRules();
				for(int j=0; j<trigRules.size(); j++)
				{
					Rule tRule = (Rule)trigRules.elementAt(j);
					writer.write("ruleExec.update(gui, RuleExecutor.UPDATE_ONE, \"" + tRule.getName() + "\", action);");
					writer.write(NEWLINE);
				}				
				
				// game-ending:
				if(tempAct.hasGameEndingTrigger())
				{
					Vector trigs = tempAct.getAllTriggers();
					boolean putElse7 = false;
					for(int j=0; j<trigs.size(); j++)
					{
						ActionTypeTrigger tempTrig = (ActionTypeTrigger)trigs.elementAt(j);
						if(tempTrig.isGameEndingTrigger())
						{
							if(putElse7)
							{
								writer.write("else ");
							}
							else
							{
								putElse7 = true;
							}
							writer.write("if(menuText.equals(\"" + ((UserActionTypeTrigger)tempTrig).getMenuText() + "\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
						
							writer.write("// stop game and give score:");
							writer.write(NEWLINE);
							writer.write(getUpperCaseLeading(tempAct.getName()) + "Action a = (" + getUpperCaseLeading(tempAct.getName()) 
								+ "Action)action;");
							writer.write(NEWLINE);
							// find the scoring attribute:
							ActionTypeParticipantTrigger scoringPartTrig = null;
							ActionTypeParticipantConstraint scoringPartConst = null;
							ActionTypeParticipantAttributeConstraint scoringAttConst = null;
							Vector partTrigs = tempTrig.getAllParticipantTriggers();
							for(int k=0; k<partTrigs.size(); k++)
							{
								ActionTypeParticipantTrigger partTrig = (ActionTypeParticipantTrigger)partTrigs.elementAt(k);
								Vector partConsts = partTrig.getAllConstraints();
								for(int m=0; m<partConsts.size(); m++)
								{
									ActionTypeParticipantConstraint partConst = (ActionTypeParticipantConstraint)partConsts.elementAt(m);
									ActionTypeParticipantAttributeConstraint[] attConsts = partConst.getAllAttributeConstraints();
									for(int n=0; n<attConsts.length; n++)
									{
										if(attConsts[n].isScoringAttribute())
										{
											scoringAttConst = attConsts[n];
											scoringPartConst = partConst;
											scoringPartTrig = partTrig;
											break;
										}
									}
								}
							}
							if((scoringAttConst != null) && (scoringPartConst != null) && (scoringPartTrig != null))
							{					
								writer.write("if(a.getAll" + scoringPartTrig.getParticipant().getName() + "s().size() > 0)");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);						
								writer.write(getUpperCaseLeading(scoringPartConst.getSimSEObjectType().getName()) + " t = ("
									+ getUpperCaseLeading(scoringPartConst.getSimSEObjectType().getName()) + ")(a.getAll" 
									+ scoringPartTrig.getParticipant().getName() + "s().elementAt(0));");
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
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);
							}
						}
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
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
			JOptionPane.showMessageDialog(null, ("Error writing file " + psddFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
