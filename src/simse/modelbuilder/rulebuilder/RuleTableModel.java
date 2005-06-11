/* This class defines the model for the rule table used in the RuleBuilderGUI */

package simse.modelbuilder.rulebuilder;

import simse.modelbuilder.actionbuilder.*;
import javax.swing.table.*;
import java.util.*;

public class RuleTableModel extends AbstractTableModel
{
	Vector data; // data in table
	ActionType action; // ActionType in focus, whose participants are being displayed in the table
	String[] columnNames = {"Name", "Type", "Timing"}; // column names
	
	
	public RuleTableModel(ActionType act)
	{
		action = act;
		data = new Vector();
		refreshData();
	}
	
	
	public RuleTableModel() // Creates an empty table
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
		Vector rules = action.getAllRules();
		
		if(rules != null)
		{
			// Initialize rule names:
			for(int i=0; i<rules.size(); i++)
			{
				temp.add(((Rule)rules.elementAt(i)).getName());
			}
			if(data.isEmpty()) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 0);
			}
			
			// Initialize rule types:
			temp = new Vector();
			for(int i=0; i<rules.size(); i++)
			{
				if(((Rule)rules.elementAt(i)) instanceof CreateObjectsRule)
				{
					temp.add(Rule.CREATE_OBJECTS_RULE);
				}
				else if(((Rule)rules.elementAt(i)) instanceof DestroyObjectsRule)
				{
					temp.add(Rule.DESTROY_OBJECTS_RULE);
				}
				else if(((Rule)rules.elementAt(i)) instanceof EffectRule)
				{
					temp.add(Rule.EFFECT_RULE);
				}
			}
			if(data.size() < 2) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 1);
			}
			
			// Initialize rule timing:
			temp = new Vector();
			for(int i=0; i<rules.size(); i++)
			{
				if(((Rule)rules.elementAt(i)).getTiming() == RuleTiming.CONTINUOUS)
				{
					temp.add("Continuous");
				}
				else if(((Rule)rules.elementAt(i)).getTiming() == RuleTiming.TRIGGER)
				{
					temp.add("Trigger");
				}
				else if(((Rule)rules.elementAt(i)).getTiming() == RuleTiming.DESTROYER)
				{
					temp.add("Destroyer");
				}
			}
			if(data.size() < 3) // first-time initialization
			{
				data.add(temp);
			}
			else // refreshing value
			{
				data.setElementAt(temp, 2);
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
