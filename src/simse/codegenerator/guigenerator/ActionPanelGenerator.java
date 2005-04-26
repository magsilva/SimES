/* This class is responsible for generating all of the code for the action panel in the GUI */

package simse.codegenerator.guigenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.graphicsbuilder.*;
import simse.codegenerator.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ActionPanelGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	private String imageDirURL = "/simse/gui/icons/"; // location of images directory
	*/

	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file
	private DefinedActionTypes actTypes; // holds all of the defined action types from an ssa file
	private File directory; // directory to save generated code into


	public ActionPanelGenerator(DefinedObjectTypes objs, DefinedActionTypes acts, File dir)
	{
		objTypes = objs;
		actTypes = acts;
		directory = dir;
	}


	public void generate()
	{
		File actPanelFile = new File(directory, ("simse\\gui\\ActionPanel.java"));
		if(actPanelFile.exists())
		{
			actPanelFile.delete(); // delete old version of file
		}
		try
		{
			String dotEmpName = "";

			Vector vEmp = objTypes.getAllObjectTypesOfType(SimSEObjectTypeTypes.EMPLOYEE);

			for (int i = 0; i < vEmp.size();i++)
			{
				SimSEObjectType sso = (SimSEObjectType)vEmp.elementAt(i);
				dotEmpName = sso.getName();
			}


			FileWriter writer = new FileWriter(actPanelFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.logic.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.actions.*;");
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
			writer.write("public class ActionPanel extends JPanel implements MouseListener, ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Logic logic;");
			writer.write(NEWLINE);
			writer.write("private SimSEGUI mainGUIFrame;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JPopupMenu popup;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private Employee selectedEmp;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JPanel actionPaneMain;");
			writer.write(NEWLINE);
			writer.write("private Hashtable empsToEmpPanels;");
			writer.write(NEWLINE);
			writer.write("private Hashtable empsToPicPanels;");
			writer.write(NEWLINE);
			writer.write("//private Hashtable empsToActPanels;");
			writer.write(NEWLINE);
			writer.write("private Hashtable empsToPicLabels;");
			writer.write(NEWLINE);
			writer.write("private Hashtable empsToKeyLabels;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// constructor:
			writer.write("public ActionPanel(SimSEGUI gui, State s, Logic l)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("logic = l;");
			writer.write(NEWLINE);
			writer.write("mainGUIFrame = gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("actionPaneMain = new JPanel();");
			writer.write(NEWLINE);
			writer.write("actionPaneMain.setLayout(new BoxLayout(actionPaneMain, BoxLayout.Y_AXIS));");
			writer.write(NEWLINE);
			writer.write("actionPaneMain.setBackground(new Color(102, 102, 102, 255));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JScrollPane actionPane = new JScrollPane(actionPaneMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);");
			writer.write(NEWLINE);
			writer.write("actionPane.setPreferredSize(new Dimension(225, 495));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("empsToEmpPanels = new Hashtable();");
			writer.write(NEWLINE);
			writer.write("empsToPicPanels = new Hashtable();");
			writer.write(NEWLINE);
			writer.write("//empsToActPanels = new Hashtable();");
			writer.write(NEWLINE);
			writer.write("empsToPicLabels = new Hashtable();");
			writer.write(NEWLINE);
			writer.write("empsToKeyLabels = new Hashtable();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JPanel titlePanel = new JPanel(new BorderLayout());");
			writer.write(NEWLINE);
			writer.write("titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));");
			writer.write(NEWLINE);
			writer.write("titlePanel.setBackground(new Color(102, 102, 102, 255));");
			writer.write(NEWLINE);
			writer.write("JLabel titleLabel = new JLabel(\"Current Activities:\");");
			writer.write(NEWLINE);
			writer.write("Font f = titleLabel.getFont();");
			writer.write(NEWLINE);
			writer.write("Font newFont = new Font(f.getName(), f.getStyle(), 15);");
			writer.write(NEWLINE);
			writer.write("titleLabel.setFont(newFont);");
			writer.write(NEWLINE);
			writer.write("titleLabel.setForeground(Color.WHITE);");
			writer.write(NEWLINE);
			writer.write("titlePanel.add(titleLabel, BorderLayout.WEST);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("selectedEmp = null;");
			writer.write(NEWLINE);
			writer.write("popup = new JPopupMenu();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("update();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("add(titlePanel);");
			writer.write(NEWLINE);
			writer.write("add(actionPane);");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// createPopupMenu function:
			writer.write("public void createPopupMenu(Component c, int x, int y)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("popup.removeAll();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			writer.write("if(mainGUIFrame.getEngine().isRunning())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);

			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if(selectedEmp != null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector menuItems = selectedEmp.getMenu();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<menuItems.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String item = (String)menuItems.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("JMenuItem tempItem = new JMenuItem(item);");
			writer.write(NEWLINE);
			writer.write("tempItem.addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("popup.add(tempItem);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(menuItems.size() >= 1)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("popup.show(c, x, y);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
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
			// took the border stuff out because it wasn't showing up:
			/*writer.write("Dimension d = getSize();");
			writer.write(NEWLINE);
			writer.write("int width = (int)d.getWidth();");
			writer.write(NEWLINE);
			writer.write("g.setColor(new Color(102,102,102,255));");
			writer.write(NEWLINE);
			writer.write("g.fillRect(0, 0, width, (int)d.getHeight());");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// repeat the border down the height of screen:");
			writer.write(NEWLINE);
			writer.write("for (int i=0; i<(int)d.getHeight(); i+=100)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("g.drawImage(border, 0, i, this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);*/
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// update function:
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("actionPaneMain.removeAll();");
			writer.write(NEWLINE);
			writer.write("Vector allEmps = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<allEmps.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Employee emp = (Employee)allEmps.elementAt(i);");
			writer.write(NEWLINE);


if (CodeGenerator.allowHireFire)
{
writer.write("if (!emp.getHired())");
writer.write(NEWLINE);
writer.write("continue;");
writer.write(NEWLINE);
}

			writer.write("if(empsToEmpPanels.get(emp) == null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("empsToEmpPanels.put(emp, new JPanel());");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(empsToPicPanels.get(emp) == null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("empsToPicPanels.put(emp, new JPanel());");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("/*if(empsToActPanels.get(emp) == null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JPanel temp = new JPanel();");
			writer.write(NEWLINE);
			writer.write("temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));");
			writer.write(NEWLINE);
			writer.write("temp.setMinimumSize(new Dimension(150, 10));");
			writer.write(NEWLINE);
			writer.write("empsToActPanels.put(emp, temp);");
			writer.write(NEWLINE);
			writer.write("}*/");
			writer.write(NEWLINE);
			writer.write("JPanel empPanel = (JPanel)empsToEmpPanels.get(emp);");
			writer.write(NEWLINE);
			writer.write("empPanel.removeAll();");
			writer.write(NEWLINE);
			writer.write("JPanel picPanel = (JPanel)empsToPicPanels.get(emp);");
			writer.write(NEWLINE);
			writer.write("picPanel.removeAll();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("GridBagLayout gbLayout = new GridBagLayout();");
			writer.write(NEWLINE);
			writer.write("empPanel.setLayout(gbLayout);");
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc = new GridBagConstraints();");
			writer.write(NEWLINE);
			writer.write("gbc.fill = GridBagConstraints.NONE;");
			writer.write(NEWLINE);
			writer.write("gbc.gridwidth = 3;");
			writer.write(NEWLINE);
			writer.write("gbc.gridheight = 1;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("empPanel.setBackground(new Color(102, 102, 102, 255));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("picPanel.setLayout(new BoxLayout(picPanel, BoxLayout.Y_AXIS));");
			writer.write(NEWLINE);
			writer.write("picPanel.setBackground(new Color(102, 102, 102, 255));");
			writer.write(NEWLINE);
			writer.write("if(empsToPicLabels.get(emp) == null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("ImageIcon ico = new ImageIcon(ImageLoader.getImageFromURL(TabPanel.getImage(emp)));");
			writer.write(NEWLINE);
			writer.write("Image scaledImage = ico.getImage().getScaledInstance(35, 35, Image.SCALE_AREA_AVERAGING);");
			writer.write(NEWLINE);
			writer.write("ico.setImage(scaledImage);");
			writer.write(NEWLINE);
			writer.write("JLabel temp = new JLabel(ico);");
			writer.write(NEWLINE);
			writer.write("temp.addMouseListener(this);");
			writer.write(NEWLINE);
			writer.write("empsToPicLabels.put(emp, temp);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JLabel picLabel = (JLabel)empsToPicLabels.get(emp);");
			writer.write(NEWLINE);
			writer.write("picLabel.setHorizontalAlignment(SwingConstants.LEFT);");
			writer.write(NEWLINE);
			writer.write("picPanel.add(picLabel);");
			writer.write(NEWLINE);

			// go through all employee types:
			Vector empTypes = objTypes.getAllObjectTypesOfType(SimSEObjectTypeTypes.EMPLOYEE);
			for(int i=0; i<empTypes.size(); i++)
			{
				SimSEObjectType tempType = (SimSEObjectType)empTypes.elementAt(i);
				if(i > 0)
				{
					writer.write("else ");
				}

Vector v =  tempType.getAllAttributes();
Attribute keyAtt = null;

for (int j = 0; j < v.size(); j++)
{
	Attribute att = (Attribute)v.elementAt(j);

	if (att.isKey())
		keyAtt = att;
}


				writer.write("if(emp instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(tempType.getName()) + " e = (" + getUpperCaseLeading(tempType.getName()) + ")emp;");
				writer.write(NEWLINE);
				writer.write("if(empsToKeyLabels.get(e) == null)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write("JLabel temp = new JLabel(\"\" + e.get"+getUpperCaseLeading(keyAtt.getName())+"());");
				writer.write(NEWLINE);
				writer.write("temp.setForeground(Color.WHITE);");
				writer.write(NEWLINE);
				writer.write("temp.setHorizontalAlignment(SwingConstants.LEFT);");
				writer.write(NEWLINE);
				writer.write("temp.setHorizontalTextPosition(SwingConstants.LEFT);");
				writer.write(NEWLINE);
				writer.write("empsToKeyLabels.put(e, temp);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write("JLabel keyLabel = (JLabel)empsToKeyLabels.get(e);");
				writer.write(NEWLINE);
				writer.write("picPanel.add(keyLabel);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}

			writer.write("picPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));");
			writer.write(NEWLINE);
			writer.write("gbc.weightx = 1;");
			writer.write(NEWLINE);
			writer.write("gbc.weighty = 1;");
			writer.write(NEWLINE);
			writer.write("gbc.anchor = GridBagConstraints.WEST;");
			writer.write(NEWLINE);
			writer.write("empPanel.add(picPanel);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JPanel actsPanel = new JPanel();");
			writer.write(NEWLINE);
			writer.write("//actsPanel.removeAll();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("actsPanel.setLayout(new BoxLayout(actsPanel, BoxLayout.Y_AXIS));");
			writer.write(NEWLINE);
			writer.write("actsPanel.setBackground(new Color(102, 102, 102, 255));");
			writer.write(NEWLINE);
			writer.write("Vector acts = state.getActionStateRepository().getAllActions(emp);");
			writer.write(NEWLINE);
			writer.write("for(int j=0; j<acts.size(); j++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("simse.adts.actions.Action tempAct = (simse.adts.actions.Action)acts.elementAt(j);");
			writer.write(NEWLINE);

			// go through all action types and generate code for those that are visible:
			Vector allActs = actTypes.getAllActionTypes();
			boolean putElse = false;
			for(int i=0; i<allActs.size(); i++)
			{
				ActionType tempActType = (ActionType)allActs.elementAt(i);
				if((tempActType.isVisible()) && (tempActType.getDescription() != null) && (tempActType.getDescription().length() > 0))
				{
					if(putElse)
					{
						writer.write("else ");
					}
					else
					{
						putElse = true;
					}
					writer.write("if(tempAct instanceof " + getUpperCaseLeading(tempActType.getName()) + "Action)");
					writer.write(NEWLINE);
					writer.write(OPEN_BRACK);
					writer.write(NEWLINE);
					writer.write("JLabel tempLabel = new JLabel(\"" + tempActType.getDescription() + "\");");
					writer.write(NEWLINE);
					writer.write("tempLabel.setFont(new Font(tempLabel.getFont().getName(), tempLabel.getFont().getStyle(), 10));");
					writer.write(NEWLINE);
					writer.write("tempLabel.setForeground(Color.WHITE);");
					writer.write(NEWLINE);
					writer.write("actsPanel.add(tempLabel);");
					writer.write(NEWLINE);
					writer.write(CLOSED_BRACK);
					writer.write(NEWLINE);
				}
			}
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("gbc.weightx = 2;");
			writer.write(NEWLINE);
			writer.write("gbc.anchor = GridBagConstraints.EAST;");
			writer.write(NEWLINE);
			writer.write("actsPanel.setPreferredSize(new Dimension(150, (int)((Dimension)actsPanel.getPreferredSize()).getHeight()));");
			writer.write(NEWLINE);
			writer.write("empPanel.add(actsPanel);");
			writer.write(NEWLINE);
			writer.write("empPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("actionPaneMain.add(empPanel);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write("actionPaneMain.update(actionPaneMain.getGraphics());");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mouseListener functions:
			writer.write("public void mouseClicked(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mousePressed(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mouseEntered(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write("public void mouseExited(MouseEvent me){}");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mouseReleased function:
			writer.write("public void mouseReleased(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(me.getComponent() instanceof JLabel)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JLabel label = (JLabel)me.getComponent();");
			writer.write(NEWLINE);
			writer.write("Employee emp = getEmpFromPicLabel(label);");
			writer.write(NEWLINE);
			writer.write("if(emp != null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(me.getButton() == MouseEvent.BUTTON1) // left button clicked");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("mainGUIFrame.getTabPanel().setGUIChanged();");
			writer.write(NEWLINE);
			writer.write("mainGUIFrame.getTabPanel().setObjectInFocus(emp);");
			writer.write(NEWLINE);
			writer.write("mainGUIFrame.getAttributePanel().setGUIChanged();");
			writer.write(NEWLINE);
			writer.write("mainGUIFrame.getAttributePanel().setObjectInFocus(emp, new ImageIcon(ImageLoader.getImageFromURL(TabPanel.getImage(emp))));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if(me.isPopupTrigger() && (state.getClock().isStopped() == false)) // right-click");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("selectedEmp = emp;");
			writer.write(NEWLINE);
			writer.write("createPopupMenu(label, me.getX(), me.getY());");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// popupMenuActions function:
			writer.write("public void popupMenuActions(JMenuItem source)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JMenuItem item = (JMenuItem)source;");
			writer.write(NEWLINE);
			writer.write("logic.getMenuInputManager().menuItemSelected(selectedEmp, item.getText(), mainGUIFrame);");
			writer.write(NEWLINE);
			writer.write("mainGUIFrame.getWorld().update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// actionPerformed function:
			writer.write("public void actionPerformed(ActionEvent e)	// dealing with actions generated by popup menus");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = e.getSource();");
			writer.write(NEWLINE);
			writer.write("if(source instanceof JMenuItem)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("popupMenuActions((JMenuItem)source);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getEmpFromPicLabel function:
			writer.write("private Employee getEmpFromPicLabel(JLabel label)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("for(Enumeration keys=empsToPicLabels.keys(); keys.hasMoreElements();)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Employee keyEmp = (Employee)keys.nextElement();");
			writer.write(NEWLINE);
			writer.write("if(((JLabel)(empsToPicLabels.get(keyEmp))) == label)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return keyEmp;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("return null;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + actPanelFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}


	private String getUpperCaseLeading(String s)
	{
		return (s.substring(0, 1).toUpperCase() + s.substring(1));
	}
}
