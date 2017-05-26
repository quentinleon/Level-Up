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
	private double speed = 5;
	
	double nextX;
	double nextY;
	double xVelocity;
	double yVelocity;
	
	public boolean moving;
	
	public int direction;
	
	public int health = 20;
	public int damage = 2;
	
	public boolean dead;
	
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
	//animation strips
	private Animation idle_u = new Animation("idle_u", 2);
	private Animation idle_d = new Animation("idle_d", 2);
	private Animation idle_l = new Animation("idle_l", 2);
	private Animation idle_r = new Animation("idle_r", 2);

	private Animation walk_u = new Animation("walk_u", 5);
	private Animation walk_d = new Animation("walk_d", 5);
	private Animation walk_l = new Animation("walk_l", 5);
	private Animation walk_r = new Animation("walk_r", 5);
	
	private Animation attack_u = new Animation("attack_u", 5);
	private Animation attack_d = new Animation("attack_d", 5);
	private Animation attack_l = new Animation("attack_l", 5);
	private Animation attack_r = new Animation("attack_r", 5);

	private int animFrame = 0;
	private int FPS = 5;
	private int counter = 0;
	
	public Player(Game g, double x, double y){
		this(g);
		xPos = x;
		yPos = y;
		dead = false;
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
		
		if(game.map.isTraversable((int)(nextX + .1), (int)(yPos + .7) ) && game.map.isTraversable((int)(nextX + .6), (int)(yPos + .7) )){
			xPos = nextX;
		}
		
		if(game.map.isTraversable((int)(xPos + .1), (int)(nextY + .7) ) && game.map.isTraversable((int)(xPos + .6), (int)(nextY + .7) )){
			yPos = nextY;
		}
		
		if(xVelocity > 0 || yVelocity > 0){
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
		}
		else{
			dead = false;
		}
		
		counter ++;
		if(counter > 60/FPS){
			counter = 0;
			animFrame ++;
			if(animFrame >= (img.getWidth() / img.getHeight(null))){
				animFrame = 0;
			}
		}
	}

	public void setPosition(double x, double y){
		xPos = x;
		yPos = y;
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
			
		}
	}
}
