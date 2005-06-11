/* This class defines the model for the attribute table used in the StartStateBuilderGUI */

package simse.modelbuilder.startstatebuilder;

import simse.modelbuilder.objectbuilder.*;
import javax.swing.table.*;
import java.util.*;
import java.text.*;

public class StartStateAttributeTableModel extends AbstractTableModel
{
	Vector data; // data in table
	SimSEObject object; // SimSEObject in focus, whose attributes are being displayed in the table
	String[] columnNames = {"Name", "Type", "Visible?", "Min Value", "Max Value", "Min Digits", "Max Digits", "Key?", "Visible at End?", 
		"Value"}; // column names
	NumberFormat numFormat; // for displaying number values
	
	
	public StartStateAttributeTableModel(SimSEObject obj)
	{
		object = obj;
		data = new Vector();
		numFormat = NumberFormat.getNumberInstance(Locale.US);
		refreshData();
	}
	
	
	public StartStateAttributeTableModel() // Creates an empty table
	{
		data = new Vector();
		numFormat = NumberFormat.getNumberInstance(Locale.US);
	}
	
	
	public void setObjectInFocus(SimSEObject obj) // sets the table to display the attributes of the obj parameter
	{
		object = obj;
		refreshData();
	}
	
	
	public SimSEObject getObjectInFocus() // returns the object currently in focus
	{
		return object;
	}
	
	
	public void clearObjectInFocus() // clears the object currently in focus
	{
		object = null;
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
		if(object != null)
		{
			Vector attributes = object.getSimSEObjectType().getAllAttributes();
			
			if(attributes != null)
			{
				// Initialize attribute names:
				for(int i=0; i<attributes.size(); i++)
				{
					temp.add(((Attribute)attributes.elementAt(i)).getName());
				}
				if(data.isEmpty()) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 0);
				}
				
				// Initialize attribute types:
				temp = new Vector();
				
				for(int i=0; i<attributes.size(); i++)
				{
					temp.add(AttributeTypes.getText(((Attribute)attributes.elementAt(i)).getType()));
				}
				if(data.size() < 2) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 1);
				}
				
				// Initialize attribute visible variable:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					temp.add(new Boolean(((Attribute)attributes.elementAt(i)).isVisible()));
				}
				if(data.size() < 3) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 2);
				}
				
				// Initialize attribute min values:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					Attribute tempAttr = ((Attribute)attributes.elementAt(i));
					if(tempAttr instanceof NonNumericalAttribute)
					{
						temp.add("N/A"); // this field not applicable for non-numerical attributes
					}
					else // numerical attribute
					{
						if(((NumericalAttribute)tempAttr).isMinBoundless())
						{
							temp.add("Boundless");
						}
						else
						{
							temp.add(numFormat.format(((NumericalAttribute)tempAttr).getMinValue()));
						}
					}
				}
				if(data.size() < 4) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 3);
				}
				
				// Initialize attribute max values:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					Attribute tempAttr = ((Attribute)attributes.elementAt(i));
					if(tempAttr instanceof NonNumericalAttribute)
					{
						temp.add("N/A"); // this field not applicable for non-numerical attributes
					}
					else // numerical attribute
					{
						if(((NumericalAttribute)tempAttr).isMaxBoundless())
						{
							temp.add("Boundless");
						}
						else
						{
							temp.add(numFormat.format(((NumericalAttribute)tempAttr).getMaxValue()));
						}
					}
				}
				if(data.size() < 5) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 4);
				}
				
				// Initialize attribute min num digits values:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					Attribute tempAttr = ((Attribute)attributes.elementAt(i));
					if((tempAttr instanceof NonNumericalAttribute) || (tempAttr.getType() == AttributeTypes.INTEGER)) // non-numerical or integer
						// attribute
					{
						temp.add("N/A"); // this field not applicable for non-numerical or integer attributes
					}
					else // double attribute
					{
						if(((NumericalAttribute)tempAttr).getMinNumFractionDigits() == null)
						{
							temp.add("Boundless");
						}
						else
						{
							NumericalAttribute temp2 = (NumericalAttribute)tempAttr;
							temp.add(numFormat.format(((Integer)(temp2.getMinNumFractionDigits())).intValue()));
						}
					}
				}
				if(data.size() < 6) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 5);
				}
				
				// Initialize attribute max num digits values:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					Attribute tempAttr = ((Attribute)attributes.elementAt(i));
					if((tempAttr instanceof NonNumericalAttribute) || (tempAttr.getType() == AttributeTypes.INTEGER)) // non-numerical or integer
					{
						temp.add("N/A"); // this field not applicable for non-numerical or integer attributes
					}
					else // double attribute
					{
						if(((NumericalAttribute)tempAttr).getMaxNumFractionDigits() == null)
						{
							temp.add("Boundless");
						}
						else
						{
							temp.add(numFormat.format(((NumericalAttribute)tempAttr).getMaxNumFractionDigits()));
						}
					}
				}
				if(data.size() < 7) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 6);
				}			
				
				// Initialize attribute key variable:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					temp.add(new Boolean(((Attribute)attributes.elementAt(i)).isKey()));
				}
				if(data.size() < 8) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 7);
				}
				
				// Initialize attribute visible at end variable:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					temp.add(new Boolean(((Attribute)attributes.elementAt(i)).isVisibleOnCompletion()));
				}
				if(data.size() < 9) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 8);
				}						
				
				// Initialize attribute value variable:
				temp = new Vector();
				for(int i=0; i<attributes.size(); i++)
				{
					InstantiatedAttribute tempAtt = object.getAttribute(((Attribute)(attributes.elementAt(i))).getName());
					
					if(tempAtt == null) // attribute not instantiated yet
					{
						temp.add("");
					}
					else // tempAtt != null
					{
						Object tempVal = tempAtt.getValue();
						if(tempVal != null)
						{
							temp.add(object.getAttribute(((Attribute)(attributes.elementAt(i))).getName()).getValue().toString());
						}
						else
						{
							temp.add("");
						}
					}
				}
				if(data.size() < 10) // first-time initialization
				{
					data.add(temp);
				}
				else // refreshing value
				{
					data.setElementAt(temp, 9);
				}
			}
			fireTableDataChanged(); // notify listeners that table data has changed
		}
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
