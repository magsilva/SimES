/*
 * This class is for generating the CreatedObjects into a file and reading that
 * file into memory
 */

package simse.modelbuilder.startstatebuilder;

import simse.modelbuilder.ModelFileManipulator;
import simse.modelbuilder.objectbuilder.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class StartStateFileManipulator {
  private DefinedObjectTypes objectTypes;
  private CreatedObjects objects;
  private Random ranNumGen; // random number generator

  public StartStateFileManipulator(DefinedObjectTypes defObjs,
      CreatedObjects createdObjs) {
    objectTypes = defObjs;
    objects = createdObjs;
    ranNumGen = new Random();
  }

  public Vector loadFile(File inputFile) // loads the start state portion of the
                                         // model file, filling the
  // "objects" data structures with the data from the file. Returns a Vector of
  // warning messages
  {
    objects.clearAll();
    Vector warnings = new Vector(); // vector of warning messages
    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      boolean foundBeginningOfStartState = false;
      while (!foundBeginningOfStartState) {
        String currentLine = reader.readLine(); // read in a line of text from
                                                // the file
        if (currentLine.equals(ModelFileManipulator.BEGIN_CREATED_OBJECTS_TAG)) // beginning of start
                                                           // state objects
        {
          foundBeginningOfStartState = true;
          boolean endOfCreatedObjects = false;
          while (!endOfCreatedObjects) {
            currentLine = reader.readLine();
            if (currentLine.equals(ModelFileManipulator.END_CREATED_OBJECTS_TAG)) // end of start
                                                             // state objects
            {
              endOfCreatedObjects = true;
            } else // not end of start state objects yet
            {
              if (currentLine.equals(ModelFileManipulator.BEGIN_STARTING_NARRATIVE_TAG)) {
                StringBuffer startNarr = new StringBuffer();
                String tempInLine = reader.readLine();
                while (tempInLine.equals(ModelFileManipulator.END_STARTING_NARRATIVE_TAG) == false) // not
                                                                               // done
                                                                               // yet
                {
                  startNarr.append(tempInLine);
                  tempInLine = reader.readLine();
                  if (tempInLine.equals(ModelFileManipulator.END_STARTING_NARRATIVE_TAG) == false) // not
                                                                              // done
                                                                              // yet
                  {
                    startNarr.append('\n');
                  }
                }
                objects.setStartingNarrative(startNarr.toString());
              }

              else if (currentLine.equals(ModelFileManipulator.BEGIN_OBJECT_TAG)) {
                String metaType = reader.readLine();
                String type = reader.readLine();
                SimSEObjectType objType = objectTypes.getObjectType((Integer
                    .parseInt(metaType)), type);
                // get the SimSEObjectType from the type and name in these 2
                // lines of the file
                if (objType == null) // no such object type found
                {
                  warnings.add("Object type "
                      + SimSEObjectTypeTypes
                          .getText(Integer.parseInt(metaType)) + " " + type
                      + " removed -- ignoring created object of this type");
                  // ignore this whole object:
                  while (true) {
                    String tempLine = reader.readLine();
                    if (tempLine.equals(ModelFileManipulator.END_OBJECT_TAG)) {
                      break;
                    }
                  }
                } else {
                  SimSEObject newObj = new SimSEObject(objType); // create a new
                                                                 // object with
                                                                 // the
                                                                 // SimSEObjectType
                  boolean endOfObj = false;
                  while (!endOfObj) {
                    currentLine = reader.readLine(); // get the next line
                    if (currentLine.equals(ModelFileManipulator.END_OBJECT_TAG)) // end of object
                    {
                      // make sure you have all of the attributes from the
                      // SimSEObjectType added as instantiated
                      // attributes:
                      Vector atts = objType.getAllAttributes();
                      for (int i = 0; i < atts.size(); i++) {
                        Attribute tempAtt = (Attribute) atts.elementAt(i);
                        if (newObj.hasAttribute(tempAtt) == false) // object
                                                                   // doesn't
                                                                   // have this
                                                                   // attribute
                                                                   // added as
                                                                   // an
                        // instantiated attribute
                        {
                          newObj
                              .addAttribute(new InstantiatedAttribute(tempAtt)); // create
                                                                                 // and
                                                                                 // add
                                                                                 // a
                                                                                 // new
                                                                                 // instantiated
                          // attribute with no value
                        }
                      }
                      endOfObj = true;
                      objects.addObject(newObj); // add object to defined object
                                                 // types
                    } else if (currentLine
                        .equals(ModelFileManipulator.BEGIN_INSTANTIATED_ATTRIBUTE_TAG)) // beginning
                                                                   // of
                                                                   // attribute
                    {
                      String attName = reader.readLine(); // get the attribute
                                                          // name
                      Attribute attribute = objType.getAttribute(attName); // get
                                                                           // the
                                                                           // attribute
                      if (attribute == null) // no such attribute
                      {
                        warnings
                            .add(SimSEObjectTypeTypes.getText(Integer
                                .parseInt(metaType))
                                + " "
                                + type
                                + " "
                                + attName
                                + " attribute removed -- ignoring this attribute in created object of this type");
                        // ignore entire instantiated attribute:
                        while (true) {
                          String tempLine = reader.readLine();
                          if (tempLine.equals(ModelFileManipulator.END_INSTANTIATED_ATTRIBUTE_TAG)) {
                            break;
                          }
                        }
                      } else {
                        String value = reader.readLine(); // get attribute's
                                                          // value (if any)
                        if (value.equals(ModelFileManipulator.EMPTY_VALUE)) // attribute has no value
                        {
                          if (attribute.isKey()) // key attribute, but has no
                                                 // value
                          {
                            if (attribute.getType() == AttributeTypes.BOOLEAN) {
                              boolean trueTaken = false;
                              boolean falseTaken = false;

                              // check if there are any of this same object type
                              // that have "true" or "false" assigned to their
                              // key
                              //attribute value:
                              Vector objs = objects.getAllObjects();
                              for (int i = 0; i < objs.size(); i++) {
                                SimSEObject obj2 = (SimSEObject) objs
                                    .elementAt(i);
                                if ((obj2 != newObj)
                                    && (obj2.getSimSEObjectType() == newObj
                                        .getSimSEObjectType())) // not the
                                                                // object in
                                // question, but same object type
                                {
                                  if ((obj2.getKey().isInstantiated())
                                      && (obj2.getKey().getValue() instanceof Boolean)) {
                                    if (((Boolean) (obj2.getKey().getValue()))
                                        .booleanValue() == true) {
                                      trueTaken = true;
                                      break;
                                    } else if (((Boolean) (obj2.getKey()
                                        .getValue())).booleanValue() == false) {
                                      falseTaken = true;
                                      break;
                                    }
                                  }
                                }
                              }
                              if (trueTaken && !falseTaken) // just true is
                                                            // taken
                              {
                                newObj.addAttribute(new InstantiatedAttribute(
                                    attribute, new Boolean(false))); // set it
                                                                     // to false
                                String warning = ("Key attribute value not instantiated for start state object: "
                                    + newObj.getSimSEObjectType().getName()
                                    + " "
                                    + SimSEObjectTypeTypes.getText(newObj
                                        .getSimSEObjectType().getType()) + "; random boolean (false) assigned");
                                warnings.add(warning);
                              } else // just false is taken, both are taken, or
                                     // neither is taken
                              {
                                newObj.addAttribute(new InstantiatedAttribute(
                                    attribute, new Boolean(true))); // set it to
                                                                    // true
                                String warning = ("Key attribute value not instantiated for start state object: "
                                    + newObj.getSimSEObjectType().getName()
                                    + " "
                                    + SimSEObjectTypeTypes.getText(newObj
                                        .getSimSEObjectType().getType()) + "; random boolean (true) assigned");
                                warnings.add(warning);
                              }
                            }

                            else if (attribute.getType() == AttributeTypes.STRING) {
                              boolean strTaken = true;
                              StringBuffer ranStr = new StringBuffer();
                              while (strTaken) {
                                int ranNumChars = ranNumGen.nextInt(41) + 1; // number
                                                                             // of
                                                                             // random
                                                                             // characters
                                                                             // to
                                                                             // generate
                                                                             // (the
                                                                             // + 1
                                                                             // is
                                                                             // there
                                // so that it will never be 0 chars)
                                for (int i = ranNumChars; i > 0; i--) {
                                  char ranChr = (char) ((ranNumGen.nextInt(95)) + 32);
                                  ranStr.append(ranChr);
                                }
                                if (objects.getAllObjectsOfType(
                                    newObj.getSimSEObjectType()).size() == 0) {
                                  strTaken = false;
                                } else {
                                  // check if ranStr is already taken:
                                  Vector objs = objects.getAllObjects();
                                  for (int j = 0; j < objs.size(); j++) {
                                    SimSEObject obj2 = (SimSEObject) objs
                                        .elementAt(j);
                                    if ((obj2 != newObj)
                                        && (obj2.getSimSEObjectType() == newObj
                                            .getSimSEObjectType())) // not the
                                                                    // object
                                    // in question, but same object type
                                    {
                                      if ((obj2.getKey().isInstantiated())
                                          && (obj2.getKey().getValue() instanceof String)
                                          && (((String) (obj2.getKey()
                                              .getValue())).equals(ranStr
                                              .toString()))) {
                                        strTaken = true;
                                      } else {
                                        strTaken = false;
                                      }
                                    }
                                  }
                                }
                              }

                              // set the value to the random string:
                              newObj.addAttribute(new InstantiatedAttribute(
                                  attribute, ranStr.toString()));
                              String warning = ("Key attribute value not instantiated for start state object: "
                                  + newObj.getSimSEObjectType().getName()
                                  + " "
                                  + SimSEObjectTypeTypes.getText(newObj
                                      .getSimSEObjectType().getType())
                                  + "; random string \"" + ranStr.toString() + "\" assigned");
                              warnings.add(warning);
                            }

                            else if (attribute.getType() == AttributeTypes.INTEGER) {
                              boolean intTaken = true;
                              int ranInt = 0;
                              while (intTaken) {
                                ranInt = ranNumGen.nextInt(999999);
                                if (objects.getAllObjectsOfType(
                                    newObj.getSimSEObjectType()).size() == 0) {
                                  intTaken = false;
                                } else {
                                  // check if ranInt is already taken:
                                  Vector objs = objects.getAllObjects();
                                  for (int i = 0; i < objs.size(); i++) {
                                    SimSEObject obj2 = (SimSEObject) objs
                                        .elementAt(i);
                                    if ((obj2 != newObj)
                                        && (obj2.getSimSEObjectType() == newObj
                                            .getSimSEObjectType())) // not the
                                                                    // object
                                    // in question, but same object type
                                    {
                                      if ((obj2.getKey().isInstantiated())
                                          && (obj2.getKey().getValue() instanceof Integer)
                                          && (((Integer) (obj2.getKey()
                                              .getValue())).intValue() == ranInt)) {
                                        intTaken = true;
                                      } else {
                                        intTaken = false;
                                      }
                                    }
                                  }
                                }
                              }

                              // check if random int is within min/max values:
                              boolean withinRange = false;
                              while (!withinRange) {
                                withinRange = true;
                                if (((NumericalAttribute) attribute)
                                    .isMinBoundless() == false) // has a min val
                                {
                                  if (ranInt < ((NumericalAttribute) attribute)
                                      .getMinValue().intValue()) {
                                    withinRange = false;
                                  }
                                }
                                if (((NumericalAttribute) attribute)
                                    .isMaxBoundless() == false) // has a max val
                                {
                                  if (ranInt > ((NumericalAttribute) attribute)
                                      .getMaxValue().intValue()) {
                                    withinRange = false;
                                  }
                                }
                                if (!withinRange) {
                                  // generate new random int:
                                  ranInt = ranNumGen.nextInt(999999);
                                }
                              }

                              // set the value to the random int:
                              newObj.addAttribute(new InstantiatedAttribute(
                                  attribute, new Integer(ranInt)));
                              String warning = ("Key attribute value not instantiated for start state object: "
                                  + newObj.getSimSEObjectType().getName()
                                  + " "
                                  + SimSEObjectTypeTypes.getText(newObj
                                      .getSimSEObjectType().getType())
                                  + "; random integer " + ranInt + " assigned");
                              warnings.add(warning);
                            }

                            else if (attribute.getType() == AttributeTypes.DOUBLE) {
                              boolean dblTaken = true;
                              double ranDbl = 0;
                              while (dblTaken) {
                                ranDbl = ranNumGen.nextDouble();
                                if (objects.getAllObjectsOfType(
                                    newObj.getSimSEObjectType()).size() == 0) {
                                  dblTaken = false;
                                } else {
                                  // check if ranDbl is already taken:
                                  Vector objs = objects.getAllObjects();
                                  for (int i = 0; i < objs.size(); i++) {
                                    SimSEObject obj2 = (SimSEObject) objs
                                        .elementAt(i);
                                    if ((obj2 != newObj)
                                        && (obj2.getSimSEObjectType().getName()
                                            .equals(newObj.getSimSEObjectType()
                                                .getName()))
                                        && (obj2.getSimSEObjectType().getType() == newObj
                                            .getSimSEObjectType().getType())) // not
                                                                              // the
                                                                              // object
                                    // in question, but same object type
                                    {
                                      if ((obj2.getKey().isInstantiated())
                                          && (obj2.getKey().getValue() instanceof Double)
                                          && (((Double) (obj2.getKey()
                                              .getValue())).doubleValue() == ranDbl)) {
                                        dblTaken = true;
                                        break;
                                      } else {
                                        dblTaken = false;
                                      }
                                    }
                                  }
                                }
                              }
                              // set the value to the random double:
                              newObj.addAttribute(new InstantiatedAttribute(
                                  attribute, new Double(ranDbl)));
                              String warning = ("Key attribute value not instantiated for start state object: "
                                  + newObj.getSimSEObjectType().getName()
                                  + " "
                                  + SimSEObjectTypeTypes.getText(newObj
                                      .getSimSEObjectType().getType())
                                  + "; random double " + ranDbl + " assigned");
                              warnings.add(warning);
                            }
                          }

                          else // not key
                          {
                            newObj.addAttribute(new InstantiatedAttribute(
                                attribute)); // create and add a new
                            // instantiated attribute with no value
                            reader.readLine(); // read in the
                                               // END_INSTANTIATED_ATTRIBUTE tag
                          }
                        } else // attribute has a value
                        {
                          if (attribute.getType() == AttributeTypes.INTEGER) // integer
                                                                             // attribute
                          {
                            try {
                              Integer intVal = new Integer(value);
                              boolean valid = true;
                              if (((NumericalAttribute) attribute)
                                  .isMinBoundless() == false) // has a min val
                              {
                                if (intVal.intValue() < ((NumericalAttribute) attribute)
                                    .getMinValue().intValue()) {
                                  valid = false;
                                  if (!attribute.isKey()) // not key
                                  {
                                    warnings
                                        .add("Min val of attribute "
                                            + attName
                                            + " for created object of type "
                                            + SimSEObjectTypeTypes
                                                .getText(Integer
                                                    .parseInt(metaType))
                                            + " "
                                            + type
                                            + " changed to "
                                            + ((NumericalAttribute) attribute)
                                                .getMinValue().intValue()
                                            + " -- value "
                                            + intVal.intValue()
                                            + " now not within range -- ignoring "
                                            + " this attribute value in created object of this type");
                                  }

                                }
                              }
                              if (((NumericalAttribute) attribute)
                                  .isMaxBoundless() == false) // has a max val
                              {
                                if (intVal.intValue() > ((NumericalAttribute) attribute)
                                    .getMaxValue().intValue()) {
                                  valid = false;
                                  if (!attribute.isKey()) // not key
                                  {
                                    warnings
                                        .add("Max val of attribute "
                                            + attName
                                            + " for created object of type "
                                            + SimSEObjectTypeTypes
                                                .getText(Integer
                                                    .parseInt(metaType))
                                            + " "
                                            + type
                                            + " changed to "
                                            + ((NumericalAttribute) attribute)
                                                .getMaxValue().intValue()
                                            + " -- value "
                                            + intVal.intValue()
                                            + " now not within range -- ignoring "
                                            + " this attribute in created object of this type");
                                  }
                                }
                              }
                              if (valid) {
                                newObj.addAttribute(new InstantiatedAttribute(
                                    attribute, intVal));
                                reader.readLine(); // read in the
                                                   // END_INSTANTIATED_ATTRIBUTE
                                                   // tag
                              } else // invalid
                              {
                                if (attribute.isKey()) // this attribute is key,
                                                       // so have to assign
                                                       // random value to this
                                                       // attribute:
                                {
                                  boolean intTaken = true;
                                  int ranInt = 0;
                                  while (intTaken) {
                                    ranInt = ranNumGen.nextInt(999999);
                                    if (objects.getAllObjectsOfType(
                                        newObj.getSimSEObjectType()).size() == 0) {
                                      intTaken = false;
                                    } else {
                                      // check if ranInt is already taken:
                                      Vector objs = objects.getAllObjects();
                                      for (int i = 0; i < objs.size(); i++) {
                                        SimSEObject obj2 = (SimSEObject) objs
                                            .elementAt(i);
                                        if ((obj2 != newObj)
                                            && (obj2.getSimSEObjectType() == newObj
                                                .getSimSEObjectType())) // not
                                                                        // the
                                        // object in question, but same object
                                        // type
                                        {
                                          if ((obj2.getKey().isInstantiated())
                                              && (obj2.getKey().getValue() instanceof Integer)
                                              && (((Integer) (obj2.getKey()
                                                  .getValue())).intValue() == ranInt)) {
                                            intTaken = true;
                                          } else {
                                            intTaken = false;
                                          }
                                        }
                                      }
                                    }
                                  }

                                  // check if random int is within min/max
                                  // values:
                                  boolean withinRange = false;
                                  while (!withinRange) {
                                    withinRange = true;
                                    if (((NumericalAttribute) attribute)
                                        .isMinBoundless() == false) // has a min
                                                                    // val
                                    {
                                      if (ranInt < ((NumericalAttribute) attribute)
                                          .getMinValue().intValue()) {
                                        withinRange = false;
                                      }
                                    }
                                    if (((NumericalAttribute) attribute)
                                        .isMaxBoundless() == false) // has a max
                                                                    // val
                                    {
                                      if (ranInt > ((NumericalAttribute) attribute)
                                          .getMaxValue().intValue()) {
                                        withinRange = false;
                                      }
                                    }
                                    if (!withinRange) {
                                      // generate new random int:
                                      ranInt = ranNumGen.nextInt(999999);
                                    }
                                  }

                                  // set the value to the random int:
                                  newObj
                                      .addAttribute(new InstantiatedAttribute(
                                          attribute, new Integer(ranInt)));
                                  String warning = ("Key attribute value not instantiated for start state object: "
                                      + newObj.getSimSEObjectType().getName()
                                      + " "
                                      + SimSEObjectTypeTypes.getText(newObj
                                          .getSimSEObjectType().getType())
                                      + "; random integer " + ranInt + " assigned");
                                  warnings.add(warning);
                                }

                                else // attribute not key
                                {
                                  newObj
                                      .addAttribute(new InstantiatedAttribute(
                                          attribute)); // create and add
                                  // a new instantiated attribute with no value
                                  reader.readLine(); // read in
                                                     // END_INSTANTIATED_ATTRIBUTE
                                                     // tag
                                }
                              }
                            } catch (NumberFormatException e) // this attribute
                                                              // has been
                                                              // changed to int
                                                              // from another
                                                              // type
                            {
                              if (attribute.isKey()) // this attribute is key,
                                                     // so have to assign random
                                                     // value to attribute:
                              {
                                boolean intTaken = true;
                                int ranInt = 0;
                                while (intTaken) {
                                  ranInt = ranNumGen.nextInt(999999);
                                  if (objects.getAllObjectsOfType(
                                      newObj.getSimSEObjectType()).size() == 0) {
                                    intTaken = false;
                                  } else {
                                    // check if ranInt is already taken:
                                    Vector objs = objects.getAllObjects();
                                    for (int i = 0; i < objs.size(); i++) {
                                      SimSEObject obj2 = (SimSEObject) objs
                                          .elementAt(i);
                                      if ((obj2 != newObj)
                                          && (obj2.getSimSEObjectType() == newObj
                                              .getSimSEObjectType())) // not the
                                      // object in question, but same object
                                      // type
                                      {
                                        if ((obj2.getKey().isInstantiated())
                                            && (obj2.getKey().getValue() instanceof Integer)
                                            && (((Integer) (obj2.getKey()
                                                .getValue())).intValue() == ranInt)) {
                                          intTaken = true;
                                        } else {
                                          intTaken = false;
                                        }
                                      }
                                    }
                                  }
                                }

                                // check if random int is within min/max values:
                                boolean withinRange = false;
                                while (!withinRange) {
                                  withinRange = true;
                                  if (((NumericalAttribute) attribute)
                                      .isMinBoundless() == false) // has a min
                                                                  // val
                                  {
                                    if (ranInt < ((NumericalAttribute) attribute)
                                        .getMinValue().intValue()) {
                                      withinRange = false;
                                    }
                                  }
                                  if (((NumericalAttribute) attribute)
                                      .isMaxBoundless() == false) // has a max
                                                                  // val
                                  {
                                    if (ranInt > ((NumericalAttribute) attribute)
                                        .getMaxValue().intValue()) {
                                      withinRange = false;
                                    }
                                  }
                                  if (!withinRange) {
                                    // generate new random int:
                                    ranInt = ranNumGen.nextInt(999999);
                                  }
                                }

                                // set the value to the random int:
                                newObj.addAttribute(new InstantiatedAttribute(
                                    attribute, new Integer(ranInt)));
                                String warning = ("Key attribute value type changed for start state object: "
                                    + newObj.getSimSEObjectType().getName()
                                    + " "
                                    + SimSEObjectTypeTypes.getText(newObj
                                        .getSimSEObjectType().getType())
                                    + "; old value invalid; new random integer "
                                    + ranInt + " assigned");
                                warnings.add(warning);
                              } else // not key
                              {
                                warnings
                                    .add("Attribute "
                                        + attribute.getName()
                                        + "type for object type "
                                        + SimSEObjectTypeTypes.getText(Integer
                                            .parseInt(metaType))
                                        + " "
                                        + type
                                        + " changed -- value for start state object of this type no longer valid -- "
                                        + " ignoring attribute value");
                              }
                            }
                          }

                          else if (attribute.getType() == AttributeTypes.DOUBLE) // double
                                                                                 // attribute
                          {
                            try {
                              Double doubleVal = new Double(value);
                              boolean valid = true;
                              if (((NumericalAttribute) attribute)
                                  .isMinBoundless() == false) // has a min val
                              {
                                if (doubleVal.doubleValue() < ((NumericalAttribute) attribute)
                                    .getMinValue().doubleValue()) {
                                  valid = false;
                                  if (!attribute.isKey()) // not key
                                  {
                                    warnings
                                        .add("Min val of attribute "
                                            + attName
                                            + " for created object of type "
                                            + SimSEObjectTypeTypes
                                                .getText(Integer
                                                    .parseInt(metaType))
                                            + " "
                                            + type
                                            + " changed to "
                                            + ((NumericalAttribute) attribute)
                                                .getMinValue().doubleValue()
                                            + " -- value "
                                            + doubleVal.doubleValue()
                                            + " now not within range -- ignoring "
                                            + " this attribute in created object of this type");
                                  }
                                }
                              }
                              if (((NumericalAttribute) attribute)
                                  .isMaxBoundless() == false) // has a max val
                              {
                                if (doubleVal.doubleValue() > ((NumericalAttribute) attribute)
                                    .getMaxValue().doubleValue()) {
                                  valid = false;
                                  if (!attribute.isKey()) // not key
                                  {
                                    warnings
                                        .add("Max val of attribute "
                                            + attName
                                            + " for created object of type "
                                            + SimSEObjectTypeTypes
                                                .getText(Integer
                                                    .parseInt(metaType))
                                            + " "
                                            + type
                                            + " changed to "
                                            + ((NumericalAttribute) attribute)
                                                .getMaxValue().doubleValue()
                                            + " -- value "
                                            + doubleVal.doubleValue()
                                            + " now not within range -- ignoring "
                                            + " this attribute in created object of this type");
                                  }
                                }
                              }
                              if (valid) {
                                newObj.addAttribute(new InstantiatedAttribute(
                                    attribute, doubleVal));
                                reader.readLine(); // read in the
                                                   // END_INSTANTIATED_ATTRIBUTE
                                                   // tag
                              } else // invalid
                              {
                                if (attribute.isKey()) // this attribute is key,
                                                       // so have to assign
                                                       // random value to
                                                       // attribute:
                                {
                                  boolean dblTaken = true;
                                  double ranDbl = 0;
                                  while (dblTaken) {
                                    ranDbl = ranNumGen.nextDouble();
                                    if (objects.getAllObjectsOfType(
                                        newObj.getSimSEObjectType()).size() == 0) {
                                      dblTaken = false;
                                    } else {
                                      // check if ranDbl is already taken:
                                      Vector objs = objects.getAllObjects();
                                      for (int i = 0; i < objs.size(); i++) {
                                        SimSEObject obj2 = (SimSEObject) objs
                                            .elementAt(i);
                                        if ((obj2 != newObj)
                                            && (obj2.getSimSEObjectType()
                                                .getName()
                                                .equals(newObj
                                                    .getSimSEObjectType()
                                                    .getName()))
                                            && (obj2.getSimSEObjectType()
                                                .getType() == newObj
                                                .getSimSEObjectType().getType())) // not
                                                                                  // the
                                        // object in question, but same object
                                        // type
                                        {
                                          if ((obj2.getKey().isInstantiated())
                                              && (obj2.getKey().getValue() instanceof Double)
                                              && (((Double) (obj2.getKey()
                                                  .getValue())).doubleValue() == ranDbl)) {
                                            dblTaken = true;
                                            break;
                                          } else {
                                            dblTaken = false;
                                          }
                                        }
                                      }
                                    }
                                  }
                                  // set the value to the random double:
                                  newObj
                                      .addAttribute(new InstantiatedAttribute(
                                          attribute, new Double(ranDbl)));
                                  String warning = ("Key attribute value not instantiated for start state object: "
                                      + newObj.getSimSEObjectType().getName()
                                      + " "
                                      + SimSEObjectTypeTypes.getText(newObj
                                          .getSimSEObjectType().getType())
                                      + "; random double " + ranDbl + " assigned");
                                  warnings.add(warning);
                                } else // attribute not key
                                {
                                  newObj
                                      .addAttribute(new InstantiatedAttribute(
                                          attribute)); // create and add
                                  // a new instantiated attribute with no value
                                  reader.readLine(); // read in
                                                     // END_INSTANTIATED_ATTRIBUTE
                                                     // tag
                                }
                              }
                            } catch (NumberFormatException e) // this attribute
                                                              // has been
                                                              // changed to
                                                              // double from
                                                              // another type
                            {
                              if (attribute.isKey()) // this attribute is key,
                                                     // so have to assign random
                                                     // value to attribute:
                              {
                                boolean dblTaken = true;
                                double ranDbl = 0;
                                while (dblTaken) {
                                  ranDbl = ranNumGen.nextDouble();
                                  if (objects.getAllObjectsOfType(
                                      newObj.getSimSEObjectType()).size() == 0) {
                                    dblTaken = false;
                                  } else {
                                    // check if ranDbl is already taken:
                                    Vector objs = objects.getAllObjects();
                                    for (int i = 0; i < objs.size(); i++) {
                                      SimSEObject obj2 = (SimSEObject) objs
                                          .elementAt(i);
                                      if ((obj2 != newObj)
                                          && (obj2.getSimSEObjectType()
                                              .getName().equals(newObj
                                              .getSimSEObjectType().getName()))
                                          && (obj2.getSimSEObjectType()
                                              .getType() == newObj
                                              .getSimSEObjectType().getType())) // not
                                                                                // the
                                      // object in question, but same object
                                      // type
                                      {
                                        if ((obj2.getKey().isInstantiated())
                                            && (obj2.getKey().getValue() instanceof Double)
                                            && (((Double) (obj2.getKey()
                                                .getValue())).doubleValue() == ranDbl)) {
                                          dblTaken = true;
                                          break;
                                        } else {
                                          dblTaken = false;
                                        }
                                      }
                                    }
                                  }
                                }
                                // set the value to the random double:
                                newObj.addAttribute(new InstantiatedAttribute(
                                    attribute, new Double(ranDbl)));
                                String warning = ("Key attribute value type changed for start state object: "
                                    + newObj.getSimSEObjectType().getName()
                                    + " "
                                    + SimSEObjectTypeTypes.getText(newObj
                                        .getSimSEObjectType().getType())
                                    + "; old value invalid; random double "
                                    + ranDbl + " assigned");
                                warnings.add(warning);
                              } else // not key
                              {
                                warnings
                                    .add("Attribute type changed for "
                                        + attribute.getName()
                                        + " attribute for object type "
                                        + SimSEObjectTypeTypes.getText(Integer
                                            .parseInt(metaType))
                                        + " "
                                        + type
                                        + " changed -- value for created object of this type no longer valid -- "
                                        + " ignoring attribute value");
                              }
                            }
                          } else if (attribute.getType() == AttributeTypes.STRING) // string
                                                                                   // attribute
                          {
                            newObj.addAttribute(new InstantiatedAttribute(
                                attribute, value));
                            reader.readLine(); // read in the
                                               // END_INSTANTIATED_ATTRIBUTE tag
                          } else if (attribute.getType() == AttributeTypes.BOOLEAN) // boolean
                                                                                    // attribute
                          {
                            newObj.addAttribute(new InstantiatedAttribute(
                                attribute, new Boolean(value)));
                            reader.readLine(); // read in the
                                               // END_INSTANTIATED_ATTRIBUTE tag
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      reader.close();
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(null,
          ("Cannot find model file " + inputFile.getPath()), "File Not Found",
          JOptionPane.WARNING_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error reading model file: "
          + inputFile.getPath() + "! " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
    return warnings;
  }
}