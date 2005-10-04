/*
 * This class defines an effect of an EffectRule on a particular SimSEObjectType
 * instantiation of a participant
 */

package simse.modelbuilder.rulebuilder;

import java.util.*;
import simse.modelbuilder.objectbuilder.*;

public class ParticipantTypeRuleEffect implements Cloneable {
  private SimSEObjectType ssObjType; // pointer to the SimSEObjectType for this
                                     // participant type
  private Vector attributeEffects; // Vector of ParticipantAttributeRuleEffects
                                   // for the attributes of this participant
  private String otherActEffect; // the effect of this action on the
                                 // participant's other actions (defined by
                                 // OtherActionsEffect class)

  public ParticipantTypeRuleEffect(SimSEObjectType type) {
    ssObjType = type;
    // initialize an effect for each attribute:
    attributeEffects = new Vector();
    Vector attributes = ssObjType.getAllAttributes();
    for (int i = 0; i < attributes.size(); i++) {
      attributeEffects.add(new ParticipantAttributeRuleEffect(
          (Attribute) attributes.elementAt(i)));
    }
    otherActEffect = OtherActionsEffect.NONE;
  }

  public Object clone() {
    try {
      ParticipantTypeRuleEffect cl = (ParticipantTypeRuleEffect) (super.clone());
      // participant:
      cl.ssObjType = ssObjType; // NOTE: since this is a pointer to the object
                                // type, it must remain a pointer to the
      // object type, even in the clone. So BE CAREFUL!!

      // clone attribute effects:
      Vector clonedAttEffects = new Vector();
      for (int i = 0; i < attributeEffects.size(); i++) {
        clonedAttEffects
            .add((ParticipantAttributeRuleEffect) (((ParticipantAttributeRuleEffect) (attributeEffects
                .elementAt(i))).clone()));
      }
      cl.attributeEffects = clonedAttEffects;

      // other effect:
      cl.otherActEffect = otherActEffect;
      return cl;
    } catch (CloneNotSupportedException c) {
      System.out.println(c.getMessage());
    }
    return null;
  }

  public SimSEObjectType getSimSEObjectType() {
    return ssObjType;
  }

  public Vector getAllAttributeEffects() {
    return attributeEffects;
  }

  public String getOtherActionsEffect() {
    return otherActEffect;
  }

  public ParticipantAttributeRuleEffect getAttributeEffect(String attName) // returns
                                                                           // the
                                                                           // effect
                                                                           // for
                                                                           // the
                                                                           // attribute
                                                                           // with
                                                                           // the
                                                                           // specified
  // name
  {
    for (int i = 0; i < attributeEffects.size(); i++) {
      ParticipantAttributeRuleEffect tempEffect = (ParticipantAttributeRuleEffect) attributeEffects
          .elementAt(i);
      if (tempEffect.getAttribute().getName().equals(attName)) {
        return tempEffect;
      }
    }
    return null;
  }

  public void addAttributeEffect(ParticipantAttributeRuleEffect newAttEffect) // if
                                                                              // there
                                                                              // is
                                                                              // already
                                                                              // an
                                                                              // effect
                                                                              // for
                                                                              // this
                                                                              // attribute,
                                                                              // the
                                                                              // new
  // one replaces it
  {
    for (int i = 0; i < attributeEffects.size(); i++) {
      ParticipantAttributeRuleEffect tempEffect = (ParticipantAttributeRuleEffect) attributeEffects
          .elementAt(i);
      if (tempEffect.getAttribute().getName().equals(
          newAttEffect.getAttribute().getName())) {
        attributeEffects.remove(tempEffect);
      }
    }
    attributeEffects.add(newAttEffect);
  }

  public void addAttributeEffect(ParticipantAttributeRuleEffect newAttEffect,
      int position) // adds the new effect in the specified
  // position
  {
    attributeEffects.add(position, newAttEffect);
  }

  public void removeAttributeEffect(String attName) // removes the attribute
                                                    // effect for the attribute
                                                    // with the specified name
  {
    for (int i = 0; i < attributeEffects.size(); i++) {
      ParticipantAttributeRuleEffect tempEffect = (ParticipantAttributeRuleEffect) attributeEffects
          .elementAt(i);
      if (tempEffect.getAttribute().getName().equals(attName)) {
        attributeEffects.remove(tempEffect);
      }
    }
  }

  public void setOtherActionsEffect(String newEffect) {
    otherActEffect = newEffect;
  }
}

