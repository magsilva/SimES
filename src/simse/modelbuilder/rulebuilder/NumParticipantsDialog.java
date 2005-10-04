/*
 * This class defines the dialog for choosing a number of participants in an
 * action for the effect rule info form
 */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.Color;

public class NumParticipantsDialog extends JDialog implements ActionListener {
  private Vector participants; // participants to choose from
  private JTextField fieldInFocus; // text field to edit
  private JTextArea echoedField; // echoed text field in the button gui
  private boolean trimWhitespace; // whether or not to trip the trailing
                                  // whitespace in the text field b4 appending
                                  // to it

  private JComboBox activeList; // list of active statuses
  private JComboBox partList; // list of participants
  private JButton okButton;
  private JButton cancelButton;

  public NumParticipantsDialog(JDialog owner, Vector parts, JTextField tField,
      JTextArea echoedTField, boolean trim) {
    super(owner, true);
    setTitle("Num Participants");

    participants = parts;
    fieldInFocus = tField;
    echoedField = echoedTField;
    trimWhitespace = trim;

    // main pane:
    Box mainPane = Box.createVerticalBox();

    // top pane:
    JPanel topPane = new JPanel();
    topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));
    JPanel statusPane = new JPanel();
    statusPane.add(new JLabel("Choose status:"));
    topPane.add(statusPane);
    activeList = new JComboBox();
    activeList.addItem("All");
    activeList.addItem("Active");
    activeList.addItem("Inactive");
    JPanel activeListPane = new JPanel();
    activeListPane.add(activeList);
    topPane.add(activeListPane);
    JPanel partPane = new JPanel();
    partPane.add(new JLabel("Choose participant:"));
    topPane.add(partPane);
    JPanel partListPane = new JPanel();
    partList = new JComboBox();
    partListPane.add(partList);
    topPane.add(partListPane);
    refreshPartList();

    // bottom pane
    JPanel bottomPane = new JPanel();
    okButton = new JButton("OK");
    okButton.addActionListener(this);
    bottomPane.add(okButton);
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);
    bottomPane.add(cancelButton);

    // Add panes to main pane:
    mainPane.add(topPane);
    mainPane.add(bottomPane);

    // Set main window frame properties:
    setBackground(Color.black);
    setContentPane(mainPane);
    validate();
    pack();
    repaint();
    toFront();
    // Make it show up in the center of the screen:
    Point ownerLoc = owner.getLocationOnScreen();
    Point thisLoc = new Point();
    thisLoc.setLocation((ownerLoc.getX() + (owner.getWidth() / 2) - (this
        .getWidth() / 2)), (ownerLoc.getY() + (owner.getHeight() / 2) - (this
        .getHeight() / 2)));
    setLocation(thisLoc);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent evt) // handles user actions
  {
    Object source = evt.getSource();
    if (source == cancelButton) {
      setVisible(false);
      dispose();
    }

    else if (source == okButton) {
      String active = (String) activeList.getSelectedItem();
      String activeString = new String();
      if (active.equals("All")) {
        activeString = "num";
      } else if (active.equals("Active")) {
        activeString = "numActive";
      } else if (active.equals("Inactive")) {
        activeString = "numInactive";
      }
      String partString = ((String) partList.getSelectedItem()).replace(' ',
          '-');
      // set the text field:
      if (trimWhitespace) {
        setFocusedTextFieldText(fieldInFocus.getText().trim().concat(
            activeString + "-" + partString + " "));
      } else {
        setFocusedTextFieldText(fieldInFocus.getText().concat(
            activeString + "-" + partString + " "));
      }
      setVisible(false);
      dispose();
    }
  }

  private void refreshPartList() {
    for (int i = 0; i < participants.size(); i++) {
      ActionTypeParticipant tempPart = (ActionTypeParticipant) participants
          .elementAt(i);
      // add participant name:
      partList.addItem(tempPart.getName());
      Vector types = tempPart.getAllSimSEObjectTypes();
      for (int j = 0; j < types.size(); j++) // add all of this participant's
                                             // SimSEObjectTypes to the list:
      {
        SimSEObjectType tempType = (SimSEObjectType) types.elementAt(j);
        partList.addItem(tempPart.getName() + " " + tempType.getName() + " "
            + SimSEObjectTypeTypes.getText(tempType.getType()));
      }
    }
  }

  private void setFocusedTextFieldText(String text) // sets both the text fields
                                                    // passed in to the
                                                    // specified text
  {
    fieldInFocus.setText(text);
    echoedField.setText(text);
  }
}