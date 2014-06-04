package us.ttyl.game;

public class Missile 
{
	int m_x;
	int m_y;
	int m_speed;
	int m_range;
	int m_traveled;
	double m_direction;
	int m_playerId;
	
	public Missile(int startX, int startY, int speed, int range, double direction, int playerId)
	{
		m_playerId = playerId;
		m_x = startX;
		m_y = startY;
		m_speed = speed;
		m_range = range;
		m_direction = direction;
	}

}
