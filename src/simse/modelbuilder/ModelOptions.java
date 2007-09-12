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
  boolean expToolAccessibleDuringGame; // whether or not to make the 
  																		 // explanatory tool accessible during
  																		 // the game
  File iconDirectory; // directory containing icons for this model
  File codeGenerationDestinationDirectory; // directory to generate code into

  public ModelOptions(boolean includeEveryoneStopOption, 
  		boolean expToolAccessibleDuringGame, File iconDirectory, 
  		File codeGenerationDestinationDirectory) {
    this.includeEveryoneStopOption = includeEveryoneStopOption;
    this.expToolAccessibleDuringGame = expToolAccessibleDuringGame;
    this.iconDirectory = iconDirectory;
    this.codeGenerationDestinationDirectory = 
    	codeGenerationDestinationDirectory;
  }

  public ModelOptions() {
    includeEveryoneStopOption = false;
    expToolAccessibleDuringGame = false;
  }

  public boolean getEveryoneStopOption() 
  {
    return includeEveryoneStopOption;
  }

  public void setEveryoneStopOption(boolean everyoneStop) {
    includeEveryoneStopOption = everyoneStop;
  }
  
  public boolean getExplanatoryToolAccessOption() 
  {
    return expToolAccessibleDuringGame;
  }

  public void setExplanatoryToolAccessOption(boolean option) {
    expToolAccessibleDuringGame = option;
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
    expToolAccessibleDuringGame = false;
    iconDirectory = null;
    codeGenerationDestinationDirectory = null;
  }
}