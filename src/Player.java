import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player implements Mob {

	public InputHandler input;
	private Game game;
	
	private double xPos = 0;
	private double yPos = 0;
	
	private final double speed = 5;
	private final double[] boundingBox = {.3,.7,.7,.9};
	
	private int exitX;
	private int exitY;
	
	double nextX;
	double nextY;
	double xVelocity;
	double yVelocity;
	
	private boolean moving;
	
	private int direction;
	
	private final int MAX_HEALTH = 20;;
	private int health = MAX_HEALTH;
	private int damage = 2;
	
	private boolean dead;
	
	//current image displayed
	BufferedImage img;
	
	class Animation {
		private BufferedImage image;
		private int fps;
		
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
	private Animation walk_u = new Animation("walk_u", 5);
	private Animation walk_d = new Animation("walk_d", 5);
	private Animation walk_l = new Animation("walk_l", 5);
	private Animation walk_r = new Animation("walk_r", 5);
	
	private Animation attack_u = new Animation("attack_u", 5);
	private Animation attack_d = new Animation("attack_d", 5);
	private Animation attack_l = new Animation("attack_l", 5);
	private Animation attack_r = new Animation("attack_r", 5);
	
	private Animation deadanim = new Animation("dead", 1);

	private int animFrame = 0;
	private int FPS = 5;
	private int counter = 0;
	
	public Player(Game g, double x, double y){
		this(g);
		xPos = x;
		yPos = y;
		dead = false;
		direction = 180;
	}
	
	public Player(Game game){
		input = new InputHandler();
		this.game = game;
		
		img = walk_r.getImage();
		//TODO set img based on movement state
	}
	
	public void init() {
		
	}

	public void damage(int damageTaken){
		health -= damageTaken;
	}
	
	public void update() {
		xVelocity = 0;
		yVelocity = 0;
		
		if(input.up.isPressed()){
			yVelocity -= speed / 60;
		}
		if(input.down.isPressed()){
			yVelocity += speed / 60;
		}
		if(input.left.isPressed()){
			xVelocity -= speed / 60;
		}
		if(input.right.isPressed()){
			xVelocity += speed / 60;
		}
			
		nextX = (xPos + xVelocity);
		nextY = (yPos + yVelocity);
		
		if(game.map.isTraversable((int)(nextX + boundingBox[0]), (int)(yPos + boundingBox[1]) ) 
				&& game.map.isTraversable((int)(nextX + boundingBox[2]), (int)(yPos + boundingBox[3]) )){
			xPos = nextX;
		}
		
		if(game.map.isTraversable((int)(xPos + boundingBox[0]), (int)(nextY + boundingBox[1]) )
				&& game.map.isTraversable((int)(xPos + boundingBox[2]), (int)(nextY + boundingBox[3]) )){
			yPos = nextY;
		}
		
		if(xVelocity != 0 || yVelocity != 0){
			moving = true;
		}
		else{
			moving = false;
		}
		
		//find the direction player is facing, use for animation choosing and attack
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
		//System.out.println(direction);
		
		if(health <= 0){
			dead = true;
			health = 0;
		}
		else{
			dead = false;
		}
		
		if(direction >= 45 && direction <= 135){
			if(img != walk_r.getImage()){
				img = walk_r.getImage();
				animFrame = 0;
			}
		}
		else if(direction > 135 && direction < 225){
			if(img != walk_d.getImage()){
				img = walk_d.getImage();
				animFrame = 0;
			}
		}
		else if(direction >= 225 && direction <= 315){
			if(img != walk_l.getImage()){	
				img = walk_l.getImage();
				animFrame = 0;
			}
		}
		else if((direction > 315 && direction < 360) || (direction >= 0 && direction < 45)){
			if(img != walk_u.getImage()){
				img = walk_u.getImage();
				animFrame = 0;
			}
		}
		
		if(Math.abs(exitX - xPos) < .5 && Math.abs(exitY - yPos) < .5){
			game.loadNextLevel();
		}
		
		counter ++;
		if(counter > 60/FPS){
			counter = 0;
			if(moving){
				animFrame ++;
				if(animFrame >= (img.getWidth() / img.getHeight(null))){
					animFrame = 0;
				}
			}
		}
	}

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

	public void draw(Graphics g, Camera cam) {
		if(img != null){
			int drawPosX = (int)((xPos - cam.getX()) * Game.UNIT);
			int drawPosY = (int)((yPos - cam.getY()) * Game.UNIT);
			g.drawImage(img, drawPosX, drawPosY, drawPosX + Game.UNIT, drawPosY + Game.UNIT,
					img.getHeight(null) * animFrame, 0,
					img.getHeight(null) * (animFrame + 1), img.getHeight(null), null);
			if(game.debug){
				g.setColor(Color.GREEN);
				g.fillOval(drawPosX + (int)(boundingBox[0] * Game.UNIT), drawPosY + (int)(boundingBox[1] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[0] * Game.UNIT), drawPosY + (int)(boundingBox[3] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[2] * Game.UNIT), drawPosY + (int)(boundingBox[1] * Game.UNIT), 4, 4);
				g.fillOval(drawPosX + (int)(boundingBox[2] * Game.UNIT), drawPosY + (int)(boundingBox[3] * Game.UNIT), 4, 4);
				
				g.fillRect(drawPosX - 10, drawPosY, 10, Game.UNIT);
				g.setColor(Color.RED);
				g.fillRect(drawPosX - 10, drawPosY, 10, Game.UNIT - (int)(Game.UNIT * ((double)health/MAX_HEALTH)));
				g.setColor(Color.BLACK);
				g.drawRect(drawPosX - 10, drawPosY, 10, Game.UNIT);
			}	
		}
	}
}
