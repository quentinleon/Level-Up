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
	
	BufferedImage img;
	
	
	public Enemy(Mob player, int healthNumber, int damageNumber, int speedNumber){
		health = healthNumber;
		target = player;
		damage = damageNumber;
		speed = speedNumber;
		playerX = target.getX();
		playerY = target.getY();
		
		img = null;
		try{
			img = ImageIO.read(new File("resources/mobs/enemy/Fire Mob.png"));
		} catch(IOException e){
			System.out.println("Could not load all player sprites!");
		}
	}

	public void init() {
		xPos = Math.random() * 51;
		yPos = Math.random() * 51;
	}

	public void update() {
		
		double testX = target.getX();
		double testY = target.getY();
		
		if ((testX != playerX || testY != playerY) && close() == true){
			
			if (testX > xPos){
				xPos += speed / 60;
			}
			else if (testX < xPos){
				xPos -= speed / 60;
			}
			
			if (testY > yPos){
				yPos += speed / 60;
			}
			else if (testY < xPos){
				yPos -= speed / 60;
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
					0, 0, img.getHeight(null), img.getHeight(null), null);
		}
		
	}

}
