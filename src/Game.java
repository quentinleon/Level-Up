import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int TILE_SIZE = 16; //the pixel size of each tile image
	public static final double TILE_SCALE = 3; //the factor to multiply the size with
	public static final int UNIT = (int)(TILE_SIZE * TILE_SCALE); //Factor to multiply world coordinates into screenspace pixel coordinates

	public TileMap map;
	public Player player;
	
	public Game () {
		map = new TileMap(100,100);
		
		new Thread(this).start();
	}
	
	public void run(){
		init();
		while(true){ //TODO run at regular increments (60 fps), or as fast as possible (normalize with delta time)
			update();
		}
	}
	
	public void init() {
		player = new Player(this);
	}
	
	long lastTime = System.currentTimeMillis();
	double counter = 0;
	
	public void update(){
		long nowTime = System.currentTimeMillis();
		counter += (nowTime - lastTime);
		lastTime = nowTime;
		
		if(counter >= 1000/60){
			counter = 0;
			player.update();
		}
		
		repaint();
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(new Color(100,100,100));
		
		g.fillRect(0, 0, getWidth(), getHeight());
		int drawWidth = (int)(player.getX() + (getWidth()/UNIT) + 2);
		int drawHeight = (int)(player.getY() + (getHeight()/UNIT) + 2);
		
		map.drawMap(g, player.getX(), player.getY(), drawWidth, drawHeight);
		//draw mobs
		//draw objects
		map.drawShadows(g, player.getX(), player.getY(), drawWidth, drawHeight);
		map.drawWalls(g, player.getX(), player.getY(), drawWidth, drawHeight);
		//draw lights?
	}
}
