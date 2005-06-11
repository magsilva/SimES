/* This class is for generating the SimSE model data structures into a file and reading that file into memory*/

package simse.modelbuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;
import simse.modelbuilder.mapeditor.*;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class ModelFileManipulator
{
	private DefinedObjectTypes objectTypes;
	private CreatedObjects objects;
	private DefinedActionTypes actionTypes;
	private TileData[][] mapRep;
	private ArrayList sopUsers;

	// general constants
	private final char NEWLINE = '\n';
	private final String EMPTY_VALUE = new String("<>");
	private final String BOUNDLESS = new String("boundless");

	// object types constants:
	private final String BEGIN_OBJECT_TYPES_TAG = new String ("<beginDefinedObjectTypes>");
	private final String END_OBJECT_TYPES_TAG = new String ("<endDefinedObjectTypes>");
	private final String BEGIN_OBJECT_TYPE_TAG = new String("<beginObjectType>");
	private final String END_OBJECT_TYPE_TAG = new String("<endObjectType>");
	private final String BEGIN_ATTRIBUTE_TAG = new String("<beginAttribute>");
	private final String END_ATTRIBUTE_TAG = new String("<endAttribute>");

	// start state constants:
	private final String BEGIN_CREATED_OBJECTS_TAG = new String("<beginCreatedObjects>");
	private final String END_CREATED_OBJECTS_TAG = new String("<endCreatedObjects>");
	private final String BEGIN_OBJECT_TAG = new String("<beginObject>");
	private final String END_OBJECT_TAG = new String("<endObject>");
	private final String BEGIN_INSTANTIATED_ATTRIBUTE_TAG = new String("<beginInstantiatedAttribute>");
	private final String END_INSTANTIATED_ATTRIBUTE_TAG = new String("<endInstantiatedAttribute>");
	private final String BEGIN_STARTING_NARRATIVE_TAG = new String("<beginStartingNarrative>");
	private final String END_STARTING_NARRATIVE_TAG = new String("<endStartingNarrative>");

	// action types constants:
	private final String BEGIN_DEFINED_ACTIONS_TAG = "<beginDefinedActionTypes>";
	private final String END_DEFINED_ACTIONS_TAG = new String("<endDefinedActionTypes>");
	private final String BEGIN_ACTION_TYPE_TAG = new String("<beginActionType>");
	private final String END_ACTION_TYPE_TAG = new String("<endActionType>");
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

	// rule constants:
	private final String BEGIN_RULES_TAG = "<beginRules>";
	private final String END_RULES_TAG = "<endRules>";
	private final String BEGIN_EFFECT_RULE_TAG = "<beginEffectRule>";
	private final String END_EFFECT_RULE_TAG = "<endEffectRule>";
	private final String BEGIN_PARTICIPANT_EFFECT_TAG = "<beginParticipantRuleEffect>";
	private final String END_PARTICIPANT_EFFECT_TAG = "<endParticipantRuleEffect>";
	private final String BEGIN_PARTICIPANT_TYPE_EFFECT_TAG = "<beginParticipantTypeRuleEffect>";
	private final String END_PARTICIPANT_TYPE_EFFECT_TAG = "<endParticipantTypeRuleEffect>";
	private final String BEGIN_ATTRIBUTE_EFFECT_TAG = "<beginParticipantAttributeRuleEffect>";
	private final String END_ATTRIBUTE_EFFECT_TAG = "<endParticipantAttributeRuleEffect>";
	private final String BEGIN_RULE_INPUT_TAG = "<beginRuleInput>";
	private final String END_RULE_INPUT_TAG = "<endRuleInput>";
	private final String BEGIN_RULE_INPUT_CONDITION_TAG = "<beginRuleInputCondition>";
	private final String END_RULE_INPUT_CONDITION_TAG = "<endRuleInputCondition>";
	private final String BEGIN_CREATE_OBJECTS_RULE_TAG = "<beginCreateObjectsRule>";
	private final String END_CREATE_OBJECTS_RULE_TAG = "<endCreateObjectsRule>";
	private final String BEGIN_DESTROY_OBJECTS_RULE_TAG = "<beginDestroyObjectsRule>";
	private final String END_DESTROY_OBJECTS_RULE_TAG = "<endDestroyObjectsRule>";
	private final String BEGIN_PARTICIPANT_CONDITION_TAG = "<beginDestroyObjectsRuleParticipantCondition>";
	private final String END_PARTICIPANT_CONDITION_TAG = "<endDestroyObjectsRuleParticipantCondition>";

	// graphics constants:
	private final String BEGIN_GRAPHICS_TAG = "<beginGraphics>";
	private final String END_GRAPHICS_TAG = "<endGraphics>";
	private final String BEGIN_ICONS_DIR_TAG = "<beginIconDirectoryPath>";
	private final String END_ICONS_DIR_TAG = "<endIconDirectoryPath>";

	// map constants:
	private final String BEGIN_MAP_TAG = "<beginMap>";
	private final String END_MAP_TAG = "<endMap>";
	private final String BEGIN_SOP_USERS_TAG = "<beginSOPUsers>";
	private final String END_SOP_USERS_TAG = "<endSOPUsers>";



	// allow hire and fire constants
private final String BEGIN_ALLOW_HIRE_FIRE_TAG = "<beginAllowHireFire>";
private final String END_ALLOW_HIRE_FIRE_TAG = "<endAllowHireFire>";

	public ModelFileManipulator(DefinedObjectTypes objTypes, DefinedActionTypes defActs, CreatedObjects createdObjs, ArrayList sops,
		TileData[][] map)
	{
		objectTypes = objTypes;
		objects = createdObjs;
		actionTypes = defActs;
		sopUsers = sops;
		mapRep = map;
	}


	public void generateFile(File outputFile, File iconDirectory, Hashtable startStateObjsToImages, Hashtable ruleObjsToImages, boolean allowHireFire) // generates
		// a file with the name of the given file parameter that contains the model data structures in memory
	{
		if(outputFile.exists())
		{
			outputFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(outputFile);


			//*******ALLOW HIRE AND FIRE TAG*******
			writer.write(BEGIN_ALLOW_HIRE_FIRE_TAG);
			writer.write(NEWLINE);
			writer.write(""+allowHireFire);
			writer.write(NEWLINE);
			writer.write(END_ALLOW_HIRE_FIRE_TAG);
			writer.write(NEWLINE);

			//*******OBJECT TYPES**********
			writer.write(BEGIN_OBJECT_TYPES_TAG);
			writer.write(NEWLINE);
			// go through each object type and write it to the file:
			Vector objTypes = objectTypes.getAllObjectTypes();
			for(int i=0; i<objTypes.size(); i++)
			{
				SimSEObjectType tempObj = (SimSEObjectType)(objTypes.elementAt(i));
				writer.write(BEGIN_OBJECT_TYPE_TAG);
				writer.write(NEWLINE);
				writer.write((new Integer(tempObj.getType())).toString());
				writer.write(NEWLINE);
				writer.write(tempObj.getName());
				writer.write(NEWLINE);
				Vector attributes = tempObj.getAllAttributes();
				// go through each attribute and write it to the file:
				for(int j=0; j<attributes.size(); j++)
				{
					Attribute tempAtt = (Attribute)(attributes.elementAt(j));
					writer.write(BEGIN_ATTRIBUTE_TAG);
					writer.write(NEWLINE);
					writer.write(tempAtt.getName());
					writer.write(NEWLINE);
					writer.write((new Integer(tempAtt.getType())).toString());
					writer.write(NEWLINE);

					// visible:
					if(tempAtt.isVisible())
					{
						writer.write('1');
					}
					else // tempAtt.isVisible() == false
					{
						writer.write('0');
					}
					writer.write(NEWLINE);

					// key:
					if(tempAtt.isKey())
					{
						writer.write('1');
					}
					else // tempAtt.isKey() == false
					{
						writer.write('0');
					}
					writer.write(NEWLINE);

					// visibleOnCompletion:
					if(tempAtt.isVisibleOnCompletion())
					{
						writer.write('1');
					}
					else // tempAtt.isVisibleOnCompletion() == false
					{
						writer.write('0');
					}
					writer.write(NEWLINE);

					if(tempAtt instanceof NumericalAttribute)
					{
						// min value:
						if(((NumericalAttribute)tempAtt).isMinBoundless())
						{
							writer.write(BOUNDLESS);
						}
						else // tempAtt.isMinBoundless() == false
						{
							writer.write(((NumericalAttribute)tempAtt).getMinValue().toString());
						}
						writer.write(NEWLINE);

						// max value:
						if(((NumericalAttribute)tempAtt).isMaxBoundless())
						{
							writer.write(BOUNDLESS);
						}
						else // tempAtt.isMaxBoundless() == false
						{
							writer.write(((NumericalAttribute)tempAtt).getMaxValue().toString());
						}
						writer.write(NEWLINE);

						if(tempAtt.getType() == AttributeTypes.DOUBLE) // double attribute
						{
							// min/max num digits:
							// min:
							if(((NumericalAttribute)tempAtt).getMinNumFractionDigits() == null)
							{
								writer.write(BOUNDLESS);
							}
							else
							{
								writer.write(((NumericalAttribute)tempAtt).getMinNumFractionDigits().toString());
							}
							writer.write(NEWLINE);

							// max:
							if(((NumericalAttribute)tempAtt).getMaxNumFractionDigits() == null)
							{
								writer.write(BOUNDLESS);
							}
							else
							{
								writer.write(((NumericalAttribute)tempAtt).getMaxNumFractionDigits().toString());
							}
							writer.write(NEWLINE);
						}
					}
					writer.write(END_ATTRIBUTE_TAG);
					writer.write(NEWLINE);
				}
				writer.write(END_OBJECT_TYPE_TAG);
				writer.write(NEWLINE);
			}
			writer.write(END_OBJECT_TYPES_TAG);
			writer.write(NEWLINE);


			//***********START STATE OBJECTS*************
			writer.write(BEGIN_CREATED_OBJECTS_TAG);
			writer.write(NEWLINE);
			// starting narrative:
			writer.write(BEGIN_STARTING_NARRATIVE_TAG);
			writer.write(NEWLINE);
			String startNarr = objects.getStartingNarrative();
			if((startNarr.equals(null) == false) && (startNarr.length() > 0))
			{
				writer.write(startNarr);
			}
			else
			{
				writer.write(EMPTY_VALUE);
			}
			writer.write(NEWLINE);
			writer.write(END_STARTING_NARRATIVE_TAG);
			writer.write(NEWLINE);

			// go through each object and write it to the file:
			Vector objs = objects.getAllObjects();
			for(int i=0; i<objs.size(); i++)
			{
				SimSEObject tempObj = (SimSEObject)(objs.elementAt(i));
				writer.write(BEGIN_OBJECT_TAG);
				writer.write(NEWLINE);
				writer.write((new Integer(tempObj.getSimSEObjectType().getType())).toString());
				writer.write(NEWLINE);
				writer.write(tempObj.getName());
				writer.write(NEWLINE);
				Vector instAttributes = tempObj.getAllAttributes();
				// go through each instantiated attribute and write it to the file:
				for(int j=0; j<instAttributes.size(); j++)
				{
					InstantiatedAttribute tempInstAtt = (InstantiatedAttribute)(instAttributes.elementAt(j));
					writer.write(BEGIN_INSTANTIATED_ATTRIBUTE_TAG);
					writer.write(NEWLINE);
					writer.write(tempInstAtt.getAttribute().getName());
					writer.write(NEWLINE);
					if(tempInstAtt.getValue() != null) // attribute has a value
					{
						writer.write(tempInstAtt.getValue().toString());
					}
					else // attribute does not have a value
					{
						writer.write(EMPTY_VALUE);
					}
					writer.write(NEWLINE);
					writer.write(END_INSTANTIATED_ATTRIBUTE_TAG);
					writer.write(NEWLINE);
				}
				writer.write(END_OBJECT_TAG);
				writer.write(NEWLINE);
			}
			writer.write(END_CREATED_OBJECTS_TAG);
			writer.write(NEWLINE);


			//************ACTION TYPES*************
			writer.write(BEGIN_DEFINED_ACTIONS_TAG);
			writer.write(NEWLINE);
			// go through each action type and write it to the file:
			Vector actions = actionTypes.getAllActionTypes();
			for(int i=0; i<actions.size(); i++)
			{
				ActionType tempAct = (ActionType)(actions.elementAt(i));
				writer.write(BEGIN_ACTION_TYPE_TAG);
				writer.write(NEWLINE);
				writer.write(tempAct.getName()); // action type name
				writer.write(NEWLINE);
				writer.write("" + tempAct.isVisible()); // visibility
				writer.write(NEWLINE);
				if(tempAct.isVisible())
				{
					writer.write(tempAct.getDescription()); // description
					writer.write(NEWLINE);
				}

				Vector participants = tempAct.getAllParticipants();
				// go through each participant and write it to the file:
				for(int j=0; j<participants.size(); j++)
				{
					ActionTypeParticipant tempPart = (ActionTypeParticipant)(participants.elementAt(j));
					writer.write(BEGIN_PARTICIPANT_TAG);
					writer.write(NEWLINE);
					writer.write(tempPart.getName()); // participant name
					writer.write(NEWLINE);
					writer.write(SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType())); // SimSEObjectTypeType of participant
					writer.write(NEWLINE);
					// NOTE: This restricted stuff has been taken out, but this is still here for backwards compatability with file reader
					writer.write("true"); // restricted
					writer.write(NEWLINE);
					writer.write(BEGIN_QUANTITY_TAG);
					writer.write(NEWLINE);
					writer.write(Guard.getText(tempPart.getQuantity().getGuard())); // quantity guard
					writer.write(NEWLINE);
					if(tempPart.getQuantity().isQuantityBoundless()) // quantity is boundless
					{
						writer.write(EMPTY_VALUE);
					}
					else // quantity has a value
					{
						writer.write(tempPart.getQuantity().getQuantity()[0].toString()); // quantity
					}
					writer.write(NEWLINE);
					if(tempPart.getQuantity().getQuantity()[1] == null) // max value is boundless
					{
						writer.write(EMPTY_VALUE);
					}
					else // max value has a value
					{
						writer.write(tempPart.getQuantity().getQuantity()[1].toString()); // max value
					}
					writer.write(NEWLINE);
					writer.write(END_QUANTITY_TAG);
					writer.write(NEWLINE);
					writer.write(BEGIN_SSOBJ_TYPE_NAMES_TAG);
					writer.write(NEWLINE);
					Vector ssObjTypes = tempPart.getAllSimSEObjectTypes(); // get all of the SimSEObjectTypes for this participant
					// go through each SimSEObjectType and write its name to the file:
					for(int k=0; k<ssObjTypes.size(); k++)
					{
						SimSEObjectType tempType = (SimSEObjectType)(ssObjTypes.elementAt(k));
						writer.write(tempType.getName()); // SimSEObjectType name
						writer.write(NEWLINE);
					}
					writer.write(END_SSOBJ_TYPE_NAMES_TAG);
					writer.write(NEWLINE);
					writer.write(END_PARTICIPANT_TAG);
					writer.write(NEWLINE);
				}

				// triggers:
				Vector allTrigs = tempAct.getAllTriggers();
				for(int j=0; j<allTrigs.size(); j++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)allTrigs.elementAt(j);
					writer.write(BEGIN_TRIGGER_TAG);
					writer.write(NEWLINE);
					// trigger name:
					writer.write(tempTrig.getName());
					writer.write(NEWLINE);
					// trigger type:
					if(tempTrig instanceof AutonomousActionTypeTrigger) // autonomous trigger
					{
						writer.write(ActionTypeTrigger.AUTO); // auto trigger type
						writer.write(NEWLINE);
					}
					else if(tempTrig instanceof RandomActionTypeTrigger) // random trigger
					{
						writer.write(ActionTypeTrigger.RANDOM); // random trigger type
						writer.write(NEWLINE);
						writer.write((new Double(((RandomActionTypeTrigger)(tempTrig)).getFrequency())).toString()); // frequency
						writer.write(NEWLINE);
					}
					else if(tempTrig instanceof UserActionTypeTrigger) // user trigger
					{
						writer.write(ActionTypeTrigger.USER);
						writer.write(NEWLINE);
						writer.write(((UserActionTypeTrigger)(tempTrig)).getMenuText()); // menu text
						writer.write(NEWLINE);
					}
					// trigger text:
					if((tempTrig.getTriggerText() != null) && (tempTrig.getTriggerText().length() > 0))
					{
						writer.write(tempTrig.getTriggerText());
					}
					else
					{
						writer.write(EMPTY_VALUE);
					}
					writer.write(NEWLINE);
					// priority:
					writer.write((new Integer(tempTrig.getPriority())).toString());
					writer.write(NEWLINE);
					// is game-ending trigger:
					writer.write((new Boolean(tempTrig.isGameEndingTrigger())).toString());
					writer.write(NEWLINE);
					// participant triggers:
					Vector partTriggers = tempTrig.getAllParticipantTriggers();
					// go through each ActionTypeParticipantTrigger and write it to the file:
					for(int k=0; k<partTriggers.size(); k++)
					{
						writer.write(BEGIN_PARTICIPANT_TRIGGER_TAG);
						writer.write(NEWLINE);
						ActionTypeParticipantTrigger tempPartTrig = (ActionTypeParticipantTrigger)(partTriggers.elementAt(k));
						writer.write(tempPartTrig.getParticipant().getName()); // participant name
						writer.write(NEWLINE);
						// go through each ActionTypeParticipantConstraint and write it to the file:
						Vector partConstraints = tempPartTrig.getAllConstraints();
						for(int m=0; m<partConstraints.size(); m++)
						{
							ActionTypeParticipantConstraint tempPartConst = (ActionTypeParticipantConstraint)(partConstraints.elementAt(m));
							writer.write(BEGIN_PARTICIPANT_CONSTRAINT_TAG);
							writer.write(NEWLINE);
							writer.write(tempPartConst.getSimSEObjectType().getName()); // name of the SimSEObject type for this participant
							// constraint
							writer.write(NEWLINE);
							// go through each ActionTypeParticipantAttributeConstraint and write it to the file:
							ActionTypeParticipantAttributeConstraint[] attConstraints = tempPartConst.getAllAttributeConstraints();
							for(int n=0; n<attConstraints.length; n++)
							{
								ActionTypeParticipantAttributeConstraint tempAttConst = attConstraints[n];
								writer.write(BEGIN_ATTRIBUTE_CONSTRAINT_TAG);
								writer.write(NEWLINE);
								writer.write(tempAttConst.getAttribute().getName()); // attribute name
								writer.write(NEWLINE);
								writer.write(tempAttConst.getGuard()); // guard
								writer.write(NEWLINE);
								if(tempAttConst.isConstrained()) // has a value
								{
									writer.write(tempAttConst.getValue().toString()); // value
								}
								else // is unconstrained by a value
								{
									writer.write(EMPTY_VALUE);
								}
								writer.write(NEWLINE);
								writer.write((new Boolean(tempAttConst.isScoringAttribute())).toString());
								writer.write(NEWLINE);
								writer.write(END_ATTRIBUTE_CONSTRAINT_TAG);
								writer.write(NEWLINE);
							}
							writer.write(END_PARTICIPANT_CONSTRAINT_TAG);
							writer.write(NEWLINE);
						}
						writer.write(END_PARTICIPANT_TRIGGER_TAG);
						writer.write(NEWLINE);
					}
					writer.write(END_TRIGGER_TAG);
					writer.write(NEWLINE);
				}

				// destroyers:
				Vector allDests = tempAct.getAllDestroyers();
				for(int j=0; j<allDests.size(); j++)
				{
					ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(j);
					writer.write(BEGIN_DESTROYER_TAG);
					writer.write(NEWLINE);
					// destroyer name:
					writer.write(tempDest.getName());
					writer.write(NEWLINE);
					// destroyer type:
					if(tempDest instanceof AutonomousActionTypeDestroyer) // autonomous destroyer
					{
						writer.write(ActionTypeDestroyer.AUTO); // auto destroyer type
						writer.write(NEWLINE);
					}
					else if(tempDest instanceof RandomActionTypeDestroyer) // random destroyer
					{
						writer.write(ActionTypeDestroyer.RANDOM); // random destroyer type
						writer.write(NEWLINE);
						writer.write((new Double(((RandomActionTypeDestroyer)(tempDest)).getFrequency())).toString()); // frequency
						writer.write(NEWLINE);
					}
					else if(tempDest instanceof UserActionTypeDestroyer) // user destroyer
					{
						writer.write(ActionTypeDestroyer.USER);
						writer.write(NEWLINE);
						writer.write(((UserActionTypeDestroyer)(tempDest)).getMenuText()); // menu text
						writer.write(NEWLINE);
					}
					else if(tempDest instanceof TimedActionTypeDestroyer) // timed destroyer
					{
						writer.write(ActionTypeDestroyer.TIMED);
						writer.write(NEWLINE);
						writer.write((new Integer(((TimedActionTypeDestroyer)(tempDest)).getTime())).toString()); // time
						writer.write(NEWLINE);
					}
					// destroyer text:
					if((tempDest.getDestroyerText() != null) && (tempDest.getDestroyerText().length() > 0))
					{
						writer.write(tempDest.getDestroyerText());
					}
					else
					{
						writer.write(EMPTY_VALUE);
					}
					writer.write(NEWLINE);
					// priority:
					writer.write((new Integer(tempDest.getPriority())).toString());
					writer.write(NEWLINE);
					// is game-ending destroyer:
					writer.write((new Boolean(tempDest.isGameEndingDestroyer())).toString());
					writer.write(NEWLINE);
					// participant destroyers:
					Vector partDestroyers = tempDest.getAllParticipantDestroyers();
					// go through each ActionTypeParticipantDestroyer and write it to the file:
					for(int k=0; k<partDestroyers.size(); k++)
					{
						writer.write(BEGIN_PARTICIPANT_DESTROYER_TAG);
						writer.write(NEWLINE);
						ActionTypeParticipantDestroyer tempPartDest = (ActionTypeParticipantDestroyer)(partDestroyers.elementAt(k));
						writer.write(tempPartDest.getParticipant().getName()); // participant name
						writer.write(NEWLINE);
						// participant constraints:
						Vector partConstraints = tempPartDest.getAllConstraints();
						// go through each ActionTypeParticipantConstraint and write it to the file:
						for(int m=0; m<partConstraints.size(); m++)
						{
							ActionTypeParticipantConstraint tempPartConst = (ActionTypeParticipantConstraint)(partConstraints.elementAt(m));
							writer.write(BEGIN_PARTICIPANT_CONSTRAINT_TAG);
							writer.write(NEWLINE);
							writer.write(tempPartConst.getSimSEObjectType().getName()); // name of the SimSEObject type for this participant
							// constraint
							writer.write(NEWLINE);
							// attribute constraints:
							ActionTypeParticipantAttributeConstraint[] attConstraints = tempPartConst.getAllAttributeConstraints();
							for(int n=0; n<attConstraints.length; n++)
							{
								ActionTypeParticipantAttributeConstraint tempAttConst = attConstraints[n];
								writer.write(BEGIN_ATTRIBUTE_CONSTRAINT_TAG);
								writer.write(NEWLINE);
								writer.write(tempAttConst.getAttribute().getName()); // attribute name
								writer.write(NEWLINE);
								writer.write(tempAttConst.getGuard()); // guard
								writer.write(NEWLINE);
								if(tempAttConst.isConstrained()) // has a value
								{
									writer.write(tempAttConst.getValue().toString()); // value
								}
								else // is unconstrained by a value
								{
									writer.write(EMPTY_VALUE);
								}
								writer.write(NEWLINE);
								writer.write((new Boolean(tempAttConst.isScoringAttribute())).toString());
								writer.write(NEWLINE);
								writer.write(END_ATTRIBUTE_CONSTRAINT_TAG);
								writer.write(NEWLINE);
							}
							writer.write(END_PARTICIPANT_CONSTRAINT_TAG);
							writer.write(NEWLINE);
						}
						writer.write(END_PARTICIPANT_DESTROYER_TAG);
						writer.write(NEWLINE);
					}
					writer.write(END_DESTROYER_TAG);
					writer.write(NEWLINE);
				}
				writer.write(END_ACTION_TYPE_TAG);
				writer.write(NEWLINE);
			}
			writer.write(END_DEFINED_ACTIONS_TAG);
			writer.write(NEWLINE);


			//**************RULES***************
			writer.write(BEGIN_RULES_TAG);
			writer.write(NEWLINE);
			// go through each action type and write its rules to the file:
			for(int i=0; i<actions.size(); i++)
			{
				ActionType tempAct = (ActionType)(actions.elementAt(i));
				Vector rules = tempAct.getAllRules();
				// go through each rule and write it to the file:
				for(int j=0; j<rules.size(); j++)
				{
					Rule tempRule = (Rule)rules.elementAt(j);
					if(tempRule instanceof EffectRule) // EffectRule
					{
						writer.write(BEGIN_EFFECT_RULE_TAG);
						writer.write(NEWLINE);
						writer.write(tempRule.getName()); // rule name
						writer.write(NEWLINE);
						writer.write((new Integer(tempRule.getPriority())).toString()); // rule priority
						writer.write(NEWLINE);
						writer.write(tempAct.getName()); // action name
						writer.write(NEWLINE);
						writer.write((new Integer(tempRule.getTiming())).toString()); // rule timing
						writer.write(NEWLINE);
						writer.write((new Boolean(tempRule.getExecuteOnJoins())).toString()); // rule execute on join status
						writer.write(NEWLINE);
						Vector partEffects = ((EffectRule)tempRule).getAllParticipantRuleEffects();
						// go through each ParticipantRuleEffect and write it to the file:
						for(int k=0; k<partEffects.size(); k++)
						{
							writer.write(BEGIN_PARTICIPANT_EFFECT_TAG);
							writer.write(NEWLINE);
							ParticipantRuleEffect tempPartEffect = (ParticipantRuleEffect)partEffects.elementAt(k);
							writer.write(tempPartEffect.getParticipant().getName()); // participant name
							writer.write(NEWLINE);
							Vector partTypeEffects = tempPartEffect.getAllParticipantTypeEffects();
							// go through each ParticipantTypeRuleEffect and write it to the file:
							for(int m=0; m<partTypeEffects.size(); m++)
							{
								writer.write(BEGIN_PARTICIPANT_TYPE_EFFECT_TAG);
								writer.write(NEWLINE);
								ParticipantTypeRuleEffect tempPartTypeEffect = (ParticipantTypeRuleEffect)partTypeEffects.elementAt(m);
								writer.write((new Integer(tempPartTypeEffect.getSimSEObjectType().getType())).toString());
								// SimSEObjectTypeType
								writer.write(NEWLINE);
								writer.write(tempPartTypeEffect.getSimSEObjectType().getName()); // SimSEObjectType name
								writer.write(NEWLINE);
								writer.write(tempPartTypeEffect.getOtherActionsEffect()); // other actions effect
								writer.write(NEWLINE);
								Vector partTypeAttEffects = tempPartTypeEffect.getAllAttributeEffects();
								// go through each ParticipantAttributeRuleEffect and write it to the file:
								for(int n=0; n<partTypeAttEffects.size(); n++)
								{
									writer.write(BEGIN_ATTRIBUTE_EFFECT_TAG);
									writer.write(NEWLINE);
									ParticipantAttributeRuleEffect tempPartAttEffect =
										(ParticipantAttributeRuleEffect)partTypeAttEffects.elementAt(n);
									writer.write(tempPartAttEffect.getAttribute().getName()); // attribute name
									writer.write(NEWLINE);
									writer.write(tempPartAttEffect.getEffect());
									writer.write(NEWLINE);
									writer.write(END_ATTRIBUTE_EFFECT_TAG);
									writer.write(NEWLINE);
								}
								writer.write(END_PARTICIPANT_TYPE_EFFECT_TAG);
								writer.write(NEWLINE);
							}
							writer.write(END_PARTICIPANT_EFFECT_TAG);
							writer.write(NEWLINE);
						}

						//  rule input:
						Vector inputs = ((EffectRule)tempRule).getAllRuleInputs();
						// go through each rule input and write it to the file:
						for(int k=0; k<inputs.size(); k++)
						{
							writer.write(BEGIN_RULE_INPUT_TAG);
							writer.write(NEWLINE);
							RuleInput tempInput = (RuleInput)inputs.elementAt(k);
							writer.write(tempInput.getName()); // input name
							writer.write(NEWLINE);
							writer.write(tempInput.getType()); // input type
							writer.write(NEWLINE);
							writer.write(tempInput.getPrompt()); // input prompt
							writer.write(NEWLINE);
							writer.write((new Boolean(tempInput.isCancelable())).toString()); // cancelable
							writer.write(NEWLINE);
							// input condition:
							writer.write(BEGIN_RULE_INPUT_CONDITION_TAG);
							writer.write(NEWLINE);
							writer.write(tempInput.getCondition().getGuard()); // input condition guard
							writer.write(NEWLINE);
							if(tempInput.getCondition().isConstrained()) // input has a constraining/condition value
							{
								writer.write(tempInput.getCondition().getValue().toString()); // input condition value
							}
							else // unconstrained
							{
								writer.write(EMPTY_VALUE);
							}
							writer.write(NEWLINE);
							writer.write(END_RULE_INPUT_CONDITION_TAG);
							writer.write(NEWLINE);
							writer.write(END_RULE_INPUT_TAG);
							writer.write(NEWLINE);
						}
						writer.write(END_EFFECT_RULE_TAG);
						writer.write(NEWLINE);
					}
					// create objects rule:
					else if(tempRule instanceof CreateObjectsRule)
					{
						writer.write(BEGIN_CREATE_OBJECTS_RULE_TAG);
						writer.write(NEWLINE);
						writer.write(tempRule.getName());
						writer.write(NEWLINE);
						writer.write((new Integer(tempRule.getPriority())).toString()); // rule priority
						writer.write(NEWLINE);
						writer.write(tempAct.getName()); // action name
						writer.write(NEWLINE);
						writer.write((new Integer(tempRule.getTiming())).toString()); // rule timing
						writer.write(NEWLINE);
						Vector ruleObjs = ((CreateObjectsRule)tempRule).getAllSimSEObjects();
						// go through each object that this rule creates and write it to the file:
						for(int k=0; k<ruleObjs.size(); k++)
						{
							writer.write(BEGIN_OBJECT_TAG);
							writer.write(NEWLINE);
							SimSEObject tempObj = (SimSEObject)ruleObjs.elementAt(k);
							writer.write((new Integer(tempObj.getSimSEObjectType().getType())).toString()); // SimSEObjectTypeType
							writer.write(NEWLINE);
							writer.write(tempObj.getSimSEObjectType().getName()); // SimSEObjectType name
							writer.write(NEWLINE);
							Vector atts = tempObj.getAllAttributes();
							// go through each instantiated attribute of this object and write it to the file:
							for(int m=0; m<atts.size(); m++)
							{
								writer.write(BEGIN_INSTANTIATED_ATTRIBUTE_TAG);
								writer.write(NEWLINE);
								InstantiatedAttribute tempInstAtt = (InstantiatedAttribute)atts.elementAt(m);
								writer.write(tempInstAtt.getAttribute().getName()); // attribute name
								writer.write(NEWLINE);
								if(tempInstAtt.isInstantiated())
								{
									writer.write(tempInstAtt.getValue().toString());
								}
								else // not instantiated (this case should never happen, but it's included just in case)
								{
									writer.write(EMPTY_VALUE);
								}
								writer.write(NEWLINE);
								writer.write(END_INSTANTIATED_ATTRIBUTE_TAG);
								writer.write(NEWLINE);
							}
							writer.write(END_OBJECT_TAG);
							writer.write(NEWLINE);
						}
						writer.write(END_CREATE_OBJECTS_RULE_TAG);
						writer.write(NEWLINE);
					}
					// destroy objects rule:
					else if(tempRule instanceof DestroyObjectsRule)
					{
						writer.write(BEGIN_DESTROY_OBJECTS_RULE_TAG);
						writer.write(NEWLINE);
						writer.write(tempRule.getName());
						writer.write(NEWLINE);
						writer.write((new Integer(tempRule.getPriority())).toString()); // rule priority
						writer.write(NEWLINE);
						writer.write(tempAct.getName()); // action name
						writer.write(NEWLINE);
						writer.write((new Integer(tempRule.getTiming())).toString()); // rule timing
						writer.write(NEWLINE);

						// participant conditions:
						Vector partConds = ((DestroyObjectsRule)tempRule).getAllParticipantConditions();
						// go through each participant condition and write it to the file:
						for(int m=0; m<partConds.size(); m++)
						{
							writer.write(BEGIN_PARTICIPANT_CONDITION_TAG);
							writer.write(NEWLINE);
							DestroyObjectsRuleParticipantCondition tempPartCond = (DestroyObjectsRuleParticipantCondition)(partConds.elementAt(m));
							writer.write(tempPartCond.getParticipant().getName()); // participant name
							writer.write(NEWLINE);
							// go through each ActionTypeParticipantConstraint and write it to the file:
							Vector partConstraints = tempPartCond.getAllConstraints();
							for(int n=0; n<partConstraints.size(); n++)
							{
								ActionTypeParticipantConstraint tempPartConst = (ActionTypeParticipantConstraint)(partConstraints.elementAt(n));
								writer.write(BEGIN_PARTICIPANT_CONSTRAINT_TAG);
								writer.write(NEWLINE);
								writer.write(tempPartConst.getSimSEObjectType().getName()); // name of the SimSEObject type for this participant
								// constraint
								writer.write(NEWLINE);
								// go through each ActionTypeParticipantAttributeConstraint and write it to the file:
								ActionTypeParticipantAttributeConstraint[] attConstraints = tempPartConst.getAllAttributeConstraints();
								for(int p=0; p<attConstraints.length; p++)
								{
									ActionTypeParticipantAttributeConstraint tempAttConst = attConstraints[p];
									writer.write(BEGIN_ATTRIBUTE_CONSTRAINT_TAG);
									writer.write(NEWLINE);
									writer.write(tempAttConst.getAttribute().getName()); // attribute name
									writer.write(NEWLINE);
									writer.write(tempAttConst.getGuard()); // guard
									writer.write(NEWLINE);
									if(tempAttConst.isConstrained()) // has a value
									{
										writer.write(tempAttConst.getValue().toString()); // value
									}
									else // is unconstrained by a value
									{
										writer.write(EMPTY_VALUE);
									}
									writer.write(NEWLINE);
									writer.write(END_ATTRIBUTE_CONSTRAINT_TAG);
									writer.write(NEWLINE);
								}
								writer.write(END_PARTICIPANT_CONSTRAINT_TAG);
								writer.write(NEWLINE);
							}
							writer.write(END_PARTICIPANT_CONDITION_TAG);
							writer.write(NEWLINE);
						}
						writer.write(END_DESTROY_OBJECTS_RULE_TAG);
						writer.write(NEWLINE);
					}
				}
			}
			writer.write(END_RULES_TAG);
			writer.write(NEWLINE);


			//***************GRAPHICS******************
			writer.write(BEGIN_GRAPHICS_TAG);
			writer.write(NEWLINE);

			// icons directory:
			writer.write(BEGIN_ICONS_DIR_TAG);
			writer.write(NEWLINE);
			if((iconDirectory != null) && (iconDirectory.getPath().length() > 0)) // has icon dir
			{
				writer.write(iconDirectory.getPath());
			}
			else
			{
				writer.write("");
			}
			writer.write(NEWLINE);
			writer.write(END_ICONS_DIR_TAG);
			writer.write(NEWLINE);

			// start state objects:
			for(int i=0; i<objs.size(); i++)
			{
				SimSEObject obj = (SimSEObject)objs.elementAt(i);
				// SimSEObjectTypeType:
				writer.write(SimSEObjectTypeTypes.getText(obj.getSimSEObjectType().getType()));
				writer.write(NEWLINE);
				// SimSEObjectType:
				writer.write(obj.getSimSEObjectType().getName());
				writer.write(NEWLINE);
				// key att value:
				if(obj.getSimSEObjectType().hasKey() == false) // doesn't have a key attribute
				{
					writer.write("");
					writer.write(NEWLINE);
				}
				else // has a key attribute
				{
					if(obj.getKey().isInstantiated() == false) // doesn't have a key attribute value
					{
						writer.write("");
						writer.write(NEWLINE);
					}
					else // has a key attribute value
					{
						writer.write(obj.getKey().getValue().toString());
						writer.write(NEWLINE);
					}
				}
				if(((String)startStateObjsToImages.get(obj) != null) && (((String)startStateObjsToImages.get(obj)).length() > 0)) // has an image
				{
					writer.write((String)startStateObjsToImages.get(obj));
					writer.write(NEWLINE);
				}
				else
				{
					writer.write("");
					writer.write(NEWLINE);
				}
				writer.write(NEWLINE);
			}

			// create objects rules objects:
			for(int i=0; i<actions.size(); i++)
			{
				ActionType act = (ActionType)actions.elementAt(i);
				Vector rules = act.getAllCreateObjectsRules();
				for(int j=0; j<rules.size(); j++)
				{
					CreateObjectsRule rule = (CreateObjectsRule)rules.elementAt(j);
					Vector ruleObjs = rule.getAllSimSEObjects();
					for(int k=0; k<ruleObjs.size(); k++)
					{
						SimSEObject obj = (SimSEObject)ruleObjs.elementAt(k);
						// SimSEObjectTypeType:
						writer.write(SimSEObjectTypeTypes.getText(obj.getSimSEObjectType().getType()));
						writer.write(NEWLINE);
						// SimSEObjectType:
						writer.write(obj.getSimSEObjectType().getName());
						writer.write(NEWLINE);
						// key att value:
						if(obj.getSimSEObjectType().hasKey() == false) // doesn't have a key attribute
						{
							writer.write("");
							writer.write(NEWLINE);
						}
						else // has a key attribute
						{
							if(obj.getKey().isInstantiated() == false) // doesn't have a key attribute value
							{
								writer.write("");
								writer.write(NEWLINE);
							}
							else // has a key attribute value
							{
								writer.write(obj.getKey().getValue().toString());
								writer.write(NEWLINE);
							}
						}
						if(((String)ruleObjsToImages.get(obj) != null) && (((String)ruleObjsToImages.get(obj)).length() > 0)) // has an image
						{
							writer.write((String)ruleObjsToImages.get(obj));
							writer.write(NEWLINE);
						}
						else
						{
							writer.write("");
							writer.write(NEWLINE);
						}
						writer.write(NEWLINE);
					}
				}
			}

			writer.write(END_GRAPHICS_TAG);
			writer.write(NEWLINE);


			//***************MAP************************
			writer.write(BEGIN_MAP_TAG);
			writer.write(NEWLINE);
			writer.write(BEGIN_SOP_USERS_TAG);
			writer.write(NEWLINE);
			// write sop objects to file
			for (int i = 0; i < sopUsers.size(); i++)
			{
				UserData tmp = (UserData)sopUsers.get(i);
				writer.write(tmp.getName());
				writer.write(NEWLINE);
				writer.write("" + tmp.isDisplayed());
				writer.write(NEWLINE);
				writer.write("" + tmp.isActivated());
				writer.write(NEWLINE);
				writer.write("" + tmp.getXLocation());
				writer.write(NEWLINE);
				writer.write("" + tmp.getYLocation());
				writer.write(NEWLINE);
				writer.write("");
				writer.write(NEWLINE);
			}
			writer.write(END_SOP_USERS_TAG);
			writer.write(NEWLINE);

			// write map to file
			for (int i = 0; i < MapData.Y_MAPSIZE; i++)
			{
				for (int j = 0; j < MapData.X_MAPSIZE; j++)
				{
					writer.write("" + mapRep[j][i].getBaseKey());
					writer.write(NEWLINE);
					writer.write("" + mapRep[j][i].getFringeKey());
					writer.write(NEWLINE);
				}
			}
			writer.write(END_MAP_TAG);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file: " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
}
