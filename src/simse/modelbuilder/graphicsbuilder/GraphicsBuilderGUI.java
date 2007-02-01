package simse.modelbuilder.graphicsbuilder;

import simse.modelbuilder.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class GraphicsBuilderGUI extends JPanel implements ActionListener {
  private ModelBuilderGUI mainGUI;
  private CreatedObjects objects; // data structure for holding all of the
                                  // created SimSE objects
  private DefinedObjectTypes objTypes;
  private DefinedActionTypes actTypes;
  private SimSEObject objInFocus; // object currently selected
  private SopFileManipulator sopFileManip; // for generating/loading .sop files
  private Hashtable startStateObjsToImgFilenames; // maps SimSEObjects (keys)
                                                  // from the start state to the
                                                  // filename of the associated
                                                  // image file (String)
  private Hashtable ruleObjsToImgFilenames; // maps SimSEObjects (keys) from the
                                            // create objects rules to the
                                            // filename of the associated image
                                            // file (String)
  private Hashtable imagesToFilenames; // maps ImageIcons (keys) to the filename
                                       // of that image's file (String) (values)
  private File imageDir; // directory containing images
  private JFileChooser imageDirChooser; // for opening image directory
  private JButton iconDirButt; // button for changing icon directory
  private JList objectList; // list of objects to match pictures to
  private Vector objectListData; // data for objectList
  private JButton matchButton; // button to match a picture to an object
  private JLabel selectedImage; // shows the image selected or matched
  private JPanel imagesPanel; // holds the image buttons
  private JLabel topPanelLabel;
  private JLabel bottomPanelLabel;
  private WarningListPane warningPane;

  public GraphicsBuilderGUI(ModelBuilderGUI owner, DefinedObjectTypes objTs,
      CreatedObjects objs, DefinedActionTypes acts) {
    mainGUI = owner;
    imagesToFilenames = new Hashtable();
    startStateObjsToImgFilenames = new Hashtable();
    ruleObjsToImgFilenames = new Hashtable();
    objects = objs;
    objTypes = objTs;
    actTypes = acts;

    sopFileManip = new SopFileManipulator(objTypes, objects, actTypes,
        startStateObjsToImgFilenames, ruleObjsToImgFilenames);

    // image dir file chooser:
    imageDirChooser = new JFileChooser();
    imageDirChooser.addChoosableFileFilter(new DirectoryFileFilter()); // make
                                                                       // it so
                                                                       // it
                                                                       // only
                                                                       // displays
                                                                       // directories
    imageDirChooser
        .setDialogTitle("Please select the directory where available icons are located:");
    imageDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    // Create main panel (box):
    Box mainPane = Box.createVerticalBox();
    mainPane.setPreferredSize(new Dimension(1024, 650));

    // Create top pane:
    Box topPane = Box.createVerticalBox();
    JPanel topButtonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    iconDirButt = new JButton("Icon Directory");
    iconDirButt.addActionListener(this);
    iconDirButt
        .setToolTipText("Change the directory where the icons are located");
    topButtonPane.add(iconDirButt);
    topPane.add(topButtonPane);
    JPanel topLabelPane = new JPanel();
    topPanelLabel = new JLabel();
    topLabelPane.add(topPanelLabel);
    topPane.add(topLabelPane);
    // objectList:
    objectList = new JList();
    objectList.setVisibleRowCount(10); // make 10 items visible at a time
    objectList.setFixedCellWidth(650);
    objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only
                                                                      // allow
                                                                      // the
                                                                      // user to
                                                                      // select
                                                                      // one
                                                                      // item at
                                                                      // a time
    JScrollPane objectListPane = new JScrollPane(objectList);
    topPane.add(objectListPane);
    refreshObjectList();
    setupObjectListSelectionListenerStuff();

    // Create middle pane:
    JPanel middlePane = new JPanel();
    matchButton = new JButton("Match");
    matchButton.addActionListener(this);
    middlePane.add(matchButton);
    selectedImage = new JLabel();
    selectedImage.setText("No image selected");
    middlePane.add(selectedImage);

    // Create bottom pane:
    Box bottomPane = Box.createVerticalBox();
    JPanel bottomLabelPane = new JPanel();
    bottomPanelLabel = new JLabel();
    bottomLabelPane.add(bottomPanelLabel);
    bottomPane.add(bottomLabelPane);
    // Images panel:
    imagesPanel = new JPanel(new GridLayout(0, 5));
    JScrollPane imagesScrollPane = new JScrollPane(imagesPanel);
    imagesScrollPane.setPreferredSize(new Dimension(650, 300));
    bottomPane.add(imagesScrollPane);

    // Warning pane:
    warningPane = new WarningListPane();

    // Add panes and separators to main pane:
    mainPane.add(topPane);
    mainPane.add(middlePane);
    JSeparator separator2 = new JSeparator();
    separator2.setMaximumSize(new Dimension(2700, 1));
    mainPane.add(separator2);
    mainPane.add(bottomPane);
    JSeparator separator3 = new JSeparator();
    separator3.setMaximumSize(new Dimension(2900, 1));
    mainPane.add(separator3);
    mainPane.add(warningPane);
    add(mainPane);

    // make it so no file is open to begin with:
    setNoOpenFile();
    validate();
    repaint();
  }

  /*
   * reloads the graphics from a temporary file; if resetUI is true, clears all
   * current selections in the UI.
   */
  public void reload(File tempFile, boolean resetUI)
  {
    // reload:
    Vector returnVector = sopFileManip.loadFile(tempFile);
    imageDir = (File) returnVector.elementAt(0);
    generateWarnings((Vector) returnVector.elementAt(1));

    if (resetUI) {
	    // reset UI stuff:
	    refreshImagePane();
	    topPanelLabel.setText("Choose an object:");
	    bottomPanelLabel.setText("Choose an image:");
	    selectedImage.setEnabled(true);
	    objectList.setEnabled(true);
	
	    selectedImage.setIcon(null);
	    refreshObjectList();
	    clearObjectInFocus();
	    matchButton.setEnabled(false);
    }
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

  public Hashtable getStartStateObjsToImages() {
    return startStateObjsToImgFilenames;
  }

  public Hashtable getRuleObjsToImages() {
    return ruleObjsToImgFilenames;
  }

  public File getImageDirectory() {
    return imageDir;
  }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();

    if (source == matchButton) {
      if (objInFocus != null) // there is an object selected
      {
        if (selectedImage.getIcon() != null) // there is an image selected
        {
          // match the image and the object in the hashtable:
          String imgFilename = (String) imagesToFilenames
              .get((ImageIcon) selectedImage.getIcon());
          if (objects.getAllObjects().contains(objInFocus)) // a start state
                                                            // object
          {
            startStateObjsToImgFilenames.put(objInFocus, imgFilename);
          } else // a rule-generated object
          {
            ruleObjsToImgFilenames.put(objInFocus, imgFilename);
          }
          mainGUI.setFileModSinceLastSave();
        } else {
          JOptionPane.showMessageDialog(null,
              "Please choose an image to match", "Match Unsuccessful",
              JOptionPane.WARNING_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(null, "Please choose an object to match",
            "Match Unsuccessful", JOptionPane.WARNING_MESSAGE);
      }
    } else if (source == iconDirButt) // icon directory button
    {
      // bring up image dir file chooser:
      imageDirChooser.setSelectedFile(new File(""));
      int dirReturnVal = imageDirChooser.showOpenDialog(this);
      if (dirReturnVal == JFileChooser.APPROVE_OPTION) {
        File f = imageDirChooser.getSelectedFile();
        if (f.isDirectory()) // valid
        {
          imageDir = f;
          refreshImagePane();
        }
      }
    } else if (source instanceof JButton) // one of the image buttons
    {
      JButton butt = (JButton) source;
      selectedImage.setText(null);
      selectedImage.setIcon((ImageIcon) butt.getIcon());
      if (objectList.isSelectionEmpty() == false) // an object is selected
      {
        matchButton.setEnabled(true);
      } else // no object selected
      {
        matchButton.setEnabled(false);
      }
    }
  }

  private boolean isImageFile(String filename) {
    if (filename.indexOf('.') >= 0) {
      String extension = filename.substring(filename.lastIndexOf('.') + 1);
      if ((extension.equalsIgnoreCase("png"))
          || (extension.equalsIgnoreCase("gif"))
          || (extension.equalsIgnoreCase("jpg"))
          || (extension.equalsIgnoreCase("jpeg"))) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private void refreshObjectList() {
    objectListData = new Vector();
    Vector currentObjs = new Vector();
    currentObjs.addAll(objects.getAllObjects());

    // append all objects created by CreateObjectsRules to the currentObjs
    // Vector:
    Vector actions = actTypes.getAllActionTypes();
    for (int i = 0; i < actions.size(); i++) {
      ActionType act = (ActionType) actions.elementAt(i);
      Vector rules = act.getAllCreateObjectsRules();
      for (int j = 0; j < rules.size(); j++) {
        CreateObjectsRule rule = (CreateObjectsRule) rules.elementAt(j);
        Vector objs = rule.getAllSimSEObjects();
        for (int k = 0; k < objs.size(); k++) {
          currentObjs.add((SimSEObject) objs.elementAt(k));
        }
      }
    }

    // go through all objects and add their info to the list
    for (int i = 0; i < currentObjs.size(); i++) {
      StringBuffer data = new StringBuffer();
      SimSEObject tempObj = (SimSEObject) currentObjs.elementAt(i);
      data.append(tempObj.getSimSEObjectType().getName()
          + " "
          + SimSEObjectTypeTypes
              .getText(tempObj.getSimSEObjectType().getType()) + " ");
      if (tempObj.getSimSEObjectType().hasKey()
          && tempObj.getKey().isInstantiated()) // has key and it is
                                                // instantiated
      {
        data.append(tempObj.getKey().getValue().toString());
        objectListData.add(data.toString());
      }
    }
    objectList.setListData(objectListData);
  }

  private void refreshImagePane() {
    imagesPanel.removeAll();
    if ((imageDir != null) && (imageDir.exists())) // image dir exists
    {
      String pictureFiles[] = imageDir.list(); // list out all picture filenames
                                               // and store in pictureFiles[]

      for (int i = 0; i < pictureFiles.length; i++) {
        if (isImageFile(pictureFiles[i])) // to prevent non-image files from
                                          // being loaded
        {
          ImageIcon img = new ImageIcon(imageDir.getPath().concat(
              "\\" + pictureFiles[i]));
          imagesToFilenames.put(img, pictureFiles[i]);
          JButton button = new JButton(img);
          button.addActionListener(this);
          imagesPanel.add(button);
        }
      }
      validate();
      repaint();
    }
  }

  private void clearObjectInFocus() {
    objInFocus = null;
    matchButton.setEnabled(false);
  }

  public void setNewOpenFile(File f) {
    refreshImagePane();
    topPanelLabel.setText("Choose an object:");
    bottomPanelLabel.setText("Choose an image:");
    selectedImage.setEnabled(true);
    objectList.setEnabled(true);
    warningPane.clearWarnings();
    iconDirButt.setEnabled(true);
    if (f.exists()) // file has been saved before
    {
      reload(f, true);
    }
  }

  public void setNoOpenFile() // makes it so there's no open file in the GUI
  {
    selectedImage.setIcon(null);
    selectedImage.setText("No image selected");
    selectedImage.setEnabled(false);
    imagesToFilenames.clear();
    startStateObjsToImgFilenames.clear();
    ruleObjsToImgFilenames.clear();
    refreshObjectList();
    imageDir = null;
    clearObjectInFocus();
    refreshImagePane();
    topPanelLabel.setText("No File Opened");
    bottomPanelLabel.setText("");
    objectList.setEnabled(false);
    iconDirButt.setEnabled(false);
    warningPane.clearWarnings();
  }

  private void setupObjectListSelectionListenerStuff() // enables match button
                                                       // whenever both an
                                                       // object and an image
                                                       // are selected
  {
    // Copied from a Java tutorial:
    ListSelectionModel rowSM = objectList.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent lse) {
        //Ignore extra messages.
        if (lse.getValueIsAdjusting())
          return;

        ListSelectionModel lsm = (ListSelectionModel) lse.getSource();
        if (lsm.isSelectionEmpty() == false) {
          // get the selected object information from the selected item in
          // objectList:
          String selectedItem = (String) objectList.getSelectedValue();
          String ssObjType = selectedItem.substring(0, selectedItem
              .indexOf(' '));
          String temp = selectedItem.substring(selectedItem.indexOf(' ') + 1); // take
                                                                               // off
                                                                               // SimSEObjectTypeType
          String ssObjTypeType = temp.substring(0, temp.indexOf(' '));
          String keyAttVal = temp.substring(temp.indexOf(' ') + 1);
          int metaType = SimSEObjectTypeTypes
              .getIntRepresentation(ssObjTypeType);

          // set the object in focus:
          SimSEObjectType objType = objTypes.getObjectType(metaType, ssObjType); // get
                                                                                 // the
                                                                                 // SimSEObjectType
          if (objType.hasKey()) // objType has a key attribute specified
          {
            if (objType.getKey().getType() == AttributeTypes.INTEGER) {
              try {
                Integer val = new Integer(keyAttVal);
                // try to get the object from the CreatedObjects:
                objInFocus = objects.getObject(metaType, ssObjType, val);
                if (objInFocus == null) // not a start state object
                {
                  // get it from the rules:
                  objInFocus = getObjectFromRules(metaType, ssObjType, val);
                }
              } catch (NumberFormatException e) {
                System.out.println(e);
              }
            } else if (objType.getKey().getType() == AttributeTypes.DOUBLE) {
              try {
                Double val = new Double(keyAttVal);
                // try to get the object from the CreatedObjects:
                objInFocus = objects.getObject(metaType, ssObjType, val);
                if (objInFocus == null) // not a start state object
                {
                  // get it from the rules:
                  objInFocus = getObjectFromRules(metaType, ssObjType, val);
                }
              } catch (NumberFormatException e) {
                System.out.println(e);
              }
            } else if (objType.getKey().getType() == AttributeTypes.BOOLEAN) {
              // try to get the object from the CreatedObjects:
              objInFocus = objects.getObject(metaType, ssObjType, new Boolean(
                  keyAttVal));
              if (objInFocus == null) // not a start state object
              {
                // get it from the rules:
                objInFocus = getObjectFromRules(metaType, ssObjType,
                    new Boolean(keyAttVal));
              }
            } else // string
            {
              // try to get the object from the CreatedObjects:
              objInFocus = objects.getObject(metaType, ssObjType, keyAttVal);
              if (objInFocus == null) // not a start state object
              {
                // get it from the rules:
                objInFocus = getObjectFromRules(metaType, ssObjType, keyAttVal);
              }
            }
          }

          if (startStateObjsToImgFilenames.containsKey(objInFocus)) // object in
                                                                    // focus is
                                                                    // a start
                                                                    // state
                                                                    // object
                                                                    // and has
                                                                    // an image
                                                                    // matched
                                                                    // to it
          {
            // display the associated image:
            selectedImage.setText(null);
            ImageIcon img = getImage((String) startStateObjsToImgFilenames
                .get(objInFocus)); // get the image from the filename
            selectedImage.setIcon(img);
          } else if (ruleObjsToImgFilenames.containsKey(objInFocus)) // object
                                                                     // in focus
                                                                     // is a
                                                                     // rule-generated
                                                                     // object
                                                                     // and has
                                                                     // an image
                                                                     // matched
                                                                     // to it
          {
            // display the associated image:
            selectedImage.setText(null);
            ImageIcon img = getImage((String) ruleObjsToImgFilenames
                .get(objInFocus)); // get the image from the filename
            selectedImage.setIcon(img);
          } else // object in focus is not matched to an image
          {
            selectedImage.setIcon(null);
            selectedImage.setText("No image selected");
          }

          if ((selectedImage.getText() == null)
              || (selectedImage.getText().length() == 0)) // image selected
          {
            // enable match button:
            matchButton.setEnabled(true);
          } else // no image selected
          {
            // disable match button:
            matchButton.setEnabled(false);
          }
        }
      }
    });
  }

  private ImageIcon getImage(String filename) // returns the image associated
                                              // with the specified filename
  {
    for (Enumeration imgs = imagesToFilenames.keys(); imgs.hasMoreElements();) {
      ImageIcon icon = (ImageIcon) imgs.nextElement();
      if (((String) imagesToFilenames.get(icon)).equals(filename)) {
        return icon;
      }
    }
    return null;
  }

  private SimSEObject getObjectFromRules(int type, String simSEObjectTypeName,
      Object keyAttValue) // returns the specified object if it is
  // generated by one of the CreateObjectsRules. Otherwise, returns null
  {
    Vector actions = actTypes.getAllActionTypes();
    for (int i = 0; i < actions.size(); i++) {
      ActionType act = (ActionType) actions.elementAt(i);
      Vector rules = act.getAllCreateObjectsRules();
      for (int j = 0; j < rules.size(); j++) {
        CreateObjectsRule rule = (CreateObjectsRule) rules.elementAt(j);
        Vector objs = rule.getAllSimSEObjects();
        for (int k = 0; k < objs.size(); k++) {
          SimSEObject tempObj = ((SimSEObject) objs.elementAt(k));
          if ((tempObj.getSimSEObjectType().getType() == type)
              && (tempObj.getName().equals(simSEObjectTypeName))
              && (tempObj.getKey().isInstantiated())
              && (tempObj.getKey().getValue().equals(keyAttValue))) {
            return tempObj;
          }
        }
      }
    }
    return null;
  }
}