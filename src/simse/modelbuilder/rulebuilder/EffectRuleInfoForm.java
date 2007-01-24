/* This class defines the GUI for specifying EffectRules with */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.Color;

public class EffectRuleInfoForm extends JDialog implements ActionListener,
    KeyListener, MouseListener {
  private EffectRule ruleInFocus; // copy of rule being edited
  private EffectRule actualRule; // actual rule being edited
  private ActionType actionInFocus; // action to whom this rule belongs
  private ActionTypeParticipant participantInFocus; // participant currently the
                                                    // focus of this GUI
  private SimSEObjectType objectTypeInFocus; // SimSEObjectType of the
                                             // participant currently in focus
  private DefinedActionTypes actions; // existing defined actions
  private boolean newRule; // whether this is a newly created rule (true) or
                           // editing an existing one (false)
  private JTextField lastFocusedTextField; // text field most recently in focus
  private JTextArea echoedTextField; // echoed text field for the button pad gui

  private boolean justClosedButtonPad = false;

  // constants for text field validation:
  private final int OPEN_PAREN = 0;
  private final int CLOSED_PAREN = 1;
  private final int NUMBER = 2;
  private final int OPERATOR = 3;
  private final int DIGIT = 4;

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
  private JButton timeElapsedButton;
  private JButton actTimeElapsedButton;
  private Vector digitButtons; // 0, 1, 2, 3, ...
  private JButton buttonDot;
  private Vector operatorButtons; // +, -, *, /, (, )
  private JButton buttonMinus;
  private JButton buttonOpenParen;
  private JButton buttonCloseParen;
  private Vector otherButtons; // e.g., random(min, max)
  private JButton randomButton;
  private JButton stringButton;
  private JButton booleanButton;
  private JButton backspaceButton;
  private ButtonPadGUI buttGUI;

  private JLabel partEffectsTitle; // title for the main frame
  private JScrollPane effectsMiddlePane;
  private Box effectsMiddleMiddlePane; // pane for holding all of the
                                       // participant info
  private Vector textFields; // Vector of text fields, one for each attribute of
                             // the object type currently in focus
  private JList participantList; // list of participants to choose from for
                                 // editing
  private Vector partNames; // list of participants for JList
  private JButton viewEditEffectsButton; // for viewing a participant's effects
  private JButton buttonPadButton; // for bringing up button pad
  private JPanel otherActEffectPanel; // main panel for the specifying effect on
                                      // participant's other actions:
  private ButtonGroup buttonGroupOtherActEffect; // for following radio buttons
  private JRadioButton activateButton; // for activating all other actions of
                                       // participant in focus
  private JRadioButton deactivateButton; // for deactivating all other actions
                                         // of participant in focus
  private JRadioButton noneButton; // for specifying no effects on the other
                                   // actions of the participant in focus
  private JList ruleInputList; // list for holding rule inputs
  private JButton newRuleInputButton; // for adding a new rule input
  private JButton viewEditInputButton; // for viewing/editing existing rule
                                       // input
  private JButton removeInputButton; // for removing a rule input
  private ButtonGroup timingButtonGroup; // for radio buttons for choosing the
                                         // timing of the rule
  private JRadioButton continuousButton; // for denoting a continuous rule
  private JRadioButton triggerButton; // for denoting a trigger rule
  private JRadioButton destroyerButton; // for denoting a destroyer rule
  private JCheckBox joinCheckBox; // for denoting this rule's relation to
                                  // joining/un-joining
  private JCheckBox visibilityCheckBox; // for denoting this rule's visibility
                                        // in the explanatory tool
  private JButton annotationButton; // for viewing/editing this rule's
                                    // annotation
  private JButton okButton; // for ok'ing the changes made in this form
  private JButton cancelButton; // for cancelling the changes made in this form

  public EffectRuleInfoForm(JFrame owner, EffectRule rule, ActionType act,
      DefinedActionTypes acts, DefinedObjectTypes objs, boolean newOrEdit) {
    super(owner, true);
    newRule = newOrEdit;
    actualRule = rule;
    actionInFocus = act;
    if (!newRule) // editing existing rule
    {
      ruleInFocus = (EffectRule) (actualRule.clone()); // make a copy of the
                                                       // rule for editing
    } else // new rule
    {
      ruleInFocus = rule;
    }
    actions = acts;
    textFields = new Vector();
    echoedTextField = new JTextArea(50, 100);//250);
    echoedTextField.setEditable(false);
    echoedTextField.setLineWrap(true);
    echoedTextField.setWrapStyleWord(true);

    // Set window title:
    setTitle(ruleInFocus.getName() + " Rule Information - SimSE");

    // Create main panel (box):
    Box mainPane = Box.createVerticalBox();

    // Create effectsTopPane:
    JPanel effectsTopPane = new JPanel();
    partEffectsTitle = new JLabel("No participant selected");
    effectsTopPane.add(partEffectsTitle);

    // Create effectsMiddlePane:
    effectsMiddleMiddlePane = Box.createVerticalBox();
    effectsMiddlePane = new JScrollPane(effectsMiddleMiddlePane);
    effectsMiddlePane
        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    effectsMiddlePane.setPreferredSize(new Dimension(1000, 200));

    // Radio buttons / Other actions effect panel:
    otherActEffectPanel = new JPanel(new BorderLayout());
    JPanel radioButtonsPane = new JPanel();
    activateButton = new JRadioButton(OtherActionsEffect.ACTIVATE_ALL);
    deactivateButton = new JRadioButton(OtherActionsEffect.DEACTIVATE_ALL);
    noneButton = new JRadioButton(OtherActionsEffect.NONE);
    JLabel labelT = new JLabel("Effect on Participant's Other Actions: ");
    radioButtonsPane.add(labelT);
    radioButtonsPane.add(activateButton);
    radioButtonsPane.add(deactivateButton);
    radioButtonsPane.add(noneButton);
    radioButtonsPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    buttonGroupOtherActEffect = new ButtonGroup();
    buttonGroupOtherActEffect.add(activateButton);
    buttonGroupOtherActEffect.add(deactivateButton);
    buttonGroupOtherActEffect.add(noneButton);
    otherActEffectPanel.add(radioButtonsPane, BorderLayout.WEST);

    // button pad:
    inputButton = new JButton("Rule Input");
    inputButton.addActionListener(this);
    inputButton.addKeyListener(this);
    attributeThisPartButton = new JButton("Attributes - this participant");
    attributeThisPartButton.addActionListener(this);
    attributeThisPartButton.addKeyListener(this);
    attributeOtherPartButton = new JButton("Attributes - other participants");
    attributeOtherPartButton.addActionListener(this);
    attributeOtherPartButton.addKeyListener(this);
    numObjectsButton = new JButton("Num participants in action");
    numObjectsButton.addActionListener(this);
    numObjectsButton.addKeyListener(this);
    numActionsThisPartButton = new JButton("Num actions - this participant");
    numActionsThisPartButton.addActionListener(this);
    numActionsThisPartButton.addKeyListener(this);
    numActionsOtherPartButton = new JButton("Num actions - other participants");
    numActionsOtherPartButton.addActionListener(this);
    numActionsOtherPartButton.addKeyListener(this);
    // initialize time buttons:
    timeButtons = new Vector();
    timeElapsedButton = new JButton("totalTimeElapsed");
    timeElapsedButton.addActionListener(this);
    timeElapsedButton.addKeyListener(this);
    timeButtons.add(timeElapsedButton);
    actTimeElapsedButton = new JButton("actionTimeElapsed");
    actTimeElapsedButton.addActionListener(this);
    actTimeElapsedButton.addKeyListener(this);
    timeButtons.add(actTimeElapsedButton);

    // initialize digit buttons:
    digitButtons = new Vector();
    JButton button0 = new JButton("0");
    digitButtons.add(button0);
    JButton button1 = new JButton("1");
    digitButtons.add(button1);
    JButton button2 = new JButton("2");
    digitButtons.add(button2);
    JButton button3 = new JButton("3");
    digitButtons.add(button3);
    JButton button4 = new JButton("4");
    digitButtons.add(button4);
    JButton button5 = new JButton("5");
    digitButtons.add(button5);
    JButton button6 = new JButton("6");
    digitButtons.add(button6);
    JButton button7 = new JButton("7");
    digitButtons.add(button7);
    JButton button8 = new JButton("8");
    digitButtons.add(button8);
    JButton button9 = new JButton("9");
    digitButtons.add(button9);
    buttonDot = new JButton(".");
    digitButtons.add(buttonDot);

    for (int i = 0; i < digitButtons.size(); i++) {
      JButton b = (JButton) digitButtons.get(i);
      b.addActionListener(this);
      b.addKeyListener(this);
    }

    // initialize operator buttons:
    operatorButtons = new Vector();
    JButton buttonPlus = new JButton("+");
    operatorButtons.add(buttonPlus);
    buttonMinus = new JButton("-");
    operatorButtons.add(buttonMinus);
    JButton buttonMultiply = new JButton("*");
    operatorButtons.add(buttonMultiply);
    JButton buttonDivide = new JButton("/");
    operatorButtons.add(buttonDivide);
    buttonOpenParen = new JButton("(");
    operatorButtons.add(buttonOpenParen);
    buttonCloseParen = new JButton(")");
    operatorButtons.add(buttonCloseParen);

    for (int i = 0; i < operatorButtons.size(); i++) {
      JButton b = (JButton) operatorButtons.get(i);
      b.addActionListener(this);
      b.addKeyListener(this);
    }

    // initialize other buttons:
    otherButtons = new Vector();
    randomButton = new JButton("Random(min, max)");
    randomButton.setMnemonic('R');
    otherButtons.add(randomButton);
    stringButton = new JButton("String");
    stringButton.setMnemonic('S');
    otherButtons.add(stringButton);
    booleanButton = new JButton("Boolean");
    booleanButton.setMnemonic('B');
    otherButtons.add(booleanButton);
    backspaceButton = new JButton("Backspace");
    otherButtons.add(backspaceButton);

    for (int i = 0; i < otherButtons.size(); i++) {
      JButton b = (JButton) otherButtons.get(i);
      b.addActionListener(this);
      b.addKeyListener(this);
    }

    // Create effectsBottomPane:
    JPanel effectsBottomPane = new JPanel();
    // label:
    effectsBottomPane.add(new JLabel("Participants:"));
    // list of participants:
    participantList = new JList();
    participantList.addMouseListener(this);
    participantList.setVisibleRowCount(5); // make 5 items visible at a time
    participantList.setFixedCellWidth(250);
    participantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only
                                                                           // allow
                                                                           // the
                                                                           // user
                                                                           // to
                                                                           // select
                                                                           // one
                                                                           // item
                                                                           // at a
                                                                           // time
    JScrollPane participantListPane = new JScrollPane(participantList);
    effectsBottomPane.add(participantListPane);
    refreshParticipantList();
    setupParticipantListSelectionListenerStuff();
    // button:
    viewEditEffectsButton = new JButton("View/Edit Effects");
    viewEditEffectsButton.addActionListener(this);
    viewEditEffectsButton.setEnabled(false);
    effectsBottomPane.add(viewEditEffectsButton);
    buttonPadButton = new JButton("Button Pad");
    buttonPadButton.addActionListener(this);
    buttonPadButton.setEnabled(false);
    effectsBottomPane.add(buttonPadButton);

    // Create inputPane:
    Box inputPane = Box.createVerticalBox();
    JPanel inputTitlePane = new JPanel();
    inputTitlePane.add(new JLabel("Rule Input:"));
    inputPane.add(inputTitlePane);
    JPanel newInputButtonPane = new JPanel();
    newRuleInputButton = new JButton("Add new rule input");
    newRuleInputButton.addActionListener(this);
    newInputButtonPane.add(newRuleInputButton);
    inputPane.add(newInputButtonPane);
    JPanel bottomInputPane = new JPanel();
    bottomInputPane.add(new JLabel("Existing Rule Input:"));
    ruleInputList = new JList();
    ruleInputList.setVisibleRowCount(5); // make 5 items visible at a time
    ruleInputList.setFixedCellWidth(250);
    ruleInputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only
                                                                         // allow
                                                                         // the
                                                                         // user
                                                                         // to
                                                                         // select
                                                                         // one
                                                                         // item
                                                                         // at a
                                                                         // time
    ruleInputList.addMouseListener(this);
    JScrollPane inputListPane = new JScrollPane(ruleInputList);
    refreshRuleInputList();
    setupRuleInputListSelectionListenerStuff();
    bottomInputPane.add(inputListPane);
    // rule button pane:
    Box ruleButtonPane = Box.createVerticalBox();
    viewEditInputButton = new JButton("View/Edit");
    viewEditInputButton.setEnabled(false);
    viewEditInputButton.addActionListener(this);
    JPanel pane1 = new JPanel();
    pane1.add(viewEditInputButton);
    ruleButtonPane.add(pane1);
    removeInputButton = new JButton("Remove   ");
    removeInputButton.setEnabled(false);
    removeInputButton.addActionListener(this);
    JPanel pane2 = new JPanel();
    pane2.add(removeInputButton);
    ruleButtonPane.add(pane2);
    bottomInputPane.add(ruleButtonPane);
    inputPane.add(bottomInputPane);

    // Create timing pane:
    JPanel timingPane = new JPanel();
    timingPane.add(new JLabel("Timing of Rule:"));
    timingButtonGroup = new ButtonGroup();
    continuousButton = new JRadioButton("Continuous Rule");
    continuousButton.setSelected(true);
    continuousButton.addActionListener(this);
    timingPane.add(continuousButton);
    triggerButton = new JRadioButton("Trigger Rule");
    triggerButton.addActionListener(this);
    timingPane.add(triggerButton);
    destroyerButton = new JRadioButton("Destroyer Rule");
    destroyerButton.addActionListener(this);
    timingPane.add(destroyerButton);
    timingButtonGroup.add(continuousButton);
    timingButtonGroup.add(triggerButton);
    timingButtonGroup.add(destroyerButton);

    // Create join pane:
    JPanel joinPane = new JPanel();
    joinCheckBox = new JCheckBox(
        "Re-execute rule with each joining/un-joining participant");
    joinCheckBox
        .setToolTipText("Re-execute this rule for all participants whenever any participant joins (for a trigger rule) or un-joins (for a destroyer rule)");
    joinPane.add(joinCheckBox);

    refreshTimingButtons();
    refreshJoinCheckBox();

    // Create annotation pane:
    JPanel annotationPane = new JPanel();
    visibilityCheckBox = new JCheckBox("Rule visible in explanatory tool");
    visibilityCheckBox.setToolTipText("Make this rule visible to the "
        + "player in the user interface of the explanatory tool");
    refreshVisibilityCheckBox();
    annotationPane.add(visibilityCheckBox);
    annotationButton = new JButton("View/Edit Annotation");
    annotationButton.setToolTipText("View/edit an annotation for this "
        + "rule");
    annotationButton.addActionListener(this);
    annotationPane.add(annotationButton);

    // Create bottom pane:
    JPanel bottomPane = new JPanel();
    okButton = new JButton("OK");
    okButton.addActionListener(this);
    bottomPane.add(okButton);
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);
    bottomPane.add(cancelButton);

    // Add panes to main pane:
    mainPane.add(effectsTopPane);
    mainPane.add(effectsMiddlePane);
    mainPane.add(effectsBottomPane);
    JSeparator separator1 = new JSeparator();
    separator1.setMaximumSize(new Dimension(1000, 1));
    mainPane.add(separator1);
    mainPane.add(inputPane);
    JSeparator separator2 = new JSeparator();
    separator2.setMaximumSize(new Dimension(1000, 1));
    mainPane.add(separator2);
    mainPane.add(timingPane);
    mainPane.add(joinPane);
    JSeparator separator3 = new JSeparator();
    separator3.setMaximumSize(new Dimension(1000, 1));
    mainPane.add(separator3);
    mainPane.add(annotationPane);
    JSeparator separator4 = new JSeparator();
    separator4.setMaximumSize(new Dimension(1000, 1));
    mainPane.add(separator4);
    mainPane.add(bottomPane);

    // Set main window frame properties:
    addKeyListener(this);
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

  public void mouseClicked(MouseEvent me) {
    int clicks = me.getClickCount();
    Object o = me.getSource();

    if (clicks >= 2) {
      boolean handled = false;
      if (o == participantList && viewEditEffectsButton.isEnabled()) {
        viewEditEffects();
        handled = true;
      } else if (o == ruleInputList)//&& ruleInputList.getSelectedIndex() >= 0
                                    // )
      {
        editRuleInput(ruleInFocus.getRuleInput((String) ruleInputList
            .getSelectedValue()));
        handled = true;
      } else if (!handled)
        newButtonPad();
    }

  }

  public void mousePressed(MouseEvent me) {
  }

  public void mouseReleased(MouseEvent me) {
  }

  public void mouseEntered(MouseEvent me) {
  }

  public void mouseExited(MouseEvent me) {
  }

  public void keyPressed(KeyEvent ke) {
    int key = ke.getKeyCode();
    JButton btn = new JButton(ke.getKeyChar() + "");
    boolean enabled = false;

    switch (key) {
    case KeyEvent.VK_BACK_SPACE:
      backspaceButtonChosen();
      break;

    case KeyEvent.VK_R:
      if (randomButton.isEnabled())
        randomButtonChosen();
      break;

    case KeyEvent.VK_S:
      if (stringButton.isEnabled())
        stringButtonChosen();
      break;

    case KeyEvent.VK_B:
      if (booleanButton.isEnabled())
        booleanButtonChosen();
      break;

    case KeyEvent.VK_0:
    case KeyEvent.VK_1:
    case KeyEvent.VK_2:
    case KeyEvent.VK_3:
    case KeyEvent.VK_4:
    case KeyEvent.VK_5:
    case KeyEvent.VK_6:
    case KeyEvent.VK_7:
    case KeyEvent.VK_8:
    case KeyEvent.VK_9:
    case KeyEvent.VK_DECIMAL:
    case KeyEvent.VK_PERIOD:
    case KeyEvent.VK_NUMPAD0:
    case KeyEvent.VK_NUMPAD1:
    case KeyEvent.VK_NUMPAD2:
    case KeyEvent.VK_NUMPAD3:
    case KeyEvent.VK_NUMPAD4:
    case KeyEvent.VK_NUMPAD5:
    case KeyEvent.VK_NUMPAD6:
    case KeyEvent.VK_NUMPAD7:
    case KeyEvent.VK_NUMPAD8:
    case KeyEvent.VK_NUMPAD9:
      for (int i = 0; i < digitButtons.size(); i++) {
        JButton b = (JButton) digitButtons.get(i);
        if (b.getText().trim().equalsIgnoreCase(ke.getKeyChar() + ""))
          enabled = b.isEnabled();
      }
      if (enabled)
        digitButtonChosen(btn);
      break;

    case KeyEvent.VK_ADD:
    case KeyEvent.VK_SUBTRACT:
    case KeyEvent.VK_MULTIPLY:
    case KeyEvent.VK_DIVIDE:
    case KeyEvent.VK_ASTERISK:
    case KeyEvent.VK_SLASH:
    case KeyEvent.VK_MINUS:
    case KeyEvent.VK_LEFT_PARENTHESIS:
    case KeyEvent.VK_RIGHT_PARENTHESIS:
      for (int i = 0; i < operatorButtons.size(); i++) {
        JButton b = (JButton) operatorButtons.get(i);

        if (b.getText().trim().equalsIgnoreCase(ke.getKeyChar() + ""))
          enabled = b.isEnabled();
      }
      if (enabled)
        operatorButtonChosen(btn);
      break;

    }

    boolean found = false;
    for (int i = 0; !found && i < operatorButtons.size(); i++) {
      JButton abtn = (JButton) operatorButtons.get(i);
      if (abtn.isEnabled()) {
        abtn.requestFocus();
        found = true;
      }
    }
    for (int i = 0; !found && i < digitButtons.size(); i++) {
      JButton abtn = (JButton) digitButtons.get(i);
      if (abtn.isEnabled()) {
        abtn.requestFocus();
        found = true;
      }
    }
    for (int i = 0; !found && i < otherButtons.size(); i++) {
      JButton abtn = (JButton) otherButtons.get(i);
      if (abtn.isEnabled()) {
        abtn.requestFocus();
        found = true;
      }
    }

  }

  public void keyReleased(KeyEvent ke) {
  }

  public void keyTyped(KeyEvent ke) {
  }

  public void actionPerformed(ActionEvent evt) // handles user actions
  {

    if (buttGUI != null)
      buttGUI.requestFocus();

    Object source = evt.getSource();
    if (source == viewEditEffectsButton) {
      viewEditEffects();
    }

    else if (source == buttonPadButton) {
      newButtonPad();
    }

    // buttons on button pad:
    else if (source == inputButton) {
      inputButtonChosen();
    }

    else if (source == attributeThisPartButton) {
      attributeThisPartButtonChosen();
    }

    else if (source == attributeOtherPartButton) {
      attributeOtherPartButtonChosen();
    }

    else if (source == numObjectsButton) {
      numObjectsButtonChosen();
    }

    else if (source == numActionsThisPartButton) {
      numActionsThisPartButtonChosen();
    }

    else if (source == numActionsOtherPartButton) {
      numActionsOtherPartButtonChosen();
    }

    else if (source == timeElapsedButton) {
      // append the text to the text field:
      if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
          && (getLastToken(lastFocusedTextField.getText()).equals("-"))) // negative
      // sign was last token
      {
        setFocusedTextFieldText(lastFocusedTextField.getText().trim().concat(
            "totalTimeElapsed "));
      } else {
        setFocusedTextFieldText(lastFocusedTextField.getText().concat(
            "totalTimeElapsed "));
      }
      refreshButtonPad(NUMBER);
    }

    else if (source == actTimeElapsedButton) {
      actTimeElapsedButtonChosen();
    }

    else if (source == randomButton) {
      randomButtonChosen();
    }

    else if (source == newRuleInputButton) {
      newRuleInput();
    }

    else if (source == viewEditInputButton) {
      if (ruleInputList.getSelectedIndex() >= 0) // a rule input is selected
      {
        editRuleInput(ruleInFocus.getRuleInput((String) ruleInputList
            .getSelectedValue()));
      }
    }

    else if (source == removeInputButton) {
      if (ruleInputList.getSelectedIndex() >= 0) // a rule input is selected
      {
        removeRuleInput((String) ruleInputList.getSelectedValue());
      }
    }

    else if (source == triggerButton) // trigger button has been clicked
    {
      joinCheckBox.setEnabled(true);
    }

    else if (source == destroyerButton) // destroyer button has been clicked
    {
      joinCheckBox.setEnabled(true);
    }

    else if (source == continuousButton) // continuous button has been clicked
    {
      joinCheckBox.setEnabled(false);
    }

    else if (source == annotationButton) // annotation button selected
    {
      RuleAnnotationForm form = new RuleAnnotationForm(this, ruleInFocus);
    }

    else if (source == cancelButton) // cancel button has been pressed
    {
      // Close window:
      setVisible(false);
      dispose();
    }

    else if (source == okButton) // okButton has been pressed
    {
      setParticipantInFocusDataFromForm();
      Vector errors = validateInput(); // check validity of input of object type
                                       // currently in focus
      if (errors.size() == 0) // input valid
      {
        if (!newRule) // existing rule being edited
        {
          // set the values to the actual participant from the copy whose values
          // have been edited:
          actualRule.setParticipantRuleEffects(ruleInFocus
              .getAllParticipantRuleEffects());
          actualRule.setRuleInputs(ruleInFocus.getAllRuleInputs());
          actualRule.setTiming(ruleInFocus.getTiming());
          actualRule.setVisibilityInExplanatoryTool(ruleInFocus
              .isVisibleInExplanatoryTool());
          actualRule.setAnnotation(ruleInFocus.getAnnotation());
          actualRule.setExecuteOnJoins(ruleInFocus.getExecuteOnJoins());
        } else // new rule
        {
          actionInFocus.addRule(ruleInFocus);
        }

        // close window:
        setVisible(false);
        dispose();
      } else // input not valid
      {
        for (int i = 0; i < errors.size(); i++) {
          JOptionPane.showMessageDialog(null, ((String) errors.elementAt(i)),
              "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
      }
    }

    else if (source == stringButton) {
      stringButtonChosen();
    }

    else if (source == booleanButton) {
      booleanButtonChosen();
    }

    else if (source == backspaceButton) {
      backspaceButtonChosen();
    }

    else if ((source instanceof JButton)
        && (((JButton) source).getText().length() == 1)
        && ((Character.isDigit(((JButton) source).getText().toCharArray()[0])) || (((JButton) source)
            .getText().toCharArray()[0] == '.')))
    // digit button (or '.' button)
    {
      digitButtonChosen((JButton) source);
    }

    else if ((source instanceof JButton)
        && ((((JButton) source).getText().equals("+"))
            || (((JButton) source).getText().equals("-"))
            || (((JButton) source).getText().equals("*")) || (((JButton) source)
            .getText().equals("/")))) // operator button chosen
    {
      operatorButtonChosen((JButton) source);
    }

    else if ((source instanceof JButton)
        && (((JButton) source).getText().equals("("))) {
      // append the text to the text field:
      if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
          && (getLastToken(lastFocusedTextField.getText()).equals("-"))) // negative
      // sign was last token
      {
        setFocusedTextFieldText(lastFocusedTextField.getText().trim().concat(
            "("));
      } else {
        setFocusedTextFieldText(lastFocusedTextField.getText().concat("("));
      }
      refreshButtonPad(OPEN_PAREN);
    }

    else if ((source instanceof JButton)
        && (((JButton) source).getText().equals(")"))) {
      // append the text to the text field:
      setFocusedTextFieldText(lastFocusedTextField.getText().trim()
          .concat(") "));
      refreshButtonPad(CLOSED_PAREN);
    }
  }

  private void refreshParticipantList() {
    partNames = new Vector();
    Vector parts = actionInFocus.getAllParticipants();
    for (int i = 0; i < parts.size(); i++) {
      ActionTypeParticipant tempPart = (ActionTypeParticipant) parts
          .elementAt(i);
      partNames.add(tempPart.getName() + ":"); // add each participant's name to
                                               // the list
      Vector objTypes = tempPart.getAllSimSEObjectTypes();
      for (int j = 0; j < objTypes.size(); j++) {
        SimSEObjectType tempType = (SimSEObjectType) objTypes.elementAt(j);
        partNames.add("--" + tempType.getName() + " "
            + SimSEObjectTypeTypes.getText(tempType.getType()));
      }

    }
    participantList.setListData(partNames);
  }

  private void setupParticipantListSelectionListenerStuff() // enables view/edit
                                                            // effects button
                                                            // whenever a list
                                                            // item
                                                            // (participant's
  // object type) is selected
  {
    // Copied from a Java tutorial:
    ListSelectionModel rowSM = participantList.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting())
          return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if ((lsm.isSelectionEmpty() == false)
            && (((String) participantList.getSelectedValue()).startsWith("--"))) // a
        // object type is selected (not a participant name)
        {
          viewEditEffectsButton.setEnabled(true);
        } else if ((lsm.isSelectionEmpty() == false)
            && (((String) participantList.getSelectedValue()).startsWith("--")) == false)
        // a participant name is selected
        {
          viewEditEffectsButton.setEnabled(false);
        }
      }
    });
  }

  private void refreshRuleInputList() {
    Vector inputs = ruleInFocus.getAllRuleInputs();
    Vector inputNames = new Vector();
    for (int i = 0; i < inputs.size(); i++) {
      inputNames.add(((RuleInput) inputs.elementAt(i)).getName());
    }
    ruleInputList.setListData(inputNames);
  }

  private void refreshTimingButtons() {
    int timing = ruleInFocus.getTiming();
    if (timing == RuleTiming.CONTINUOUS) {
      continuousButton.setSelected(true);
      triggerButton.setSelected(false);
      destroyerButton.setSelected(false);
      joinCheckBox.setEnabled(false);
    } else if (timing == RuleTiming.TRIGGER) {
      triggerButton.setSelected(true);
      continuousButton.setSelected(false);
      destroyerButton.setSelected(false);
      joinCheckBox.setEnabled(true);
    } else if (timing == RuleTiming.DESTROYER) {
      destroyerButton.setSelected(true);
      continuousButton.setSelected(false);
      triggerButton.setSelected(false);
      joinCheckBox.setEnabled(true);
    }
  }

  private void refreshJoinCheckBox() {
    joinCheckBox.setSelected(ruleInFocus.getExecuteOnJoins());
  }

  private void refreshVisibilityCheckBox() {
    visibilityCheckBox.setSelected(ruleInFocus.isVisibleInExplanatoryTool());
  }

  private void setupRuleInputListSelectionListenerStuff() // enables view/edit
                                                          // and remove buttons
                                                          // whenever a list
                                                          // item (rule input)
                                                          // is
  // selected
  {
    // Copied from a Java tutorial:
    ListSelectionModel rowSM = ruleInputList.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting())
          return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty() == false) {
          viewEditInputButton.setEnabled(true);
          removeInputButton.setEnabled(true);
        }
      }
    });
  }

  private void viewEditEffects() {
    int selectedIndex = participantList.getSelectedIndex();
    if (selectedIndex >= 0) // a participant is selected
    {
      String selectedString = (String) participantList.getSelectedValue();
      // move up in the list from the selected item 'til you find the
      // participant name:
      int i = (selectedIndex - 1);
      String item = (String) partNames.elementAt(i);
      while (item.startsWith("--")) {
        i--;
        item = (String) partNames.elementAt(i);
      }
      ActionTypeParticipant tempPart = actionInFocus.getParticipant(item
          .substring(0, (item.length() - 1))); // trim off
      // the colon to get the participant name
      SimSEObjectType tempType = tempPart.getSimSEObjectType(selectedString
          .substring(2, selectedString.indexOf(" ")));
      // trim off dashes at beginning to get the type name
      if ((objectTypeInFocus != null)
          && (((tempPart == participantInFocus) && (tempType != objectTypeInFocus)) || (tempPart != participantInFocus))) // selection
                                                                                                                          // is
                                                                                                                          // not
                                                                                                                          // the
                                                                                                                          // SimSEObjectType
                                                                                                                          // and
                                                                                                                          // participant
                                                                                                                          // that
                                                                                                                          // was
                                                                                                                          // already
                                                                                                                          // in
                                                                                                                          // focus
      {
        setParticipantInFocusDataFromForm();
        setParticipantInFocus(tempPart, tempType); // set the new focus
      } else if (objectTypeInFocus == null) {
        setParticipantInFocus(tempPart, tempType); // set the new focus
      }
    }
  }

  private Vector validateInput() // returns a vector of error messages (if any)
  {
    Vector messages = new Vector();
    Vector partEffects = ruleInFocus.getAllParticipantRuleEffects();
    // go through all ParticipantRuleEffects to validate them:
    for (int i = 0; i < partEffects.size(); i++) {
      ParticipantRuleEffect tempRuleEffect = (ParticipantRuleEffect) partEffects
          .elementAt(i);
      Vector partTypeEffects = tempRuleEffect.getAllParticipantTypeEffects();

      // go through all ParticipantTypeRuleEffects to validate them:
      for (int j = 0; j < partTypeEffects.size(); j++) {
        ParticipantTypeRuleEffect tempTypeRuleEffect = (ParticipantTypeRuleEffect) partTypeEffects
            .elementAt(j);
        Vector attRuleEffects = tempTypeRuleEffect.getAllAttributeEffects();
        // go through all ParticipantAttributeRuleEffects to validate them:
        for (int k = 0; k < attRuleEffects.size(); k++) {
          String message = new String();
          ParticipantAttributeRuleEffect tempAttEffect = (ParticipantAttributeRuleEffect) attRuleEffects
              .elementAt(k);
          if ((tempAttEffect.getAttribute().getType() == AttributeTypes.INTEGER)
              || (tempAttEffect.getAttribute().getType() == AttributeTypes.DOUBLE)) // numerical
                                                                                    // attribute
                                                                                    // (needs
                                                                                    // checking)
          {
            String effect = tempAttEffect.getEffect();
            if ((effect != null) && (effect.length() > 0)) // non-empty
            {
              // Check for num open paren > num close paren:
              int numOpen = 0;
              int numClose = 0;
              char[] text = effect.toCharArray();
              for (int m = 0; m < text.length; m++) {
                if (text[m] == '(') {
                  numOpen++;
                } else if (text[m] == ')') {
                  numClose++;
                }
              }
              if (numOpen > numClose) {
                message = ("Invalid expression for "
                    + tempRuleEffect.getParticipant().getName()
                    + " "
                    + tempTypeRuleEffect.getSimSEObjectType().getName()
                    + " "
                    + SimSEObjectTypeTypes.getText(tempTypeRuleEffect
                        .getSimSEObjectType().getType()) + " "
                    + tempAttEffect.getAttribute().getName() + " attribute: number of opening parentheses > number of closing parentheses");
              }

              // Check for ending with an invalid ending token:
              String lastToken = getLastToken(effect);
              int lastTokenType = getLastTokenType(effect);
              if ((lastToken.trim().equals("-"))
                  || (lastToken.trim().equals("."))
                  || (lastTokenType == OPEN_PAREN)
                  || (lastTokenType == OPERATOR)) // invalid ending tokens
              {
                if ((message != null) && (message.length() > 0)) // message
                                                                 // already has
                                                                 // some text
                {
                  // just concatenate the rest of it:
                  message = message
                      .concat("; incomplete expression / invalid ending token");
                } else // message empty at this point
                {
                  message = ("Invalid expression for "
                      + tempRuleEffect.getParticipant().getName()
                      + " "
                      + tempTypeRuleEffect.getSimSEObjectType().getName()
                      + " "
                      + SimSEObjectTypeTypes.getText(tempTypeRuleEffect
                          .getSimSEObjectType().getType()) + " "
                      + tempAttEffect.getAttribute().getName() + ": incomplete expression / invalid ending token");
                }
              }
              if ((message != null) && (message.length() > 0)) // message got
                                                               // instantiated
              {
                messages.add(message);
              }
            }
          }
        }
      }
    }
    return messages;
  }

  /*
   * Takes what's currently in the form for the participant in focus and sets
   * the current temporary copy of that participant with those values for the
   * object type in focus
   */
  private void setParticipantInFocusDataFromForm() {
    // timing:
    if (continuousButton.isSelected()) {
      ruleInFocus.setTiming(RuleTiming.CONTINUOUS);
    } else if (triggerButton.isSelected()) {
      ruleInFocus.setTiming(RuleTiming.TRIGGER);
    } else if (destroyerButton.isSelected()) {
      ruleInFocus.setTiming(RuleTiming.DESTROYER);
    }

    // executeOnJoins status:
    ruleInFocus.setExecuteOnJoins(joinCheckBox.isSelected()
        && joinCheckBox.isEnabled());

    // explanatory tool visibility:
    ruleInFocus.setVisibilityInExplanatoryTool(visibilityCheckBox.isSelected());

    if (objectTypeInFocus != null) {
      Vector attributes = objectTypeInFocus.getAllAttributes();
      for (int i = 0; i < attributes.size(); i++) {
        // get the participant attribute rule effect for the attribute:
        Attribute tempAtt = (Attribute) (attributes.elementAt(i));
        ParticipantAttributeRuleEffect attEffect = ruleInFocus
            .getParticipantRuleEffect(participantInFocus.getName())
            .getParticipantTypeEffect(objectTypeInFocus).getAttributeEffect(
                tempAtt.getName());

        // set the effect from the corresponding text field:
        attEffect.setEffect(((JTextField) textFields.elementAt(i)).getText());
      }

      // set the other actions effects:
      if (activateButton.isSelected()) // activate all
      {
        ruleInFocus.getParticipantRuleEffect(participantInFocus.getName())
            .getParticipantTypeEffect(objectTypeInFocus).setOtherActionsEffect(
                OtherActionsEffect.ACTIVATE_ALL);
      } else if (deactivateButton.isSelected()) // deactivate all
      {
        ruleInFocus.getParticipantRuleEffect(participantInFocus.getName())
            .getParticipantTypeEffect(objectTypeInFocus).setOtherActionsEffect(
                OtherActionsEffect.DEACTIVATE_ALL);
      } else if (noneButton.isSelected()) // none
      {
        ruleInFocus.getParticipantRuleEffect(participantInFocus.getName())
            .getParticipantTypeEffect(objectTypeInFocus).setOtherActionsEffect(
                OtherActionsEffect.NONE);
      }
    }
  }

  private void setParticipantInFocus(ActionTypeParticipant part,
      SimSEObjectType type) // makes the specified participant and object
  // type the focus of the gui
  {
    participantInFocus = part;
    objectTypeInFocus = type;

    partEffectsTitle.setText(participantInFocus.getName() + " "
        + objectTypeInFocus.getName() + " "
        + SimSEObjectTypeTypes.getText(objectTypeInFocus.getType())
        + " Effects:"); // set the title

    // clear data structures:
    textFields.removeAllElements();
    effectsMiddleMiddlePane.removeAll();

    JPanel middlePane = new JPanel();
    JPanel namePanel = new JPanel(new GridLayout(0, 1, 0, 3));
    JPanel equalsPanel = new JPanel(new GridLayout(0, 1, 0, 3));
    JPanel expPanel = new JPanel(new GridLayout(0, 1, 0, 1));
    // get all attributes:
    Vector attributes = type.getAllAttributes();
    for (int index = 0; index < attributes.size(); index++) // go through each
                                                            // one and add its
                                                            // info:
    {
      Attribute att = (Attribute) (attributes.elementAt(index));

      // name and type info:
      StringBuffer attLabel = new StringBuffer(att.getName() + " ("
          + AttributeTypes.getText(att.getType())); // attribute name & type
      if ((att.getType() == AttributeTypes.INTEGER)
          || (att.getType() == AttributeTypes.DOUBLE)) // double or integer
                                                       // attribute
      {
        if (((NumericalAttribute) att).isMinBoundless() == false) {
          attLabel.append(", min value = "
              + ((NumericalAttribute) att).getMinValue().toString());
        }
        if (((NumericalAttribute) att).isMaxBoundless() == false) {
          attLabel.append(", max value = "
              + ((NumericalAttribute) att).getMaxValue().toString());
        }
      }
      attLabel.append(")");
      JLabel label = new JLabel(attLabel.toString());
      label.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
      namePanel.add(label);

      // equals sign:
      JLabel equalsLabel = new JLabel(" = ");
      equalsLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
      equalsPanel.add(equalsLabel);

      // text field:
      JTextField expTextField = new JTextField(500);
      if (att.isKey()) {
        expTextField.setEditable(false);
      }
      else {
        expTextField.addMouseListener(this);
	      // set the text field to the correct value:
	      ParticipantRuleEffect a = ruleInFocus
	          .getParticipantRuleEffect(participantInFocus.getName());
	      ParticipantTypeRuleEffect b = a
	          .getParticipantTypeEffect(objectTypeInFocus);
	      ParticipantAttributeRuleEffect c = b.getAttributeEffect(att.getName());
	      String exp = c.getEffect();
	      if ((exp != null) && (exp.length() > 0)) // non-empty
	      {
	        expTextField.setText(exp);
	      }
	
	      // make it so whenever the text field gains focus, the button pad will be
	      // refreshed:
	      FocusListener l = new FocusListener() {
	        public void focusGained(FocusEvent ev) {
	          if (!justClosedButtonPad) // valid focus gained, not just as a result
	                                    // of closing the button pad -- if you don't
	                                    // do this, it
	          // will bring the first text field into focus every time you close the
	          // button pad.
	          {
	            if (lastFocusedTextField != null) // there was a text field in focus
	                                              // before
	            {
	              lastFocusedTextField.setBackground(null);
	            }
	            lastFocusedTextField = (JTextField) ev.getSource();
	            lastFocusedTextField.setBackground(Color.YELLOW);
	            refreshButtonPad();
	            buttonPadButton.setEnabled(true);
	          } else // just closed button pad
	          {
	            lastFocusedTextField.requestFocus();
	            justClosedButtonPad = false;
	          }
	        }
	
	        public void focusLost(FocusEvent ev) {
	        }
	      };
	      expTextField.addFocusListener(l);
      }

      expPanel.add(expTextField);
      textFields.add(expTextField);
    }

    // other actions effect:
    if (ruleInFocus.getParticipantRuleEffect(participantInFocus.getName())
        .getParticipantTypeEffect(objectTypeInFocus).getOtherActionsEffect()
        .equals(OtherActionsEffect.ACTIVATE_ALL)) {
      activateButton.setSelected(true);
      deactivateButton.setSelected(false);
      noneButton.setSelected(false);
    } else if (ruleInFocus.getParticipantRuleEffect(
        participantInFocus.getName()).getParticipantTypeEffect(
        objectTypeInFocus).getOtherActionsEffect().equals(
        OtherActionsEffect.DEACTIVATE_ALL)) {
      deactivateButton.setSelected(true);
      activateButton.setSelected(false);
      noneButton.setSelected(false);
    } else // (ruleInFocus.getParticipantRuleEffect(participantInFocus.getName()).getParticipantTypeEffect(objectTypeInFocus).getOtherActionsEffect().equals(OtherActionsEffect.NONE))
    {
      noneButton.setSelected(true);
      activateButton.setSelected(false);
      deactivateButton.setSelected(false);
    }

    buttonPadButton.setEnabled(false);

    middlePane.add(namePanel);
    middlePane.add(equalsPanel);
    middlePane.add(expPanel);
    effectsMiddleMiddlePane.add(middlePane);
    effectsMiddleMiddlePane.add(otherActEffectPanel);

    repaint();
  }

  private void newRuleInput() // creates a new rule input and adds it to the
                              // action in focus
  {
    String response = JOptionPane.showInputDialog(null,
        "Enter a name for this Rule Input:", "Enter Rule Input Name",
        JOptionPane.QUESTION_MESSAGE); // Show input dialog
    if (response != null) {
      if (ruleInputNameInputValid(response) == false) // input is invalid
      {
        JOptionPane
            .showMessageDialog(
                null,
                "Please enter a unique name, between 1 and 40 alphanumeric characters, and no spaces",
                "Invalid Input", JOptionPane.WARNING_MESSAGE); // warn user to
                                                               // enter a valid
                                                               // name
        newRuleInput(); // try again
      } else // user has entered valid input
      {
        RuleInput newInput = new RuleInput(response); // create new rule input
        RuleInputInfoForm rInfoForm = new RuleInputInfoForm(this, ruleInFocus,
            newInput);

        // Makes it so this gui will refresh itself after this rule input info
        // form closes:
        WindowFocusListener l = new WindowFocusListener() {
          public void windowLostFocus(WindowEvent ev) {
            refreshRuleInputList();
            viewEditInputButton.setEnabled(false);
            removeInputButton.setEnabled(false);
          }

          public void windowGainedFocus(WindowEvent ev) {
          }
        };
        rInfoForm.addWindowFocusListener(l);
      }
    }
  }

  private boolean ruleInputNameInputValid(String input) // returns true if input
                                                        // is a valid rule input
                                                        // name, false if not
  {
    char[] cArray = input.toCharArray();

    // Check for length constraints:
    if ((cArray.length == 0) || (cArray.length > 40)) // user has entered
                                                      // nothing or a string
                                                      // longer than 40 chars
    {
      return false;
    }

    // Check for invalid characters:
    for (int i = 0; i < cArray.length; i++) {
      if ((Character.isLetterOrDigit(cArray[i])) == false) // character is not a
                                                           // letter or a digit
                                                           // (hence, invalid)
      {
        return false;
      }
    }

    // Check for uniqueness of name:
    Vector existingInputs = ruleInFocus.getAllRuleInputs();
    for (int i = 0; i < existingInputs.size(); i++) {
      RuleInput tempInput = (RuleInput) existingInputs.elementAt(i);
      if (tempInput.getName().equalsIgnoreCase(input)) // name not unique
      {
        return false;
      }
    }
    return true; // none of the invalid conditions exist
  }

  private void editRuleInput(RuleInput input) // brings up the window for
                                              // editing an existing rule input
  {
    RuleInputInfoForm rInfoForm = new RuleInputInfoForm(this, ruleInFocus,
        input);

    // Makes it so this gui will refresh itself after this rule input info form
    // closes:
    WindowFocusListener l = new WindowFocusListener() {
      public void windowLostFocus(WindowEvent ev) {
        refreshRuleInputList();
        viewEditInputButton.setEnabled(false);
        removeInputButton.setEnabled(false);
      }

      public void windowGainedFocus(WindowEvent ev) {
      }
    };
    rInfoForm.addWindowFocusListener(l);
  }

  private void removeRuleInput(String inputName) // removes the rule input with
                                                 // the specified name from the
                                                 // rule in focus
  {

    int choice = JOptionPane.showConfirmDialog(null, ("Really remove "
        + inputName + "rule input?"), "Confirm Rule Input Removal",
        JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      ruleInFocus.removeRuleInput((String) ruleInputList.getSelectedValue()); // remove
                                                                              // rule
                                                                              // input
                                                                              // from
                                                                              // rule
                                                                              // in
                                                                              // focus
      viewEditInputButton.setEnabled(false);
      removeInputButton.setEnabled(false);
      refreshRuleInputList();
    } else // choice == JOptionPane.NO_OPTION
    {
    }
  }

  private void newButtonPad() // creates a new button pad for the participant in
                              // focus
  {
    // bring up new button pad:
    refreshButtonPad();
    refreshButtonPad(getLastTokenType(lastFocusedTextField.getText()));
    echoedTextField.setText(lastFocusedTextField.getText());
    buttGUI = new ButtonPadGUI(this, inputButton, attributeThisPartButton,
        attributeOtherPartButton, numObjectsButton, numActionsThisPartButton,
        numActionsOtherPartButton, timeButtons, digitButtons, operatorButtons,
        otherButtons, echoedTextField);

    // Makes it so this gui will refresh itself after this rule input info form
    // closes:
    WindowFocusListener l = new WindowFocusListener() {
      public void windowLostFocus(WindowEvent ev) {
        justClosedButtonPad = true;
      }

      public void windowGainedFocus(WindowEvent ev) {
      }
    };
    buttGUI.addWindowFocusListener(l);

    buttonPadButton.setEnabled(false);
  }

  private void refreshButtonPad() // refreshes the button pad by
                                  // enabling/disabling buttons
  // according to the attribute in focus and what's in the field in focus
  {
    // get the attInFocus:
    Attribute attInFocus = getAttributeInFocus();

    if ((attInFocus != null)
        && ((attInFocus.getType() == AttributeTypes.STRING) || (attInFocus
            .getType() == AttributeTypes.BOOLEAN))) // string or boolean
    // attribute
    {
      Vector inputs = ruleInFocus.getAllRuleInputs();
      inputButton.setEnabled(false);
      if (attInFocus.getType() == AttributeTypes.STRING) // string attribute
      {
        // enable/disable input button, depending on what kind of inputs
        // currently exist:
        for (int i = 0; i < inputs.size(); i++) {
          if (((RuleInput) inputs.elementAt(i)).getType().equals(
              InputType.STRING)) // string rule input type
          {
            inputButton.setEnabled(true);
            break;
          }
        }
        // enable string button:
        stringButton.setEnabled(true);
        // disable boolean button:
        booleanButton.setEnabled(false);
      } else if (attInFocus.getType() == AttributeTypes.BOOLEAN) // boolean
                                                                 // attribute
      {
        // enable/disable input button, depending on what kind of inputs
        // currently exist:
        for (int i = 0; i < inputs.size(); i++) {
          if (((RuleInput) inputs.elementAt(i)).getType().equals(
              InputType.BOOLEAN)) // boolean rule input type
          {
            inputButton.setEnabled(true);
            break;
          }
        }
        // enable boolean button:
        booleanButton.setEnabled(true);
        // disable string button:
        stringButton.setEnabled(false);
      }
      // enable buttons:
      attributeThisPartButton.setEnabled(true);
      // disable other participant attribute button:
      attributeOtherPartButton.setEnabled(false);
      // disable all numerical buttons:
      numObjectsButton.setEnabled(false);
      numActionsThisPartButton.setEnabled(false);
      numActionsOtherPartButton.setEnabled(false);
      for (int i = 0; i < timeButtons.size(); i++) {
        ((JButton) timeButtons.elementAt(i)).setEnabled(false);
      }
      for (int i = 0; i < digitButtons.size(); i++) {
        ((JButton) digitButtons.elementAt(i)).setEnabled(false);
      }
      for (int i = 0; i < operatorButtons.size(); i++) {
        ((JButton) operatorButtons.elementAt(i)).setEnabled(false);
      }
      randomButton.setEnabled(false);
    } else if ((attInFocus != null)
        && ((attInFocus.getType() == AttributeTypes.INTEGER) || (attInFocus
            .getType() == AttributeTypes.DOUBLE))) // integer or double
    // attribute
    {
      // enable or disable input button, depending on the types of inputs that
      // currently exist:
      Vector inputs = ruleInFocus.getAllRuleInputs();
      inputButton.setEnabled(false);
      for (int i = 0; i < inputs.size(); i++) {
        if ((((RuleInput) inputs.elementAt(i)).getType()
            .equals(InputType.INTEGER))
            || (((RuleInput) inputs.elementAt(i)).getType()
                .equals(InputType.DOUBLE))) // numerical rule input type
        {
          inputButton.setEnabled(true);
          break;
        }
      }
      // disable string & boolean buttons:
      stringButton.setEnabled(false);
      booleanButton.setEnabled(false);
      // enable all other buttons:
      attributeThisPartButton.setEnabled(true);
      attributeOtherPartButton.setEnabled(true);
      numObjectsButton.setEnabled(true);
      numActionsThisPartButton.setEnabled(true);
      numActionsOtherPartButton.setEnabled(true);
      for (int i = 0; i < timeButtons.size(); i++) {
        ((JButton) timeButtons.elementAt(i)).setEnabled(true);
      }
      for (int i = 0; i < digitButtons.size(); i++) {
        ((JButton) digitButtons.elementAt(i)).setEnabled(true);
      }
      for (int i = 0; i < operatorButtons.size(); i++) {
        ((JButton) operatorButtons.elementAt(i)).setEnabled(true);
      }
      randomButton.setEnabled(true);
      refreshButtonPad(OPERATOR); // refresh the button pad as if an operator
                                  // has just been entered
    }
  }

  private void refreshButtonPad(int lastTokenType) // refreshes the button pad
                                                   // by disabling buttons
                                                   // according to what the last
                                                   // entry into
  // the field was
  {
    if ((getAttributeInFocus().getType() == AttributeTypes.STRING)
        || (getAttributeInFocus().getType() == AttributeTypes.BOOLEAN))
    // boolean or string attribute
    {
      if ((lastFocusedTextField.getText() != null)
          && (lastFocusedTextField.getText().length() > 0)) // non-empty text
                                                            // field
      {
        disableAllButtons();
      } else // empty text field
      {
        // Rule input button:
        if (inputButton.isEnabled()) // if button is enabled, check if you need
                                     // to disable it or not:
        {
          Attribute attInFocus = getAttributeInFocus();
          Vector inputs = ruleInFocus.getAllRuleInputs();
          if (attInFocus.getType() == AttributeTypes.STRING) // string attribute
          {
            boolean disableButt = true;
            for (int i = 0; i < inputs.size(); i++) {
              if (((RuleInput) inputs.elementAt(i)).getType().equals(
                  InputType.STRING)) // string rule input type
              {
                disableButt = false;
                break;
              }
            }
            if (disableButt) {
              inputButton.setEnabled(false);
            }
          } else if (attInFocus.getType() == AttributeTypes.BOOLEAN) // boolean
                                                                     // attribute
          {
            boolean disableButt = true;
            for (int i = 0; i < inputs.size(); i++) {
              if (((RuleInput) inputs.elementAt(i)).getType().equals(
                  InputType.BOOLEAN)) // boolean rule input type
              {
                disableButt = false;
                break;
              }
            }
            if (disableButt) {
              inputButton.setEnabled(false);
            }
          }
        }
      }
    } else {
      if (lastTokenType == OPEN_PAREN) {
        // disable all operator buttons:
        for (int i = 0; i < operatorButtons.size(); i++) {
          ((JButton) operatorButtons.elementAt(i)).setEnabled(false);
        }
        // enable neg. sign button:
        buttonMinus.setEnabled(true);

        // enable open paren button:
        buttonOpenParen.setEnabled(true);

        // disable close paren button:
        buttonCloseParen.setEnabled(false);

        // enable all other buttons:
        inputButton.setEnabled(true);
        attributeThisPartButton.setEnabled(true);
        attributeOtherPartButton.setEnabled(true);
        numObjectsButton.setEnabled(true);
        numActionsThisPartButton.setEnabled(true);
        numActionsOtherPartButton.setEnabled(true);
        for (int i = 0; i < timeButtons.size(); i++) {
          ((JButton) timeButtons.elementAt(i)).setEnabled(true);
        }
        for (int i = 0; i < digitButtons.size(); i++) {
          ((JButton) digitButtons.elementAt(i)).setEnabled(true);
        }
        randomButton.setEnabled(true);
      }

      else if ((lastTokenType == CLOSED_PAREN) || (lastTokenType == NUMBER)) {
        // disable all buttons except operators (enable those):
        inputButton.setEnabled(false);
        attributeThisPartButton.setEnabled(false);
        attributeOtherPartButton.setEnabled(false);
        numObjectsButton.setEnabled(false);
        numActionsThisPartButton.setEnabled(false);
        numActionsOtherPartButton.setEnabled(false);
        for (int i = 0; i < timeButtons.size(); i++) {
          ((JButton) timeButtons.elementAt(i)).setEnabled(false);
        }
        for (int i = 0; i < digitButtons.size(); i++) {
          ((JButton) digitButtons.elementAt(i)).setEnabled(false);
        }
        for (int i = 0; i < operatorButtons.size(); i++) {
          ((JButton) operatorButtons.elementAt(i)).setEnabled(true);
        }
        randomButton.setEnabled(false);
        // disable open paren:
        buttonOpenParen.setEnabled(false);
        // disable closing paren if necessary:
        if (numOpenParen() <= numClosingParen()) {
          buttonCloseParen.setEnabled(false);
        }
      }

      else if (lastTokenType == OPERATOR) {
        // disable all operator buttons:
        for (int i = 0; i < operatorButtons.size(); i++) {
          ((JButton) operatorButtons.elementAt(i)).setEnabled(false);
        }
        // enable neg. sign button:
        buttonMinus.setEnabled(true);
        // enable all other buttons:
        for (int i = 0; i < digitButtons.size(); i++) {
          ((JButton) digitButtons.elementAt(i)).setEnabled(true);
        }
        randomButton.setEnabled(true);
        for (int i = 0; i < timeButtons.size(); i++) {
          ((JButton) timeButtons.elementAt(i)).setEnabled(true);
        }
        inputButton.setEnabled(true);
        attributeThisPartButton.setEnabled(true);
        attributeOtherPartButton.setEnabled(true);
        numObjectsButton.setEnabled(true);
        numActionsThisPartButton.setEnabled(true);
        numActionsOtherPartButton.setEnabled(true);
        // enable open paren button:
        buttonOpenParen.setEnabled(true);
      }

      else if (lastTokenType == DIGIT) {
        // disable all other number buttons:
        randomButton.setEnabled(false);
        for (int i = 0; i < timeButtons.size(); i++) {
          ((JButton) timeButtons.elementAt(i)).setEnabled(false);
        }
        inputButton.setEnabled(false);
        attributeThisPartButton.setEnabled(false);
        attributeOtherPartButton.setEnabled(false);
        numObjectsButton.setEnabled(false);
        numActionsThisPartButton.setEnabled(false);
        numActionsOtherPartButton.setEnabled(false);

        // enable other buttons:
        for (int i = 0; i < operatorButtons.size(); i++) {
          ((JButton) operatorButtons.elementAt(i)).setEnabled(true);
        }
        for (int i = 0; i < digitButtons.size(); i++) {
          ((JButton) digitButtons.elementAt(i)).setEnabled(true);
        }

        // disable "." button if necessary:
        String textFieldText = lastFocusedTextField.getText();
        if (getLastToken(textFieldText).indexOf('.') != -1) // dot has already
                                                            // appeared in this
                                                            // number
        {
          buttonDot.setEnabled(false);
        }
        // disable open paren:
        buttonOpenParen.setEnabled(false);

        // disable closing paren if necessary:
        if (numOpenParen() <= numClosingParen()) {
          buttonCloseParen.setEnabled(false);
        }

        if (getLastToken(lastFocusedTextField.getText()).equals("-")) // negative
                                                                      // sign
        {
          // disable minus button:
          buttonMinus.setEnabled(false);
          // enable number buttons:
          randomButton.setEnabled(true);
          for (int i = 0; i < timeButtons.size(); i++) {
            ((JButton) timeButtons.elementAt(i)).setEnabled(true);
          }
          inputButton.setEnabled(true);
          attributeThisPartButton.setEnabled(true);
          attributeOtherPartButton.setEnabled(true);
          numObjectsButton.setEnabled(true);
          numActionsThisPartButton.setEnabled(true);
          numActionsOtherPartButton.setEnabled(true);
          // disable operator buttons:
          for (int i = 0; i < operatorButtons.size(); i++) {
            ((JButton) operatorButtons.elementAt(i)).setEnabled(false);
          }
          // enable open paren:
          buttonOpenParen.setEnabled(true);
        } else if (getLastToken(lastFocusedTextField.getText()).endsWith(".")) // dot
        {
          // disable operator buttons:
          for (int i = 0; i < operatorButtons.size(); i++) {
            ((JButton) operatorButtons.elementAt(i)).setEnabled(false);
          }
          // disable closing parens:
          buttonCloseParen.setEnabled(false);
        }
      }

      // Rule input button:
      if (inputButton.isEnabled()) // if button is enabled, check if you need to
                                   // disable it or not:
      {
        Attribute attInFocus = getAttributeInFocus();
        Vector inputs = ruleInFocus.getAllRuleInputs();
        if ((attInFocus.getType() == AttributeTypes.INTEGER)
            || (attInFocus.getType() == AttributeTypes.DOUBLE)) // integer or
                                                                // double
        // attribute
        {
          // enable or disable input button, depending on the types of inputs
          // that currently exist:
          boolean disableButt = true;
          for (int i = 0; i < inputs.size(); i++) {
            if ((((RuleInput) inputs.elementAt(i)).getType()
                .equals(InputType.INTEGER))
                || (((RuleInput) inputs.elementAt(i)).getType()
                    .equals(InputType.DOUBLE))) // numerical rule input type
            {
              disableButt = false;
              break;
            }
          }
          if (disableButt) {
            inputButton.setEnabled(false);
          }
        }
      }
    }
  }

  private int numOpenParen() // returns the number of open parentheses that are
                             // in the last focused text field
  {
    int num = 0;
    char[] text = lastFocusedTextField.getText().toCharArray();
    for (int i = 0; i < text.length; i++) {
      if (text[i] == '(') {
        num++;
      }
    }
    return num;
  }

  private int numClosingParen() // returns the number of closing parentheses
                                // that are in the last focused text field
  {
    int num = 0;
    char[] text = lastFocusedTextField.getText().toCharArray();
    for (int i = 0; i < text.length; i++) {
      if (text[i] == ')') {
        num++;
      }
    }
    return num;
  }

  private void disableAllButtons() // disables all buttons in the button pad GUI
                                   // (except backspace)
  {
    inputButton.setEnabled(false);
    attributeThisPartButton.setEnabled(false);
    attributeOtherPartButton.setEnabled(false);
    numObjectsButton.setEnabled(false);
    numActionsThisPartButton.setEnabled(false);
    numActionsOtherPartButton.setEnabled(false);
    for (int i = 0; i < timeButtons.size(); i++) {
      ((JButton) timeButtons.elementAt(i)).setEnabled(false);
    }
    for (int i = 0; i < digitButtons.size(); i++) {
      ((JButton) digitButtons.elementAt(i)).setEnabled(false);
    }
    for (int i = 0; i < operatorButtons.size(); i++) {
      ((JButton) operatorButtons.elementAt(i)).setEnabled(false);
    }
    randomButton.setEnabled(false);
    stringButton.setEnabled(false);
    booleanButton.setEnabled(false);
  }

  private Attribute getAttributeInFocus() // returns the Attribute that is
                                          // currently in focus, according to
                                          // the most recently focused
  // text field
  {
    // figure out the index of the text field most recently in focus:
    for (int i = 0; i < textFields.size(); i++) {
      if (((JTextField) textFields.elementAt(i)) == lastFocusedTextField) {
        // get the attribute in focus:
        return ((Attribute) (objectTypeInFocus.getAllAttributes().elementAt(i)));
      }
    }
    return null;
  }

  private void inputButtonChosen() // takes care of what happens when the user
                                   // clicks the input button on the button pad
  {
    Vector inputs = ruleInFocus.getAllRuleInputs();

    // get attInFocus:
    Attribute attInFocus = getAttributeInFocus();

    // make a vector of rule input names for choosing from:
    Vector choices = new Vector();
    if (attInFocus.getType() == AttributeTypes.STRING) // string attribute
    {
      for (int i = 0; i < inputs.size(); i++) {
        RuleInput tempInput = (RuleInput) inputs.elementAt(i);
        if (tempInput.getType().equals(InputType.STRING)) {
          choices.add(((RuleInput) inputs.elementAt(i)).getName());
        }
      }
    } else if (attInFocus.getType() == AttributeTypes.BOOLEAN) // boolean
                                                               // attribute
    {
      for (int i = 0; i < inputs.size(); i++) {
        RuleInput tempInput = (RuleInput) inputs.elementAt(i);
        if (tempInput.getType().equals(InputType.BOOLEAN)) {
          choices.add(((RuleInput) inputs.elementAt(i)).getName());
        }
      }
    } else if ((attInFocus.getType() == AttributeTypes.INTEGER)
        || (attInFocus.getType() == AttributeTypes.DOUBLE)) // numerical
    // attribute
    {
      for (int i = 0; i < inputs.size(); i++) {
        RuleInput tempInput = (RuleInput) inputs.elementAt(i);
        if ((tempInput.getType().equals(InputType.INTEGER))
            || (tempInput.getType().equals(InputType.DOUBLE))) {
          choices.add(((RuleInput) inputs.elementAt(i)).getName());
        }
      }
    }
    Object choiceInFocus;
    if (choices.size() == 0) // no inputs
    {
      choiceInFocus = null;
    } else {
      choiceInFocus = choices.elementAt(0);
    }
    // bring up dialog for choosing a rule input:
    String response = (String) JOptionPane.showInputDialog(this,
        "Choose a rule input:", "Rule Input", JOptionPane.PLAIN_MESSAGE, null,
        choices.toArray(), choiceInFocus);
    if ((response != null) && (response.length() > 0)) // a selection was made
    {
      // append the text for the selected input to the text field:
      if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
          && (getLastToken(lastFocusedTextField.getText()).equals("-")))
      // negative sign was last token
      {
        setFocusedTextFieldText(lastFocusedTextField.getText().trim().concat(
            "input-" + response + " "));
      } else {
        setFocusedTextFieldText(lastFocusedTextField.getText().concat(
            "input-" + response + " "));
      }
      if ((attInFocus.getType() == AttributeTypes.INTEGER)
          || (attInFocus.getType() == AttributeTypes.DOUBLE)) {
        refreshButtonPad(NUMBER);
      } else // string/boolean attribute
      {
        disableAllButtons();
      }
    }
  }

  private void attributeThisPartButtonChosen() // takes care of what to do when
                                               // this button is clicked
  {
    // attributes of the object type in focus:
    Vector atts = objectTypeInFocus.getAllAttributes();
    Attribute attInFocus = getAttributeInFocus();

    // make a vector of attribute names for choosing from:
    Vector choices = new Vector();
    if (attInFocus.getType() == AttributeTypes.STRING) // string attribute
    {
      for (int i = 0; i < atts.size(); i++) {
        Attribute tempAtt = (Attribute) atts.elementAt(i);
        if (tempAtt.getType() == AttributeTypes.STRING) {
          choices.add(((Attribute) atts.elementAt(i)).getName());
        }
      }
    }
    if (attInFocus.getType() == AttributeTypes.BOOLEAN) // boolean attribute
    {
      for (int i = 0; i < atts.size(); i++) {
        Attribute tempAtt = (Attribute) atts.elementAt(i);
        if (tempAtt.getType() == AttributeTypes.BOOLEAN) {
          choices.add(((Attribute) atts.elementAt(i)).getName());
        }
      }
    } else if ((attInFocus.getType() == AttributeTypes.INTEGER)
        || (attInFocus.getType() == AttributeTypes.DOUBLE)) // numerical
    // attribute
    {
      for (int i = 0; i < atts.size(); i++) {
        Attribute tempAtt = (Attribute) atts.elementAt(i);
        if ((tempAtt.getType() == AttributeTypes.INTEGER)
            || (tempAtt.getType() == AttributeTypes.DOUBLE)) {
          choices.add(((Attribute) atts.elementAt(i)).getName());
        }
      }
    }
    Object choiceInFocus;
    if (choices.size() == 0) // no inputs
    {
      choiceInFocus = null;
    } else {
      choiceInFocus = choices.elementAt(0);
    }
    // bring up dialog for choosing a rule input:
    String response = (String) JOptionPane.showInputDialog(this,
        "Choose an attribute:", "Attribute", JOptionPane.PLAIN_MESSAGE, null,
        choices.toArray(), choiceInFocus);
    if ((response != null) && (response.length() > 0)) // a selection was made
    {
      // append the text for the selected input to the text field:
      if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
          && (getLastToken(lastFocusedTextField.getText()).equals("-")))
      // negative sign was last token
      {
        setFocusedTextFieldText(lastFocusedTextField.getText().trim().concat(
            "this" + objectTypeInFocus.getName() + "-"
                + SimSEObjectTypeTypes.getText(objectTypeInFocus.getType())
                + ":" + response + " "));
      } else {
        setFocusedTextFieldText(lastFocusedTextField.getText().concat(
            "this" + objectTypeInFocus.getName() + "-"
                + SimSEObjectTypeTypes.getText(objectTypeInFocus.getType())
                + ":" + response + " "));
      }
      if ((attInFocus.getType() == AttributeTypes.INTEGER)
          || (attInFocus.getType() == AttributeTypes.DOUBLE)) {
        refreshButtonPad(NUMBER);
      } else // string/boolean attribute
      {
        disableAllButtons();
      }
    }
  }

  private void attributeOtherPartButtonChosen() // takes care of what to do when
                                                // this button is clicked
  {
    Attribute attInFocus = getAttributeInFocus();
    String oldText = lastFocusedTextField.getText(); // temporarily hold the
                                                     // text from the text field
    boolean trimWhitespace = false;
    if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
        && (getLastToken(lastFocusedTextField.getText()).equals("-")))
    // negative sign was last token
    {
      trimWhitespace = true;
    }
    OtherParticipantAttributeDialog opad = new OtherParticipantAttributeDialog(
        this, actionInFocus.getAllParticipants(), lastFocusedTextField,
        echoedTextField, attInFocus.getType(), trimWhitespace);
    if ((attInFocus.getType() == AttributeTypes.INTEGER)
        || (attInFocus.getType() == AttributeTypes.DOUBLE)) {
      if (lastFocusedTextField.getText().equals(oldText) == false) // text has
                                                                   // changed
      {
        refreshButtonPad(NUMBER);
      }
    } else // string/boolean attribute
    {
      if (lastFocusedTextField.getText().equals(oldText) == false) // text has
                                                                   // changed
      {
        disableAllButtons();
      }
    }
  }

  private void numObjectsButtonChosen() // takes care of what to do when this
                                        // button is clicked
  {
    String oldText = lastFocusedTextField.getText(); // temporarily hold the
                                                     // text from the text field
    boolean trimWhitespace = false;
    if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
        && (getLastToken(lastFocusedTextField.getText()).equals("-")))
    // negative sign was last token
    {
      trimWhitespace = true;
    }
    NumParticipantsDialog npd = new NumParticipantsDialog(this, actionInFocus
        .getAllParticipants(), lastFocusedTextField, echoedTextField,
        trimWhitespace);
    if (lastFocusedTextField.getText().equals(oldText) == false) // text has
                                                                 // changed
    {
      refreshButtonPad(NUMBER);
    }
  }

  private void numActionsThisPartButtonChosen() // takes care of what to do when
                                                // this button is clicked
  {
    String oldText = lastFocusedTextField.getText(); // temporarily hold the
                                                     // text from the text field
    boolean trimWhitespace = false;
    if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
        && (getLastToken(lastFocusedTextField.getText()).equals("-")))
    // negative sign was last token
    {
      trimWhitespace = true;
    }
    NumActionsThisPartDialog dialog = new NumActionsThisPartDialog(this,
        participantInFocus, objectTypeInFocus, actions, lastFocusedTextField,
        echoedTextField, trimWhitespace);
    if (lastFocusedTextField.getText().equals(oldText) == false) // text has
                                                                 // changed
    {
      refreshButtonPad(NUMBER);
    }
  }

  private void numActionsOtherPartButtonChosen() // takes care of what to do
                                                 // when this button is clicked
  {
    String oldText = lastFocusedTextField.getText(); // temporarily hold the
                                                     // text from the text field
    boolean trimWhitespace = false;
    if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
        && (getLastToken(lastFocusedTextField.getText()).equals("-")))
    // negative sign was last token
    {
      trimWhitespace = true;
    }
    NumActionsOtherPartDialog dialog = new NumActionsOtherPartDialog(this,
        actionInFocus.getAllParticipants(), actions, lastFocusedTextField,
        echoedTextField, trimWhitespace);
    if (lastFocusedTextField.getText().equals(oldText) == false) // text has
                                                                 // changed
    {
      refreshButtonPad(NUMBER);
    }
  }

  private void actTimeElapsedButtonChosen() // takes care of what to do when
                                            // this button is clicked
  {
    String text = "actionTimeElapsed";
    // append the text for the selected input to the text field:
    if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
        && (getLastToken(lastFocusedTextField.getText()).equals("-")))
    // negative sign was last token
    {
      setFocusedTextFieldText(lastFocusedTextField.getText().trim().concat(
          text + " "));
    } else {
      setFocusedTextFieldText(lastFocusedTextField.getText().concat(text + " "));
    }
    refreshButtonPad(NUMBER);
  }

  private void randomButtonChosen() // takes care of what to do when this button
                                    // is clicked
  {
    String oldText = lastFocusedTextField.getText(); // temporarily hold the
                                                     // text from the text field
    boolean trimWhitespace = false;
    if ((getLastTokenType(lastFocusedTextField.getText()) == DIGIT)
        && (getLastToken(lastFocusedTextField.getText()).equals("-")))
    // negative sign was last token
    {
      trimWhitespace = true;
    }
    RandomFunctionDialog dialog = new RandomFunctionDialog(this,
        lastFocusedTextField, echoedTextField, trimWhitespace);
    if (lastFocusedTextField.getText().equals(oldText) == false) // text has
                                                                 // changed
    {
      refreshButtonPad(NUMBER);
    }
  }

  private void digitButtonChosen(JButton digitButt) // takes care of what to do
                                                    // when this button is
                                                    // clicked
  {
    //System.out.println("*********** At the beginning of digitButtonChosen for
    // butt w/ this text: *" + digitButt.getText() + "*");
    if (getLastToken(lastFocusedTextField.getText()) == null) // empty text
                                                              // field
    {
      //System.out.println("Last token = null");
      //System.out.println("About to append the text *" + digitButt.getText() +
      // "* plus a space");
      // append the text:
      setFocusedTextFieldText(lastFocusedTextField.getText().concat(
          digitButt.getText() + " "));
    } else // non-empty text field
    {
      //System.out.println("Last token = *" +
      // getLastToken(lastFocusedTextField.getText()) + "*");
      int tokenType = getLastTokenType(lastFocusedTextField.getText());
      //System.out.println("Last token type = " + tokenType);
      if ((tokenType == OPEN_PAREN) || (tokenType == OPERATOR)) {
        //System.out.println("Last token type is open paren or operator --
        // about to append *" + digitButt.getText() + "* plus a space");
        // append the text:
        setFocusedTextFieldText(lastFocusedTextField.getText().concat(
            digitButt.getText() + " "));
      } else if (tokenType == DIGIT) {
        //System.out.println("Last token type is digit -- about to remove
        // trailing whitespace and append *" + digitButt.getText()
        //+ "* plus a space");
        // append the text, removing trailing white space from what was
        // previously in the text field:
        setFocusedTextFieldText(lastFocusedTextField.getText().trim().concat(
            digitButt.getText() + " "));
      }
    }
    //System.out.println("About to refresh button pad for digit just chosen");
    refreshButtonPad(DIGIT);
    //System.out.println("************* End of digitButtonChosen for butt w/
    // this text: *" + digitButt.getText() + "*");
  }

  private void operatorButtonChosen(JButton opButt) // takes care of what to do
                                                    // when this button is
                                                    // clicked
  {
    if (opButt.getText().equals("-")) {
      //System.out.println("************* At the beginning of
      // operatorButtonChosen function, and - button chosen");
      String lastToken = getLastToken(lastFocusedTextField.getText());
      //System.out.println("Last token = " + lastToken);
      //System.out.println("Last token type = " +
      // getLastTokenType(lastFocusedTextField.getText()));
      if ((lastToken == null) || (lastToken.length() == 0)
          || (getLastTokenType(lastFocusedTextField.getText()) == OPERATOR)
          || (getLastTokenType(lastFocusedTextField.getText()) == OPEN_PAREN)) // "-"
                                                                               // counts
                                                                               // for
                                                                               // a
                                                                               // negative
                                                                               // sign
                                                                               // in
                                                                               // this
                                                                               // case
      {
        //System.out.println("About to call digitButtonChosen -- minus button
        // counts for a digit");
        digitButtonChosen(opButt);
      } else // "-" counts for a minus operator
      {
        //System.out.println("About to append minus sign plus a space -- minus
        // button counts for a operator");
        // append text:
        setFocusedTextFieldText(lastFocusedTextField.getText().concat(
            opButt.getText() + " "));
        //System.out.println("About to refresh button pad for operator just
        // chosen");
        refreshButtonPad(OPERATOR);
      }
      //System.out.println("************* At the end of operatorButtonChosen
      // function, and - button chosen");
    } else // another operator
    {
      //System.out.println("A non minus sign operator chosen, about to append
      // text: *" + opButt.getText() + "* plus a space");
      // append text:
      setFocusedTextFieldText(lastFocusedTextField.getText().concat(
          opButt.getText() + " "));
      //System.out.println("About to refresh button pad for operator just
      // chosen.");
      refreshButtonPad(OPERATOR);
      //System.out.println("************* At the end of operatorButtonChosen
      // function, for button *" + opButt.getText() + "*");
    }
  }

  private void stringButtonChosen() // takes care of what to do when this button
                                    // is clicked
  {
    String response = JOptionPane.showInputDialog(null, "Enter the string:",
        "String", JOptionPane.QUESTION_MESSAGE); // Show input dialog
    if (response != null) {
      char[] cArray = response.toCharArray();

      // Check for length constraints:
      if ((cArray.length == 0) || (cArray.length > 500)) // user has entered
                                                         // nothing or a string
                                                         // longer than 500
                                                         // chars
      {
        JOptionPane.showMessageDialog(null,
            "Please enter a string between 1 and 500 characters",
            "Invalid Input", JOptionPane.WARNING_MESSAGE); // warn user to enter
                                                           // valid input
        stringButtonChosen(); // try again
      } else // valid input
      {
        setFocusedTextFieldText("\"" + response + "\"");
        disableAllButtons();
      }
    }
  }

  private void booleanButtonChosen() // takes care of what to do when this
                                     // button is clicked
  {
    Object[] choices = { "true", "false" };
    // bring up dialog for choosing a boolean value:
    String response = (String) JOptionPane.showInputDialog(this,
        "Choose a Boolean value:", "Boolean", JOptionPane.PLAIN_MESSAGE, null,
        choices, choices[0]);
    if ((response != null) && (response.length() > 0)) // a selection was made
    {
      // set the text field:
      setFocusedTextFieldText(response);
      disableAllButtons();
    }
  }

  private void backspaceButtonChosen() // takes care of what to do when this
                                       // button is clicked
  {
    if ((getAttributeInFocus().getType() == AttributeTypes.STRING)
        || (getAttributeInFocus().getType() == AttributeTypes.BOOLEAN))
    // string or boolean attribute
    {
      setFocusedTextFieldText(null); // clear text
      refreshButtonPad();
    } else // numerical attribute
    {
      String lastToken = getLastToken(lastFocusedTextField.getText());
      if ((lastToken != null) && (lastToken.length() > 0)) // there was text in
                                                           // the field
      {
        if (getLastTokenType(lastFocusedTextField.getText()) == DIGIT) // digit
                                                                       // token
        {
          // remove the last character:
          setFocusedTextFieldText(lastFocusedTextField.getText().trim()
              .substring(0,
                  (lastFocusedTextField.getText().trim().length() - 1)));
        } else // non-digit token
        {
          // remove the last token:
          setFocusedTextFieldText(lastFocusedTextField.getText().trim()
              .substring(
                  0,
                  (lastFocusedTextField.getText().trim().length() - lastToken
                      .length())));
        }
        if ((getLastTokenType(lastFocusedTextField.getText()) != -1)
            && (getLastTokenType(lastFocusedTextField.getText()) != OPEN_PAREN)) // last
                                                                                 // token
                                                                                 // was
                                                                                 // not
                                                                                 // null,
                                                                                 // not
                                                                                 // digit,
                                                                                 // and
                                                                                 // not
                                                                                 // open
                                                                                 // paren
        {
          // add a space to the text field:
          setFocusedTextFieldText(lastFocusedTextField.getText().trim() + " ");
        }
        if ((lastFocusedTextField.getText() == null)
            || (lastFocusedTextField.getText().length() == 0)) // all of the
                                                               // text has been
        // removed
        {
          refreshButtonPad(); // refresh button pad based on attribute type only
        } else {
          // refresh button pad based on last token type:
          refreshButtonPad(getLastTokenType(lastFocusedTextField.getText()));
        }
      }
    }
  }

  private int getLastTokenType(String tokenString) // returns the type of the
                                                   // last token in the token
                                                   // string
  {
    //System.out.println("************* At the beginning of getLastTokenType
    // with *" + tokenString + "* as thte argument");
    String token = getLastToken(tokenString);
    //System.out.println("Last token = *" + token + "*");
    if ((token == null) || (token.length() == 0)) // no last token
    {
      //System.out.println("No last token!");
      //System.out.println("*********At the end of getLastTokenType for
      // tokenstring = *" + tokenString + "*, and about to return -1");
      return -1;
    }
    if ((token.startsWith("input-")) || (token.startsWith("this"))
        || (token.startsWith("all")) || (token.startsWith("num"))
        || (token.startsWith("total")) || (token.startsWith("action"))
        || (token.startsWith("random")) || (token.startsWith("-input-"))
        || (token.startsWith("-this")) || (token.startsWith("-all"))
        || (token.startsWith("-num")) || (token.startsWith("-total"))
        || (token.startsWith("-action")) || (token.startsWith("-random"))) {
      //System.out.println("*********At the end of getLastTokenType for
      // tokenstring = *" + tokenString + "*, and about to return NUMBER");
      return NUMBER;
    } else if (token.equals("+") || token.equals("*") || token.equals("/")) {
      //System.out.println("*********At the end of getLastTokenType for
      // tokenstring = *" + tokenString + "*, and about to return OPERATOR");
      return OPERATOR;
    } else if (token.equals("-")) {
      //System.out.println("Token equals - sign");
      // get the token before this one:
      String lastLastToken = tokenString.substring(0,
          (tokenString.trim().length() - token.length())).trim();
      //System.out.println("Last last token = *" + lastLastToken + "*");
      int lastLastTokenType = getLastTokenType(lastLastToken);
      //System.out.println("Last last token type = " + lastLastTokenType);
      if ((lastLastTokenType == OPERATOR) || (lastLastTokenType == OPEN_PAREN)
          || (lastLastTokenType == -1)) // "-" counts for a
      // negative sign in this case
      {
        //System.out.println("*********At the end of getLastTokenType for
        // tokenstring = *" + tokenString + "*, and about to return DIGIT");
        return DIGIT;
      } else //((lastLastTokenType == NUMBER) || (lastLastTokenType == DIGIT))
             // // "-" counts for a minus sign in this case
      {
        //System.out.println("*********At the end of getLastTokenType for
        // tokenstring = *" + tokenString + "*, and about to return OPERATOR");
        return OPERATOR;
      }
    } else if (token.equals("(")) {
      //System.out.println("*********At the end of getLastTokenType for
      // tokenstring = *" + tokenString + "*, and about to return OPEN_PAREN");
      return OPEN_PAREN;
    } else if (token.equals(")")) {
      //System.out.println("*********At the end of getLastTokenType for
      // tokenstring = *" + tokenString + "*, and about to return
      // CLOSED_PAREN");
      return CLOSED_PAREN;
    } else {
      //System.out.println("*********At the end of getLastTokenType for
      // tokenstring = *" + tokenString + "*, and about to return DIGIT");
      return DIGIT;
    }
  }

  private String getLastToken(String tokenString) // returns the last token in
                                                  // the token string
  {
    //System.out.println("********** At the beginning of getLastToken for
    // tokenString = *" + tokenString + "*");
    //System.out.println("(tokenString.trim().indexOf(' ') = " +
    // tokenString.trim().indexOf(' '));
    //System.out.println("(tokenString.trim().indexOf('(') = " +
    // tokenString.trim().indexOf('('));
    //System.out.println("(tokenString.trim().indexOf(')') = " +
    // tokenString.trim().indexOf(')'));
    if ((tokenString.equals(null)) || (tokenString.length() == 0)) // empty text
                                                                   // field
    {
      //System.out.println("Empty tokenString!");
      return null;
    } else if ((tokenString.trim().indexOf(' ') < 0)
        && (tokenString.indexOf('(') < 0) && (tokenString.indexOf(')') < 0)) // no
    // spaces & no parentheses -- must be only one token
    {
      //System.out.println("Only one token!");
      //System.out.println("********** At the end of getLastToken for
      // tokenString = *" + tokenString +
      //"* and About to return tokenString.trim(), which = *" +
      // tokenString.trim() + "*");
      if ((tokenString.startsWith("-"))
          && (tokenString.trim().equals("-") == false)) // begins w/ a neg. sign
                                                        // but is not just a
      // neg. sign
      {
        return tokenString.trim().substring(1); // return token w/out neg. sign
      } else {
        return tokenString.trim();
      }
    } else if (tokenString.equals("(")) {
      return "(";
    } else // multiple tokens
    {
      //System.out.println("Multiple tokens!");
      String lastBlock = tokenString.trim().substring(
          tokenString.trim().lastIndexOf(' ') + 1);
      //System.out.println("lastBlock = *" + lastBlock + "*");
      if (lastBlock.endsWith(")")) {
        //System.out.println("********** At the end of getLastToken for
        // tokenString = *" + tokenString +
        //"* and About to return )");
        return ")";
      } else if (lastBlock.endsWith("(")) {
        return "(";
      } else if (lastBlock.startsWith("(")) {
        //System.out.println("********** At the end of getLastToken for
        // tokenString = *" + tokenString +
        //"* and About to return lastBlock.substring(1), which = *" +
        // lastBlock.substring(1) + "*");
        return lastBlock.substring(1); // return what comes after the
                                       // parenthesis
      } else if (lastBlock.indexOf("(") > 0) // open paren somewhere in the
                                             // middle
      {
        return lastBlock.substring(lastBlock.lastIndexOf("(") + 1);
      } else if ((lastBlock.startsWith("-"))
          && (lastBlock.trim().equals("-") == false)) // begins w/ a neg. sign
                                                      // but is not just a
      // neg. sign
      {
        return lastBlock.substring(1); // return token w/out neg. sign
      } else // no parentheses
      {
        //System.out.println("************No parentheses, at the end of
        // getLastToken for tokenString = *" + tokenString +
        //" and about to return lastBlock, which = *" + lastBlock + "*");
        return lastBlock;
      }
    }
  }

  private void setFocusedTextFieldText(String text) // sets the last focused
                                                    // text field, as well as
                                                    // the echo of this text
                                                    // field in the
  // button pad, to the specified text
  {
    lastFocusedTextField.setText(text);
    echoedTextField.setText(text);
  }
}