/*
 * This class is responsible for generating all of the code for the
 * CompositeGraph class in the explanatory tool
 */

package simse.codegenerator.explanatorytoolgenerator;

import simse.codegenerator.CodeGeneratorConstants;

import java.io.*;

import javax.swing.JOptionPane;

public class CompositeGraphGenerator implements CodeGeneratorConstants {
  private File directory; // directory to save generated code into

  public CompositeGraphGenerator(File dir) {
    directory = dir;
  }

  public void generate() {
    File compGraphFile = new File(directory,
        ("simse\\explanatorytool\\CompositeGraph.java"));
    if (compGraphFile.exists()) {
      compGraphFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(compGraphFile);
      writer
          .write("/* File generated by: simse.codegenerator.explanatorytoolgenerator.CompositeGraphGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.explanatorytool;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartMouseEvent;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartMouseListener;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartPanel;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartRenderingInfo;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.JFreeChart;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.axis.NumberAxis;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.CombinedDomainXYPlot;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.PlotOrientation;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.XYPlot;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.title.TextTitle;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.data.Range;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.ui.RectangleEdge;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.ui.RefineryUtilities;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import simse.SimSE;");
      writer.write(NEWLINE);
      writer.write("import simse.state.Clock;");
      writer.write(NEWLINE);
      writer.write("import simse.state.State;");
      writer.write(NEWLINE);
      writer.write("import simse.state.logger.Logger;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import java.awt.Color;");
      writer.write(NEWLINE);
      writer.write("import java.awt.Point;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.ActionEvent;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.ActionListener;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.MouseEvent;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.MouseListener;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.WindowAdapter;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.WindowEvent;");
      writer.write(NEWLINE);
      writer.write("import java.awt.geom.Point2D;");
      writer.write(NEWLINE);
      writer.write("import java.awt.geom.Rectangle2D;");
      writer.write(NEWLINE);
      writer.write("import java.awt.Font;");
      writer.write(NEWLINE);
      writer.write("import java.util.ArrayList;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import javax.swing.JFrame;");
      writer.write(NEWLINE);
      writer.write("import javax.swing.JMenuItem;");
      writer.write(NEWLINE);
      writer.write("import javax.swing.JOptionPane;");
      writer.write(NEWLINE);
      writer.write("import javax.swing.JSeparator;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("public class CompositeGraph extends JFrame implements ChartMouseListener, MouseListener, ActionListener {");

      // member variables:
      writer.write("private ActionGraph actGraph;");
      writer.write(NEWLINE);
      writer.write("private ObjectGraph objGraph;");
      writer.write(NEWLINE);
    	writer.write("private JFreeChart chart;");
    	writer.write(NEWLINE);
    	writer.write("private ChartPanel chartPanel;");
    	writer.write(NEWLINE);
    	writer.write("private int lastRightClickedX;");
    	writer.write(NEWLINE);
    	writer.write("private JMenuItem newBranchItem;");
    	writer.write(NEWLINE);
    	writer.write("private JSeparator separator;");
    	writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer
          .write("public CompositeGraph(ObjectGraph objGraph, ActionGraph actGraph, String branchName) {");
      writer.write(NEWLINE);
  		writer.write("super();");
  		writer.write(NEWLINE);
  		writer.write("String title = \"Composite Graph\";");
  		writer.write(NEWLINE);
  		writer.write("if (branchName != null) {");
  		writer.write(NEWLINE);
  		writer.write("title = title.concat(\" - \" + branchName);");
  		writer.write(NEWLINE);
  		writer.write(CLOSED_BRACK);
  		writer.write(NEWLINE);
  		writer.write("setTitle(title);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("this.actGraph = actGraph;");
      writer.write(NEWLINE);
      writer.write("this.objGraph = objGraph;");
      writer.write(NEWLINE);
  		writer.write("lastRightClickedX = 1;");
  		writer.write(NEWLINE);
  		writer.write("newBranchItem = new JMenuItem(\"Start new branch from here\");");
  		writer.write(NEWLINE);
  		writer.write("newBranchItem.addActionListener(this);");
  		writer.write(NEWLINE);
  		writer.write("separator = new JSeparator();");
  		writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// parent plot:");
      writer.write(NEWLINE);
      writer.write("NumberAxis domainAxis = new NumberAxis(\"Clock Ticks\");");
      writer.write(NEWLINE);
      writer
          .write("domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());");
      writer.write(NEWLINE);
      writer
          .write("CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// add the subplots:");
      writer.write(NEWLINE);
      writer.write("plot.add(objGraph.getXYPlot(), 1);");
      writer.write(NEWLINE);
      writer.write("plot.add(actGraph.getXYPlot(), 1);");
      writer.write(NEWLINE);
      writer.write("plot.setOrientation(PlotOrientation.VERTICAL);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// make a new chart containing the overlaid plot:");
      writer.write(NEWLINE);
      writer
          .write("chart = new JFreeChart(\"Composite (Object/Action) Graph\", JFreeChart.DEFAULT_TITLE_FONT, plot, true);");
      writer.write(NEWLINE);
      writer
          .write("TextTitle subtitle = new TextTitle(objGraph.getChartTitle() + \" and Selected Actions\", new Font(\"SansSerif\", Font.BOLD, 12));");
      writer.write(NEWLINE);
      writer.write("chart.addSubtitle(subtitle);");
      writer.write(NEWLINE);
      writer.write("chart.setBackgroundPaint(Color.white);");
      writer.write(NEWLINE);
      writer
          .write("chartPanel = new ChartPanel(chart, true, true, true, true, true);");
      writer.write(NEWLINE);
      writer.write("chartPanel.addChartMouseListener(this);");
      writer.write(NEWLINE);
      writer.write("chartPanel.addMouseListener(this);");
      writer.write(NEWLINE);
      writer.write("chartPanel.setPreferredSize(new java.awt.Dimension(1000, 540));");
      writer.write(NEWLINE);
      writer.write("setContentPane(chartPanel);");
      writer.write(NEWLINE);
      writer.write("pack();");
      writer.write(NEWLINE);
      writer.write("RefineryUtilities.centerFrameOnScreen(this);");
      writer.write(NEWLINE);
      writer.write("setVisible(true);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "chartMouseClicked" method:
      writer.write("// responds to LEFT mouse clicks on the chart");
      writer.write(NEWLINE);
      writer.write("public void chartMouseClicked(ChartMouseEvent event) {");
      writer.write(NEWLINE);
      writer.write("// send the event to the action graph:");
      writer.write(NEWLINE);
      writer.write("actGraph.chartMouseClicked(event);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "chartMouseMoved" method:
      writer.write("public void chartMouseMoved(ChartMouseEvent event) {}");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "mouseReleased" method:
    	writer.write("// responds to RIGHT-clicks on the chart");
    	writer.write(NEWLINE);
    	writer.write("public void mouseReleased(MouseEvent me) {");
    	writer.write(NEWLINE);
    	writer.write("if (me.getButton() != MouseEvent.BUTTON1) { // not left-click");
    	writer.write(NEWLINE);
    	writer.write("XYPlot plot = chart.getXYPlot();");
    	writer.write(NEWLINE);
    	writer.write("Range domainRange = plot.getDataRange(plot.getDomainAxis());");
    	writer.write(NEWLINE);
    	writer.write("if (domainRange != null) { // chart is not blank");
    	writer.write(NEWLINE);
    	writer.write("Point2D pt = chartPanel.translateScreenToJava2D(new Point(me.getX(), me.getY()));");
    	writer.write(NEWLINE);
    	writer.write("ChartRenderingInfo info = this.chartPanel.getChartRenderingInfo();");
    	writer.write(NEWLINE);
    	writer.write("Rectangle2D dataArea = info.getPlotInfo().getDataArea();");
    	writer.write(NEWLINE);
    	writer.write("NumberAxis domainAxis = (NumberAxis)plot.getDomainAxis();");
    	writer.write(NEWLINE);
    	writer.write("RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();");
    	writer.write(NEWLINE);
    	writer.write("double chartX = domainAxis.java2DToValue(pt.getX(), dataArea, domainAxisEdge);");
    	writer.write(NEWLINE);
    	writer.write("lastRightClickedX = (int)Math.rint(chartX);");
    	writer.write(NEWLINE);
    	writer.write("if (domainRange != null && lastRightClickedX >= domainRange.getLowerBound() && lastRightClickedX <= domainRange.getUpperBound()) { // clicked within domain range");
    	writer.write(NEWLINE);
    	writer.write("if (chartPanel.getPopupMenu().getComponentIndex(newBranchItem) == -1) { // no new branch item on menu currently");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().add(separator);");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().add(newBranchItem);");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().pack();");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().repaint();");
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write("else { // clicked outside of domain range");
    	writer.write(NEWLINE);
    	writer.write("if (chartPanel.getPopupMenu().getComponentIndex(newBranchItem) >= 0) { // new branch item currently on menu");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().remove(newBranchItem);");
    	writer.write(NEWLINE);
    	writer.write("if (chartPanel.getPopupMenu().getComponentIndex(separator) >= 0) { // has separator");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().remove(separator);");
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().pack();");
    	writer.write(NEWLINE);
    	writer.write("chartPanel.getPopupMenu().repaint();");
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
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
    	
    	// other MouseListener methods:
    	writer.write("public void mousePressed(MouseEvent me) {}");
    	writer.write(NEWLINE);
    	writer.write(NEWLINE);
    	writer.write("public void mouseClicked(MouseEvent me) {}");
    	writer.write(NEWLINE);
    	writer.write(NEWLINE);
    	writer.write("public void mouseEntered(MouseEvent me) {}");
    	writer.write(NEWLINE);
    	writer.write(NEWLINE);
    	writer.write("public void mouseExited(MouseEvent me) {}");
    	writer.write(NEWLINE);
    	writer.write(NEWLINE);
    	
    	// "actionPerformed" methods:
    	writer.write("public void actionPerformed(ActionEvent e) {");
    	writer.write(NEWLINE);
    	writer.write("if (e.getSource() == newBranchItem) {");
    	writer.write(NEWLINE);
			writer.write("String newBranchName = JOptionPane.showInputDialog(null, \"Please name this new game:\", \"Name New Game\", JOptionPane.QUESTION_MESSAGE);");
			writer.write(NEWLINE);
			writer.write("if (newBranchName != null) {");
			writer.write(NEWLINE);
			writer.write("State tempState = (State) objGraph.getLog().get(lastRightClickedX).clone();");
			writer.write(NEWLINE);
			writer.write("Logger tempLogger = new Logger(tempState, new ArrayList<State>(objGraph.getLog().subList(0, lastRightClickedX)));");
			writer.write(NEWLINE);
			writer.write("Clock tempClock = new Clock(tempLogger, lastRightClickedX);");
			writer.write(NEWLINE);
			writer.write("tempState.setClock(tempClock);");
			writer.write(NEWLINE);
			writer.write("tempState.setLogger(tempLogger);");
			writer.write(NEWLINE);
			writer.write("SimSE.startNewBranch(tempState);");
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write(NEWLINE);
      
      // "update" method:
    	writer.write("public void update() {");
    	writer.write(NEWLINE);
    	writer.write("actGraph.update();");
    	writer.write(NEWLINE);
    	writer.write("objGraph.update();");
    	writer.write(NEWLINE);
    	writer.write(CLOSED_BRACK);
    	writer.write(NEWLINE);
    	writer.write(NEWLINE);

      // ExitListener class:
      writer.write("public class ExitListener extends WindowAdapter {");
      writer.write(NEWLINE);
      writer.write("public void windowClosing(WindowEvent event) {");
      writer.write(NEWLINE);
      writer.write("setVisible(false);");
      writer.write(NEWLINE);
      writer.write("dispose();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + compGraphFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }
}