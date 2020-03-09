/*
 * This class is responsible for generating all of the code for the Logo panel
 * in the GUI
 */

package simse.codegenerator.guigenerator;

import simse.codegenerator.CodeGeneratorConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;;

public class LogoPanelGenerator implements CodeGeneratorConstants {
  private File directory; // directory to save generated code into

  public LogoPanelGenerator(File directory) {
    this.directory = directory;
  }

  public void generate() {
    File logoPanelFile = new File(directory, ("simse" + File.separator + "gui" + File.separator + "LogoPanel.java"));
    if (logoPanelFile.exists()) {
      logoPanelFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(logoPanelFile);
      writer
          .write("/* File generated by: simse.codegenerator.guigenerator.LogoPanelGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.gui;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.*;");
      writer.write(NEWLINE);
      writer.write("import java.awt.*;");
      writer.write(NEWLINE);
      writer.write("import java.awt.Dimension;");
      writer.write(NEWLINE);
      writer.write("import javax.swing.*;");
      writer.write(NEWLINE);
      writer.write("import java.awt.Color;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import simse.SimSE;");
      writer.write(NEWLINE);
      writer.write("import simse.engine.*;");
      writer.write(NEWLINE);
      writer.write("import simse.state.*;");
      writer.write(NEWLINE);
      writer.write("import simse.logic.*;");
      writer.write(NEWLINE);

      writer
          .write("public class LogoPanel extends JPanel implements MouseListener");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);

      // member variables:
      writer.write("private String path = \"" + imagesDirectory + "layout/\";");
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
      writer.write("private JButton aboutButton;");
      writer.write(NEWLINE);
      writer.write("private JButton infoButton;");
      writer.write(NEWLINE);
      writer.write("private JButton resetButton;");
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
      writer.write("private SimSEGUI gui;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer.write("public LogoPanel(SimSEGUI g)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("gui = g;");
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
      writer.write("artifactButton.setMinimumSize(new Dimension(120,16));");
      writer.write(NEWLINE);
      writer.write("customerButton = new JButton(inactiveButton[1]);");
      writer.write(NEWLINE);
      writer.write("customerButton.setMinimumSize(new Dimension(120,16));");
      writer.write(NEWLINE);
      writer.write("employeeButton = new JButton(inactiveButton[2]);");
      writer.write(NEWLINE);
      writer.write("employeeButton.setMinimumSize(new Dimension(120,16));");
      writer.write(NEWLINE);
      writer.write("projectButton = new JButton(inactiveButton[3]);");
      writer.write(NEWLINE);
      writer.write("projectButton.setMinimumSize(new Dimension(120,16));");
      writer.write(NEWLINE);
      writer.write("toolButton = new JButton(inactiveButton[4]);");
      writer.write(NEWLINE);
      writer.write("toolButton.setMinimumSize(new Dimension(120,16));");
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
      writer
          .write("inactiveButton[0] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnArtifact.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("inactiveButton[1] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnCustomer.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("inactiveButton[2] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnEmployee.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("inactiveButton[3] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnProject.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("inactiveButton[4] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnTool.gif\"));");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("activeButton[0] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnArtifactClicked.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("activeButton[1] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnCustomerClicked.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("activeButton[2] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnEmployeeClicked.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("activeButton[3] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnProjectClicked.gif\"));");
      writer.write(NEWLINE);
      writer
          .write("activeButton[4] = new ImageIcon(ImageLoader.getImageFromURL(path + \"btnToolClicked.gif\"));");
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

      writer.write("JPanel pnl = new JPanel(gbl);");
      writer.write(NEWLINE);
      writer.write("pnl.setOpaque(false);");
      writer.write(NEWLINE);
      writer.write("pnl.setBackground(new Color(0,0,0,0));");
      writer.write(NEWLINE);
      writer
          .write("GridBagConstraints gbc = new GridBagConstraints(0,0,1,5,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);");
      writer.write(NEWLINE);
      writer.write("gbl.setConstraints(pnl,gbc);");
      writer.write(NEWLINE);
      writer.write("add(pnl);");
      writer.write(NEWLINE);

      writer.write("infoButton = new JButton();");
      writer.write(NEWLINE);
      writer.write("infoButton.setBackground(new Color(0,0,0,0));");
      writer.write(NEWLINE);
      writer.write("infoButton.setOpaque(false);");
      writer.write(NEWLINE);
      writer.write("infoButton.setMinimumSize(new Dimension(24,40));");
      writer.write(NEWLINE);
      writer.write("infoButton.setPreferredSize(new Dimension(24,40));");
      writer.write(NEWLINE);
      writer.write("infoButton.setBorder(null);");
      writer.write(NEWLINE);
      writer.write("infoButton.addMouseListener(this);");
      writer.write(NEWLINE);
      writer
          .write("gbc = new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,5,0),0,0);");
      writer.write(NEWLINE);
      writer.write("gbl.setConstraints(infoButton,gbc);");
      writer.write(NEWLINE);
      writer.write("pnl.add(infoButton);");
      writer.write(NEWLINE);

      writer.write("resetButton = new JButton();");
      writer.write(NEWLINE);
      writer.write("resetButton.setBackground(new Color(0,0,0,0));");
      writer.write(NEWLINE);
      writer.write("resetButton.setOpaque(false);");
      writer.write(NEWLINE);
      writer.write("resetButton.setMinimumSize(new Dimension(24,40));");
      writer.write(NEWLINE);
      writer.write("resetButton.setPreferredSize(new Dimension(24,40));");
      writer.write(NEWLINE);
      writer.write("resetButton.setBorder(null);");
      writer.write(NEWLINE);
      writer.write("resetButton.addMouseListener(this);");
      writer.write(NEWLINE);
      writer
          .write("gbc = new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(5,0,0,0),0,0);");
      writer.write(NEWLINE);
      writer.write("gbl.setConstraints(resetButton,gbc);");
      writer.write(NEWLINE);
      writer.write("pnl.add(resetButton);");
      writer.write(NEWLINE);

      writer.write("aboutButton = new JButton();");
      writer.write(NEWLINE);
      writer.write("aboutButton.setBackground(new Color(0,0,0,0));");
      writer.write(NEWLINE);
      writer.write("aboutButton.setOpaque(false);");
      writer.write(NEWLINE);
      writer.write("aboutButton.setMinimumSize(new Dimension(170,88));");
      writer.write(NEWLINE);
      writer.write("aboutButton.setPreferredSize(new Dimension(170,88));");
      writer.write(NEWLINE);
      writer.write("addButton(aboutButton,1,0,1,5, 0,1);");
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
      writer.write("addButton(artifactButton, 2,0,3,1, 1,0);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("addLabel(lblCustSpacer1,  2,1,1,1, 0,0);");
      writer.write(NEWLINE);
      writer.write("addButton(customerButton, 3,1,2,1, 1,0);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("addLabel(lblEmpSpacer1,   2,2,1,1, 0,0);");
      writer.write(NEWLINE);
      writer.write("addLabel(lblEmpSpacer2,   3,2,1,1, 0,0);");
      writer.write(NEWLINE);
      writer.write("addButton(employeeButton, 4,2,1,1, 1,0);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("addLabel(lblProjSpacer1,  2,3,1,1, 0,0);");
      writer.write(NEWLINE);
      writer.write("addButton(projectButton,  3,3,2,1, 1,0);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("addButton(toolButton,     2,4,3,1, 1,1);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // addLabel function:
      writer
          .write("public void addLabel(JLabel lb, int x, int y, int rowspan, int colspan, double wx, double wy)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer
          .write("GridBagConstraints gbc = new GridBagConstraints(x,y,rowspan,colspan,wx,wy,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(0,0,0,0),0,0);");
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
      writer
          .write("public void addButton(JButton jb, int x, int y, int rowspan, int colspan, double wx, double wy)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer
          .write("GridBagConstraints gbc = new GridBagConstraints(x,y,rowspan,colspan,wx,wy,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1,1,1,1),0,0);");
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

      // paintComponent function:
      writer.write("public void paintComponent(Graphics g)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer
          .write("Image logo = ImageLoader.getImageFromURL(path + \"simselogo.gif\");");
      writer.write(NEWLINE);
      writer.write("g.setColor(new Color(102,102,102,255));");
      writer.write(NEWLINE);
      writer.write("g.fillRect(0,0,340,100);");
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
      writer.write("if (source.equals(aboutButton))");
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
      writer.write("else if (source.equals(infoButton))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer
          .write("new StartingNarrativeDialog(gui);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("else if (source.equals(resetButton))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer
          .write("int response = JOptionPane.showConfirmDialog(gui,\"Are You Sure You Want To Reset?\",\"Reset Game?\",JOptionPane.YES_NO_OPTION);");
      writer.write(NEWLINE);
      writer.write("if (response == JOptionPane.OK_OPTION)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
			writer.write("// reset:");
			writer.write(NEWLINE);
			writer.write("if (gui.getEngine().getTimer() != null) {");
			writer.write(NEWLINE);
			writer.write("gui.getEngine().getTimer().stop();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("gui.close();");
			writer.write(NEWLINE);
			writer.write("gui.dispose();");
			writer.write(NEWLINE);
			writer.write("SimSE.main(new String[]{});");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("else");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
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
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + logoPanelFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }
}