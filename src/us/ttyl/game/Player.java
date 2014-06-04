package us.ttyl.game;

public class Player 
{
	int m_playerId = 0;
	int m_currentLocationX = 0;
	int m_currentLocationY = 0;
	private int m_hitPoints = 0;
	
	//0 = player, 1 = computer
	int m_side = 0;
	
	public Player(int id, int x, int y, int hp, int side)
	{
		m_playerId = id;
		m_hitPoints = hp;
		m_currentLocationX = x;
		m_currentLocationY = y;
		m_side = side;
	}	

	public int getM_hitPoints() 
	{
		return m_hitPoints;
	}
	
	public void decrementHp(int hp)
	{
		m_hitPoints = m_hitPoints - hp;
	}
		
}
