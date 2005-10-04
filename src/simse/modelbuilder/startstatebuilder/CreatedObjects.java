/*
 * This class is the data structure for holding the objects that are currently
 * instantiated
 */

package simse.modelbuilder.startstatebuilder;

import simse.modelbuilder.objectbuilder.*;

import java.util.*;

public class CreatedObjects {
  Vector objs; // Vector of SimSEObjects
  String startingNarrative; // starting narrative

  public CreatedObjects(Vector v, String s) {
    objs = v;
    startingNarrative = s;
  }

  public CreatedObjects() {
    objs = new Vector();
    startingNarrative = new String();
  }

  public Vector getAllObjects() // returns a Vector of SimSEObjects
  {
    return objs;
  }

  public Vector getAllObjectsOfType(SimSEObjectType type) // returns a Vector of
                                                          // SimSEObjects, all
                                                          // of them having the
                                                          // type specified
  {
    Vector v = new Vector();
    for (int i = 0; i < objs.size(); i++) {
      SimSEObject obj = (SimSEObject) objs.elementAt(i);
      if (obj.getSimSEObjectType() == type) {
        v.add(obj);
      }
    }
    return v;
  }

  public void addObject(SimSEObject newObject) {
    objs.add(newObject);
  }

  public SimSEObject getObject(int type, String name, Object keyAttValue) // returns
                                                                          // the
                                                                          // object
                                                                          // in
                                                                          // the
                                                                          // data
                                                                          // structure
                                                                          // with
                                                                          // the
                                                                          // specified
                                                                          // type,
                                                                          // name,
                                                                          // and
                                                                          // key
                                                                          // attribute
                                                                          // value
  {
    for (int i = 0; i < objs.size(); i++) {
      SimSEObject tempObj = ((SimSEObject) objs.elementAt(i));
      if ((tempObj.getSimSEObjectType().getType() == type)
          && (tempObj.getName().equals(name))
          && (tempObj.getKey().isInstantiated())
          && (tempObj.getKey().getValue().equals(keyAttValue))) {
        return tempObj;
      }
    }
    return null;
  }

  public void removeObject(int type, String name, Object keyAttValue) // removes
                                                                      // the
                                                                      // object
                                                                      // w/ the
                                                                      // specified
                                                                      // name,
                                                                      // type,
                                                                      // and key
                                                                      // attribute
                                                                      // value
                                                                      // from
                                                                      // the
                                                                      // data
                                                                      // structure
  {
    for (int i = 0; i < objs.size(); i++) {
      SimSEObject tempObj = ((SimSEObject) objs.elementAt(i));
      if ((tempObj.getSimSEObjectType().getType() == type)
          && (tempObj.getName().equals(name))
          && (tempObj.getKey().isInstantiated())
          && (tempObj.getKey().getValue().equals(keyAttValue))) {
        objs.remove(i);
      }
    }
  }

  public void removeObject(SimSEObject obj) {
    objs.remove(obj);
  }

  public void clearAll() // removes all objects
  {
    objs.removeAllElements();
  }

  public String getStartingNarrative() {
    return startingNarrative;
  }

  public void setStartingNarrative(String s) {
    startingNarrative = s;
  }

  public void updateAllInstantiatedAttributes() // calls
                                                // updateAllInstantiatedAttributes()
                                                // for all created objects
  {
    for (int i = 0; i < objs.size(); i++) {
      SimSEObject tempObj = (SimSEObject) objs.elementAt(i);
      tempObj.updateInstantiatedAttributes();
    }
  }
}