/*
 * This class defines a data structure for holding all of the action types that
 * have been defined
 */

package simse.modelbuilder.actionbuilder;

import java.util.*;

public class DefinedActionTypes {
  Vector actions; // vector of ActionTypes that holds defined action types

  public DefinedActionTypes() {
    actions = new Vector();
  }

  public Vector getAllActionTypes() {
    return actions;
  }

  public ActionType getActionType(String actionName) // returns the action type
                                                     // with the specified name
  {
    for (int i = 0; i < actions.size(); i++) {
      ActionType tempAct = (ActionType) (actions.elementAt(i));
      if (tempAct.getName().equals(actionName)) {
        return tempAct;
      }
    }
    return null;
  }

  public void addActionType(ActionType act)
  {
//	  // insert at correct alpha order:
//	  for (int i = 0; i < actions.size(); i++) {
//	    ActionType tempAct = (ActionType) actions.elementAt(i);
//	    if (act.getName().compareToIgnoreCase(tempAct.getName()) < 0) { 
//	      // should be inserted before tempAct
//	      actions.insertElementAt(act, i);
//	      return;
//	    }
//	  }
  
	  // only reaches here if actions is empty or "act" should be placed at 
	  // the end
	  actions.add(act);
  }
  
  /*
   * adds and action type at the specified position
   */
  public void addActionType(ActionType act, int position) {
    actions.insertElementAt(act, position);
  }

  public void removeActionType(ActionType act) // removes the specified action
                                               // type from the data structure
  {
    actions.remove(act);
  }

  /*
   * removes the action with the specified name and returns the position from
   * which it was removed
   */
  public int removeActionType(String name)
  {
    for (int i = 0; i < actions.size(); i++) {
      ActionType tempAct = (ActionType) (actions.elementAt(i));
      if (tempAct.getName().equals(name)) {
        actions.removeElementAt(i);
        return i;
      }
    }
    return -1;
  }
  
  
  public int getIndexOf(ActionType type) {
    for (int i = 0; i < actions.size(); i++) {
      ActionType actType = (ActionType) actions.elementAt(i);
      if (actType.getName().equals(type.getName())) {
        return actions.indexOf(actType);
      }
    }
    return -1;
  }

  public void clearAll() // removes all existing action types from the data
                         // structure
  {
    actions.removeAllElements();
  }
  
  /*
   * sorts the action types in ascending alpha order by name
   */
//  public void sort() { 
//    Vector temp = (Vector) actions.clone();
//    clearAll();
//    for (int i = 0; i < temp.size(); i++) {
//      addActionType((ActionType)temp.elementAt(i));
//    }
//  }

  public void removeAllRules() // removes all rules from all action types
  {
    for (int i = 0; i < actions.size(); i++) {
      ActionType tempAct = (ActionType) actions.elementAt(i);
      tempAct.removeAllRules();
    }
  }
}