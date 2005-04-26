/* This class implements a filer for a JFileChooser so that only *.sme files (SimSe Map Editor) are shown (Partially copied from a Java tutorial)*/

package simse.modelbuilder.mapeditor;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class SMEFileFilter extends FileFilter
{
	// Accepts all directories and all *.sme files.
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
					if (extension.equals("sme"))
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
		return "SimSE MapEditor Files (*.sme)";
	}
}
