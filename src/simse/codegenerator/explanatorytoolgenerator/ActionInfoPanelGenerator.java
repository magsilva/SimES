/*
 * This class is responsible for generating all of the code for the
 * ActionInfoPanel class in the explanatory tool
 */

package simse.codegenerator.explanatorytoolgenerator;

import simse.codegenerator.CodeGeneratorConstants;
import simse.modelbuilder.actionbuilder.ActionType;
import simse.modelbuilder.actionbuilder.ActionTypeDestroyer;
import simse.modelbuilder.actionbuilder.ActionTypeParticipant;
import simse.modelbuilder.actionbuilder.ActionTypeTrigger;
import simse.modelbuilder.actionbuilder.DefinedActionTypes;
import simse.modelbuilder.objectbuilder.Attribute;
import simse.modelbuilder.objectbuilder.AttributeTypes;
import simse.modelbuilder.objectbuilder.SimSEObjectType;
import simse.modelbuilder.objectbuilder.SimSEObjectTypeTypes;

import java.io.*;
import java.util.Vector;

import javax.swing.*;

public class ActionInfoPanelGenerator implements CodeGeneratorConstants {
  private File directory; // directory to save generated code into
  private DefinedActionTypes actTypes;

  public ActionInfoPanelGenerator(DefinedActionTypes actTypes, File dir) {
    this.actTypes = actTypes;
    directory = dir;
  }

  public void generate() {
    File actInfoFile = new File(directory,
        ("simse\\explanatorytool\\ActionInfoPanel.java"));
    if (actInfoFile.exists()) {
      actInfoFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(actInfoFile);
      writer
          .write("/* File generated by: simse.codegenerator.explanatorytoolgenerator.ActionInfoPanelGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.explanatorytool;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import simse.adts.actions.*;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.objects.*;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import javax.swing.*;");
      writer.write(NEWLINE);
      writer.write("import javax.swing.event.ListSelectionEvent;");
      writer.write(NEWLINE);
      writer.write("import javax.swing.event.ListSelectionListener;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import java.awt.Dimension;");
      writer.write(NEWLINE);
      writer.write("import java.util.Vector;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("public class ActionInfoPanel extends JPanel implements ListSelectionListener {");
      writer.write(NEWLINE);

      // member variables:
      writer
          .write("private simse.adts.actions.Action action; // action in focus");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("private JList triggerList; // for choosing which trigger show");
      writer.write(NEWLINE);
      writer
          .write("private JList destroyerList; // for choosing which destroyer to show");
      writer.write(NEWLINE);
      writer
          .write("private JTextArea descriptionArea; // for displaying a trigger/destroyer description");
      writer.write(NEWLINE);
      writer.write("private JTextArea actionDescriptionArea; // for displaying the actoin description");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("private final int TRIGGER = 0;");
      writer.write(NEWLINE);
      writer.write("private final int DESTROYER = 1;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer
          .write("public ActionInfoPanel(simse.adts.actions.Action action) {");
      writer.write(NEWLINE);
      writer.write("this.action = action;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// Create main panel (box):");
      writer.write(NEWLINE);
      writer.write("Box mainPane = Box.createVerticalBox();");
      writer.write(NEWLINE);
      writer.write("mainPane.setPreferredSize(new Dimension(900, 550));");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// Create actionDescription pane and components:");
      writer.write(NEWLINE);
      writer.write("Box actionDescriptionPane = Box.createVerticalBox();");
      writer.write(NEWLINE);
      writer.write("JPanel actionDescriptionTitlePane = new JPanel();");
      writer.write(NEWLINE);
      writer.write("actionDescriptionTitlePane.add(new JLabel(\"ActionDescription:\"));");
      writer.write(NEWLINE);
      writer.write("actionDescriptionPane.add(actionDescriptionTitlePane);");
      writer.write(NEWLINE);
      writer.write("actionDescriptionArea = new JTextArea(1, 50);");
      writer.write(NEWLINE);
      writer.write("actionDescriptionArea.setLineWrap(true);");
      writer.write(NEWLINE);
      writer.write("actionDescriptionArea.setWrapStyleWord(true);");
      writer.write(NEWLINE);
      writer.write("actionDescriptionArea.setEditable(false);");
      writer.write(NEWLINE);
      writer.write("JScrollPane actionDescriptionScrollPane = new JScrollPane(actionDescriptionArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);");
      writer.write(NEWLINE);
      writer.write("initializeActionDescription();");
      writer.write(NEWLINE);
      writer.write("actionDescriptionPane.add(actionDescriptionScrollPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// Create participants pane and components:");
      writer.write(NEWLINE);
      writer.write("Box participantsPane = Box.createVerticalBox();");
      writer.write(NEWLINE);
      writer.write("JPanel participantsTitlePane = new JPanel();");
      writer.write(NEWLINE);
      writer.write("participantsTitlePane.add(new JLabel(\"Participants:\"));");
      writer.write(NEWLINE);
      writer.write("participantsPane.add(participantsTitlePane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// participants table:");
      writer.write(NEWLINE);
      writer
          .write("JScrollPane participantsTablePane = new JScrollPane(createParticipantsTable());");
      writer.write(NEWLINE);
      writer
          .write("participantsTablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);");
      writer.write(NEWLINE);
      writer
          .write("participantsTablePane.setPreferredSize(new Dimension(900, 125));");
      writer.write(NEWLINE);
      writer.write("participantsPane.add(participantsTablePane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// Create triggerDestroyer pane and components:");
      writer.write(NEWLINE);
      writer.write("JPanel triggerDestroyerPane = new JPanel();");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// list pane:");
      writer.write(NEWLINE);
      writer.write("Box listPane = Box.createVerticalBox();");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// trigger list:");
      writer.write(NEWLINE);
      writer.write("JPanel triggerListTitlePane = new JPanel();");
      writer.write(NEWLINE);
      writer.write("triggerListTitlePane.add(new JLabel(\"Triggers:\"));");
      writer.write(NEWLINE);
      writer.write("listPane.add(triggerListTitlePane);");
      writer.write(NEWLINE);
      writer.write("triggerList = new JList();");
      writer.write(NEWLINE);
      writer.write("triggerList.setVisibleRowCount(3);");
      writer.write(NEWLINE);
      writer.write("triggerList.setFixedCellWidth(400);");
      writer.write(NEWLINE);
      writer
          .write("triggerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);");
      writer.write(NEWLINE);
      writer.write("triggerList.addListSelectionListener(this);");
      writer.write(NEWLINE);
      writer.write("initializeTriggerList();");
      writer.write(NEWLINE);
      writer
          .write("JScrollPane triggerListPane = new JScrollPane(triggerList);");
      writer.write(NEWLINE);
      writer.write("listPane.add(triggerListPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// destroyer list:");
      writer.write(NEWLINE);
      writer.write("JPanel destroyerListTitlePane = new JPanel();");
      writer.write(NEWLINE);
      writer.write("destroyerListTitlePane.add(new JLabel(\"Destroyers:\"));");
      writer.write(NEWLINE);
      writer.write("listPane.add(destroyerListTitlePane);");
      writer.write(NEWLINE);
      writer.write("destroyerList = new JList();");
      writer.write(NEWLINE);
      writer.write("destroyerList.setVisibleRowCount(3);");
      writer.write(NEWLINE);
      writer.write("destroyerList.setFixedCellWidth(400);");
      writer.write(NEWLINE);
      writer
          .write("destroyerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);");
      writer.write(NEWLINE);
      writer.write("destroyerList.addListSelectionListener(this);");
      writer.write(NEWLINE);
      writer.write("initializeDestroyerList();");
      writer.write(NEWLINE);
      writer
          .write("JScrollPane destroyerListPane = new JScrollPane(destroyerList);");
      writer.write(NEWLINE);
      writer.write("listPane.add(destroyerListPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("triggerDestroyerPane.add(listPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// description pane:");
      writer.write(NEWLINE);
      writer.write("Box descriptionPane = Box.createVerticalBox();");
      writer.write(NEWLINE);
      writer.write("JPanel descriptionTitlePane = new JPanel();");
      writer.write(NEWLINE);
      writer.write("descriptionTitlePane.add(new JLabel(\"Description:\"));");
      writer.write(NEWLINE);
      writer.write("descriptionPane.add(descriptionTitlePane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// description text area:");
      writer.write(NEWLINE);
      writer.write("descriptionArea = new JTextArea(9, 30);");
      writer.write(NEWLINE);
      writer.write("descriptionArea.setLineWrap(true);");
      writer.write(NEWLINE);
      writer.write("descriptionArea.setWrapStyleWord(true);");
      writer.write(NEWLINE);
      writer.write("descriptionArea.setEditable(false);");
      writer.write(NEWLINE);
      writer
          .write("JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);");
      writer.write(NEWLINE);
      writer.write("descriptionPane.add(descriptionScrollPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("triggerDestroyerPane.add(descriptionPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// Add panes to main pane:");
      writer.write(NEWLINE);
      writer.write("mainPane.add(actionDescriptionPane);");
      writer.write(NEWLINE);
      writer.write("mainPane.add(participantsPane);");
      writer.write(NEWLINE);
      writer.write("mainPane.add(triggerDestroyerPane);");
      writer.write(NEWLINE);
      writer.write("add(mainPane);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// Set main window frame properties:");
      writer.write(NEWLINE);
      writer.write("setOpaque(true);");
      writer.write(NEWLINE);
      writer.write("validate();");
      writer.write(NEWLINE);
      writer.write("repaint();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "valueChanged" method:
      writer.write("// responds to list selections");
      writer.write(NEWLINE);
      writer.write("public void valueChanged(ListSelectionEvent e) {");
      writer.write(NEWLINE);
      writer
          .write("if (e.getSource() == triggerList && triggerList.getSelectedIndex() >= 0) {");
      writer.write(NEWLINE);
      writer.write("refreshDescriptionArea(TRIGGER);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// clear selection for destroyer list:");
      writer.write(NEWLINE);
      writer.write("destroyerList.clearSelection();");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer
          .write("else if (e.getSource() == destroyerList && destroyerList.getSelectedIndex() >= 0) {");
      writer.write(NEWLINE);
      writer.write("refreshDescriptionArea(DESTROYER);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// clear selection for trigger list:");
      writer.write(NEWLINE);
      writer.write("triggerList.clearSelection();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "initializeActionDescription" method:
      writer.write("// initializes the action description");
      writer.write(NEWLINE);
      writer.write("private void initializeActionDescription() {");
      writer.write(NEWLINE);
      writer.write("String text = \"\";");
      writer.write(NEWLINE);
      
      // go through all actions:
      Vector actions = actTypes.getAllActionTypes();
      boolean writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        if (act.isVisibleInExplanatoryTool()) {
          String uCaseName = getUpperCaseLeading(act.getName());
          if (writeElse) {
            writer.write("else ");
          } else {
            writeElse = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write("text = \"" + 
              act.getAnnotation().replaceAll("\n", "\\\\n").
              replaceAll("\"", "\\\\\"") + "\";");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
        }
      }
      writer.write("actionDescriptionArea.setText(text);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "initializeTriggerList" method:
      writer.write("// initializes the JList of triggers");
      writer.write(NEWLINE);
      writer.write("private void initializeTriggerList() {");
      writer.write(NEWLINE);

      // go through all actions:
      writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        if (act.isVisibleInExplanatoryTool()) {
          String uCaseName = getUpperCaseLeading(act.getName());
          if (writeElse) {
            writer.write("else ");
          } else {
            writeElse = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write("String [] list = {");
          writer.write(NEWLINE);
          Vector triggers = act.getAllTriggers();
          for (int j = 0; j < triggers.size(); j++) {
            ActionTypeTrigger trigger = (ActionTypeTrigger) triggers.get(j);
            writer.write("\"" + trigger.getName() + "\",");
            writer.write(NEWLINE);
          }
          writer.write("};");
          writer.write(NEWLINE);
          writer.write("triggerList.setListData(list);");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "initializeDestroyerList" method:
      writer.write("// initializes the JList of destroyers");
      writer.write(NEWLINE);
      writer.write("private void initializeDestroyerList() {");
      writer.write(NEWLINE);

      // go through all actions:
      writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        if (act.isVisibleInExplanatoryTool()) {
          String uCaseName = getUpperCaseLeading(act.getName());
          if (writeElse) {
            writer.write("else ");
          } else {
            writeElse = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write("String [] list = {");
          writer.write(NEWLINE);
          Vector destroyers = act.getAllDestroyers();
          for (int j = 0; j < destroyers.size(); j++) {
            ActionTypeDestroyer destroyer = (ActionTypeDestroyer) destroyers
                .get(j);
            writer.write("\"" + destroyer.getName() + "\",");
            writer.write(NEWLINE);
          }
          writer.write("};");
          writer.write(NEWLINE);
          writer.write("destroyerList.setListData(list);");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "createParticipantsTable" method:
      writer.write("private JTable createParticipantsTable() {");
      writer.write(NEWLINE);
      writer
          .write("String[] columnNames = { \"Participant Name\", \"Participant\", \"Status\" };");
      writer.write(NEWLINE);
      writer
          .write("Object[][] data = new Object[action.getAllParticipants().size()][3];");
      writer.write(NEWLINE);
      writer.write("int index = 0;");
      writer.write(NEWLINE);

      // go through all actions:
      writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        String uCaseName = getUpperCaseLeading(act.getName());
        if (act.isVisibleInExplanatoryTool()) {
          if (writeElse) {
            writer.write("else ");
          } else {
            writeElse = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write(NEWLINE);

          // go through all participants:
          Vector participants = act.getAllParticipants();
          for (int j = 0; j < participants.size(); j++) {
            ActionTypeParticipant part = (ActionTypeParticipant) participants
                .get(j);
            String lCasePartName = part.getName().toLowerCase();
            writer.write(" // " + part.getName() + " participant:");
            writer.write(NEWLINE);
            writer.write("Vector " + lCasePartName + "s = ((" + uCaseName
                + "Action)action).getAll" + part.getName() + "s();");
            writer.write(NEWLINE);
            writer
                .write("Vector active" + part.getName() + "s = ((" + uCaseName
                    + "Action)action).getAllActive" + part.getName() + "s();");
            writer.write(NEWLINE);
            writer.write("for (int i = 0; i < " + lCasePartName
                + "s.size(); i++) {");
            writer.write(NEWLINE);
            String metaType = SimSEObjectTypeTypes.getText(part
                .getSimSEObjectTypeType());
            writer.write(metaType + " " + lCasePartName + " = (" + metaType
                + ")" + lCasePartName + "s.get(i);");
            writer.write(NEWLINE);
            writer.write("data[index][0] = \"" + part.getName() + "\";");
            writer.write(NEWLINE);

            // go through all allowable SimSEObjectTypes:
            Vector types = part.getAllSimSEObjectTypes();
            for (int k = 0; k < types.size(); k++) {
              SimSEObjectType type = (SimSEObjectType) types.get(k);
              String uCaseTypeName = getUpperCaseLeading(type.getName());
              if (k < 0) {
                writer.write("else ");
              }
              writer.write("if (" + lCasePartName + " instanceof "
                  + uCaseTypeName + ") {");
              writer.write(NEWLINE);
              Attribute keyAtt = type.getKey();
              String uCaseAttName = getUpperCaseLeading(keyAtt.getName());
              writer.write("data[index][1] = \"" + type.getName() + " "
                  + metaType + " \" + ((" + uCaseTypeName + ")" + lCasePartName
                  + ").get" + uCaseAttName + "();");
              writer.write(NEWLINE);
              writer.write(NEWLINE);
              writer.write("// find out whether it's active or not:");
              writer.write(NEWLINE);
              writer.write("boolean active = false;");
              writer.write(NEWLINE);
              writer.write("for (int j = 0; j < active" + part.getName()
                  + "s.size(); j++) {");
              writer.write(NEWLINE);
              writer.write(metaType + " active" + part.getName() + " = ("
                  + metaType + ")active" + part.getName() + "s.get(j);");
              writer.write(NEWLINE);
              writer.write("if ((active" + part.getName() + " instanceof "
                  + uCaseTypeName + ") && ((" + uCaseTypeName + ")active"
                  + part.getName() + ").get");
              writer.write(uCaseAttName + "()");
              if (keyAtt.getType() == AttributeTypes.STRING) {
                writer.write(".equals(");
              } else { // non-string
                writer.write(" == ");
              }
              writer.write("((" + uCaseTypeName + ")" + lCasePartName + ").get"
                  + uCaseAttName + "())");
              if (keyAtt.getType() == AttributeTypes.STRING) {
                writer.write(")");
              }
              writer.write(" {");
              writer.write(NEWLINE);
              writer.write("active = true;");
              writer.write(NEWLINE);
              writer.write("break;");
              writer.write(NEWLINE);
              writer.write(CLOSED_BRACK);
              writer.write(NEWLINE);
              writer.write(CLOSED_BRACK);
              writer.write(NEWLINE);
              writer
                  .write("data[index][2] = active ? \"Active\" : \"Inactive\";");
              writer.write(NEWLINE);
              writer.write(CLOSED_BRACK);
              writer.write(NEWLINE);
            }
            writer.write("index++;");
            writer.write(NEWLINE);
            writer.write(CLOSED_BRACK);
            writer.write(NEWLINE);
          }
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
      }
      writer.write("return new JTable(data, columnNames);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "refreshDescriptionArea" method:
      writer
          .write("// refreshes the description area with the selected trigger/destroyer");
      writer.write(NEWLINE);
      writer.write("private void refreshDescriptionArea(int trigOrDest) {");
      writer.write(NEWLINE);
      writer
          .write("String name = trigOrDest == TRIGGER ? (String) triggerList.getSelectedValue() : (String) destroyerList.getSelectedValue();");
      writer.write(NEWLINE);
      writer.write("if (name != null) {");
      writer.write(NEWLINE);
      writer.write("String text = \"\";");
      writer.write(NEWLINE);

      // go through all actions:
      writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        String uCaseName = getUpperCaseLeading(act.getName());
        String capsName = act.getName().toUpperCase();
        if (act.isVisibleInExplanatoryTool()) {
          if (writeElse) {
            writer.write("else ");
          } else {
            writeElse = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write("// triggers:");
          writer.write(NEWLINE);

          // go through all triggers:
          Vector triggers = act.getAllTriggers();
          for (int j = 0; j < triggers.size(); j++) {
            ActionTypeTrigger trigger = (ActionTypeTrigger) triggers.get(j);
            String triggerCapsName = trigger.getName().toUpperCase();
            if (j > 0) {
              writer.write("else ");
            }
            writer.write("if (trigOrDest == TRIGGER && name.equals(\""
                + trigger.getName() + "\")) {");
            writer.write(NEWLINE);
            writer.write("text = TriggerDescriptions." + capsName + "_"
                + triggerCapsName + ";");
            writer.write(NEWLINE);
            writer.write(CLOSED_BRACK);
            writer.write(NEWLINE);
          }

          // go through all destroyers:
          Vector destroyers = act.getAllDestroyers();
          for (int j = 0; j < destroyers.size(); j++) {
            ActionTypeDestroyer destroyer = (ActionTypeDestroyer) destroyers
                .get(j);
            String destroyerCapsName = destroyer.getName().toUpperCase();
            if (j > 0) {
              writer.write("else ");
            }
            writer.write("if (trigOrDest == DESTROYER && name.equals(\""
                + destroyer.getName() + "\")) {");
            writer.write(NEWLINE);
            writer.write("text = DestroyerDescriptions." + capsName + "_"
                + destroyerCapsName + ";");
            writer.write(NEWLINE);
            writer.write(CLOSED_BRACK);
            writer.write(NEWLINE);
          }
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
      }
      writer.write("descriptionArea.setText(text);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);

      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + actInfoFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private String getUpperCaseLeading(String s) {
    return (s.substring(0, 1).toUpperCase() + s.substring(1));
  }
}