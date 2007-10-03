/*
 * This class is the data structure for holding the object types that are
 * currently in memory and manipulable by the user
 */

package simse.modelbuilder.objectbuilder;

import java.util.*;

public class DefinedObjectTypes {
  Vector<SimSEObjectType> objs; // Vector of SimSEObjectTypes

  public DefinedObjectTypes(Vector<SimSEObjectType> v) {
    objs = new Vector<SimSEObjectType>(v);
  }

  public DefinedObjectTypes() {
    objs = new Vector<SimSEObjectType>();
  }

  public Vector<SimSEObjectType> getAllObjectTypes() // returns a Vector of SimSEObjectTypes
  {
    return objs;
  }

  public void addObjectType(SimSEObjectType newObject) {
    // insert at correct alpha order:
//    for (int i = 0; i < objs.size(); i++) {
//      SimSEObjectType tempObj = (SimSEObjectType) objs.elementAt(i);
//      if (newObject.getName().compareToIgnoreCase(tempObj.getName()) < 0) { 
//        // should be inserted before tempObj
//        objs.insertElementAt(newObject, i);
//        return;
//      }
//    }
    
    // only reaches here if objs is empty or "newObject" should be placed at 
    // the end
    objs.add(newObject);
  }
  
  /*
   * Adds the new object at the specified position
   */
  public void addObjectType(SimSEObjectType newObject, int position) {
    objs.insertElementAt(newObject, position);
  }

  public Vector<SimSEObjectType> getAllObjectTypesOfType(int type) // returns a Vector of all
                                                  // SimSEObjectTypes in the
                                                  // data structure that have
                                                  // type = to the type
                                                  // parameter
  {
    Vector<SimSEObjectType> v = new Vector<SimSEObjectType>();
    for (int i = 0; i < objs.size(); i++) {
      SimSEObjectType tempObj = objs.elementAt(i);
      if (tempObj.getType() == type) // object is of the specified type
      {
        v.add(tempObj); // add it to the vector to return
      }
    }
    return v;
  }

  public SimSEObjectType getObjectType(int type, String name) // returns the
                                                              // object type in
                                                              // the data
                                                              // structure with
                                                              // the specified
                                                              // type and name
  {
    for (int i = 0; i < objs.size(); i++) {
      SimSEObjectType tempObj = objs.elementAt(i);
      if ((tempObj.getType() == type) && (tempObj.getName().equals(name))) {
        return tempObj;
      }
    }
    return null;
  }

  /*
   * removes the object type w/ the specified name and type from the data
   * structure and returns the position it was removed from, or -1 if not found
   */
  public int removeObjectType(int type, String name) 
  {
    for (int i = 0; i < objs.size(); i++) {
      SimSEObjectType tempObj = objs.elementAt(i);
      if ((tempObj.getType() == type) && (tempObj.getName().equals(name))) {
        objs.remove(i);
        return i;
      }
    }
    return -1;
  }

  public boolean hasObjectType(SimSEObjectType type) // returns true if this
                                                     // object type is there,
                                                     // false if not
  {
    if (objs.contains(type)) {
      return true;
    }
    return false;
  }
  
  public int getIndexOf(SimSEObjectType type) {
    for (int i = 0; i < objs.size(); i++) {
      SimSEObjectType objType = objs.elementAt(i);
      if (objType.getName().equals(type.getName())) {
        return objs.indexOf(objType);
      }
    }
    return -1;
  }

  public void clearAll() // removes all object types
  {
    objs.removeAllElements();
  }
  
  /*
   * sorts the object types in ascending alpha order by name
   */
//  public void sort() { 
//    Vector temp = (Vector) objs.clone();
//    clearAll();
//    for (int i = 0; i < temp.size(); i++) {
//      addObjectType((SimSEObjectType)temp.elementAt(i));
//    }
//  }
}