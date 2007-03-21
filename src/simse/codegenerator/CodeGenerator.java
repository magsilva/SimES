/* This class is responsible for generating all of the code for the simulation */

package simse.codegenerator;

import simse.codegenerator.enginegenerator.EngineGenerator;
import simse.codegenerator.explanatorytoolgenerator.ExplanatoryToolGenerator;
import simse.codegenerator.guigenerator.GUIGenerator;
import simse.codegenerator.logicgenerator.LogicGenerator;
import simse.codegenerator.stategenerator.StateGenerator;
import simse.codegenerator.utilgenerator.IDGeneratorGenerator;
import simse.modelbuilder.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;
import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.mapeditor.*;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class CodeGenerator {
  private final char NEWLINE = '\n';
  private final char OPEN_BRACK = '{';
  private final char CLOSED_BRACK = '}';

  private ModelOptions options;
  
  private StateGenerator stateGen; // generates the state component
  private LogicGenerator logicGen; // generates the logic component
  private EngineGenerator engineGen; // generates the engine component
  private GUIGenerator guiGen; // generates the GUI component
  private ExplanatoryToolGenerator expToolGen; // generates the explanatory tool
  private IDGeneratorGenerator idGen; // generates the IDGenerator
  public static boolean allowHireFire = true;

  public CodeGenerator(ModelOptions options, DefinedObjectTypes objTypes, 
      CreatedObjects objs, DefinedActionTypes actTypes, Hashtable 
      stsObjsToImages, Hashtable ruleObjsToImages, TileData[][] map,
      ArrayList userDatas) {
    this.options = options;
    stateGen = new StateGenerator(options, objTypes, actTypes);
    logicGen = new LogicGenerator(options, objTypes, actTypes);
    engineGen = new EngineGenerator(options, objs);
    guiGen = new GUIGenerator(options, objTypes, objs, actTypes, 
        stsObjsToImages, ruleObjsToImages, map, userDatas);
    expToolGen = new ExplanatoryToolGenerator(options, objTypes, objs, 
        actTypes);
    idGen = new IDGeneratorGenerator(options);
  }

  public void setAllowHireFire(boolean b) {
    allowHireFire = b;
  }

  public void generate() // causes all of this component's sub-components to
  // generate code
  {
    File codeGenDir = options.getCodeGenerationDestinationDirectory();
    if ((codeGenDir != null) && 
        ((!codeGenDir.exists()) || (!codeGenDir.isDirectory()))) {
      JOptionPane.showMessageDialog(null, ("Cannot find code generation" +
      		" destination directory " + codeGenDir.getAbsolutePath()), 
      		"File Not Found Error", JOptionPane.ERROR_MESSAGE);
    }
    else {
	    // generate directory structure:
	    File simse = new File(options.getCodeGenerationDestinationDirectory(), 
	        "simse");
	    // if directory already exists, delete all files in it:
	    if (simse.exists() && simse.isDirectory()) {
	      File[] files = simse.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    simse.mkdir();
	    
	    File lib = new File(options.getCodeGenerationDestinationDirectory(), 
	        "lib");
	    // if directory already exists, delete all files in it:
	    if (lib.exists() && lib.isDirectory()) {
	      File[] files = lib.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    lib.mkdir();
	
	    File adts = new File(simse, "adts");
	    // if directory already exists, delete all files in it:
	    if (adts.exists() && adts.isDirectory()) {
	      File[] files = adts.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    adts.mkdir();
	
	    File objects = new File(adts, "objects");
	    // if directory already exists, delete all files in it:
	    if (objects.exists() && objects.isDirectory()) {
	      File[] files = objects.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    objects.mkdir();
	
	    File actions = new File(adts, "actions");
	    // if directory already exists, delete all files in it:
	    if (actions.exists() && actions.isDirectory()) {
	      File[] files = actions.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    actions.mkdir();
	
	    File state = new File(simse, "state");
	    // if directory already exists, delete all files in it:
	    if (state.exists() && state.isDirectory()) {
	      File[] files = state.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    state.mkdir();
	
	    File logger = new File(state, "logger");
	    // if directory already exists, delete all files in it:
	    if (logger.exists() && logger.isDirectory()) {
	      File[] files = logger.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    logger.mkdir();
	
	    File logic = new File(simse, "logic");
	    // if directory already exists, delete all files in it:
	    if (logic.exists() && logic.isDirectory()) {
	      File[] files = logic.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    logic.mkdir();
	
	    File dialogs = new File(logic, "dialogs");
	    // if directory already exists, delete all files in it:
	    if (dialogs.exists() && dialogs.isDirectory()) {
	      File[] files = dialogs.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    dialogs.mkdir();
	
	    File engine = new File(simse, "engine");
	    // if directory already exists, delete all files in it:
	    if (engine.exists() && engine.isDirectory()) {
	      File[] files = engine.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    engine.mkdir();
	
	    File gui = new File(simse, "gui");
	    // if directory already exists, delete all files in it:
	    if (gui.exists() && gui.isDirectory()) {
	      File[] files = gui.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    gui.mkdir();
	    
	    File expTool = new File(simse, "explanatorytool");
	    // if directory already exists, delete all files in it:
	    if (expTool.exists() && expTool.isDirectory()) {
	      File[] files = expTool.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    expTool.mkdir();
	
	    File util = new File(simse, "util");
	    // if directory already exists, delete all files in it:
	    if (util.exists() && util.isDirectory()) {
	      File[] files = util.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        files[i].delete();
	      }
	    }
	    util.mkdir();
	
	    // generate main SimSE component:
	    File ssFile = new File(options.getCodeGenerationDestinationDirectory(), 
	        ("simse\\SimSE.java"));
	    if (ssFile.exists()) {
	      ssFile.delete(); // delete old version of file
	    }
	    try {
	      FileWriter writer = new FileWriter(ssFile);
	      writer
	          .write("/* File generated by: simse.codegenerator.CodeGenerator */");
	      writer.write(NEWLINE);
	      writer.write("package simse;");
	      writer.write(NEWLINE);
	      writer.write("import simse.gui.*;");
	      writer.write(NEWLINE);
	      writer.write("import simse.state.*;");
	      writer.write(NEWLINE);
	      writer.write("import simse.logic.*;");
	      writer.write(NEWLINE);
	      writer.write("import simse.engine.*;");
	      writer.write(NEWLINE);
	      writer.write("public class SimSE");
	      writer.write(NEWLINE);
	      writer.write(OPEN_BRACK);
	      writer.write(NEWLINE);
	      
	      // constructor:
	      writer.write("public SimSE(){}");
	      writer.write(NEWLINE);
	      
	      // main method:
	      writer.write("public static void main(String args[])");
	      writer.write(NEWLINE);
	      writer.write(OPEN_BRACK);
	      writer.write(NEWLINE);
	      writer.write("State s = new State();");
	      writer.write(NEWLINE);
	      writer.write("Logic l = new Logic(s);");
	      writer.write(NEWLINE);
	      writer.write("Engine e = new Engine(l, s);");
	      writer.write(NEWLINE);
	      writer.write("SimSEGUI gui = new SimSEGUI(e, s, l);");
	      writer.write(NEWLINE);
	      writer.write("s.getClock().setGUI(gui);");
	      writer.write(NEWLINE);
	      writer.write("gui.setBounds(0,0,1024,744);");
	      writer.write(NEWLINE);
	      writer.write("e.giveGUI(gui);");
	      writer.write(NEWLINE);
	      writer.write("l.getTriggerChecker().update(false, gui);");
	      writer.write(NEWLINE);
	      writer.write(CLOSED_BRACK);
	      writer.write(NEWLINE);
	      writer.write(CLOSED_BRACK);
	      writer.close();
	    } catch (IOException e) {
	      JOptionPane.showMessageDialog(null, ("Error writing file "
	          + ssFile.getPath() + ": " + e.toString()), "File IO Error",
	          JOptionPane.WARNING_MESSAGE);
	    }
	
	    // generate other components:
	    stateGen.generate();
	    boolean logicGenSuccess = logicGen.generate();
	    engineGen.generate();
	    boolean guiGenSuccess = guiGen.generate();
	    expToolGen.generate();
	    idGen.generate();
	    if (logicGenSuccess && guiGenSuccess) {
	      JOptionPane.showMessageDialog(null, "Simulation generated!",
	          "Generation Successful", JOptionPane.INFORMATION_MESSAGE);
	    }
    }
  }
}