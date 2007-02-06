/*
 * This class is responsible for generating all of the code for the IDGenerator
 * class
 */

package simse.codegenerator.utilgenerator;

import java.io.*;
import javax.swing.*;
import simse.modelbuilder.ModelOptions;

import simse.codegenerator.CodeGeneratorConstants;

public class IDGeneratorGenerator implements CodeGeneratorConstants {
  private ModelOptions options;

  public IDGeneratorGenerator(ModelOptions options) {
    this.options = options;
  }

  public void generate() {
    File idGenFile = new File(options.getCodeGenerationDestinationDirectory(),
        ("simse\\util\\IDGenerator.java"));
    if (idGenFile.exists()) {
      idGenFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(idGenFile);
      writer
          .write("/* File generated by: simse.codegenerator.utilgenerator.IDGeneratorGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.util;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("import java.util.ArrayList;");
      writer.write(NEWLINE);
      writer.write("import java.util.Random;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("public class IDGenerator {");
      writer.write(NEWLINE);
      writer.write("private static ArrayList usedIDs = new ArrayList();");
      writer.write(NEWLINE);
      writer.write("private static Random ranNumGen = new Random();");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getNextID" method:
      writer.write("public static int getNextID() {");
      writer.write(NEWLINE);
      writer
          .write("Integer int1 = new Integer(Math.abs(ranNumGen.nextInt()));");
      writer.write(NEWLINE);
      writer.write("if(usedIDs.contains(int1)) {");
      writer.write(NEWLINE);
      writer.write("return getNextID();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("else {");
      writer.write(NEWLINE);
      writer.write("usedIDs.add(int1);");
      writer.write(NEWLINE);
      writer.write("return int1.intValue();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);

      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + idGenFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }
}