/* This class defines an effect of an EffectRule on a participant */

package simse.modelbuilder.rulebuilder;

import java.util.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.objectbuilder.*;

public class ParticipantRuleEffect implements Cloneable
{
	private ActionTypeParticipant participant; // pointer to the participant for this effect
	private Vector partTypeEffects; // Vector of ParticipantTypeRuleEffects for each SimSEObject type that this participant can be

	public ParticipantRuleEffect(ActionTypeParticipant part)
	{
		participant = part;
		// initialize a ParticipantTypeRuleEffect for each SimSEObject type:
		partTypeEffects = new Vector();
		Vector types = participant.getAllSimSEObjectTypes();
		for(int i=0; i<types.size(); i++)
		{
			partTypeEffects.add(new ParticipantTypeRuleEffect((SimSEObjectType)types.elementAt(i)));
		}
	}


	public Object clone()
	{
		try
		{
			ParticipantRuleEffect cl = (ParticipantRuleEffect)(super.clone());
			// participant:
			cl.participant = participant; // NOTE: since this is a pointer to the participant, it must remain a pointer to the
					// participant, even in the clone.  So BE CAREFUL!!
	
			// clone participant type rule effects:
			Vector clonedEffects = new Vector();
			for(int i=0; i<partTypeEffects.size(); i++)
			{
				clonedEffects.add((ParticipantTypeRuleEffect)(((ParticipantTypeRuleEffect)(partTypeEffects.elementAt(i))).clone()));
			}
			cl.partTypeEffects = clonedEffects;
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
	
	
	public Vector getAllParticipantTypeEffects()
	{
		return partTypeEffects;
	}
	
	
	public ParticipantTypeRuleEffect getParticipantTypeEffect(SimSEObjectType type) // returns the effect for the specified SimSEObjectType
	{
		for(int i=0; i<partTypeEffects.size(); i++)
		{
			ParticipantTypeRuleEffect tempEffect = (ParticipantTypeRuleEffect)(partTypeEffects.elementAt(i));
			if(tempEffect.getSimSEObjectType() == type)
			{
				return tempEffect;
			}
		}
		return null;
	}


	public ParticipantTypeRuleEffect getParticipantTypeRuleEffect(String typeName) // note to self:
		// it's okay to just give the type name and not the SimSEObjectTypeType because all SimSEObjectTypes in one participant MUST be of
		// the same SimSEObjectTypeType! Think about it!
	{
		for(int i=0; i<partTypeEffects.size(); i++)
		{
			ParticipantTypeRuleEffect tempEffect = (ParticipantTypeRuleEffect)(partTypeEffects.elementAt(i));
			if(tempEffect.getSimSEObjectType().getName().equals(typeName))
			{
				return tempEffect;
			}
		}
		return null;
	}
	
	
	public void addParticipantTypeRuleEffect(ParticipantTypeRuleEffect newEffect) // adds the specified effect to the participant effect;
		// if an effect for its SimSEObjectType is already there, the new effect replaces it.
	{
		boolean notFound = true;
		for(int i=0; i<partTypeEffects.size(); i++)
		{
			ParticipantTypeRuleEffect tempEffect = (ParticipantTypeRuleEffect)(partTypeEffects.elementAt(i));
			if(tempEffect.getSimSEObjectType() == newEffect.getSimSEObjectType())
			{
				partTypeEffects.setElementAt(newEffect, i);
				notFound = false;
			}
		}
		if(notFound) // new effect, not a replacement for a previous one
		{
			partTypeEffects.add(newEffect);
		}
	}
	
	
	public void addParticipantTypeRuleEffect(ParticipantTypeRuleEffect newEffect, int position) // adds the new effect in the specified
		// position
	{
		partTypeEffects.add(position, newEffect);
	}
	
	
	public void removeParticipantTypeRuleEffect(SimSEObjectType type) // removes the ParticipantTypeRuleEffect for this SimSEObjectType
	{
		for(int i=0; i<partTypeEffects.size(); i++)
		{
			ParticipantTypeRuleEffect tempEffect = (ParticipantTypeRuleEffect)(partTypeEffects.elementAt(i));
			if(tempEffect.getSimSEObjectType() == type)
			{
				partTypeEffects.removeElementAt(i);
			}
		}
	}


	public void removeParticipantTypeRuleEffect(String typeName) // removes the ParticipantTypeRuleEffect for the
		// SimSEObjectType with the specified name and type; note to self:
		// it's okay to just give the type name and not the SimSEObjectTypeType because all SimSEObjectTypes in one participant MUST be of
		// the same SimSEObjectTypeType! Think about it!
	{
		for(int i=0; i<partTypeEffects.size(); i++)
		{
			ParticipantTypeRuleEffect tempEffect = (ParticipantTypeRuleEffect)(partTypeEffects.elementAt(i));
			if(tempEffect.getSimSEObjectType().getName().equals(typeName))
			{
				partTypeEffects.removeElementAt(i);
			}
		}
	}
}

