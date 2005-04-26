/* This class is responsible for generating all of the code for the different table models in the At-A-Glance Frames in the GUI */

package simse.codegenerator.guigenerator;

import simse.modelbuilder.objectbuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class AtAGlanceTableModelGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into
	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file

	public AtAGlanceTableModelGenerator(DefinedObjectTypes dots, File dir)
	{
		objTypes = dots;
		directory = dir;
	}


	public void generate()
	{
		Vector types = objTypes.getAllObjectTypes();
		for(int i=0; i<types.size(); i++)
		{
			generateTableModelFile((SimSEObjectType)types.elementAt(i));
		}
	}
	
	private void generateTableModelFile(SimSEObjectType type)
	{
		// generate file:
		File tableModelFile = new File(directory, ("simse\\gui\\" + getUpperCaseLeading(type.getName()) + "TableModel.java"));
		if(tableModelFile.exists())
		{
			tableModelFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(tableModelFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import javax.swing.table.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import java.lang.*;");
			writer.write(NEWLINE);
			writer.write("import java.text.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// constructor:
			writer.write("public class " + getUpperCaseLeading(type.getName()) + "TableModel extends AbstractTableModel");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("private Vector columnNames; // column names");
			writer.write(NEWLINE);
			writer.write("private Vector data; // data in table");
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private NumberFormat numFormat;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public " + getUpperCaseLeading(type.getName()) + "TableModel(State s)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("columnNames = new Vector();");
			writer.write(NEWLINE);
			writer.write("data = new Vector();");
			writer.write(NEWLINE);
			writer.write("numFormat = NumberFormat.getNumberInstance(Locale.US);");
			writer.write(NEWLINE);
			writer.write("initColNames();");
			writer.write(NEWLINE);
			writer.write("update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// getColumnCount function:
			writer.write("public int getColumnCount()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return columnNames.size();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
	
			// getRowCount function:
			writer.write("public int getRowCount()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return ((Vector)data.elementAt(0)).size();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
	
			// getColumnName function:
			writer.write("public String getColumnName(int col)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return ((String)columnNames.elementAt(col));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// getColumnIndex function:
			writer.write("public int getColumnIndex(String columnName)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<columnNames.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String colName = (String)columnNames.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("if(colName.equals(columnName))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return i;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("return -1;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
	
			// getValueAt function:
			writer.write("public Object getValueAt(int row, int col)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return ((Vector)data.elementAt(col)).elementAt(row);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
	
			// setValueAt function:
			writer.write("public void setValueAt(Object value, int row, int col)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("((Vector)data.elementAt(col)).add(value);");
			writer.write(NEWLINE);
			writer.write("fireTableCellUpdated(row, col);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
	
			// initColNames function:
			writer.write("private void initColNames()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			// go through all attributes:
			Vector atts = type.getAllAttributes();
			for(int i=0; i<atts.size(); i++)
			{
				Attribute a = (Attribute)atts.elementAt(i);
				if(a.isVisible())
				{
					writer.write("columnNames.add(\"" + a.getName() + "\");");
					writer.write(NEWLINE);
				}
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
	
			// update function:
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			int index = 0;
			
			// generate code for all visible attributes:
			for(int i=0; i<atts.size(); i++)
			{
				Attribute a = (Attribute)atts.elementAt(i);
				if(a.isVisible())
				{
					writer.write("// Initialize " + a.getName() + ":");
					writer.write(NEWLINE);
					if(index == 0) // first visible attribute
					{
						writer.write("Vector " + type.getName().toLowerCase() + "s = state.get" + SimSEObjectTypeTypes.getText(type.getType())
							+ "StateRepository().get" + getUpperCaseLeading(type.getName()) + "StateRepository().getAll();");
						writer.write(NEWLINE);
						writer.write("Vector ");
					}
					writer.write("temp = new Vector();");
					writer.write(NEWLINE);
					writer.write("for(int i=0; i<" + type.getName().toLowerCase() + "s.size(); i++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if(a.getType() == AttributeTypes.STRING)
					{
						writer.write("temp.add(((" + getUpperCaseLeading(type.getName()) + ")" + type.getName().toLowerCase() + "s.elementAt(i)).get"
							+ a.getName() + "());");
					}
					else if(a.getType() == AttributeTypes.BOOLEAN)
					{
						writer.write("temp.add(new Boolean(((" + getUpperCaseLeading(type.getName()) + ")" + type.getName().toLowerCase() 
							+ "s.elementAt(i)).get" + a.getName() + "()));");
					}
					else if(a.getType() == AttributeTypes.INTEGER)
					{
						writer.write("numFormat.setMinimumFractionDigits(0);");
						writer.write(NEWLINE);
						writer.write("numFormat.setMaximumFractionDigits(0);");
						writer.write(NEWLINE);
						writer.write("temp.add(numFormat.format(((" + getUpperCaseLeading(type.getName()) + ")" + type.getName().toLowerCase()
							+ "s.elementAt(i)).get" + a.getName() + "()));");
					}
					else if(a.getType() == AttributeTypes.DOUBLE)
					{
						NumericalAttribute numAtt = (NumericalAttribute)a;
						writer.write("numFormat.setMinimumFractionDigits(");
						if(numAtt.getMinNumFractionDigits() != null) // has min num fraction digits
						{
							writer.write(numAtt.getMinNumFractionDigits().toString());
						}
						else
						{
							writer.write("0");
						}
						writer.write(");");
						writer.write(NEWLINE);
						writer.write("numFormat.setMaximumFractionDigits(");
						if(numAtt.getMaxNumFractionDigits() != null) // has max num fraction digits
						{
							writer.write(numAtt.getMaxNumFractionDigits().toString());
						}
						else
						{
							writer.write("16");
						}
						writer.write(");");
						writer.write(NEWLINE);		
						writer.write("temp.add(numFormat.format(((" + getUpperCaseLeading(type.getName()) + ")" 
							+ type.getName().toLowerCase() + "s.elementAt(i)).get" + a.getName() + "()));");
						writer.write(NEWLINE);	
					}
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);	
					writer.write("if(data.size() < " + (index + 1) + ")");
					writer.write(NEWLINE);	
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);	
					writer.write("data.add(temp);");
					writer.write(NEWLINE);	
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);	
					writer.write("else");
					writer.write(NEWLINE);	
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);	
					writer.write("data.setElementAt(temp, " + index + ");");
					writer.write(NEWLINE);	
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);	
					writer.write(NEWLINE);	
					index++;
				}
			}
			
			// generate code for all visible-at-end attributes:
			writer.write("if(state.getClock().isStopped()) // game over");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);			
			for(int i=0; i<atts.size(); i++)
			{
				Attribute a = (Attribute)atts.elementAt(i);
				if((a.isVisible() == false) && (a.isVisibleOnCompletion())) // visible only at end of game
				{
					writer.write("// Initialize " + a.getName() + ":");
					writer.write(NEWLINE);
					writer.write("if(columnNames.contains(\"" + a.getName() + "\") == false)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("columnNames.add(\"" + a.getName() + "\");");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);					
					writer.write("temp = new Vector();");
					writer.write(NEWLINE);
					writer.write("for(int i=0; i<" + type.getName().toLowerCase() + "s.size(); i++)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					if(a.getType() == AttributeTypes.STRING)
					{
						writer.write("temp.add(((" + getUpperCaseLeading(type.getName()) + ")" + type.getName().toLowerCase() + "s.elementAt(i)).get"
							+ a.getName() + "());");
					}
					else if(a.getType() == AttributeTypes.BOOLEAN)
					{
						writer.write("temp.add(new Boolean(((" + getUpperCaseLeading(type.getName()) + ")" + type.getName().toLowerCase() 
							+ "s.elementAt(i)).get" + a.getName() + "()));");
					}
					else if(a.getType() == AttributeTypes.INTEGER)
					{
						writer.write("numFormat.setMinimumFractionDigits(0);");
						writer.write(NEWLINE);
						writer.write("numFormat.setMaximumFractionDigits(0);");						
						writer.write(NEWLINE);
						writer.write("temp.add(numFormat.format(((" + getUpperCaseLeading(type.getName()) + ")" + type.getName().toLowerCase()
							+ "s.elementAt(i)).get" + a.getName() + "()));");
					}
					else if(a.getType() == AttributeTypes.DOUBLE)
					{
						NumericalAttribute numAtt = (NumericalAttribute)a;
						writer.write("numFormat.setMinimumFractionDigits(");
						if(numAtt.getMinNumFractionDigits() != null) // has min num fraction digits
						{
							writer.write(numAtt.getMinNumFractionDigits().toString());
						}
						else
						{
							writer.write("0");
						}
						writer.write(");");
						writer.write(NEWLINE);
						writer.write("numFormat.setMaximumFractionDigits(");
						if(numAtt.getMaxNumFractionDigits() != null) // has max num fraction digits
						{
							writer.write(numAtt.getMaxNumFractionDigits().toString());
						}
						else
						{
							writer.write("16");
						}
						writer.write(");");
						writer.write(NEWLINE);		
						writer.write("temp.add(numFormat.format(((" + getUpperCaseLeading(type.getName()) + ")" 
							+ type.getName().toLowerCase() + "s.elementAt(i)).get" + a.getName() + "()));");
						writer.write(NEWLINE);	
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);	
					writer.write("if(data.size() < " + (index + 1) + ")");
					writer.write(NEWLINE);	
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);	
					writer.write("data.add(temp);");
					writer.write(NEWLINE);	
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);	
					writer.write("else");
					writer.write(NEWLINE);	
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);	
					writer.write("data.setElementAt(temp, " + index + ");");
					writer.write(NEWLINE);	
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);	
					writer.write(NEWLINE);	
					index++;
				}
			}	
			writer.write("fireTableStructureChanged();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("fireTableDataChanged(); // notify listeners that table data has changed");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// getColumnClass method:
			writer.write("public Class getColumnClass(int c)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return getValueAt(0, c).getClass();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);			
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + tableModelFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
