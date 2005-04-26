/* This class defines a participant in an action type */

package simse.modelbuilder.actionbuilder;

import simse.modelbuilder.objectbuilder.*;
import java.util.*;

public class ActionTypeParticipant implements Cloneable
{
	private String name; // name of the participant
	private int simseObjTypeType; // employee, artifact, tool, project, or customer
	private ActionTypeParticipantQuantity quantity; // conditions on the quantity of this participant
	private Vector simseObjTypes; // Vector of SimSEObjectTypes that this participant can be


	public ActionTypeParticipant(int ssObjTypeType)
	{
		name = new String();
		simseObjTypeType = ssObjTypeType;
		quantity = new ActionTypeParticipantQuantity(Guard.AT_LEAST);
		simseObjTypes = new Vector();
	}


	public Object clone()
	{
		try
		{
			ActionTypeParticipant cl = (ActionTypeParticipant)(super.clone());
			cl.name = name;
			cl.simseObjTypeType = simseObjTypeType;
			cl.quantity = (ActionTypeParticipantQuantity)(quantity.clone());
			Vector clonedTypes = new Vector();
			for(int i=0; i<simseObjTypes.size(); i++)
			{
				clonedTypes.add((SimSEObjectType)(((SimSEObjectType)(simseObjTypes.elementAt(i))).clone()));
			}
			cl.simseObjTypes = clonedTypes;
			return cl;
		}
		catch(CloneNotSupportedException c)
		{
			System.out.println(c.getMessage());
		}
		return null;
	}


	public String getName()
	{
		return name;
	}


	public int getSimSEObjectTypeType() // returns the SimSEObjectTypeType of this participant
	{
		return simseObjTypeType;
	}


	public void setName(String newName)
	{
		name = newName;
	}


	public void setSimSEObjectTypeType(int newType)
	{
		simseObjTypeType = newType;
		simseObjTypes.removeAllElements();
	}


	public Vector getAllSimSEObjectTypes() // returns the SimSEObjectTypes that this participant can be of
	{
		return simseObjTypes;
	}


	public ActionTypeParticipantQuantity getQuantity() // returns the conditions on the quantity of this participant
	{
		return quantity;
	}


	public void addSimSEObjectType(SimSEObjectType newType) // adds the specified SimSEObjectType to the possible types this participant
		// can be; if it's already there, it doesn't add it.
	{
		boolean notFound = true;
		for(int i=0; i<simseObjTypes.size(); i++)
		{
			SimSEObjectType tempType = (SimSEObjectType)(simseObjTypes.elementAt(i));
			if(tempType.getName().equals(newType.getName()))
			{
				notFound = false;
			}
		}
		if(notFound) // new SimSEObjectType
		{
			simseObjTypes.add(newType);
		}
	}


	public void removeSimSEObjectType(String typeName) // removes this SimSEObjectType from the possible types this participant can be
	{
		for(int i=0; i<simseObjTypes.size(); i++)
		{
			SimSEObjectType tempType = (SimSEObjectType)(simseObjTypes.elementAt(i));
			if(tempType.getName().equals(typeName))
			{
				simseObjTypes.removeElementAt(i);
			}
		}
	}


	public SimSEObjectType getSimSEObjectType(String typeName)
	{
		for(int i=0; i<simseObjTypes.size(); i++)
		{
			SimSEObjectType tempType = (SimSEObjectType)(simseObjTypes.elementAt(i));
			if(tempType.getName().equals(typeName))
			{
				return tempType;
			}
		}
		return null;
	}


	public void setQuantity(ActionTypeParticipantQuantity newQuantity) // sets the conditions on the quantity of this participant
	{
		quantity = newQuantity;
	}
}
