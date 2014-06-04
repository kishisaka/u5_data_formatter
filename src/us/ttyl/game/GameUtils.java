package us.ttyl.game;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GameUtils 
{
	/**
	 * loads the tile image map from file and splits the individual image into 512 different tile images. 
	 */
	public static ArrayList <BufferedImage> getTilesFromFile()
	{		
		ArrayList <BufferedImage> tileList = new ArrayList<BufferedImage>();
		try
		{
			BufferedImage tileMap = ImageIO.read(new File("./images/tiles.png"));			
			for(int y = 0 ; y < 16; y ++)
			{
				for(int x = 0; x < 32; x ++)
				{
					tileList.add(tileMap.getSubimage(x*32, y*32, 32, 32));										
				}
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return tileList;
	}	
}
