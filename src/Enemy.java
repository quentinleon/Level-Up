import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy implements Mob{
	
	private int health;
	private double xPos;
	private double yPos;
	private Mob target;
	private int damage;
	private int speed;
	private double playerX;
	private double playerY;
	
	private int animFrame = 0;
	private int FPS = 5;
	
	BufferedImage img;
	
	
	public Enemy(Mob player, String name, int healthNumber, int damageNumber, int speedNumber){
		health = healthNumber;
		target = player;
		damage = damageNumber;
		speed = speedNumber;
		playerX = target.getX();
		playerY = target.getY();
		
		img = null;
		try{
			img = ImageIO.read(new File("resources/mobs/enemy/" + name + ".png"));
		} catch(IOException e){
			System.out.println("Could not enemy sprite!");
		}
		
		xPos = 1;
		yPos = 1;
	}

	public void init() {
		xPos = 1;
		yPos = 1;
	}

	private int counter = 0;
	public void update() {
		
		playerX = target.getX();
		playerY = target.getY();
		
		counter ++;
		if(counter > 60/FPS){
			counter = 0;
			animFrame ++;
			if(animFrame >= (img.getWidth() / img.getHeight(null))){
				animFrame = 0;
			}
		}
		
		if (close() == true){
			
			if (playerX > xPos){
				xPos += speed / 60.0;
			}
			else if (playerX < xPos){
				xPos -= speed / 60.0;
			}
			
			if (playerY > yPos){
				yPos += speed / 60.0;
			}
			else if (playerY < xPos){
				yPos -= speed / 60.0;
			}
			
			} 
		
		
		}
		

	public boolean close(){
		boolean close = false;
		if (( Math.abs(playerX - xPos) <= 7 && Math.abs(playerY - yPos) <= 7 ) == true){
			close = true;
		}
		return close;
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

	@Override
	public void draw(Graphics g, Camera cam) {
		// TODO Auto-generated method stub
		if(img != null){
			int drawPosX = (int)((xPos - cam.getX()) * Game.UNIT);
			int drawPosY = (int)((yPos - cam.getY()) * Game.UNIT);
			g.drawImage(img, drawPosX, drawPosY, drawPosX + Game.UNIT, drawPosY + Game.UNIT,
					img.getHeight(null) * animFrame, 0,
					img.getHeight(null) * (animFrame + 1), img.getHeight(null), null);
		}
		
	}

}
