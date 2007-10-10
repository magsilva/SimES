/* This class defines a rule that destroys objects */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.actionbuilder.*;
import java.util.*;

public class DestroyObjectsRule extends Rule implements Cloneable {
  private Vector<DestroyObjectsRuleParticipantCondition> participantConditions; // vector of
                                        // DestroyObjectsRuleParticipantConditions
                                        // for this rule

  public DestroyObjectsRule(String name, ActionType act) {
    super(name, act);
    participantConditions = 
    	new Vector<DestroyObjectsRuleParticipantCondition>();
    // add an empty condition for each participant:
    Vector<ActionTypeParticipant> parts = act.getAllParticipants();
    for (int i = 0; i < parts.size(); i++) {
      addEmptyCondition(parts.elementAt(i));
    }
  }

  public Object clone() {
    DestroyObjectsRule cl = (DestroyObjectsRule) (super.clone());
    Vector<DestroyObjectsRuleParticipantCondition> clonedConditions = 
    	new Vector<DestroyObjectsRuleParticipantCondition>();
    for (int i = 0; i < participantConditions.size(); i++) {
      clonedConditions
          .add((DestroyObjectsRuleParticipantCondition) (participantConditions
              .elementAt(i).clone()));
    }
    cl.participantConditions = clonedConditions;
    return cl;
  }

  public Vector<DestroyObjectsRuleParticipantCondition> getAllParticipantConditions() {
    return participantConditions;
  }

  public DestroyObjectsRuleParticipantCondition getParticipantCondition(
      String participantName) // returns the condition for the specified
  // participant to be destroyed by this rule
  {
    for (int i = 0; i < participantConditions.size(); i++) {
      DestroyObjectsRuleParticipantCondition tempCond = participantConditions
          .elementAt(i);
      if (tempCond.getParticipant().getName().equals(participantName)) {
        return tempCond;
      }
    }
    return null;
  }

  public void addParticipantCondition(
      DestroyObjectsRuleParticipantCondition newCond) // adds the specified
                                                      // participant condition
                                                      // to this
  // DestroyObjectsRule; if a condition for this participant is already there,
  // the new condition replaces it.
  {
    boolean notFound = true;
    for (int i = 0; i < participantConditions.size(); i++) {
      DestroyObjectsRuleParticipantCondition tempCond = participantConditions
          .elementAt(i);
      if (tempCond.getParticipant().getName().equals(
          newCond.getParticipant().getName())) // condition for this participant
                                               // already
      // exists, needs to be replaced
      {
        participantConditions.setElementAt(newCond, i);
        notFound = false;
        break;
      }
    }
    if (notFound) // new condition, not a replacement for a previous one
    {
      participantConditions.add(newCond);
    }
  }

  public void addEmptyCondition(ActionTypeParticipant part) // adds a new
                                                            // condition that is
                                                            // unconstrained to
                                                            // this
  // DestroyObjectsRule; if a condition for this participant is already there,
  // the new condition replaces it.
  {
    for (int i = 0; i < participantConditions.size(); i++) {
      DestroyObjectsRuleParticipantCondition tempCond = participantConditions
          .elementAt(i);
      if (tempCond.getParticipant().getName().equals(part.getName())) // condition
                                                                      // for
                                                                      // this
                                                                      // participant
                                                                      // already
                                                                      // exists,
                                                                      // needs
                                                                      // to be
      // replaced
      {
        participantConditions.remove(tempCond);
      }
    }
    participantConditions.add(new DestroyObjectsRuleParticipantCondition(part));
  }

  public void removeParticipantConditions(String partName) // removes the
                                                           // condition for the
                                                           // participant with
                                                           // the specified name
  {
    for (int i = 0; i < participantConditions.size(); i++) {
      DestroyObjectsRuleParticipantCondition tempCond = participantConditions
          .elementAt(i);
      if (tempCond.getParticipant().getName().equals(partName)) {
        participantConditions.removeElementAt(i);
      }
    }
  }

  public void setConditions(Vector<DestroyObjectsRuleParticipantCondition> newConditions) {
    participantConditions = newConditions;
  }
}