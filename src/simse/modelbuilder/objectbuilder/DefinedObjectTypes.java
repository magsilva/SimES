/* This class is the data structure for holding the object types that are currently in memory and manipulable by the user */

package simse.modelbuilder.objectbuilder;

import java.util.*;

public class DefinedObjectTypes
{
	Vector objs; // Vector of SimSEObjectTypes
	
	
	public DefinedObjectTypes(Vector v)
	{
		objs = new Vector(v);
	}
	
	
	public DefinedObjectTypes()
	{
		objs = new Vector();
	}
	
	
	public Vector getAllObjectTypes() // returns a Vector of SimSEObjectTypes
	{
		return objs;
	}
	

	public void addObjectType(SimSEObjectType newObject)
	{
		objs.add(newObject);
	}
	
	
	public Vector getAllObjectTypesOfType(int type) // returns a Vector of all SimSEObjectTypes in the data structure that have type = to the type parameter
	{
		Vector v = new Vector();
		for(int i=0; i<objs.size(); i++)
		{
			SimSEObjectType tempObj = ((SimSEObjectType)objs.elementAt(i));
			if(tempObj.getType() == type) // object is of the specified type
			{
				v.add(tempObj); // add it to the vector to return
			}
		}
		return v;
	}
	
	
	public SimSEObjectType getObjectType(int type, String name) // returns the object type in the data structure with the specified type and name
	{
		for(int i=0; i<objs.size(); i++)
		{
			SimSEObjectType tempObj = ((SimSEObjectType)objs.elementAt(i));
			if((tempObj.getType() == type) && (tempObj.getName().equals(name)))
			{
				return tempObj;
			}
		}
		return null;
	}

	
	public void removeObjectType(int type, String name) // removes the object type w/ the specified name and type from the data structure
	{
		for(int i=0; i<objs.size(); i++)
		{
			SimSEObjectType tempObj = ((SimSEObjectType)objs.elementAt(i));
			if((tempObj.getType() == type) && (tempObj.getName().equals(name)))
			{
				objs.remove(i);
			}
		}
	}
	
	
	public boolean hasObjectType(SimSEObjectType type) // returns true if this object type is there, false if not
	{
		if(objs.contains(type))
		{
			return true;
		}
		return false;
	}
	
	
	public void clearAll() // removes all object types
	{
		objs.removeAllElements();
	}
}
