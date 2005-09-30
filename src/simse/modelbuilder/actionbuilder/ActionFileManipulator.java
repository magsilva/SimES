/* This class is for generating the DefinedActions into a file and reading that file into memory*/

package simse.modelbuilder.actionbuilder;

import simse.modelbuilder.objectbuilder.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class ActionFileManipulator
{
	private DefinedObjectTypes objectTypes;
	private DefinedActionTypes actionTypes;
	private final char NEWLINE = '\n';
	private final String EMPTY_VALUE = new String("<>");

	// object file constants:
	private final String BEGIN_OBJECT_FILENAME_TAG = "<beginObjectFileName>";
	private final String END_OBJECT_FILENAME_TAG = "<endObjectFileName>";

	// action file constants:
	private final String BEGIN_DEFINED_ACTIONS_TAG = "<beginDefinedActionTypes>";
	private final String END_DEFINED_ACTIONS_TAG = new String("<endDefinedActionTypes>");
	private final String BEGIN_ACTION_TYPE_TAG = new String("<beginActionType>");
	private final String END_ACTION_TYPE_TAG = new String("<endActionType>");
	private final String BEGIN_ACTION_TYPE_ANNOTATION_TAG = new String("<beginActionTypeAnnotation>");
	private final String END_ACTION_TYPE_ANNOTATION_TAG = new String("<endActionTypeAnnotation>");
	private final String BEGIN_PARTICIPANT_TAG = new String("<beginActionTypeParticipant>");
	private final String END_PARTICIPANT_TAG = new String("<endActionTypeParticipant>");
	private final String BEGIN_QUANTITY_TAG = new String("<beginActionTypeParticipantQuantity>");
	private final String END_QUANTITY_TAG = new String("<endActionTypeParticipantQuantity>");
	private final String BEGIN_SSOBJ_TYPE_NAMES_TAG = new String("<beginSSObjTypeNames>");
	private final String END_SSOBJ_TYPE_NAMES_TAG = new String("<endSSObjTypeNames>");
	private final String BEGIN_TRIGGER_TAG = new String("<beginActionTypeTrigger>");
	private final String END_TRIGGER_TAG = new String("<endActionTypeTrigger>");
	private final String BEGIN_PARTICIPANT_TRIGGER_TAG = new String("<beginActionTypeParticipantTrigger>");
	private final String END_PARTICIPANT_TRIGGER_TAG = new String("<endActionTypeParticipantTrigger>");
	private final String BEGIN_PARTICIPANT_CONSTRAINT_TAG = new String("<beginActionTypeParticipantConstraint>");
	private final String END_PARTICIPANT_CONSTRAINT_TAG = new String("<endActionTypeParticipantConstraint>");
	private final String BEGIN_ATTRIBUTE_CONSTRAINT_TAG = new String("<beginActionTypeParticipantAttributeConstraint>");
	private final String END_ATTRIBUTE_CONSTRAINT_TAG = new String("<endActionTypeParticipantAttributeConstraint>");
	private final String BEGIN_DESTROYER_TAG = new String("<beginActionTypeDestroyer>");
	private final String END_DESTROYER_TAG = new String("<endActionTypeDestroyer>");
	private final String BEGIN_PARTICIPANT_DESTROYER_TAG = new String("<beginActionTypeParticipantDestroyer>");
	private final String END_PARTICIPANT_DESTROYER_TAG = new String("<endActionTypeParticipantDestroyer>");


	public ActionFileManipulator(DefinedObjectTypes defObjs, DefinedActionTypes defActs)
	{
		objectTypes = defObjs;
		actionTypes = defActs;
	}


	public Vector loadFile(File inputFile) // loads the model file into memory, filling the
		// "actionTypes" data structure with the data from the file, and returns a Vector of warning messages
	{
		actionTypes.clearAll();
		Vector warnings = new Vector(); // vector of warning messages
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			boolean foundBeginningOfActions = false;
			while(!foundBeginningOfActions)
			{
				String currentLine = reader.readLine(); // read in a line of text from the file
				if(currentLine.equals(BEGIN_DEFINED_ACTIONS_TAG)) // beginning of action types
				{
					foundBeginningOfActions = true;
					boolean endOfActions = false;
					while(!endOfActions)
					{						
						currentLine = reader.readLine();
						if(currentLine.equals(END_DEFINED_ACTIONS_TAG)) // end of defined actions
						{
							endOfActions = true;
						}
						else // not end of defined actions yet
						{			
							if(currentLine.equals(BEGIN_ACTION_TYPE_TAG))
							{
								ActionType newAct = new ActionType(reader.readLine()); 
								newAct.setVisibilityInSimulation(Boolean.valueOf(reader.readLine()).booleanValue()); // set visibility
								if(newAct.isVisibleInSimulation())
								{
									newAct.setDescription(reader.readLine()); // set description
								}
								else 
								{
									newAct.setDescription(null);
								}
								
								// visibility in explanatory tool / annotation
								reader.mark(newAct.getAnnotation().length() + 10);
								currentLine = reader.readLine(); // get the next line
								
								// new format 9/28/05 that includes annotation and visibility in explanatory tool
								if (currentLine.equals("true") || currentLine.equals("false"))
								{
								    newAct.setVisibilityInExplanatoryTool(Boolean.valueOf(currentLine).booleanValue());
									StringBuffer annotation = new StringBuffer();
									String tempInLine = reader.readLine(); // get the begin annotation tag
									tempInLine = reader.readLine();
									while(tempInLine.equals(END_ACTION_TYPE_ANNOTATION_TAG) == false) // not done yet
									{
										annotation.append(tempInLine);
										tempInLine = reader.readLine();
										if(tempInLine.equals(END_ACTION_TYPE_ANNOTATION_TAG) == false) // not done yet
										{
											annotation.append('\n');
										}
									}
									newAct.setAnnotation(annotation.toString());
								}
								else // has no visibility in exp tool / annotation (older version)
								{
								    reader.reset();
								}
								
								int ssObjTypeType = 0; // SimSEObjectTypeType of this ActionType
		
								boolean endOfAct = false;
								while(!endOfAct)
								{
									currentLine = reader.readLine(); // get the next line
									if(currentLine.equals(END_ACTION_TYPE_TAG)) // end of action type
									{
										endOfAct = true;
										actionTypes.addActionType(newAct); // add action type to defined action types
									}
									else if(currentLine.equals(BEGIN_PARTICIPANT_TAG)) // beginning of ActionTypeParticipant
									{
										String partName = reader.readLine(); // get the participant name
										String metaTypeName = reader.readLine();
										ssObjTypeType = SimSEObjectTypeTypes.getIntRepresentation(metaTypeName); // get the SimSEObjectTypeType
										ActionTypeParticipant newPart = new ActionTypeParticipant(ssObjTypeType); // create a new ActionTypeParticipant
										// with this info
										newPart.setName(partName); // set the participant name
										String restricted = reader.readLine(); // get the restricted status
										// NOTE: This restricted stuff has been taken out but is still in the file for backwards compatability:
										//newPart.setRestricted((new Boolean(restricted)).booleanValue()); // set the restricted status
										boolean endOfPart = false;
										while(!endOfPart)
										{
											String currentLine2 = reader.readLine(); // get the next line
											if(currentLine2.equals(END_PARTICIPANT_TAG)) // end of ActionTypeParticipant
											{
												endOfPart = true;
												if(newPart.getAllSimSEObjectTypes().size() > 0) // participant has at least one valid
													// SimSEObjectType left
												{
													newAct.addParticipant(newPart); // add participant to action type
												}
												else // all of the participant's SimSEObjectTypes were invalid
												{
													warnings.add("All of " + newAct.getName() + " action's " + newPart.getName()
														+ " participant's allowable object types are invalid -- ignoring this participant");
												}
											}
											else if(currentLine2.equals(BEGIN_QUANTITY_TAG)) // beginning of ActionTypeParticipantQuantity
											{
												newPart.getQuantity().setGuard(Guard.getIntRepresentation(reader.readLine())); // set the Guard
												String quantity = reader.readLine(); // get quantity
												String maxVal = reader.readLine(); // get max val
												Integer[] quants = new Integer[2];
												if((quantity.equals(EMPTY_VALUE)) == false) // quantity has a value
												{
													quants[0] = new Integer(Integer.parseInt(quantity));
												}
												if((maxVal.equals(EMPTY_VALUE)) == false) // max val has a value
												{
													quants[1] = new Integer(Integer.parseInt(maxVal));
												}
												newPart.getQuantity().setQuantity(quants); // set quantity
												reader.readLine(); // get next line (END_QUANTITY_TAG)
											}
											else if(currentLine2.equals(BEGIN_SSOBJ_TYPE_NAMES_TAG)) // SimSEObjectType names
											{
												boolean endOfSSObjTypes = false;
												while(!endOfSSObjTypes)
												{
													String currentLine3 = reader.readLine(); // get the next line
													if(currentLine3.equals(END_SSOBJ_TYPE_NAMES_TAG)) // end of SimSEObjectType names
													{
														endOfSSObjTypes = true;
													}
													else
													{
														SimSEObjectType tempType = objectTypes.getObjectType(ssObjTypeType, currentLine3); // get
															// the SimSEObjectType from the defined object types
														if(tempType != null) // object type was found
														{
															newPart.addSimSEObjectType(tempType);
																// add the object type to the participant
														}
														else // object type not found
														{
															warnings.add("Object type " + SimSEObjectTypeTypes.getText(ssObjTypeType) + " "
																+ currentLine3 + " removed -- ignoring " + newAct.getName()
																	+ " action participant of this type");
														}
													}
												}
											}
										}
									}
									else if(currentLine.equals(BEGIN_TRIGGER_TAG)) // ActionTypeTrigger
									{
										ActionTypeTrigger newTrig; // ActionTypeTrigger to be filled in w/ the info. from the file
										String triggerName = reader.readLine(); // get the trigger name
										String triggerType = reader.readLine(); // get the trigger type
										if(triggerType.equals(ActionTypeTrigger.RANDOM)) // random trigger type
										{
											double freq = Double.parseDouble(reader.readLine()); // get the frequency
											newTrig = new RandomActionTypeTrigger(triggerName, newAct, freq); // create a new random trigger with
											// the specified frequency
										}
										else if(triggerType.equals(ActionTypeTrigger.USER)) // user trigger type
										{
											newTrig = new UserActionTypeTrigger(triggerName, newAct, reader.readLine()); // create a new user trigger
											// with the menu text from the file
										}
										else // autonomous trigger type
										{
											newTrig = new AutonomousActionTypeTrigger(triggerName, newAct); // create a new autonomous trigger
										}
										String triggerText = reader.readLine(); // get the trigger text
										// set the trigger text:
										if(triggerText.equals(EMPTY_VALUE) == false) // trigger text not empty
										{
											newTrig.setTriggerText(triggerText);
										}
										String currentLineTrig = reader.readLine();
										boolean getNextLine = true;
										if(currentLineTrig.startsWith("<") == false) // new format 4/28/04 that includes trigger/destroyer prioritization
										{
											newTrig.setPriority(Integer.parseInt(currentLineTrig)); // set priority
										}
										else
										{
											getNextLine = false;
										}							
										
										boolean endOfTrig = false;
										while(!endOfTrig)
										{
											if(getNextLine)
											{
												currentLineTrig = reader.readLine(); // get the next line
											}
											else
											{
												getNextLine = true;
											}
											if(currentLineTrig.startsWith("<") == false) // new format 5/13/04 that includes game-ending triggers/destroyers
											{
												newTrig.setGameEndingTrigger((new Boolean(currentLineTrig)).booleanValue());
											}
											else if(currentLineTrig.equals(END_TRIGGER_TAG)) // end of trigger
											{
												endOfTrig = true;
												newAct.addTrigger(newTrig);
											}
											else if(currentLineTrig.equals(BEGIN_PARTICIPANT_TRIGGER_TAG)) // beginning of participant trigger
											{
												ActionTypeParticipant tempPart = newAct.getParticipant(reader.readLine());
												if(tempPart != null) // participant found
												{
													ActionTypeParticipantTrigger newPartTrig =
														new ActionTypeParticipantTrigger(tempPart); // create a new participant
													// trigger with the specified participant
													boolean endOfPartTrig = false;
													while(!endOfPartTrig)
													{
														String currentLinePartTrig = reader.readLine(); // get the next line
														if(currentLinePartTrig.equals(END_PARTICIPANT_TRIGGER_TAG)) // end of participant trigger
														{
															endOfPartTrig = true;
															newTrig.addParticipantTrigger(newPartTrig);
														}
														else if(currentLinePartTrig.equals(BEGIN_PARTICIPANT_CONSTRAINT_TAG)) // beginning of particpiant
															// constraint
														{
															String ssObjTypeName = reader.readLine();
															int type = newPartTrig.getParticipant().getSimSEObjectTypeType();
															SimSEObjectType tempObjType = objectTypes.getObjectType(type, ssObjTypeName); // get
																// the SimSEObjectType from the defined objects
															if(tempObjType != null) // such a type exists
															{
																ActionTypeParticipantConstraint newPartConst =
																	new ActionTypeParticipantConstraint(tempObjType); // get the SimSEObjectTypeType
																		// from the defined objects and use that and the SimSEObjectType name (read
																		// from the file) to create a new ActionTypeParticipantConstraint
																boolean endOfPartConst = false;
																while(!endOfPartConst)
																{
																	String currentLinePartConst = reader.readLine(); // get the next line
																	if(currentLinePartConst.equals(END_PARTICIPANT_CONSTRAINT_TAG)) // end of participant
																		// constraint
																	{
																		endOfPartConst = true;
																		newPartTrig.addConstraint(newPartConst);
																	}
																	else if(currentLinePartConst.equals(BEGIN_ATTRIBUTE_CONSTRAINT_TAG)) // beginning of
																		// attribute constraint
																	{
																		String attName = reader.readLine(); // get the attribute name
																		Attribute att = newPartConst.getSimSEObjectType().getAttribute(attName); // get the
																		// actual Attribute object
																		if(att == null) // attribute not found
																		{
																			warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																				+ " " + newPartConst.getSimSEObjectType().getName() + " " + attName
																				+ " attribute removed -- ignoring this attribute in "
																				+ newAct.getName() + " action " + tempPart.getName()
																				+ " participant");
																		}
																		else // attribute found
																		{
																			String guard = reader.readLine(); // get the attribute guard
																			ActionTypeParticipantAttributeConstraint newAttConst =
																				new ActionTypeParticipantAttributeConstraint(att); // create a new attribute
																			// constraint with the specified attribute
																			newAttConst.setGuard(guard); // set the guard
																			String value = reader.readLine(); // get the value
																			if((value.equals(EMPTY_VALUE)) == false) // attribute has a constraining
																				// value
																			{
																				if(att.getType() == AttributeTypes.BOOLEAN) // boolean attribute
																				{
																					if(value.equals((new Boolean(true)).toString()))
																					{
																						newAttConst.setValue(new Boolean(true));
																					}
																					else if(value.equals((new Boolean(false)).toString()))
																					{
																						newAttConst.setValue(new Boolean(false));
																					}
																					else // a non-boolean value
																					{
																						warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																							+ " " + newPartConst.getSimSEObjectType().getName()
																							+ " " + attName + " attribute changed type -- "
																							+ " trigger value no longer valid -- ignoring "
																							+ " trigger value for this attribute in "
																							+ newAct.getName() + " action " + tempPart.getName()
																							+ " participant");
																					}
																				}
																				else if(att.getType() == AttributeTypes.INTEGER) // integer attribute
																				{
																					try
																					{
																						boolean valid = true;
																						Integer intVal = new Integer(value);
																						if(((NumericalAttribute)att).isMinBoundless() == false)
																							// has a minimum constraining value
																						{
																							if(intVal.intValue() <
																								((NumericalAttribute)att).getMinValue().intValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed min value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(((NumericalAttribute)att).isMaxBoundless() == false)
																							// has a maximum constraining value
																						{
																							if(intVal.intValue() >
																								((NumericalAttribute)att).getMaxValue().intValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed max value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(valid)
																						{
																							newAttConst.setValue(new Integer(value));
																						}
																					}
																					catch(NumberFormatException e)
																					{
																						warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																							+ " " + newPartConst.getSimSEObjectType().getName()
																							+ " " + attName + " attribute changed type -- "
																							+ " trigger value no longer valid -- ignoring "
																							+ " trigger value for this attribute in "
																							+ newAct.getName() + " action " + tempPart.getName()
																							+ " participant");
																					}
																				}
																				else if(att.getType() == AttributeTypes.DOUBLE) // double attribute
																				{
																					try
																					{
																						boolean valid = true;
																						Double doubleVal = new Double(value);
																						if(((NumericalAttribute)att).isMinBoundless() == false)
																							// has a minimum constraining value
																						{
																							if(doubleVal.doubleValue() <
																								((NumericalAttribute)att).getMinValue().doubleValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed min value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(((NumericalAttribute)att).isMaxBoundless() == false)
																							// has a maximum constraining value
																						{
																							if(doubleVal.doubleValue() >
																								((NumericalAttribute)att).getMaxValue().doubleValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed max value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(valid)
																						{
																							newAttConst.setValue(new Double(value));
																						}
																					}
																					catch(NumberFormatException e)
																					{
																						warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																							+ " " + newPartConst.getSimSEObjectType().getName()
																							+ " " + attName + " attribute changed type -- "
																							+ " trigger value no longer valid -- ignoring "
																							+ " trigger value for this attribute in "
																							+ newAct.getName() + " action " + tempPart.getName()
																							+ " participant");
																					}
																				}
																				else if(att.getType() == AttributeTypes.STRING) // string attribute
																				{
																					newAttConst.setValue(value);
																				}
																			}
																			String newLn = reader.readLine(); 
																			if(newLn.startsWith("<") == false) // new format 5/13/04 that includes scoring attributes
																			{
																				newAttConst.setScoringAttribute((new Boolean(newLn)).booleanValue());
																			}
																			// else read END_ATTRIBUTE_CONSTRAINT_TAG in
																			newPartConst.addAttributeConstraint(newAttConst); // add completed attribute constraint
																			// to the participant constraint
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
		
									else if(currentLine.equals(BEGIN_DESTROYER_TAG)) // ActionTypeDestroyer
									{
										ActionTypeDestroyer newDest; // ActionTypeDestroyer to be filled in w/ info. from the file
										String destroyerName = reader.readLine(); // get the destroyer name
										String destroyerType = reader.readLine(); // get the destroyer type
										if(destroyerType.equals(ActionTypeDestroyer.RANDOM)) // random destroyer type
										{
											double freq = Double.parseDouble(reader.readLine()); // get the frequency
											newDest = new RandomActionTypeDestroyer(destroyerName, newAct, freq); // create a new random destroyer
											// with the specified frequency
										}
										else if(destroyerType.equals(ActionTypeDestroyer.TIMED)) // timed destroyer type
										{
											int time = Integer.parseInt(reader.readLine()); // get the time
											newDest = new TimedActionTypeDestroyer(destroyerName, newAct, time); // create a new timed destroyer with the specified
											// time
										}
										else if(destroyerType.equals(ActionTypeDestroyer.USER)) // user destroyer type
										{
											newDest = new UserActionTypeDestroyer(destroyerName, newAct, reader.readLine()); // create a new user
											// destroyer with the menu text from the file
										}
										else // autonomous destoryer type
										{
											newDest = new AutonomousActionTypeDestroyer(destroyerName, newAct); // create a new autonomous
											// destroyer
										}
										String destroyerText = reader.readLine(); // get the destroyer text
										// set destroyer text:
										if(destroyerText.equals(EMPTY_VALUE) == false) // destroyer text not empty
										{
											newDest.setDestroyerText(destroyerText);
										}
										String currentLineDest = reader.readLine();
										boolean getNextLine = true;
										if(currentLineDest.startsWith("<") == false) // new format 4/28/04 that includes trigger/destroyer prioritization
										{
											newDest.setPriority(Integer.parseInt(currentLineDest)); // set priority
										}
										else
										{
											getNextLine = false;
										}	
		
										boolean endOfDest = false;
										while(!endOfDest)
										{
											if(getNextLine)
											{
												currentLineDest = reader.readLine(); // get the next line
											}
											else
											{
												getNextLine = true;
											}
											if(currentLineDest.startsWith("<") == false) // new format 5/13/04 that includes game-ending triggers/destroyers
											{
												newDest.setGameEndingDestroyer((new Boolean(currentLineDest)).booleanValue());
											}									
											else if(currentLineDest.equals(END_DESTROYER_TAG)) // end of destroyer
											{
												endOfDest = true;
												newAct.addDestroyer(newDest);
											}
											else if(currentLineDest.equals(BEGIN_PARTICIPANT_DESTROYER_TAG)) // beginning of participant destroyer
											{
												ActionTypeParticipant tempPart = newAct.getParticipant(reader.readLine());
												if(tempPart != null) // participant found
												{
													ActionTypeParticipantDestroyer newPartDest =
														new ActionTypeParticipantDestroyer(tempPart); // create a new
													// participant destroyer with the specified participant
													boolean endOfPartDest = false;
													while(!endOfPartDest)
													{
														String currentLinePartDest = reader.readLine(); // get the next line
														if(currentLinePartDest.equals(END_PARTICIPANT_DESTROYER_TAG)) // end of participant destroyer
														{
															endOfPartDest = true;
															newDest.addParticipantDestroyer(newPartDest);
														}
														else if(currentLinePartDest.equals(BEGIN_PARTICIPANT_CONSTRAINT_TAG)) // beginning of particpiant
															// constraint
														{
															String ssObjTypeName = reader.readLine();
															int type = newPartDest.getParticipant().getSimSEObjectTypeType();
															SimSEObjectType tempObjType = objectTypes.getObjectType(type, ssObjTypeName); // get
																// the SimSEObjectType from the defined objects
															if(tempObjType != null) // such a type exists
															{
																ActionTypeParticipantConstraint newPartConst =
																	new ActionTypeParticipantConstraint(tempObjType); // get the SimSEObjectTypeType
																		// from the defined objects and use that and the SimSEObjectType name (read
																		// from the file) to create a new ActionTypeParticipantConstraint
																boolean endOfPartConst = false;
																while(!endOfPartConst)
																{
																	String currentLinePartConst = reader.readLine(); // get the next line
																	if(currentLinePartConst.equals(END_PARTICIPANT_CONSTRAINT_TAG)) // end of participant
																		// constraint
																	{
																		endOfPartConst = true;
																		newPartDest.addConstraint(newPartConst);
																	}
																	else if(currentLinePartConst.equals(BEGIN_ATTRIBUTE_CONSTRAINT_TAG)) // beginning of
																		// attribute constraint
																	{
																		String attName = reader.readLine(); // get the attribute name
																		Attribute att = newPartConst.getSimSEObjectType().getAttribute(attName); // get the
																		// actual Attribute object
																		if(att == null) // attribute not found
																		{
																			// don't add a warning because one was already added for the trigger
																		}
																		else // attribute found
																		{
																			String guard = reader.readLine(); // get the attribute guard
																			ActionTypeParticipantAttributeConstraint newAttConst =
																				new ActionTypeParticipantAttributeConstraint(att); // create a new attribute
																			// constraint with the specified attribute
																			newAttConst.setGuard(guard); // set the guard
																			String value = reader.readLine(); // get the value
																			if((value.equals(EMPTY_VALUE)) == false) // attribute has a constraining
																				// value
																			{
																				if(att.getType() == AttributeTypes.BOOLEAN) // boolean attribute
																				{
																					if(value.equals((new Boolean(true)).toString()))
																					{
																						newAttConst.setValue(new Boolean(true));
																					}
																					else if(value.equals((new Boolean(false)).toString()))
																					{
																						newAttConst.setValue(new Boolean(false));
																					}
																					else // a non-boolean value
																					{
																						warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																							+ " " + newPartConst.getSimSEObjectType().getName()
																							+ " " + attName + " attribute changed type -- "
																							+ " destroyer value no longer valid -- ignoring "
																							+ " destroyer value for this attribute in "
																							+ newAct.getName() + " action " + tempPart.getName()
																							+ " participant");
																					}
																				}
																				else if(att.getType() == AttributeTypes.INTEGER) // integer attribute
																				{
																					try
																					{
																						boolean valid = true;
																						Integer intVal = new Integer(value);
																						if(((NumericalAttribute)att).isMinBoundless() == false)
																							// has a minimum constraining value
																						{
																							if(intVal.intValue() <
																								((NumericalAttribute)att).getMinValue().intValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed min value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(((NumericalAttribute)att).isMaxBoundless() == false)
																							// has a maximum constraining value
																						{
																							if(intVal.intValue() >
																								((NumericalAttribute)att).getMaxValue().intValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed max value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(valid)
																						{
																							newAttConst.setValue(new Integer(value));
																						}
																					}
																					catch(NumberFormatException e)
																					{
																						warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																							+ " " + newPartConst.getSimSEObjectType().getName()
																							+ " " + attName + " attribute changed type -- "
																							+ " destroyer value no longer valid -- ignoring "
																							+ " destroyer value for this attribute in "
																							+ newAct.getName() + " action " + tempPart.getName()
																							+ " participant");
																					}
																				}
																				else if(att.getType() == AttributeTypes.DOUBLE) // double attribute
																				{
																					try
																					{
																						boolean valid = true;
																						Double doubleVal = new Double(value);
																						if(((NumericalAttribute)att).isMinBoundless() == false)
																							// has a minimum constraining value
																						{
																							if(doubleVal.doubleValue() <
																								((NumericalAttribute)att).getMinValue().doubleValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed min value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(((NumericalAttribute)att).isMaxBoundless() == false)
																							// has a maximum constraining value
																						{
																							if(doubleVal.doubleValue() >
																								((NumericalAttribute)att).getMaxValue().doubleValue())
																									// outside of range
																							{
																								warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																									+ " "
																									+ newPartConst.getSimSEObjectType().getName()
																									+ " " + attName + " attribute changed max value -- "
																									+ " trigger value no longer within acceptable range -- ignoring "
																									+ " trigger value for this attribute in "
																									+ newAct.getName() + " action " + tempPart.getName()
																									+ " participant");
																								valid = false;
																							}
																						}
																						if(valid)
																						{
																							newAttConst.setValue(new Double(value));
																						}
																					}
																					catch(NumberFormatException e)
																					{
																						warnings.add(SimSEObjectTypeTypes.getText(newPartConst.getSimSEObjectType().getType())
																							+ " " + newPartConst.getSimSEObjectType().getName()
																							+ " " + attName + " attribute changed type -- "
																							+ " destroyer value no longer valid -- ignoring "
																							+ " destroyer value for this attribute in "
																							+ newAct.getName() + " action " + tempPart.getName()
																							+ " participant");
																					}
																				}
																				else if(att.getType() == AttributeTypes.STRING) // string attribute
																				{
																					newAttConst.setValue(value);
																				}
																			}
																			String newLn = reader.readLine(); 
																			if(newLn.startsWith("<") == false) // new format 5/13/04 that includes scoring attributes
																			{
																				newAttConst.setScoringAttribute((new Boolean(newLn)).booleanValue());
																			}
																			// else read END_ATTRIBUTE_CONSTRAINT_TAG in
																			newPartConst.addAttributeConstraint(newAttConst); // add completed attribute constraint
																			// to the participant constraint
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}		
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, ("Cannot find action file " + inputFile.getPath()), "File Not Found",
				JOptionPane.WARNING_MESSAGE);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error reading file: " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, ("Error reading file: " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		return warnings;
	}
}
