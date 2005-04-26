/* This class is responsible for generating all of the code for the Logo panel in the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class LogoPanelGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into
	private String imageURL = "/simse/gui/icons/"; // location of images directory

	public LogoPanelGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File logoPanelFile = new File(directory, ("simse\\gui\\LogoPanel.java"));
		if(logoPanelFile.exists())
		{
			logoPanelFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(logoPanelFile);
			writer.write("package simse.gui;");
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
			writer.write("import javax.swing.border.LineBorder;");
			writer.write(NEWLINE);
			writer.write("public class LogoPanel extends JPanel implements MouseListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private String path = \"" + imageURL + "layout/\";");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private TabPanel tabPane;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JButton artifactButton;");
			writer.write(NEWLINE);
			writer.write("private JButton customerButton;");
			writer.write(NEWLINE);
			writer.write("private JButton employeeButton;");
			writer.write(NEWLINE);
			writer.write("private JButton projectButton;");
			writer.write(NEWLINE);
			writer.write("private JButton toolButton;");
			writer.write(NEWLINE);
			writer.write("private JPanel aboutPanel;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private int selectedTabIndex = -1;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private GridBagLayout gbl;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private ImageIcon[] inactiveButton;");
			writer.write(NEWLINE);
			writer.write("private ImageIcon[] activeButton;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// constructor:
			writer.write("public LogoPanel()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("gbl = new GridBagLayout();");
			writer.write(NEWLINE);
			writer.write("setLayout(gbl);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// loads the tab buttons");
			writer.write(NEWLINE);
			writer.write("createButtonImageSet();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// create the buttons");
			writer.write(NEWLINE);
			writer.write("artifactButton = new JButton(inactiveButton[0]);");
			writer.write(NEWLINE);
			writer.write("artifactButton.setMinimumSize(new Dimension(100,18));");
			writer.write(NEWLINE);
			writer.write("customerButton = new JButton(inactiveButton[1]);");
			writer.write(NEWLINE);
			writer.write("customerButton.setMinimumSize(new Dimension(100,18));");
			writer.write(NEWLINE);
			writer.write("employeeButton = new JButton(inactiveButton[2]);");
			writer.write(NEWLINE);
			writer.write("employeeButton.setMinimumSize(new Dimension(100,18));");
			writer.write(NEWLINE);
			writer.write("projectButton = new JButton(inactiveButton[3]);");
			writer.write(NEWLINE);
			writer.write("projectButton.setMinimumSize(new Dimension(100,18));");
			writer.write(NEWLINE);
			writer.write("toolButton = new JButton(inactiveButton[4]);");
			writer.write(NEWLINE);
			writer.write("toolButton.setMinimumSize(new Dimension(100,18));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// create the layout:");
			writer.write(NEWLINE);
			writer.write("createLayout();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setTabPanel function:
			writer.write("public void setTabPanel(TabPanel tab)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if (tabPane == null)");
			writer.write(NEWLINE);
			writer.write("tabPane = tab;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// createButtonImageSet function:
			writer.write("public void createButtonImageSet()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("inactiveButton = new ImageIcon[5];");
			writer.write(NEWLINE);
			writer.write("activeButton = new ImageIcon[5];");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("inactiveButton[0] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnArtifact.gif\"));");
			writer.write(NEWLINE);
			writer.write("inactiveButton[1] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnCustomer.gif\"));");
			writer.write(NEWLINE);
			writer.write("inactiveButton[2] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnEmployee.gif\"));");
			writer.write(NEWLINE);
			writer.write("inactiveButton[3] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnProject.gif\"));");
			writer.write(NEWLINE);
			writer.write("inactiveButton[4] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnTool.gif\"));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("activeButton[0] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnArtifactClicked.gif\"));");
			writer.write(NEWLINE);
			writer.write("activeButton[1] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnCustomerClicked.gif\"));");
			writer.write(NEWLINE);
			writer.write("activeButton[2] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnEmployeeClicked.gif\"));");
			writer.write(NEWLINE);
			writer.write("activeButton[3] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnProjectClicked.gif\"));");
			writer.write(NEWLINE);
			writer.write("activeButton[4] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnToolClicked.gif\"));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// getSelectedTabIndex function:
			writer.write("public int getSelectedTabIndex()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return selectedTabIndex;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// createLayout function:
			writer.write("public void createLayout()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("aboutPanel = new JPanel();");
			writer.write(NEWLINE);
			writer.write("aboutPanel.setOpaque(false);");
			writer.write(NEWLINE);
			writer.write("aboutPanel.setMinimumSize(new Dimension(188,88));");
			writer.write(NEWLINE);
			writer.write("aboutPanel.setPreferredSize(new Dimension(188,88));");
			writer.write(NEWLINE);
			writer.write("addPanel(aboutPanel,0,0,1,5, 0,1);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// spacers to get the angled tab effect:");
			writer.write(NEWLINE);
			writer.write("JLabel lblCustSpacer1 = new JLabel(\"--\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblEmpSpacer1 = new JLabel (\"--\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblEmpSpacer2 = new JLabel (\"--\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblProjSpacer1 = new JLabel(\"--\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("addButton(artifactButton, 1,0,3,1, 1,0);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("addLabel(lblCustSpacer1,  1,1,1,1, 0,0);");
			writer.write(NEWLINE);
			writer.write("addButton(customerButton, 2,1,2,1, 1,0);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("addLabel(lblEmpSpacer1,   1,2,1,1, 0,0);");
			writer.write(NEWLINE);
			writer.write("addLabel(lblEmpSpacer2,   2,2,1,1, 0,0);");
			writer.write(NEWLINE);
			writer.write("addButton(employeeButton, 3,2,1,1, 1,0);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("addLabel(lblProjSpacer1,  1,3,1,1, 0,0);");
			writer.write(NEWLINE);
			writer.write("addButton(projectButton,  2,3,2,1, 1,0);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("addButton(toolButton,     1,4,3,1, 1,0);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// addLabel function:
			writer.write("public void addLabel(JLabel lb, int x, int y, int rowspan, int colspan, double wx, double wy)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc = new GridBagConstraints(x,y,rowspan,colspan,wx,wy,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(0,0,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lb,gbc);");
			writer.write(NEWLINE);
			writer.write("lb.setForeground( new Color(0,0,0,0) );");
			writer.write(NEWLINE);
			writer.write("add(lb);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// addButton function:
			writer.write("public void addButton(JButton jb, int x, int y, int rowspan, int colspan, double wx, double wy)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc = new GridBagConstraints(x,y,rowspan,colspan,wx,wy,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1,0,1,0),0,0);");
			writer.write(NEWLINE);
			writer.write("jb.setBorder(null);");
			writer.write(NEWLINE);
			writer.write("jb.addMouseListener(this);");
			writer.write(NEWLINE);
			writer.write("jb.setOpaque(false);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(jb, gbc);");
			writer.write(NEWLINE);
			writer.write("add(jb);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// addPanel function:
			writer.write("public void addPanel(JPanel jp, int x, int y, int rowspan, int colspan, double wx, double wy)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc = new GridBagConstraints(x,y,rowspan,colspan,wx,wy,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1,0,1,0),0,0);");
			writer.write(NEWLINE);
			writer.write("jp.setBorder(null);");
			writer.write(NEWLINE);
			writer.write("jp.addMouseListener(this);");
			writer.write(NEWLINE);
			writer.write("jp.setOpaque(false);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(jp, gbc);");
			writer.write(NEWLINE);
			writer.write("add(jp);");
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
			writer.write("Image logo = ImageLoader.getImageFromURL(path + \"simselogo.gif\");");
			writer.write(NEWLINE);
			writer.write("g.setColor(new Color(102,102,102,255));");
			writer.write(NEWLINE);
			writer.write("g.fillRect(0,0,325,100);");
			writer.write(NEWLINE);
			writer.write("g.drawImage(logo,0,0,this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mouseClicked function:
			writer.write("public void mouseClicked(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = me.getSource();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// clear all buttons to default setting:");
			writer.write(NEWLINE);
			writer.write("if (source instanceof JButton)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("artifactButton.setIcon(inactiveButton[0]);");
			writer.write(NEWLINE);
			writer.write("customerButton.setIcon(inactiveButton[1]);");
			writer.write(NEWLINE);
			writer.write("employeeButton.setIcon(inactiveButton[2]);");
			writer.write(NEWLINE);
			writer.write("projectButton.setIcon(inactiveButton[3]);");
			writer.write(NEWLINE);
			writer.write("toolButton.setIcon(inactiveButton[4]);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if (source.equals(artifactButton))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("artifactButton.setIcon(activeButton[0]);");
			writer.write(NEWLINE);
			writer.write("selectedTabIndex = 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if (source.equals(customerButton))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("customerButton.setIcon(activeButton[1]);");
			writer.write(NEWLINE);
			writer.write("selectedTabIndex = 1;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if (source.equals(employeeButton))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("employeeButton.setIcon(activeButton[2]);");
			writer.write(NEWLINE);
			writer.write("selectedTabIndex = 2;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if (source.equals(projectButton))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("projectButton.setIcon(activeButton[3]);");
			writer.write(NEWLINE);
			writer.write("selectedTabIndex = 3;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if (source.equals(toolButton))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("toolButton.setIcon(activeButton[4]);");
			writer.write(NEWLINE);
			writer.write("selectedTabIndex = 4;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if (source.equals(aboutPanel))");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Point p = new Point(300,300);");
			writer.write(NEWLINE);
			writer.write("JFrame f = null;");
			writer.write(NEWLINE);
			writer.write("SimSEAboutDialog about = new SimSEAboutDialog(f);");
			writer.write(NEWLINE);
			writer.write("about.setLocation(p);");
			writer.write(NEWLINE);
			writer.write("about.setVisible(true);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("tabPane.setGUIChanged();");
			writer.write(NEWLINE);
			writer.write("if (tabPane != null)");
			writer.write(NEWLINE);
			writer.write("tabPane.update();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mousePressed function:
			writer.write("public void mousePressed(MouseEvent me){}");
			writer.write(NEWLINE);

			// mouseReleased function:
			writer.write("public void mouseReleased(MouseEvent me){}");
			writer.write(NEWLINE);

			// mouseEntered function:
			writer.write("public void mouseEntered(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = me.getSource();");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("if (source instanceof JButton)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setCursor(new Cursor(Cursor.HAND_CURSOR));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mouseExited function:
			writer.write("public void mouseExited(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setCursor(new Cursor(Cursor.DEFAULT_CURSOR));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + logoPanelFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
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
