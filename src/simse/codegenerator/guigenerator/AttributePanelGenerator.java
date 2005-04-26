/* This class is responsible for generating all of the code for the attribute panel in the GUI */

package simse.codegenerator.guigenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.graphicsbuilder.*;
import simse.codegenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class AttributePanelGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	private String imageDirURL = "/simse/gui/icons/"; // location of images directory
	*/

	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file
	private File directory; // directory to save generated code into


	public AttributePanelGenerator(DefinedObjectTypes objs, File dir)
	{
		objTypes = objs;
		directory = dir;
	}


	public void generate()
	{
		File attPanelFile = new File(directory, ("simse\\gui\\AttributePanel.java"));
		if(attPanelFile.exists())
		{
			attPanelFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(attPanelFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import simse.engine.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import java.text.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Dimension;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.text.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Color;");
			writer.write(NEWLINE);
			writer.write("import java.io.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public class AttributePanel extends JPanel");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private final int ATTRIBUTE_LIST_CAPACITY = 5; // number of attributes that can be displayed in a list without making the list scrollable");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private GridBagLayout gbl;");
			writer.write(NEWLINE);
			writer.write("private ClockPanel clockPane;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private NumberFormat numFormat;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JScrollPane attributePaneLeft;");
			writer.write(NEWLINE);
			writer.write("private JScrollPane attributePaneRight;");
			writer.write(NEWLINE);
			writer.write("private JList attributeListLeft;");
			writer.write(NEWLINE);
			writer.write("private JList attributeListRight;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private Vector attributes;");
			writer.write(NEWLINE);
			writer.write("private SSObject objInFocus = null;");
			writer.write(NEWLINE);
			writer.write("private ImageIcon displayedIcon;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JLabel selectedIcon;");
			writer.write(NEWLINE);
			writer.write("private JPanel iconPanel;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private Image border;");
			writer.write(NEWLINE);
			writer.write("private Image iconBorder;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private boolean guiChanged;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// constructor:
			writer.write("public AttributePanel(SimSEGUI g, State s, Engine e)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("gbl = new GridBagLayout();");
			writer.write(NEWLINE);
			writer.write("setLayout(gbl);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("border = ImageLoader.getImageFromURL(\"" + imagesDirectory + "layout/border.gif\");");
			writer.write(NEWLINE);
			writer.write("iconBorder = ImageLoader.getImageFromURL(\"" + imagesDirectory + "layout/iconBorder.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("clockPane = new ClockPanel(g,s,e);");
			writer.write(NEWLINE);
			writer.write("clockPane.setPreferredSize(new Dimension(250,100));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("numFormat = NumberFormat.getNumberInstance(Locale.US);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("attributes = new Vector();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("attributeListLeft = new JList();");
			writer.write(NEWLINE);
			writer.write("attributePaneLeft = new JScrollPane(attributeListLeft, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);");
			writer.write(NEWLINE);
			writer.write("attributePaneLeft.setPreferredSize(new Dimension(300,95));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("attributeListRight = new JList();");
			writer.write(NEWLINE);
			writer.write("attributePaneRight = new JScrollPane(attributeListRight, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);");
			writer.write(NEWLINE);
			writer.write("attributePaneRight.setPreferredSize(new Dimension(300,95));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JPanel attributePane = new JPanel();");
			writer.write(NEWLINE);
			writer.write("((FlowLayout)attributePane.getLayout()).setHgap(5);");
			writer.write(NEWLINE);
			writer.write("((FlowLayout)attributePane.getLayout()).setVgap(0);");
			writer.write(NEWLINE);
			writer.write("attributePane.add(attributePaneLeft);");
			writer.write(NEWLINE);
			writer.write("attributePane.add(attributePaneRight);");
			writer.write(NEWLINE);
			writer.write("attributePane.setBackground(new Color(102, 102, 102, 255));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("iconPanel = new JPanel(gbl);");
			writer.write(NEWLINE);
			writer.write("iconPanel.setBackground(new Color(0,0,0,0));");
			writer.write(NEWLINE);
			writer.write("iconPanel.setPreferredSize(new Dimension(90,90));");
			writer.write(NEWLINE);
			writer.write("selectedIcon = new JLabel(new ImageIcon(ImageLoader.getImageFromURL(\"" + imagesDirectory + "grid.gif\")));");
			writer.write(NEWLINE);
			writer.write("selectedIcon.setOpaque(true);");
			writer.write(NEWLINE);
			writer.write("selectedIcon.setPreferredSize(new Dimension(50,50));");
			writer.write(NEWLINE);
			writer.write("selectedIcon.setMinimumSize(new Dimension(50,50));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc;");
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,0,1,1, 0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(-3,5,0,0), 0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(selectedIcon,gbc);");
			writer.write(NEWLINE);
			writer.write("iconPanel.add(selectedIcon);");
			writer.write(NEWLINE);
			writer.write("add(iconPanel);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(attributePane, gbc);");
			writer.write(NEWLINE);
			writer.write("add(attributePane);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(3, 0, 1, 1, 1, 1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(clockPane,gbc);");
			writer.write(NEWLINE);
			writer.write("add(clockPane);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// paintComponent function:
			writer.write("public void paintComponent(Graphics g)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Dimension d = getSize();");
			writer.write(NEWLINE);
			writer.write("int width = (int)d.getWidth();");
			writer.write(NEWLINE);
			writer.write("g.setColor(new Color(102,102,102,255));");
			writer.write(NEWLINE);
			writer.write("g.fillRect(0,0,width,110);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// repeat the border across the width of screen:");
			writer.write(NEWLINE);
			writer.write("for (int i = 0; i < width; i+=100)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("g.drawImage(border,i,0,this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("// draw the design for the selectedIcon");
			writer.write(NEWLINE);
			writer.write("g.drawImage(iconBorder,05,11,this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setObjectInFocus function:
			writer.write("public void setObjectInFocus(SSObject obj, Icon img)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("objInFocus = obj;");
			writer.write(NEWLINE);
			writer.write("if(img != null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setIcon(img);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("updateAttributeList();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// update function:
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("updateAttributeList();");
			writer.write(NEWLINE);
			writer.write("clockPane.update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// setGUIChanged function:
			writer.write("public void setGUIChanged()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("guiChanged = true;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// updateAttributeList function:
			writer.write("private void updateAttributeList()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

//			writer.write("if(!guiChanged)");

			writer.write("if(false)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("attributes.clear();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// loop through all SimSEObjectTypeTypes:
			String[] metaTypes = SimSEObjectTypeTypes.getAllTypesAsStrings();
			for(int i=0; i<metaTypes.length; i++)
			{
				String typeName = metaTypes[i];
				writer.write("// " + typeName + ":");
				writer.write(NEWLINE);
				if(i > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if((objInFocus != null) && state.get" + typeName + "StateRepository().getAll().contains(objInFocus))");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				// go through all object types:
				Vector types = objTypes.getAllObjectTypes();
				// Make a vector of only the types that have the correct meta type:
				Vector correctTypes = new Vector();
				for(int j=0; j<types.size(); j++)
				{
					SimSEObjectType temp = (SimSEObjectType)types.elementAt(j);
					if(temp.getType() == SimSEObjectTypeTypes.getIntRepresentation(typeName))
					{
						correctTypes.add(temp);
					}
				}
				for(int j=0; j<correctTypes.size(); j++)
				{
					SimSEObjectType tempType = (SimSEObjectType)correctTypes.elementAt(j);
					if(j > 0) // not on first element
					{
						writer.write("else ");
					}
					writer.write("if(objInFocus instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write(getUpperCaseLeading(tempType.getName()) + " p = (" + getUpperCaseLeading(tempType.getName()) + ")objInFocus;");
					writer.write(NEWLINE);
					writer.write("attributes.add(\"<html><font size=2>Type: " + tempType.getName() + "</font></html>\");");
					writer.write(NEWLINE);
					// go through all attributes:
					Vector atts = tempType.getAllAttributes();
					for(int k=0; k<atts.size(); k++)
					{
						Attribute a = (Attribute)atts.elementAt(k);
						if(a.isVisible())
						{
							if(a.getType() == AttributeTypes.DOUBLE) // double att -- need to do formatting stuff
							{
								NumericalAttribute numAtt = (NumericalAttribute)a;
								if(numAtt.getMinNumFractionDigits() != null) // has a min num fraction digits
								{
									writer.write("numFormat.setMinimumFractionDigits(" + numAtt.getMinNumFractionDigits().intValue() + ");");
									writer.write(NEWLINE);
								}
								else
								{
									// set it to the default minimum:
									writer.write("numFormat.setMinimumFractionDigits(0);");
									writer.write(NEWLINE);
								}
								if(numAtt.getMaxNumFractionDigits() != null) // has a max num fraction digits
								{
									writer.write("numFormat.setMaximumFractionDigits(" + numAtt.getMaxNumFractionDigits().intValue() + ");");
									writer.write(NEWLINE);
								}
								else
								{
									// set it to the default maximum:
									writer.write("numFormat.setMaximumFractionDigits(16);");
									writer.write(NEWLINE);
								}
								writer.write("attributes.add(\"<html><font size=2>" + numAtt.getName() + ": \" + numFormat.format(p.get"
									+ getUpperCaseLeading(numAtt.getName()) + "()) + \"</font></html>\");");
								writer.write(NEWLINE);
							}
							else // non-double att -- no formatting required
							{
								writer.write("attributes.add(\"<html><font size=2>" + a.getName() + ": \" + p.get" + getUpperCaseLeading(a.getName()) + "() + \"</font></html>\");");
								writer.write(NEWLINE);
							}
						}
						else if(a.isVisibleOnCompletion())
						{
							writer.write("if(state.getClock().isStopped()) // game is over");
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							if(a.getType() == AttributeTypes.DOUBLE) // double att -- need to do formatting stuff
							{
								NumericalAttribute numAtt = (NumericalAttribute)a;
								if(numAtt.getMinNumFractionDigits() != null) // has a min num fraction digits
								{
									writer.write("numFormat.setMinimumFractionDigits(" + numAtt.getMinNumFractionDigits().intValue() + ");");
									writer.write(NEWLINE);
								}
								else
								{
									// set it to the default minimum:
									writer.write("numFormat.setMinimumFractionDigits(0);");
									writer.write(NEWLINE);
								}
								if(numAtt.getMaxNumFractionDigits() != null) // has a max num fraction digits
								{
									writer.write("numFormat.setMaximumFractionDigits(" + numAtt.getMaxNumFractionDigits().intValue() + ");");
									writer.write(NEWLINE);
								}
								else
								{
									// set it to the default maximum:
									writer.write("numFormat.setMaximumFractionDigits(16);");
									writer.write(NEWLINE);
								}
								writer.write("attributes.add(\"<html><font size=2>" + numAtt.getName() + ": \" + numFormat.format(p.get"
									+ numAtt.getName() + "()) + \"</font></html>\");");
								writer.write(NEWLINE);
							}
							else // non-double att -- no formatting required
							{
								writer.write("attributes.add(\"<html><font size=2>" + a.getName() + ": \" + p.get" + a.getName() + "() + \"</font></html>\");");
								writer.write(NEWLINE);
							}
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
					}
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setIcon(new ImageIcon(ImageLoader.getImageFromURL(\"" + imagesDirectory + "grid.gif\")));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("attributeListLeft.setListData(attributes);");
			writer.write(NEWLINE);
			writer.write("attributeListRight.setListData(new Vector());");
			writer.write(NEWLINE);
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// distribute attributes to both sides, if needed:");
			writer.write(NEWLINE);
			writer.write("Vector rightHandAtts = new Vector();");
			writer.write(NEWLINE);
			writer.write("if(attributes.size() > ATTRIBUTE_LIST_CAPACITY) // need to use 2nd list");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("while((ATTRIBUTE_LIST_CAPACITY) < attributes.size()) // there are still more elements to move to right");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("rightHandAtts.add(attributes.remove(ATTRIBUTE_LIST_CAPACITY)); // remove from left, put on right");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("attributeListRight.setListData(rightHandAtts);");
			writer.write(NEWLINE);
			writer.write("attributeListLeft.setListData(attributes);");
			writer.write(NEWLINE);
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if(attributePaneLeft.getHorizontalScrollBar().isVisible()) // need to move one more over to account for extra space that");
			writer.write(NEWLINE);
			writer.write("// scrollbar takes up");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("rightHandAtts.add(0, attributes.remove(attributes.size() - 1)); // move");
			writer.write(NEWLINE);
			writer.write("attributeListRight.setListData(rightHandAtts);");
			writer.write(NEWLINE);
			writer.write("attributeListLeft.setListData(attributes);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write("guiChanged = false;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setIcon function:
			writer.write("public void setIcon(Icon img)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("selectedIcon.setBackground(Color.WHITE);");
			writer.write(NEWLINE);
			writer.write("selectedIcon.setIcon(img);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + attPanelFile.getPath() + ": " + e.toString()), "File IO Error",
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
