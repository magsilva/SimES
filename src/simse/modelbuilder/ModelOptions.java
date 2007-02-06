/*
 * This class is the data structure for holding the options definted for a
 * model.
 */

package simse.modelbuilder;

import java.io.File;

public class ModelOptions {
  boolean includeEveryoneStopOption; // whether or not to include an
  																	 // "everyone stop what you're doing"
  	 																 // option on the employees' menus
  File iconDirectory; // directory containing icons for this model
  File codeGenerationDestinationDirectory; // directory to generate code into

  public ModelOptions(boolean everyoneStop, File iconDir, File codeGenDir) {
    includeEveryoneStopOption = everyoneStop;
    iconDirectory = iconDir;
    codeGenerationDestinationDirectory = codeGenDir;
  }

  public ModelOptions() {
    includeEveryoneStopOption = false;
  }

  public boolean getEveryoneStopOption() 
  {
    return includeEveryoneStopOption;
  }

  public void setEveryoneStopOption(boolean everyoneStop) {
    includeEveryoneStopOption = everyoneStop;
  }
  
  public File getIconDirectory() {
    return iconDirectory;
  }
  
  public void setIconDirectory(File f) {
    iconDirectory = f;
  }
  
  public File getCodeGenerationDestinationDirectory() {
    return codeGenerationDestinationDirectory;
  }
  
  public void setCodeGenerationDestinationDirectory(File f) {
    codeGenerationDestinationDirectory = f;
  }
  
  /*
   * Sets all options to default settings
   */
  public void clearAll() {
    includeEveryoneStopOption = false;
    iconDirectory = null;
    codeGenerationDestinationDirectory = null;
  }
}