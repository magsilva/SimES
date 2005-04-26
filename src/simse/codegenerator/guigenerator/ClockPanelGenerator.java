/* This class is responsible for generating all of the code for the Clock panel in the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class ClockPanelGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';

	private File directory; // directory to save generated code into
	private String imageDirURL = "/simse/gui/icons/"; // location of images directory

	public ClockPanelGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File clockPanelFile = new File(directory, ("simse\\gui\\ClockPanel.java"));
		if(clockPanelFile.exists())
		{
			clockPanelFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(clockPanelFile);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write("import simse.engine.*;");
			writer.write(NEWLINE);
			writer.write("import simse.state.*;");
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
			writer.write("public class ClockPanel extends JPanel implements MouseListener");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);

			// member variables:
			writer.write("private State state;");
			writer.write(NEWLINE);
			writer.write("private Engine engine;");
			writer.write(NEWLINE);
			writer.write("private SimSEGUI gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private int screenX = 300; // x width of the screen");
			writer.write(NEWLINE);
			writer.write("private int screenY = 100; // y width of the screen");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private JTextField txtAdvClock;");
			writer.write(NEWLINE);
			writer.write("private static JLabel btnAdvClock;");
			writer.write(NEWLINE);
			writer.write("private static JLabel btnNextEvent;");
			writer.write(NEWLINE);
			writer.write("private JLabel lblTimeElapsed;");
			writer.write(NEWLINE);
			writer.write("private Image[] timeElapsedDigits;");
			writer.write(NEWLINE);
			writer.write("private static JCheckBox stopCBox;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private boolean cursorDisplayed;");
			writer.write(NEWLINE);
			writer.write("private String cursor;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("private static ImageIcon icoNextEvent = new ImageIcon(ImageLoader.getImageFromURL(\""
				 + imageDirURL + "layout/btnNextEvent.gif\"));");
			writer.write(NEWLINE);
			writer.write("private static ImageIcon icoAdvClock = new ImageIcon(ImageLoader.getImageFromURL(\""
				 + imageDirURL + "layout/btnAdvClock.gif\"));");
			writer.write(NEWLINE);
			writer.write("private static ImageIcon icoStopClock = new ImageIcon(ImageLoader.getImageFromURL(\""
				 + imageDirURL + "layout/btnStopClock.gif\"));");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// constructor:
			writer.write("public ClockPanel(SimSEGUI g, State s, Engine e)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("gui = g;");
			writer.write(NEWLINE);
			writer.write("state = s;");
			writer.write(NEWLINE);
			writer.write("engine = e;");
			writer.write(NEWLINE);
			writer.write("buildGUI();");
			writer.write(NEWLINE);
			writer.write("update();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);

			// buildGUI function
			writer.write("private void buildGUI()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("GridBagLayout gbl = new GridBagLayout();");
			writer.write(NEWLINE);
			writer.write("setLayout(gbl);");
			writer.write(NEWLINE);
			writer.write("GridBagConstraints gbc;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			writer.write("gbc = new GridBagConstraints(0,0,1,1, 1,0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(12,0,0,25), 0,0);");
			writer.write(NEWLINE);
			writer.write("btnNextEvent = new JLabel(icoNextEvent);");
			writer.write(NEWLINE);
			writer.write("btnNextEvent.addMouseListener(this);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(btnNextEvent,gbc);");
			writer.write(NEWLINE);
			writer.write("add(btnNextEvent);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			writer.write("gbc = new GridBagConstraints(0,1,1,1, 1,0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2,0,0,25), 0,0);");
			writer.write(NEWLINE);
			writer.write("btnAdvClock = new JLabel(icoAdvClock);");
			writer.write(NEWLINE);
			writer.write("btnAdvClock.addMouseListener(this);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(btnAdvClock,gbc);");
			writer.write(NEWLINE);
			writer.write("add(btnAdvClock);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			writer.write("gbc = new GridBagConstraints(0,2,1,1, 1,0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(-3,0,0,100),0,0);");
			writer.write(NEWLINE);
			writer.write("stopCBox = new JCheckBox();");
			writer.write(NEWLINE);
			writer.write("stopCBox.setBackground(Color.WHITE);");
			writer.write(NEWLINE);
			writer.write("stopCBox.setOpaque(false);");
			writer.write(NEWLINE);
			writer.write("stopCBox.addMouseListener(this);");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(stopCBox, gbc);");
			writer.write(NEWLINE);
			writer.write("add(stopCBox);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			writer.write("gbc = new GridBagConstraints(0,3,1,1, 1,1, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(-5,0,0,30), 0,0);");
			writer.write(NEWLINE);
			writer.write("txtAdvClock = new JTextField(\"1\");");
			writer.write(NEWLINE);
			writer.write("txtAdvClock.setForeground(Color.DARK_GRAY);");
			writer.write(NEWLINE);
			writer.write("txtAdvClock.setOpaque(false);");
			writer.write(NEWLINE);
			writer.write("txtAdvClock.setPreferredSize(new Dimension(90,18));");
			writer.write(NEWLINE);
			writer.write("txtAdvClock.setBorder(new LineBorder(new Color(0, 0, 0, 0)));");
			writer.write(NEWLINE);
			writer.write("gbl.setConstraints(txtAdvClock,gbc);");
			writer.write(NEWLINE);
			writer.write("add(txtAdvClock);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// resetAdvClockImage function
			writer.write("public static void resetAdvClockImage()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("stopCBox.setEnabled(true);");
			writer.write(NEWLINE);
			writer.write("btnAdvClock.setIcon(icoAdvClock);");
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// setAdvClockImage function
			writer.write("public static void setAdvClockImage()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("btnAdvClock.setIcon(icoStopClock);");
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// mouseClicked function
			writer.write("public void mouseClicked(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("if(state.getClock().isStopped() == false) // clock not stopped");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = me.getSource();");
			writer.write(NEWLINE);
			writer.write("if(source == btnNextEvent)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("stopCBox.setEnabled(false);");
			writer.write(NEWLINE);
			writer.write("nextEvent();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else if(source == btnAdvClock)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write("if(engine.isRunning())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("engine.stop();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("int ticks = 0;");
			writer.write(NEWLINE);
			writer.write("try");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("ticks = Integer.parseInt(txtAdvClock.getText().trim());");
			writer.write(NEWLINE);
			writer.write("if(ticks <= 0) // negative number entered)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JOptionPane.showMessageDialog(null, \"Please enter a positive integer number of clock ticks\", \"Error\", JOptionPane.ERROR_MESSAGE);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("else");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("engine.setSteps(ticks);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("catch(NumberFormatException nfe)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("JOptionPane.showMessageDialog(null, \"Please enter a positive integer number of clock ticks\", \"Error\", JOptionPane.ERROR_MESSAGE);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("if(stopCBox.isEnabled())");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("engine.setStopAtEvents(stopCBox.isSelected());");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mousePressed function:
			writer.write("public void mousePressed(MouseEvent me) {}");
			writer.write(NEWLINE);

			// mouseReleased function:
			writer.write("public void mouseReleased(MouseEvent me) {}");
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// mouseEntered function:
			writer.write("public void mouseEntered(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Object source = me.getSource();");
			writer.write(NEWLINE);
			writer.write("if(source == btnNextEvent || source == btnAdvClock)");
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

			// mouseExited function:
			writer.write("public void mouseExited(MouseEvent me)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("setCursor(new Cursor(Cursor.DEFAULT_CURSOR));");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// paintComponent function:
			writer.write("public void paintComponent(Graphics g)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("Image img = ImageLoader.getImageFromURL(\"" + imageDirURL + "layout/clock.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("g.setColor(Color.DARK_GRAY);");
			writer.write(NEWLINE);
			writer.write("g.fillRect(0,0,242+10,96+20);");
			writer.write(NEWLINE);
			writer.write("g.drawImage(img, 0, 0, this);");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("// draws the Time Elapsed numbers");
			writer.write(NEWLINE);
			writer.write("for (int i = 0; i < timeElapsedDigits.length && i < 9; i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("int blanks = (9 - timeElapsedDigits.length) * 10;");
			writer.write(NEWLINE);
			writer.write("if (blanks < 0)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("blanks = 0;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("int x = 20 + blanks + (i * 10);");
			writer.write(NEWLINE);
			writer.write("g.drawImage(timeElapsedDigits[i],x,52,this);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);


			// nextEvent function:
			writer.write("// jumps to the next event -- the next time someone has an overhead text to say");
			writer.write(NEWLINE);
			writer.write("public void nextEvent()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("engine.setStopAtEvents(true);");
			writer.write(NEWLINE);
			writer.write("engine.setSteps(10000);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// update function:
			writer.write("// used to refresh the clock number displayed next to Elapsed Time");
			writer.write(NEWLINE);
			writer.write("public void update()");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("// offset used so that when the number of digits increase (1, 10, 100, 1000, etc) it shifts accordingly");
			writer.write(NEWLINE);
			writer.write("int clockTicks = state.getClock().getTime();");
			writer.write(NEWLINE);
			writer.write("convert(clockTicks);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);

			// covert function
			writer.write("// converts the time into a series of digit images");
			writer.write(NEWLINE);
			writer.write("private void convert(int time)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("ArrayList list = new ArrayList();");
			writer.write(NEWLINE);
			writer.write("String s = \"\" + time;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("for (int i = 0; i < s.length(); i++)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("int digit = Character.digit(s.charAt(i),10);");
			writer.write(NEWLINE);
			writer.write("Image img = ImageLoader.getImageFromURL(\"" + imageDirURL + "layout/n\" + digit + \".gif\");");
			writer.write(NEWLINE);
			writer.write("list.add(img);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write("timeElapsedDigits = (Image[]) list.toArray(new Image[1]);");
			writer.write(NEWLINE);
			writer.write("repaint();");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);


			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + clockPanelFile.getPath() + ": " + e.toString()), "File IO Error",
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
