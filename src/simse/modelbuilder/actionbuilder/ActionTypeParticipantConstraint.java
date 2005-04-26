/* This class defines a constraint on an action type participant */

package simse.modelbuilder.actionbuilder;

import simse.modelbuilder.objectbuilder.*;
import java.util.*;

public class ActionTypeParticipantConstraint implements Cloneable
{
	SimSEObjectType objType; // pointer to the SimSEObjectType that this constraint is on
	ActionTypeParticipantAttributeConstraint[] constraints; // constraints on this SimSEObjectType's attributes

	public ActionTypeParticipantConstraint(SimSEObjectType type)
	{
		objType = type;
		Vector atts = type.getAllAttributes();
		constraints = new ActionTypeParticipantAttributeConstraint[atts.size()];
		for(int i=0; i<constraints.length; i++)
		{
			constraints[i] = new ActionTypeParticipantAttributeConstraint((Attribute)(atts.elementAt(i)));
		}
	}


	public Object clone()
	{
		try
		{
			ActionTypeParticipantConstraint cl = (ActionTypeParticipantConstraint)(super.clone());
			cl.objType = objType; // NOTE: since this is a pointer to the object type, it must remain a pointer to the
				// object type, even in the clone.  So BE CAREFUL!!
			ActionTypeParticipantAttributeConstraint[] clonedConsts = new ActionTypeParticipantAttributeConstraint[constraints.length];
			for(int i=0; i<constraints.length; i++)
			{
				clonedConsts[i] = (ActionTypeParticipantAttributeConstraint)(constraints[i].clone());
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


	public SimSEObjectType getSimSEObjectType() // returns a COPY of the SimSEObjectType
	{
		return (SimSEObjectType)objType.clone();
	}


	public ActionTypeParticipantAttributeConstraint[] getAllAttributeConstraints()
	{
		return constraints;
	}


	public void setAttributeConstraints(ActionTypeParticipantAttributeConstraint[] newConstraints)
	{
		constraints = newConstraints;
	}


	public void addAttributeConstraint(ActionTypeParticipantAttributeConstraint newConstraint) // replaces existing constraint for the
		// specified attribute with the new one
	{
		for(int i=0; i<constraints.length; i++)
		{
			if(constraints[i].getAttribute().getName().equals(newConstraint.getAttribute().getName()))
			{
				constraints[i] = newConstraint;
			}
		}
	}


	public ActionTypeParticipantAttributeConstraint getAttributeConstraint(String name) // returns the constraint on the attribute with
		// the specified name
	{
		for(int i=0; i<constraints.length; i++)
		{
			if(constraints[i].getAttribute().getName().equals(name))
			{
				return constraints[i];
			}
		}
		return null;
	}



}
