/* This class defines an "effects" rule -- a rule that has complex effects */

package simse.modelbuilder.rulebuilder;

import java.util.*;
import simse.modelbuilder.actionbuilder.*;

public class EffectRule extends Rule implements Cloneable
{
	private Vector inputs; // Vector of RuleInputs for this rule
	private Vector participantRuleEffects; // Vector of ParticipantRuleEffects for this rule

	public EffectRule(String name, ActionType act)
	{
		super(name, act);
		inputs = new Vector();
		participantRuleEffects = new Vector();
	}


	public Object clone()
	{
		EffectRule cl = (EffectRule)(super.clone());

		// clone inputs:
		Vector clonedInputs = new Vector();
		for(int i=0; i<inputs.size(); i++)
		{
			clonedInputs.add((RuleInput)(((RuleInput)(inputs.elementAt(i))).clone()));
		}
		cl.inputs = clonedInputs;

		// clone rule effects:
		Vector clonedEffects = new Vector();
		for(int i=0; i<participantRuleEffects.size(); i++)
		{
			clonedEffects.add((ParticipantRuleEffect)(((ParticipantRuleEffect)(participantRuleEffects.elementAt(i))).clone()));
		}
		cl.participantRuleEffects = clonedEffects;
		return cl;
	}


	public Vector getAllRuleInputs()
	{
		return inputs;
	}


	public RuleInput getRuleInput(String name) // returns the rule input with the specified name
	{
		for(int i=0; i<inputs.size(); i++)
		{
			RuleInput tempInput = (RuleInput)inputs.elementAt(i);
			if(tempInput.getName().equals(name))
			{
				return tempInput;
			}
		}
		return null;
	}


	public void addRuleInput(RuleInput newInput)
	{
		inputs.add(newInput);
	}


	public void removeRuleInput(String name) // removes the rule input with the specified name
	{
		for(int i=0; i<inputs.size(); i++)
		{
			RuleInput tempInput = (RuleInput)inputs.elementAt(i);
			if(tempInput.getName().equals(name))
			{
				inputs.remove(tempInput);
			}
		}
	}


	public void removeRuleInput(RuleInput i)
	{
		inputs.remove(i);
	}


	public void setRuleInputs(Vector ruleInputs)
	{
		inputs = ruleInputs;
	}


	public Vector getAllParticipantRuleEffects()
	{
		return participantRuleEffects;
	}


	public ParticipantRuleEffect getParticipantRuleEffect(String partName) // returns the ParticipantRuleEffect with the participant
		// with the specified name
	{
		for(int i=0; i<participantRuleEffects.size(); i++)
		{
			ParticipantRuleEffect tempEffect = (ParticipantRuleEffect)participantRuleEffects.elementAt(i);
			if(tempEffect.getParticipant().getName().equals(partName))
			{
				return tempEffect;
			}
		}
		return null;
	}


	public void addParticipantRuleEffect(ParticipantRuleEffect newEffect)
	{
		participantRuleEffects.add(newEffect);
	}


	public void removeParticipantRuleEffect(String partName) // removes the ParticipantRuleEffect for the participant with the
		// specified name
	{
		for(int i=0; i<participantRuleEffects.size(); i++)
		{
			ParticipantRuleEffect tempEffect = (ParticipantRuleEffect)participantRuleEffects.elementAt(i);
			if(tempEffect.getParticipant().getName().equals(partName))
			{
				participantRuleEffects.remove(tempEffect);
			}
		}
	}
	
	
	public boolean hasParticipantRuleEffect(String partName) // returns true if this effect rule has a participant rule effect for the
		// participant with the specified name
	{
		for(int i=0; i<participantRuleEffects.size(); i++)
		{
			ParticipantRuleEffect tempEffect = (ParticipantRuleEffect)participantRuleEffects.elementAt(i);
			if(tempEffect.getParticipant().getName().equals(partName))
			{
				return true;
			}
		}
		return false;
	}

	public void removeParticipantRuleEffect(ParticipantRuleEffect e)
	{
		participantRuleEffects.remove(e);
	}


	public void setParticipantRuleEffects(Vector effects)
	{
		participantRuleEffects = effects;
	}
}

