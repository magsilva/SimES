/* This class is responsible for generating all of the code for the SimSEMap class in the GUI */

package simse.codegenerator.guigenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.graphicsbuilder.*;
import simse.modelbuilder.mapeditor.*;
import simse.codegenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class SimSEMapGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	private String imageDirURL = "/simse/gui/icons/"; // directory where images are
	*/

	private File directory; // directory to save generated code into
	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file
	private	CreatedObjects objs; // holds all of the created objects from an sts file
	private Hashtable objsToImages; // maps SimSEObjects (keys) to pathname (String) of image file (values)
	private Hashtable objsToXYLocs; // maps SimSEObjects (keys) to XYLocations (Vectors) of employees (values)
	private TileData[][] mapRep; // representation of map
	private ArrayList userDatas; // array list of UserDatas


	public SimSEMapGenerator(DefinedObjectTypes dots, CreatedObjects cObjs, Hashtable oToI, TileData[][] map, ArrayList users, File dir)
	{
		objTypes = dots;
		objs = cObjs;
		objsToImages = oToI;
		mapRep = map;
		userDatas = users;
		objsToXYLocs = new Hashtable();
		directory = dir;
	}


	public void generate()
	{
		// generate file:
		File ssmFile = new File(directory, ("simse\\gui\\SimSEMap.java"));
		if(ssmFile.exists())
		{
			ssmFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(ssmFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.logic.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Image;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.io.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public class SimSEMap extends JPanel implements MouseListener, ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("protected State state;");
			writer.write(NEWLINE);
			writer.write("protected Logic logic;");
			writer.write(NEWLINE);
			writer.write("protected String sopFile; // location of sop file if loaded");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("protected TileData[][] mapRep;");
			writer.write(NEWLINE);
			writer.write("protected ArrayList sopUsers; // all of the DisplayedEmployees in the state");
			writer.write(NEWLINE);
			writer.write("protected int ssObjCount;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// constructor:
			writer.write("public SimSEMap(State s, Logic l)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("logic = l;");
			writer.write(NEWLINE);
			writer.write("mapRep = new TileData[MapData.X_MAPSIZE][MapData.Y_MAPSIZE];");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<MapData.Y_MAPSIZE; i++)");
			writer.write(NEWLINE);
			writer.write("for(int j=0; j<MapData.X_MAPSIZE; j++)");
			writer.write(NEWLINE);
			writer.write("mapRep[j][i] = new TileData(MapData.TILE_GRID, MapData.TRANSPARENT);");
			writer.write(NEWLINE);
			writer.write("sopUsers = new ArrayList();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// get all of the employees from the state:");
			writer.write(NEWLINE);
			writer.write("Vector allEmps = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<allEmps.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Employee tempEmp = (Employee)allEmps.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("DisplayedEmployee tmpUser = new DisplayedEmployee(tempEmp, null, this, false, false, -1, -1);");
			writer.write(NEWLINE);
			writer.write("sopUsers.add(tmpUser);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// go through all sopUsers and set information:");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<sopUsers.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("DisplayedEmployee user = (DisplayedEmployee)sopUsers.get(i);");
			writer.write(NEWLINE);
			writer.write("user.setXYLocations(getXYCoordinates(user.getEmployee())[0], getXYCoordinates(user.getEmployee())[1]);");
			writer.write(NEWLINE);
			writer.write("user.setUserIcon(getImage(user.getEmployee()));");
			writer.write(NEWLINE);

			// go through all user datas:
			for(int i=0; i<userDatas.size(); i++)
			{
				UserData tmpUser = (UserData)userDatas.get(i);
				if(i > 0)
				{
					writer.write("else ");
				}
				writer.write("if((user.getEmployee() instanceof "
					+ getUpperCaseLeading(tmpUser.getSimSEObject().getSimSEObjectType().getName()) + ") && ((("
					+ getUpperCaseLeading(tmpUser.getSimSEObject().getSimSEObjectType().getName())
					+ ")user.getEmployee()).get");
				SimSEObjectType objType = tmpUser.getSimSEObject().getSimSEObjectType();
				writer.write(getUpperCaseLeading(objType.getKey().getName()) + "()");

				if((tmpUser.getSimSEObject().getKey() != null) && (tmpUser.getSimSEObject().getKey().isInstantiated())) // key is instantiated
				{
					Object keyAttVal = tmpUser.getSimSEObject().getKey().getValue();
					if(objType.getKey().getType() == AttributeTypes.STRING) // string attribute
					{
						writer.write(".equals(\"" + keyAttVal.toString() + "\")))");
					}
					else // non-string attribute
					{
						writer.write(" == " + keyAttVal.toString() + "))");
					}

					// x y locations:
					Vector xys = new Vector();
					xys.add(new Integer(tmpUser.getXLocation()));
					xys.add(new Integer(tmpUser.getYLocation()));
					objsToXYLocs.put(tmpUser.getSimSEObject(), xys);

					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("user.setDisplayed(" + tmpUser.isDisplayed() + ");");
					writer.write(NEWLINE);
					writer.write("user.setActivated(" + tmpUser.isActivated() + ");");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Generator exception: " + objType.getName() + " "
						+ (SimSEObjectTypeTypes.getText(tmpUser.getSimSEObject().getSimSEObjectType().getType()))
						+ " object has no key attribute value.");
				}
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// map info:
			writer.write("// map objects:");
			writer.write(NEWLINE);
			for (int i = 0; i < MapData.Y_MAPSIZE; i++)
			{
				for (int j = 0; j < MapData.X_MAPSIZE; j++)
				{
					writer.write("mapRep[" + j + "][" + i + "].baseKey = " + mapRep[j][i].getBaseKey() + ";");
					writer.write(NEWLINE);
					writer.write("mapRep[" + j + "][" + i + "].fringeKey = " + mapRep[j][i].getFringeKey() + ";");
					writer.write(NEWLINE);
				}
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// other functions:
			writer.write("public void mouseClicked(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mousePressed(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mouseReleased(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mouseEntered(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mouseExited(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void actionPerformed(ActionEvent e){}");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getImage function:
			writer.write("protected String getImage(Employee e) // returns the url of the employee's image");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// go through all object types:
			Vector types = objTypes.getAllObjectTypes();
			// Make a vector of only the employee types:
			Vector empTypes = new Vector();
			for(int i=0; i<types.size(); i++)
			{
				SimSEObjectType temp = (SimSEObjectType)types.elementAt(i);
				if(temp.getType() == SimSEObjectTypeTypes.EMPLOYEE)
				{
					empTypes.add(temp);
				}
			}
			// go through all employee types:
			for(int i=0; i<empTypes.size(); i++)
			{
				SimSEObjectType tempType = (SimSEObjectType)empTypes.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(e instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(tempType.getName()) + " p = (" + getUpperCaseLeading(tempType.getName()) + ")e;");
				writer.write(NEWLINE);

				// go through all of the Employee created objects (and objects created by create objects rules):
				Enumeration createdObjects = objsToImages.keys();
				boolean putElse = false;
				for(int k=0; k<objsToImages.size(); k++)
				{
					SimSEObject obj = (SimSEObject)createdObjects.nextElement();
					if(obj.getSimSEObjectType().getName().equals(tempType.getName()))
					{
						boolean allAttValuesInit = true; // whether or not all this object's attribute values are initialized
						Vector atts = obj.getAllAttributes();
						if(atts.size() < obj.getSimSEObjectType().getAllAttributes().size()) // not all atts instantiated
						{
							allAttValuesInit = false;
						}
						else
						{
							for(int m=0; m<atts.size(); m++)
							{
								InstantiatedAttribute att = (InstantiatedAttribute)atts.elementAt(m);
								if(att.isInstantiated() == false) // not instantiated
								{
									allAttValuesInit = false;
									break;
								}
							}
						}
						if(allAttValuesInit)
						{
							if(putElse)
							{
								writer.write("else ");
							}
							else
							{
								putElse = true;
							}
							writer.write("if(p.get" + getUpperCaseLeading(obj.getKey().getAttribute().getName()) + "()");
							if(obj.getKey().getAttribute().getType() == AttributeTypes.STRING) // string att
							{
								writer.write(".equals(\"" + obj.getKey().getValue().toString() + "\"))");
							}
							else // integer, double, or boolean att
							{
								writer.write(" == " + obj.getKey().getValue().toString() + ")");
							}
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							if(((objsToImages.get(obj)) != null) && (((String)objsToImages.get(obj)).length() > 0))
							{
								String imagePath = (iconsDirectory+ ((String)objsToImages.get(obj)));
								writer.write("return \"" + imagePath + "\";");
								writer.write(NEWLINE);
							}
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("return null;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// getXYCoordinates function:
			writer.write("protected int[] getXYCoordinates(Employee emp) // returns the xy tile coorindates of the specified employee");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("int[] xys = {-1, -1};");
			writer.write(NEWLINE);

			// go through all employee types:
			for(int i=0; i<empTypes.size(); i++)
			{
				SimSEObjectType tempType = (SimSEObjectType)empTypes.elementAt(i);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(emp instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(tempType.getName()) + " p = (" + getUpperCaseLeading(tempType.getName()) + ")emp;");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);

				// go through all of the Employees:
				Enumeration employees = objsToXYLocs.keys();
				boolean putElse = false;
				for(int k=0; k<objsToXYLocs.size(); k++)
				{
					SimSEObject obj = (SimSEObject)employees.nextElement();
					if(obj.getSimSEObjectType().getName().equals(tempType.getName())) // matching type
					{
						if(putElse)
						{
							writer.write("else ");
						}
						else
						{
							putElse = true;
						}
						writer.write("if(p.get" + getUpperCaseLeading(obj.getKey().getAttribute().getName()) + "()");
						if(obj.getKey().isInstantiated())
						{
							if(obj.getKey().getAttribute().getType() == AttributeTypes.STRING) // string att
							{
								writer.write(".equals(\"" + obj.getKey().getValue().toString() + "\"))");
							}
							else // integer, double, or boolean att
							{
								writer.write(" == " + obj.getKey().getValue().toString() + ")");
							}
						}
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						if(((objsToXYLocs.get(obj)) != null) && (((Vector)objsToXYLocs.get(obj)).size() >= 2))
						{
							int x = ((Integer)(((Vector)objsToXYLocs.get(obj)).elementAt(0))).intValue();
							int y = ((Integer)(((Vector)objsToXYLocs.get(obj)).elementAt(1))).intValue();
							writer.write("xys[0] = " + x + ";");
							writer.write(NEWLINE);
							writer.write("xys[1] = " + y + ";");
							writer.write(NEWLINE);
						}
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("return xys;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getSopUsers function
			writer.write("public ArrayList getSopUsers()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return sopUsers;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);



			// TileData class:
			writer.write("protected class TileData");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("int baseKey;");
			writer.write(NEWLINE);
			writer.write("int fringeKey;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public TileData(int b, int f)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("baseKey = b;");
			writer.write(NEWLINE);
			writer.write("fringeKey = f;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setBase function:
			writer.write("public void setBase(int b)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("baseKey = b;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setFringe function:
			writer.write("public void setFringe(int f)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("fringeKey = f;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getBase function:
			writer.write("public Image getBase()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return MapData.getImage(baseKey);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getFringe function:
			writer.write("public Image getFringe()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return MapData.getImage(fringeKey);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + ssmFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}


	private String replaceAll(String s, char replaced, String replacer)
	{
		char[] cArray = s.toCharArray();
		StringBuffer newStr = new StringBuffer();
		for(int i=0; i<cArray.length; i++)
		{
			if(cArray[i] == replaced)
			{
				newStr.append(replacer);
			}
			else
			{
				newStr.append(cArray[i]);
			}
		}
		return newStr.toString();
	}
}
