import java.awt.Graphics;

public interface Mob {
	public void update(); //called 60 times a second
	public void draw(Graphics g, Camera cam); //called by the renderer and is used to draw itself into the game scene
	public void damage(int damage); //apply <damage> amount of damage to the Mob
	public double getX();
	public double getY();
	public int getHealth();
	public String getName();
	public void setPosition(double x, double y);
	public void setHealth(int h);
}
