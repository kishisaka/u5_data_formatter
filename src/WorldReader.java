import java.io.File;
import java.io.FileInputStream;

public class WorldReader 
{	
	MapChunk[][] m_worldMap = new MapChunk[16][16];
	
	/**
	 * read underworld or brittania file.
	 *  
	 *  The Underworld map.
	 * 
	 * Its size is 256x256 tiles.
	 * It is divided into 256 chunks. Each chunk has a size of 16x16 tiles,
	 * and each tile is stored as a uint8 (in Ultima 4, the chunk size was
	 * 32x32 tiles).
	 * Unlike BRIT.DAT, it is not compressed (no chunks were left out).
	 * The chunks are stored from west to east, north to south, i.e. the first
	 * chunk in the map is the one in the northwest corner.
	 * The tiles in a chunk are also stored from west to east, north to south.
	 *    
	 * @param fileName
	 */
	public WorldReader(String fileName)
	{
		try
		{
			System.out.println("reading " + fileName);
			File mapFile = new File(fileName);
			FileInputStream fis = new FileInputStream(mapFile);			
			for(int mapY = 0; mapY < 16; mapY ++)
			{
				for(int mapX = 0; mapX < 16; mapX ++)
				{
					MapChunk chunk = m_worldMap[mapX][mapY];
					if (chunk == null)
					{
						System.out.println("chunk is null, creating new chunk.");
						chunk = new MapChunk();
						chunk.setM_x(mapX);
						chunk.setM_y(mapY);
						m_worldMap[mapX][mapY] = chunk;
					}
					for(int chunkY = 0; chunkY < 16; chunkY ++)
					{
						for(int chunkX = 0; chunkX < 16; chunkX ++)
						{													
							int mapData = (byte)fis.read();
							System.out.println("[" + mapX + "][" + mapY + "] - [" + chunkX + "][" + chunkY + "]: " + mapData);							
							chunk.getM_mapChunk()[chunkX][chunkY] = (byte)mapData;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();			
		}
	}	
}
