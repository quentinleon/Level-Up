import java.awt.Graphics;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;

import javax.swing.JPanel;


public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int TILE_SIZE = 16; //the pixel size of each tile image
	public static final double TILE_SCALE = 5; //the factor to multiply the size with
	public static final int UNIT = (int)(TILE_SIZE * TILE_SCALE); //Factor to multiply world coordinates into screenspace pixel coordinates

	public TileMap map;
	public Player player;
	public Camera camera;
	
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		player = new Player(this, 0, 0);
		camera = new Camera(player);
		camera.setXOffset((screenSize.getWidth()/2) / Game.UNIT);
		camera.setYOffset((screenSize.getHeight()/2) / Game.UNIT);
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
			camera.update();
		}
		
		repaint();
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(new Color(100,100,100));
		
		g.fillRect(0, 0, getWidth(), getHeight());
		int drawWidth = (int)(camera.getX() + (getWidth()/UNIT) + 2);
		int drawHeight = (int)(camera.getY() + (getHeight()/UNIT) + 2);
		
		map.drawMap(g, camera.getX(), camera.getY(), drawWidth, drawHeight);
		//draw mobs
		player.draw(g, camera);
		//draw objects
		map.drawShadows(g, camera.getX(), camera.getY(), drawWidth, drawHeight);
		map.drawWalls(g, camera.getX(), camera.getY(), drawWidth, drawHeight);
		//draw lights?
	}
}
