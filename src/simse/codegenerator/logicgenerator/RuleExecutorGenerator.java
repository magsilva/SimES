/* This class is responsible for generating all of the code for the logic's rule executor component in SimSE*/

package simse.codegenerator.logicgenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;
import simse.modelbuilder.startstatebuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class RuleExecutorGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	
	private File directory; // directory to save generated code into
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file
	private FileWriter writer;
	private File ruleExFile;
	private Vector nonPrioritizedRules;
	private Vector prioritizedRules;
	private Vector outerVariables; // for keeping track of which variables have been generated
	private Vector warnings; // holds warning messages about any errors that are run into during generation
	
	public RuleExecutorGenerator(DefinedActionTypes dats, File dir)
	{
		actTypes = dats;
		directory = dir;
		outerVariables = new Vector();
		warnings = new Vector();
		initializeRuleLists();
	}
	
	
	public void generate()
	{
		try
		{
			ruleExFile = new File(directory, ("simse\\logic\\RuleExecutor.java"));
			if(ruleExFile.exists())
			{
				ruleExFile.delete(); // delete old version of file
			}
			writer = new FileWriter(ruleExFile);
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
			writer.write("public class RuleExecutor");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			
			// member variables:
			writer.write("public static final int UPDATE_ALL_CONTINUOUS = 0;");
			writer.write(NEWLINE);
			writer.write("public static final int UPDATE_ONE = 1;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);			
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Random ranNumGen;");
			writer.write(NEWLINE);			
			
			// constructor:
			writer.write("public RuleExecutor(State s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("ranNumGen = new Random();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// update function:
			writer.write("public void update(JFrame gui, int updateInstructions, String ruleName, simse.adts.actions.Action action)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// generate prioritized rules:
			for(int i=0; i<prioritizedRules.size(); i++)
			{
				generateRuleExecutor((Rule)prioritizedRules.elementAt(i));
			}
			// generate non-prioritized rules:
			for(int i=0; i<nonPrioritizedRules.size(); i++)
			{
				generateRuleExecutor((Rule)nonPrioritizedRules.elementAt(i));
			}
			writer.write("((SimSEGUI)gui).update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			
			// checkAllMins method:
			writer.write("private void checkAllMins(JFrame parent)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector actions = state.getActionStateRepository().getAllActions();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<actions.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action act = (simse.adts.actions.Action)actions.elementAt(i);");
			writer.write(NEWLINE);
			Vector actions = actTypes.getAllActionTypes();
			// go through all action types:
			for(int i=0; i<actions.size(); i++)
			{
				ActionType act = (ActionType)actions.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(act instanceof " + getUpperCaseLeading(act.getName()) + "Action)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(act.getName()) + "Action b = (" + getUpperCaseLeading(act.getName()) + "Action)act;");
				writer.write(NEWLINE);
				writer.write("if(");
				// go through all participants:
				Vector parts = act.getAllParticipants();
				for(int j=0; j<parts.size(); j++)
				{
					if(j > 0) // not on first element
					{
						writer.write(" || ");
					}
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(j);
					writer.write("(b.getAll" + part.getName() + "s().size() < ");
					if(part.getQuantity().isMinValBoundless()) // min val boundless
					{
						writer.write("-999999");
					}
					else // min val has a value
					{
						writer.write("" + part.getQuantity().getMinVal().intValue());
					}
					writer.write(")");
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// get the destroyer text from the highest priority destroyer:
				String destText = new String();
				if(act.getAllDestroyers().size() > 0) // has at least one destroyer
				{
					ActionTypeDestroyer highestPriDest = (ActionTypeDestroyer)act.getAllDestroyers().elementAt(0);
					Vector allDests = act.getAllDestroyers();
					for(int j=0; j<allDests.size(); j++)
					{
						ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(j);
						if(tempDest.getPriority() > highestPriDest.getPriority())
						{
							highestPriDest = tempDest;
						}
					}
					destText = highestPriDest.getDestroyerText();
					if((destText.equals(null) == false) && (destText.length() > 0)) // has destroyer text
					{				
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
						writer.write("((Employee)d).setOverheadText(\"" + destText + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("else if(d instanceof Customer)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("((Customer)d).setOverheadText(\"" + destText + "\");");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
							
				// get any destroyer rules:
				Vector destRules = act.getAllDestroyerRules();
				for(int j=0; j<destRules.size(); j++)
				{
					Rule r = (Rule)destRules.elementAt(j);
					writer.write("update(parent, UPDATE_ONE, \"" + r.getName() + "\", b);");
					writer.write(NEWLINE);
				}
				writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(act.getName()) + "ActionStateRepository().remove(b);");
				writer.write(NEWLINE);
				
				// game-ending?:
				// get the highest priority destroyer:
				if(act.getAllDestroyers().size() > 0) // has at least one destroyer
				{
					ActionTypeDestroyer highestPriDest = (ActionTypeDestroyer)act.getAllDestroyers().elementAt(0);
					Vector allDests = act.getAllDestroyers();
					for(int j=0; j<allDests.size(); j++)
					{
						ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(j);
						if(tempDest.getPriority() > highestPriDest.getPriority())
						{
							highestPriDest = tempDest;
						}
					}
				
					if(highestPriDest.isGameEndingDestroyer())
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
						Vector partDests = highestPriDest.getAllParticipantDestroyers();
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
				}
				
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
			
			// generate warnings, if any:
			if(warnings.size() > 0)
			{
				warnings.add(0, "ERROR! Incomplete simulation generated!!");
				WarningListDialog wld = new WarningListDialog(warnings, "Code Generation Errors");
			}
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + ruleExFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private void generateRuleExecutor(Rule rule)
	{
		try
		{
			if(rule instanceof EffectRule) // EFFECT RULE
			{
				EffectRule effRule = (EffectRule)rule;
				ActionType action = rule.getActionType();
				writer.write("// " + rule.getName() + " rule (" + action.getName() + " Action):");
				writer.write(NEWLINE);
				if(vectorContainsString(outerVariables, (action.getName().toLowerCase() + "Acts")) == false) // this variable has not been generated yet
				{
					outerVariables.add(new String(action.getName().toLowerCase() + "Acts")); // add the variable name to the record-keeping Vector
					writer.write("Vector " + action.getName().toLowerCase() + "Acts = state.getActionStateRepository().get"
						+ getUpperCaseLeading(action.getName()) + "ActionStateRepository().getAllActions();");
					writer.write(NEWLINE);					
				}
				writer.write("if((updateInstructions ==");
				if(rule.getTiming() == RuleTiming.CONTINUOUS) // continuous rule
				{
					writer.write("UPDATE_ALL_CONTINUOUS))");
				}				
				else if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER)) // trigger/destroyer rule
				{
					writer.write("UPDATE_ONE) && (ruleName.equals(\"" + rule.getName() + "\")))");
				}
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);					
				writer.write("for(int i=0; i<" + action.getName().toLowerCase() + "Acts.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(action.getName()) + "Action " + action.getName().toLowerCase() + "Act = ("
					+ getUpperCaseLeading(action.getName()) + "Action)" + action.getName().toLowerCase() + "Acts.elementAt(i);");
				writer.write(NEWLINE);
				if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER))
				{
					writer.write("if(" + action.getName().toLowerCase() + "Act == action)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
				}
				// code to make sure it has the min num of participants:
				writer.write("if(");
				Vector parts = action.getAllParticipants();
				for(int i=0; i<parts.size(); i++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(i);
					if(i > 0) // not on first element
					{
						writer.write(" && ");
					}
					writer.write("(" + action.getName().toLowerCase() + "Act.getAll" + part.getName() + "s().size() >= ");
					if(part.getQuantity().isMinValBoundless()) // min val boundless
					{
						writer.write("0");
					}
					else // has min val
					{
						writer.write(part.getQuantity().getMinVal().toString());
					}
					writer.write(")");
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				
				// get rule input(s), if any:
				Vector rInputs = effRule.getAllRuleInputs();
				for(int j=0; j<rInputs.size(); j++)
				{
					RuleInput input = (RuleInput)rInputs.elementAt(j);
					String inputName = input.getName();
					
					if((input.getType().equals(InputType.DOUBLE)) || (input.getType().equals(InputType.INTEGER))) // numerical rule input type
					{
						writer.write("double input" + inputName + " = 0;"); // have to initialize it to something or else it doesn't work!
						writer.write(NEWLINE);													
						writer.write("boolean gotValidInput" + j + " = false;");
						writer.write(NEWLINE);
						if(j == 0) // on first rule input
						{
							writer.write("boolean cancel = false;");
							writer.write(NEWLINE);
						}
						writer.write("if(!cancel)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);						
						writer.write("while(!gotValidInput" + j + ")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("String response = JOptionPane.showInputDialog(null, \"" + input.getPrompt() + ": ("
							+ input.getType());
						if(input.getCondition().isConstrained()) // input has a condition
						{
							writer.write(" " + input.getCondition().getGuard() + " " + input.getCondition().getValue());
						}
						writer.write(")\", \"Input\", JOptionPane.QUESTION_MESSAGE);");
						writer.write(NEWLINE);
						writer.write("if(response != null)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("try");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(input.getType() + " temp = new " + input.getType() + "(response);");
						writer.write(NEWLINE);
						String tempTypeStr = new String();
						if(input.getType().equals(InputType.INTEGER))
						{
							tempTypeStr = "int";
						}
						else if(input.getType().equals(InputType.DOUBLE))
						{
							tempTypeStr = "double";
						}
						if(input.getCondition().isConstrained()) // input has a condition
						{
							writer.write("if(temp." + tempTypeStr + "Value() " + input.getCondition().getGuard() + " " 
								+ input.getCondition().getValue() + ")");												
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
						}
						writer.write("input" + inputName + " = (double)(temp." + tempTypeStr + "Value());");
						writer.write(NEWLINE);
						writer.write("gotValidInput" + j + " = true;");
						writer.write(NEWLINE);
						if(input.getCondition().isConstrained())
						{
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write("else");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("JOptionPane.showMessageDialog(null, \"Invalid Input -- Please try again!\", \"Invalid Input\", JOptionPane.WARNING_MESSAGE);");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write("catch(NumberFormatException e)");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write("JOptionPane.showMessageDialog(null, \"Invalid Input -- Please try again!\", \"Invalid Input\", JOptionPane.WARNING_MESSAGE);");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);												
						writer.write("else // action cancelled");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						if(input.isCancelable())
						{
							writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(action.getName()) 
								+ "ActionStateRepository().remove(" + action.getName().toLowerCase() + "Act);");
							writer.write(NEWLINE);
							writer.write("cancel = true;");
							writer.write(NEWLINE);
							writer.write("break;");							
						}
						else // not cancelable
						{
							writer.write("JOptionPane.showMessageDialog(null, \"You must enter input -- Please try again!\", \"Invalid Input\", JOptionPane.WARNING_MESSAGE);");
						}
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);	
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					else // boolean or string rule input type
					{
						if(input.getType().equals(InputType.STRING)) // string rule input type
						{
							writer.write("String input" + inputName + " = new String();");
							writer.write(NEWLINE);												
							writer.write("boolean gotValidInput" + j + " = false;");
							writer.write(NEWLINE);
							if(j == 0) // on first rule input
							{
								writer.write("boolean cancel = false;");
								writer.write(NEWLINE);
							}
							writer.write("if(!cancel)");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);							
							writer.write("while(!gotValidInput" + j + ")");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("String response = JOptionPane.showInputDialog(null, \"" + input.getPrompt() + ": (String)\", \"Input\", JOptionPane.QUESTION_MESSAGE);");
							writer.write(NEWLINE);
							writer.write("if((response != null) && (response.length() > 0))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("input" + inputName + " = response;");
							writer.write(NEWLINE);
							writer.write("gotValidInput" + j + " = true;");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write("else // action cancelled");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							if(input.isCancelable())
							{
								writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(action.getName()) 
									+ "ActionStateRepository().remove(" + action.getName().toLowerCase() + "Act);");
								writer.write(NEWLINE);
								writer.write("break;");							
							}
							else // not cancelable
							{
								writer.write("JOptionPane.showMessageDialog(null, \"You must enter input -- Please try again!\", \"Invalid Input\", JOptionPane.WARNING_MESSAGE);");
							}
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);							
						}
						else if(input.getType().equals(InputType.BOOLEAN)) // boolean rule input type
						{
							writer.write("boolean input" + inputName + " = false;");
							writer.write(NEWLINE);												
							writer.write("boolean gotValidInput" + j + " = false;");
							writer.write(NEWLINE);
							if(j == 0) // on first rule input
							{
								writer.write("boolean cancel = false;");
								writer.write(NEWLINE);
							}
							writer.write("if(!cancel)");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);							
							writer.write("while(!gotValidInput" + j + ")");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("String response = JOptionPane.showInputDialog(null, \"" + input.getPrompt() + ": (true or false)\", \"Input\", JOptionPane.QUESTION_MESSAGE);");
							writer.write(NEWLINE);
							writer.write("if(response != null)");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("if(response.equalsIgnoreCase(\"true\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("input" + inputName + " = true;");
							writer.write(NEWLINE);
							writer.write("gotValidInput" + j + " = true;");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write("else if(response.equalsIgnoreCase(\"false\"))");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("gotValidInput" + j + " = true;");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write("else");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("JOptionPane.showMessageDialog(null, \"Invalid Input -- Please try again!\", \"Invalid Input\", JOptionPane.WARNING_MESSAGE);");
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
							writer.write("else // action cancelled");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							if(input.isCancelable())
							{
								writer.write("state.getActionStateRepository().get" + getUpperCaseLeading(action.getName()) 
									+ "ActionStateRepository().remove(" + action.getName().toLowerCase() + "Act);");
								writer.write(NEWLINE);
								writer.write("break;");							
							}
							else // not cancelable
							{
								writer.write("JOptionPane.showMessageDialog(null, \"You must enter input -- Please try again!\", \"Invalid Input\", JOptionPane.WARNING_MESSAGE);");
							}							
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
				
				for(int j=0; j<rInputs.size(); j++)
				{
					if(j == 0) // first element
					{
						writer.write("if(gotValidInput" + j);
					}
					else // not on first element
					{
						writer.write(" && gotValidInput" + j);
					}
					if(j == (rInputs.size() - 1)) // on last rule input
					{
						writer.write(")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
					}
				}
				Vector ruleVariables = new Vector(); // record-keeping
				// go through all participant rule effects:
				Vector partRuleEffects = effRule.getAllParticipantRuleEffects();
				for(int j=0; j<partRuleEffects.size(); j++)
				{
					ParticipantRuleEffect partRuleEff = (ParticipantRuleEffect)partRuleEffects.elementAt(j);
					if(vectorContainsString(ruleVariables, (partRuleEff.getParticipant().getName().toLowerCase() + "s")) == false) // this variable
						// has not been generated yet
					{
						ruleVariables.add(new String(partRuleEff.getParticipant().getName().toLowerCase() + "s")); // add the variable name to the 
						//record-keeping Vector
						writer.write("Vector " + partRuleEff.getParticipant().getName().toLowerCase() + "s = " + action.getName().toLowerCase()
							+ "Act.getAllActive" + partRuleEff.getParticipant().getName() + "s();");
						writer.write(NEWLINE);
					}
					writer.write("for(int j=0; j<" + partRuleEff.getParticipant().getName().toLowerCase() + "s.size(); j++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("SSObject " + partRuleEff.getParticipant().getName().toLowerCase() + "2 = (SSObject)"
						+ partRuleEff.getParticipant().getName().toLowerCase() + "s.elementAt(j);");
					writer.write(NEWLINE);
					// go through all participant type rule effects:
					Vector partTypeEffects = partRuleEff.getAllParticipantTypeEffects();
					Vector variables = new Vector(); // for keeping track of which variables are generated
					for(int k=0; k<partTypeEffects.size(); k++)
					{
						Vector ruleInputVariables = new Vector();
						if(k > 0) // not on first element
						{
							writer.write("else ");
						}
						ParticipantTypeRuleEffect partTypeRuleEff = (ParticipantTypeRuleEffect)partTypeEffects.elementAt(k);
						writer.write("if(" + partRuleEff.getParticipant().getName().toLowerCase() + "2 instanceof " 
							+ getUpperCaseLeading(partTypeRuleEff.getSimSEObjectType().getName()) + ")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(getUpperCaseLeading(partTypeRuleEff.getSimSEObjectType().getName()) + " "
							+ partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + " = ("
							+ getUpperCaseLeading(partTypeRuleEff.getSimSEObjectType().getName()) + ")" 
							+ partRuleEff.getParticipant().getName().toLowerCase() + "2;");
						writer.write(NEWLINE);
						
						// effect on participants' other actions:
						if(partTypeRuleEff.getOtherActionsEffect().equals(OtherActionsEffect.NONE) == false) // has an effect on participant's other actions
						{
							writer.write("Vector otherActs = state.getActionStateRepository().getAll");
							boolean activateAll = true;
							if(partTypeRuleEff.getOtherActionsEffect().equals(OtherActionsEffect.DEACTIVATE_ALL))
							{
								activateAll = false;
								writer.write("Active");
							}
							else if(partTypeRuleEff.getOtherActionsEffect().equals(OtherActionsEffect.ACTIVATE_ALL))
							{
								activateAll = true;
								writer.write("Inactive");
							}
							writer.write("Actions(" + partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ");");
							writer.write(NEWLINE);
							writer.write("for(int k=0; k<otherActs.size(); k++)");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)otherActs.elementAt(k);");
							writer.write(NEWLINE);
							// go through all action types:
							Vector allActTypes = actTypes.getAllActionTypes();
							for(int m=0; m<allActTypes.size(); m++)
							{
								ActionType tempActType = (ActionType)allActTypes.elementAt(m);
								if(m > 0) // not on first element
								{
									writer.write("else ");
								}
								writer.write("if(tempAct instanceof " + getUpperCaseLeading(tempActType.getName()) + "Action)");
								writer.write(NEWLINE);
								writer.write(OPEN_BRACK);
								writer.write(NEWLINE);
								if(tempActType.getName().equals(action.getName()))
								{
									writer.write("if(tempAct.equals(" + action.getName().toLowerCase() + "Act) == false)");
									writer.write(NEWLINE);
									writer.write(OPEN_BRACK);
									writer.write(NEWLINE);
								}
								// go through all participants:
								Vector allParts = tempActType.getAllParticipants();
								for(int n=0; n<allParts.size(); n++)
								{
									ActionTypeParticipant tempPart = (ActionTypeParticipant)allParts.elementAt(n);
									if(tempPart.getSimSEObjectTypeType() == partTypeRuleEff.getSimSEObjectType().getType())
									{
										writer.write("((" + getUpperCaseLeading(tempActType.getName()) + "Action)tempAct).set" + tempPart.getName());
										if(activateAll)
										{
											writer.write("Active");
										}
										else
										{
											writer.write("Inactive");
										}
										writer.write("(" + partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ");");
										writer.write(NEWLINE);
									}
								}
								if(tempActType.getName().equals(action.getName()))
								{
									writer.write(CLOSED_BRACK);
									writer.write(NEWLINE);
								}	
								writer.write(CLOSED_BRACK);
								writer.write(NEWLINE);								
							}	
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
						
						// go through all participant attribute rule effects:
						Vector partAttRuleEffects = partTypeRuleEff.getAllAttributeEffects();
						for(int m=0; m<partAttRuleEffects.size(); m++)
						{
							ParticipantAttributeRuleEffect partAttRuleEff = (ParticipantAttributeRuleEffect)partAttRuleEffects.elementAt(m);
							if((partAttRuleEff.getEffect().equals(null) == false) && (partAttRuleEff.getEffect().length() > 0))
							{
								if((partAttRuleEff.getAttribute().getType() == AttributeTypes.INTEGER) ||
									(partAttRuleEff.getAttribute().getType() == AttributeTypes.DOUBLE)) // numerical attributes
								{
									// go through the effect once to collect all of the information you need:
									String effect = partAttRuleEff.getEffect();
									boolean finished = false;
									int counter = 0;
									while(!finished)
									{
										counter++;
										String nextToken = getNextToken(effect);
										
										// attributes other participants:
										if(nextToken.startsWith("all") || nextToken.startsWith("-all"))
										{
											if(effect.trim().length() == nextToken.trim().length()) // on last token
											{
												effect = null;
											}
											else // not on last token
											{
												effect = effect.trim().substring(nextToken.length()).trim();
											}										
											if(nextToken.startsWith("-"))
											{
												nextToken = nextToken.substring(1); // remove the minus sign for now
											}
											String activeInactiveToken = nextToken.substring(0, nextToken.indexOf('-')); // get whether it's all, allActive, 
											// or allInactive
											nextToken = nextToken.substring(activeInactiveToken.length() + 1).trim();
											String partName = nextToken.substring(0, nextToken.indexOf('-')); // get the participant name
											// check for validity of participant name:
											if(action.getParticipant(partName) == null) // invalid participant name
											{
												warnings.add("Invalid participant name: \"" + partName + "\" in effect rule " + effRule.getName() + " for " 
													+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
													+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
													+ " attribute effect");
											}
											nextToken = nextToken.substring(partName.length() + 1).trim();
											String ssObjType = nextToken.substring(0, nextToken.indexOf('-')); // get the SimSEObjectType
											if(action.getParticipant(partName) != null) // valid participant name
											{
												// check for validity of object type name:
												if(action.getParticipant(partName).getSimSEObjectType(ssObjType) == null) // invalid SimSEObjectType
												{
													warnings.add("Invalid object type: \"" + ssObjType + "\" in effect rule " + effRule.getName() + " for " 
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");
												}		
											}
											nextToken = nextToken.substring(ssObjType.length() + 1).trim();
											String attName = nextToken.substring(nextToken.indexOf(':') + 1).trim();
											if((action.getParticipant(partName) != null) 
												&& (action.getParticipant(partName).getSimSEObjectType(ssObjType) != null)) // valid participant name
													// and SimSEObjectType
											{
												// check for validity of attribute name:
												if(action.getParticipant(partName).getSimSEObjectType(ssObjType).getAttribute(attName) == null) // invalid attribute
												{
													warnings.add("Invalid attribute name: \"" + attName + "\" in effect rule " + effRule.getName() + " for "
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");
												}
												else // valid attribute
												{
													// check for validity of attribute type:
													if((action.getParticipant(partName).getSimSEObjectType(ssObjType).getAttribute(attName).getType() != AttributeTypes.INTEGER) &&
														(action.getParticipant(partName).getSimSEObjectType(ssObjType).getAttribute(attName).getType() != AttributeTypes.DOUBLE)) 
															// non-numerical attribute -- invalid
													{
														warnings.add("Invalid (non-numerical) attribute type: \"" + attName + "\" in effect rule " + effRule.getName() + " for "
															+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
															+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
															+ " attribute effect");							
													}														
												}
											}
											if(vectorContainsString(variables, (activeInactiveToken + partName + ssObjType + attName)) == false) // this variable
												// has not been generated yet
											{
												variables.add(new String(activeInactiveToken + partName + ssObjType + attName)); // add to the record-keeping
												// Vector
												writer.write("double " + activeInactiveToken + partName + ssObjType + attName + " = 0;");
												writer.write(NEWLINE);
												if(vectorContainsString(variables, (activeInactiveToken + partName + "s")) == false) // this variable has not been generated yet
												{
													variables.add(new String(activeInactiveToken + partName + "s")); // add the variable name to the record-keeping
													// Vector
													writer.write("Vector " + activeInactiveToken + partName + "s = " + action.getName().toLowerCase() + "Act.getAll");
													if(activeInactiveToken.indexOf("Active") > 0)
													{
														writer.write("Active");
													}
													else if(activeInactiveToken.indexOf("Inactive") > 0)
													{
														writer.write("Inactive");
													}
													writer.write(partName + "s();");
													writer.write(NEWLINE);
												}
												writer.write("for(int k=0; k<" + activeInactiveToken + partName + "s.size(); k++)");
												writer.write(NEWLINE);
												writer.write(OPEN_BRACK);
												writer.write(NEWLINE);
												writer.write("Object " + partName.toLowerCase() + "3 = " + activeInactiveToken + partName + "s.elementAt(k);");
												writer.write(NEWLINE);
												writer.write("if(" + partName.toLowerCase() + "3 instanceof " + ssObjType + ")");
												writer.write(NEWLINE);
												writer.write(OPEN_BRACK);
												writer.write(NEWLINE);
												writer.write(activeInactiveToken + partName + ssObjType + attName + " += (double)(((" + ssObjType + ")" + partName.toLowerCase()
													+ "3).get" + attName + "());");											
												writer.write(NEWLINE);
												writer.write(CLOSED_BRACK);
												writer.write(NEWLINE);
												writer.write(CLOSED_BRACK);
												writer.write(NEWLINE);
											}
										}
										
										// num actions or num participants:
										else if(nextToken.startsWith("num") || nextToken.startsWith("-num"))
										{
											if(effect.trim().length() == nextToken.trim().length()) // on last token
											{
												effect = null;
											}
											else // not on last token
											{
												effect = effect.trim().substring(nextToken.length()).trim();
											}										
											if(nextToken.startsWith("-"))
											{
												nextToken = nextToken.substring(1); // remove the minus sign for now
											}										
											String firstWord = nextToken.substring(0, nextToken.indexOf('-'));
											
											if(firstWord.endsWith("This")) // num actions this participant
											{
												ActionTypeParticipant part = partRuleEff.getParticipant();
												String actionName = nextToken.substring(nextToken.indexOf(':') + 1).trim();
												// check for validity of action name:
												if((!(actionName.equals("*"))) && (actTypes.getActionType(actionName) == null))
												{
													warnings.add("Invalid action name: \"" + actionName + "\" in effect rule " + effRule.getName() + " for "
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");
												}
												StringBuffer variableName = new StringBuffer("num");
												if(firstWord.indexOf("Active") >= 0)
												{
													variableName.append("Active");
												}
												else if(firstWord.indexOf("Inactive") >= 0)
												{
													variableName.append("Inactive");
												}
												variableName.append("ActionsThisPart");
												if(actionName.equals("*")) // wild card character -- all actions
												{
													variableName.append("A");
												}
												else // action name
												{
													variableName.append(actionName);
												}
												if(vectorContainsString(variables, variableName.toString()) == false) // variable has not been generated yet
												{
													variables.add(variableName.toString()); // add the variable name to the record-keeping vector
													writer.write("double " + variableName + " = (double)(state.getActionStateRepository().");
													if(actionName.equals("*") == false) // not the wild card character -- an actual action name
													{
														writer.write("get" + getUpperCaseLeading(actionName) + "ActionStateRepository().");
													}
													writer.write("getAll");
													if(variableName.indexOf("Active") >= 0)
													{
														writer.write("Active");
													}
													else if(variableName.indexOf("Inactive") >= 0)
													{
														writer.write("Inactive");
													}
													writer.write("Actions(" + part.getName().toLowerCase() + "2).size());");
													writer.write(NEWLINE);
												}
											}
											
											else if(firstWord.indexOf("All") >= 0) // num actions other participants
											{
												StringBuffer variableName = new StringBuffer("numActionsAll");
												if(firstWord.indexOf("Active") >= 0)
												{
													variableName.append("Active");
												}
												else if(firstWord.indexOf("Inactive") >= 0)
												{
													variableName.append("Inactive");
												}
												String tempStr = nextToken.substring(nextToken.indexOf('-') + 1); // take off the first word
												String partName = new String();
												String objTypeName = new String();
												if(tempStr.indexOf('-') >= 0) // includes SimSEObjectType and meta type
												{
													partName = tempStr.substring(0, tempStr.indexOf('-')); // get the participant name
													// check for validity of part name:
													if(action.getParticipant(partName) == null) // invalid participant name
													{
														warnings.add("Invalid participant name: \"" + partName + "\" in effect rule " + effRule.getName() + " for "
															+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
															+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
															+ " attribute effect");
													}													
													variableName.append(partName);
													tempStr = tempStr.substring(tempStr.indexOf('-') + 1); // take off the participant name
													objTypeName = tempStr.substring(0, tempStr.indexOf('-')); // get the SimSEObjectType name
													if(action.getParticipant(partName) != null) // valid participant name
													{
														// check for validity of SimSEObjectType name:
														if(action.getParticipant(partName).getSimSEObjectType(objTypeName) == null) // invalid SimSEObjectType name
														{
															warnings.add("Invalid object type name: \"" + objTypeName + "\" in effect rule " + effRule.getName() + " for "
																+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
																+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
																+ " attribute effect");
														}																
													}
													variableName.append(objTypeName);
												}
												else // does not include SimSEObjectType and meta type
												{
													partName = tempStr.substring(0, tempStr.indexOf(':')); // get the participant name
													// check for validity of part name:
													if(action.getParticipant(partName) == null) // invalid participant name
													{
														warnings.add("Invalid participant name: \"" + partName + "\" in effect rule " + effRule.getName() + " for "
															+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
															+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
															+ " attribute effect");
													}														
													variableName.append(partName);
												}
												String actionName = nextToken.substring(nextToken.indexOf(':') + 1).trim(); // get the action name
												// check for validity of action name:
												if((!(actionName.equals("*"))) && (actTypes.getActionType(actionName) == null)) // invalid action name
												{
													warnings.add("Invalid action name: \"" + actionName + "\" in effect rule " + effRule.getName() + " for "
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");
												}												
												if(actionName.equals("*")) // wildcard character -- any action
												{
													variableName.append("A");
												}
												else // specific action name
												{
													variableName.append(actionName);
												}
												if(vectorContainsString(variables, variableName.toString()) == false) // variable has not been generated yet
												{
													variables.add(variableName.toString()); // add the variable name to the record-keeping vector
													writer.write("double " + variableName + " = 0;");
													writer.write(NEWLINE);
												}
												StringBuffer variableName2 = new StringBuffer();
												if((variableName.indexOf("Active") >= 0) || (variableName.indexOf("Inactive") >= 0))
												{
													if(variableName.indexOf("Active") >= 0) // active
													{
														variableName2.append("allActive" + partName + "s");
													}
													else if(variableName.indexOf("Inactive") >= 0) // inactive
													{
														variableName2.append("allInactive" + partName + "s");
													}
													if(vectorContainsString(variables, variableName2.toString()) == false) // variable has not been generated yet
													{
														variables.add(variableName2.toString()); // add the variable name to the record-keeping vector
														writer.write("Vector " + variableName2 + " = " + action.getName().toLowerCase() + "Act.getAll");
														if(variableName2.indexOf("Active") >= 0)
														{
															writer.write("Active");
														}
														else
														{
															writer.write("Inactive");
														}
														writer.write(partName + "s();");
														writer.write(NEWLINE);
													}
												}
												else
												{
													variableName2.append(partName.toLowerCase() + "s");
													if(vectorContainsString(variables, variableName2.toString()) == false) // variable has not been generated yet
													{
														variables.add(variableName2.toString());
														writer.write("Vector " + variableName2 + " = " + action.getName().toLowerCase() + "Act.getAll" + partName
															+ "s();");
														writer.write(NEWLINE);
													}
												}
												writer.write("for(int k=0; k<" + variableName2 + ".size(); k++)");
												writer.write(NEWLINE);
												writer.write(OPEN_BRACK);
												writer.write(NEWLINE);
												writer.write("Object " + partName.toLowerCase() + "3 = " + variableName2 + ".elementAt(k);");
												writer.write(NEWLINE);
												boolean objTypeNameSpecified = false;
												if((objTypeName.equals(null) == false) && (objTypeName.length() > 0)) // an object type name was specified
												{
													objTypeNameSpecified = true;
													writer.write("if(" + partName.toLowerCase() + "3 instanceof " + objTypeName + ")");
													writer.write(NEWLINE);
													writer.write(OPEN_BRACK);
												}
												writer.write("Vector ");
												String vectorName = new String();
												if(actionName.equals("*")) // wildcard
												{
													vectorName = "actions";
													writer.write(vectorName + " = state.getActionStateRepository().getAllActions();");
												}
												else // action name specified
												{
													vectorName = (actionName.toLowerCase() + "Actions");
													writer.write(vectorName + " = state.getActionStateRepository().get" + getUpperCaseLeading(actionName)
														+ "ActionStateRepository().getAllActions();");
												}
												writer.write(NEWLINE);
												writer.write("for(int m=0; m<" + vectorName + ".size(); m++)");
												writer.write(NEWLINE);
												writer.write(OPEN_BRACK);
												writer.write(NEWLINE);
												if(actionName.equals("*"))
												{
													writer.write("simse.adts.actions.Action action = (simse.adts.actions.Action)" + vectorName + ".elementAt(m);");
													writer.write(NEWLINE);
													writer.write("if(action");
												}
												else // action name specified
												{
													writer.write(getUpperCaseLeading(actionName) + "Action " + actionName.toLowerCase() + "Action = ("
														+ getUpperCaseLeading(actionName) + "Action)" + actionName.toLowerCase() + "Actions.elementAt(m);");
													writer.write(NEWLINE);
													writer.write("if(" + actionName.toLowerCase() + "Action");
												}
												writer.write(".getAllParticipants().contains(" + partName.toLowerCase() + "3))");
												writer.write(NEWLINE);
												writer.write(OPEN_BRACK);
												writer.write(NEWLINE);
												writer.write(variableName + "++;");
												writer.write(NEWLINE);
												writer.write(CLOSED_BRACK);
												writer.write(NEWLINE);
												writer.write(CLOSED_BRACK);
												writer.write(NEWLINE);
												if(objTypeNameSpecified)
												{
													writer.write(CLOSED_BRACK);
													writer.write(NEWLINE);
												}
												writer.write(CLOSED_BRACK);
												writer.write(NEWLINE);												
											}
											
											else // num participants
											{
												StringBuffer variableName = new StringBuffer("num");
												if(firstWord.indexOf("Active") >= 0)
												{
													variableName.append("Active");
												}
												else if(firstWord.indexOf("Inactive") >= 0)
												{
													variableName.append("Inactive");
												}											
												String tempStr = nextToken.substring(nextToken.indexOf('-') + 1); // take off the first word
												String partName = new String();
												String ssObjType = new String();
												if(tempStr.indexOf('-') >= 0) // contains SimSEObjectType and meta type
												{
													partName = tempStr.substring(0, tempStr.indexOf('-'));
													// check for validity of participant name:
													if(action.getParticipant(partName) == null) // invalid participant name
													{
														warnings.add("Invalid participant name: \"" + partName + "\" in effect rule " + effRule.getName() + " for "
															+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
															+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
															+ " attribute effect");
													}													
													variableName.append(partName);
													String tempStr2 = tempStr.substring(tempStr.indexOf('-') + 1); // take off the part name
													ssObjType = tempStr2.substring(0, tempStr2.indexOf('-'));
													if(action.getParticipant(partName) != null) // valid participant name
													{
														// check for validity of SimSEObjectType name:
														if(action.getParticipant(partName).getSimSEObjectType(ssObjType) == null) // invalid SimSEObjectType name
														{
															warnings.add("Invalid object type name: \"" + ssObjType + "\" in effect rule " + effRule.getName() + " for "
																+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
																+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
																+ " attribute effect");
														}																
													}													
													variableName.append(ssObjType);
													if(vectorContainsString(variables, variableName.toString()) == false) // variable has not been generated yet
													{
														variables.add(variableName.toString()); // add the variable name to the record-keeping vector
														writer.write("double " + variableName	+ " = 0;");
														writer.write(NEWLINE);
														String variableName2 = new String();
														if(firstWord.indexOf("Active") >= 0)
														{
															variableName2 = ("allActive" + partName + "s");
														}
														else if(firstWord.indexOf("Inactive") >= 0)
														{
															variableName2 = ("allInactive" + partName + "s");
														}
														else // active/inactive status not specified
														{
															variableName2 = (partName.toLowerCase() + "s");
														}
														if((vectorContainsString(ruleVariables, variableName2) == false) && 
															(vectorContainsString(variables, variableName2) == false)) // variable has not been generated yet
														{
															// dont' add it to the outer vector because if it's needed in the outer loop later, it won't be able
															// to access it because this is being declared in the inner loop.  Only add it to the inner one:
															variables.add(variableName2);
															writer.write("Vector " + variableName2 + " = " + action.getName().toLowerCase() + "Act.getAll");
															if(firstWord.indexOf("Active") >= 0)
															{
																writer.write("Active");
															}
															else if(firstWord.indexOf("Inactive") >= 0)
															{
																writer.write("Inactive");
															}
															writer.write(partName + "s();");
															writer.write(NEWLINE);
														}
														writer.write("for(int k=0; k<" + variableName2 + ".size(); k++)");
														writer.write(NEWLINE);
														writer.write(OPEN_BRACK);
														writer.write(NEWLINE);
														writer.write("SSObject " + partName.toLowerCase() + "2 = (SSObject)" + variableName2 + ".elementAt(k);");
														writer.write(NEWLINE);
														writer.write("if(" + partName.toLowerCase() + "2 instanceof " + ssObjType + ")");
														writer.write(NEWLINE);
														writer.write(OPEN_BRACK);
														writer.write(NEWLINE);
														writer.write(variableName + "++;");
														writer.write(NEWLINE);
														writer.write(CLOSED_BRACK);
														writer.write(NEWLINE);
														writer.write(CLOSED_BRACK);
														writer.write(NEWLINE);
													}
												}
												else // no SimSEObjectType and meta type specified
												{
													partName = tempStr;
													// check for validity of participant name:
													if(action.getParticipant(partName) == null) // invalid participant name
													{
														warnings.add("Invalid participant name: \"" + partName + "\" in effect rule " + effRule.getName() + " for "
															+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
															+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
															+ " attribute effect");
													}														
													variableName.append(partName);
													if(vectorContainsString(variables, variableName.toString()) == false) // variable has not been generated yet
													{
														variables.add(variableName.toString()); // add the variable name to the record-keeping vector
														writer.write("double " + variableName + " = (double)(" + action.getName().toLowerCase() + "Act.getAll");
														if(firstWord.indexOf("Active") >= 0)
														{
															writer.write("Active");
														}
														else if(firstWord.indexOf("Inactive") >= 0)
														{
															writer.write("Inactive");
														}
														writer.write(partName + "s().size());");
														writer.write(NEWLINE);
													}																							
												}									
											}
										}
										else if((nextToken.startsWith("input")) || (nextToken.startsWith("-input"))) // rule input
										{
											if(effect.trim().length() == nextToken.trim().length()) // on last token
											{
												effect = null;
											}
											else // not on last token
											{
												effect = effect.trim().substring(nextToken.length()).trim();
											}										
											if(nextToken.startsWith("-"))
											{
												nextToken = nextToken.substring(1); // remove the minus sign for now
											}
											String inputName = nextToken.substring(nextToken.indexOf('-') + 1); // get the input name
											// check for validity of rule input name:
											if(effRule.getRuleInput(inputName) == null) // invalid rule input name
											{
												warnings.add("Invalid rule input name: \"" + inputName + "\" in effect rule " + effRule.getName() + " for "
													+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
													+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
													+ " attribute effect");	
											}
											else // valid rule input name
											{
												// check for validity of rule input type:
												if((effRule.getRuleInput(inputName).getType().equals(InputType.INTEGER) == false) 
													&& (effRule.getRuleInput(inputName).getType().equals(InputType.DOUBLE) == false)) // non-numerical type -- invalid
												{
													warnings.add("Invalid (non-numerical) rule input type: \"" + inputName + "\" in effect rule " + effRule.getName() + " for "
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");	
												}
											}
										}
										
										else if(effect.trim().length() == nextToken.trim().length()) // on last token
										{
											//System.out.println("effect = *" + effect + "*");
											//System.out.println("nextToken right here after effect = *" + nextToken + "*");
											effect = null;
										}
										else
										{
											//System.out.println("effect was *" + effect + "*");
											//System.out.println("nextToken is *" + nextToken + "*");
											effect = effect.trim().substring(nextToken.length()).trim();
											//System.out.println("effect is now *" + effect + "*");
										}
										
										if((effect == null) || (effect.trim().length() == 0)) // that was the last token
										{
											finished = true;
										}
									}
									
									//*********************SECOND RUN-THROUGH***************************
									// go through the effect again to write out the whole expression:
									effect = partAttRuleEff.getEffect();
									finished = false;
									StringBuffer expression = new StringBuffer();		
									while(!finished)
									{
										String nextToken = getNextToken(effect);
										//System.out.println("nextToken = *" + nextToken + "*");
										
										// attributes other participants:
										if(nextToken.startsWith("all") || nextToken.startsWith("-all"))
										{
											String token = nextToken;
											boolean isNegative = false;
											if(token.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
												token = token.substring(1); // remove the minus sign for now
											}
											String activeInactiveToken = token.substring(0, token.indexOf('-')); // get whether it's all, allActive, 
											// or allInactive
											token = token.substring(activeInactiveToken.length() + 1).trim();
											String partName = token.substring(0, token.indexOf('-')); // get the participant name
											token = token.substring(partName.length() + 1).trim();
											String ssObjType = token.substring(0, token.indexOf('-')); // get the SimSEObjectType
											token = token.substring(ssObjType.length() + 1).trim();
											String attName = token.substring(token.indexOf(':') + 1).trim();
											// append this variable name onto the expression:
											expression.append(activeInactiveToken + partName + ssObjType + attName);
											if(isNegative)
											{
												expression.append("))");
											}
											expression.append(" ");
										}
										
										// num actions or num participants:
										else if(nextToken.startsWith("num") || nextToken.startsWith("-num"))
										{
											String token = nextToken;
											boolean isNegative = false;
											if(token.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
												token = token.substring(1); // remove the minus sign for now
											}																				
											String firstWord = token.substring(0, token.indexOf('-'));
											
											if(firstWord.endsWith("This")) // num actions this participant
											{
												ActionTypeParticipant part = partRuleEff.getParticipant();
												String actionName = token.substring(token.indexOf(':') + 1).trim();
												StringBuffer variableName = new StringBuffer("num");
												if(firstWord.indexOf("Active") >= 0)
												{
													variableName.append("Active");
												}
												else if(firstWord.indexOf("Inactive") >= 0)
												{
													variableName.append("Inactive");
												}
												variableName.append("ActionsThisPart");
												if(actionName.equals("*")) // wild card character -- all actions
												{
													variableName.append("A");
												}
												else // action name
												{
													variableName.append(actionName);
												}
												// append the variable name to the expression:
												expression.append(variableName);
												if(isNegative)
												{
													expression.append("))");
												}
												expression.append(" ");
											}
											
											else if(firstWord.indexOf("All") >= 0) // num actions other participants
											{
												StringBuffer variableName = new StringBuffer("numActionsAll");
												if(firstWord.indexOf("Active") >= 0)
												{
													variableName.append("Active");
												}
												else if(firstWord.indexOf("Inactive") >= 0)
												{
													variableName.append("Inactive");
												}
												String tempStr = token.substring(token.indexOf('-') + 1); // take off the first word
												String partName = new String();
												String objTypeName = new String();
												if(tempStr.indexOf('-') >= 0) // includes SimSEObjectType and meta type
												{
													partName = tempStr.substring(0, tempStr.indexOf('-')); // get the participant name
													variableName.append(partName);
													tempStr = tempStr.substring(tempStr.indexOf('-') + 1); // take off the participant name
													objTypeName = tempStr.substring(0, tempStr.indexOf('-')); // get the SimSEObjectType name
													variableName.append(objTypeName);
												}
												else // does not include SimSEObjectType and meta type
												{
													partName = tempStr.substring(0, tempStr.indexOf(':')); // get the participant name
													variableName.append(partName);
												}
												String actionName = token.substring(token.indexOf(':') + 1).trim(); // get the action name
												if(actionName.equals("*")) // wildcard character -- any action
												{
													variableName.append("A");
												}
												else // specific action name
												{
													variableName.append(actionName);
												}
												// append the variable name to the expression:
												expression.append(variableName);
												if(isNegative)
												{
													expression.append("))");
												}
												expression.append(" ");
											}
											
											else // num participants
											{
												StringBuffer variableName = new StringBuffer("num");
												if(firstWord.indexOf("Active") >= 0)
												{
													variableName.append("Active");
												}
												else if(firstWord.indexOf("Inactive") >= 0)
												{
													variableName.append("Inactive");
												}											
												String tempStr = token.substring(token.indexOf('-') + 1); // take off the first word
												String partName = new String();
												String ssObjType = new String();
												if(tempStr.indexOf('-') >= 0) // contains SimSEObjectType and meta type
												{
													partName = tempStr.substring(0, tempStr.indexOf('-'));
													variableName.append(partName);
													String tempStr2 = tempStr.substring(tempStr.indexOf('-') + 1); // take off the part name
													ssObjType = tempStr2.substring(0, tempStr2.indexOf('-'));
													variableName.append(ssObjType);
													// append the variable name to the expression:
													expression.append(variableName);
													if(isNegative)
													{
														expression.append("))");
													}
													expression.append(" ");													
												}
												else // no SimSEObjectType and meta type specified
												{
													partName = tempStr;
													variableName.append(partName);
													// append the variable name to the expression:
													expression.append(variableName);
													if(isNegative)
													{
														expression.append("))");
													}
													expression.append(" ");													
												}									
											}
										}
										
										// rule input:
										else if((nextToken.startsWith("input")) || (nextToken.startsWith("-input")))
										{
											String token = nextToken;
											boolean isNegative = false;
											if(token.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
												token = token.substring(1); // remove the minus sign for now
											}
											String inputName = token.substring(token.indexOf('-') + 1); // get the input name
											// append the variable name to the expression:
											expression.append("input" + inputName);
											if(isNegative)
											{
												expression.append("))");
											}
											expression.append(" ");											
										}
										
										// attributes this participant:
										else if((nextToken.startsWith("this")) || (nextToken.startsWith("-this")))
										{
											String token = nextToken;
											boolean isNegative = false;
											if(token.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
												token = token.substring(1); // remove the minus sign for now
											}		
											// append to expression:
											expression.append("((double)(" + partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".get" 
												+ token.substring(token.indexOf(':') + 1) + "()))");
											if(isNegative)
											{
												expression.append("))");
											}
											expression.append(" ");
										}
										
										// total time elapsed:
										else if((nextToken.startsWith("totalTimeElapsed")) || (nextToken.startsWith("-totalTimeElapsed")))
										{
											boolean isNegative = false;
											if(nextToken.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
											}	
											// append to expression:
											expression.append("((double)(state.getClock().getTime()))");
											if(isNegative)
											{
												expression.append("))");
											}
											expression.append(" ");
										}
										
										// action time elapsed:
										else if((nextToken.startsWith("actionTimeElapsed")) || (nextToken.startsWith("-actionTimeElapsed")))
										{
											boolean isNegative = false;
											if(nextToken.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
											}	
											// append to expression:
											expression.append("((double)(" + action.getName().toLowerCase() + "Act.getTimeElapsed()))");
											if(isNegative)
											{
												expression.append("))");
											}
											expression.append(" ");
										}		
										
										// random:
										else if((nextToken.startsWith("random")) || (nextToken.startsWith("-random")))
										{
											String token = nextToken;
											boolean isNegative = false;									
											if(token.startsWith("-"))
											{
												isNegative = true;
												expression.append("(-1 * (");
												token = token.substring(1); // remove the minus sign for now
											}	
											// get min & max vals:
											try
											{
												Integer minVal = new Integer(token.substring((token.indexOf(':') + 1), token.indexOf(',')));
												Integer maxVal = new Integer(token.substring(token.indexOf(',') + 1));
												// append to expression:
												expression.append("((double)((ranNumGen.nextInt(" + maxVal + " - " + minVal + " + 1) + " + minVal + ")))");
												if(isNegative)
												{
													expression.append("))");
												}
												expression.append(" ");												
											}
											catch(NumberFormatException e)
											{
												JOptionPane.showMessageDialog(null, ("Error reading random value in expression for  " + effRule.getName() + " effect rule: " + e.toString()), 
													"Malformed Effect Rule Expression", JOptionPane.WARNING_MESSAGE);												
											}
										}		
										
										// other token:
										else
										{
											expression.append(nextToken + " ");
										}
										
										if(effect.trim().length() == nextToken.trim().length()) // on last token
										{
											effect = null;
										}
										else // not on last token
										{
											effect = effect.trim().substring(nextToken.length());
										}											
										
										if((effect == null) || (effect.trim().length() == 0)) // that was the last token
										{
											finished = true;
										}
									}		
									String attType = new String();
									if(partAttRuleEff.getAttribute().getType() == AttributeTypes.INTEGER)
									{
										attType = "int";
									}
									else if(partAttRuleEff.getAttribute().getType() == AttributeTypes.DOUBLE)
									{
										attType = "double";
									}
									writer.write(partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".set" 
										+ partAttRuleEff.getAttribute().getName() + "((" + attType + ")(" + expression.toString().trim() + "));");
									writer.write(NEWLINE);									
								}
								else // string or boolean attribute
								{
									int counter = 0;
									String effect = partAttRuleEff.getEffect();
									StringBuffer expression = new StringBuffer();									
									String nextToken = getNextToken(effect);
									
									// attribute this participant:
									if(nextToken.startsWith("this"))
									{									
										// get the attribute name:
										String attName = nextToken.substring(nextToken.indexOf(':') + 1);
										// check for validity of attribute name:
										if(partTypeRuleEff.getSimSEObjectType().getAttribute(attName) == null) // invalid attribute name
										{
											warnings.add("Invalid attribute name: \"" + attName + "\" in effect rule " + effRule.getName() + " for "
												+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
												+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
												+ " attribute effect");												
										}
										else // valid attribute name
										{
											// check for validity of attribute type:
											if(partTypeRuleEff.getSimSEObjectType().getAttribute(attName).getType() != partAttRuleEff.getAttribute().getType()) // invalid attribute type
											{
												warnings.add("Invalid attribute type (" + AttributeTypes.getText(partTypeRuleEff.getSimSEObjectType().getAttribute(attName).getType()) 
													+ "): \"" + attName + "\" in effect rule " + effRule.getName() + " for "
													+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
													+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
													+ " attribute effect");													
											}
										}
										writer.write(partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".set" + partAttRuleEff.getAttribute().getName()
											+ "(" + partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".get" + attName + "());");
										writer.write(NEWLINE);
									}
									
									// rule input:
									else if(nextToken.startsWith("input"))
									{
										String inputName = nextToken.substring(nextToken.indexOf('-') + 1); // get the input name
										// check for validity of rule input name:
										if(effRule.getRuleInput(inputName) == null) // invalid rule input name
										{
											warnings.add("Invalid rule input name: \"" + inputName + "\" in effect rule " + effRule.getName() + " for "
												+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
												+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
												+ " attribute effect");	
										}
										else // valid rule input name
										{
											// check for validity of rule input type:
											if(partAttRuleEff.getAttribute().getType() == AttributeTypes.STRING) // string attribute
											{
												if(effRule.getRuleInput(inputName).getType().equals(InputType.STRING) == false) // type doesn't match -- invalid
												{
													warnings.add("Invalid rule input type (non-String): \"" + inputName + "\" in effect rule " + effRule.getName() + " for "
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");													
												}
											}
											else if(partAttRuleEff.getAttribute().getType() == AttributeTypes.BOOLEAN) // boolean attribute
											{
												if(effRule.getRuleInput(inputName).getType().equals(InputType.BOOLEAN) == false) // type doesn't match -- invalid
												{
													warnings.add("Invalid rule input type (non-Boolean): \"" + inputName + "\" in effect rule " + effRule.getName() + " for "
														+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
														+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
														+ " attribute effect");														
												}
											}
											
											// write the expression:
											writer.write(partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".set" + partAttRuleEff.getAttribute().getName()
												+ "(input" + inputName + ");");
											writer.write(NEWLINE);
										}
									}
									
									// literal string:
									else if(nextToken.startsWith("\""))
									{
										if(partAttRuleEff.getAttribute().getType() != AttributeTypes.STRING) // invalid 
										{
											warnings.add("Invalid expression (wrong type) in effect rule " + effRule.getName() + " for "
												+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
												+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
												+ " attribute effect");					
										}
										// write the expression:
										writer.write(partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".set" + partAttRuleEff.getAttribute().getName()
											+ "(" + effect.trim() + ");");
										writer.write(NEWLINE);
									}
									
									// boolean val:
									else if(nextToken.startsWith("true"))
									{
										if(partAttRuleEff.getAttribute().getType() != AttributeTypes.BOOLEAN) // invalid
										{
											warnings.add("Invalid expression (wrong type) in effect rule " + effRule.getName() + " for "
												+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
												+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
												+ " attribute effect");					
										}		
										// write the expression:
										writer.write(partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".set" + partAttRuleEff.getAttribute().getName()
											+ "(true);");
										writer.write(NEWLINE);											
									}
									else if(nextToken.startsWith("false"))
									{
										if(partAttRuleEff.getAttribute().getType() != AttributeTypes.BOOLEAN) // invalid
										{
											warnings.add("Invalid expression (wrong type) in effect rule " + effRule.getName() + " for "
												+ partRuleEff.getParticipant().getName() + " " + partTypeRuleEff.getSimSEObjectType().getName() + " "
												+ SimSEObjectTypeTypes.getText(partTypeRuleEff.getSimSEObjectType().getType()) + " " + partAttRuleEff.getAttribute().getName()
												+ " attribute effect");					
										}		
										// write the expression:
										writer.write(partTypeRuleEff.getSimSEObjectType().getName().toLowerCase() + ".set" + partAttRuleEff.getAttribute().getName()
											+ "(false);");
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
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				if(rInputs.size() > 0)
				{
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER))
				{
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);				
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);				
			}
			
			else if(rule instanceof CreateObjectsRule) // CREATE OBJECTS RULE
			{
				CreateObjectsRule coRule = (CreateObjectsRule)rule;
				ActionType action = rule.getActionType();
				writer.write("// " + rule.getName() + " rule (" + action.getName() + " Action):");
				writer.write(NEWLINE);
				if(vectorContainsString(outerVariables, (action.getName().toLowerCase() + "Acts")) == false) // this variable has not been generated yet
				{
					outerVariables.add(new String(action.getName().toLowerCase() + "Acts")); // add the variable name to the record-keeping Vector
					writer.write("Vector " + action.getName().toLowerCase() + "Acts = state.getActionStateRepository().get"
						+ getUpperCaseLeading(action.getName()) + "ActionStateRepository().getAllActions();");
					writer.write(NEWLINE);					
				}
				writer.write("if((updateInstructions ==");
				if(rule.getTiming() == RuleTiming.CONTINUOUS) // continuous rule
				{
					writer.write("UPDATE_ALL_CONTINUOUS))");
				}				
				else if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER)) // trigger/destroyer rule
				{
					writer.write("UPDATE_ONE) && (ruleName.equals(\"" + rule.getName() + "\")))");
				}
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(NEWLINE);				
				writer.write("for(int i=0; i<" + action.getName().toLowerCase() + "Acts.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(action.getName()) + "Action " + action.getName().toLowerCase() + "Act = ("
					+ getUpperCaseLeading(action.getName()) + "Action)" + action.getName().toLowerCase() + "Acts.elementAt(i);");
				writer.write(NEWLINE);
				if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER))
				{				
					writer.write("if(" + action.getName().toLowerCase() + "Act == action)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
				}
				// code to make sure it has the min num of participants:
				writer.write("if(");
				Vector parts = action.getAllParticipants();
				for(int i=0; i<parts.size(); i++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(i);
					if(i > 0) // not on first element
					{
						writer.write(" && ");
					}
					writer.write("(" + action.getName().toLowerCase() + "Act.getAll" + part.getName() + "s().size() >= ");
					if(part.getQuantity().isMinValBoundless()) // min val boundless
					{
						writer.write("0");
					}
					else // has min val
					{
						writer.write(part.getQuantity().getMinVal().toString());
					}
					writer.write(")");
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);	
				writer.write("if((" + action.getName().toLowerCase() + "Act.getTimeElapsed() == 0) || (" + action.getName().toLowerCase() + "Act.getTimeElapsed() == 1))");
				writer.write(NEWLINE);	
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				// go through all of the objects to create:
				Vector objsToCreate = coRule.getAllSimSEObjects();
				for(int i=0; i<objsToCreate.size(); i++)
				{
					StringBuffer lineToCreateObj = new StringBuffer();
					SimSEObject obj = (SimSEObject)objsToCreate.elementAt(i);
					lineToCreateObj.append(getUpperCaseLeading(obj.getSimSEObjectType().getName()) + " " + obj.getSimSEObjectType().getName().toLowerCase() + i + " = new "
						+ getUpperCaseLeading(obj.getSimSEObjectType().getName()) + "(");
					boolean createObj = true;
					// go through all instantiated attributes:
					Vector instAtts = obj.getAllAttributes();
					if(instAtts.size() >= obj.getSimSEObjectType().getAllAttributes().size()) // all attributes are instantiated
					{
						for(int j=0; j<instAtts.size(); j++)
						{
							InstantiatedAttribute att = (InstantiatedAttribute)instAtts.elementAt(j);
							if(att.isInstantiated() == false) // not instantiated
							{
								warnings.add("Not all attributes have been assigned starting values for the " + obj.getSimSEObjectType().getName() + " "
									+ SimSEObjectTypeTypes.getText(obj.getSimSEObjectType().getType()) + " created in the " + coRule.getName() 
									+ " Create Objects Rule");													
								createObj = false;
								break;
							}
							else // instantiated
							{
								if(att.getAttribute().getType() == AttributeTypes.STRING) // string attribute
								{
									lineToCreateObj.append("\"");
								}
								lineToCreateObj.append(att.getValue());
								if(att.getAttribute().getType() == AttributeTypes.STRING) // string attribute
								{
									lineToCreateObj.append("\"");
								}
								if(j < (instAtts.size() - 1)) // not on last iteration
								{
									lineToCreateObj.append(", ");
								}
							}
						}
					}
					else // not all atts are instantiated
					{
						warnings.add("Not all attributes have been assigned starting values for the " + obj.getSimSEObjectType().getName() + " "
							+ SimSEObjectTypeTypes.getText(obj.getSimSEObjectType().getType()) + " created in the " + coRule.getName() + " Create Objects Rule");							
						createObj = false;
					}
					if(createObj)
					{
						lineToCreateObj.append(");");
						writer.write(lineToCreateObj.toString());
						writer.write(NEWLINE);
						writer.write("state.get" + SimSEObjectTypeTypes.getText(obj.getSimSEObjectType().getType()) + "StateRepository().get"
							+ getUpperCaseLeading(obj.getSimSEObjectType().getName()) + "StateRepository().add(" + obj.getSimSEObjectType().getName().toLowerCase()
							+ i + ");");
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER))
				{				
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);				
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);	
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			
			else if(rule instanceof DestroyObjectsRule) // DESTROY OBJECTS RULE
			{
				DestroyObjectsRule doRule = (DestroyObjectsRule)rule;
				ActionType action = rule.getActionType();
				writer.write("// " + rule.getName() + " rule (" + action.getName() + " Action):");
				writer.write(NEWLINE);
				if(vectorContainsString(outerVariables, (action.getName().toLowerCase() + "Acts")) == false) // this variable has not been generated yet
				{
					outerVariables.add(new String(action.getName().toLowerCase() + "Acts")); // add the variable name to the record-keeping Vector
					writer.write("Vector " + action.getName().toLowerCase() + "Acts = state.getActionStateRepository().get"
						+ getUpperCaseLeading(action.getName()) + "ActionStateRepository().getAllActions();");
					writer.write(NEWLINE);					
				}
				writer.write("if((updateInstructions ==");
				if(rule.getTiming() == RuleTiming.CONTINUOUS) // continuous rule
				{
					writer.write("UPDATE_ALL_CONTINUOUS))");
				}				
				else if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER)) // trigger/destroyer rule
				{
					writer.write("UPDATE_ONE) && (ruleName.equals(\"" + rule.getName() + "\")))");
				}
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);				
				writer.write("for(int i=0; i<" + action.getName().toLowerCase() + "Acts.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(action.getName()) + "Action " + action.getName().toLowerCase() + "Act = ("
					+ getUpperCaseLeading(action.getName()) + "Action)" + action.getName().toLowerCase() + "Acts.elementAt(i);");
				writer.write(NEWLINE);
				if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER))
				{				
					writer.write("if(" + action.getName().toLowerCase() + "Act == action)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
				}
				// code to make sure it has the min num of participants:
				writer.write("if(");
				Vector parts = action.getAllParticipants();
				for(int i=0; i<parts.size(); i++)
				{
					ActionTypeParticipant part = (ActionTypeParticipant)parts.elementAt(i);
					if(i > 0) // not on first element
					{
						writer.write(" && ");
					}
					writer.write("(" + action.getName().toLowerCase() + "Act.getAll" + part.getName() + "s().size() >= ");
					if(part.getQuantity().isMinValBoundless()) // min val boundless
					{
						writer.write("0");
					}
					else // has min val
					{
						writer.write(part.getQuantity().getMinVal().toString());
					}
					writer.write(")");
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);	
				writer.write("if((" + action.getName().toLowerCase() + "Act.getTimeElapsed() == 0) || (" + action.getName().toLowerCase() + "Act.getTimeElapsed() == 1))");
				writer.write(NEWLINE);	
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);			
			
				// go through each participant condition:
				Vector partConditions = doRule.getAllParticipantConditions();
				for(int j=0; j<partConditions.size(); j++)
				{
					DestroyObjectsRuleParticipantCondition cond = (DestroyObjectsRuleParticipantCondition)partConditions.elementAt(j);
					ActionTypeParticipant part = cond.getParticipant();
					
					writer.write("Vector " + part.getName().toLowerCase() + "s = ((" + getUpperCaseLeading(action.getName()) 
						+ "Action)" + action.getName().toLowerCase() + "Act).getAll" + part.getName() + "s();");
					writer.write(NEWLINE);
					writer.write("for(int j=0; j<" + part.getName().toLowerCase() + "s.size(); j++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write(SimSEObjectTypeTypes.getText(part.getSimSEObjectTypeType()) + " a = (" 
						+ SimSEObjectTypeTypes.getText(part.getSimSEObjectTypeType()) + ")" + part.getName().toLowerCase() + "s.elementAt(j);");
					writer.write(NEWLINE);
					// go through all participant constraints:
					Vector constraints = cond.getAllConstraints();
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
									writer.write(" && ");
								}
								writer.write("(((" + getUpperCaseLeading(objTypeName) + ")a).get" + tempAttConst.getAttribute().getName() + "()");
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
								writer.write(")");
								numAttConsts++;
							}								
						}
						if(numAttConsts > 0) // there is at least one constraint
						{
							writer.write(")");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							writer.write("state.get" + SimSEObjectTypeTypes.getText(constraint.getSimSEObjectType().getType()) + "StateRepository().get" 
								+ getUpperCaseLeading(constraint.getSimSEObjectType().getName()) + "StateRepository().remove((" 
								+ getUpperCaseLeading(constraint.getSimSEObjectType().getName()) + ")a);");
							writer.write(NEWLINE);
							writer.write("state.getActionStateRepository().removeFromAllActions(a);");
							writer.write(NEWLINE);
							writer.write("checkAllMins(gui);");
							writer.write(NEWLINE);							
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
						else // no constraints -- destroy object
						{
							writer.write("state.get" + SimSEObjectTypeTypes.getText(constraint.getSimSEObjectType().getType()) + "StateRepository().get" 
								+ getUpperCaseLeading(constraint.getSimSEObjectType().getName()) + "StateRepository().remove((" 
								+ getUpperCaseLeading(constraint.getSimSEObjectType().getName()) + ")a);");
							writer.write(NEWLINE);
							writer.write("state.getActionStateRepository().removeFromAllActions(a);");
							writer.write(NEWLINE);
							writer.write("checkAllMins(gui);");
							writer.write(NEWLINE);							
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);						
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				if((rule.getTiming() == RuleTiming.TRIGGER) || (rule.getTiming() == RuleTiming.DESTROYER))
				{				
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);				
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);	
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);					
			}				
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + ruleExFile.getPath() + ": " + e.toString()), "File IO Error",
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
	
	
	private String getNextToken(String tokenString) // returns the next token in the token string
	{
		//System.out.println("********** At the beginning of getLastToken for tokenString = *" + tokenString + "*");
		//System.out.println("(tokenString.trim().indexOf(' ') = " + tokenString.trim().indexOf(' '));
		//System.out.println("(tokenString.trim().indexOf('(') = " + tokenString.trim().indexOf('('));
		//System.out.println("(tokenString.trim().indexOf(')') = " + tokenString.trim().indexOf(')'));
		if((tokenString.equals(null)) || (tokenString.length() == 0)) // empty text field
		{
			//System.out.println("Empty tokenString!");
			return null;
		}
		else if((tokenString.trim().indexOf(' ') < 0) && (tokenString.indexOf('(') < 0) && (tokenString.indexOf(')') < 0)) // no
			// spaces & no parentheses -- must be only one token
		{
			//System.out.println("Only one token!");
			//System.out.println("********** At the end of getLastToken for tokenString = *" + tokenString +
			//"* and About to return tokenString.trim(), which = *" + tokenString.trim() + "*");
			if(tokenString.trim().startsWith("-"))
			{
				if(tokenString.trim().length() > 1) // just the minus sign -- means it's an operator
				{
					return tokenString.trim().substring(0); // return just neg. sign
				}
				else // more stuff after the minus sign -- means it's a negative sign
				{
					return tokenString.trim();
				}
			}
			else
			{
				return tokenString.trim();
			}
		}
		else if(tokenString.startsWith("("))
		{
			return "(";
		}
		else if(tokenString.startsWith(")"))
		{
			return ")";
		}
		else // multiple tokens
		{
			if(tokenString.trim().startsWith("("))
			{
				//System.out.println("********** At the end of getLastToken for tokenString = *" + tokenString +
				//"* and About to return lastBlock.substring(1), which = *" + lastBlock.substring(1) + "*");
				return "(";
			}
			else if(tokenString.trim().startsWith(")"))
			{
				return ")";
			}			
			//System.out.println("Multiple tokens!");
			//System.out.println("tokenString here = *" + tokenString + "*");
			String firstBlock = new String();
			if(tokenString.trim().indexOf(' ') >= 0) // contains a space
			{
				firstBlock = tokenString.trim().substring(0, tokenString.trim().indexOf(' '));
			}
			else if(tokenString.indexOf(')') >= 0) // contains a closing paren but no spaces
			{
				firstBlock = tokenString.trim().substring(0, tokenString.trim().indexOf(')'));
			}
			
			if(firstBlock.endsWith(")"))
			{
				return getNextToken(firstBlock.substring(0, (firstBlock.length() -1)));
			}
			else if(firstBlock.startsWith("-"))
			{
				if(firstBlock.length() == 1) // space after the minus sign -- means it's an operator
				{
					return tokenString.trim().substring(0, 1); // return just neg. sign
				}
				else // no space after the minus sign -- means it's a negative sign
				{
					return firstBlock;
				}
			}
			else // no parentheses or minus signs
			{
				//System.out.println("************No parentheses, at the end of getLastToken for tokenString = *" + tokenString +
				//" and about to return lastBlock, which = *" + lastBlock + "*");
				return firstBlock;
			}
		}
	}
	
	
	private void initializeRuleLists() // gets the rules in prioritized order
	{
		// initialize lists:
		nonPrioritizedRules = new Vector();
		prioritizedRules = new Vector();
		Vector allActions = actTypes.getAllActionTypes();
		// go through all action types and get their rules:
		for(int i=0; i<allActions.size(); i++)
		{
			ActionType tempAct = (ActionType)allActions.elementAt(i);
			Vector trigRules = tempAct.getAllTriggerRules();
			Vector contRules = tempAct.getAllContinuousRules();
			Vector destRules = tempAct.getAllDestroyerRules();
			Vector rules = new Vector();
			// add all rules in order to vector:
			for(int j=0; j<trigRules.size(); j++)
			{
				rules.add(trigRules.elementAt(j));
			}
			for(int j=0; j<contRules.size(); j++)
			{
				rules.add(contRules.elementAt(j));
			}
			for(int j=0; j<destRules.size(); j++)
			{
				rules.add(destRules.elementAt(j));
			}
			// go through each rule and add it to the list:
			for(int j=0; j<rules.size(); j++)
			{
				Rule tempRule = (Rule)rules.elementAt(j);
				int priority = tempRule.getPriority();
				if(priority == -1) // rule is not prioritized yet
				{
					nonPrioritizedRules.addElement(tempRule);
				}
				else // priority >= 0
				{
					if(prioritizedRules.size() == 0) // no elements have been added yet to the prioritized rule list
					{
						prioritizedRules.add(tempRule);
					}
					else
					{
						// find the correct position to insert the rule at:
						for(int k=0; k<prioritizedRules.size(); k++)
						{
							Rule tempR = (Rule)prioritizedRules.elementAt(k);
							if(priority <= tempR.getPriority())
							{
								prioritizedRules.insertElementAt(tempRule, k); // insert the rule info
								break;
							}
							else if(k == (prioritizedRules.size() - 1)) // on the last element
							{
								prioritizedRules.add(tempRule); // add the rule info to the end of the list
								break;
							}
						}
					}
				}
			}
		}
	}
}
