import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * used to reconstitute the brittania file. The original file is missing water chunks.
 * which are contained in the ovl file. 
 * 
 * see http://codex.ultimaaiera.com/wiki/Ultima_V_Internal_Formats for details.
 *  
 * @author test
 *
 */
public class FileDecompressor 
{
	String m_mapFile = "h:/u5/BRIT.DAT";
	String m_overlayFile = "h:/u5/DATA.OVL/";
	String m_uncompressedMapFile = "/brit_full.dat";
	
	byte[][] m_landChunks = new byte[16][16];	
	MapChunk[][] m_brittania = new MapChunk[16][16];
	
	public static void main(String args[])
	{
		new FileDecompressor();
	}
	
	public FileDecompressor()
	{
		//openOverlayFile();
		openOverlayFileForTownCoords();
		//processLandData();
		//writeNewBrittaniaOut();
	}
	
	/**
	 * put together uncompressed map data from compressed brittania file and overlay containing the water only chunks. 
	 */
	public void processLandData()
	{
		System.out.println("prcessing land data");
		try
		{
			//open compressed brittania land data
			File mapFile = new File(m_mapFile);
			FileInputStream fis = new FileInputStream(mapFile);
			
			// iterate over overlay data
			for(int mapY = 0; mapY < 16; mapY ++)
			{
				for(int mapX = 0; mapX < 16; mapX ++)
				{			
					System.out.println("land type: " + m_landChunks[mapX][mapY]);
					if (m_landChunks[mapX][mapY] == 0)
					{						
						//generate land chunk from file
						System.out.println("generating land chunk");
						MapChunk chunk = m_brittania[mapX][mapY];
						if (chunk == null)
						{
							System.out.println("chunk is null, creating new chunk.");
							chunk = new MapChunk();
							chunk.setM_x(mapX);
							chunk.setM_y(mapY);
							m_brittania[mapX][mapY] = chunk;
						}
						for(int chunkY = 0; chunkY < 16; chunkY ++)
						{
							for(int chunkX = 0; chunkX < 16; chunkX ++)
							{													
								int mapData = (byte)fis.read();
								// System.out.println("[" + mapX + "][" + mapY + "] - [" + chunkX + "][" + chunkY + "]: " + mapData);							
								chunk.getM_mapChunk()[chunkX][chunkY] = (byte)mapData;
							}
						}
					}
					else
					{
						//generate water chunk
						System.out.println("generating water chunk");
						MapChunk chunk = m_brittania[mapX][mapY];
						if (chunk == null)
						{
							System.out.println("chunk is null, creating new chunk.");
							chunk = new MapChunk();
							chunk.setM_x(mapX);
							chunk.setM_y(mapY);
							m_brittania[mapX][mapY] = chunk;
						}
						for(int chunkY = 0; chunkY < 16; chunkY ++)
						{
							for(int chunkX = 0; chunkX < 16; chunkX ++)
							{																												
								chunk.getM_mapChunk()[chunkX][chunkY] = 0x1;
							}
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
	
	/**
	 * get overlay that contains water only chunks
	 */
	public void openOverlayFile()
	{
		System.out.println("reading overlay data");
		try
		{
			File f = new File(m_overlayFile);			
			FileInputStream fs = new FileInputStream(f);
			fs.skip(0x3886);
			for(int y = 0; y < 16; y ++)
			{
				for(int x = 0; x < 16; x ++)
				{
					int type = fs.read();
					System.out.println(type);
					if (type == 255)
					{
						m_landChunks[x][y] = (byte)1;
					}
					else
					{
						m_landChunks[x][y] = (byte)0;
					}
				}
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("done reading overlay data");
	}
	
	/**
	 * get overlay that contains water only chunks
	 */
	public void openOverlayFileForTownCoords()
	{
		System.out.println("reading overlay data");
		try
		{	
			byte[] cityNames = new byte[273];
			File f = new File(m_overlayFile);			
			FileInputStream fs = new FileInputStream(f);
			fs.skip(0xa4d);
			fs.read(cityNames);
			fs.close();
			String cities = new String(cityNames);
			StringTokenizer st = new StringTokenizer(cities, String.valueOf('\0'));
			while(st.hasMoreTokens())
			{
				System.out.println(st.nextToken());
			}
			
			byte[] xCoords = new byte[40];
			f = new File(m_overlayFile);			
			fs = new FileInputStream(f);
			fs.skip(0x1e9a);
			fs.read(xCoords);
			fs.close();
			
			byte[] yCoords = new byte[40];
			f = new File(m_overlayFile);			
			fs = new FileInputStream(f);
			fs.skip(0x1ec2);
			fs.read(yCoords);
			fs.close();
			
			for(int i = 0; i < 40; i ++)
			{				
				System.out.println((0xFF & xCoords[i]) + ":" + (0xFF & yCoords[i]) + "   |   chunk :" + ((0xFF & xCoords[i])/16) + ":" + ((0xFF & yCoords[i])/16) + "  map :" + ((0xFF & xCoords[i])%16) + ":" + ((0xFF & yCoords[i])%16));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("done reading overlay data");
	}
	
	/**
	 * write out the uncompressed brittania file from map chunk data. 
	 */
	public void writeNewBrittaniaOut()
	{
		try
		{
			//setup output file
			File mapFile = new File(m_uncompressedMapFile);
			FileOutputStream fos = new FileOutputStream(mapFile);
			
			//write out all chunks
			for(int chunkY = 0; chunkY < 16; chunkY ++)
			{
				for(int chunkX = 0; chunkX < 16; chunkX ++)
				{
					//get map chunk
					byte[][] mapChunk = m_brittania[chunkX][chunkY].getM_mapChunk();
					for(int mapY = 0; mapY < 16; mapY ++)
					{
						for(int mapX = 0; mapX < 16; mapX ++)
						{
							//write map bytes. 
							fos.write(mapChunk[mapX][mapY]);
						}
					}
				}
			}
			//close output file. 
			fos.flush();
			fos.close();
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
