/*
 * This class is responsible for generating all of the code for the repositories
 * for the ADTs derived from the SimSEObjectTypes in an .sso file
 */

package simse.codegenerator.stategenerator;

import simse.codegenerator.*;
import simse.modelbuilder.*;
import simse.modelbuilder.objectbuilder.*;
import simse.modelbuilder.actionbuilder.*;

import java.util.*;
import java.io.*;
import javax.swing.*;

public class RepositoryGenerator implements CodeGeneratorConstants {
  private ModelOptions options;
  private DefinedObjectTypes objTypes; // holds all of the defined object types
                                       // from an sso file
  private DefinedActionTypes actTypes; // holds all of the defined action types
                                       // from an ssa file

  public RepositoryGenerator(ModelOptions opts, DefinedObjectTypes dots, 
      DefinedActionTypes dats) {
    options = opts;
    objTypes = dots;
    actTypes = dats;
  }

  public void generate() {
    Vector objs = objTypes.getAllObjectTypes();
    // go through each object and generate a repository for it:
    for (int i = 0; i < objs.size(); i++) {
      generateObjectRepository((SimSEObjectType) objs.elementAt(i));
    }

    // generate meta-object type repositories:
    generateMetaObjectTypeRepository(SimSEObjectTypeTypes
        .getText(SimSEObjectTypeTypes.EMPLOYEE));
    generateMetaObjectTypeRepository(SimSEObjectTypeTypes
        .getText(SimSEObjectTypeTypes.ARTIFACT));
    generateMetaObjectTypeRepository(SimSEObjectTypeTypes
        .getText(SimSEObjectTypeTypes.TOOL));
    generateMetaObjectTypeRepository(SimSEObjectTypeTypes
        .getText(SimSEObjectTypeTypes.CUSTOMER));
    generateMetaObjectTypeRepository(SimSEObjectTypeTypes
        .getText(SimSEObjectTypeTypes.PROJECT));

    // generate action repositories for each action type:
    Vector acts = actTypes.getAllActionTypes();
    for (int i = 0; i < acts.size(); i++) {
      generateActionRepository((ActionType) acts.elementAt(i));
    }

    // generate ActionStateRepository:
    File asrFile = new File(options.getCodeGenerationDestinationDirectory(),
        ("simse\\state\\ActionStateRepository.java"));
    if (asrFile.exists()) {
      asrFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(asrFile);
      writer
          .write("/* File generated by: simse.codegenerator.stategenerator.RepositoryGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.state;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.objects.*;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.actions.*;");
      writer.write(NEWLINE);
      writer.write("import java.util.*;");
      writer.write(NEWLINE);
      writer.write("public class ActionStateRepository implements Cloneable");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);

      // member variables:
      for (int i = 0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType) acts.elementAt(i);
        writer.write(getUpperCaseLeading(tempAct.getName())
            + "ActionStateRepository "
            + tempAct.getName().substring(0, 1).toLowerCase() + i + ";");
        writer.write(NEWLINE);
      }
      writer.write(NEWLINE);

      // constructor:
      writer.write("public ActionStateRepository()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      for (int i = 0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType) acts.elementAt(i);
        writer.write(tempAct.getName().substring(0, 1).toLowerCase() + i
            + " = new " + getUpperCaseLeading(tempAct.getName())
            + "ActionStateRepository();");
        writer.write(NEWLINE);
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "clone" method:
      writer.write("public Object clone() {");
      writer.write(NEWLINE);
      writer.write("try {");
      writer.write(NEWLINE);
      writer.write("ActionStateRepository cl = (ActionStateRepository) (super.clone());");
      writer.write(NEWLINE);
      for (int i = 0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType)acts.elementAt(i);
        writer.write("cl." + tempAct.getName().substring(0, 1).toLowerCase() + i
            + " = (" + getUpperCaseLeading(tempAct.getName()) + "ActionStateRepository)(" +
            tempAct.getName().substring(0, 1).toLowerCase() + i + ".clone());");
        writer.write(NEWLINE);
      }
      writer.write("return cl;");
      writer.write(NEWLINE);
      writer.write("} catch (CloneNotSupportedException c) {");
      writer.write(NEWLINE);
      writer.write("System.out.println(c.getMessage());");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllActions" method:
      writer.write("public Vector getAllActions()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      for (int i = 0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType) acts.elementAt(i);
        writer.write("all.addAll("
            + tempAct.getName().substring(0, 1).toLowerCase() + i
            + ".getAllActions());");
        writer.write(NEWLINE);
      }
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllActions(SSObject) method:
      writer.write("public Vector getAllActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      writer.write("Vector actions = getAllActions();");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<actions.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Action b = (Action)(actions.elementAt(i));");
      writer.write(NEWLINE);
      writer.write("Vector parts = b.getAllParticipants();");
      writer.write(NEWLINE);
      writer.write("for(int j=0; j<parts.size(); j++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(parts.elementAt(j).equals(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("all.add(b);");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllActiveActions(SSObject) method:
      writer.write("public Vector getAllActiveActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      writer.write("Vector actions = getAllActions();");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<actions.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Action b = (Action)(actions.elementAt(i));");
      writer.write(NEWLINE);
      writer.write("Vector parts = b.getAllActiveParticipants();");
      writer.write(NEWLINE);
      writer.write("for(int j=0; j<parts.size(); j++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(parts.elementAt(j).equals(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("all.add(b);");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllInactiveActions(SSObject) method:
      writer.write("public Vector getAllInactiveActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      writer.write("Vector actions = getAllActions();");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<actions.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Action b = (Action)(actions.elementAt(i));");
      writer.write(NEWLINE);
      writer.write("Vector parts = b.getAllInactiveParticipants();");
      writer.write(NEWLINE);
      writer.write("for(int j=0; j<parts.size(); j++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(parts.elementAt(j).equals(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("all.add(b);");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // removeFromAllActions method:
      writer.write("public void removeFromAllActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      // go through all action types:
      for (int i = 0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType) acts.elementAt(i);
        writer.write("Vector " + tempAct.getName().toLowerCase() + "actions = "
            + tempAct.getName().substring(0, 1).toLowerCase() + i
            + ".getAllActions();");
        writer.write(NEWLINE);
        writer.write("for(int i=0; i<" + tempAct.getName().toLowerCase()
            + "actions.size(); i++)");
        writer.write(NEWLINE);
        writer.write(OPEN_BRACK);
        writer.write(NEWLINE);
        writer.write(getUpperCaseLeading(tempAct.getName()) + "Action b = ("
            + getUpperCaseLeading(tempAct.getName()) + "Action)"
            + tempAct.getName().toLowerCase() + "actions.elementAt(i);");
        writer.write(NEWLINE);
        // go through all participants:
        Vector participants = tempAct.getAllParticipants();
        for (int j = 0; j < participants.size(); j++) {
          ActionTypeParticipant part = (ActionTypeParticipant) participants
              .elementAt(j);
          writer.write("if(a instanceof "
              + SimSEObjectTypeTypes.getText(part.getSimSEObjectTypeType())
              + ")");
          writer.write(NEWLINE);
          writer.write(OPEN_BRACK);
          writer.write(NEWLINE);
          writer.write("b.remove" + part.getName() + "(("
              + SimSEObjectTypeTypes.getText(part.getSimSEObjectTypeType())
              + ")a);");
          writer.write(NEWLINE);
          writer.write(CLOSED_BRACK);
          writer.write(NEWLINE);
        }
        writer.write(CLOSED_BRACK);
        writer.write(NEWLINE);
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "get[ActionType]ActionStateRepository" methods:
      for (int i = 0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType) acts.elementAt(i);
        writer.write("public " + getUpperCaseLeading(tempAct.getName())
            + "ActionStateRepository get"
            + getUpperCaseLeading(tempAct.getName())
            + "ActionStateRepository()");
        writer.write(NEWLINE);
        writer.write(OPEN_BRACK);
        writer.write(NEWLINE);
        writer.write("return "
            + tempAct.getName().substring(0, 1).toLowerCase() + i + ";");
        writer.write(NEWLINE);
        writer.write(CLOSED_BRACK);
        writer.write(NEWLINE);
      }
      writer.write(NEWLINE);
      
      // "getActionWithId" method:
      writer.write("public Action getActionWithId(int id) {");
      writer.write(NEWLINE);
      for (int i =0; i < acts.size(); i++) {
        ActionType tempAct = (ActionType)acts.get(i);
        if (i > 0) {
          writer.write("else ");
        }
        writer.write("if (" + tempAct.getName().substring(0, 1).toLowerCase()
            + i + ".getActionWithId(id) != null) {");
        writer.write(NEWLINE);
        writer.write("return " + tempAct.getName().substring(0, 1).toLowerCase()
            + i + ".getActionWithId(id);");
        writer.write(NEWLINE);
        writer.write(CLOSED_BRACK);
        writer.write(NEWLINE);
      }
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      
      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + asrFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private void generateObjectRepository(SimSEObjectType objType) {
    String uCaseName = getUpperCaseLeading(objType.getName());
    String lCaseName = objType.getName().toLowerCase();
    File repFile = new File(options.getCodeGenerationDestinationDirectory(), 
        ("simse\\state\\" + uCaseName + "StateRepository.java"));
    if (repFile.exists()) {
      repFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(repFile);
      writer
          .write("/* File generated by: simse.codegenerator.stategenerator.RepositoryGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.state;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.objects.*;");
      writer.write(NEWLINE);
      writer.write("import java.util.*;");
      writer.write(NEWLINE);
      writer.write("public class " + uCaseName + 
          "StateRepository implements Cloneable");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);

      // member variables:
      writer.write("private Vector " + lCaseName + "s;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer.write("public " + uCaseName + "StateRepository()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write(lCaseName + "s = new Vector();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "clone" method:
      writer.write("public Object clone() {");
      writer.write(NEWLINE);
      writer.write("try {");
      writer.write(NEWLINE);
      writer.write(uCaseName + "StateRepository cl = (" + uCaseName + 
          "StateRepository) super.clone();");
      writer.write(NEWLINE);
      writer.write("Vector cloned" + lCaseName + "s = new Vector();");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < " + lCaseName + "s.size(); i++) {");
      writer.write(NEWLINE);
      writer.write("cloned" + lCaseName + "s.addElement((" + uCaseName + 
          ")(((" + uCaseName + ")(" + lCaseName + 
          "s.elementAt(i))).clone()));");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("cl." + lCaseName + "s = cloned" + lCaseName + "s;");
      writer.write(NEWLINE);
      writer.write("return cl;");
      writer.write(NEWLINE);
      writer.write("} catch (CloneNotSupportedException c) {");
      writer.write(NEWLINE);
      writer.write("System.out.println(c.getMessage());");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "add" method:
      writer.write("public void add(" + getUpperCaseLeading(objType.getName())
          + " a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("boolean add = true;");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<" + objType.getName().toLowerCase()
          + "s.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write(getUpperCaseLeading(objType.getName()) + " "
          + objType.getName().toLowerCase() + " = ("
          + getUpperCaseLeading(objType.getName()) + ")"
          + objType.getName().toLowerCase() + "s.elementAt(i);");
      writer.write(NEWLINE);
      writer.write("if(" + objType.getName().toLowerCase() + ".get"
          + getUpperCaseLeading(objType.getKey().getName()) + "()");
      if (objType.getKey().getType() == AttributeTypes.STRING) // string key
                                                               // attribute
      {
        writer.write(".equals(");
      } else // boolean or numerical key attribute
      {
        writer.write(" == ");
      }
      writer.write("a.get" + getUpperCaseLeading(objType.getKey().getName())
          + "())");
      if (objType.getKey().getType() == AttributeTypes.STRING) {
        // add the extra (
        writer.write(")");
      }
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("add = false;");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("if(add)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write(objType.getName().toLowerCase() + "s.add(a);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "get" method:
      Attribute keyAtt = objType.getKey();
      String attType;
      if (keyAtt.getType() == AttributeTypes.INTEGER) {
        attType = "int";
      } else if (keyAtt.getType() == AttributeTypes.DOUBLE) {
        attType = "double";
      } else if (keyAtt.getType() == AttributeTypes.BOOLEAN) {
        attType = "boolean";
      } else //(keyAtt.getType() == AttributeTypes.STRING)
      {
        attType = "String";
      }
      writer.write("public " + getUpperCaseLeading(objType.getName()) + " get("
          + attType + " " + keyAtt.getName().toLowerCase() + ")");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<" + objType.getName().toLowerCase()
          + "s.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      if (keyAtt.getType() == AttributeTypes.STRING) // string key attribute
      {
        writer.write("if(((" + getUpperCaseLeading(objType.getName()) + ")"
            + objType.getName().toLowerCase() + "s.elementAt(i)).get"
            + getUpperCaseLeading(keyAtt.getName()) + "().equals("
            + keyAtt.getName().toLowerCase() + "))");
      } else // int, double, or boolean key attribute
      {
        writer.write("if(((" + getUpperCaseLeading(objType.getName()) + ")"
            + objType.getName().toLowerCase() + "s.elementAt(i)).get"
            + getUpperCaseLeading(keyAtt.getName()) + "() == "
            + keyAtt.getName().toLowerCase() + ")");
      }
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("return (" + getUpperCaseLeading(objType.getName()) + ")("
          + objType.getName().toLowerCase() + "s.elementAt(i));");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAll" method:
      writer.write("public Vector getAll()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("return " + objType.getName().toLowerCase() + "s;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "remove" method:
      writer.write("public boolean remove("
          + getUpperCaseLeading(objType.getName()) + " a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer
          .write("return " + objType.getName().toLowerCase() + "s.remove(a);");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);

      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + repFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private void generateMetaObjectTypeRepository(String typeName) {
    File repFile = new File(options.getCodeGenerationDestinationDirectory(),
        ("simse\\state\\" + typeName + "StateRepository.java"));
    if (repFile.exists()) {
      repFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(repFile);
      writer
          .write("/* File generated by: simse.codegenerator.stategenerator.RepositoryGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.state;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.objects.*;");
      writer.write(NEWLINE);
      writer.write("import java.util.*;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      writer.write("public class " + typeName + "StateRepository implements Cloneable");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);

      // member variables:
      Vector objs = objTypes.getAllObjectTypesOfType(SimSEObjectTypeTypes
          .getIntRepresentation(typeName));
      for (int i = 0; i < objs.size(); i++) {
        SimSEObjectType tempType = (SimSEObjectType) objs.elementAt(i);
        writer.write(getUpperCaseLeading(tempType.getName())
            + "StateRepository "
            + tempType.getName().substring(0, 1).toLowerCase() + i + ";");
        writer.write(NEWLINE);
      }
      writer.write(NEWLINE);

      // constructor:
      writer.write("public " + typeName + "StateRepository()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      for (int i = 0; i < objs.size(); i++) {
        SimSEObjectType tempType = (SimSEObjectType) objs.elementAt(i);
        writer.write(tempType.getName().substring(0, 1).toLowerCase() + i
            + " = new " + getUpperCaseLeading(tempType.getName())
            + "StateRepository();");
        writer.write(NEWLINE);
      }
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "clone" method:
      writer.write("public Object clone() {");
      writer.write(NEWLINE);
      writer.write("try {");
      writer.write(NEWLINE);
      writer.write(typeName + "StateRepository cl = (" + typeName + 
          "StateRepository) (super.clone());");
      writer.write(NEWLINE);
      for (int i = 0; i < objs.size(); i++) {
        SimSEObjectType tempType = (SimSEObjectType)objs.get(i);
        writer.write("cl." + tempType.getName().substring(0, 1).toLowerCase() 
            + i + " = (" + getUpperCaseLeading(tempType.getName()) + 
            "StateRepository)(" + tempType.getName().substring(0, 1).toLowerCase()
            + i + ".clone());");
        writer.write(NEWLINE);
      }
      writer.write("return cl;");
      writer.write(NEWLINE);
      writer.write("} catch (CloneNotSupportedException c) {");
      writer.write(NEWLINE);
      writer.write("System.out.println(c.getMessage());");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAll" method:
      writer.write("public Vector getAll()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      for (int i = 0; i < objs.size(); i++) {
        SimSEObjectType tempType = (SimSEObjectType) objs.elementAt(i);
        writer.write("all.addAll("
            + tempType.getName().substring(0, 1).toLowerCase() + i
            + ".getAll());");
        writer.write(NEWLINE);
      }
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "get[SimSEObjectType]StateRepository" methods:
      for (int i = 0; i < objs.size(); i++) {
        SimSEObjectType tempType = (SimSEObjectType) objs.elementAt(i);
        writer.write("public " + getUpperCaseLeading(tempType.getName())
            + "StateRepository get" + getUpperCaseLeading(tempType.getName())
            + "StateRepository()");
        writer.write(NEWLINE);
        writer.write(OPEN_BRACK);
        writer.write(NEWLINE);
        writer.write("return "
            + tempType.getName().substring(0, 1).toLowerCase() + i + ";");
        writer.write(NEWLINE);
        writer.write(CLOSED_BRACK);
        writer.write(NEWLINE);
      }
      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + repFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private void generateActionRepository(ActionType actType) {
    String uCaseName = getUpperCaseLeading(actType.getName());
    File repFile = new File(options.getCodeGenerationDestinationDirectory(),
        ("simse\\state\\" + uCaseName + "ActionStateRepository.java"));
    if (repFile.exists()) {
      repFile.delete(); // delete old version of file
    }
    try {
      FileWriter writer = new FileWriter(repFile);
      writer
          .write("/* File generated by: simse.codegenerator.stategenerator.RepositoryGenerator */");
      writer.write(NEWLINE);
      writer.write("package simse.state;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.objects.*;");
      writer.write(NEWLINE);
      writer.write("import simse.adts.actions.*;");
      writer.write(NEWLINE);
      writer.write("import java.util.*;");
      writer.write(NEWLINE);
      writer.write("public class " + uCaseName
          + "ActionStateRepository implements Cloneable");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);

      // member variables:
      writer.write("private Vector actions;");
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // constructor:
      writer.write("public " + uCaseName + "ActionStateRepository()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("actions = new Vector();");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "clone" method:
      writer.write("public Object clone() {");
      writer.write(NEWLINE);
      writer.write("try {");
      writer.write(NEWLINE);
      writer.write(uCaseName + "ActionStateRepository cl = (" + uCaseName +
          "ActionStateRepository) (super.clone());");
      writer.write(NEWLINE);
      writer.write("Vector clonedActions = new Vector();");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < actions.size(); i++) {");
      writer.write(NEWLINE);
      writer.write("clonedActions.add((" + uCaseName + "Action)(((" + uCaseName
          + "Action)(actions.elementAt(i))).clone()));");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("cl.actions = clonedActions;");
      writer.write(NEWLINE);
      writer.write("return cl;");
      writer.write(NEWLINE);
      writer.write("} catch (CloneNotSupportedException c) {");
      writer.write(NEWLINE);
      writer.write("System.out.println(c.getMessage());");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "add" method:
      writer.write("public boolean add("
          + getUpperCaseLeading(actType.getName()) + "Action a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(actions.contains(a) == false)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("actions.add(a);");
      writer.write(NEWLINE);
      writer.write("return true;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return false;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "remove" method:
      writer.write("public boolean remove("
          + getUpperCaseLeading(actType.getName()) + "Action a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(actions.contains(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("actions.remove(a);");
      writer.write(NEWLINE);
      writer.write("return true;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return false;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllActions()" method:
      writer.write("public Vector getAllActions()");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("return actions;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllActions(object)" method:
      writer.write("public Vector getAllActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<actions.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write(getUpperCaseLeading(actType.getName()) + "Action b = ("
          + getUpperCaseLeading(actType.getName())
          + "Action)(actions.elementAt(i));");
      writer.write(NEWLINE);
      writer.write("Vector parts = b.getAllParticipants();");
      writer.write(NEWLINE);
      writer.write("for(int j=0; j<parts.size(); j++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(parts.elementAt(j).equals(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("all.add(b);");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllActiveActions(object)" method:
      writer.write("public Vector getAllActiveActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<actions.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write(getUpperCaseLeading(actType.getName()) + "Action b = ("
          + getUpperCaseLeading(actType.getName())
          + "Action)(actions.elementAt(i));");
      writer.write(NEWLINE);
      writer.write("Vector parts = b.getAllActiveParticipants();");
      writer.write(NEWLINE);
      writer.write("for(int j=0; j<parts.size(); j++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(parts.elementAt(j).equals(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("all.add(b);");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);

      // "getAllInactiveActions(object)" method:
      writer.write("public Vector getAllInactiveActions(SSObject a)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("Vector all = new Vector();");
      writer.write(NEWLINE);
      writer.write("for(int i=0; i<actions.size(); i++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write(getUpperCaseLeading(actType.getName()) + "Action b = ("
          + getUpperCaseLeading(actType.getName())
          + "Action)(actions.elementAt(i));");
      writer.write(NEWLINE);
      writer.write("Vector parts = b.getAllInactiveParticipants();");
      writer.write(NEWLINE);
      writer.write("for(int j=0; j<parts.size(); j++)");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("if(parts.elementAt(j).equals(a))");
      writer.write(NEWLINE);
      writer.write(OPEN_BRACK);
      writer.write(NEWLINE);
      writer.write("all.add(b);");
      writer.write(NEWLINE);
      writer.write("break;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return all;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(NEWLINE);
      
      // "getActionWithId" method:
      writer.write("public " + getUpperCaseLeading(actType.getName()) + 
          "Action getActionWithId(int id) {");
      writer.write(NEWLINE);
      writer.write("for (int i = 0; i < actions.size(); i++) {");
      writer.write(NEWLINE);
      writer.write(getUpperCaseLeading(actType.getName()) + "Action act = (" +
          getUpperCaseLeading(actType.getName()) + "Action)actions.get(i);");
      writer.write(NEWLINE);
      writer.write("if (act.getId() == id) {");
      writer.write(NEWLINE);
      writer.write("return act;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);
      writer.write("return null;");
      writer.write(NEWLINE);
      writer.write(CLOSED_BRACK);
      writer.write(NEWLINE);

      writer.write(CLOSED_BRACK);
      writer.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, ("Error writing file "
          + repFile.getPath() + ": " + e.toString()), "File IO Error",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private String getUpperCaseLeading(String s) {
    return (s.substring(0, 1).toUpperCase() + s.substring(1));
  }
}