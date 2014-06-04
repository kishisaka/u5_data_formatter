package us.ttyl.game; 

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameEngine extends JPanel
{
	BufferedImage m_worldImage = null;
	BufferedImage m_playerHighlight = null;
	
	ArrayList <BufferedImage> m_tiles = null;	
	ArrayList <Missile> m_missiles = new ArrayList<Missile>(); 
	ArrayList <Player> m_playerList = new ArrayList<Player>();	
	
	Player m_currentSelectedPlayer = null;	
	
	MapChunk[][] m_map = null;
	
	public static void main(String args[])
	{
		new GameEngine();
	}
	
	public GameEngine()
	{
		//setup players
		playerListSetup();
		m_currentSelectedPlayer = m_playerList.get(0);
		
		m_tiles = GameUtils.getTilesFromFile();
		
		try
		{
			m_playerHighlight = ImageIO.read(new File("./images/highlight.png"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
	    	public void mouseReleased(MouseEvent event)
	    	{	
	    		int x = event.getX();
	    		int y = event.getY();
	    		
	    		double deg = MathBox.track((double)(x-m_currentSelectedPlayer.m_currentLocationX*32),(double)(y-m_currentSelectedPlayer.m_currentLocationY*32));	    		
	    		
	    		Missile m = new Missile(m_currentSelectedPlayer.m_currentLocationX*32, m_currentSelectedPlayer.m_currentLocationX*32, 15, 256, deg, m_currentSelectedPlayer.m_playerId);
	    		m_missiles.add(m);
	    		
	    		getNextPlayer();
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
	    		//37 left
	    		//38 up
	    		//39 right 
	    		//40 down
	    		
	    		String keyPressed = KeyEvent.getKeyText(event.getKeyCode());
	    		System.out.println("keyPressed: " + keyPressed + " : " + event.getKeyCode());
	    		if (event.getKeyCode() == 37)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationX = m_currentSelectedPlayer.m_currentLocationX - 1;
	    		}
	    		if (event.getKeyCode() == 38)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationY = m_currentSelectedPlayer.m_currentLocationY - 1;
	    		}
	    		if (event.getKeyCode() == 39)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationX = m_currentSelectedPlayer.m_currentLocationX + 1;
	    		}
	    		if (event.getKeyCode() == 40)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationY = m_currentSelectedPlayer.m_currentLocationY + 1;
	    		}
	    		
	    		getNextPlayer();
	    		
	    	}
		});	    
	    f.getContentPane().add(this);
	    f.pack();
	    f.setSize(800,800);
	    f.setVisible(true);
	    
	    //start the screen refresher which repaints the screen 50 times a second. 
	    Thread playerWindowRefresher = new Thread(new Runnable()
	    {	    		    
	    	public void run()
	    	{
	    		while (true)
	    		{
		    		try
		    		{
		    			Thread.sleep(50);
		    		}
		    		catch (Exception e)
		    		{
		    			e.printStackTrace();
		    		}
		    		drawPlayField();
		    		//moveComputerUnits();		    		
		    		updateMissileLocation();
		    		missileHitAndResolveDamage();
		    		removeExhaustedMissiles();
		    		repaint();
	    		}
	    	}
	    });
	    playerWindowRefresher.start();
	}
	
	/*
	public void moveComputerUnits()
	{
		ArrayList <Player>players = new ArrayList<Player>();
		ArrayList <Player>computerPlayers = new ArrayList<Player>();
		
		for(int i = 0; i < m_playerList.size(); i ++)
		{			
			if(p.m_side == 0)
			{
				players.add(m_playerList.get(i));
			}
			else
			{
				computerPlayers.add(m_playerList.get(i));
			}
		}
		
		for(int i = 0; i < computerPlayers.size(); i ++)
		{			
			for(int j = 0; j < players.)
			{
				int direction = MathBox.findNextDirection(, );
				if (MathBox.LEFT == direction)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationX = m_currentSelectedPlayer.m_currentLocationX - 1;
	    		}
	    		if (MathBox.UP == direction)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationY = m_currentSelectedPlayer.m_currentLocationY - 1;
	    		}
	    		if (MathBox.RIGHT == direction)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationX = m_currentSelectedPlayer.m_currentLocationX + 1;
	    		}
	    		if (MathBox.DOWN == direction)
	    		{
	    			m_currentSelectedPlayer.m_currentLocationY = m_currentSelectedPlayer.m_currentLocationY + 1;
	    		}
			}
		}
	}
	*/
	/*
	public int findClosestPlayer(Player p)
	{		
		for(int i = 0; i < m_playerList.size(); i ++)
		{
			Player p = m_playerList.get(i);
			
		}
	}
	*/
	
	
	public void playerListSetup()
	{
		Player p1 = new Player(0,3,3,15,0);
		m_playerList.add(p1);
		Player p2 = new Player(1,3,6,15,0);
		m_playerList.add(p2);
		Player p3 = new Player(2,3,9,15,0);
		m_playerList.add(p3);
		Player p4 = new Player(3,5,5,15,0);
		m_playerList.add(p4);
		Player p5 = new Player(4,5,7,15,0);
		m_playerList.add(p5);
		Player p6 = new Player(5,7,6,15,0);
		m_playerList.add(p6);
		
		Player pa1 = new Player(6,19,15,15,1);
		m_playerList.add(pa1);
		Player pa2 = new Player(7,19,17,15,1);
		m_playerList.add(pa2);
		Player pa3 = new Player(8,19,19,15,1);
		m_playerList.add(pa3);
		Player pa4 = new Player(9,17,16,15,1);
		m_playerList.add(pa4);
		Player pa5 = new Player(10,17,18,15,1);
		m_playerList.add(pa5);
		Player pa6 = new Player(11,15,17,15,1);
		m_playerList.add(pa6);
	}
	
	public void drawPlayField()
	{
		m_worldImage = new BufferedImage(672, 672, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = m_worldImage.getGraphics();
		for(int y = 0; y< 21; y ++)
		{
			for(int x = 0; x< 21; x ++)
			{
				boolean playerPainted = false;
				for(int i = 0; i < m_playerList.size(); i ++)
				{
					Player p = m_playerList.get(i);
					if (x == p.m_currentLocationX  && y == p.m_currentLocationY && p.getM_hitPoints() > 0)
					{
						g.drawImage(m_tiles.get(320), x*32, y*32, null);
						playerPainted = true;
						break;
					}
					if (x == p.m_currentLocationX  && y == p.m_currentLocationY && p.getM_hitPoints() <= 0)
					{
						g.drawImage(m_tiles.get(287), x*32, y*32, null);
						playerPainted = true;
						break;
					}
				}
				if (playerPainted == false)
				{			
					g.drawImage(m_tiles.get(5), x*32, y*32, null);
				}			
			}
			int currentPlayerX = m_currentSelectedPlayer.m_currentLocationX * 32;
			int currentPlayerY = m_currentSelectedPlayer.m_currentLocationY * 32;
			g.drawImage(m_playerHighlight, currentPlayerX, currentPlayerY, null);					
		}		
	}
	
	public void updateMissileLocation()
	{
		for(int i = 0; i < m_missiles.size(); i ++)
		{
			Missile m = m_missiles.get(i);
			Graphics g = m_worldImage.getGraphics();
						
			Coordinate c = MathBox.getPointForDeg(m.m_direction, m.m_traveled);
			
			g.drawImage(m_tiles.get(0), (int)c.m_x + m_playerList.get(m.m_playerId).m_currentLocationX*32, (int)c.m_y + m_playerList.get(m.m_playerId).m_currentLocationY*32, null);
			m.m_x = (int)c.m_x + m_playerList.get(m.m_playerId).m_currentLocationX*32;
			m.m_y = (int)c.m_y + m_playerList.get(m.m_playerId).m_currentLocationY*32;
			m.m_traveled = m.m_traveled + m.m_speed;
		}
	}
	
	public void missileHitAndResolveDamage()
	{
		for(int i = 0; i < m_missiles.size(); i ++)
		{
			Missile m = m_missiles.get(i);
			
			//generate missile rectangle for intersection test
			Rectangle missile = new Rectangle(m.m_x, m.m_y, 32, 32);
			
			//test each player in list or see if missile clips player
			for(int j = 0; j < m_playerList.size(); j ++)
			{
				Player p = m_playerList.get(j);				
				
				//only check if missile is not owned by launching player. 
				if(p.m_playerId != m.m_playerId)
				{					
					// convert player coords to pixels
					int x = p.m_currentLocationX * 32;
					int y = p.m_currentLocationY * 32;
					
					//generate player rectangle for intersection test
					Rectangle player = new Rectangle(x, y, 32, 32);										
					
					//test if missile clipped player bounding box
					if (missile.intersects(player) && p.getM_hitPoints() > 0 && m_playerList.get(m.m_playerId).m_side != p.m_side)
					{
						//notify a hit 						
						p.decrementHp(1);
						System.out.println("missile hit player: " + j + " hp:" + p.getM_hitPoints());
						//set missile to be removed
						m.m_traveled = m.m_range;
						break;
					}			
				}
			}	
		}
	}
	
	/**
	 * get next player that is alive and allow that player to have a turn 
	 */
	public void getNextPlayer()
	{		
		int currentIndex = m_currentSelectedPlayer.m_playerId + 1;
		while(true)
		{
			if (currentIndex == m_playerList.size())
			{
				currentIndex = 0;
			}	
			
			if ((m_playerList.get(currentIndex)).getM_hitPoints() > 0)
			{
				m_currentSelectedPlayer = m_playerList.get(currentIndex);
				break;
			}
			else
			{
				currentIndex ++;
			}						
		}
	}
	
	public void removeExhaustedMissiles()
	{
		for(int i = 0; i < m_missiles.size(); i ++)
		{
			Missile m = m_missiles.get(i);
			if (m.m_traveled >= m.m_range)
			{
				m_missiles.remove(i);
			}
		}
	}	
	
	public void paintComponent(Graphics g)
	{
		g.drawImage(m_worldImage, 0, 0, null);
	}
}

