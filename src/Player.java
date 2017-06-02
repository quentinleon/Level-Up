import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player implements Mob {

	private InputHandler input;
	private Game game;
	
	//Main logic variables
	private double xPos;
	private double yPos;
	private int health;
	private int direction;
	
	//A bunch of constants
	private final double speed = 5;
	private final double[] boundingBox = {.3,.7,.7,.9};
	private final int MAX_HEALTH = 20;;
	private final int damage = 2;
	private final double range = 1;
	
	//The exit x and y coordinates
	private int exitX;
	private int exitY;
	
	//current player states (used for animations)
	private boolean moving;
	private boolean attacking;
	private boolean dead;
	
	//current image displayed
	BufferedImage img;
	
	
	//this subclass is used to load and hold spritesheets
	class Animation {
		private BufferedImage image;
		private int fps; // not fully implemented, but could enable variable fps animations
		
		//load the image based on path
		public Animation(String path, int fps){
			this.fps = fps;
			image = null;
			try{
				image = ImageIO.read(new File("resources/mobs/player/" + path + ".png"));
			} catch(IOException e){
				System.out.println("Could not load the " + path + " animation!");
			}
		}
		
		public BufferedImage getImage(){
			return image;
		}
		
		public int getFPS(){
			return fps;
		}
	}
	//all the different animations being used currently
	private Animation walk_u = new Animation("walk_u", 5);
	private Animation walk_d = new Animation("walk_d", 5);
	private Animation walk_l = new Animation("walk_l", 5);
	private Animation walk_r = new Animation("walk_r", 5);
	
	private Animation attack_u = new Animation("attack_u", 5);
	private Animation attack_d = new Animation("attack_d", 5);
	private Animation attack_l = new Animation("attack_l", 5);
	private Animation attack_r = new Animation("attack_r", 5);
	
	public Player(Game g, double x, double y){
		this(g);
		xPos = x;
		yPos = y;
	}
	
	public Player(Game game){
		this.input = new InputHandler();
		this.game = game;
		direction = 180;
		resetHealth();
	}
	
	public void resetHealth(){
		health = MAX_HEALTH;
	}

	public boolean isDead(){
		return dead;
	}
	
	public InputHandler getInput() {
		return input;
	}
	
	//do damage to enemies in range
	public void attack(){
		for(Mob enemy : game.enemies){
			if(Math.abs(enemy.getX() - xPos) < range && Math.abs(enemy.getY() - yPos) < range){
				enemy.damage(damage);
			}
		}
	}
	
	//take damage
	public void damage(int damageTaken){
		health -= damageTaken;
	}
	
	//animation instance variables
	private int animFrame = 0;
	private int FPS = 5;
	private int counter = 0;
	
	public void update() {
		double xVelocity = 0;
		double yVelocity = 0;
		
		//Grab inputs
		if(dead && getInput().restart.isPressed()){
			game.restart();
		}
		if(getInput().attack.isPressed()){
			attacking = true;
		}
		if(getInput().up.isPressed()){
			yVelocity -= speed / 60;
		}
		if(getInput().down.isPressed()){
			yVelocity += speed / 60;
		}
		if(getInput().left.isPressed()){
			xVelocity -= speed / 60;
		}
		if(getInput().right.isPressed()){
			xVelocity += speed / 60;
		}
		
		//the desired positions on each axes
		double nextX = (xPos + xVelocity);
		double nextY = (yPos + yVelocity);
		
		//is the desired location on the X axis open? if so, move there
		if(game.map.isTraversable((int)(nextX + boundingBox[0]), (int)(yPos + boundingBox[1]) ) 
				&& game.map.isTraversable((int)(nextX + boundingBox[2]), (int)(yPos + boundingBox[3]) )){
			xPos = nextX;
		}
		
		//is the desired location on the Y axis open? if so, move there
		if(game.map.isTraversable((int)(xPos + boundingBox[0]), (int)(nextY + boundingBox[1]) )
				&& game.map.isTraversable((int)(xPos + boundingBox[2]), (int)(nextY + boundingBox[3]) )){
			yPos = nextY;
		}
		
		//set moving animation state based on velocity
		if(xVelocity != 0 || yVelocity != 0){
			moving = true;
		}
		else{
			moving = false;
		}
		
		//find the direction player is facing based on velocity
		if(xVelocity == 0 && yVelocity < 0){
			direction = 0;
		}
		else if(xVelocity > 0 && yVelocity < 0){
			direction = 45;
		}		
		else if(xVelocity > 0 && yVelocity == 0){
			direction = 90;
		}
		else if(xVelocity > 0 && yVelocity > 0){
			direction = 135;
		}
		else if(xVelocity  == 0 && yVelocity > 0){
			direction = 180;
		}
		else if(xVelocity < 0 && yVelocity > 0){
			direction = 225;
		}
		else if(xVelocity < 0 && yVelocity == 0){
			direction = 270;
		}
		else if(xVelocity < 0 && yVelocity < 0){
			direction = 315;
		}

		//use the direction and whether or not we are attacking to select an animation
		if(direction >= 45 && direction <= 135){
			if(!attacking){
				if(img != walk_r.getImage()){
					img = walk_r.getImage();
					animFrame = 0;
				}
			} else {
				if(img != attack_r.getImage()){
					img = attack_r.getImage();
					animFrame = 0;
				}
			}
		}
		else if(direction > 135 && direction < 225){
			if(!attacking){
				if(img != walk_d.getImage()){
					img = walk_d.getImage();
					animFrame = 0;
				}
			} else {
				if(img != attack_d.getImage()){
					img = attack_d.getImage();
				}
			}
		}
		else if(direction >= 225 && direction <= 315){
			if(!attacking){
				if(img != walk_l.getImage()){	
					img = walk_l.getImage();
					animFrame = 0;
				}
			} else {
				if(img != attack_l.getImage()){
					img = attack_l.getImage();
				}
			}
		}
		else if((direction > 315 && direction < 360) || (direction >= 0 && direction < 45)){
			if(!attacking){
				if(img != walk_u.getImage()){
					img = walk_u.getImage();
					animFrame = 0;
				}
			} else {
				if(img != attack_u.getImage()){
					img = attack_u.getImage();
				}
			}
		}
		
		
		//If we're next to the exit, load the next level
		if(Math.abs(exitX - xPos) < .5 && Math.abs(exitY - yPos) < .5){
			game.loadNextLevel();
		}
		
		//if we have no health, set death state to true
		if(health <= 0){
			dead = true;
			health = 0;
		}
		else{
			dead = false;
		}
		
		//if we're attacking, set the fps to 20, otherwise set it to 5
		FPS = attacking ? 20 : 5;
		
		//Cycle through the sprite sheets at the designated fps
		counter ++;
		if(counter > 60/FPS){
			counter = 0;
			//if we're attacking
			if(attacking){
				animFrame ++;
				//cycle through until the end of the sprite sheet
				if(animFrame >= (img.getWidth() / img.getHeight(null))){
					animFrame = 0;
					//then call the attack method on the end of the animation
					attack();
					attacking = false;
				}
			}
			else if(moving){
				//cycle through animations if we're moving
				animFrame ++;
				if(animFrame >= (img.getWidth() / img.getHeight(null))){
					animFrame = 0;
				}
			}
		}
	}

	//getter and setter methods
	
	public String getName(){
		return "player";
	}
	
	public void setPosition(double x, double y){
		xPos = x;
		yPos = y;
	}
	
	public void setExit(int x, int y){
		exitX = x;
		exitY = y;
	}
	
	public void setHealth(int h){
		this.health = h;
	}
	
	public int getHealth(){
		return health;
	}
	
	@Override
	public double getX() {
		return xPos;
	}

	@Override
	public double getY() {
		return yPos;
	}

	//draw the player
	public void draw(Graphics g, Camera cam) {
		if(img != null){
			int drawPosX = (int)((xPos - cam.getX()) * Game.UNIT);
			int drawPosY = (int)((yPos - cam.getY()) * Game.UNIT);
			g.drawImage(img, drawPosX, drawPosY, drawPosX + Game.UNIT, drawPosY + Game.UNIT,
					img.getHeight(null) * animFrame, 0,
					img.getHeight(null) * (animFrame + 1), img.getHeight(null), null);
			if(game.debug){
				//draw colliders if we're in debug mode
				g.setColor(Color.GREEN);
				g.fillOval(drawPosX + (int)(boundingBox[0] * Game.UNIT), drawPosY + (int)(boundingBox[1] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[0] * Game.UNIT), drawPosY + (int)(boundingBox[3] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[2] * Game.UNIT), drawPosY + (int)(boundingBox[1] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[2] * Game.UNIT), drawPosY + (int)(boundingBox[3] * Game.UNIT), 4, 4);
			}	
			
			//draw health bar
			g.setColor(Color.RED);
			g.fillRect(20, 20, 200, 20);
			g.setColor(Color.GREEN);
			g.fillRect(20, 20, (int)(200 * health/MAX_HEALTH), 20);
			g.setColor(Color.BLACK);
			g.drawRect(20,20,200,20);
		}
	}
}
