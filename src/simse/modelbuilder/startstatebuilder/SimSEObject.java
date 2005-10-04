/* This class defines a generic object for use in SimSE */

package simse.modelbuilder.startstatebuilder;

import simse.modelbuilder.objectbuilder.*;
import java.util.*;

public class SimSEObject implements Cloneable {
  SimSEObjectType objType; // SimSEObjectType that this object is instantiating
  Vector instantiatedAttributes; // instantiated attributes of this object

  public SimSEObject(Vector attrs, SimSEObjectType t) {
    instantiatedAttributes = attrs;
    objType = t;
  }

  public SimSEObject(SimSEObjectType t) {
    instantiatedAttributes = new Vector();
    objType = t;
    Vector atts = objType.getAllAttributes();
    for (int i = 0; i < atts.size(); i++) {
      Attribute a = (Attribute) atts.elementAt(i);
      InstantiatedAttribute newInstAtt = new InstantiatedAttribute(a);
    }
  }

  public SimSEObject() {
  }

  public Object clone() {
    try {
      SimSEObject cl = (SimSEObject) (super.clone());
      cl.objType = (SimSEObjectType) (objType.clone());
      Vector clonedAtts = new Vector();
      for (int i = 0; i < instantiatedAttributes.size(); i++) {
        clonedAtts
            .add((InstantiatedAttribute) (((InstantiatedAttribute) (instantiatedAttributes
                .elementAt(i))).clone()));
      }
      cl.instantiatedAttributes = clonedAtts;
      return cl;
    } catch (CloneNotSupportedException c) {
      System.out.println(c.getMessage());
    }
    return null;
  }

  public String getName() {
    return objType.getName();
  }

  public Vector getAllAttributes() // returns a Vector of InstantiatedAttributes
  {
    return instantiatedAttributes;
  }

  public SimSEObjectType getSimSEObjectType() {
    return objType;
  }

  public InstantiatedAttribute getKey() // returns the instantiated attribute
                                        // that is the "key" of this object --
                                        // the attribute that must
  // be unique
  {
    for (int i = 0; i < instantiatedAttributes.size(); i++) {
      InstantiatedAttribute tempAttr = (InstantiatedAttribute) (instantiatedAttributes
          .elementAt(i));
      if (tempAttr.getAttribute().isKey()) {
        return tempAttr;
      }
    }
    return null;
  }

  public void setInstantiatedAttributes(Vector attrs) {
    instantiatedAttributes = attrs;
  }

  public int getNumAttributes() // returns the number of attributes this object
                                // has
  {
    return instantiatedAttributes.size();
  }

  public InstantiatedAttribute getAttribute(String name) // returns the
                                                         // instanatiated
                                                         // attribute with the
                                                         // specified name
  {
    for (int i = 0; i < instantiatedAttributes.size(); i++) {
      InstantiatedAttribute a = (InstantiatedAttribute) instantiatedAttributes
          .elementAt(i);
      if (a.getAttribute().getName().equals(name)) {
        return a;
      }
    }
    return null;
  }

  public void addAttribute(InstantiatedAttribute att) // adds the instantiated
                                                      // attribute to this
                                                      // object
  {
    if (!hasAttribute(att.getAttribute())) // doesn't already have this
                                           // attribute
    {
      instantiatedAttributes.add(att);
    }
  }

  public boolean hasAttribute(Attribute att) // returns true if this object has
                                             // the specified attribute as an
                                             // instantiated
  // attribute, false otherwise
  {
    for (int i = 0; i < instantiatedAttributes.size(); i++) {
      InstantiatedAttribute instAtt = (InstantiatedAttribute) instantiatedAttributes
          .elementAt(i);
      if ((instAtt.getAttribute().getName().equals(att.getName())))// &&
                                                                   // (instAtt.getAttribute().getType()
                                                                   // ==
                                                                   // att.getType()))
      {
        return true;
      }
    }
    return false;
  }

  public void updateInstantiatedAttributes() // checks to make sure that each
                                             // attribute in the object type has
                                             // a corresponding instantiated
  // attribute in this SimSEObject, and if not, creates one for it
  {
    Vector atts = objType.getAllAttributes();
    for (int i = 0; i < atts.size(); i++) {
      Attribute a = (Attribute) atts.elementAt(i);
      if (!hasAttribute(a)) // doesn't have this attribute
      {
        instantiatedAttributes.add(new InstantiatedAttribute(a));
      }
    }
  }
}