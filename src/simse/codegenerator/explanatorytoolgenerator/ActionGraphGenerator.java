/*
 * This class is responsible for generating all of the code for the ActionGraph
 * class in the explanatory tool
 */

package simse.codegenerator.explanatorytoolgenerator;

import simse.codegenerator.CodeGeneratorConstants;
import simse.modelbuilder.actionbuilder.ActionType;
import simse.modelbuilder.actionbuilder.DefinedActionTypes;

import java.io.*;
import java.util.Vector;

import javax.swing.*;

public class ActionGraphGenerator implements CodeGeneratorConstants {
  private File directory; // directory to save generated code into
  private DefinedActionTypes actTypes;

  public ActionGraphGenerator(DefinedActionTypes actTypes, File dir) {
    this.actTypes = actTypes;
    directory = dir;
  }

  public void generate() {
    File actGraphFile = new File(directory,
        ("simse\\explanatorytool\\ActionGraph.java"));
    if (actGraphFile.exists()) {
      actGraphFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(actGraphFile);
      writer
          .write("/* File generated by: simse.codegenerator.explanatorytoolgenerator.ActionGraphGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.explanatorytool;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import simse.adts.actions.*;");
      writer.write(NEWLINE);
      writer.write("import simse.state.State;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartFactory;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartMouseEvent;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartMouseListener;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartPanel;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.JFreeChart;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.axis.NumberAxis;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.axis.ValueAxis;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.entity.ChartEntity;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.entity.XYItemEntity;");
      writer.write(NEWLINE);
      writer
          .write("import org.jfree.chart.labels.AbstractXYItemLabelGenerator;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.labels.XYToolTipGenerator;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.PlotOrientation;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.XYPlot;");
      writer.write(NEWLINE);
      writer
          .write("import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.data.xy.XYDataItem;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.data.xy.XYDataset;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.data.xy.XYSeries;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.data.xy.XYSeriesCollection;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.ui.RectangleInsets;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.ui.RefineryUtilities;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import java.awt.Color;");
      writer.write(NEWLINE);
      writer.write("import java.util.ArrayList;");
      writer.write(NEWLINE);
      writer.write("import java.util.Enumeration;");
      writer.write(NEWLINE);
      writer.write("import java.util.Hashtable;");
      writer.write(NEWLINE);
      writer.write("import java.util.List;");
      writer.write(NEWLINE);
      writer.write("import java.util.Vector;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import javax.swing.JFrame;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("public class ActionGraph extends JFrame implements ChartMouseListener {");
      writer.write(NEWLINE);

      // member variables:
      writer.write("private State[] log;");
      writer.write(NEWLINE);
      writer.write("private String[] actionNames;");
      writer.write(NEWLINE);
      writer.write("private JFreeChart chart; // chart object");
      writer.write(NEWLINE);
      writer
          .write("private Hashtable series = new Hashtable(); // a Hashtable to map action ids to XYSeries");
      writer.write(NEWLINE);
      writer
          .write("private ArrayList indices = new ArrayList(); // an ArrayList to map action indices to series names");
      writer.write(NEWLINE);
      writer
          .write("private int actionIndex = 1; // counter for used action indices to be used for their y-values");
      writer.write(NEWLINE);

      // generate an index and an indices array list for each type of action:
      Vector actions = actTypes.getAllActionTypes();
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        if (act.isVisibleInExplanatoryTool()) {
          String lCaseName = act.getName().toLowerCase();
          writer
              .write("private int "
                  + lCaseName
                  + "Index = 1; // index to be used for labeling multiple actions of the same type");
          writer.write(NEWLINE);
          writer
              .write("private ArrayList "
                  + lCaseName
                  + "Indices = new ArrayList(); // an ArrayList to map indices for "
                  + act.getName() + " Action labels to action ids");
          writer.write(NEWLINE);
        }
      }
      writer.write(NEWLINE);

      // constructor:
      writer
          .write("public ActionGraph(ArrayList log, String[] actionNames, boolean showChart) {");
      writer.write(NEWLINE);
      writer.write("super(\"Action Graph\");");
      writer.write(NEWLINE);
      writer.write("this.log = new State[log.size()];");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < log.size(); i++) {");
      writer.write(NEWLINE);
      writer.write("this.log[i] = (State) log.get(i);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("this.actionNames = actionNames;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("// add dummy entries for 0 positions (since we don't want to display a series on the 0 line):");
      writer.write(NEWLINE);
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        if (act.isVisibleInExplanatoryTool()) {
          writer.write(act.getName().toLowerCase()
              + "Indices.add(0, new Integer(-1));");
          writer.write(NEWLINE);
        }
      }
      writer.write(NEWLINE);
      writer.write("XYDataset dataset = createDataset();");
      writer.write(NEWLINE);
      writer.write("chart = createChart(dataset);");
      writer.write(NEWLINE);
      writer.write("ChartPanel chartPanel = new ChartPanel(chart);");
      writer.write(NEWLINE);
      writer.write("chartPanel.addChartMouseListener(this);");
      writer.write(NEWLINE);
      writer
          .write("chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));");
      writer.write(NEWLINE);
      writer.write("setContentPane(chartPanel);");
      writer.write(NEWLINE);
      writer.write("pack();");
      writer.write(NEWLINE);
      writer.write("RefineryUtilities.centerFrameOnScreen(this);");
      writer.write(NEWLINE);
      writer.write("setVisible(showChart);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "createDataset" method:
      writer.write("// Creates the dataset for this graph");
      writer.write(NEWLINE);
      writer.write("private XYDataset createDataset() {");
      writer.write(NEWLINE);
      writer.write("// add a dummy entry for index 0:");
      writer.write(NEWLINE);
      writer.write("indices.add(0, \"Action\");");
      writer.write(NEWLINE);
      writer.write("// go through each action:");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < actionNames.length; i++) {");
      writer.write(NEWLINE);

      // go through each action and generate code for it:
      boolean writeElse = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        String uCaseName = getUpperCaseLeading(act.getName());
        String lCaseName = act.getName().toLowerCase();
        if (act.isVisibleInExplanatoryTool()) {
          if (writeElse) {
            writer.write("else ");
          } else {
            writeElse = true;
          }
          writer.write("if (actionNames[i].equals(\"" + uCaseName + "\")) {");
          writer.write(NEWLINE);
          writer.write("// go through the " + uCaseName
              + "ActionStateRepository for each clock tick:");
          writer.write(NEWLINE);
          writer.write("for (int j = 0; j < log.length; j++) {");
          writer.write(NEWLINE);
          writer.write("State state = log[j];");
          writer.write(NEWLINE);
          writer.write("Vector " + lCaseName
              + "Actions = state.getActionStateRepository().get" + uCaseName
              + "ActionStateRepository().getAllActions();");
          writer.write(NEWLINE);
          writer.write(NEWLINE);
          writer.write("// go through each " + uCaseName + "Action:");
          writer.write(NEWLINE);
          writer.write("for (int k = 0; k < " + lCaseName
              + "Actions.size(); k++) {");
          writer.write(NEWLINE);
          writer.write(uCaseName + "Action action = (" + uCaseName + "Action)"
              + lCaseName + "Actions.get(k);");
          writer.write(NEWLINE);
          writer.write(NEWLINE);
          writer.write("// update series:");
          writer.write(NEWLINE);
          writer.write("updateSeries(action, j);");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
          writer.write(NEWLINE);
        }
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// create dataset:");
      writer.write(NEWLINE);
      writer.write("XYSeriesCollection dataset = new XYSeriesCollection();");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// add all series to the dataset:");
      writer.write(NEWLINE);
      writer.write("Enumeration e = series.elements();");
      writer.write(NEWLINE);
      writer.write("while (e.hasMoreElements()) {");
      writer.write(NEWLINE);
      writer.write("dataset.addSeries((XYSeries) e.nextElement());");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return dataset;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "createChart" method:
      writer.write("// Creates the chart for this graph");
      writer.write(NEWLINE);
      writer.write("private JFreeChart createChart(XYDataset dataset) {");
      writer.write(NEWLINE);
      writer.write("// create the chart:");
      writer.write(NEWLINE);
      writer
          .write("JFreeChart chart = ChartFactory.createXYLineChart(\"Action Graph\", \"Clock Ticks\", null, dataset, PlotOrientation.VERTICAL, true, true, false);");
      writer.write(NEWLINE);
      writer.write("chart.setBackgroundPaint(Color.WHITE);");
      writer.write(NEWLINE);
      writer.write("XYPlot plot = (XYPlot) chart.getPlot();");
      writer.write(NEWLINE);
      writer
          .write("plot.getRenderer().setToolTipGenerator(new ActionGraphToolTipGenerator());");
      writer.write(NEWLINE);
      writer.write("plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xCC));");
      writer.write(NEWLINE);
      writer
          .write("plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));");
      writer.write(NEWLINE);
      writer.write("plot.setDomainGridlinePaint(Color.BLACK);");
      writer.write(NEWLINE);
      writer.write("plot.setRangeGridlinePaint(Color.BLACK);");
      writer.write(NEWLINE);
      writer.write("ValueAxis rangeAxis = plot.getRangeAxis();");
      writer.write(NEWLINE);
      writer.write("rangeAxis.setTickLabelsVisible(false);");
      writer.write(NEWLINE);
      writer.write("rangeAxis.setTickMarksVisible(false);");
      writer.write(NEWLINE);
      writer.write("rangeAxis.setAxisLineVisible(false);");
      writer.write(NEWLINE);
      writer
          .write("rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());");
      writer.write(NEWLINE);
      writer
          .write("plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());");
      writer.write(NEWLINE);
      writer
          .write("XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();");
      writer.write(NEWLINE);
      writer.write("renderer.setShapesVisible(true);");
      writer.write(NEWLINE);
      writer.write("renderer.setShapesFilled(true);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("// change the auto tick unit selection to integer units only:");
      writer.write(NEWLINE);
      writer
          .write("NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();");
      writer.write(NEWLINE);
      writer
          .write("domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("return chart;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "updateSeries" method:
      writer
          .write("private void updateSeries(simse.adts.actions.Action action, int clockTick) {");
      writer.write(NEWLINE);
      writer
          .write("// if a series has not been created for this action, create one:");
      writer.write(NEWLINE);
      writer.write("if (!series.containsKey(new Integer(action.getId()))) {");
      writer.write(NEWLINE);
      writer.write("XYSeries newSeries = null;");
      writer.write(NEWLINE);
      writer.write("String newSeriesName = \"\";");
      writer.write(NEWLINE);

      // go through each action and generate code for it:
      boolean writeElse2 = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        String uCaseName = getUpperCaseLeading(act.getName());
        String lCaseName = act.getName().toLowerCase();
        if (act.isVisibleInExplanatoryTool()) {
          if (writeElse2) {
            writer.write("else ");
          } else {
            writeElse2 = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write("newSeriesName = \"" + uCaseName + "Action-\" + "
              + lCaseName + "Index;");
          writer.write(NEWLINE);
          writer.write("newSeries = new XYSeries(newSeriesName);");
          writer.write(NEWLINE);
          writer.write(lCaseName + "Indices.add(" + lCaseName
              + "Index, new Integer(action.getId()));");
          writer.write(NEWLINE);
          writer.write(lCaseName + "Index++;");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
      }

      writer.write("// add the data value to the series:");
      writer.write(NEWLINE);
      writer.write("newSeries.add(clockTick, actionIndex);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// add the series to the Hashtable:");
      writer.write(NEWLINE);
      writer.write("series.put(new Integer(action.getId()), newSeries);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// add the index entry to the ArrayList:");
      writer.write(NEWLINE);
      writer.write("indices.add(actionIndex, newSeriesName);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// update the index for the next new action:");
      writer.write(NEWLINE);
      writer.write("actionIndex++;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("else {");
      writer.write(NEWLINE);
      writer
          .write("XYSeries oldSeries = (XYSeries) series.get(new Integer(action.getId()));");
      writer.write(NEWLINE);
      writer.write("int index = 0;");
      writer.write(NEWLINE);

      // go through each action and generate code for it:
      boolean writeElse3 = false;
      for (int i = 0; i < actions.size(); i++) {
        ActionType act = (ActionType) actions.get(i);
        String uCaseName = getUpperCaseLeading(act.getName());
        String lCaseName = act.getName().toLowerCase();
        if (act.isVisibleInExplanatoryTool()) {
          if (writeElse3) {
            writer.write("else ");
          } else {
            writeElse3 = true;
          }
          writer.write("if (action instanceof " + uCaseName + "Action) {");
          writer.write(NEWLINE);
          writer.write("index = " + lCaseName
              + "Indices.indexOf(new Integer(action.getId()));");
          writer.write(NEWLINE);
          writer.write(NEWLINE);
          writer.write("// add the data value to the series:");
          writer.write(NEWLINE);
          writer.write("oldSeries.add(clockTick, indices.indexOf(\""
              + uCaseName + "Action-\" + index));");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "chartMouseClicked" method:
      writer.write("// responds to mouse clicks on the chart");
      writer.write(NEWLINE);
      writer.write("public void chartMouseClicked(ChartMouseEvent event) {");
      writer.write(NEWLINE);
      writer.write("ChartEntity entity = event.getEntity();");
      writer.write(NEWLINE);
      writer
          .write("if ((entity != null) && (entity instanceof XYItemEntity)) {");
      writer.write(NEWLINE);
      writer.write("XYItemEntity xyEntity = (XYItemEntity) entity;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// get the y-value of the action (action index):");
      writer.write(NEWLINE);
      writer
          .write("int yVal = (int) xyEntity.getDataset().getYValue(xyEntity.getSeriesIndex(), xyEntity.getItem());");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// get the x-value of the action (clock tick):");
      writer.write(NEWLINE);
      writer
          .write("int xVal = (int) xyEntity.getDataset().getXValue(xyEntity.getSeriesIndex(), xyEntity.getItem());");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// get the series name of the action:");
      writer.write(NEWLINE);
      writer.write("String seriesName = (String) indices.get(yVal);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("// get the action id:");
      writer.write(NEWLINE);
      writer.write("int actionId = getIdOfActionWithSeriesName(seriesName);");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("if (actionId > -1) { // valid action");
      writer.write(NEWLINE);
      writer
          .write("Action action = log[xVal].getActionStateRepository().getActionWithId(actionId);");
      writer.write(NEWLINE);
      writer.write("if (action != null) {");
      writer.write("// bring up ActionInfo window:");
      writer.write(NEWLINE);
      writer
          .write("ActionInfoWindow actWindow = new ActionInfoWindow(this, seriesName, action, xVal);");
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

      // "chartMouseMoved" method:
      writer.write("public void chartMouseMoved(ChartMouseEvent event) {}");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getIdOfActionWithSeriesName" method:
      writer
          .write("// returns the id of the action that corresponds to the given series name");
      writer.write(NEWLINE);
      writer
          .write("private int getIdOfActionWithSeriesName(String seriesName) {");
      writer.write(NEWLINE);
      writer.write("Enumeration keys = series.keys();");
      writer.write(NEWLINE);
      writer.write("while (keys.hasMoreElements()) {");
      writer.write(NEWLINE);
      writer.write("Integer id = (Integer) keys.nextElement();");
      writer.write(NEWLINE);
      writer.write("XYSeries xys = (XYSeries) series.get(id);");
      writer.write(NEWLINE);
      writer.write("if (xys.getKey().equals(seriesName)) {");
      writer.write(NEWLINE);
      writer.write("return id.intValue();");
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

      // "getXYPlot" method:
      writer.write("public XYPlot getXYPlot() {");
      writer.write(NEWLINE);
      writer.write("return chart.getXYPlot();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // ActionGraphToolTipGenerator class:
      writer
          .write("public class ActionGraphToolTipGenerator extends AbstractXYItemLabelGenerator implements XYToolTipGenerator {");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("public ActionGraphToolTipGenerator() {");
      writer.write(NEWLINE);
      writer.write("super();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer
          .write("public String generateToolTip(XYDataset dataset, int series, int item) {");
      writer.write(NEWLINE);
      writer
          .write("return new String(dataset.getSeriesKey(series) + \": click for Action info\");");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);

      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + actGraphFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private String getUpperCaseLeading(String s) {
    return (s.substring(0, 1).toUpperCase() + s.substring(1));
  }
}