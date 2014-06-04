package us.ttyl.game;

public class Coordinate 
{
	double m_x;
	double m_y;	
	
	public boolean equals(Object obj)
	{
		Coordinate coord = (Coordinate)obj;
		if (coord.m_x == m_x && coord.m_y == m_y)
		{
			return true;
		}
		else
		{
			return false;
		}		
	}
		
	public int hashCode()
	{
		return (int)(m_x* 1000) + (int)(m_y*100);
	}
	
	public Coordinate(double x, double y)
	{
		m_x = x;
		m_y = y;
	}

	public double getM_x() 
	{
		return m_x;
	}

	public double getM_y() 
	{
		return m_y;
	}
	
	public String toString()
	{
		return (int)m_x + " : " + (int)m_y;
	}
}
