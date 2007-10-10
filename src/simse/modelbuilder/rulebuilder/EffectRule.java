/* This class defines an "effects" rule -- a rule that has complex effects */

package simse.modelbuilder.rulebuilder;

import java.util.*;
import simse.modelbuilder.actionbuilder.*;

public class EffectRule extends Rule implements Cloneable {
  private Vector<RuleInput> inputs; // Vector of RuleInputs for this rule
  private Vector<ParticipantRuleEffect> participantRuleEffects; // Vector of ParticipantRuleEffects for
                                         // this rule

  public EffectRule(String name, ActionType act) {
    super(name, act);
    inputs = new Vector<RuleInput>();
    participantRuleEffects = new Vector<ParticipantRuleEffect>();
  }

  public Object clone() {
    EffectRule cl = (EffectRule) (super.clone());

    // clone inputs:
    Vector<RuleInput> clonedInputs = new Vector<RuleInput>();
    for (int i = 0; i < inputs.size(); i++) {
      clonedInputs.add((RuleInput) (inputs.elementAt(i).clone()));
    }
    cl.inputs = clonedInputs;

    // clone rule effects:
    Vector<ParticipantRuleEffect> clonedEffects = 
    	new Vector<ParticipantRuleEffect>();
    for (int i = 0; i < participantRuleEffects.size(); i++) {
      clonedEffects
          .add((ParticipantRuleEffect) 
          		(participantRuleEffects.elementAt(i).clone()));
    }
    cl.participantRuleEffects = clonedEffects;
    return cl;
  }

  public Vector<RuleInput> getAllRuleInputs() {
    return inputs;
  }

  public RuleInput getRuleInput(String name) // returns the rule input with the
                                             // specified name
  {
    for (int i = 0; i < inputs.size(); i++) {
      RuleInput tempInput = inputs.elementAt(i);
      if (tempInput.getName().equals(name)) {
        return tempInput;
      }
    }
    return null;
  }

  public void addRuleInput(RuleInput newInput) {
    inputs.add(newInput);
  }

  public void removeRuleInput(String name) // removes the rule input with the
                                           // specified name
  {
    for (int i = 0; i < inputs.size(); i++) {
      RuleInput tempInput = inputs.elementAt(i);
      if (tempInput.getName().equals(name)) {
        inputs.remove(tempInput);
      }
    }
  }

  public void removeRuleInput(RuleInput i) {
    inputs.remove(i);
  }

  public void setRuleInputs(Vector<RuleInput> ruleInputs) {
    inputs = ruleInputs;
  }

  public Vector<ParticipantRuleEffect> getAllParticipantRuleEffects() {
    return participantRuleEffects;
  }

  public ParticipantRuleEffect getParticipantRuleEffect(String partName) // returns
                                                                         // the
                                                                         // ParticipantRuleEffect
                                                                         // with
                                                                         // the
                                                                         // participant
  // with the specified name
  {
    for (int i = 0; i < participantRuleEffects.size(); i++) {
      ParticipantRuleEffect tempEffect = participantRuleEffects.elementAt(i);
      if (tempEffect.getParticipant().getName().equals(partName)) {
        return tempEffect;
      }
    }
    return null;
  }

  public void addParticipantRuleEffect(ParticipantRuleEffect newEffect) {
    participantRuleEffects.add(newEffect);
  }

  public void removeParticipantRuleEffect(String partName) // removes the
                                                           // ParticipantRuleEffect
                                                           // for the
                                                           // participant with
                                                           // the
  // specified name
  {
    for (int i = 0; i < participantRuleEffects.size(); i++) {
      ParticipantRuleEffect tempEffect = participantRuleEffects.elementAt(i);
      if (tempEffect.getParticipant().getName().equals(partName)) {
        participantRuleEffects.remove(tempEffect);
      }
    }
  }

  public boolean hasParticipantRuleEffect(String partName) // returns true if
                                                           // this effect rule
                                                           // has a participant
                                                           // rule effect for
                                                           // the
  // participant with the specified name
  {
    for (int i = 0; i < participantRuleEffects.size(); i++) {
      ParticipantRuleEffect tempEffect = participantRuleEffects.elementAt(i);
      if (tempEffect.getParticipant().getName().equals(partName)) {
        return true;
      }
    }
    return false;
  }

  public void removeParticipantRuleEffect(ParticipantRuleEffect e) {
    participantRuleEffects.remove(e);
  }

  public void setParticipantRuleEffects(Vector<ParticipantRuleEffect> effects) {
    participantRuleEffects = effects;
  }
}

