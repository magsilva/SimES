/* This class defines a data structure for holding all of the action types that have been defined */

package simse.modelbuilder.actionbuilder;

import java.util.*;


public class DefinedActionTypes
{
	Vector actions; // vector of ActionTypes that holds defined action types

	public DefinedActionTypes()
	{
		actions = new Vector();
	}


	public Vector getAllActionTypes()
	{
		return actions;
	}


	public ActionType getActionType(String actionName) // returns the action type with the specified name
	{
		for(int i=0; i<actions.size(); i++)
		{
			ActionType tempAct = (ActionType)(actions.elementAt(i));
			if(tempAct.getName().equals(actionName))
			{
				return tempAct;
			}
		}
		return null;
	}


	public void addActionType(ActionType act) // adds the action type to the data structure
	{
		actions.add(act);
	}


	public void removeActionType(ActionType act) // removes the specified action type from the data structure
	{
		actions.remove(act);
	}


	public void removeActionType(String name) // removes the action with the specified name from the data structure
	{
		for(int i=0; i<actions.size(); i++)
		{
			ActionType tempAct = (ActionType)(actions.elementAt(i));
			if(tempAct.getName().equals(name))
			{
				actions.removeElementAt(i);
			}
		}
	}
	
	
	public void clearAll() // removes all existing action types from the data structure
	{
		actions.removeAllElements();
	}
	
	
	public void removeAllRules() // removes all rules from all action types
	{
		for(int i=0; i<actions.size(); i++)
		{
			ActionType tempAct = (ActionType)actions.elementAt(i);
			tempAct.removeAllRules();
		}
	}
}
