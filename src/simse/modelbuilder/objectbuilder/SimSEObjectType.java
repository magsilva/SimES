/* This class defines a generic object type for use in SimSE */

package simse.modelbuilder.objectbuilder;

import java.util.*;

public class SimSEObjectType implements Cloneable {
  String name; // name of the object
  Vector attributes; // attributes of this object
  int type; // type of object (see SimSEObjectTypeTypes class)

  public SimSEObjectType(Vector attrs, int t, String n) {
    attributes = attrs;
    type = t;
    name = n;
  }

  public SimSEObjectType(int t, String n) {
    type = t;
    name = n;
    attributes = new Vector();
  }

  public SimSEObjectType() {
  }

  public String getName() {
    return name;
  }

  public Object clone() {
    try {
      SimSEObjectType cl = (SimSEObjectType) super.clone();
      cl.name = name;
      Vector clonedAtts = new Vector();
      for (int i = 0; i < attributes.size(); i++) {
        clonedAtts.addElement((Attribute) (((Attribute) (attributes
            .elementAt(i))).clone()));
      }
      cl.attributes = clonedAtts;
      return cl;
    } catch (CloneNotSupportedException c) {
      System.out.println(c.getMessage());
    }
    return null;
  }

  public Vector getAllAttributes() // returns a Vector of Attributes
  {
    return attributes;
  }
  
  public Vector getAllVisibleAttributes() {
    Vector visibleAtts = new Vector();
    for (int i=0; i<attributes.size(); i++) {
      Attribute att = (Attribute)attributes.get(i);
      if (att.isVisible()) {
        visibleAtts.add(att);
      }
    }
    return visibleAtts;
  }
  
  public Vector getAllVisibleOnCompletionAttributes() {
    Vector visibleAtts = new Vector();
    for (int i=0; i<attributes.size(); i++) {
      Attribute att = (Attribute)attributes.get(i);
      if (att.isVisibleOnCompletion()) {
        visibleAtts.add(att);
      }
    }
    return visibleAtts;
  }
  
  public int getNumVisibleAttributes() {
    int numVisibleAtts = 0;
    for (int i=0; i<attributes.size(); i++) {
      Attribute att = (Attribute)attributes.get(i);
      if (att.isVisible()) {
        numVisibleAtts++;
      }
    }
    return numVisibleAtts;
  }
  
  public int getNumVisibleOnCompletionAttributes() {
    int numVisibleAtts = 0;
    for (int i=0; i<attributes.size(); i++) {
      Attribute att = (Attribute)attributes.get(i);
      if (att.isVisibleOnCompletion()) {
        numVisibleAtts++;
      }
    }
    return numVisibleAtts;
  }

  public int getType() {
    return type;
  }

  public Attribute getKey() // returns the attribute that is the "key" of this
  // object -- the attribute that must be unique
  {
    for (int i = 0; i < attributes.size(); i++) {
      Attribute tempAttr = (Attribute) (attributes.elementAt(i));
      if (tempAttr.isKey()) {
        return tempAttr;
      }
    }
    return null;
  }

  public boolean hasKey() // returns true if this object has a key, false
  // otherwise
  {
    for (int i = 0; i < attributes.size(); i++) {
      Attribute tempAttr = (Attribute) (attributes.elementAt(i));
      if (tempAttr.isKey()) {
        return true;
      }
    }
    return false;
  }

  public void setName(String newName) {
    name = newName;
  }

  public void setAttributes(Vector attrs) {
    attributes = attrs;
  }

  public void setType(int newType) {
    type = newType;
  }

  public void addAttribute(Attribute a) {
    attributes.add(a); // adds the attribute to the end of the Vector
  }

  public void addAttribute(Attribute a, int index) // adds the attribute at the
  // specified index
  {
    attributes.add(index, a);
  }

  public int removeAttribute(String name) // removes the attribute with the
  // specified name and returns the
  // position it removed it from
  {
    for (int i = 0; i < attributes.size(); i++) {
      Attribute a = (Attribute) attributes.elementAt(i);
      if (a.getName().equals(name)) {
        int index = attributes.indexOf(a);
        attributes.remove(a);
        return index;
      }
    }
    return -1;
  }
  
  /*
   * Returns the index of the attribute with the specified name
   */
  public int getAttributeIndex(String name) 
  {
    for (int i = 0; i < attributes.size(); i++) {
      Attribute a = (Attribute) attributes.elementAt(i);
      if (a.getName().equals(name)) {
        int index = attributes.indexOf(a);
        return index;
      }
    }
    return -1;
  }

  public int getNumAttributes() // returns the number of attributes this object
  // has
  {
    return attributes.size();
  }

  public Attribute getAttribute(String name) // returns the attribute with the
  // specified name
  {
    for (int i = 0; i < attributes.size(); i++) {
      Attribute a = (Attribute) attributes.elementAt(i);
      if (a.getName().equals(name)) {
        return a;
      }
    }
    return null;
  }
}