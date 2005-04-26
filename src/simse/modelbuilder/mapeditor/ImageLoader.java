// ImageLoader is a utility class that fetches images and returns them given a URL

package simse.modelbuilder.mapeditor;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.*;

public class ImageLoader
{
	
	public static Image getImageFromURL(String url)
	{
		URL imgURL = ImageLoader.class.getResource(url);
		Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
		return(img);
	}
}
