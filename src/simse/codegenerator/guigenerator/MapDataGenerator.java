/* This class is responsible for generating all of the code for the MapDataGenerator class for the GUI */

package simse.codegenerator.guigenerator;

import java.util.*;
import java.io.*;
import javax.swing.*;


public class MapDataGenerator
{
	private final char NEWLINE = '\n';
	private final char OPEN_BRACK = '{';
	private final char CLOSED_BRACK = '}';
	
	private String imageDir; // location of image directory

	private File directory; // directory to save generated code into

	public MapDataGenerator(File dir)
	{
		directory = dir;
	}


	public void generate()
	{
		File mdFile = new File(directory, ("simse\\gui\\MapData.java"));
		if(mdFile.exists())
		{
			mdFile.delete(); // delete old version of file
		}
		try
		{
			FileWriter writer = new FileWriter(mdFile);
			writer.write("// MapData works as a structure (having only fields).  It contains all the keys for any particular image, as well as");
			writer.write(NEWLINE);
			writer.write("// the Image associated with that key. Currently some keys are not in use i.e. have no image associated with them");
			writer.write(NEWLINE);
			writer.write("package simse.gui;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("import java.awt.Image;");
			writer.write(NEWLINE);
			writer.write("import java.awt.Toolkit;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("public class MapData");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);				

			// member variables:
			writer.write("static int TILE_SIZE = 50;		// size of 1 tile");
			writer.write(NEWLINE);
			writer.write("static int X_MAPSIZE = 16;		// number of tiles along X axis for map");
			writer.write(NEWLINE);
			writer.write("static int Y_MAPSIZE = 10;		// number of tiles along Y axis for map");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TRANSPARENT = -1;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_GRID = 0;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_DARK = -2;");
			writer.write(NEWLINE);
			writer.write("static final int USER_SELECTED = -11;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_FLOOR = -3;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_CHAIRT = 101;    // chair, face north");
			writer.write(NEWLINE);
			writer.write("static final int TILE_CHAIRB = 102;    // face east");
			writer.write(NEWLINE);
			writer.write("static final int TILE_CHAIRL = 103;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_CHAIRR = 104;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_TRASHCANE = 2;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_TRASHCANF = 3;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_COMPUTER = 4;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_TL = 401; // square table");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_TM = 402;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_TR = 403;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_ML = 404;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_MM = 405;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_MR = 406;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_BL = 407;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_BM = 408;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_STABLE_BR = 409;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE1 = 411;   // round table");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE2 = 412;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE3 = 413;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE4 = 414;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE5 = 415;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE6 = 416;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE7 = 417;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE8 = 418;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_RTABLE9 = 419;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_DOOR = 5;        // door");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_T = 601;    // wall");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_B = 602;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_L = 603;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_R = 604;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_TL = 605;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_TR = 606;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_BL = 607;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_WALL_BR = 608;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static final int TILE_DOOR_TO = 611;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_DOOR_TC = 612;");
			writer.write(NEWLINE);	
			writer.write("static final int TILE_DOOR_LO = 613;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_DOOR_LC = 614;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_DOOR_RO = 615;");
			writer.write(NEWLINE);
			writer.write("static final int TILE_DOOR_RC = 616;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static String dir = \"/simse/gui/icons/\";");
			writer.write(NEWLINE);
			writer.write("static Image transparent = ImageLoader.getImageFromURL(dir + \"transparent.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image grid = ImageLoader.getImageFromURL(dir + \"grid.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image floor = ImageLoader.getImageFromURL(dir + \"floor.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image chairT = ImageLoader.getImageFromURL(dir + \"chairT.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image chairB = ImageLoader.getImageFromURL(dir + \"chairB.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image chairL = ImageLoader.getImageFromURL(dir + \"chairL.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image chairR = ImageLoader.getImageFromURL(dir + \"chairR.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image computer = ImageLoader.getImageFromURL(dir + \"computer.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image table = ImageLoader.getImageFromURL(dir + \"table.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image dark = ImageLoader.getImageFromURL(dir + \"dark.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image trashcanE = ImageLoader.getImageFromURL(dir + \"trashcan_e.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image trashcanF = ImageLoader.getImageFromURL(dir + \"trashcan_f.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static Image wallT = ImageLoader.getImageFromURL(dir + \"wall/wall_t.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallB = ImageLoader.getImageFromURL(dir + \"wall/wall_b.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallL = ImageLoader.getImageFromURL(dir + \"wall/wall_l.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallR = ImageLoader.getImageFromURL(dir + \"wall/wall_r.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallTL = ImageLoader.getImageFromURL(dir + \"wall/wall_tl.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallTR = ImageLoader.getImageFromURL(dir + \"wall/wall_tr.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallBL = ImageLoader.getImageFromURL(dir + \"wall/wall_bl.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image wallBR = ImageLoader.getImageFromURL(dir + \"wall/wall_br.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static Image doorTO = ImageLoader.getImageFromURL(dir + \"wall/door_to.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image doorTC = ImageLoader.getImageFromURL(dir + \"wall/door_tc.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image doorLO = ImageLoader.getImageFromURL(dir + \"wall/door_lo.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image doorLC = ImageLoader.getImageFromURL(dir + \"wall/door_lc.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image doorRO = ImageLoader.getImageFromURL(dir + \"wall/door_ro.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image doorRC = ImageLoader.getImageFromURL(dir + \"wall/door_rc.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static Image sTableTL = ImageLoader.getImageFromURL(dir + \"table/sTable_tl.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image sTableTM = ImageLoader.getImageFromURL(dir + \"table/sTable_tm.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image sTableTR = ImageLoader.getImageFromURL(dir + \"table/sTable_tr.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image sTableBL = ImageLoader.getImageFromURL(dir + \"table/sTable_bl.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image sTableBM = ImageLoader.getImageFromURL(dir + \"table/sTable_bm.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image sTableBR = ImageLoader.getImageFromURL(dir + \"table/sTable_br.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static Image speechTL = ImageLoader.getImageFromURL(dir + \"speechTL.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image speechTR = ImageLoader.getImageFromURL(dir + \"speechTR.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image speechBL = ImageLoader.getImageFromURL(dir + \"speechBL.gif\");");
			writer.write(NEWLINE);
			writer.write("static Image speechBR = ImageLoader.getImageFromURL(dir + \"speechBR.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("static Image error = ImageLoader.getImageFromURL(dir + \"error.gif\");");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// getImage(String) function:
			writer.write("public static Image getImage(String file)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("return Toolkit.getDefaultToolkit().getImage(file);");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			
			// getImage(int) function:
			writer.write("public static Image getImage(int key)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("switch(key)");
			writer.write(NEWLINE);
			writer.write(OPEN_BRACK);
			writer.write(NEWLINE);
			writer.write("case TRANSPARENT:");
			writer.write(NEWLINE);
			writer.write("return transparent;");
			writer.write(NEWLINE);
			writer.write("case TILE_GRID:");
			writer.write(NEWLINE);
			writer.write("return grid;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_FLOOR:");
			writer.write(NEWLINE);
			writer.write("return floor;");
			writer.write(NEWLINE);
			writer.write("case TILE_COMPUTER:");
			writer.write(NEWLINE);
			writer.write("return computer;");
			writer.write(NEWLINE);
			writer.write("case TILE_CHAIRT:");
			writer.write(NEWLINE);
			writer.write("return chairT;");
			writer.write(NEWLINE);
			writer.write("case TILE_CHAIRB:");
			writer.write(NEWLINE);
			writer.write("return chairB;");
			writer.write(NEWLINE);
			writer.write("case TILE_CHAIRL:");
			writer.write(NEWLINE);
			writer.write("return chairL;");
			writer.write(NEWLINE);
			writer.write("case TILE_CHAIRR:");
			writer.write(NEWLINE);
			writer.write("return chairR;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_TRASHCANE:");
			writer.write(NEWLINE);
			writer.write("return trashcanE;");
			writer.write(NEWLINE);
			writer.write("case TILE_TRASHCANF:");
			writer.write(NEWLINE);
			writer.write("return trashcanF;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_DOOR_TO:");
			writer.write(NEWLINE);
			writer.write("return doorTO;");
			writer.write(NEWLINE);
			writer.write("case TILE_DOOR_TC:");
			writer.write(NEWLINE);
			writer.write("return doorTC;");
			writer.write(NEWLINE);
			writer.write("case TILE_DOOR_LO:");
			writer.write(NEWLINE);
			writer.write("return doorLO;");
			writer.write(NEWLINE);
			writer.write("case TILE_DOOR_LC:");
			writer.write(NEWLINE);
			writer.write("return doorLC;");
			writer.write(NEWLINE);
			writer.write("case TILE_DOOR_RO:");
			writer.write(NEWLINE);
			writer.write("return doorRO;");
			writer.write(NEWLINE);
			writer.write("case TILE_DOOR_RC:");
			writer.write(NEWLINE);
			writer.write("return doorRC;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_TL:");
			writer.write(NEWLINE);
			writer.write("return wallTL;");
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_T:");
			writer.write(NEWLINE);
			writer.write("return wallT;");
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_TR:");
			writer.write(NEWLINE);
			writer.write("return wallTR;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_L:");
			writer.write(NEWLINE);
			writer.write("return wallL;");
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_R:");
			writer.write(NEWLINE);
			writer.write("return wallR;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_BL:");
			writer.write(NEWLINE);
			writer.write("return wallBL;");
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_B:");
			writer.write(NEWLINE);
			writer.write("return wallB;");
			writer.write(NEWLINE);
			writer.write("case TILE_WALL_BR:");
			writer.write(NEWLINE);
			writer.write("return wallBR;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_STABLE_TL:");
			writer.write(NEWLINE);
			writer.write("return sTableTL;");
			writer.write(NEWLINE);
			writer.write("case TILE_STABLE_TM:");
			writer.write(NEWLINE);
			writer.write("return sTableTM;");
			writer.write(NEWLINE);
			writer.write("case TILE_STABLE_TR:");
			writer.write(NEWLINE);
			writer.write("return sTableTR;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("case TILE_STABLE_BL:");
			writer.write(NEWLINE);
			writer.write("return sTableBL;");
			writer.write(NEWLINE);
			writer.write("case TILE_STABLE_BM:");
			writer.write(NEWLINE);
			writer.write("return sTableBM;");
			writer.write(NEWLINE);
			writer.write("case TILE_STABLE_BR:");
			writer.write(NEWLINE);
			writer.write("return sTableBR;");
			writer.write(NEWLINE);
			writer.write(NEWLINE);
			writer.write("default:");
			writer.write(NEWLINE);
			writer.write("return error;");
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			writer.write(CLOSED_BRACK);
			writer.write(NEWLINE);
			
			writer.write(CLOSED_BRACK);
			writer.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, ("Error writing file " + mdFile.getPath() + ": " + e.toString()), "File IO Error",
				JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private String replaceAll(String s, char replaced, String replacer)
	{
		char[] cArray = s.toCharArray();
		StringBuffer newStr = new StringBuffer();
		for(int i=0; i<cArray.length; i++)
		{
			if(cArray[i] == replaced)
			{
				newStr.append(replacer);
			}
			else
			{
				newStr.append(cArray[i]);
			}
		}
		return newStr.toString();
	}		
}
