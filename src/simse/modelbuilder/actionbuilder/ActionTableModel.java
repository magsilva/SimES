/* This class defines the model for the action table used in the ActionBuilderGUI */

package simse.modelbuilder.actionbuilder;

import simse.modelbuilder.objectbuilder.*;
import javax.swing.table.*;
import java.util.*;
import java.lang.*;
import java.text.*;

public class ActionTableModel extends AbstractTableModel
{
	Vector data; // data in table
	ActionType action; // ActionType in focus, whose participants are being displayed in the table
	String[] columnNames = {"Name", "Quantity Guard", "Quantity", "Participant Meta-Type", "Possible Participant Types"}; 
	// column names
	
	
	public ActionTableModel(ActionType act)
	{
		action = act;
		data = new Vector();
		refreshData();
	}
	
	
	public ActionTableModel() // Creates an empty table
	{
		data = new Vector();
	}
	
	
	public void setActionTypeInFocus(ActionType act) // sets the table to display the participants of this action type
	{
		action = act;
		refreshData();
	}
	
	
	public ActionType getActionTypeInFocus() // returns the action type currently in focus
	{
		return action;
	}
	
	
	public void clearActionTypeInFocus() // clears the action type currently in focus
	{
		action = null;
		data.removeAllElements();
		fireTableDataChanged(); // notify listeners that data has changed
	}
	
	
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	
	public int getRowCount()
	{
		if(data.size() == 0)
		{
			return 0;
		}
		return ((Vector)data.elementAt(0)).size();
	}
	
	
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	
	public Object getValueAt(int row, int col)
	{
		return ((Vector)data.elementAt(col)).elementAt(row);
	}
	
	
	public void setValueAt(Object value, int row, int col)
	{
		((Vector)data.elementAt(col)).add(value);
		fireTableCellUpdated(row, col);
	}
	
	
	// Initialize/refresh table data:
	public void refreshData()
	{
		Vector temp = new Vector();
		Vector participants = action.getAllParticipants();
		
		if(participants != null)
		{
			// Initialize participant names:
			for(int i=0; i<participants.size(); i++)
			{
				temp.add(((ActionTypeParticipant)participants.elementAt(i)).getName());
			}
			if(data.isEmpty()) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 0);
			}
			
			// Initialize participant quantity guards:
			temp = new Vector();
			for(int i=0; i<participants.size(); i++)
			{
				temp.add(Guard.getText(((ActionTypeParticipant)participants.elementAt(i)).getQuantity().getGuard()));
			}
			if(data.size() < 2) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 1);
			}
			
			// Initialize participant quantity:
			StringBuffer quantString = new StringBuffer();
			temp = new Vector();
			for(int i=0; i<participants.size(); i++)
			{
				quantString = new StringBuffer(); // clear the string buffer
				Integer[] quant = ((ActionTypeParticipant)participants.elementAt(i)).getQuantity().getQuantity();
				if(quant[0] == null)
				{
					quantString.append("Boundless");
				}
				else
				{
					quantString.append(quant[0].intValue());
				}
				if((((ActionTypeParticipant)(participants.elementAt(i))).getQuantity().getGuard()) == Guard.AT_LEAST_AND_AT_MOST)
				{
					quantString.append(", ");
					if(quant[1] == null)
					{
						quantString.append("Boundless");
					}
					else
					{
						quantString.append(quant[1].intValue());
					}
				}
				temp.add(quantString.toString());
			}
			if(data.size() < 3) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 2);
			}
			
			// Initialize participant metatype:
			temp = new Vector();
			for(int i=0; i<participants.size(); i++)
			{
				temp.add(SimSEObjectTypeTypes.getText(((ActionTypeParticipant)participants.elementAt(i)).getSimSEObjectTypeType()));
			}
			if(data.size() < 4) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 3);
			}
			
			// Initialize participant types:
			temp = new Vector();
			for(int i=0; i<participants.size(); i++)
			{
				Vector types = ((ActionTypeParticipant)participants.elementAt(i)).getAllSimSEObjectTypes();
				StringBuffer s = new StringBuffer();
				for(int j=0; j<types.size(); j++)
				{
					s.append(((SimSEObjectType)(types.elementAt(j))).getName());
					if(j < (types.size() - 1)) // not the last element yet
					{
						s.append(", ");
					}
				}
				temp.add(s);
			}
			if(data.size() < 5) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 4);
			}
		}
		fireTableDataChanged(); // notify listeners that table data has changed
	}
	
	
	/*
	* JTable uses this method to determine the default renderer/
	* editor for each cell.  (Copied from a Java tutorial)
	*/
	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}
}
