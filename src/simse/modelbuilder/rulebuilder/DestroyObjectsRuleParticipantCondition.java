/* This class defines a condition for an action type participant */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import java.util.*;

public class DestroyObjectsRuleParticipantCondition implements Cloneable
{
	private ActionTypeParticipant participant; // participant
	private Vector constraints; // vector of ActionTypeParticipantConstraints for this participant

	public DestroyObjectsRuleParticipantCondition(ActionTypeParticipant part)
	{
		participant = part;
		constraints = new Vector();
		// add an empty constraint for each participant type:
		Vector types = participant.getAllSimSEObjectTypes();
		for(int i=0; i<types.size(); i++)
		{
			addEmptyConstraint((SimSEObjectType)types.elementAt(i));
		}
	}


	public Object clone()
	{
		try
		{
			DestroyObjectsRuleParticipantCondition cl = (DestroyObjectsRuleParticipantCondition)(super.clone());
			cl.participant = participant; // NOTE: since this is a pointer to the participant, it must remain a pointer to the
				// participant, even in the clone.  So BE CAREFUL!!
			Vector clonedConsts = new Vector();
			for(int i=0; i<constraints.size(); i++)
			{
				clonedConsts.add((ActionTypeParticipantConstraint)(((ActionTypeParticipantConstraint)(constraints.elementAt(i))).clone()));
			}
			cl.constraints = clonedConsts;
			return cl;
		}
		catch(CloneNotSupportedException c)
		{
			System.out.println(c.getMessage());
		}
		return null;
	}


	public ActionTypeParticipant getParticipant() // returns a COPY of the participant
	{
		return (ActionTypeParticipant)(participant.clone());
	}


	public void setParticipant(ActionTypeParticipant newPart)
	{
		participant = newPart;
	}


	public Vector getAllConstraints()
	{
		return constraints;
	}


	public ActionTypeParticipantConstraint getConstraint(SimSEObjectType type) // returns the constraint for the specified SimSEObjectType
	{
		for(int i=0; i<constraints.size(); i++)
		{
			ActionTypeParticipantConstraint tempConst = (ActionTypeParticipantConstraint)(constraints.elementAt(i));
			if(tempConst.getSimSEObjectType() == type)
			{
				return tempConst;
			}
		}
		return null;
	}


	public ActionTypeParticipantConstraint getConstraint(String typeName) // note to self: it's okay to just give the type name and not
		// the SimSEObjectTypeType because all SimSEObjectTypes in one participant MUST be of the same SimSEObjectTypeType! Think about it!
	{
		for(int i=0; i<constraints.size(); i++)
		{
			ActionTypeParticipantConstraint tempConst = (ActionTypeParticipantConstraint)(constraints.elementAt(i));
			if(tempConst.getSimSEObjectType().getName().equals(typeName))
			{
				return tempConst;
			}
		}
		return null;
	}


	public void addConstraint(ActionTypeParticipantConstraint newConst) // adds the specified constraint to the participant; if a constraint
		// for its SimSEObjectType is already there, the new constraint replaces it.
	{
		boolean notFound = true;
		for(int i=0; i<constraints.size(); i++)
		{
			ActionTypeParticipantConstraint tempConst = (ActionTypeParticipantConstraint)(constraints.elementAt(i));
			if(tempConst.getSimSEObjectType().getName().equals(newConst.getSimSEObjectType().getName()))
			{
				constraints.setElementAt(newConst, i);
				notFound = false;
				break;
			}
		}
		if(notFound) // new constraint, not a replacement for a previous one
		{
			constraints.add(newConst);
		}
	}


	public void addEmptyConstraint(SimSEObjectType type) // adds a new constraint (of the specified SimSEObjectType) that is
		// unconstrained to this participant; if a constraint with this type is already there, the new constraint replaces it.
	{
		for(int i=0; i<constraints.size(); i++)
		{
			ActionTypeParticipantConstraint tempConst = (ActionTypeParticipantConstraint)(constraints.elementAt(i));
			if(tempConst.getSimSEObjectType().getName().equals(type.getName()))
			{
				constraints.remove(tempConst);
			}
		}
		constraints.add(new ActionTypeParticipantConstraint(type));
	}


	public void removeConstraint(SimSEObjectType type) // removes the constraint for this SimSEObjectType
	{
		for(int i=0; i<constraints.size(); i++)
		{
			ActionTypeParticipantConstraint tempConst = (ActionTypeParticipantConstraint)(constraints.elementAt(i));
			if(tempConst.getSimSEObjectType() == type)
			{
				constraints.removeElementAt(i);
			}
		}
	}


	public void removeConstraint(String typeName) // removes the constraint for the SimSEObjectType with the specified name; note to self:
		// it's okay to just give the type name and not the SimSEObjectTypeType because all SimSEObjectTypes in one participant MUST be of
		// the same SimSEObjectTypeType! Think about it!
	{
		for(int i=0; i<constraints.size(); i++)
		{
			ActionTypeParticipantConstraint tempConst = (ActionTypeParticipantConstraint)(constraints.elementAt(i));
			if(tempConst.getSimSEObjectType().getName().equals(typeName))
			{
				constraints.removeElementAt(i);
			}
		}
	}


	public void setConstraints(Vector newConstraints)
	{
		constraints = newConstraints;
	}
}
