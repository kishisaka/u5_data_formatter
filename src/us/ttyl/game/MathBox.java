package us.ttyl.game;

/**
 * extracted from the radar/orge test game I made way back in 2003, lucky I saved that thing.  
 * @author test
 *
 */
public class MathBox 
{
	public static final int LEFT = 1;
	public static final int UP = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;
	
	/**
	 * given an x and a y, get the direction that we should shoot the missile
	 * @param a
	 * @param b
	 * @return
	 */
	public static double track(double a, double b)
	{
		double returnDeg = 0;

	    if (a <= 0 && b > 0)
	    {
	    	double convertA = a * -1;
	    	double deg = 180 - (Math.toDegrees(Math.atan(b/convertA)) + 90);
	    	returnDeg = deg + 90;
	    }

	    if (a < 0 && b <= 0)
	    {
	    	double convertA = a * -1;
	    	double deg = 180 - (Math.toDegrees(Math.atan(b/convertA)) + 90);
	    	returnDeg = deg + 90;
	    }

	    if (a >= 0 && b < 0)
	    {
	    	double convertB = b * -1;
	    	double deg = 180 - (Math.toDegrees(Math.atan(convertB/a)) + 90);
	    	returnDeg = deg + 270;
	    }

	    if (a >= 0 && b >= 0)
	    {
	    	returnDeg = Math.toDegrees(Math.atan(b/a));
	    }	    
	    return returnDeg;
	}

	
	/**
	 * given a direction and the range that we should shoot, get a x,y coord to paint the missile.
	 * @param deg
	 * @param hypotenuse
	 * @return
	 */
	public static Coordinate getPointForDeg(double deg, int hypotenuse)
	{				
		double x = Math.cos(Math.toRadians(deg)) * hypotenuse;
		double y = Math.sin(Math.toRadians(deg)) * hypotenuse;
		return new Coordinate(x,y);
	}
	
	/**
	 * get distance between two coords;
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int getDistance(int x1, int y1, int x2, int y2)
	{		
		int x = x1 - x2;
		x = x*x;
		int y = y1 - y2;
		y = y*y;				
		int d = (int)(Math.sqrt(x+y));
		return d;
	}
	
	/**
	 * find the next direction that character a should go to get closer to the target character b
	 * 1 is left, 2 is up, 3 right, 4 down 
	 * @param a characterA
	 * @param b characterB
	 * @return
	 */
	public static int findNextDirection(Player a, Player b)
	{
		int tempDistance = 0;
		int currentDistance = 10000000;
		int direction = 0; 
		
		int tempX = 0;
		int tempY = 0;
		
		//incrememt X
		tempX = a.m_currentLocationX + 1;
		tempY = a.m_currentLocationY;
		tempDistance = getDistance(tempX, tempY, b.m_currentLocationX, b.m_currentLocationY);
		if (tempDistance < currentDistance)
		{
			direction = RIGHT;
			currentDistance = tempDistance;
			
		}
		//decrement Y
		tempX = a.m_currentLocationX;
		tempY = a.m_currentLocationY - 1;
		tempDistance = getDistance(tempX, tempY, b.m_currentLocationX, b.m_currentLocationY);
		if (tempDistance < currentDistance)
		{
			direction = UP;
			currentDistance = tempDistance;
			
		}
		//decrementX
		tempX = a.m_currentLocationX - 1;
		tempY = a.m_currentLocationY;
		tempDistance = getDistance(tempX, tempY, b.m_currentLocationX, b.m_currentLocationY);
		if (tempDistance < currentDistance)
		{
			direction = LEFT;
			currentDistance = tempDistance;
			
		}
		//decrement Y
		tempX = a.m_currentLocationX;
		tempY = a.m_currentLocationY + 1;
		tempDistance = getDistance(tempX, tempY, b.m_currentLocationX, b.m_currentLocationY);
		if (tempDistance < currentDistance)
		{
			direction = DOWN;
			currentDistance = tempDistance;			
		}
		return direction;
	}
}
