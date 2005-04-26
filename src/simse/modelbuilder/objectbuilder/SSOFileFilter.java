/* This class implements a filer for a JFileChooser so that only *.sso files are shown (Partially copied from a Java tutorial)*/

package simse.modelbuilder.objectbuilder;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class SSOFileFilter extends FileFilter 
{
	// Accepts all directories and all *.sso files.
  public boolean accept(File f) 
	{
		String fileName = f.getName();
		if (f.isDirectory())
		{
    	return true;
    }
		else
		{	
			if(fileName.length() >= 5)
			{
				String extension = fileName.substring(fileName.length() - 3);
		    if (extension != null)
				{
					if (extension.equals("sso"))
					{
		      	return true;
					}
					else
					{
		      	return false;
					}
				}
		    return false;
			}
			return false;
		}
	}

  // The description of this filter
  public String getDescription()
	{
  	return "SimSE Object Files (*.sso)";
	}
}
