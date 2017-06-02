import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy implements Mob{
	
	private Game game;
	private int health;
	private String name;
	private double xPos;
	private double yPos;
	private Mob target;
	private int damageValue;
	private int speed;
	private double playerX;
	private double playerY;
	private int tickCount;
	private final double[] boundingBox = {.2,.6,.8,.8};
	
	private int animFrame = 0;
	private int FPS = 5;
	
	BufferedImage img;
	
	public Enemy(Game g, Mob target, String name){ 
		if(name.equals("fire1")){
			loadEnemy(g, target, "fireMonster", 1, 2, 3);
		} else if(name.equals("piskel1")){
			loadEnemy(g, target, "piskel", 1, 1, 4);
		} else {
			System.out.println("Unknown enemy \"" + name + "\"");
		}
	}
	
	public Enemy(Game game, Mob target, String name, int healthNumber, int damageNumber, int speedNumber){
		loadEnemy(game, target, name, healthNumber, damageNumber, speedNumber);
	}
	
	private void loadEnemy(Game game, Mob target, String name, int healthNumber, int damageNumber, int speedNumber){
		this.game = game;
		this.target = target;
		this.name = name;
		this.health = healthNumber;
		this.damageValue = damageNumber;
		this.speed = speedNumber;
		
		img = null;
		try{
			img = ImageIO.read(new File("resources/mobs/enemy/" + name + ".png"));
		} catch(IOException e){
			System.out.println("Could not load enemy sprite!");
		}
	}

	public void setPosition(double x, double y){
		xPos = x;
		yPos = y;
	}
	
	public void setHealth(int h){
		this.health = h;
	}
	
	public int getHealth(){
		return health;
	}
	
	public String getName(){
		return name;
	}
	
	public void init() {

	}

	private int counter = 0;
	
	private double nextX;
	private double nextY;
	
	public void update() {
		if(health > 0) {
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
			
			double inrange = .5;
			
			//if we are close, but not too close on the X, move the X
			if(inSight() && Math.abs(playerX - xPos) > inrange){
				if (playerX > xPos){
					nextX = xPos + (speed / 60.0);
				}
				else if (playerX < xPos){
					nextX = xPos - (speed / 60.0);
				}
			} 
			//if we are close, but not too close on the Y, move the Y
			if(inSight() && Math.abs(playerY - yPos) > inrange){
				if (playerY > yPos){
					nextY = yPos + (speed / 60.0);
				}
				else if (playerY < yPos){
					nextY = yPos - (speed / 60.0);
				}
			}
			
			if(game.map.isTraversable((int)(nextX + boundingBox[0]), (int)(yPos + boundingBox[1]) ) 
					&& game.map.isTraversable((int)(nextX + boundingBox[2]), (int)(yPos + boundingBox[3]) )){
				xPos = nextX;
			}
			
			if(game.map.isTraversable((int)(xPos + boundingBox[0]), (int)(nextY + boundingBox[1]) )
					&& game.map.isTraversable((int)(xPos + boundingBox[2]), (int)(nextY + boundingBox[3]) )){
				yPos = nextY;
			}
			
			//if we are very close to the player, start counting
			if (( Math.abs(playerX - xPos) <= inrange * 2 && Math.abs(playerY - yPos) <= inrange * 2 ) == true){
				tickCount++;
			}
			
			if (tickCount / 60 >= 1){
				tickCount = 0;
				attack(damageValue);	
			}
		}
	}
	
	public void attack(int damage){
		target.damage(damage);
	}
	
	public void damage(int damage){
		health -= damage;
	}
	
	public boolean inSight(){
		boolean close = false;
		if (( Math.abs(playerX - xPos) <= 7 && Math.abs(playerY - yPos) <= 7 ) == true){
			close = true;
		}
		return close;
	}
	
	public boolean inRange(){
		boolean damageDistance = false;
		if (( Math.abs(playerX - xPos) <= 1 && Math.abs(playerY - yPos) <= 1 ) == true){
			damageDistance = true;
			tickCount++;
			System.out.println(tickCount);
		}
		return damageDistance;
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
			if(health > 0){
				g.drawImage(img, drawPosX, drawPosY, drawPosX + Game.UNIT, drawPosY + Game.UNIT,
						img.getHeight(null) * animFrame, 0,
						img.getHeight(null) * (animFrame + 1), img.getHeight(null), null);
			}
			if(game.debug){
				g.setColor(Color.BLUE);
				g.fillOval(drawPosX + (int)(boundingBox[0] * Game.UNIT), drawPosY + (int)(boundingBox[1] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[0] * Game.UNIT), drawPosY + (int)(boundingBox[3] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[2] * Game.UNIT), drawPosY + (int)(boundingBox[1] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[2] * Game.UNIT), drawPosY + (int)(boundingBox[3] * Game.UNIT), 4, 4);
			}
		}
		
	}

}
