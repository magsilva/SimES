/* This class is responsible for generating all of the code for the SimSE About Dialog in the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;

import simse.codegenerator.*;


public class SimSEAboutDialogGenerator implements CodeGeneratorConstants
{
	/*
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	private String imageURL = "/simse/gui/icons/"; // location of images directory
*/

	private File directory; // directory to save generated code into


	public SimSEAboutDialogGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File aboutDialogFile = new File(directory, ("simse\\gui\\SimSEAboutDialog.java"));
		if(aboutDialogFile.exists())
		{
			aboutDialogFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(aboutDialogFile);

			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.*;");
			writer.write(NEWLINE);
			writer.write("import java.awt.event.*;");
			writer.write(NEWLINE);
			writer.write("import java.util.*;");
			writer.write(NEWLINE);
			writer.write("import java.io.*;");
			writer.write(NEWLINE);
			writer.write("import javax.swing.border.*;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public class SimSEAboutDialog extends JDialog implements ActionListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);


			// member variables
			writer.write("GridBagLayout gbl;");
			writer.write(NEWLINE);
			writer.write("JLabel lblIcon;");
			writer.write(NEWLINE);
			writer.write("private JButton btnOK;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private String versionNo = \"v1.0\";");
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// constructor
			writer.write("public SimSEAboutDialog(Frame f)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("super(f,\"About Simse\", true);");
			writer.write(NEWLINE);
			writer.write("setDefaultCloseOperation(2);");
			writer.write(NEWLINE);
			writer.write("setSize(380,350);");
			writer.write(NEWLINE);
			writer.write("buildGUI();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// buildGUI function
			writer.write("public void buildGUI()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc;");
			writer.write(NEWLINE);
			writer.write("Container con = getContentPane();");
			writer.write(NEWLINE);
			writer.write("gbl = new GridBagLayout();");
			writer.write(NEWLINE);
			writer.write("con.setLayout(gbl);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// logo");
			writer.write(NEWLINE);
			writer.write("JPanel top = new JPanel(gbl);");
			writer.write(NEWLINE);
			writer.write("top.setBorder(new LineBorder(Color.WHITE,2));");
			writer.write(NEWLINE);
			writer.write("top.setBackground(new Color(102,102,102,255));");
			writer.write(NEWLINE);
			writer.write("JLabel lblLogo = new JLabel(new ImageIcon(ImageLoader.getImageFromURL(\""+imagesDirectory +"layout/simselogo-about.gif\")));");
			writer.write(NEWLINE);
			writer.write("lblLogo.setBounds(0,0,100,200);");
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,0,1,1,1,0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5,0,5,0), 0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblLogo,gbc);");
			writer.write(NEWLINE);
			writer.write("top.add(lblLogo);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JPanel mid = new JPanel(gbl);");
			writer.write(NEWLINE);
			writer.write("JLabel lblSimse = new JLabel(\"SimSE: \" + versionNo);");
			writer.write(NEWLINE);
			writer.write("JLabel lblDesc = new JLabel(\"An Educational Software Engineering Simulation Environment\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblUrl = new JLabel(\"http://www.ics.uci.edu/~emilyo/SimSE\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblSpacer = new JLabel(\" \");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,0,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblSimse,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblSimse);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,1,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblDesc,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblDesc);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,2,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblUrl,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblUrl);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,3,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblSpacer,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblSpacer);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("JLabel lblTeam = new JLabel(\"Developers:\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblAndre = new JLabel(\"  Andre van der Hoek\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblEmily = new JLabel(\"  Emily Oh Navarro\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblCalvin = new JLabel(\"  Calvin Lee\");");
			writer.write(NEWLINE);
			writer.write("JLabel lblBeverly = new JLabel(\"  Beverly Chan\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,4,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblTeam,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblTeam);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,5,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblAndre,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblAndre);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,6,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblEmily,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblEmily);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,7,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblCalvin,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblCalvin);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,8,1,1,1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,10,0,0),0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(lblBeverly,gbc);");
			writer.write(NEWLINE);
			writer.write("mid.add(lblBeverly);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// add the panels");
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,0,1,1, 1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(top,gbc);");
			writer.write(NEWLINE);
			writer.write("con.add(top);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,1,1,1, 1,0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(mid,gbc);");
			writer.write(NEWLINE);
			writer.write("con.add(mid);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("btnOK = new JButton(\"Close\");");
			writer.write(NEWLINE);
			writer.write("btnOK.addActionListener(this);");
			writer.write(NEWLINE);
			writer.write("gbc = new GridBagConstraints(0,2,1,1, 1,1, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10), 2,0);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(btnOK,gbc);");
			writer.write(NEWLINE);
			writer.write("con.add(btnOK);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// actionPerformed function
			writer.write("public void actionPerformed(ActionEvent event)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = event.getSource();");
			writer.write(NEWLINE);
			writer.write("// handle the okay / cancel button");
			writer.write(NEWLINE);
			writer.write("if (source instanceof JButton)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if (source == btnOK)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + aboutDialogFile.getPath() + ": " + e.toString()), "File IO Error",
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
