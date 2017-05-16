import java.awt.Graphics;

public class Player implements Mob {

	public InputHandler input;
	private double xPos = 0;
	private double yPos = 0;
	private double speed = 5;
	
	public Player(Game game){
		input = new InputHandler(game);
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

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
