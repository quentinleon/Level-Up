import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player implements Mob {

	public InputHandler input;
	
	private double xPos = 0;
	private double yPos = 0;
	private double speed = 5;
	
	BufferedImage right;
	
	public Player(Game game){
		input = new InputHandler(game);
		
		right = null;
		try{
			right = ImageIO.read(new File("resources/mobs/player/right.png"));
		} catch(IOException e){
			System.out.println("Could not load all player sprites!");
		}
	}
	
	public void init() {
		
	}

	public void update() {
		if(input.up.isPressed()){
			yPos -= speed / 60;
		}
		if(input.down.isPressed()){
			yPos += speed / 60;	
		}
		if(input.left.isPressed()){
			xPos -= speed / 60;
		}
		if(input.right.isPressed()){
			xPos += speed / 60;
		}
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return yPos;
	}

	public void draw(Graphics g) {
		if(right != null){
			int drawPosX = (int)((0) * Game.UNIT);
			int drawPosY = (int)((0) * Game.UNIT);
			g.drawImage(right, drawPosX, drawPosY, drawPosX + Game.UNIT, drawPosY + Game.UNIT,
					0, 0, right.getHeight(null), right.getHeight(null), null);
		}
	}

}
