import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy implements Mob{
	
	private Game game;
	private String name;
	
	private double xPos;
	private double yPos;
	
	private Mob target;
	private int health;
	private int damageValue;
	private int speed;
	
	private int tickCount;
	private final double[] boundingBox = {.2,.6,.8,.8};
	
	BufferedImage img;
	
	//This constructor is for quick enemy spawning but with different stats based on level
	public Enemy(Game g, Mob target){ 
		int level = g.getLevel();
		//System.out.println("Loading level " + level + " enemy!");
		if(level == 5){
			loadEnemy(g, target, "piskel", 1, 1, 3);
		} else if(level == 4){
			loadEnemy(g, target, "piskel", 2, 2, 3);
		} else if(level == 3){
			loadEnemy(g, target, "piskel", 3, 2, 3);
		} else if(level == 2){
			loadEnemy(g, target, "piskel", 4, 3, 4);
		} else if(level == 1){
			loadEnemy(g, target, "fireMonster", 6, 4, 5);
		}
	}
	
	//TODO This constructor is for loading mobs from a file by name
	public Enemy(Game g, Mob target, String name){ 
		if(name.equals("fire1")){
			loadEnemy(g, target, "fireMonster", 1, 2, 3);
		} else if(name.equals("piskel1")){
			loadEnemy(g, target, "piskel", 1, 1, 4);
		} else {
			System.out.println("Unknown enemy \"" + name + "\"");
		}
	}
	
	//Ugly constructor, don't use (unless absolutely necessary)
	public Enemy(Game game, Mob target, String name, int healthNumber, int damageNumber, int speedNumber){
		loadEnemy(game, target, name, healthNumber, damageNumber, speedNumber);
	}
	
	//initializes the enemy with its stats and loads its sprite sheet
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
	
	//instance variables for movement and collisions
	private double nextX;
	private double nextY;
	
	//instance variables for animations
	private int counter = 0;
	private int animFrame = 0;
	private int FPS = 5;
	
	//(Called 60 times a second from Game update loop)
	public void update() {
		
		if(health > 0) {

			//Loop through the spreadsheet at <FPS> frames per second
			counter ++;
			if(counter > 60/FPS){
				counter = 0;
				animFrame ++;
				if(animFrame >= (img.getWidth() / img.getHeight(null))){
					animFrame = 0;
				}
			}
			
			double inrange = .5;
			
			//if we are close, but not too close on the X, set our desired position on the X axis (nextX)
			if(inSight() && Math.abs(target.getX() - xPos) > inrange){
				if (target.getX() > xPos){
					nextX = xPos + (speed / 60.0);
				}
				else if (target.getX() < xPos){
					nextX = xPos - (speed / 60.0);
				}
			} 
			//if we are close, but not too close on the Y, set our desired position on the Y axis (nextY)
			if(inSight() && Math.abs(target.getY() - yPos) > inrange){
				if (target.getY() > yPos){
					nextY = yPos + (speed / 60.0);
				}
				else if (target.getY() < yPos){
					nextY = yPos - (speed / 60.0);
				}
			}
			
			//can we move onto our X axis desired position? (nextX) if we can, then do it
			if(game.map.isTraversable((int)(nextX + boundingBox[0]), (int)(yPos + boundingBox[1]) ) 
					&& game.map.isTraversable((int)(nextX + boundingBox[2]), (int)(yPos + boundingBox[3]) )){
				xPos = nextX;
			}
			
			//can we move onto our Y axis desired position? (nextY) if we can, then do it
			if(game.map.isTraversable((int)(xPos + boundingBox[0]), (int)(nextY + boundingBox[1]) )
					&& game.map.isTraversable((int)(xPos + boundingBox[2]), (int)(nextY + boundingBox[3]) )){
				yPos = nextY;
			}
			
			//if we are very close to the player, start counting
			if (( Math.abs(target.getX() - xPos) <= inrange * 2 && Math.abs(target.getY() - yPos) <= inrange * 2 ) == true){
				tickCount++;
			}
			
			//every second spent close to the player, attack him
			if (tickCount / 60 >= 1){
				tickCount = 0;
				attack(damageValue);	
			}
		}
	}
	
	//do damage to the target
	public void attack(int damage){
		target.damage(damage);
	}
	
	//take damage
	public void damage(int damage){
		health -= damage;
	}
	
	//is the target close enough to move towards?
	//TODO update to use sight lines?
	public boolean inSight(){
		boolean close = false;
		if (( Math.abs(target.getX() - xPos) <= 5 && Math.abs(target.getY() - yPos) <= 5 ) == true){
			close = true;
		}
		return close;
	}
	
	//is the target close enough to hit
	public boolean inRange(){
		boolean damageDistance = false;
		if (( Math.abs(target.getX() - xPos) <= 1 && Math.abs(target.getY() - yPos) <= 1 ) == true){
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
