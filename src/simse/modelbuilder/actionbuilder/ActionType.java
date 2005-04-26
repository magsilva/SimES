/* This class defines an action type */

package simse.modelbuilder.actionbuilder;

import java.util.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.rulebuilder.*;

public class ActionType implements Cloneable
{
	private String name;
	private boolean visible; // whether or not the existence of this action should be visible in the user interface of the simulation
	private String description; // description of this action, to be shown in the user interface of the simulation (if visible)
	private Vector participants; // all of the ActionTypeParticipants involved in this action
	private Vector triggers; // a Vector of ActionTypeTriggers for this ActionType
	private Vector destroyers; // a Vector ActionTypeDestroyers for this ActionType
	private Vector rules; // all of the Rules associated with this ActionType

	public ActionType(String n)
	{
		name = n;
		visible = false;
		description = null;
		participants = new Vector();
		triggers = new Vector();
		destroyers = new Vector();
		rules = new Vector();
	}


	public Object clone()
	{
		try
		{
			ActionType cl = (ActionType)(super.clone());
			cl.name = name;
			cl.visible = visible;
			cl.description = description;
			Vector clonedTrigs = new Vector();
			for(int i=0; i<triggers.size(); i++)
			{
				clonedTrigs.add((ActionTypeTrigger)(((ActionTypeTrigger)(triggers.elementAt(i))).clone()));
			}
			cl.triggers = clonedTrigs;
			Vector clonedDests = new Vector();
			for(int i=0; i<destroyers.size(); i++)
			{
				clonedDests.add((ActionTypeDestroyer)(((ActionTypeDestroyer)(destroyers.elementAt(i))).clone()));
			}
			cl.destroyers = clonedDests;			
			Vector clonedParts = new Vector();
			for(int i=0; i<participants.size(); i++)
			{
				clonedParts.add((ActionTypeParticipant)(((ActionTypeParticipant)(participants.elementAt(i))).clone()));
			}
			cl.participants = clonedParts;
			Vector clonedRules = new Vector();
			for(int i=0; i<rules.size(); i++)
			{
				clonedRules.add((Rule)(((Rule)(rules.elementAt(i))).clone()));
			}
			cl.rules = clonedRules;

			return cl;
		}
		catch(CloneNotSupportedException c)
		{
			System.out.println(c.getMessage());
		}
		return null;
	}


	public String getName()
	{
		return name;
	}
	
	
	public boolean isVisible()
	{
		return visible;
	}
	
	
	public void setVisibility(boolean newVis)
	{
		visible = newVis;
	}
	
	
	public String getDescription()
	{
		return description;
	}
	
	
	public void setDescription(String s)
	{
		description = s;
	}


	public Vector getAllParticipants() // returns a vector of all ActionTypeParticipants involved in this action type
	{
		return participants;
	}


	public Vector getAllTriggers() // returns a vector of all ActionTypeTriggers for this action type
	{
		return triggers;
	}
	

	public ActionTypeTrigger getTrigger(String name)
	{
		for(int i=0; i<triggers.size(); i++)
		{
			ActionTypeTrigger trigger = (ActionTypeTrigger)triggers.elementAt(i);
			if(trigger.getName().equals(name))
			{
				return trigger;
			}
		}
		return null;
	}


	public Vector getAllDestroyers() // returns a vector of all ActionTypeDestroyers for this action type
	{
		return destroyers;
	}
	

	public ActionTypeDestroyer getDestroyer(String name)
	{
		for(int i=0; i<destroyers.size(); i++)
		{
			ActionTypeDestroyer destroyer = (ActionTypeDestroyer)destroyers.elementAt(i);
			if(destroyer.getName().equals(name))
			{
				return destroyer;
			}
		}
		return null;
	}


	public Vector getAllRules()
	{
		return rules;
	}


	public Vector getAllCreateObjectsRules() // returns all rules of type CreateObjectsRule
	{
		Vector coRules = new Vector();
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule instanceof CreateObjectsRule)
			{
				coRules.add(tempRule);
			}
		}
		return coRules;
	}
	
	
	public Vector getAllDestroyObjectsRules() // returns all rules of type DestroyObjectsRule
	{
		Vector doRules = new Vector();
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule instanceof DestroyObjectsRule)
			{
				doRules.add(tempRule);
			}
		}
		return doRules;
	}


	public Vector getAllEffectRules() // returns all rules of type EffectRule
	{
		Vector eRules = new Vector();
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule instanceof EffectRule)
			{
				eRules.add(tempRule);
			}
		}
		return eRules;
	}


	public Rule getRule(String name) // returns the rule with the specified name
	{
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getName().equals(name))
			{
				return tempRule;
			}
		}
		return null;
	}


	public void addRule(Rule newRule) // adds this rule to the action type; if there already exists one with its name, it replaces it
	{
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getName().equals(newRule.getName()))
			{
				rules.removeElement(tempRule);
			}
		}
		rules.addElement(newRule);
	}


	public void addRule(Rule newRule, int index) // adds this rule to the action type at the specified index; if there already exists
		// one with its name, it replaces it
	{
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getName().equals(newRule.getName()))
			{
				rules.removeElement(tempRule);
			}
		}
		rules.add(index, newRule);
	}


	public void removeRule(String name) // removes the rule with the specified name from the action type
	{
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getName().equals(name))
			{
				rules.removeElement(tempRule);
			}
		}
	}


	public void addTrigger(ActionTypeTrigger newTrigger) // adds this trigger to the action type; if there already exists one with its name,
		// it replaces it
	{
		for(int i=0; i<triggers.size(); i++)
		{
			ActionTypeTrigger tempTrig = (ActionTypeTrigger)triggers.elementAt(i);
			if(tempTrig.getName().equals(newTrigger.getName()))
			{
				triggers.removeElement(tempTrig);
			}
		}
		triggers.addElement(newTrigger);
	}
	
	
	public void addTrigger(ActionTypeTrigger newTrigger, int index) // adds this trigger to the action type at the specified index; if
		// there already exists one with its name, it replaces it
	{
		for(int i=0; i<triggers.size(); i++)
		{
			ActionTypeTrigger tempTrig = (ActionTypeTrigger)triggers.elementAt(i);
			if(tempTrig.getName().equals(newTrigger.getName()))
			{
				triggers.removeElement(tempTrig);
			}
		}
		triggers.add(index, newTrigger);
	}	
	
	
	public int removeTrigger(String name) // removes the trigger with the specified name from the action type, returns its index
	{
		for(int i=0; i<triggers.size(); i++)
		{
			ActionTypeTrigger tempTrig = (ActionTypeTrigger)triggers.elementAt(i);
			if(tempTrig.getName().equals(name))
			{
				triggers.removeElement(tempTrig);
				return i;
			}
		}
		return - 1;
	}	
	
	
	public void setTriggers(Vector newTrigs) // replaces all existing triggers with the triggers being passed in in the vector
	{
		triggers = newTrigs;
	}


	public void addDestroyer(ActionTypeDestroyer newDestroyer) // adds this destroyer to the action type; if there already exists one with 
		// its name, it replaces it
	{
		for(int i=0; i<destroyers.size(); i++)
		{
			ActionTypeDestroyer tempDest = (ActionTypeDestroyer)destroyers.elementAt(i);
			if(tempDest.getName().equals(newDestroyer.getName()))
			{
				destroyers.removeElement(tempDest);
			}
		}
		destroyers.addElement(newDestroyer);
	}
	
	
	public void addDestroyer(ActionTypeDestroyer newDestroyer, int index) // adds this destroyer to the action type at the specified index; 
		// if there already exists one with its name, it replaces it
	{
		for(int i=0; i<destroyers.size(); i++)
		{
			ActionTypeDestroyer tempDest = (ActionTypeDestroyer)destroyers.elementAt(i);
			if(tempDest.getName().equals(newDestroyer.getName()))
			{
				destroyers.removeElement(tempDest);
			}
		}
		destroyers.add(index, newDestroyer);
	}		
	
	
	public int removeDestroyer(String name) // removes the destroyer with the specified name from the action type, returns its index
	{
		for(int i=0; i<destroyers.size(); i++)
		{
			ActionTypeDestroyer tempDest = (ActionTypeDestroyer)destroyers.elementAt(i);
			if(tempDest.getName().equals(name))
			{
				destroyers.removeElement(tempDest);
				return i;
			}
		}
		return - 1;
	}
	
	
	public void setDestroyers(Vector newDests) // replaces all existing destroyers with the destroyers being passed in in the vector
	{
		destroyers = newDests;
	}	


	public ActionTypeParticipant getParticipant(String name) // returns the participant with the specified name
	{
		for(int i=0; i<participants.size(); i++)
		{
			ActionTypeParticipant tempPart = (ActionTypeParticipant)(participants.elementAt(i));
			if(tempPart.getName().equals(name))
			{
				return tempPart;
			}
		}
		return null;
	}


	public void addParticipant(ActionTypeParticipant part)
	{
		participants.add(part);
	}


	public void addParticipant(ActionTypeParticipant part, int index) // adds the participant at the specified position
	{
		participants.add(index, part);
	}


	public void removeParticipant(String name) // removes the participant with the specified name
	{
		for(int i=0; i<participants.size(); i++)
		{
			ActionTypeParticipant tempPart = (ActionTypeParticipant)(participants.elementAt(i));
			if(tempPart.getName().equals(name))
			{
				participants.removeElementAt(i);
				
				// remove participant triggers for this participant:
				for(int j=0; j<triggers.size(); j++)
				{
					ActionTypeTrigger tempTrig = (ActionTypeTrigger)triggers.elementAt(j);
					tempTrig.removeTrigger(tempPart.getName());
				}
				
				// remove participant destroyer for this participant:
				for(int j=0; j<destroyers.size(); j++)
				{
					ActionTypeDestroyer tempDest = (ActionTypeDestroyer)destroyers.elementAt(j);
					tempDest.removeDestroyer(tempPart.getName());
				}
			}
		}			
	}


	public void removeParticipant(ActionTypeParticipant part)
	{
		participants.remove(part);
		// remove participant triggers for this participant:
		for(int j=0; j<triggers.size(); j++)
		{
			ActionTypeTrigger tempTrig = (ActionTypeTrigger)triggers.elementAt(j);
			tempTrig.removeTrigger(part.getName());
		}
		
		// remove participant destroyer for this participant:
		for(int j=0; j<destroyers.size(); j++)
		{
			ActionTypeDestroyer tempDest = (ActionTypeDestroyer)destroyers.elementAt(j);
			tempDest.removeDestroyer(part.getName());
		}		
	}
	
	
	public boolean hasTriggerRules() // returns true if this action has at least one trigger rule, false otherwise
	{
		// go through all rules:
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getTiming() == RuleTiming.TRIGGER)
			{
				return true;
			}
		}
		return false;
	}
	
	
	public Vector getAllTriggerRules() // returns them in prioritized order, from first to execute to last to execute
	{
		// initialize lists:
		Vector nonPrioritizedRules = new Vector();
		Vector prioritizedRules = new Vector();
		// go through each rule and add the trigger ones to the list:
		for(int j=0; j<rules.size(); j++)
		{
			Rule tempRule = (Rule)rules.elementAt(j);
			if(tempRule.getTiming() == RuleTiming.TRIGGER)
			{
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
		// add all of the non-prioritized rules to the end of the prioritized rules vector (not using addAll() because I'm not sure it'll
		// maintain the order):
		for(int i=0; i<nonPrioritizedRules.size(); i++)
		{
			prioritizedRules.add(nonPrioritizedRules.elementAt(i));
		}
		return prioritizedRules;
	}
	
	
	public boolean hasDestroyerRules() // returns true if this action has at least one destroyer rule, false otherwise
	{
		// go through all rules:
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getTiming() == RuleTiming.DESTROYER)
			{
				return true;
			}
		}
		return false;
	}
	
	
	public Vector getAllDestroyerRules() // returns them in prioritized order, from first to execute to last to execute
	{
		// initialize lists:
		Vector nonPrioritizedRules = new Vector();
		Vector prioritizedRules = new Vector();
		// go through each rule and add the trigger ones to the list:
		for(int j=0; j<rules.size(); j++)
		{
			Rule tempRule = (Rule)rules.elementAt(j);
			if(tempRule.getTiming() == RuleTiming.DESTROYER)
			{
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
		// add all of the non-prioritized rules to the end of the prioritized rules vector (not using addAll() because I'm not sure it'll
		// maintain the order):
		for(int i=0; i<nonPrioritizedRules.size(); i++)
		{
			prioritizedRules.add(nonPrioritizedRules.elementAt(i));
		}
		return prioritizedRules;
	}	
	
	
	public boolean hasContinuousRules() // returns true if this action has at least one continuous rule, false otherwise
	{
		// go through all rules:
		for(int i=0; i<rules.size(); i++)
		{
			Rule tempRule = (Rule)rules.elementAt(i);
			if(tempRule.getTiming() == RuleTiming.CONTINUOUS)
			{
				return true;
			}
		}
		return false;
	}	
	
	
	public Vector getAllContinuousRules() // returns them in prioritized order, from first to execute to last to execute
	{
		// initialize lists:
		Vector nonPrioritizedRules = new Vector();
		Vector prioritizedRules = new Vector();
		// go through each rule and add the trigger ones to the list:
		for(int j=0; j<rules.size(); j++)
		{
			Rule tempRule = (Rule)rules.elementAt(j);
			if(tempRule.getTiming() == RuleTiming.CONTINUOUS)
			{
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
		// add all of the non-prioritized rules to the end of the prioritized rules vector (not using addAll() because I'm not sure it'll
		// maintain the order):
		for(int i=0; i<nonPrioritizedRules.size(); i++)
		{
			prioritizedRules.add(nonPrioritizedRules.elementAt(i));
		}
		return prioritizedRules;
	}	
	
	
	public boolean hasGameEndingTrigger() // returns true if this action has one or more game-ending triggers
	{
		for(int i=0; i<triggers.size(); i++)
		{
			ActionTypeTrigger tempTrig = (ActionTypeTrigger)triggers.elementAt(i);
			if(tempTrig.isGameEndingTrigger())
			{
				return true;
			}
		}
		return false;
	}
	
	
	public boolean hasGameEndingDestroyer() // returns true if this action has one or more game-ending destroyers
	{
		for(int i=0; i<destroyers.size(); i++)
		{
			ActionTypeDestroyer tempDest = (ActionTypeDestroyer)destroyers.elementAt(i);
			if(tempDest.isGameEndingDestroyer())
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void removeAllRules()
	{
		rules.clear();
	}
}
