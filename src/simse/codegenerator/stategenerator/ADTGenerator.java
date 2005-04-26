/* This class is responsible for generating all of the code for the ADTs derived from the SimSEObjectTypes in an .sso file */

package simse.codegenerator.stategenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ADTGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	
	private File directory; // directory to save generated code into
	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file
	
	public ADTGenerator(DefinedObjectTypes dots, DefinedActionTypes dats, File dir)
	{
		objTypes = dots;
		actTypes = dats;
		directory = dir;
	}
	
	
	public void generate()
	{
		Vector objs = objTypes.getAllObjectTypes();
		// generate SSObject class:
		File objClass = new File(directory, ("simse\\adts\\objects\\SSObject.java"));
		if(objClass.exists())
		{
			objClass.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(objClass);
			writer.write("package simse.adts.objects;");
			writer.write(NEWLINE);
			writer.write("public abstract class SSObject");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("public SSObject(){}");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file SSObject.java"), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		
		// generate abstract object classes:
		generateAbstractObjectClass(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.ARTIFACT));
		generateAbstractObjectClass(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.TOOL));
		generateAbstractObjectClass(SimSEObjectTypeTypes.getText(SimSEObjectTypeTypes.PROJECT));
		
		// generate :
		File empClass = new File(directory, ("simse\\adts\\objects\\Employee.java"));
		if(empClass.exists())
		{
			empClass.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(empClass);
			writer.write("package simse.adts.objects;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("public abstract class Employee extends SSObject");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("private Vector menu;");
			writer.write(NEWLINE);
			writer.write("private String overheadText;");
			writer.write(NEWLINE);
			writer.write("public static final String IDLE_STRING = \"I'm not doing anything right now\";");
			writer.write(NEWLINE);
			writer.write("public Employee()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("menu = new Vector();");
			writer.write(NEWLINE);
			writer.write("menu.add(\"Everyone stop what you're doing\");");
			writer.write(NEWLINE);
			writer.write("overheadText = new String();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "getMenu" function:
			writer.write("public Vector getMenu()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return menu;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "clearMenu" function:
			writer.write("public void clearMenu()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("menu.removeAllElements();");
			writer.write(NEWLINE);
			writer.write("menu.add(\"Everyone stop what you're doing\");");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "addMenuItem" function:
			writer.write("public boolean addMenuItem(String s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("boolean alreadyContains = false;");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<menu.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String item = (String)menu.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(item.equals(s))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("alreadyContains = true;");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(!alreadyContains)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("menu.add(s);");
			writer.write(NEWLINE);
			writer.write("return true;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("return false;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "removeMenuItem" function:
			writer.write("public boolean removeMenuItem(String s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<menu.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String item = (String)menu.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(item.equals(s))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("menu.remove(item);");
			writer.write(NEWLINE);
			writer.write("return true;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);			
			writer.write("return false;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "getOverheadText" function:
			writer.write("public String getOverheadText()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return overheadText;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "setOverheadText" function:
			writer.write("public void setOverheadText(String s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if((s != null) && (s.length() > 0))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if((overheadText != null) && (overheadText.length() > 0))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(overheadText.equals(IDLE_STRING))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("overheadText = s;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);		
			writer.write("else if(overheadText.indexOf(s) == -1) // overhead text doesn't already contain this string");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("overheadText = overheadText.concat(\" AND \" + s);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("overheadText = s;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);		
			
			// "clearOverheadText" function:
			writer.write("public void clearOverheadText()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("overheadText = new String();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + empClass.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		
		// generate Customer class:
		File custClass = new File(directory, ("simse\\adts\\objects\\Customer.java"));
		if(custClass.exists())
		{
			custClass.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(custClass);
			writer.write("package simse.adts.objects;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("public abstract class Customer extends SSObject");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("private String overheadText;");
			writer.write(NEWLINE);
			writer.write("public Customer()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("overheadText = new String();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			// "getOverheadText" function:
			writer.write("public String getOverheadText()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String temp = overheadText;");
			writer.write(NEWLINE);
			writer.write("overheadText = new String();");
			writer.write(NEWLINE);
			writer.write("return temp;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			// "setOverheadText" function:
			writer.write("public void setOverheadText(String s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("overheadText = s;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			// "hasOverheadText" function:
			writer.write("public boolean hasOverheadText()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(overheadText == null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return false;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return true;");
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
			JOptionPane.showMessageDialog(null, ("Error writing file " + custClass.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		
		
		// go through each object and generate code for it:
		for(int i=0; i<objs.size(); i++)
		{
			generateObjectADT((SimSEObjectType)objs.elementAt(i));
		}
		
		// generate Action class:
		File actClass = new File(directory, ("simse\\adts\\actions\\Action.java"));
		if(actClass.exists())
		{
			actClass.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(actClass);
			writer.write("package simse.adts.actions;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("public abstract class Action");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("private int timeElapsed;");
			writer.write(NEWLINE);
			writer.write("public Action()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("timeElapsed = 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("public void incrementTimeElapsed()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("timeElapsed++;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("public int getTimeElapsed()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return timeElapsed;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("public abstract Vector getAllParticipants();");
			writer.write(NEWLINE);
			writer.write("public abstract Vector getAllActiveParticipants();");
			writer.write(NEWLINE);
			writer.write("public abstract Vector getAllInactiveParticipants();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();			
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + actClass.getPath()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
		
		// go through each action and generate code for it:
		Vector acts = actTypes.getAllActionTypes();
		for(int i=0; i<acts.size(); i++)
		{
			generateActionADT((ActionType)acts.elementAt(i));
		}
	}
	
	
	private void generateAbstractObjectClass(String className)
	{
		File absClass = new File(directory, ("simse\\adts\\objects\\" + className + ".java"));
		if(absClass.exists())
		{
			absClass.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(absClass);
			writer.write("package simse.adts.objects;");
			writer.write(NEWLINE);
			writer.write("public abstract class " + className + " extends SSObject");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("public " + className + "(){}");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + absClass.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private void generateObjectADT(SimSEObjectType objType)
	{
		File adtFile = new File(directory, ("simse\\adts\\objects\\" + getUpperCaseLeading(objType.getName()) + ".java"));
		if(adtFile.exists())
		{
			adtFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(adtFile);
			writer.write("package simse.adts.objects;");
			writer.write(NEWLINE);
			writer.write("public class " + getUpperCaseLeading(objType.getName()) + " extends "
				+ SimSEObjectTypeTypes.getText(objType.getType()));
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			
			// member variables/attributes:
			Vector attributes = objType.getAllAttributes();
			for(int i=0; i<attributes.size(); i++)
			{
				Attribute att = (Attribute)attributes.elementAt(i);
				writer.write("private ");
				
				// type:
				writer.write(getTypeAsString(att) + " ");
				
				// variable name:
				writer.write(att.getName().toLowerCase() + ";");
				writer.write(NEWLINE);
			}
			
			// constructor:
			writer.write("public " + getUpperCaseLeading(objType.getName()) + "(");
			for(int i=0; i<attributes.size(); i++)
			{
				Attribute att = (Attribute)attributes.elementAt(i);
				writer.write(getTypeAsString(att) + " ");
				writer.write(att.getName().substring(0, 1).toLowerCase() + i);
				if(i == (attributes.size() - 1)) // on last attribute
				{
					writer.write(")");
				}
				else // not on last attribute
				{
					writer.write(", ");
				}
			}
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// assignments:
			for(int i=0; i<attributes.size(); i++)
			{
				Attribute att = (Attribute)attributes.elementAt(i);
				writer.write("set" + att.getName() + "(" + (att.getName().substring(0, 1).toLowerCase() + i) + ");");
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// get and set functions:
			for(int i=0; i<attributes.size(); i++)
			{
				Attribute att = (Attribute)attributes.elementAt(i);
				// "get" method:
				writer.write("public ");
				writer.write(getTypeAsString(att) + " ");
				writer.write("get" + att.getName() + "()");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("return " + att.getName().toLowerCase() + ";");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				// "set" method:
				writer.write("public void set" + att.getName() + "(");
				writer.write(getTypeAsString(att) + " a)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				if((att instanceof NumericalAttribute) && (((NumericalAttribute)att).isMinBoundless() == false)) // has a min value
				{
					String minVal = ((NumericalAttribute)att).getMinValue().toString();
					writer.write("if(a < " + minVal + ")");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write(att.getName().toLowerCase() + " = " + minVal + ";");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					if(((NumericalAttribute)att).isMaxBoundless() == false) // has a max value
					{
						String maxVal = ((NumericalAttribute)att).getMaxValue().toString();
						writer.write("else if(a > " + maxVal + ")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(att.getName().toLowerCase() + " = " + maxVal + ";");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write("else");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write(att.getName().toLowerCase() + " = a;");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				else if((att instanceof NumericalAttribute) && (((NumericalAttribute)att).isMaxBoundless() == false)) // has a max value
				{
					String maxVal = ((NumericalAttribute)att).getMaxValue().toString();
					writer.write("if(a > " + maxVal + ")");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write(att.getName().toLowerCase() + " = " + maxVal + ";");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
					if(((NumericalAttribute)att).isMinBoundless() == false) // has a min value
					{
						String minVal = ((NumericalAttribute)att).getMinValue().toString();
						writer.write("else if(a < " + minVal + ")");
						writer.write(NEWLINE);
						writer.write(OPEN_BRACK);
						writer.write(NEWLINE);
						writer.write(att.getName().toLowerCase() + " = " + minVal + ";");
						writer.write(NEWLINE);
						writer.write(CLOSED_BRACK);
						writer.write(NEWLINE);
					}
					writer.write("else");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write(att.getName().toLowerCase() + " = a;");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				else
				{
					writer.write(att.getName().toLowerCase() + " = a;");
					writer.write(NEWLINE);
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + adtFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private String getTypeAsString(Attribute att)
	{
		if(att.getType() == AttributeTypes.INTEGER)
		{
			return "int";
		}
		else if(att.getType() == AttributeTypes.DOUBLE)
		{
			return "double";
		}
		else if(att.getType() == AttributeTypes.BOOLEAN)
		{
			return "boolean";
		}
		else //(att.getType() == AttributeTypes.STRING)
		{
			return "String";
		}
	}
	
	
	private void generateActionADT(ActionType actType)
	{
		File adtFile = new File(directory, ("simse\\adts\\actions\\" + getUpperCaseLeading(actType.getName()) + "Action.java"));
		if(adtFile.exists())
		{
			adtFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(adtFile);
			writer.write("package simse.adts.actions;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("public class " + getUpperCaseLeading(actType.getName()) + "Action extends Action");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			
			// member variables/attributes:
			Vector participants = actType.getAllParticipants();
			for(int i=0; i<participants.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(i);
				writer.write("private Hashtable " + tempPart.getName().toLowerCase() + "s;");
				writer.write(NEWLINE);
			}
			
			boolean hasTimedDestroyer = false;
			Vector allDests = actType.getAllDestroyers();
			for(int i=0; i<allDests.size(); i++)
			{
				ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(i);
				if(tempDest instanceof TimedActionTypeDestroyer)
				{
					hasTimedDestroyer = true;
					break;
				}
			}
			
			if(hasTimedDestroyer) // timed destroyer
			{
				// give it a timeToLive member variable:
				writer.write("private int timeToLive;");
				writer.write(NEWLINE);			
			}
			
			// constructor:
			writer.write("public " + getUpperCaseLeading(actType.getName()) + "Action()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			for(int i=0; i<participants.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(i);
				writer.write(tempPart.getName().toLowerCase() + "s = new Hashtable();");
				writer.write(NEWLINE);
			}
			if(hasTimedDestroyer) // timed destroyer
			{
				// find the timed destroyer:
				for(int j=0; j<allDests.size(); j++)
				{
					ActionTypeDestroyer tempDest = (ActionTypeDestroyer)allDests.elementAt(j);
					if(tempDest instanceof TimedActionTypeDestroyer)
					{
						writer.write("timeToLive = " + ((TimedActionTypeDestroyer)tempDest).getTime() + ";");
						writer.write(NEWLINE);			
						break;
					}
				}
			}			
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// methods:
			if(hasTimedDestroyer) // timed destroyer
			{
				// "getTimeToLive" method:
				writer.write("public int getTimeToLive()");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("return timeToLive;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "decrementTimeToLive" method:
				writer.write("public void decrementTimeToLive()");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("timeToLive--;");
				writer.write(NEWLINE);
				writer.write("if(timeToLive < 0)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("timeToLive = 0;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			
			// "getAllParticipants" method:
			writer.write("public Vector getAllParticipants()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector all = new Vector();");
			writer.write(NEWLINE);
			for(int i=0; i<participants.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(i);
				writer.write("all.addAll(getAll" + tempPart.getName() + "s());");
				writer.write(NEWLINE);
			}
			writer.write("return all;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "getAllActiveParticipants" method:
			writer.write("public Vector getAllActiveParticipants()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector all = new Vector();");
			writer.write(NEWLINE);
			for(int i=0; i<participants.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(i);
				writer.write("all.addAll(getAllActive" + tempPart.getName() + "s());");
				writer.write(NEWLINE);
			}
			writer.write("return all;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			// "getAllInactiveParticipants" method:
			writer.write("public Vector getAllInactiveParticipants()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector all = new Vector();");
			writer.write(NEWLINE);
			for(int i=0; i<participants.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(i);
				writer.write("all.addAll(getAllInactive" + tempPart.getName() + "s());");
				writer.write(NEWLINE);
			}
			writer.write("return all;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			for(int i=0; i<participants.size(); i++)
			{
				ActionTypeParticipant tempPart = (ActionTypeParticipant)participants.elementAt(i);
				
				// "getAll[Participant]s" method:
				writer.write("public Vector getAll" + tempPart.getName() + "s()");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector a = new Vector();");
				writer.write(NEWLINE);
				writer.write("Enumeration e = " + tempPart.getName().toLowerCase() + "s.keys();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<" + tempPart.getName().toLowerCase() + "s.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("a.add((" + SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + ")e.nextElement());");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("return a;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "getAllActive[Participant]s" method:
				writer.write("public Vector getAllActive" + tempPart.getName() + "s()");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector a = new Vector();");
				writer.write(NEWLINE);
				writer.write("Enumeration e = " + tempPart.getName().toLowerCase() + "s.keys();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<" + tempPart.getName().toLowerCase() + "s.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " key = ("
					+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + ")e.nextElement();");
				writer.write(NEWLINE);
				writer.write("if(((Boolean)(" + tempPart.getName().toLowerCase() + "s.get(key))).booleanValue() == true)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("a.add(key);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("return a;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "getAllInactive[Participant]s" method:
				writer.write("public Vector getAllInactive" + tempPart.getName() + "s()");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("Vector a = new Vector();");
				writer.write(NEWLINE);
				writer.write("Enumeration e = " + tempPart.getName().toLowerCase() + "s.keys();");
				writer.write(NEWLINE);
				writer.write("for(int i=0; i<" + tempPart.getName().toLowerCase() + "s.size(); i++)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " key = ("
					+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + ")e.nextElement();");
				writer.write(NEWLINE);
				writer.write("if(((Boolean)(" + tempPart.getName().toLowerCase() + "s.get(key))).booleanValue() == false)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("a.add(key);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("return a;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "add" method:
				writer.write("public boolean add" + tempPart.getName() + "("
					+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " a)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("if((" + tempPart.getName().toLowerCase() + "s.containsKey(a))");
				Vector types = tempPart.getAllSimSEObjectTypes();
				if(types.size() > 0)
				{
					writer.write(" ||");
				}
				for(int j=0; j<types.size(); j++)
				{
					if(j > 0) // not on first element
					{
						writer.write(" &&");
					}
					else // on first element
					{
						writer.write("(");
					}
					SimSEObjectType tempType = (SimSEObjectType)types.elementAt(j);
					writer.write(" ((a instanceof " + getUpperCaseLeading(tempType.getName()) + ") == false)");
				}
				if(types.size() > 0)
				{
					writer.write(")");
				}
				if(tempPart.getQuantity().isMaxValBoundless() == false) // has a maximum number of participants that can be in this action
				{
					writer.write(" || (" + tempPart.getName().toLowerCase() + "s.size() >= " + tempPart.getQuantity().getMaxVal().toString()
						+ ")");
				}
				writer.write(")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("return false;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("else");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(tempPart.getName().toLowerCase() + "s.put(a, new Boolean(true));");
				writer.write(NEWLINE);
				writer.write("return true;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "remove" method:
				writer.write("public boolean remove" + tempPart.getName() + "("
					+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " a)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("if(" + tempPart.getName().toLowerCase() + "s.containsKey(a))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(tempPart.getName().toLowerCase() + "s.remove(a);");
				writer.write(NEWLINE);
				writer.write("return true;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("return false;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "setActive" method:
				writer.write("public boolean set" + tempPart.getName() + "Active("
					+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " a)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("if(" + tempPart.getName().toLowerCase() + "s.containsKey(a))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(tempPart.getName().toLowerCase() + "s.put(a, new Boolean(true));");
				writer.write(NEWLINE);
				writer.write("return true;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("return false;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				
				// "setInactive" method:
				writer.write("public boolean set" + tempPart.getName() + "Inactive("
					+ SimSEObjectTypeTypes.getText(tempPart.getSimSEObjectTypeType()) + " a)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("if(" + tempPart.getName().toLowerCase() + "s.containsKey(a))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(tempPart.getName().toLowerCase() + "s.put(a, new Boolean(false));");
				writer.write(NEWLINE);
				writer.write("return true;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("return false;");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + adtFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
