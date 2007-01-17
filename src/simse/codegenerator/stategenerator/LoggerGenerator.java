/*
 * This class is responsible for generating all of the code for the state's
 * logger component
 */

package simse.codegenerator.stategenerator;

import java.io.*;

import javax.swing.*;

import simse.codegenerator.*;

public class LoggerGenerator implements CodeGeneratorConstants {
  private File directory; // directory to generate into

  public LoggerGenerator(File dir) {
    directory = dir;
  }

  public void generate() {
    File loggerFile = new File(directory, ("simse\\state\\Logger\\Logger.java"));
    if (loggerFile.exists()) {
      loggerFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(loggerFile);
      writer
          .write("/* File generated by: simse.codegenerator.stategenerator.LoggerGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.state.logger;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import simse.state.*;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import java.util.ArrayList;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("public class Logger {");
      writer.write(NEWLINE);

      // member variables:
      writer.write("private State state; // current state");
      writer.write(NEWLINE);
      writer.write("private ArrayList log; // an array list of states for the current simulation");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer.write("public Logger(State state) {");
      writer.write(NEWLINE);
      writer.write("this.state = state;");
      writer.write(NEWLINE);
      writer.write("log = new ArrayList();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getLogFile" method:
      writer.write("// returns the state log");
      writer.write(NEWLINE);
      writer.write("public ArrayList getLog() {");
      writer.write(NEWLINE);
      writer.write("return log;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "update" method:
      writer
          .write("// updates the log with the current state");
      writer.write(NEWLINE);
      writer.write("public void update() {");
      writer.write(NEWLINE);
      writer.write("log.add((State)state.clone());");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);

      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + loggerFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }
}