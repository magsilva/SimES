/* This class is for all file manipulation involving sop files*/

package simse.modelbuilder.graphicsbuilder;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.rulebuilder.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class SopFileManipulator
{
	private DefinedObjectTypes objectTypes;
	private CreatedObjects objects;
	private DefinedActionTypes actTypes;
	private Hashtable startStateObjsToImages; // maps SimSEObjects from CreatedObjects to filename of associated image file (String)
	private Hashtable ruleObjsToImages; // maps SimSEObjects from CreateObjectsRules in DefinedActionTypes to filename of associated image file (String)
	private final char NEWLINE = '\n';
	private final String BEGIN_GRAPHICS_TAG = "<beginGraphics>";
	private final String END_GRAPHICS_TAG = "<endGraphics>";	
	
	
	public SopFileManipulator(DefinedObjectTypes defObjs, CreatedObjects createdObjs, DefinedActionTypes acts, Hashtable stsObjsToImgs, 
		Hashtable ruleObjsToImgs)
	{
		objectTypes = defObjs;
		objects = createdObjs;
		actTypes = acts;
		startStateObjsToImages = stsObjsToImgs;
		ruleObjsToImages = ruleObjsToImgs;
	}
	
	
	public Vector loadFile(File inputFile) // loads the mdl file, filling the startStateObjsToImages, ruleObjsToImages, createdObjs, 
		// objTypes, and actTypes with the data from the file, and returns as the first element in the Vector, a String denoting the pathname
		// for the directory in which the icons are located, and as the second element in the Vector, a Vector of warning messages.
	{
		startStateObjsToImages.clear();
		ruleObjsToImages.clear();
		String iconDirectory = new String();
		Vector warnings = new Vector(); // vector of warning messages
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			boolean foundBeginningOfGraphics = false;
			while(!foundBeginningOfGraphics)
			{
				String currentLine = reader.readLine(); // read in a line of text from the file
				if(currentLine.equals(BEGIN_GRAPHICS_TAG)) // beginning of graphics
				{
					foundBeginningOfGraphics = true;
					currentLine = reader.readLine(); // read in BEGIN_ICONS_DIR_TAG
					iconDirectory = reader.readLine(); // get the icon directory path
					currentLine = reader.readLine(); // read in END_ICONS_DIR_TAG
					boolean endOfGraphics = false;
					while(!endOfGraphics)
					{						
						currentLine = reader.readLine(); // read in the next line of text from the file
						if(currentLine.equals(END_GRAPHICS_TAG)) // end of graphics
						{
							endOfGraphics = true;
						}
						else // not end of graphics yet
						{
							String ssObjTypeTypeName = currentLine;
							String ssObjTypeName = reader.readLine();
							String keyAttVal = reader.readLine();
							String imageFile = reader.readLine();
							String blankLine = reader.readLine();
							
							if((imageFile == null) || (imageFile.length() == 0)) // no image
							{
							}
							else // has image
							{
								SimSEObjectType tempObjType = objectTypes.getObjectType(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), 
									ssObjTypeName);
								if(tempObjType == null) // object type not found
								{
									warnings.add("Invalid object type: " + ssObjTypeTypeName + " " + ssObjTypeName + "; ignoring object of this type");
								}
								else // object type found
								{
									// find out what type the key attribute value is:
									Attribute keyAtt = tempObjType.getKey();
									if(keyAtt.getType() == AttributeTypes.INTEGER) // integer attribute
									{
										try
										{
											Integer keyInt = new Integer(keyAttVal);
											// try to get the object from created objects:
											SimSEObject tempObj = objects.getObject(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, 
												keyInt);
											if(tempObj == null) // object not found
											{
												// try to get the object from the CreateObjectsRules:
												tempObj = getObjectFromRules(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, keyInt);
												if(tempObj == null) // object still not found
												{
													warnings.add("Invalid object: " + ssObjTypeTypeName + " " + ssObjTypeName + " - " + keyAttVal
														+ "; ignoring this object");
												}
												else // object found
												{
													ruleObjsToImages.put(tempObj, imageFile);
												}
											}
											else // object found
											{
												startStateObjsToImages.put(tempObj, imageFile);
											}
										}
										catch(NumberFormatException e)
										{
											warnings.add("Invalid key attribute value for object: " + ssObjTypeTypeName + " " + ssObjTypeName + " - " + keyAttVal
												+ "; ignoring this object");
										}
									}
									
									else if(keyAtt.getType() == AttributeTypes.DOUBLE) // double attribute
									{
										try
										{
											Double keyDouble = new Double(keyAttVal);
											// try to get the object from created objects:
											SimSEObject tempObj = objects.getObject(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, 
												keyDouble);
											if(tempObj == null) // object not found
											{
												// try to get the object from the CreateObjectsRules:
												tempObj = getObjectFromRules(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, keyDouble);
												if(tempObj == null) // object still not found
												{
													warnings.add("Invalid object: " + ssObjTypeTypeName + " " + ssObjTypeName + " - " + keyAttVal
														+ "; ignoring this object");
												}
												else // object found
												{											
													ruleObjsToImages.put(tempObj, imageFile);
												}
											}
											else // object found
											{
												startStateObjsToImages.put(tempObj, imageFile);
											}
										}
										catch(NumberFormatException e)
										{
											warnings.add("Invalid key attribute value for object: " + ssObjTypeTypeName + " " + ssObjTypeName + " - " + keyAttVal 
												+ "; ignoring this object");
										}
									}	
									
									else if(keyAtt.getType() == AttributeTypes.BOOLEAN) // boolean attribute
									{
										Boolean keyBool = new Boolean(keyAttVal);
										// try to get the object from created objects:
										SimSEObject tempObj = objects.getObject(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, 
											keyBool);
										if(tempObj == null) // object not found
										{									
											// try to get the object from the CreateObjectsRules:
											tempObj = getObjectFromRules(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, keyBool);
											if(tempObj == null) // object still not found
											{
												warnings.add("Invalid object: " + ssObjTypeTypeName + " " + ssObjTypeName + " - " + keyAttVal
													+ "; ignoring this object");
											}
											else // object found
											{										
												ruleObjsToImages.put(tempObj, imageFile);
											}								
										}
										else // object found
										{
											startStateObjsToImages.put(tempObj, imageFile);
										}
									}	
									
									else if(keyAtt.getType() == AttributeTypes.STRING) // string attribute
									{
										// try to get the object from created objects:
										SimSEObject tempObj = objects.getObject(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, keyAttVal);
										if(tempObj == null) // object not found
										{
											// try to get the object from the CreateObjectsRules:
											tempObj = getObjectFromRules(SimSEObjectTypeTypes.getIntRepresentation(ssObjTypeTypeName), ssObjTypeName, keyAttVal);
											if(tempObj == null) // object still not found
											{
												warnings.add("Invalid object: " + ssObjTypeTypeName + " " + ssObjTypeName + " - " + keyAttVal
													+ "; ignoring this object");
											}
											else // object found
											{								
												ruleObjsToImages.put(tempObj, imageFile);
											}								
										}
										else // object found
										{
											startStateObjsToImages.put(tempObj, imageFile);
										}
									}								
								}
							}
						}
					}	
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, ("Cannot find file " + inputFile.getPath()), "File Not Found",
				JOptionPane.WARNING_MESSAGE);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error reading file! " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		Vector returnVector = new Vector();
		returnVector.add(new File(iconDirectory));
		returnVector.add(warnings);
		return returnVector;		
	}
	
	
	private SimSEObject getObjectFromRules(int type, String simSEObjectTypeName, Object keyAttValue) // returns the specified object if it is 
		// generated by one of the CreateObjectsRules.  Otherwise, returns null
	{
		Vector actions = actTypes.getAllActionTypes();
		for(int i=0; i<actions.size(); i++)
		{
			ActionType act = (ActionType)actions.elementAt(i);
			Vector rules = act.getAllCreateObjectsRules();
			for(int j=0; j<rules.size(); j++)
			{
				CreateObjectsRule rule = (CreateObjectsRule)rules.elementAt(j);
				Vector objs = rule.getAllSimSEObjects();
				for(int k=0; k<objs.size(); k++)
				{
					SimSEObject tempObj = ((SimSEObject)objs.elementAt(k));
					if((tempObj.getSimSEObjectType().getType() == type) && (tempObj.getName().equals(simSEObjectTypeName)) 
						&& (tempObj.getKey().isInstantiated()) && (tempObj.getKey().getValue().equals(keyAttValue)))
					{
						return tempObj;
					}
				}
			}
		}		
		return null;
	}
}
