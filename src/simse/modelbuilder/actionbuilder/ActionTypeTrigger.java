/* This class defines a trigger for an action type */

package simse.modelbuilder.actionbuilder;

import java.util.*;

public abstract class ActionTypeTrigger implements Cloneable {
  private String name;
  private Vector participantTriggers; // vector of ActionTypeParticipantTriggers
                                      // for this action type trigger
  private String triggerText; // text that shows up over the head of an employee
                              // participant when this is triggered
  private int priority; // priority of execution of this trigger
  private boolean gameEnding; // whether or not this is a game-ending trigger
  private ActionType action; // POINTER TO action type that this trigger is
                             // attached to

  // trigger type constants:
  public static final String AUTO = "Autonomous";
  public static final String USER = "User-Initiated";
  public static final String RANDOM = "Random";

  public ActionTypeTrigger(String n, ActionType a) {
    name = n;
    participantTriggers = new Vector();
    triggerText = new String();
    priority = -1;
    gameEnding = false;
    action = a;
  }

  public Object clone() {
    try {
      ActionTypeTrigger cl = (ActionTypeTrigger) (super.clone());
      cl.name = name;
      Vector clonedTriggers = new Vector();
      for (int i = 0; i < participantTriggers.size(); i++) {
        clonedTriggers
            .add((ActionTypeParticipantTrigger) (((ActionTypeParticipantTrigger) (participantTriggers
                .elementAt(i))).clone()));
      }
      cl.participantTriggers = clonedTriggers;
      cl.triggerText = triggerText;
      cl.priority = priority;
      cl.gameEnding = gameEnding;
      cl.action = action; // NOTE: since this is a pointer to the action, it
                          // must remain a pointer to the
      // action, even in the clone. So BE CAREFUL!!
      return cl;
    } catch (CloneNotSupportedException c) {
      System.out.println(c.getMessage());
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String n) {
    name = n;
  }

  public String getTriggerText() {
    return triggerText;
  }

  public void setTriggerText(String newText) {
    triggerText = newText;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int newPri) {
    priority = newPri;
  }

  public boolean isGameEndingTrigger() {
    return gameEnding;
  }

  public void setGameEndingTrigger(boolean val) {
    gameEnding = val;
  }

  public Vector getAllParticipantTriggers() {
    return participantTriggers;
  }

  public ActionTypeParticipantTrigger getParticipantTrigger(
      String participantName) // returns the trigger for the specified
                              // participant
  {
    for (int i = 0; i < participantTriggers.size(); i++) {
      ActionTypeParticipantTrigger tempTrig = (ActionTypeParticipantTrigger) (participantTriggers
          .elementAt(i));
      if (tempTrig.getParticipant().getName().equals(participantName)) {
        return tempTrig;
      }
    }
    return null;
  }

  public void addParticipantTrigger(ActionTypeParticipantTrigger newTrig) // adds
                                                                          // the
                                                                          // specified
                                                                          // participant
                                                                          // trigger
                                                                          // to
                                                                          // this
  // ActionTypeTrigger; if a trigger for this participant is already there, the
  // new trigger replaces it.
  {
    boolean notFound = true;
    for (int i = 0; i < participantTriggers.size(); i++) {
      ActionTypeParticipantTrigger tempTrig = (ActionTypeParticipantTrigger) (participantTriggers
          .elementAt(i));
      if (tempTrig.getParticipant().getName().equals(
          newTrig.getParticipant().getName())) // trigger for this participant
                                               // already
      // exists, needs to be replaced
      {
        participantTriggers.setElementAt(newTrig, i);
        notFound = false;
      }
    }
    if (notFound) // new trigger, not a replacement for a previous one
    {
      participantTriggers.add(newTrig);
    }
  }

  public void addEmptyTrigger(ActionTypeParticipant part) // adds a new
                                                          // participant trigger
                                                          // that is
                                                          // unconstrained to
                                                          // this
  // ActionTypeTrigger; if a trigger for this participant is already there, the
  // new trigger replaces it.
  {
    for (int i = 0; i < participantTriggers.size(); i++) {
      ActionTypeParticipantTrigger tempTrig = (ActionTypeParticipantTrigger) (participantTriggers
          .elementAt(i));
      if (tempTrig.getParticipant().getName().equals(part.getName())) // trigger
                                                                      // for
                                                                      // this
                                                                      // participant
                                                                      // already
                                                                      // exists,
                                                                      // needs
                                                                      // to be
      // replaced
      {
        participantTriggers.remove(tempTrig);
      }
    }
    participantTriggers.add(new ActionTypeParticipantTrigger(part));
  }

  public void removeTrigger(String partName) // removes the trigger for the
                                             // participant with the specified
                                             // name
  {
    for (int i = 0; i < participantTriggers.size(); i++) {
      ActionTypeParticipantTrigger tempTrig = (ActionTypeParticipantTrigger) (participantTriggers
          .elementAt(i));
      if (tempTrig.getParticipant().getName().equals(partName)) {
        participantTriggers.removeElementAt(i);
      }
    }
  }

  public void setTriggers(Vector newTriggers) {
    participantTriggers = newTriggers;
  }

  public ActionTypeTrigger morph(String triggerType) // morphs this
                                                     // ActionTypeTrigger to the
                                                     // new type of trigger
                                                     // specified and returns
  // it.
  {
    if (triggerType.equals(AUTO)) // autonomous trigger type
    {
      AutonomousActionTypeTrigger autoTrig = new AutonomousActionTypeTrigger(
          name, action);
      autoTrig.setTriggers(participantTriggers);
      autoTrig.setTriggerText(triggerText);
      autoTrig.setPriority(priority);
      autoTrig.setGameEndingTrigger(gameEnding);
      return autoTrig;
    }
    if (triggerType.equals(USER)) // user trigger type
    {
      UserActionTypeTrigger userTrig = new UserActionTypeTrigger(name, action);
      if (this instanceof UserActionTypeTrigger) {
        userTrig.setMenuText(((UserActionTypeTrigger) (this)).getMenuText());
        userTrig.setRequiresConfirmation(((UserActionTypeTrigger) (this)).
            requiresConfirmation());
      }
      userTrig.setTriggers(participantTriggers);
      userTrig.setTriggerText(triggerText);
      userTrig.setPriority(priority);
      userTrig.setGameEndingTrigger(gameEnding);
      return userTrig;
    }
    if (triggerType.equals(RANDOM)) // random trigger type
    {
      RandomActionTypeTrigger randomTrig = new RandomActionTypeTrigger(name,
          action);
      if (this instanceof RandomActionTypeTrigger) {
        randomTrig.setFrequency(((RandomActionTypeTrigger) (this))
            .getFrequency());
      }
      randomTrig.setTriggers(participantTriggers);
      randomTrig.setTriggerText(triggerText);
      randomTrig.setPriority(priority);
      randomTrig.setGameEndingTrigger(gameEnding);
      return randomTrig;
    }
    return null;
  }

  public ActionType getActionType() // returns a COPY of the action type that
                                    // this trigger is attached to
  {
    return (ActionType) action.clone();
  }
}