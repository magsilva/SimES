/*
SimSE - MapEditor
Professor Andre van der Hoek
Emily Oh Navarro
developed by: Kuan-Sung Lee (Ethan)
uciethan@yahoo.com

co-developed by : Calvin Lee
*/

package simse.modelbuilder.mapeditor;

import simse.modelbuilder.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;
import simse.modelbuilder.graphicsbuilder.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class MapEditorGUI extends JPanel
{
	private MapEditorMap map;
	private WarningListPane warningPane;

	public MapEditorGUI(ModelBuilderGUI owner, DefinedObjectTypes objectTypes, CreatedObjects objects, DefinedActionTypes actions, 
		File iconDir,	Hashtable startStateObjsToImages, Hashtable ruleObjsToImages)
	{
		map = new MapEditorMap(owner, objectTypes, objects, actions, iconDir, startStateObjsToImages, ruleObjsToImages);
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
	
	
	public ArrayList getUserDatas()
	{
		return map.getUserDatas();
	}
	
	
	public TileData[][] getMap()
	{
		return map.getMap();
	}	
	
	
	public void reload(File tempFile, File iconDir) // reloads the map from a temporary file
	{		
		// reload:
		Vector warnings = map.loadFile(tempFile, iconDir);
		generateWarnings(warnings);
	}
	
	
	private void generateWarnings(Vector warnings) // displays warnings of errors found during checking for inconsistencies
	{
		if(warnings.size() > 0) // there is at least 1 warning
		{
			warningPane.setWarnings(warnings);
		}
	}	
	
	
	public void setNoOpenFile()
	{
		map.setNoOpenFile();	
		warningPane.clearWarnings();
	}
	
	
	public void setNewOpenFile(File f, File iconDir)
	{
		map.setNewOpenFile();	
		warningPane.clearWarnings();
		if(f.exists()) // file has been saved before
		{
			reload(f, iconDir);
		}
	}	
}
