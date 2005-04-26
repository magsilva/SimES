/* This class implements a filer for a JFileChooser so that only *.sop files are shown (Partially copied from a Java tutorial)*/

package simse.modelbuilder.graphicsbuilder;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class SOPFileFilter extends FileFilter 
{
	// Accepts all directories and all *.sop files.
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
					if (extension.equals("sop"))
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

  //The description of this filter
  public String getDescription()
	{
  	return "SimSE Graphics Builder Files (*.sop)";
	}
}
