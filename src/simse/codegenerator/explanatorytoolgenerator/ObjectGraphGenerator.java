/*
 * This class is responsible for generating all of the code for the ObjectGraph
 * class in the explanatory tool
 */

package simse.codegenerator.explanatorytoolgenerator;

import simse.codegenerator.CodeGeneratorConstants;
import simse.modelbuilder.objectbuilder.Attribute;
import simse.modelbuilder.objectbuilder.AttributeTypes;
import simse.modelbuilder.objectbuilder.NumericalAttribute;
import simse.modelbuilder.objectbuilder.DefinedObjectTypes;
import simse.modelbuilder.objectbuilder.SimSEObjectType;
import simse.modelbuilder.objectbuilder.SimSEObjectTypeTypes;
import simse.modelbuilder.startstatebuilder.CreatedObjects;
import simse.modelbuilder.startstatebuilder.SimSEObject;

import java.io.*;
import java.util.Vector;

import javax.swing.*;

public class ObjectGraphGenerator implements CodeGeneratorConstants {
  private File directory; // directory to save generated code into
  private DefinedObjectTypes objTypes;
  private CreatedObjects objects;

  public ObjectGraphGenerator(DefinedObjectTypes objTypes, CreatedObjects objs,
      File dir) {
    this.objTypes = objTypes;
    this.objects = objs;
    directory = dir;
  }

  public void generate() {
    File objGraphFile = new File(directory,
        ("simse\\explanatorytool\\ObjectGraph.java"));
    if (objGraphFile.exists()) {
      objGraphFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(objGraphFile);
      writer
          .write("/* File generated by: simse.codegenerator.explanatorytoolgenerator.ObjectGraphGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.explanatorytool;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import simse.adts.objects.*;");
      writer.write(NEWLINE);
      writer.write("import simse.state.*;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartFactory;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.ChartPanel;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.JFreeChart;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.axis.NumberAxis;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.PlotOrientation;");
      writer.write(NEWLINE);
      writer.write("import org.jfree.chart.plot.XYPlot;");
      writer.write(NEWLINE);
      writer
          .write("import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;");
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
      writer.write("import java.awt.event.WindowAdapter;");
      writer.write(NEWLINE);
      writer.write("import java.awt.event.WindowEvent;");
      writer.write(NEWLINE);
      writer.write("import java.util.ArrayList;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import javax.swing.JFrame;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("public class ObjectGraph extends JFrame {");
      writer.write(NEWLINE);

      // member variables:
      writer.write("private State[] log;");
      writer.write(NEWLINE);
      writer.write("private String objTypeType;");
      writer.write(NEWLINE);
      writer.write("private String objType;");
      writer.write(NEWLINE);
      writer.write("private String keyAttVal;");
      writer.write(NEWLINE);
      writer.write("private String[] attributes;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer
          .write("public ObjectGraph(String title, ArrayList log, String objTypeType, String objType, String keyAttVal, String[] attributes) {");
      writer.write(NEWLINE);
      writer.write("super(title);");
      writer.write(NEWLINE);
      writer.write("this.log = new State[log.size()];");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < log.size(); i++) {");
      writer.write(NEWLINE);
      writer.write("this.log[i] = (State)log.get(i);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("this.objTypeType = objTypeType;");
      writer.write(NEWLINE);
      writer.write("this.objType = objType;");
      writer.write(NEWLINE);
      writer.write("this.keyAttVal = keyAttVal;");
      writer.write(NEWLINE);
      writer.write("this.attributes = attributes;");
      writer.write(NEWLINE);
      writer.write("XYDataset dataset = createDataset();");
      writer.write(NEWLINE);
      writer.write("JFreeChart chart = createChart(dataset);");
      writer.write(NEWLINE);
      writer.write("ChartPanel chartPanel = new ChartPanel(chart);");
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
      writer.write("setVisible(true);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "createDataset" method:
      writer.write("// Creates the dataset for this graph");
      writer.write(NEWLINE);
      writer.write("private XYDataset createDataset() {");
      writer.write(NEWLINE);
      writer.write("XYSeries[] series = new XYSeries[attributes.length];");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < attributes.length; i++) {");
      writer.write(NEWLINE);
      writer.write("series[i] = new XYSeries(attributes[i]);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < log.length; i++) {");
      writer.write(NEWLINE);
      writer.write("for (int j = 0; j < attributes.length; j++) {");
      writer.write(NEWLINE);

      // go through all object types and generate code for them:
      Vector types = objTypes.getAllObjectTypes();
      for (int i = 0; i < types.size(); i++) {
        SimSEObjectType type = (SimSEObjectType) types.get(i);
        String uCaseName = getUpperCaseLeading(type.getName());
        String lCaseName = type.getName().toLowerCase();
        if (i > 0) {
          writer.write("else ");
        }
        writer.write("if (objTypeType.equals(\""
            + SimSEObjectTypeTypes.getText(type.getType())
            + "\") && objType.equals(\"" + uCaseName + "\")) {");
        writer.write(NEWLINE);
        writer.write(uCaseName + " " + lCaseName + " = null;");
        writer.write(NEWLINE);

        // go through each created object of that type and generate code for it:
        Vector objsOfType = objects.getAllObjectsOfType(type);
        for (int j = 0; j < objsOfType.size(); j++) {
          SimSEObject obj = (SimSEObject) objsOfType.get(j);
          if (j > 0) {
            writer.write("else ");
          }
          writer.write("if (keyAttVal.equals(\""
              + obj.getKey().getValue().toString() + "\")) {");
          writer.write(NEWLINE);
          writer.write(lCaseName + " = log[i].get"
              + SimSEObjectTypeTypes.getText(type.getType())
              + "StateRepository().get" + uCaseName + "StateRepository().get(");
          if (obj.getKey().getAttribute().getType() == AttributeTypes.STRING) { // String
            // attribute
            writer.write("\"" + obj.getKey().getValue().toString() + "\");");
          } else { // non-String attribute
            writer.write(obj.getKey().getValue().toString() + ");");
          }
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
        writer.write("if (" + lCaseName + " != null) {");
        writer.write(NEWLINE);

        // go through each attribute for this type and generate code for it:
        Vector atts = type.getAllAttributes();
        boolean writeElse = false;
        for (int j = 0; j < atts.size(); j++) {
          Attribute att = (Attribute) atts.get(j);
          if ((att instanceof NumericalAttribute)
              && (att.isVisible() || att.isVisibleOnCompletion())) {
            if (writeElse) {
              writer.write("else ");
            }
            writer.write("if (attributes[j].equals(\""
                + getUpperCaseLeading(att.getName()) + "\")) {");
            writer.write(NEWLINE);
            writer.write("series[j].add(i, " + lCaseName + ".get"
                + getUpperCaseLeading(att.getName()) + "());");
            writer.write(NEWLINE);
            writer.write(CLOSED_BRACK);
            writer.write(NEWLINE);
            writeElse = true;
          }
        }
        writer.write(CLOSED_BRACK);
        writer.write(NEWLINE);
        writer.write(CLOSED_BRACK);
        writer.write(NEWLINE);
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("XYSeriesCollection dataset = new XYSeriesCollection();");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < series.length; i++) {");
      writer.write(NEWLINE);
      writer.write("dataset.addSeries(series[i]);");
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
          .write("JFreeChart chart = ChartFactory.createXYLineChart((objType + \" \" + objTypeType + \" \" + keyAttVal + \" \" + \"Attributes\"), \"Clock Ticks\", null, dataset, PlotOrientation.VERTICAL, true, true, false);");
      writer.write(NEWLINE);
      writer.write("chart.setBackgroundPaint(Color.white);");
      writer.write(NEWLINE);
      writer.write("XYPlot plot = (XYPlot) chart.getPlot();");
      writer.write(NEWLINE);
      writer.write("plot.setBackgroundPaint(Color.lightGray);");
      writer.write(NEWLINE);
      writer
          .write("plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));");
      writer.write(NEWLINE);
      writer.write("plot.setDomainGridlinePaint(Color.white);");
      writer.write(NEWLINE);
      writer.write("plot.setRangeGridlinePaint(Color.white);");
      writer.write(NEWLINE);
      writer
          .write("XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();");
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
          + objGraphFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private String getUpperCaseLeading(String s) {
    return (s.substring(0, 1).toUpperCase() + s.substring(1));
  }
}