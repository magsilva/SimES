/*
 * This class is the GUI responsible for displaying the button pad for
 * specifying effects on attributes for EffectRules with
 */

package simse.modelbuilder.rulebuilder;

import java.awt.event.*;
import java.awt.*;
import java.awt.Dimension;
import javax.swing.*;
import java.util.*;
import java.awt.Color;

public class ButtonPadGUI extends JDialog implements ActionListener {
  // button pad:
  private JButton inputButton; // inputs to the rule
  private JButton attributeThisPartButton; // attributes of the participant in
                                           // focus
  private JButton attributeOtherPartButton; // attributes of the other
                                            // participants in the action
  private JButton numObjectsButton; // numbers of objects in the action
  private JButton numActionsThisPartButton; // number of actions the participant
                                            // in focus is involved in
  private JButton numActionsOtherPartButton; // number of actions the other
                                             // participants in the action are
                                             // involved in
  private Vector timeButtons; // numbers relating to time
  private Vector digitButtons; // 0, 1, 2, 3, ...
  private Vector operatorButtons; // +, -, *, /, (, )
  private Vector otherButtons; // e.g., random(min, max)
  private JTextArea echoedTextField; // text field from the effect rule info
                                     // form to be echoed under the button pad
  private JButton okButton;

  public ButtonPadGUI(JDialog owner, JButton input, JButton attsThis,
      JButton attsOther, JButton numObjects, JButton numActionsThis,
      JButton numActionsOther, Vector time, Vector digits, Vector operators,
      Vector other, JTextArea echoedTField, String attDescription) {
    super(owner, true);

    inputButton = input;
    attributeThisPartButton = attsThis;
    attributeOtherPartButton = attsOther;
    numObjectsButton = numObjects;
    numActionsThisPartButton = numActionsThis;
    numActionsOtherPartButton = numActionsOther;
    timeButtons = time;
    digitButtons = digits;
    operatorButtons = operators;
    otherButtons = other;
    echoedTextField = echoedTField;

    // Set window title:
    setTitle("Button Pad - " + attDescription);

    // Create main panel (box):
    Box mainPane = Box.createVerticalBox();
    mainPane.setPreferredSize(new Dimension(660, 477));

    // Top panel:
    JPanel topPane = new JPanel(new GridLayout(0, 3, 3, 3));
    // add buttons to pane:
    topPane.add(inputButton);
    topPane.add(attributeThisPartButton);
    topPane.add(attributeOtherPartButton);
    topPane.add(numObjectsButton);
    topPane.add(numActionsThisPartButton);
    topPane.add(numActionsOtherPartButton);
    // time buttons:
    for (int i = 0; i < timeButtons.size(); i++) {
      topPane.add((JButton) timeButtons.elementAt(i));
    }

    // Middle panel:
    JPanel middlePane = new JPanel();
    middlePane.add(new JLabel("Constants, operators, other:"));

    // Bottom panel:
    JPanel bottomPane = new JPanel(new GridLayout(0, 4, 3, 3));
    // digit buttons:
    for (int i = 0; i < digitButtons.size(); i++) {
      bottomPane.add((JButton) digitButtons.elementAt(i));
    }
    // operator buttons:
    for (int i = 0; i < operatorButtons.size(); i++) {
      bottomPane.add((JButton) operatorButtons.elementAt(i));
    }
    // other buttons:
    for (int i = 0; i < otherButtons.size(); i++) {
      bottomPane.add((JButton) otherButtons.elementAt(i));
    }

    // Echoed panel:
    JScrollPane echoedPane = new JScrollPane(echoedTextField);
    echoedPane
        .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    echoedPane.setPreferredSize(new Dimension(660, 100));

    // Okay panel:
    JPanel okPane = new JPanel();
    okButton = new JButton("OK");
    okButton.addActionListener(this);
    okPane.add(okButton);

    // Add panes to main pane:
    mainPane.add(topPane);
    JSeparator separator1 = new JSeparator();
    separator1.setMaximumSize(new Dimension(660, 1));
    mainPane.add(separator1);
    mainPane.add(middlePane);
    mainPane.add(bottomPane);
    JSeparator separator2 = new JSeparator();
    separator2.setMaximumSize(new Dimension(660, 2));
    mainPane.add(separator2);
    mainPane.add(echoedPane);
    mainPane.add(okPane);

    // Set main window frame properties:
    setBackground(Color.black);
    setContentPane(mainPane);
    validate();
    pack();
    repaint();
    toFront();
    // Make it show up in the center, lower half of the screen, slightly above
    // center:
    Point ownerLoc = owner.getLocationOnScreen();
    Point thisLoc = new Point();
    thisLoc.setLocation((ownerLoc.getX() + (owner.getWidth() / 2) - (this
        .getWidth() / 2)), (ownerLoc.getY() + (owner.getHeight() / 2) - 200));
    setLocation(thisLoc);
    setVisible(true);
  }

  public void okButtonPressed() {
    setVisible(false);
    dispose();
  }

  public void actionPerformed(ActionEvent evt) // handles user actions
  {
    Object source = evt.getSource();
    if (source == okButton) {
      okButtonPressed();
    }
  }

}