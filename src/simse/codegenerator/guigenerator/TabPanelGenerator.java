/* This class is responsible for generating all of the code for the tab panel in the GUI */

package simse.codegenerator.guigenerator;

import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.graphicsbuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class TabPanelGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into
	private DefinedObjectTypes objTypes; // holds all of the defined object types from an sso file
	private Hashtable objsToImages; // maps SimSEObjects (keys) to pathname (String) of image file (values)
	private String imageDirURL = "/simse/gui/icons/"; // path of image directory

	public TabPanelGenerator(DefinedObjectTypes dots, Hashtable oToI, File dir)
	{
		objTypes = dots;
		directory = dir;
		objsToImages = oToI;
	}


	public void generate()
	{
		// generate file:
		File tabPanelFile = new File(directory, ("simse\\gui\\TabPanel.java"));
		if(tabPanelFile.exists())
		{
			tabPanelFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(tabPanelFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write("import simse.adts.objects.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
			writer.write(NEWLINE);
			writer.write("import simse.logic.*;");
			writer.write(NEWLINE);
			writer.write("import simse.engine.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Dimension;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.border.*;");
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
			writer.write("public class TabPanel extends JPanel implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("public static final int ARTIFACT = 0;");
			writer.write(NEWLINE);
			writer.write("public static final int CUSTOMER = 1;");
			writer.write(NEWLINE);
			writer.write("public static final int EMPLOYEE = 2;");
			writer.write(NEWLINE);
			writer.write("public static final int PROJECT = 3;");
			writer.write(NEWLINE);
			writer.write("public static final int TOOL = 4;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public static final int MAXBUTTONS = 16;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private LogoPanel logoPane;");
			writer.write(NEWLINE);
			writer.write("private AttributePanel attributePane;");
			writer.write(NEWLINE);
			// get all SimSEObjectTypeTypes:
			String[] types = SimSEObjectTypeTypes.getAllTypesAsStrings();
			for(int i=0; i<types.length; i++)
			{
				writer.write("private " + types[i] + "sAtAGlanceFrame " + types[i].toLowerCase() + "Frame;");
				writer.write(NEWLINE);
			}

			writer.write(NEWLINE);
			writer.write("private GridBagLayout gbl;");
			writer.write(NEWLINE);
			writer.write("private boolean guiChanged;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// the 5 different tabs:");
			writer.write(NEWLINE);
			writer.write("private JButton[] artifactButton;");
			writer.write(NEWLINE);
			writer.write("private JButton[] customerButton;");
			writer.write(NEWLINE);
			writer.write("private JButton[] employeeButton;");
			writer.write(NEWLINE);
			writer.write("private JButton[] projectButton;");
			writer.write(NEWLINE);
			writer.write("private JButton[] toolButton;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Hashtable objsToImages; // maps Objects (keys) to ImageIcons (values)");
			writer.write(NEWLINE);
			writer.write("private Hashtable buttonsToObjs; // maps JButtons (keys) to Objects (values)");
			writer.write(NEWLINE);
			writer.write("private JPanel buttonsPane;");
			writer.write(NEWLINE);
			writer.write("private SSObject objInFocus = null;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// for the blue line around the icons:");
			writer.write(NEWLINE);
			writer.write("private Border defaultBorder;");
			writer.write(NEWLINE);
			writer.write("private Border selectedBorder;");
			writer.write(NEWLINE);
			writer.write("private Color btnBlue = new Color(180,180,255,255);");
			writer.write(NEWLINE);
			writer.write("private Image border;");
			writer.write(NEWLINE);
			writer.write("private ImageIcon allIcon;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// constructor:
			writer.write("public TabPanel(State s, AttributePanel a)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("guiChanged = true;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("attributePane = a;");
			writer.write(NEWLINE);
			writer.write("objsToImages = new Hashtable();");
			writer.write(NEWLINE);
			writer.write("buttonsToObjs = new Hashtable();");
			writer.write(NEWLINE);
			for(int i=0; i<types.length; i++)
			{
				writer.write(types[i].toLowerCase() + "Frame = new " + types[i] + "sAtAGlanceFrame(state);");
				writer.write(NEWLINE);
			}
			writer.write(NEWLINE);;
			writer.write("border = ImageLoader.getImageFromURL(\"" + imageDirURL + "layout/border.gif\");");
			writer.write(NEWLINE);
			writer.write("allIcon = new ImageIcon(ImageLoader.getImageFromURL(\"/simse/gui/icons/all.GIF\"));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// get the Border styles:");
			writer.write(NEWLINE);
			writer.write("defaultBorder = new JButton().getBorder();");
			writer.write(NEWLINE);
			writer.write("selectedBorder = new BevelBorder(BevelBorder.RAISED,new Color(80,80,225,255), new Color(0,0,115,255));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Create main panel:");
			writer.write(NEWLINE);
			writer.write("gbl = new GridBagLayout();");
			writer.write(NEWLINE);
			writer.write("setBackground(new Color(102,102,102,255));");
			writer.write(NEWLINE);
			writer.write("setLayout(gbl);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JPanel mainPane = new JPanel(gbl);");
			writer.write(NEWLINE);
			writer.write("logoPane = new LogoPanel();");
			writer.write(NEWLINE);
			writer.write("logoPane.setMinimumSize(new Dimension(325,90));");
			writer.write(NEWLINE);
			writer.write("logoPane.setPreferredSize(new Dimension(325,90));");
			writer.write(NEWLINE);
			writer.write("logoPane.setTabPanel(this);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Create buttons pane:");
			writer.write(NEWLINE);
			writer.write("buttonsPane = new JPanel(gbl);");
			writer.write(NEWLINE);
			writer.write("buttonsPane.setBackground(new Color(69,135,156,255)); // dark green color");
			writer.write(NEWLINE);
			writer.write("JScrollPane buttonsScrollPane = new JScrollPane(buttonsPane, JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);");
			writer.write(NEWLINE);
			writer.write("buttonsScrollPane.setPreferredSize(new Dimension(295, 80));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("generateButtons();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Add panes and labels to main pane:");
			writer.write(NEWLINE);
			writer.write("mainPane.add(buttonsScrollPane);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Add Logo Pane:");
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,0,1,2,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(logoPane,gbc);");
			writer.write(NEWLINE);
			writer.write("add(logoPane);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// Add main pane to this pane:");
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(5,0,0,0), 0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(mainPane,gbc);");
			writer.write(NEWLINE);
			writer.write("add(mainPane);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("setPreferredSize(new Dimension(800, 100));");
			writer.write(NEWLINE);
			writer.write("updateImages();");
			writer.write(NEWLINE);
			writer.write("update();");
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
			writer.write("g.fillRect(0,0,width,100);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// repeat the border across the width of screen:");
			writer.write(NEWLINE);
			writer.write("for (int i = 0; i < width; i+=100)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("g.drawImage(border,i,92,this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// generateButtons function:
			writer.write("public void generateButtons()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("artifactButton = new JButton[MAXBUTTONS];");
			writer.write(NEWLINE);
			writer.write("customerButton = new JButton[MAXBUTTONS];");
			writer.write(NEWLINE);
			writer.write("employeeButton = new JButton[MAXBUTTONS];");
			writer.write(NEWLINE);
			writer.write("projectButton = new JButton[MAXBUTTONS];");
			writer.write(NEWLINE);
			writer.write("toolButton = new JButton[MAXBUTTONS];");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// generate list of <maxbuttons>:");
			writer.write(NEWLINE);
			writer.write("for (int i = 0; i < MAXBUTTONS; i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("artifactButton[i] = new JButton();");
			writer.write(NEWLINE);
			writer.write("artifactButton[i].addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("customerButton[i] = new JButton();");
			writer.write(NEWLINE);
			writer.write("customerButton[i].addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("employeeButton[i] = new JButton();");
			writer.write(NEWLINE);
			writer.write("employeeButton[i].addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("projectButton[i] = new JButton();");
			writer.write(NEWLINE);
			writer.write("projectButton[i].addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("toolButton[i] = new JButton();");
			writer.write(NEWLINE);
			writer.write("toolButton[i].addActionListener(this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("setButtonConstraints(artifactButton);");
			writer.write(NEWLINE);
			writer.write("setButtonConstraints(customerButton);");
			writer.write(NEWLINE);
			writer.write("setButtonConstraints(employeeButton);");
			writer.write(NEWLINE);
			writer.write("setButtonConstraints(projectButton);");
			writer.write(NEWLINE);
			writer.write("setButtonConstraints(toolButton);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setButtonConstraints function:
			writer.write("public void setButtonConstraints(JButton[] button)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Dimension dim = new Dimension(35,35);");
			writer.write(NEWLINE);
			writer.write("int shift;");
			writer.write(NEWLINE);
			writer.write("int index;");
			writer.write(NEWLINE);
			writer.write("for (int j = 0; j < 2; j++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("shift = 8 * j;");
			writer.write(NEWLINE);
			writer.write("for (int i = 0; i < MAXBUTTONS / 2; i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc = new GridBagConstraints(i,j,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(2,1,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("index = shift + i;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("button[index].setIcon(null);");
			writer.write(NEWLINE);
			writer.write("button[index].setPreferredSize(dim);");
			writer.write(NEWLINE);
			writer.write("button[index].setBackground(Color.LIGHT_GRAY);");
			writer.write(NEWLINE);
			writer.write("button[index].setBorder(defaultBorder);");
			writer.write(NEWLINE);
			writer.write("button[index].setEnabled(false);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(button[index], gbc);");
			writer.write(NEWLINE);
			writer.write("buttonsPane.add(button[index]);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// actionPerformed function:
			writer.write("public void actionPerformed(ActionEvent evt)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("guiChanged = true;");
			writer.write(NEWLINE);
			writer.write("Object source = evt.getSource();");
			writer.write(NEWLINE);
			writer.write("if(source instanceof JButton)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JButton button = (JButton)source;");
			writer.write(NEWLINE);
			writer.write("if(buttonsToObjs.get(button) != null)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("attributePane.setGUIChanged();");
			writer.write(NEWLINE);
			writer.write("objInFocus = (SSObject)buttonsToObjs.get(button);");
			writer.write(NEWLINE);
			writer.write("String filename = getImage(objInFocus);");
			writer.write(NEWLINE);
			writer.write("attributePane.setObjectInFocus(objInFocus, (new ImageIcon(ImageLoader.getImageFromURL(filename))));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("Enumeration buttons = buttonsToObjs.keys();");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<buttonsToObjs.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JButton key = (JButton)buttons.nextElement();");
			writer.write(NEWLINE);
			writer.write("key.setBackground(Color.WHITE);");
			writer.write(NEWLINE);
			writer.write("key.setBorder(defaultBorder);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("button.setBackground(btnBlue);");
			writer.write(NEWLINE);
			writer.write("button.setBorder(selectedBorder);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if(((ImageIcon)button.getIcon()).equals(allIcon))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("switch(logoPane.getSelectedTabIndex())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			for(int i=0; i<types.length; i++)
			{
				writer.write("case " + types[i].toUpperCase() + ":");
				writer.write(NEWLINE);
				writer.write("if(" + types[i].toLowerCase() + "Frame.getState()==Frame.ICONIFIED)");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
     		 	writer.write(types[i].toLowerCase() + "Frame.setState(Frame.NORMAL);");
				writer.write(NEWLINE);
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
				writer.write(types[i].toLowerCase() + "Frame.setVisible(true);");
				writer.write(NEWLINE);
				writer.write("break;");
				writer.write(NEWLINE);
			}
			writer.write("default:");
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

			// setObjectInFocus function:
			writer.write("public void setObjectInFocus(SSObject obj)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("objInFocus = obj;");
			writer.write(NEWLINE);
			writer.write("update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// setGUIChanged function
			writer.write("public void setGUIChanged()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("guiChanged = true;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);


			// update function:
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(!guiChanged)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("// clear buttons:");
			writer.write(NEWLINE);
			writer.write("buttonsToObjs.clear();");
			writer.write(NEWLINE);
			writer.write("buttonsPane.removeAll();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// update images:");
			writer.write(NEWLINE);
			writer.write("updateImages();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JButton[] buttonList;");
			writer.write(NEWLINE);
			writer.write("Vector objs;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("switch (logoPane.getSelectedTabIndex())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("case ARTIFACT:");
			writer.write(NEWLINE);
			writer.write("buttonList = artifactButton;");
			writer.write(NEWLINE);
			writer.write("objs = state.getArtifactStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case CUSTOMER:");
			writer.write(NEWLINE);
			writer.write("buttonList = customerButton;");
			writer.write(NEWLINE);
			writer.write("objs = state.getCustomerStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case EMPLOYEE:");
			writer.write(NEWLINE);
			writer.write("buttonList = employeeButton;");
			writer.write(NEWLINE);
			writer.write("objs = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case PROJECT:");
			writer.write(NEWLINE);
			writer.write("buttonList = projectButton;");
			writer.write(NEWLINE);
			writer.write("objs = state.getProjectStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case TOOL:");
			writer.write(NEWLINE);
			writer.write("buttonList = toolButton;");
			writer.write(NEWLINE);
			writer.write("objs = state.getToolStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("default:");
			writer.write(NEWLINE);
			writer.write("buttonList = toolButton;");
			writer.write(NEWLINE);
			writer.write("objs = new Vector();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("setButtonConstraints(buttonList);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if(logoPane.getSelectedTabIndex() != -1) // there is a type of object selected");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("boolean atLeastOneObj = false;");
			writer.write(NEWLINE);
			writer.write("if(objs.size() > 0) // there is at least one object of the selected type");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("atLeastOneObj = true;");
			writer.write(NEWLINE);
			writer.write("JButton allButton = buttonList[0];");
			writer.write(NEWLINE);
			writer.write("allButton.setEnabled(true);");
			writer.write(NEWLINE);
			writer.write("allButton.setBorder(defaultBorder);");
			writer.write(NEWLINE);
			writer.write("allButton.setIcon(allIcon);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// go through all objects:");
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<objs.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("SSObject obj = (SSObject)objs.elementAt(i);");
			writer.write(NEWLINE);
			writer.write("JButton button = null;");
			writer.write(NEWLINE);
			writer.write("if(atLeastOneObj)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("button = buttonList[i + 1];");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("button = buttonList[i];");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("button.setEnabled(true);");
			writer.write(NEWLINE);
			writer.write("button.setIcon((ImageIcon)(objsToImages.get(obj)));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if(obj.equals(objInFocus))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("button.setBackground(btnBlue);");
			writer.write(NEWLINE);
			writer.write("button.setBorder(selectedBorder);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("button.setBackground(Color.WHITE);");
			writer.write(NEWLINE);
			writer.write("button.setBorder(defaultBorder);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("buttonsToObjs.put(button, obj);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if(i == (MAXBUTTONS - 1)) // reached the max");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("attributePane.update();");
			writer.write(NEWLINE);
			for(int i=0; i<types.length; i++)
			{
				writer.write(types[i].toLowerCase() + "Frame.update();");
				writer.write(NEWLINE);
			}
			writer.write("validate();");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("guiChanged= false;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// updateImages function:
			writer.write("private void updateImages()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Vector objs;");
			writer.write(NEWLINE);
			writer.write("switch(logoPane.getSelectedTabIndex())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("case ARTIFACT:");
			writer.write(NEWLINE);
			writer.write("objs = state.getArtifactStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case CUSTOMER:");
			writer.write(NEWLINE);
			writer.write("objs = state.getCustomerStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case EMPLOYEE:");
			writer.write(NEWLINE);
			writer.write("objs = state.getEmployeeStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case PROJECT:");
			writer.write(NEWLINE);
			writer.write("objs = state.getProjectStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("case TOOL:");
			writer.write(NEWLINE);
			writer.write("objs = state.getToolStateRepository().getAll();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write("default:");
			writer.write(NEWLINE);
			writer.write("objs = new Vector();");
			writer.write(NEWLINE);
			writer.write("break;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("for(int i=0; i<objs.size(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String filename = getImage(objs.elementAt(i));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("ImageIcon ico = new ImageIcon(ImageLoader.getImageFromURL(filename));");
			writer.write(NEWLINE);
			writer.write("Image scaledImage = ico.getImage().getScaledInstance(35,35, Image.SCALE_AREA_AVERAGING);");
			writer.write(NEWLINE);
			writer.write("ico.setImage(scaledImage);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("objsToImages.put(objs.elementAt(i), ico);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getImage function:
			writer.write("public static String getImage(Object obj)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("String url = \"\";");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			// go through all object types:
			Vector ssObjTypes = objTypes.getAllObjectTypes();
			for(int j=0; j<ssObjTypes.size(); j++)
			{
				SimSEObjectType tempType = (SimSEObjectType)ssObjTypes.elementAt(j);
				if(j > 0) // not on first element
				{
					writer.write("else ");
				}
				writer.write("if(obj instanceof " + getUpperCaseLeading(tempType.getName()) + ")");
				writer.write(NEWLINE);
				writer.write(OPEN_BRACK);
				writer.write(NEWLINE);
				writer.write(getUpperCaseLeading(tempType.getName()) + " p = (" + getUpperCaseLeading(tempType.getName()) + ")obj;");
				writer.write(NEWLINE);

				// go through all of the created objects (and objects created by create objects rules) with matching type and metatype:
				Enumeration createdObjects = objsToImages.keys();
				boolean putElse = false;
				for(int k=0; k<objsToImages.size(); k++)
				{
					SimSEObject obj = (SimSEObject)createdObjects.nextElement();
					if(obj.getName().equals(tempType.getName())) // same type
					{
						boolean allAttValuesInit = true; // whether or not all this object's attribute values are initialized
						Vector atts = obj.getAllAttributes();
						if(atts.size() < obj.getSimSEObjectType().getAllAttributes().size()) // not all atts instantiated
						{
							allAttValuesInit = false;
						}
						else
						{
							for(int m=0; m<atts.size(); m++)
							{
								InstantiatedAttribute att = (InstantiatedAttribute)atts.elementAt(m);
								if(att.isInstantiated() == false) // not instantiated
								{
									allAttValuesInit = false;
									break;
								}
							}
						}
						if(allAttValuesInit)
						{
							if(putElse)
							{
								writer.write("else ");
							}
							else
							{
								putElse = true;
							}
							writer.write("if(p.get" + obj.getKey().getAttribute().getName() + "()");
							if(obj.getKey().getAttribute().getType() == AttributeTypes.STRING) // string att
							{
								writer.write(".equals(\"" + obj.getKey().getValue().toString() + "\"))");
							}
							else // integer, double, or boolean att
							{
								writer.write(" == " + obj.getKey().getValue().toString() + ")");
							}
							writer.write(NEWLINE);
							writer.write(OPEN_BRACK);
							writer.write(NEWLINE);
							if(((objsToImages.get(obj)) != null) && (((String)objsToImages.get(obj)).length() > 0))
							{
								String imagePath = (imageDirURL + ((String)objsToImages.get(obj)));
								writer.write("url = \"" + imagePath + "\";");
								writer.write(NEWLINE);
							}
							writer.write(CLOSED_BRACK);
							writer.write(NEWLINE);
						}
					}
				}
				writer.write(CLOSED_BRACK);
				writer.write(NEWLINE);
			}
			writer.write("return url;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + tabPanelFile.getPath() + ": " + e.toString()), "File IO Error",
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
