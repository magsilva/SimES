/* This class is responsible for generating all of the code for the destroyer checker component in the Logic component of SimSE*/

package simse.codegenerator.logicgenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class DestroyerCheckerGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	
	private File directory; // directory to save generated code into
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file
	private FileWriter writer;
	private File destFile;
	private Vector nonPrioritizedDestroyers;
	private Vector prioritizedDestroyers;	
	private Vector allDestroyers;
	
	public DestroyerCheckerGenerator(DefinedActionTypes dats, File dir)
	{
		actTypes = dats;
		directory = dir;
		initializeDestroyerLists();
	}
	
	
	public void generate()
	{
		try
		{
			destFile = new File(directory, ("simse\\logic\\DestroyerChecker.java"));
			if(destFile.exists())
			{
				destFile.delete(); // delete old version of file
			}
			writer = new FileWriter(destFile);
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
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("public class DestroyerChecker");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			
			// member variables:
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private RuleExecutor ruleExec;");
			writer.write(NEWLINE);
			writer.write("private TriggerChecker trigCheck;");
			writer.write(NEWLINE);
			writer.write("private Random ranNumGen;");
			writer.write(NEWLINE);
			
			// constructor:
			writer.write("public DestroyerChecker(State s, RuleExecutor r, TriggerChecker t)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("ruleExec = r;");
			writer.write(NEWLINE);
			writer.write("trigCheck = t;");
			writer.write(NEWLINE);
			writer.write("ranNumGen = new Random();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// update function:
			writer.write("public void update(boolean updateUserDestsOnly, JFrame gui)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector actions = state.getActionStateRepository().getAllActions();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<actions.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)actions.elementAt(i);");
			writer.write(NEWLINE);
			// go through each destroyer:
			for(int i=0; i<allDestroyers.size(); i++)
			{
				ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDestroyers.elementAt(i);
				ActionType tempAct = tempDest.getActionType();
				/*if(i > 0) // not on first element
				{
					writer.write("else ");
				}*/
				writer.write("if((tempAct instanceof " + getUpperCaseLeading(tempAct.getName()) + "Action) && (state.getActionStateRepository().get" + getUpperCaseLeading(tempAct.getName()) + "ActionStateRepository().getAllActions().contains(tempAct)))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				if(tempDest instanceof TimedActionTypeDestroyer) // timed destroyer
				{
					writer.write("if(!updateUserDestsOnly)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);					
					writer.write("if(((" + getUpperCaseLeading(tempAct.getName()) + "Action)tempAct).getTimeToLive() == 0)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("Vector b = tempAct.getAllParticipants();");
					writer.write(NEWLINE);
					writer.write("for(int j=0; j<b.size(); j++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("SSObject c = (SSObject)b.elementAt(j);");
					writer.write(NEWLINE);
					writer.write("if(c instanceof Employee)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if((tempDest.getDestroyerText() != null) && (tempDest.getDestroyerText().length() > 0))
					{
						writer.write("((Employee)c).setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
						writer.write(NEWLINE);
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write("else if(c instanceof Customer)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if((tempDest.getDestroyerText() != null) && (tempDest.getDestroyerText().length() > 0))
					{					
						writer.write("((Customer)c).setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
					}
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);

					// execute all destroyer rules:
					Vector destRules = tempAct.getAllDestroyerRules();
					for(int k=0; k<destRules.size(); k++)
					{
						Rule dRule = (Rule)destRules.elementAt(k);
						writer.write("ruleExec.update(gui, RuleExecutor.UPDATE_ONE, \"" + dRule.getName() + "\", tempAct);");
						writer.write(NEWLINE);
					}					
					writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(tempAct.getName()) + "ActionStateRepository().remove(("
						+ getUpperCaseLeading(tempAct.getName()) + "Action)tempAct);");
					writer.write(NEWLINE);
					
					// game-ending:
					if(tempDest.isGameEndingDestroyer())
					{
						writer.write("// stop game and give score:");
						writer.write(NEWLINE);
						writer.write(getUpperCaseLeading(tempAct.getName()) + "Action t111 = (" + getUpperCaseLeading(tempAct.getName()) 
							+ "Action)tempAct;");
						writer.write(NEWLINE);					
						// find the scoring attribute:
						ActionTypeParticipantDestroyer scoringPartDest = null;
						ActionTypeParticipantConstraint scoringPartConst = null;
						ActionTypeParticipantAttributeConstraint scoringAttConst = null;
						Vector partDests = tempDest.getAllParticipantDestroyers();
						for(int j=0; j<partDests.size(); j++)
						{
							ActionTypeParticipantDestroyer partDest = (ActionTypeParticipantDestroyer)partDests.elementAt(j);
							Vector partConsts = partDest.getAllConstraints();
							for(int k=0; k<partConsts.size(); k++)
							{
								ActionTypeParticipantConstraint partConst = (ActionTypeParticipantConstraint)partConsts.elementAt(k);
								ActionTypeParticipantAttributeConstraint[] attConsts = partConst.getAllAttributeConstraints();
								for(int m=0; m<attConsts.length; m++)
								{
									if(attConsts[m].isScoringAttribute())
									{
										scoringAttConst = attConsts[m];
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
						}
					}
					
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write("else");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("((" + getUpperCaseLeading(tempAct.getName()) + "Action)tempAct).decrementTimeToLive();");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);					
				}
				else // random, user, or autonomous destroyer
				{	
					if((tempDest instanceof RandomActionTypeDestroyer) 
						|| (tempDest instanceof AutonomousActionTypeDestroyer)) // random
							// or autonomous destroyer
					{
						writer.write("if(!updateUserDestsOnly)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
					}
					writer.write("boolean destroy = true;");
					writer.write(NEWLINE);
					
					Vector destroyers = tempDest.getAllParticipantDestroyers();
					// go through each participant destroyer:
					for(int j=0; j<destroyers.size(); j++)
					{
						ActionTypeParticipantDestroyer dest = (ActionTypeParticipantDestroyer)destroyers.elementAt(j);
						ActionTypeParticipant part = dest.getParticipant();
						
						writer.write("Vector " + part.getName().toLowerCase() + "s = ((" + getUpperCaseLeading(tempAct.getName()) 
							+ "Action)tempAct).getAll" + part.getName() + "s();");
						writer.write(NEWLINE);
						writer.write("for(int j=0; j<" + part.getName().toLowerCase() + "s.size(); j++)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(SimSEObjectTypeTypes.getText(part.getSimSEObjectTypeType()) + " a = (" 
							+ SimSEObjectTypeTypes.getText(part.getSimSEObjectTypeType()) + ")" + part.getName().toLowerCase() + "s.elementAt(j);");
						writer.write(NEWLINE);
						// go through all participant constraints:
						Vector constraints = dest.getAllConstraints();
						for(int k=0; k<constraints.size(); k++)
						{
							ActionTypeParticipantConstraint constraint = (ActionTypeParticipantConstraint)constraints.elementAt(k);				
							String objTypeName = constraint.getSimSEObjectType().getName();					
							if(k > 0) // not on first element
							{
								writer.write("else ");
							}
							writer.write("if(a instanceof " + getUpperCaseLeading(objTypeName) + ")");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							// go through all attribute constraints:
							ActionTypeParticipantAttributeConstraint[] attConstraints = constraint.getAllAttributeConstraints();
							int numAttConsts = 0;
							for(int m=0; m<attConstraints.length; m++)
							{
								ActionTypeParticipantAttributeConstraint tempAttConst = attConstraints[m];
								if(tempAttConst.isConstrained())
								{
									if(numAttConsts == 0) // this is the first attribute that we've come across that's constrained
									{
										writer.write("if(");
									}
									else
									{
										writer.write(" || ");
									}
									writer.write("(!(((" + getUpperCaseLeading(objTypeName) + ")a).get" + tempAttConst.getAttribute().getName() + "()");
									if(tempAttConst.getAttribute().getType() == AttributeTypes.STRING)
									{
										writer.write(".equals(" + "\"" + tempAttConst.getValue().toString() + "\")");
									}
									else
									{
										if(tempAttConst.getGuard().equals(AttributeGuard.EQUALS))
										{
											writer.write(" == ");
										}
										else
										{
											writer.write(" " + tempAttConst.getGuard() + " ");
										}
										writer.write(tempAttConst.getValue().toString());
									}
									writer.write("))");
									numAttConsts++;
								}								
							}
							if(numAttConsts > 0) // there is at least one constraint
							{
								writer.write(")");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								writer.write("destroy = false;");
								writer.write(NEWLINE);
								writer.write("break;");
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
					writer.write("if(");
					if(tempDest instanceof RandomActionTypeDestroyer)
					{
						writer.write("(destroy) && ((ranNumGen.nextDouble() * 100.0) < " + ((RandomActionTypeDestroyer)(tempDest)).getFrequency() + "))");
					}
					else // user, action or autonomous
					{
						writer.write("destroy)");
					}
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("Vector b = tempAct.getAllParticipants();");
					writer.write(NEWLINE);
					writer.write("for(int j=0; j<b.size(); j++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("SSObject c = (SSObject)b.elementAt(j);");
					writer.write(NEWLINE);
					writer.write("if(c instanceof Employee)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if((tempDest instanceof AutonomousActionTypeDestroyer) || (tempDest instanceof RandomActionTypeDestroyer))
					{
						if((tempDest.getDestroyerText() != null) && (tempDest.getDestroyerText().length() > 0))
						{						
							writer.write("((Employee)c).setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
						}
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("else if(c instanceof Customer)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						if((tempDest.getDestroyerText() != null) && (tempDest.getDestroyerText().length() > 0))
						{						
							writer.write("((Customer)c).setOverheadText(\"" + tempDest.getDestroyerText() + "\");");
						}
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);

						// execute all destroyer rules:
						Vector destRules = tempAct.getAllDestroyerRules();
						for(int k=0; k<destRules.size(); k++)
						{
							Rule dRule = (Rule)destRules.elementAt(k);
							writer.write("ruleExec.update(gui, RuleExecutor.UPDATE_ONE, \"" + dRule.getName() + "\", tempAct);");
							writer.write(NEWLINE);
						}						
						writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(tempAct.getName()) + "ActionStateRepository().remove(("
							+ getUpperCaseLeading(tempAct.getName()) + "Action)tempAct);");
						writer.write(NEWLINE);
						
						// game-ending:
						if(tempDest.isGameEndingDestroyer())
						{
							writer.write("// stop game and give score:");
							writer.write(NEWLINE);
							writer.write(getUpperCaseLeading(tempAct.getName()) + "Action t111 = (" + getUpperCaseLeading(tempAct.getName()) 
								+ "Action)tempAct;");
							writer.write(NEWLINE);
							// find the scoring attribute:
							ActionTypeParticipantDestroyer scoringPartDest = null;
							ActionTypeParticipantConstraint scoringPartConst = null;
							ActionTypeParticipantAttributeConstraint scoringAttConst = null;
							Vector partDests = tempDest.getAllParticipantDestroyers();
							for(int j=0; j<partDests.size(); j++)
							{
								ActionTypeParticipantDestroyer partDest = (ActionTypeParticipantDestroyer)partDests.elementAt(j);
								Vector partConsts = partDest.getAllConstraints();
								for(int k=0; k<partConsts.size(); k++)
								{
									ActionTypeParticipantConstraint partConst = (ActionTypeParticipantConstraint)partConsts.elementAt(k);
									ActionTypeParticipantAttributeConstraint[] attConsts = partConst.getAllAttributeConstraints();
									for(int m=0; m<attConsts.length; m++)
									{
										if(attConsts[m].isScoringAttribute())
										{
											scoringAttConst = attConsts[m];
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
						}
						
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);						
					}
					else // user destroyer
					{
						writer.write("((Employee)c).addMenuItem(\"" + ((UserActionTypeDestroyer)(tempDest)).getMenuText() + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
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
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("// update trigger checker:");
			writer.write(NEWLINE);			
			writer.write("trigCheck.update(true, gui);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + destFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}


	
	private void initializeDestroyerLists() // gets the destroyers in prioritized order according to their priority
	{
		// initialize lists:
		nonPrioritizedDestroyers = new Vector();
		prioritizedDestroyers = new Vector();
		Vector allActions = actTypes.getAllActionTypes();
		// go through all action types and get their destroyers:
		for(int i=0; i<allActions.size(); i++)
		{
			ActionType tempAct = (ActionType)allActions.elementAt(i);
			Vector dests = tempAct.getAllDestroyers();
			for(int j=0; j<dests.size(); j++)
			{
				ActionTypeDestroyer tempDest = (ActionTypeDestroyer)dests.elementAt(j);
				int priority = tempDest.getPriority();
				if(priority == -1) // destroyer is not prioritized
				{
					nonPrioritizedDestroyers.addElement(tempDest);
				}
				else // priority >= 0
				{
					if(prioritizedDestroyers.size() == 0) // no elements have been added yet to the prioritized destroyer list
					{
						prioritizedDestroyers.add(tempDest);
					}
					else
					{
						// find the correct position to insert the destroyer at:
						for(int k=0; k<prioritizedDestroyers.size(); k++)
						{
							ActionTypeDestroyer tempA = (ActionTypeDestroyer)prioritizedDestroyers.elementAt(k);
							if(priority <= tempA.getPriority())
							{
								prioritizedDestroyers.insertElementAt(tempDest, k); // insert the destroyer
								break;
							}
							else if(k == (prioritizedDestroyers.size() - 1)) // on the last element
							{
								prioritizedDestroyers.add(tempDest); // add the destroyer to the end of the list
								break;
							}
						}
					}
				}
			}
		}	
		// make it all into one:
		allDestroyers = new Vector();
		for(int i=0; i<prioritizedDestroyers.size(); i++)
		{
			allDestroyers.add((ActionTypeDestroyer)prioritizedDestroyers.elementAt(i));
		}
		for(int i=0; i<nonPrioritizedDestroyers.size(); i++)
		{
			allDestroyers.add((ActionTypeDestroyer)nonPrioritizedDestroyers.elementAt(i));
		}
	}
}
