/* This class implements a filer for a JFileChooser that only allows selection of directories (Partially copied from a Java tutorial)*/

package simse.modelbuilder.graphicsbuilder;

import java.io.File;
import javax.swing.filechooser.*;

public class DirectoryFileFilter extends FileFilter
{
	
	// Accepts all directories
	public boolean accept(File f)
	{
		if(f.isDirectory())
		{
			return true;
		}
		return false;
	}
	
	// The description of this filter
	public String getDescription()
	{
		return "Directories";
	}
}
