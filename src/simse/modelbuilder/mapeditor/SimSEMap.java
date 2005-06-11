/*
This class contains methods that are used in both MapEditorGUI and World.
*/

package simse.modelbuilder.mapeditor;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class SimSEMap extends JPanel implements MouseListener, ActionListener
{
	protected File iconDirectory;		// location of the icon directory

	protected DefinedObjectTypes objectTypes;
	protected CreatedObjects objects;
	protected DefinedActionTypes actions;
	protected Hashtable startStateObjsToImages;
	protected Hashtable ruleObjsToImages;

	protected TileData[][] mapRep;
	protected ArrayList sopUsers;
	protected int ssObjCount;

	// map constants:
	private final String BEGIN_MAP_TAG = "<beginMap>";
	private final String END_MAP_TAG = "<endMap>";


	public SimSEMap(DefinedObjectTypes objTypes, CreatedObjects objs, DefinedActionTypes acts, File iconDir,
		Hashtable startStateObjs, Hashtable ruleObjs)
	{
		objectTypes = objTypes;
		objects = objs;
		actions = acts;
		iconDirectory = iconDir;
		startStateObjsToImages = startStateObjs;
		ruleObjsToImages = ruleObjs;

		mapRep = new TileData[MapData.X_MAPSIZE][MapData.Y_MAPSIZE];
		for (int i = 0; i < MapData.Y_MAPSIZE; i++)
		{
			for (int j = 0; j < MapData.X_MAPSIZE; j++)
			{
				mapRep[j][i] = new TileData(MapData.TILE_GRID, MapData.TRANSPARENT);
			}
		}

		sopUsers = new ArrayList();
	}


	public Vector loadFile(File inputFile, File iconDir) // loads the map from the input file (.mdl) and returns a Vector of warning messages
	{
		iconDirectory = iconDir;
		Vector warnings = new Vector(); // vector of warning messages
		sopUsers.clear();

		// load the startste objects
		Enumeration ssObj = startStateObjsToImages.keys();
		Enumeration ssImg = startStateObjsToImages.elements();

		while (ssObj.hasMoreElements())
		{
			SimSEObject simObj = (SimSEObject)ssObj.nextElement();
			String tmpIconLoc = iconDirectory.getPath() + "\\" + (String)ssImg.nextElement();

			int type = simObj.getSimSEObjectType().getType();
			String objType = SimSEObjectTypeTypes.getText(type);

			if(objType.equals(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.EMPLOYEE)))
			{
				Vector attrib = simObj.getAllAttributes();

				boolean hired = true;
				for (int i = 0; i < attrib.size(); i++)
				{
					InstantiatedAttribute ia = (InstantiatedAttribute)attrib.get(i);

					if (ia.getAttribute().getName().equalsIgnoreCase("hired"))
					{
						Boolean b = (Boolean)ia.getValue();
						// in case adding hire/fire to model, it will start as null and none
						// of the employees will be visible
						if (b != null)
							hired = b.booleanValue();
					}
				}
				UserData tmpUser = new UserData(simObj,tmpIconLoc,this,false,hired,-1,-1);
				sopUsers.add(tmpUser);
			}
		}

		ssObjCount = sopUsers.size();

		Enumeration rObj = ruleObjsToImages.keys();
		Enumeration rImg = ruleObjsToImages.elements();

		// load the rule objects
		while (rObj.hasMoreElements())
		{
			SimSEObject simObj = (SimSEObject)rObj.nextElement();
			String tmpIconLoc = iconDirectory.getPath() + "\\" + (String)rImg.nextElement();

			int type = simObj.getSimSEObjectType().getType();
			String objType = SimSEObjectTypeTypes.getText(type);

			if(objType.equals(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.EMPLOYEE)))
			{
				UserData tmpUser = new UserData(simObj,tmpIconLoc,this,false,false,-1,-1);
				sopUsers.add(tmpUser);
			}
		}


		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			boolean foundBeginningOfMap = false;
			while(!foundBeginningOfMap)
			{
				String currentLine = reader.readLine(); // read in a line of text from the file
				if(currentLine.equals(BEGIN_MAP_TAG)) // beginning of map
				{
					foundBeginningOfMap = true;
					boolean endOfMap = false;
					while(!endOfMap)
					{
						currentLine = reader.readLine(); // read in the next line of text from the file
						if(currentLine.equals(END_MAP_TAG)) // end of graphics
						{
							endOfMap = true;
						}
						else // not end of map yet
						{
							if(currentLine.equals("<beginSOPUsers>"))
							{
								currentLine = reader.readLine();
								while (!currentLine.equals("<endSOPUsers>")) // read in the sop users:
								{
									String tmpName = currentLine;
									boolean tmpDisplayed = reader.readLine().equals("true");
									boolean activated = reader.readLine().equals("true");
									int tmpX = Integer.parseInt(reader.readLine());
									int tmpY = Integer.parseInt(reader.readLine());

									for(int i=0; i<sopUsers.size(); i++)
									{
										UserData user = (UserData)sopUsers.get(i);
										if(user.getName().equals(tmpName))
										{
											user.setDisplayed(tmpDisplayed);
//											user.setActivated(activated);
											user.setXYLocations(tmpX,tmpY);
										}
									}

									currentLine = reader.readLine();		// ignore space
									currentLine = reader.readLine();		// next SOP object or <endSOPUsers>
								}
							}

							// draw map
							for (int i = 0; i < MapData.Y_MAPSIZE; i++)
							{
								for (int j = 0; j < MapData.X_MAPSIZE; j++)
								{
									mapRep[j][i].baseKey = Integer.parseInt(reader.readLine());
									mapRep[j][i].fringeKey = Integer.parseInt(reader.readLine());
								}
							}
						}
					}
				}
			}
			reader.close();
		}
		catch (IOException i)
		{
			JOptionPane.showMessageDialog(null,"An error has occured while reading file.");
		}
		catch(NumberFormatException e){}

		return warnings;
	}


	public void mouseClicked(MouseEvent me){}
	public void mousePressed(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void actionPerformed(ActionEvent e){}
}
