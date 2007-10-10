/*
 * This class defines a "create objects" rule -- a rule whose effect is that it
 * creates new SimSEObjects
 */

package simse.modelbuilder.rulebuilder;

import java.util.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;

public class CreateObjectsRule extends Rule implements Cloneable {
  private Vector<SimSEObject> objects; // Vector of SimSEObjects that this rule creates as
                          // its effect

  public CreateObjectsRule(String name, ActionType act) {
    super(name, act);
    objects = new Vector<SimSEObject>();
  }

  public Object clone() {
    CreateObjectsRule cl = (CreateObjectsRule) (super.clone());
    Vector<SimSEObject> clonedObjs = new Vector<SimSEObject>();
    for (int i = 0; i < objects.size(); i++) {
      clonedObjs.add((SimSEObject) (objects.elementAt(i).clone()));
    }
    cl.objects = clonedObjs;
    return cl;
  }

  public Vector<SimSEObject> getAllSimSEObjects() // returns all SimSEObjects that are
                                     // created as an effect of this rule
  {
    return objects;
  }

  public void setObjects(Vector<SimSEObject> newObjs) {
    objects = newObjs;
  }

  public void addSimSEObject(SimSEObject newObj) // adds the specified
                                                 // SimSEObject to this rule
  {
    objects.add(newObj);
  }

  public void addSimSEObject(SimSEObject newObj, int index) // adds the
                                                            // specified
                                                            // SimSEObject to
                                                            // this rule in the
                                                            // specified
                                                            // position
  {
    objects.add(index, newObj);
  }

  public void removeSimSEObject(String name, int type, Object keyVal) // removes
                                                                      // the
                                                                      // SimSEObject
                                                                      // with
                                                                      // the
                                                                      // specified
                                                                      // SimSEObjectType
  // name, type, and key attribute value from this rule
  {
    for (int i = 0; i < objects.size(); i++) {
      SimSEObject tempObj = objects.elementAt(i);
      if ((tempObj.getName().equals(name))
          && (tempObj.getSimSEObjectType().getType() == type)
          && (tempObj.getKey().isInstantiated())
          && (tempObj.getKey().getValue().equals(keyVal))) // found a match
      {
        objects.removeElementAt(i);
      }
    }
  }

  public void removeSimSEObject(SimSEObject obj) // removes this SimSEObject
                                                 // from this rule (if it
                                                 // exists)
  {
    objects.remove(obj);
  }
}