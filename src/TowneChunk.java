
public class TowneChunk
{
	private int m_x = 0;
	private int m_y = 0;
	private byte[][] m_towneChunk = new byte[32][32];
		
	public int getM_x() 
	{
		return m_x;
	}

	public void setM_x(int mX) 
	{
		m_x = mX;
	}

	public int getM_y()
	{
		return m_y;
	}

	public void setM_y(int mY)
	{
		m_y = mY;
	}

	public byte[][] getM_towneChunk()
	{
		return m_towneChunk;
	}
}
