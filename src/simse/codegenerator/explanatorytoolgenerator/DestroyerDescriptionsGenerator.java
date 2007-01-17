/*
 * This class is responsible for generating all of the code for the
 * DestroyerDescriptions class in the explanatory tool
 */

package simse.codegenerator.explanatorytoolgenerator;

import simse.codegenerator.CodeGeneratorConstants;
import simse.modelbuilder.actionbuilder.ActionType;
import simse.modelbuilder.actionbuilder.ActionTypeParticipantAttributeConstraint;
import simse.modelbuilder.actionbuilder.ActionTypeParticipantConstraint;
import simse.modelbuilder.actionbuilder.ActionTypeParticipantDestroyer;
import simse.modelbuilder.actionbuilder.ActionTypeDestroyer;
import simse.modelbuilder.actionbuilder.DefinedActionTypes;
import simse.modelbuilder.actionbuilder.RandomActionTypeDestroyer;
import simse.modelbuilder.actionbuilder.TimedActionTypeDestroyer;
import simse.modelbuilder.actionbuilder.UserActionTypeDestroyer;
import simse.modelbuilder.objectbuilder.AttributeTypes;

import java.io.*;
import java.util.Vector;

import javax.swing.*;

public class DestroyerDescriptionsGenerator implements CodeGeneratorConstants {
  private File directory; // directory to save generated code into
  private DefinedActionTypes actTypes;

  public DestroyerDescriptionsGenerator(DefinedActionTypes actTypes, File dir) {
    this.actTypes = actTypes;
    directory = dir;
  }

  public void generate() {
    File destDescFile = new File(directory,
        ("simse\\explanatorytool\\DestroyerDescriptions.java"));
    if (destDescFile.exists()) {
      destDescFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(destDescFile);
      writer
          .write("/* File generated by: simse.codegenerator.explanatorytoolgenerator.DestroyerDescriptionsGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.explanatorytool;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("public class DestroyerDescriptions {");
      writer.write(NEWLINE);

      // go through all actions:
      Vector actions = actTypes.getAllActionTypes();
      boolean writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        if (act.isVisibleInExplanatoryTool()) {
          Vector destroyers = act.getAllDestroyers();
          for (int j = 0; j < destroyers.size(); j++) {
            ActionTypeDestroyer destroyer = (ActionTypeDestroyer) destroyers
                .get(j);
            writer.write("static final String " + act.getName().toUpperCase()
                + "_" + destroyer.getName().toUpperCase() + " = ");
            writer.write(NEWLINE);
            writer.write("\"This action stops ");
            if (destroyer instanceof TimedActionTypeDestroyer) {
              writer.write("when the action has been occuring for "
                  + ((TimedActionTypeDestroyer) destroyer).getTime()
                  + " clock ticks.");
            } else {
              if (destroyer instanceof RandomActionTypeDestroyer) {
                writer.write(((RandomActionTypeDestroyer) destroyer)
                    .getFrequency()
                    + "% of the time ");
              } else if (destroyer instanceof UserActionTypeDestroyer) {
                writer.write("when the user chooses the menu item \\\""
                    + ((UserActionTypeDestroyer) destroyer).getMenuText()
                    + "\\\" and ");
              }
              writer.write("when the following conditions are met: \\n");

              // go through all participant conditions:
              Vector partDestroyers = destroyer.getAllParticipantDestroyers();
              for (int k = 0; k < partDestroyers.size(); k++) {
                ActionTypeParticipantDestroyer partDestroyer = (ActionTypeParticipantDestroyer) partDestroyers
                    .get(k);
                String partName = partDestroyer.getParticipant().getName();

                // go through all ActionTypeParticipantConstraints for this
                // participant:
                Vector partConstraints = partDestroyer.getAllConstraints();
                for (int m = 0; m < partConstraints.size(); m++) {
                  ActionTypeParticipantConstraint partConstraint = (ActionTypeParticipantConstraint) partConstraints
                      .get(m);
                  String typeName = partConstraint.getSimSEObjectType()
                      .getName();

                  // go through all ActionTypeParticipantAttributeConstraints
                  // for this type:
                  ActionTypeParticipantAttributeConstraint[] attConstraints = partConstraint
                      .getAllAttributeConstraints();
                  for (int n = 0; n < attConstraints.length; n++) {
                    ActionTypeParticipantAttributeConstraint attConstraint = attConstraints[n];
                    String attName = attConstraint.getAttribute().getName();
                    String attGuard = attConstraint.getGuard();
                    if (attConstraint.isConstrained()) {
                      String condVal = attConstraint.getValue().toString();
                      writer.write(partName + "." + attName + " (" + typeName
                          + ") " + attGuard + " ");
                      if (attConstraint.getAttribute().getType() == AttributeTypes.STRING) {
                        writer.write("\\\"" + condVal + "\\\"");
                      } else {
                        writer.write(condVal);
                      }
                      writer.write(" \\n");
                    }
                  }
                }
              }
            }
            writer.write("\";");
            writer.write(NEWLINE);
          }
        }
      }
      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + destDescFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }
}