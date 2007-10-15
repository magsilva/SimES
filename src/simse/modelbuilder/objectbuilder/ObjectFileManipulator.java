/*
 * This class is for generating the DefinedObjectTypes into a file and reading
 * that file into memory
 */

package simse.modelbuilder.objectbuilder;

import java.io.*;
import javax.swing.*;

import simse.modelbuilder.ModelFileManipulator;

public class ObjectFileManipulator {
  private DefinedObjectTypes objects;
  private boolean allowHireFire;

  public ObjectFileManipulator(DefinedObjectTypes objs) {
    objects = objs;
  }

  public boolean isAllowHireFireChecked() {
    return false;
  }

  public void loadFile(File inputFile) // loads the mdl file into memory,
  // filling the "objects" data structure
  // with the data from the file
  {
    objects.clearAll();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      boolean foundBeginningOfObjectTypes = false;
      while (!foundBeginningOfObjectTypes) {
        String currentLine = reader.readLine(); // read in a line of text from
        // the file
        if (currentLine.equals(ModelFileManipulator.BEGIN_ALLOW_HIRE_FIRE_TAG)) {
          allowHireFire = reader.readLine().equals("true");
          reader.readLine(); // remove the end allowHireFire tag
          allowHireFire = false; // removed this functionality, always false now
        }

        if (currentLine.equals(ModelFileManipulator.BEGIN_OBJECT_TYPES_TAG)) // beginning of object
        // types
        {
          foundBeginningOfObjectTypes = true;
          boolean endOfObjectTypes = false;
          while (!endOfObjectTypes) {
            currentLine = reader.readLine();
            if (currentLine.equals(ModelFileManipulator.END_OBJECT_TYPES_TAG)) // end of object types
            {
              endOfObjectTypes = true;
            } else // not end of object types yet
            {
              if (currentLine.equals(ModelFileManipulator.BEGIN_OBJECT_TAG)) {
                SimSEObjectType newObj = new SimSEObjectType(Integer
                    .parseInt(reader.readLine()), reader.readLine()); // create
                // a new
                // object
                // in memory with the type and name specified in the following 2
                // lines of the file
                boolean endOfObj = false;
                while (!endOfObj) {
                  currentLine = reader.readLine(); // get the next line
                  if (currentLine.equals(ModelFileManipulator.END_OBJECT_TAG)) // end of object
                  {
                    endOfObj = true;
                    objects.addObjectType(newObj); // add object type to defined
                    // object types
                  } else if (currentLine.equals(ModelFileManipulator.BEGIN_ATTRIBUTE_TAG)) // beginning
                  // of
                  // attribute
                  {
                    String attName = reader.readLine(); // get the attribute
                    // name
                    int type = Integer.parseInt(reader.readLine()); // get the
                    // attribute
                    // type

                    String visString = reader.readLine(); // get attribute's
                    // visibility
                    boolean visible;
                    if (visString.equals("1")) {
                      visible = true;
                    } else // visString equals 0
                    {
                      visible = false;
                    }

                    String keyString = reader.readLine(); // get attribute's
                    // "key" variable
                    boolean key;
                    if (keyString.equals("1")) {
                      key = true;
                    } else // keyString equals 0
                    {
                      key = false;
                    }

                    String visAtEndString = reader.readLine(); // get
                    // attribute's
                    // visibility on
                    // completion
                    boolean visibleAtEnd;
                    if (visAtEndString.equals("1")) {
                      visibleAtEnd = true;
                    } else // visAtEndString equals 0
                    {
                      visibleAtEnd = false;
                    }

                    if (type == AttributeTypes.INTEGER) // integer attribute
                    {
                      String minValStr = reader.readLine(); // get the minimum
                      // value
                      Integer minVal = null;
                      if (minValStr.equals(ModelFileManipulator.BOUNDLESS) == false) // minimum value
                      // is not
                      // boundless
                      {
                        minVal = new Integer(minValStr);
                      }
                      String maxValStr = reader.readLine(); // get the maximum
                      // value
                      Integer maxVal = null;
                      if (maxValStr.equals(ModelFileManipulator.BOUNDLESS) == false) // maximum value
                      // is not
                      // boundless
                      {
                        maxVal = new Integer(maxValStr);
                      }
                      newObj.addAttribute(new NumericalAttribute(attName, type,
                          visible, key, visibleAtEnd, minVal, maxVal, null,
                          null));
                      // add attribute to object
                    } else if (type == AttributeTypes.DOUBLE) // double
                    // attribute
                    {
                      String minValStr = reader.readLine(); // get the minimum
                      // value
                      Double minVal = null;
                      if (minValStr.equals(ModelFileManipulator.BOUNDLESS) == false) // minimum value
                      // is not
                      // boundless
                      {
                        minVal = new Double(minValStr);
                      }
                      String maxValStr = reader.readLine(); // get the maximum
                      // value
                      Double maxVal = null;
                      if (maxValStr.equals(ModelFileManipulator.BOUNDLESS) == false) // maximum value
                      // is not
                      // boundless
                      {
                        maxVal = new Double(maxValStr);
                      }

                      String minDigStr = reader.readLine(); // get the minimum
                      // num digits
                      Integer minDig = null;
                      if (minDigStr.equals(ModelFileManipulator.BOUNDLESS) == false) // minimum num
                      // digits not
                      // boundless
                      {
                        minDig = new Integer(minDigStr);
                      }
                      String maxDigStr = reader.readLine(); // get the maximum
                      // value
                      Integer maxDig = null;
                      if (maxDigStr.equals(ModelFileManipulator.BOUNDLESS) == false) // maximum num
                      // digits not
                      // boundless
                      {
                        maxDig = new Integer(maxDigStr);
                      }
                      newObj.addAttribute(new NumericalAttribute(attName, type,
                          visible, key, visibleAtEnd, minVal, maxVal, minDig,
                          maxDig));
                      // add attribute to object
                    } else // non-numerical attribute
                    {
                      newObj.addAttribute(new NonNumericalAttribute(attName,
                          type, visible, key, visibleAtEnd)); // add attribute
                      // to
                      // object
                    }
                    reader.readLine(); // read in the END_ATTRIBUTE tag
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
          ("Cannot find object file " + inputFile.getPath()), "File Not Found",
          JOptionPane.WARNING_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error reading file: " + e
          .toString()), "File IO Error", JOptionPane.WARNING_MESSAGE);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, ("Error reading file: " + e
          .toString()), "File IO Error", JOptionPane.WARNING_MESSAGE);
    }
  }
}