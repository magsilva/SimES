/* This class defines a rule for an action type */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.actionbuilder.*;
import java.util.*;

public abstract class Rule implements Cloneable
{
	private String name; // name of the rule
	private int timing; // timing of the rule (defined by RuleTiming class)
	private int priority; // priority of this rule for execution
	private ActionType actType; // POINTER TO action type that this rule is attached to

	// rule type constants:
	public static final String CREATE_OBJECTS_RULE = "Create objects Rule";
	public static final String DESTROY_OBJECTS_RULE = "Destroy objects Rule";
	public static final String EFFECT_RULE = "Effect Rule";

	public Rule(String n, ActionType act)
	{
		name = n;
		timing = RuleTiming.CONTINUOUS;
		priority = -1;
		actType = act;
	}


	public Object clone()
	{
		try
		{
			Rule cl = (Rule)(super.clone());
			cl.name = name;
			cl.timing = timing;
			cl.priority = priority;
			cl.actType = actType; // NOTE: since this is a pointer to the action type, it must remain a pointer to the
				// action type, even in the clone.  So BE CAREFUL!!
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
	
	
	public int getTiming()
	{
		return timing;
	}


	public int getPriority()
	{
		return priority;
	}
	
	
	public void setTiming(int newTiming)
	{
		timing = newTiming;
	}
	

	public void setPriority(int newPri)
	{
		priority = newPri;
	}
	
	
	public ActionType getActionType() // returns a COPY of the action type
	{
		return (ActionType)actType.clone();
	}
}
