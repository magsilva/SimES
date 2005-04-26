package simse.modelbuilder.mapeditor;

import javax.swing.*;
import java.awt.*;

public class Map extends JFrame
{
	int[][] tiles = new int[20][20];
	int tileSize = 0;
	int mapX = 0;
	int mapY = 0;
	
	public Map(int ts, int x, int y)
	{
		tileSize = ts;
		mapX = x;
		mapY = y;
	}
}
