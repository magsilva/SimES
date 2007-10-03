/*
 * SimSE - MapEditor Professor Andre van der Hoek Emily Oh Navarro developed by:
 * Kuan-Sung Lee (Ethan) uciethan@yahoo.com
 * 
 * co-developed by : Calvin Lee
 */

package simse.modelbuilder.mapeditor;

import simse.modelbuilder.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class MapEditorGUI extends JPanel {
  private MapEditorMap map;
  private WarningListPane warningPane;

  public MapEditorGUI(ModelBuilderGUI owner, ModelOptions options, 
      DefinedObjectTypes objectTypes, CreatedObjects objects, 
      DefinedActionTypes actions, Hashtable startStateObjsToImages, 
      Hashtable ruleObjsToImages) {
    map = new MapEditorMap(owner, options, objectTypes, objects, actions, 
        startStateObjsToImages, ruleObjsToImages);
    warningPane = new WarningListPane();

    Box mainPane = Box.createVerticalBox();
    mainPane.setPreferredSize(new Dimension(1024, 650));

    // Add panes and separators to main pane:
    mainPane.add(map);
    JSeparator separator = new JSeparator();
    separator.setMaximumSize(new Dimension(2700, 1));
    mainPane.add(separator);
    mainPane.add(warningPane);
    add(mainPane);
    setNoOpenFile();
    validate();
    repaint();
  }

  public ArrayList<UserData> getUserDatas() {
    return map.getUserDatas();
  }

  public TileData[][] getMap() {
    return map.getMap();
  }

  public void reload(File tempFile) // reloads the map from a temporary file
  {
    // reload:
    Vector warnings = map.loadFile(tempFile);
    generateWarnings(warnings);
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

  public void setNoOpenFile() {
    map.setNoOpenFile();
    warningPane.clearWarnings();
  }

  public void setNewOpenFile(File f) {
    map.setNewOpenFile();
    warningPane.clearWarnings();
    if (f.exists()) // file has been saved before
    {
      reload(f);
    }
  }
}