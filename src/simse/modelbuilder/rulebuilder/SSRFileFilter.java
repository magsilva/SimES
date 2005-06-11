/* This class implements a filer for a JFileChooser so that only *.ssr files are shown (Partially copied from a Java tutorial)*/

package simse.modelbuilder.rulebuilder;

import java.io.File;
import javax.swing.filechooser.*;

public class SSRFileFilter extends FileFilter 
{
	//Accept all directories and all *.ssr files.
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
					if (extension.equals("ssr"))
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
  	return "SimSE Rule Files (*.ssr)";
	}
}
