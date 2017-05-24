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
	}
	
	public Player(Game game){
		input = new InputHandler();
		
		img = walk_r.getImage();
		//TODO set img based on movement state
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
		
		counter ++;
		if(counter > 60/FPS){
			counter = 0;
			animFrame ++;
			if(animFrame >= (img.getWidth() / img.getHeight(null))){
				animFrame = 0;
			}
		}
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
