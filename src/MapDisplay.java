import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * map creator frame, eventually to be turned into a map editor and tile 
 * engine for the adventure test game prototype. 
 * 
 * @author test
 *
 */
public class MapDisplay extends JPanel
{
	//mode 0 = land, mode 1 = towne, mode 2 = underworld, mode 3 = combat
	int m_mode = 1;
	
	// the main tile set
	ArrayList <BufferedImage> m_tiles = null;
	
	//player window size
	int m_userWindowSize = 20;
	
	// animated water tiles. 
	BufferedImage m_waterTile1 = null;
	BufferedImage m_waterTile2 = null;
	BufferedImage m_waterTile3 = null;
	BufferedImage m_lava = null;
	AnimatedTile m_waterfall = null;
	AnimatedTile m_fountain = null;
	
	// The world! 	
	WorldReader m_underworld = null;
	WorldReader m_brittania = null;
	TowneReader m_townes = null;
	TowneReader m_castles = null;
	TowneReader m_keeps = null;

	// user display window (11x11 tiles)
	int[][] m_userWindow = new int[m_userWindowSize][m_userWindowSize];
	
	//starting x,y chunk and staring x,y location on starting chunk 
	int m_currentChunkX = 0;
	int m_currentChunkY = 0;
	int m_currentMapX = 0;
	int m_currentMapY = 0;	
	
	int m_playerX = 0;
	int m_playerY = 0;
	
	/**
	 * startup stuff here! 
	 */
	public MapDisplay()
	{	
		// initialize maps
		m_underworld = new WorldReader("D:\\dev/U5DataFormatter\\data\\underworld.dat");
		m_brittania = new WorldReader("D:\\dev\\U5DataFormatter\\data\\brit_full.dat");
		m_townes = new TowneReader("D:\\dev\\U5DataFormatter\\data\\towne.dat");
		m_castles = new TowneReader("D:\\dev\\U5DataFormatter\\data\\castle.dat");
		m_keeps = new TowneReader("D:\\dev\\U5DataFormatter\\data\\keep.dat");
		
		//get tiles from file		
		m_tiles = GameUtils.getTilesFromFile();
		
		//set pixel cycle tiles here
		m_waterTile1 = m_tiles.get(1);
		m_waterTile2 = m_tiles.get(2);
		m_waterTile3 = m_tiles.get(3);	
		m_lava = m_tiles.get(143);
		
		//set animated tiles here
		m_waterfall = new AnimatedTile();
		m_waterfall.setM_startIndex(212);
		m_waterfall.setM_indexOffset(212);
		
		m_fountain = new AnimatedTile();
		m_fountain.setM_startIndex(216);
		m_fountain.setM_indexOffset(216);		
		
		JFrame f = new JFrame("MapCreator");
	    f.addWindowListener(new WindowAdapter() 
	    {
	    	public void windowClosing(WindowEvent e)
	    	{
	    		System.exit(0);
	    	}
	    });	
	    
	    f.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseClicked(MouseEvent event)
	    	{	
	    		//System.out.println(event.getX() + ":" + event.getY());
	    	}
	    });
	    f.addMouseMotionListener(new MouseAdapter() 
	    {
	    	public void mouseMoved(MouseEvent event)
	    	{   		
	    		//System.out.println(event.getX() + ":" + event.getY());
	    	}
	    });	    
	    
	    f.addKeyListener(new KeyAdapter() 
	    {
	    	public void keyReleased(KeyEvent event)
	    	{	   
	    		String keyPressed = KeyEvent.getKeyText(event.getKeyCode());
	    		//System.out.println("keyPressed: " + keyPressed);
	    		updateMap(keyPressed);
	    	}
		});	    
	    f.getContentPane().add(this);
	    f.pack();
	    f.setSize(800,800);
	    f.setVisible(true);
	    
	    //start the screen refresher which repaints the screen 20 times a second. 
	    Thread playerWindowRefresher = new Thread(new Runnable()
	    {	    		    
	    	public void run()
	    	{
	    		while (true)
	    		{
		    		try
		    		{
		    			Thread.sleep(40);
		    		}
		    		catch (Exception e)
		    		{
		    			e.printStackTrace();
		    		}
		    		repaint();
	    		}
	    	}
	    });
	    playerWindowRefresher.start();
	}
	
	/**
	 * when movement is detected via keypress, move the player window in the direction intended! 
	 * @param keyPressed
	 */
	public void updateMap(String keyPressed)
	{
		//for world map mode
		if (m_mode == 0)
		{	
			updatePlayerWorldLocation(keyPressed, m_brittania);
		}		
		
		// for towne map mode
		if (m_mode == 1)
		{	
			updatePlayerTowneLocation(keyPressed, m_townes);
		}
		
		// for underworld map mode 
		if (m_mode == 2)
		{
			updatePlayerWorldLocation(keyPressed, m_underworld);
		}
	} 	
	
	/**
	 * update the player map location on a town map
	 * @param keyPressed
	 * @param data
	 */
	public void updatePlayerTowneLocation(String keyPressed, TowneReader data)
	{
		// for towne map mode
		if (m_mode == 1)
		{				
			if (keyPressed.equals("Left"))
			{
				//update map position						
				m_currentMapX = m_currentMapX - 1;
				if (m_currentMapX < 0)
				{
					m_currentMapX = 31;
					
					//update chunk position
					m_currentChunkX = m_currentChunkX - 1;
					if (m_currentChunkX < 0)
					{
						m_currentChunkX = 3;
					}
				}
			}
			
			if (keyPressed.equals("Right"))
			{
				m_currentMapX = m_currentMapX + 1;
				if (m_currentMapX > 31)
				{
					m_currentMapX = 0;
					
					//update chunk position
					m_currentChunkX = m_currentChunkX + 1;
					if (m_currentChunkX > 3)
					{
						m_currentChunkX = 0;
					}				
				}
			}
			if (keyPressed.equals("Up"))
			{
				m_currentMapY = m_currentMapY - 1;
				if (m_currentMapY < 0)
				{
					m_currentMapY = 31;
					
					//update chunk position
					m_currentChunkY = m_currentChunkY - 1;
					if (m_currentChunkY < 0)
					{
						m_currentChunkY = 3;
					}
				}
			}		
			if (keyPressed.equals("Down"))
			{
				m_currentMapY = m_currentMapY + 1;
				if (m_currentMapY > 31)
				{
					m_currentMapY = 0;
					
					//update chunk position
					m_currentChunkY = m_currentChunkY + 1;
					if (m_currentChunkY > 3)
					{
						m_currentChunkY = 0;
					}	
				}
			}
			//System.out.println("cX: " + m_currentMapX + " , cY: " + m_currentMapY + "  |  chunkX: " + m_currentChunkX + " , chunkY: " + m_currentChunkY);
			
			//initialize new buffer positions! 
			int bufferX = m_currentMapX;
			int bufferY = m_currentMapY;
			int bufferChunkX = m_currentChunkX;
			int bufferChunkY = m_currentChunkY;
			
			//update the user window
			for(int y= 0; y < m_userWindowSize; y ++)
			{
				for(int x = 0; x < m_userWindowSize; x ++)
				{		
					// check x
					if (bufferX < 0)
					{
						bufferX = 31;
						
						//update chunk position
						bufferChunkX = bufferChunkX - 1;
						if (bufferChunkX < 0)
						{
							bufferChunkX = 3;
						}					
					}
					if (bufferX > 31)
					{
						bufferX = 0;
						
						//update chunk position
						bufferChunkX = bufferChunkX + 1;
						if (bufferChunkX > 3)
						{
							bufferChunkX = 0;
						}				
					}
					//check y
					if (bufferY < 0)
					{
						bufferY = 31;
						
						//update chunk position
						bufferChunkY = bufferChunkY - 1;
						if (bufferChunkY < 0)
						{
							bufferChunkY = 3;
						}
					}
					if (bufferY > 31)
					{
						bufferY = 0;
						
						//update chunk position
						bufferChunkY = bufferChunkY + 1;
						if (bufferChunkY > 3)
						{
							bufferChunkY = 0;
						}	
					}
											
					int tileIndex = (int)(data.m_towneMap[bufferChunkX][bufferChunkY].getM_towneChunk()[bufferX][bufferY]);												
					
					// System.out.println("bufferX: " + bufferX + " , bufferY: " + bufferY + "  |  bufferChunkX: " + bufferChunkX + " , bufferChunkY: " + bufferChunkY + " | tile: " + tileIndex);					
					
					m_userWindow[x][y] = 0xFF&tileIndex;
					
					//update buffer X position						
					bufferX = bufferX + 1;
				}
				//update buffer Y position
				bufferY = bufferY + 1;
				
				//reset X to start position
				bufferX = m_currentMapX;			
				bufferChunkX = m_currentChunkX;								
			}				
		}
	}
	
	/**
	 * update player orientation on the world map
	 * @param keyPressed
	 * @param data
	 */
	public void updatePlayerWorldLocation(String keyPressed, WorldReader data) 
	{
		if (keyPressed.equals("Left"))
		{
			//update map position						
			m_currentMapX = m_currentMapX - 1;
			if (m_currentMapX < 0)
			{
				m_currentMapX = 15;
				
				//update chunk position
				m_currentChunkX = m_currentChunkX - 1;
				if (m_currentChunkX < 0)
				{
					m_currentChunkX = 15;
				}
			}
		}
		
		if (keyPressed.equals("Right"))
		{
			m_currentMapX = m_currentMapX + 1;
			if (m_currentMapX > 15)
			{
				m_currentMapX = 0;
				
				//update chunk position
				m_currentChunkX = m_currentChunkX + 1;
				if (m_currentChunkX > 15)
				{
					m_currentChunkX = 0;
				}				
			}
		}
		if (keyPressed.equals("Up"))
		{
			m_currentMapY = m_currentMapY - 1;
			if (m_currentMapY < 0)
			{
				m_currentMapY = 15;
				
				//update chunk position
				m_currentChunkY = m_currentChunkY - 1;
				if (m_currentChunkY < 0)
				{
					m_currentChunkY = 15;
				}
			}
		}		
		if (keyPressed.equals("Down"))
		{
			m_currentMapY = m_currentMapY + 1;
			if (m_currentMapY > 15)
			{
				m_currentMapY = 0;
				
				//update chunk position
				m_currentChunkY = m_currentChunkY + 1;
				if (m_currentChunkY > 15)
				{
					m_currentChunkY = 0;
				}	
			}
		}
		//System.out.println("cX: " + m_currentMapX + " , cY: " + m_currentMapY + "  |  chunkX: " + m_currentChunkX + " , chunkY: " + m_currentChunkY);
		
		//initialize new buffer positions! 
		int bufferX = m_currentMapX;
		int bufferY = m_currentMapY;
		int bufferChunkX = m_currentChunkX;
		int bufferChunkY = m_currentChunkY;
		
		//update the user window
		for(int y= 0; y < m_userWindowSize; y ++)
		{
			for(int x = 0; x < m_userWindowSize; x ++)
			{		
				// check x
				if (bufferX < 0)
				{
					bufferX = 15;
					
					//update chunk position
					bufferChunkX = bufferChunkX - 1;
					if (bufferChunkX < 0)
					{
						bufferChunkX = 15;
					}					
				}
				if (bufferX > 15)
				{
					bufferX = 0;
					
					//update chunk position
					bufferChunkX = bufferChunkX + 1;
					if (bufferChunkX > 15)
					{
						bufferChunkX = 0;
					}				
				}
				//check y
				if (bufferY < 0)
				{
					bufferY = 15;
					
					//update chunk position
					bufferChunkY = bufferChunkY - 1;
					if (bufferChunkY < 0)
					{
						bufferChunkY = 15;
					}
				}
				if (bufferY > 15)
				{
					bufferY = 0;
					
					//update chunk position
					bufferChunkY = bufferChunkY + 1;
					if (bufferChunkY > 15)
					{
						bufferChunkY = 0;
					}	
				}
				//System.out.println("bufferX: " + bufferX + " , bufferY: " + bufferY + "  |  bufferChunkX: " + bufferChunkX + " , bufferChunkY: " + bufferChunkY + " | tile: " + (int)(data.m_worldMap[bufferChunkX][bufferChunkY].getM_mapChunk()[bufferX][bufferY]));
				
				int tileIndex = (int)(data.m_worldMap[bufferChunkX][bufferChunkY].getM_mapChunk()[bufferX][bufferY]);
				
				m_userWindow[x][y] = 0xFF&tileIndex;
				
				//update buffer X position						
				bufferX = bufferX + 1;
			}
			//update buffer Y position
			bufferY = bufferY + 1;
			
			//reset X to start position
			bufferX = m_currentMapX;			
			bufferChunkX = m_currentChunkX;								
		}		
	}
	
	/**
	 * copy animated tiles into a masked tile to render animated tiles (water, lava, etc). 
	 * when porting this to Android, use Bitmap instead of BufferedImage. 
	 * mask must be red color (0xFF0000)
	 * @param imageToImpose
	 * @param mask
	 * @return
	 */	
	public BufferedImage colorMask(BufferedImage imageToImpose, BufferedImage mask) 
	{		
		BufferedImage imageOut = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		
		//copy mask to imageOut
		for(int y = 0; y < 32; y ++)
		{
			for(int x = 0; x < 32; x ++)
			{				
				long colorValue = mask.getRGB(x, y);
				// System.out.println(x + ":" + y + " color: " + colorValue);
				imageOut.setRGB(x, y, (int)colorValue);				    			
			}
		}
		
		//superimpose image into masked area only! 
		for(int y = 0; y < 32; y ++)
		{
			for(int x = 0; x < 32; x ++)
			{
				long colorValue = imageToImpose.getRGB(x, y);
				if (imageOut.getRGB(x, y) == -65536)
				{
					imageOut.setRGB(x, y, (int)colorValue);
				}				    			
			}
		}
		
		return imageOut;
	}
	
	/**
	 * animate 4 frames of tiles. tile set that completes animation must be contiguous in the tile list. 
	 * @param tile
	 * @return
	 */
	public BufferedImage animateTile(AnimatedTile tile)
	{
		BufferedImage tileImage = m_tiles.get(tile.getM_indexOffset());
		
		tile.setM_indexOffset(tile.getM_indexOffset() + 1);
		if (tile.getM_indexOffset() - tile.getM_startIndex() >= 3)
		{
			tile.setM_indexOffset(tile.getM_startIndex());
		}
		
		return tileImage;
	}
	
	/**
	 * move image pixels down 1 row
	 * @param image
	 * @return
	 */
	public BufferedImage colorCycle(BufferedImage image)
	{
		BufferedImage newlyCycledImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		for(int y = 0; y < 32; y ++)
		{
			for(int x = 0; x < 32; x ++)
			{
				int colorValue = image.getRGB(x, y);
				if ((y+1) > 31)
				{
					newlyCycledImage.setRGB(x, 0, colorValue);
				}
				else
				{
					newlyCycledImage.setRGB(x, y + 1, colorValue);
				}
			}
		}
		return newlyCycledImage;
	}
	
	public void updateBuffer(int x, int y)
	{
		
	}
	
	/**
	 * paint the currently loaded map! 
	 */
	public void paintComponent(Graphics g)
	{	
		//update water image 
		m_waterTile1 = colorCycle(m_waterTile1);
		m_waterTile2 = colorCycle(m_waterTile2);	
		m_waterTile3 = colorCycle(m_waterTile3);
		m_lava = colorCycle(m_lava);
		BufferedImage waterfall = animateTile(m_waterfall);
		BufferedImage fountain = animateTile(m_fountain);
		
		HashMap <Integer,BufferedImage>waterTiles = new HashMap<Integer,BufferedImage>(20); 
		
		for(int i = 52; i < 56; i ++)
		{
			waterTiles.put(i, colorMask(m_waterTile3, m_tiles.get(i)));
		}

		for(int i = 96; i < 112; i ++)
		{
			waterTiles.put(i, colorMask(m_waterTile3, m_tiles.get(i)));
		}		
		
		waterTiles.put(228, colorMask(m_waterTile3, m_tiles.get(228)));
		waterTiles.put(229, colorMask(m_waterTile3, m_tiles.get(229)));
		waterTiles.put(230, colorMask(m_waterTile3, m_tiles.get(230)));
		waterTiles.put(231, colorMask(m_waterTile3, m_tiles.get(231)));

	    //draw map! 
	    for(int y = 0; y < m_userWindowSize; y ++)
	    {
	    	for(int x = 0; x < m_userWindowSize; x ++)
	    	{	
	    		try
	    		{	
	    			int tileId = m_userWindow[x][y];
	    			switch (tileId)
	    			{
	    				case 1: g.drawImage(m_waterTile1, x*32, y*32, null); break;
	    				case 2: g.drawImage(m_waterTile2, x*32, y*32, null); break;
	    				case 3: g.drawImage(m_waterTile3, x*32, y*32, null); break;
	    				case 52: g.drawImage(waterTiles.get(52), x*32, y*32, null); break;
	    				case 53: g.drawImage(waterTiles.get(53), x*32, y*32, null); break;
	    				case 54: g.drawImage(waterTiles.get(54), x*32, y*32, null); break;
	    				case 55: g.drawImage(waterTiles.get(55), x*32, y*32, null); break;
	    				case 96: g.drawImage(waterTiles.get(96), x*32, y*32, null); break;	    				
	    				case 97: g.drawImage(waterTiles.get(97), x*32, y*32, null); break;
	    				case 98: g.drawImage(waterTiles.get(98), x*32, y*32, null); break;
	    				case 99: g.drawImage(waterTiles.get(99), x*32, y*32, null); break;
	    				case 100: g.drawImage(waterTiles.get(100), x*32, y*32, null); break;
	    				case 101: g.drawImage(waterTiles.get(101), x*32, y*32, null); break;
	    				case 102: g.drawImage(waterTiles.get(102), x*32, y*32, null); break;
	    				case 103: g.drawImage(waterTiles.get(103), x*32, y*32, null); break;
	    				case 104: g.drawImage(waterTiles.get(104), x*32, y*32, null); break;
	    				case 105: g.drawImage(waterTiles.get(105), x*32, y*32, null); break;
	    				case 106: g.drawImage(waterTiles.get(106), x*32, y*32, null); break;
	    				case 107: g.drawImage(waterTiles.get(107), x*32, y*32, null); break;
	    				case 108: g.drawImage(waterTiles.get(108), x*32, y*32, null); break;
	    				case 109: g.drawImage(waterTiles.get(109), x*32, y*32, null); break;
	    				case 110: g.drawImage(waterTiles.get(110), x*32, y*32, null); break;
	    				case 111: g.drawImage(waterTiles.get(111), x*32, y*32, null); break;
	    				case 228: g.drawImage(waterTiles.get(228), x*32, y*32, null); break;
	    				case 229: g.drawImage(waterTiles.get(229), x*32, y*32, null); break;
	    				case 230: g.drawImage(waterTiles.get(230), x*32, y*32, null); break;
	    				case 231: g.drawImage(waterTiles.get(231), x*32, y*32, null); break;
	    				case 143: g.drawImage(m_lava, x*32, y*32, null); break;
	    				case 212: g.drawImage(waterfall, x*32, y*32, null); break;
	    				case 216: g.drawImage(fountain, x*32, y*32, null); break;
	    				default: g.drawImage(m_tiles.get(m_userWindow[x][y]), x*32, y*32, null); break;
	    			}	    			    		
	    		}
	    		catch (Exception e)
	    		{
	    			e.printStackTrace();
	    			System.out.println(x*32 + ":" + y*32);
	    		}	    			    			    		
	    	}
	    }	   
	}
	
	public static void main(String args[])
	{
		new MapDisplay();
	}
}

