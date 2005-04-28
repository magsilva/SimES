// ImageLoader is a utility class that fetches images and returns them given a URL

package simse.modelbuilder.mapeditor;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.ImageIcon;

public class ImageLoader
{
	public static Image getImageFromZippedURL(String zipURL, String relativeImageURL){
		try{
			ZipInputStream zis = new ZipInputStream(ImageLoader.class.getResourceAsStream(zipURL));
			while(zis.available() == 1){
				ZipEntry ze = zis.getNextEntry();
				if (ze == null)
					break;
				if (!ze.getName().equals(relativeImageURL))
					continue;
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte buf[] = new byte[2048];
				while(true){
					int len = zis.read(buf, 0, buf.length);
					if(len != -1)
						baos.write(buf, 0, len);
					else{
						zis.close();
						baos.flush();
						baos.close();
						//System.err.println(imgPath);
						return Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image getImageFromURL(String url)
	{
		URL imgURL = ImageLoader.class.getResource(url);
		Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
		return(img);
	}
}
