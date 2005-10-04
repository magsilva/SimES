/* This class defines the GUI for building rules with */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.*;

import java.awt.event.*;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class RuleBuilderGUI extends JPanel implements ActionListener,
    ListSelectionListener, MouseListener {
  private ModelBuilderGUI mainGUI;
  private DefinedActionTypes actions; // data structure for holding all of the
                                      // created action types
  private DefinedObjectTypes objects; // data structure for holding all of the
                                      // created SimSE object types
  private RuleFileManipulator ruleFileManip; // for saving/loading rule files

  private WarningListPane warningPane;

  private JLabel ruleTableTitle; // label for title of rule table
  private JScrollPane ruleTablePane; // pane for rule table
  private JTable ruleTable; // table for displaying the rules of an action
  private RuleTableModel ruleTblMod; // model for above table
  private JButton editRuleButton; // button for editing an existing rule
  private JButton removeRuleButton; // button for removing a rule
  private JButton renameRuleButton; // button for renaming a rule
  private JButton addEffectRuleButton; // button for adding a new effect rule
  private JButton addCreateObjectsRuleButton; // button for adding a new create
                                              // objects rule
  private JButton addDestroyObjectsRuleButton; // button for adding a new
                                               // destroy objects rule
  private JList definedActionsList; // JList of defined actions

  //private JButton viewEditRulesButton; // button for viewing/editing the
  // rules of an action

  public RuleBuilderGUI(ModelBuilderGUI owner, DefinedObjectTypes objTypes,
      DefinedActionTypes acts) {
    mainGUI = owner;
    actions = acts;
    objects = objTypes;
    ruleFileManip = new RuleFileManipulator(objects, actions);

    // Create main panel (box):
    Box mainPane = Box.createVerticalBox();
    mainPane.setPreferredSize(new Dimension(1024, 650));

    // Create top pane:
    JPanel topPane = new JPanel();

    // Create top left pane:
    Box topLeftPane = Box.createVerticalBox();
    JPanel tableTitlePane = new JPanel();
    ruleTableTitle = new JLabel("No action type selected");
    tableTitlePane.add(ruleTableTitle);
    topLeftPane.add(tableTitlePane);
    // Create rule table pane and table:
    ruleTblMod = new RuleTableModel();
    ruleTable = new JTable(ruleTblMod);
    ruleTable.addMouseListener(this);
    ruleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ruleTablePane = new JScrollPane(ruleTable);
    ruleTablePane
        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    ruleTablePane.setPreferredSize(new Dimension(450, 275));
    setupRuleTableSelectionListenerStuff();
    topLeftPane.add(ruleTablePane);
    topPane.add(topLeftPane);

    // Create top right pane:
    Box topRightPane = Box.createVerticalBox();
    JPanel editRuleButtonPane = new JPanel();
    editRuleButton = new JButton("View/Edit Rule");
    editRuleButton.addActionListener(this);
    editRuleButton.setEnabled(false);
    editRuleButtonPane.add(editRuleButton);
    topRightPane.add(editRuleButtonPane);
    JPanel renameRuleButtonPane = new JPanel();
    renameRuleButton = new JButton("Rename Rule");
    renameRuleButton.addActionListener(this);
    renameRuleButton.setEnabled(false);
    renameRuleButtonPane.add(renameRuleButton);
    topRightPane.add(renameRuleButtonPane);
    JPanel removeRuleButtonPane = new JPanel();
    removeRuleButton = new JButton("Remove Rule");
    removeRuleButton.addActionListener(this);
    removeRuleButton.setEnabled(false);
    removeRuleButtonPane.add(removeRuleButton);
    topRightPane.add(removeRuleButtonPane);
    topPane.add(topRightPane);

    // Create middle pane:
    JPanel middlePane = new JPanel();
    addEffectRuleButton = new JButton("Add New Effect Rule");
    addEffectRuleButton.addActionListener(this);
    addEffectRuleButton.setEnabled(false);
    middlePane.add(addEffectRuleButton);
    addCreateObjectsRuleButton = new JButton("Add New Create Objects Rule");
    addCreateObjectsRuleButton.addActionListener(this);
    addCreateObjectsRuleButton.setEnabled(false);
    middlePane.add(addCreateObjectsRuleButton);
    addDestroyObjectsRuleButton = new JButton("Add New Destroy Objects Rule");
    addDestroyObjectsRuleButton.addActionListener(this);
    addDestroyObjectsRuleButton.setEnabled(false);
    middlePane.add(addDestroyObjectsRuleButton);

    // Create bottom pane:
    JPanel bottomPane = new JPanel();
    bottomPane.add(new JLabel("Actions:"));

    // Create and add defined actions list to a scroll pane:
    definedActionsList = new JList();
    definedActionsList.setVisibleRowCount(7); // make 7 items visible at a time
    definedActionsList.setFixedCellWidth(250);
    definedActionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only
                                                                              // allow
                                                                              // the
                                                                              // user
                                                                              // to
                                                                              // select
                                                                              // one
                                                                              // item
                                                                              // at a
                                                                              // time
    definedActionsList.addListSelectionListener(this);
    JScrollPane definedActionsListPane = new JScrollPane(definedActionsList);
    bottomPane.add(definedActionsListPane);
    refreshDefinedActionsList();
    setupDefinedActionsListSelectionListenerStuff();

    // Warning pane:
    warningPane = new WarningListPane();

    // Add panes and separators to main pane:
    mainPane.add(topPane);
    mainPane.add(middlePane);
    JSeparator separator1 = new JSeparator();
    separator1.setMaximumSize(new Dimension(2900, 1));
    mainPane.add(separator1);
    mainPane.add(bottomPane);
    JSeparator separator2 = new JSeparator();
    separator2.setMaximumSize(new Dimension(2900, 1));
    mainPane.add(separator2);
    mainPane.add(warningPane);
    add(mainPane);

    // make it so no file is open to begin with:
    setNoOpenFile();

    validate();
    repaint();
  }

  public void mousePressed(MouseEvent me) {
  }

  public void mouseReleased(MouseEvent me) {
  }

  public void mouseEntered(MouseEvent me) {
  }

  public void mouseExited(MouseEvent me) {
  }

  public void mouseClicked(MouseEvent me) {
    int clicks = me.getClickCount();

    if (me.getButton() == MouseEvent.BUTTON1 && clicks >= 2) {
      if (ruleTable.getSelectedRow() >= 0) // a row is selected
      {
        Rule tempRule = ruleTblMod.getActionTypeInFocus().getRule(
            (String) (ruleTblMod.getValueAt(ruleTable.getSelectedRow(), 0)));
        editRule(tempRule);
        editRuleButton.setEnabled(false);
        removeRuleButton.setEnabled(false);
        renameRuleButton.setEnabled(false);
      }
    }
  }

  public void valueChanged(ListSelectionEvent e) {
    if (definedActionsList.getSelectedIndex() >= 0) // an action is selected
    {
      ActionType tempAct = (ActionType) (actions.getAllActionTypes()
          .elementAt(definedActionsList.getSelectedIndex()));
      // get the selected action type
      setActionInFocus(tempAct);
    }
  }

  public void reload(File tempFile) // reloads the action types from a temporary
                                    // file
  {
    // reload:
    Vector warnings = ruleFileManip.loadFile(tempFile);
    generateWarnings(warnings);

    // reset UI stuff:
    clearActionInFocus();
    refreshDefinedActionsList();

    addCreateObjectsRuleButton.setEnabled(false);
    addDestroyObjectsRuleButton.setEnabled(false);
    addEffectRuleButton.setEnabled(false);
    editRuleButton.setEnabled(false);
    removeRuleButton.setEnabled(false);
    renameRuleButton.setEnabled(false);
  }

  private void generateWarnings(Vector warnings) // displays warnings of errors
                                                 // found during checking for
                                                 // inconsistencies
  {
    if (warnings.size() > 0) // there is at least 1 warning
    {
      warningPane.setWarnings(warnings);
    }
  }

  public void actionPerformed(ActionEvent evt) // handles user actions
  {
    Object source = evt.getSource();
    if (source == addCreateObjectsRuleButton) {
      newCreateObjectsRule();
    }

    else if (source == addDestroyObjectsRuleButton) {
      newDestroyObjectsRule();
    }

    else if (source == addEffectRuleButton) {
      newEffectRule();
    }

    else if (source == editRuleButton) {
      if (ruleTable.getSelectedRow() >= 0) // a row is selected
      {
        Rule tempRule = ruleTblMod.getActionTypeInFocus().getRule(
            (String) (ruleTblMod.getValueAt(ruleTable.getSelectedRow(), 0)));
        editRule(tempRule);
        editRuleButton.setEnabled(false);
        removeRuleButton.setEnabled(false);
        renameRuleButton.setEnabled(false);
      }
    }

    else if (source == removeRuleButton) {
      if (ruleTable.getSelectedRow() >= 0) // a row is selected
      {
        Rule tempRule = ruleTblMod.getActionTypeInFocus().getRule(
            (String) (ruleTblMod.getValueAt(ruleTable.getSelectedRow(), 0)));
        removeRule(tempRule);
      }
    } else if (source == renameRuleButton) {
      if (ruleTable.getSelectedRow() >= 0) // a row is selected
      {
        Rule tempRule = ruleTblMod.getActionTypeInFocus().getRule(
            (String) (ruleTblMod.getValueAt(ruleTable.getSelectedRow(), 0)));
        renameRule(tempRule);
      }
    }
  }

  private void newCreateObjectsRule() // creates a new create objects rule and
                                      // adds it to the action in focus
  {
    String response = JOptionPane.showInputDialog(null,
        "Enter a name for this Create Objects Rule:", "Enter Rule Name",
        JOptionPane.QUESTION_MESSAGE); // Show input dialog
    if (response != null) {
      if (ruleNameInputValid(response) == false) // input is invalid
      {
        JOptionPane
            .showMessageDialog(
                null,
                "Please enter a unique name, between 2 and 40 alphabetic characters, and no spaces",
                "Invalid Input", JOptionPane.WARNING_MESSAGE); // warn user to
                                                               // enter a valid
                                                               // name
        newCreateObjectsRule(); // try again
      } else // user has entered valid input
      {
        CreateObjectsRule newRule = new CreateObjectsRule(response, ruleTblMod
            .getActionTypeInFocus()); // create new rule
        CreateObjectsRuleInfoForm rInfoForm = new CreateObjectsRuleInfoForm(
            mainGUI, newRule, ruleTblMod.getActionTypeInFocus(), actions,
            objects, true);

        // Makes it so this gui will refresh itself after this rule info form
        // closes:
        WindowFocusListener l = new WindowFocusListener() {
          public void windowLostFocus(WindowEvent ev) {
            ruleTblMod.refreshData();
            editRuleButton.setEnabled(false);
            removeRuleButton.setEnabled(false);
            renameRuleButton.setEnabled(false);
            mainGUI.setFileModSinceLastSave();
          }

          public void windowGainedFocus(WindowEvent ev) {
          }
        };
        rInfoForm.addWindowFocusListener(l);
      }
    }
  }

  private void newDestroyObjectsRule() // creates a new destroy objects rule and
                                       // adds it to the action in focus
  {
    String response = JOptionPane.showInputDialog(null,
        "Enter a name for this Destroy Objects Rule:", "Enter Rule Name",
        JOptionPane.QUESTION_MESSAGE); // Show input dialog
    if (response != null) {
      if (ruleNameInputValid(response) == false) // input is invalid
      {
        JOptionPane
            .showMessageDialog(
                null,
                "Please enter a unique name, between 2 and 40 alphabetic characters, and no spaces",
                "Invalid Input", JOptionPane.WARNING_MESSAGE); // warn user to
                                                               // enter a valid
                                                               // name
        newDestroyObjectsRule(); // try again
      } else // user has entered valid input
      {
        DestroyObjectsRule newRule = new DestroyObjectsRule(response,
            ruleTblMod.getActionTypeInFocus()); // create new rule
        DestroyObjectsRuleInfoForm dInfoForm = new DestroyObjectsRuleInfoForm(
            mainGUI, newRule, ruleTblMod.getActionTypeInFocus(), actions, true);

        // Makes it so this gui will refresh itself after this rule info form
        // closes:
        WindowFocusListener l = new WindowFocusListener() {
          public void windowLostFocus(WindowEvent ev) {
            ruleTblMod.refreshData();
            editRuleButton.setEnabled(false);
            removeRuleButton.setEnabled(false);
            renameRuleButton.setEnabled(false);
            mainGUI.setFileModSinceLastSave();
          }

          public void windowGainedFocus(WindowEvent ev) {
          }
        };
        dInfoForm.addWindowFocusListener(l);
      }
    }
  }

  private void newEffectRule() // creates a new effect rule and adds it to the
                               // action in focus
  {
    String response = JOptionPane.showInputDialog(null,
        "Enter a name for this Effect Rule:", "Enter Rule Name",
        JOptionPane.QUESTION_MESSAGE); // Show input dialog
    if (response != null) {
      if (ruleNameInputValid(response) == false) // input is invalid
      {
        JOptionPane
            .showMessageDialog(
                null,
                "Please enter a unique name, between 2 and 40 alphabetic characters, and no spaces",
                "Invalid Input", JOptionPane.WARNING_MESSAGE); // warn user to
                                                               // enter a valid
                                                               // name
        newEffectRule(); // try again
      } else // user has entered valid input
      {
        EffectRule newRule = new EffectRule(response, ruleTblMod
            .getActionTypeInFocus()); // create new rule
        // create a ParticipantRuleEffect for each participant in the action
        // type:
        Vector parts = ruleTblMod.getActionTypeInFocus().getAllParticipants();
        for (int i = 0; i < parts.size(); i++) {
          newRule.addParticipantRuleEffect(new ParticipantRuleEffect(
              (ActionTypeParticipant) parts.elementAt(i)));
        }
        EffectRuleInfoForm rInfoForm = new EffectRuleInfoForm(mainGUI, newRule,
            ruleTblMod.getActionTypeInFocus(), actions, objects, true);

        // Makes it so this gui will refresh itself after this rule info form
        // closes:
        WindowFocusListener l = new WindowFocusListener() {
          public void windowLostFocus(WindowEvent ev) {
            ruleTblMod.refreshData();
            editRuleButton.setEnabled(false);
            removeRuleButton.setEnabled(false);
            renameRuleButton.setEnabled(false);
            mainGUI.setFileModSinceLastSave();
          }

          public void windowGainedFocus(WindowEvent ev) {
          }
        };
        rInfoForm.addWindowFocusListener(l);
      }
    }
  }

  private void editRule(Rule rule) {
    if (rule instanceof CreateObjectsRule) {
      CreateObjectsRuleInfoForm rInfoForm = new CreateObjectsRuleInfoForm(
          mainGUI, (CreateObjectsRule) rule, ruleTblMod.getActionTypeInFocus(),
          actions, objects, false);

      // Makes it so this gui will refresh itself after this rule info form
      // closes:
      WindowFocusListener l = new WindowFocusListener() {
        public void windowLostFocus(WindowEvent ev) {
          ruleTblMod.refreshData();
          editRuleButton.setEnabled(false);
          removeRuleButton.setEnabled(false);
          renameRuleButton.setEnabled(false);
          mainGUI.setFileModSinceLastSave();
        }

        public void windowGainedFocus(WindowEvent ev) {
        }
      };
      rInfoForm.addWindowFocusListener(l);
    }

    else if (rule instanceof DestroyObjectsRule) {
      DestroyObjectsRuleInfoForm rInfoForm = new DestroyObjectsRuleInfoForm(
          mainGUI, (DestroyObjectsRule) rule,
          ruleTblMod.getActionTypeInFocus(), actions, false);

      // Makes it so this gui will refresh itself after this rule info form
      // closes:
      WindowFocusListener l = new WindowFocusListener() {
        public void windowLostFocus(WindowEvent ev) {
          ruleTblMod.refreshData();
          editRuleButton.setEnabled(false);
          removeRuleButton.setEnabled(false);
          renameRuleButton.setEnabled(false);
          mainGUI.setFileModSinceLastSave();
        }

        public void windowGainedFocus(WindowEvent ev) {
        }
      };
      rInfoForm.addWindowFocusListener(l);
    }

    else if (rule instanceof EffectRule) {
      EffectRuleInfoForm rInfoForm = new EffectRuleInfoForm(mainGUI,
          (EffectRule) rule, ruleTblMod.getActionTypeInFocus(), actions,
          objects, false);

      // Makes it so this gui will refresh itself after this rule info form
      // closes:
      WindowFocusListener l = new WindowFocusListener() {
        public void windowLostFocus(WindowEvent ev) {
          ruleTblMod.refreshData();
          editRuleButton.setEnabled(false);
          removeRuleButton.setEnabled(false);
          renameRuleButton.setEnabled(false);
          mainGUI.setFileModSinceLastSave();
        }

        public void windowGainedFocus(WindowEvent ev) {
        }
      };
      rInfoForm.addWindowFocusListener(l);
    }
  }

  private void renameRule(Rule rule) // removes the action with the specified
                                     // name from the data structure
  {

    String response = JOptionPane.showInputDialog(null, "Enter new name for "
        + rule.getName(), "Rename Rule Type", JOptionPane.QUESTION_MESSAGE); // Show
                                                                             // input
                                                                             // dialog
    if (response != null) {
      if (ruleNameInputValid(response) == false) // input is invalid
      {
        JOptionPane
            .showMessageDialog(
                null,
                "Please enter a unique name, between 2 and 40 alphabetic characters, and no spaces",
                "Invalid Input", JOptionPane.WARNING_MESSAGE); // warn user to
                                                               // enter a valid
                                                               // number
        renameRule(rule); // try again
      } else // user has entered valid input
      {
        rule.setName(response);
        ruleTblMod.refreshData();
        //setActionInFocus(act); // set newly created object to be the focus of
        // the GUI
        ((ModelBuilderGUI) mainGUI).setFileModSinceLastSave();
        //updateDefinedActionsList();
      }
    }
  }

  private void removeRule(Rule rule) {
    int choice = JOptionPane.showConfirmDialog(null, ("Really remove "
        + rule.getName() + " rule?"), "Confirm Rule Removal",
        JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      // Remove rule:
      ruleTblMod.getActionTypeInFocus().removeRule(rule.getName());
      ruleTblMod.refreshData();
      removeRuleButton.setEnabled(false);
      renameRuleButton.setEnabled(false);
      editRuleButton.setEnabled(false);
      mainGUI.setFileModSinceLastSave();
    } else // choice == JOptionPane.NO_OPTION
    {
    }
  }

  private boolean ruleNameInputValid(String input) // returns true if input is a
                                                   // valid rule name, false if
                                                   // not
  {
    char[] cArray = input.toCharArray();

    // Check for length constraints:
    if ((cArray.length < 2) || (cArray.length > 40)) // user has entered a
                                                     // string shorter than 2
                                                     // chars or longer than 40
                                                     // chars
    {
      return false;
    }

    // Check for invalid characters:
    for (int i = 0; i < cArray.length; i++) {
      if ((Character.isLetter(cArray[i])) == false) // character is not a letter
                                                    // (hence, invalid)
      {
        return false;
      }
    }

    // Check for uniqueness of name:
    Vector existingActionTypes = actions.getAllActionTypes();
    for (int i = 0; i < existingActionTypes.size(); i++) {
      ActionType tempAct = ((ActionType) existingActionTypes.elementAt(i));
      Vector existingRules = tempAct.getAllRules();
      for (int j = 0; j < existingRules.size(); j++) {
        Rule tempRule = ((Rule) existingRules.elementAt(j));
        if (tempRule.getName().equalsIgnoreCase(input)) // name entered is not
                                                        // unique (there is
                                                        // already another rule
                                                        // defined with the
        // same name (hence, invalid)
        {
          return false;
        }
      }
    }
    return true; // none of the invalid conditions exist
  }

  private void setupRuleTableSelectionListenerStuff() // enables edit and remove
                                                      // rule buttons whenever a
                                                      // row (rule) is selected
  {
    // Copied from a Java tutorial:
    ListSelectionModel rowSM = ruleTable.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting())
          return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty() == false) {
          editRuleButton.setEnabled(true);
          removeRuleButton.setEnabled(true);
          renameRuleButton.setEnabled(true);
        }
      }
    });
  }

  private void setupDefinedActionsListSelectionListenerStuff() // enables
                                                               // view/edit
                                                               // rules button
                                                               // whenever a
                                                               // list item
                                                               // (action) is
                                                               // selected
  {
    // Copied from a Java tutorial:
    ListSelectionModel rowSM = definedActionsList.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting())
          return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
      }
    });
  }

  private void refreshDefinedActionsList() {
    Vector actionNames = new Vector();
    Vector currentActs = actions.getAllActionTypes();
    for (int i = 0; i < currentActs.size(); i++) {
      actionNames.add(((ActionType) (currentActs.elementAt(i))).getName());
    }
    definedActionsList.setListData(actionNames);
  }

  private void setActionInFocus(ActionType newAct) // sets the given action type
                                                   // as the focus of this GUI
  {
    ruleTblMod.setActionTypeInFocus(newAct); // set focus of rule table to new
                                             // action type
    ruleTableTitle.setText((newAct.getName()) + " Action Rules:"); // change
                                                                   // title of
                                                                   // table to
                                                                   // reflect
                                                                   // new action
    // enable/disable buttons:
    addCreateObjectsRuleButton.setEnabled(true);
    addDestroyObjectsRuleButton.setEnabled(true);
    addEffectRuleButton.setEnabled(true);
    editRuleButton.setEnabled(false);
    removeRuleButton.setEnabled(false);
    renameRuleButton.setEnabled(false);
  }

  private void clearActionInFocus() // clears the GUI so that it doesn't have an
                                    // action type in focus
  {
    ruleTblMod.clearActionTypeInFocus(); // clear the table
    ruleTableTitle.setText("No action type selected");
    // disable buttons:
    addCreateObjectsRuleButton.setEnabled(false);
    addDestroyObjectsRuleButton.setEnabled(false);
    addEffectRuleButton.setEnabled(false);
    editRuleButton.setEnabled(false);
    removeRuleButton.setEnabled(false);
    renameRuleButton.setEnabled(false);
  }

  public void setNewOpenFile(File f) {
    clearActionInFocus();
    refreshDefinedActionsList();
    // disable UI components:
    addCreateObjectsRuleButton.setEnabled(false);
    addDestroyObjectsRuleButton.setEnabled(false);
    addEffectRuleButton.setEnabled(false);
    editRuleButton.setEnabled(false);
    removeRuleButton.setEnabled(false);
    renameRuleButton.setEnabled(false);

    warningPane.clearWarnings();
    if (f.exists()) // file has been saved before
    {
      reload(f);
    }
  }

  public void setNoOpenFile() // makes it so there's no open file in the GUI
  {
    clearActionInFocus();
    refreshDefinedActionsList();
    // disable UI components:
    addCreateObjectsRuleButton.setEnabled(false);
    addDestroyObjectsRuleButton.setEnabled(false);
    addEffectRuleButton.setEnabled(false);
    editRuleButton.setEnabled(false);
    removeRuleButton.setEnabled(false);
    renameRuleButton.setEnabled(false);

    warningPane.clearWarnings();
  }
}