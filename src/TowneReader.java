import java.io.File;
import java.io.FileInputStream;

/**
 * 32x32 block sizes. 
 * @author test
 *
 */
public class TowneReader
{	
	TowneChunk[][] m_towneMap = new TowneChunk[4][4];
		
	public TowneReader(String fileName)
	{
		try
		{
			System.out.println("reading " + fileName);
			File mapFile = new File(fileName);
			FileInputStream fis = new FileInputStream(mapFile);			
			for(int mapY = 0; mapY < 4; mapY ++)
			{
				for(int mapX = 0; mapX < 4; mapX ++)
				{
					TowneChunk chunk = m_towneMap[mapX][mapY];
					if (chunk == null)
					{
						System.out.println("chunk is null, creating new chunk.");
						chunk = new TowneChunk();
						chunk.setM_x(mapX);
						chunk.setM_y(mapY);
						m_towneMap[mapX][mapY] = chunk;
					}
					for(int chunkY = 0; chunkY < 32; chunkY ++)
					{
						for(int chunkX = 0; chunkX < 32; chunkX ++)
						{													
							int mapData = (byte)fis.read();
							System.out.println("[" + mapX + "][" + mapY + "] - [" + chunkX + "][" + chunkY + "]: " + mapData);							
							chunk.getM_towneChunk()[chunkX][chunkY] = (byte)mapData;
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
