import java.awt.Graphics;

import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int TILE_SIZE = 64; //the pixel size of each tile image
	public static final double TILE_SCALE = 2; //the factor to multiply the size with
	public static final int UNIT = (int)(TILE_SIZE * TILE_SCALE); //Factor to multiply world coordinates into screenspace pixel coordinates

	public double deltaTime;
	public TileMap map;
	public InputHandler input;
	
	public Game () {
		map = new TileMap(20,20);
		
		new Thread(this).start();
	}
	
	private double xPos = 0;
	private double yPos = 0;
	private double speed = 4;
	
	public void run(){
		init();
		while(true){ //TODO run at regular increments (60 fps), or as fast as possible (normalize with delta time)
			update();
		}
	}
	
	public void init() {
		input = new InputHandler(this);
	}
	
	long lastTime = System.currentTimeMillis();
	public void update(){
		long nowTime = System.currentTimeMillis();
		deltaTime = (lastTime - nowTime) / 1000D;
		lastTime = nowTime;
		
		if(input.up.isPressed()){
			yPos -= speed * deltaTime;
		}
		if(input.down.isPressed()){
			yPos += speed * deltaTime;	
		}
		if(input.left.isPressed()){
			xPos -= speed * deltaTime;
		}
		if(input.right.isPressed()){
			xPos += speed * deltaTime;
		}
		repaint();
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.DrawMap(g, xPos, yPos);
	}
}
