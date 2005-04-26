/*
MapData works as a structure (having only fields).  It contains all the keys for any
particular image, as well as the Image associated with that key.

currently some keys are not in use i.e. have no image associated with them
*/

package simse.modelbuilder.mapeditor;

import java.awt.Image;
import java.awt.Toolkit;

public class MapData
{
	static int TILE_SIZE = 50;		// size of 1 tile
	public static int X_MAPSIZE = 16;		// number of tiles along X axis for map
	public static int Y_MAPSIZE = 10;		// number of tiles along Y axis for map

	static final int TRANSPARENT = -1;
	static final int TILE_GRID = 0;
	static final int TILE_DARK = -2;
	static final int USER_SELECTED = -11;
	static final int TILE_FLOOR = -3;

	static final int TILE_CHAIRT = 101;    // chair, face north
	static final int TILE_CHAIRB = 102;    // face east
	static final int TILE_CHAIRL = 103;
	static final int TILE_CHAIRR = 104;

	static final int TILE_TRASHCANE = 2;
	static final int TILE_TRASHCANF = 3;

	static final int TILE_COMPUTER = 4;
	static final int TILE_PAPERS = 5;

	static final int TILE_STABLE_TL = 401; // square table
	static final int TILE_STABLE_TM = 402;
	static final int TILE_STABLE_TR = 403;
	static final int TILE_STABLE_ML = 404;
	static final int TILE_STABLE_MM = 405;
	static final int TILE_STABLE_MR = 406;
	static final int TILE_STABLE_BL = 407;
	static final int TILE_STABLE_BM = 408;
	static final int TILE_STABLE_BR = 409;

	static final int TILE_RTABLE1 = 411;   // round table
	static final int TILE_RTABLE2 = 412;
	static final int TILE_RTABLE3 = 413;
	static final int TILE_RTABLE4 = 414;
	static final int TILE_RTABLE5 = 415;
	static final int TILE_RTABLE6 = 416;
	static final int TILE_RTABLE7 = 417;
	static final int TILE_RTABLE8 = 418;
	static final int TILE_RTABLE9 = 419;

	static final int TILE_DOOR = 5;        // door

	static final int TILE_WALL_T = 601;    // wall
	static final int TILE_WALL_B = 602;
	static final int TILE_WALL_L = 603;
	static final int TILE_WALL_R = 604;
	static final int TILE_WALL_TL = 605;
	static final int TILE_WALL_TR = 606;
	static final int TILE_WALL_BL = 607;
	static final int TILE_WALL_BR = 608;

	static final int TILE_DOOR_TO = 611;
	static final int TILE_DOOR_TC = 612;
	static final int TILE_DOOR_LO = 613;
	static final int TILE_DOOR_LC = 614;
	static final int TILE_DOOR_RO = 615;
	static final int TILE_DOOR_RC = 616;



	static String dir = "/data/images/";
	static Image transparent = ImageLoader.getImageFromURL(dir + "transparent.gif");
	static Image grid = ImageLoader.getImageFromURL(dir + "grid.gif");
	static Image floor = ImageLoader.getImageFromURL(dir + "floor.gif");
	static Image chairT = ImageLoader.getImageFromURL(dir + "chairT.gif");
	static Image chairB = ImageLoader.getImageFromURL(dir + "chairB.gif");
	static Image chairL = ImageLoader.getImageFromURL(dir + "chairL.gif");
	static Image chairR = ImageLoader.getImageFromURL(dir + "chairR.gif");
	static Image computer = ImageLoader.getImageFromURL(dir + "computer.gif");
	static Image table = ImageLoader.getImageFromURL(dir + "table.gif");
	static Image dark = ImageLoader.getImageFromURL(dir + "dark.gif");
	static Image trashcanE = ImageLoader.getImageFromURL(dir + "trashcan_e.gif");
	static Image trashcanF = ImageLoader.getImageFromURL(dir + "trashcan_f.gif");
	static Image papers = ImageLoader.getImageFromURL(dir + "papers.gif");

	static Image wallT = ImageLoader.getImageFromURL(dir + "wall/wall_t.gif");
	static Image wallB = ImageLoader.getImageFromURL(dir + "wall/wall_b.gif");
	static Image wallL = ImageLoader.getImageFromURL(dir + "wall/wall_l.gif");
	static Image wallR = ImageLoader.getImageFromURL(dir + "wall/wall_r.gif");
	static Image wallTL = ImageLoader.getImageFromURL(dir + "wall/wall_tl.gif");
	static Image wallTR = ImageLoader.getImageFromURL(dir + "wall/wall_tr.gif");
	static Image wallBL = ImageLoader.getImageFromURL(dir + "wall/wall_bl.gif");
	static Image wallBR = ImageLoader.getImageFromURL(dir + "wall/wall_br.gif");

	static Image doorTO = ImageLoader.getImageFromURL(dir + "wall/door_to.gif");
	static Image doorTC = ImageLoader.getImageFromURL(dir + "wall/door_tc.gif");
	static Image doorLO = ImageLoader.getImageFromURL(dir + "wall/door_lo.gif");
	static Image doorLC = ImageLoader.getImageFromURL(dir + "wall/door_lc.gif");
	static Image doorRO = ImageLoader.getImageFromURL(dir + "wall/door_ro.gif");
	static Image doorRC = ImageLoader.getImageFromURL(dir + "wall/door_rc.gif");


	static Image sTableTL = ImageLoader.getImageFromURL(dir + "table/sTable_tl.gif");
	static Image sTableTM = ImageLoader.getImageFromURL(dir + "table/sTable_tm.gif");
	static Image sTableTR = ImageLoader.getImageFromURL(dir + "table/sTable_tr.gif");
	static Image sTableBL = ImageLoader.getImageFromURL(dir + "table/sTable_bl.gif");
	static Image sTableBM = ImageLoader.getImageFromURL(dir + "table/sTable_bm.gif");
	static Image sTableBR = ImageLoader.getImageFromURL(dir + "table/sTable_br.gif");


	static Image speechTL = ImageLoader.getImageFromURL(dir + "speechTL.gif");
	static Image speechTR = ImageLoader.getImageFromURL(dir + "speechTR.gif");
	static Image speechBL = ImageLoader.getImageFromURL(dir + "speechBL.gif");
	static Image speechBR = ImageLoader.getImageFromURL(dir + "speechBR.gif");

	static Image error = ImageLoader.getImageFromURL(dir + "error.gif");


	public static String getImageDirectoryURL()
	{
		return dir;
	}


	public static Image getImage(String file)
	{
		return Toolkit.getDefaultToolkit().getImage(file);
	}


	public static Image getImage(int key)
	{
		switch (key)
		{
		case TRANSPARENT:
			return transparent;
		case TILE_GRID:
			return grid;

		case TILE_FLOOR:
			return floor;
		case TILE_COMPUTER:
			return computer;
		case TILE_CHAIRT:
			return chairT;
		case TILE_CHAIRB:
			return chairB;
		case TILE_CHAIRL:
			return chairL;
		case TILE_CHAIRR:
			return chairR;

		case TILE_TRASHCANE:
			return trashcanE;
		case TILE_TRASHCANF:
			return trashcanF;
		case TILE_PAPERS:
			return papers;


		case TILE_DOOR_TO:
			return doorTO;
		case TILE_DOOR_TC:
			return doorTC;
		case TILE_DOOR_LO:
			return doorLO;
		case TILE_DOOR_LC:
			return doorLC;
		case TILE_DOOR_RO:
			return doorRO;
		case TILE_DOOR_RC:
			return doorRC;


		case TILE_WALL_TL:
			return wallTL;
		case TILE_WALL_T:
			return wallT;
		case TILE_WALL_TR:
			return wallTR;

		case TILE_WALL_L:
			return wallL;
		case TILE_WALL_R:
			return wallR;

		case TILE_WALL_BL:
			return wallBL;
		case TILE_WALL_B:
			return wallB;
		case TILE_WALL_BR:
			return wallBR;

		case TILE_STABLE_TL:
			return sTableTL;
		case TILE_STABLE_TM:
			return sTableTM;
		case TILE_STABLE_TR:
			return sTableTR;


		case TILE_STABLE_BL:
			return sTableBL;
		case TILE_STABLE_BM:
			return sTableBM;
		case TILE_STABLE_BR:
			return sTableBR;

		default:
			return error;
		}
	}
}
