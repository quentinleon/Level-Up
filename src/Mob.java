import java.awt.Graphics;

public interface Mob {
	public void init(); //called once at game start
	public void update(); //called 60 times a second
	public void draw(Graphics g, Camera cam); 
	public void damage(int damage);
	public double getX();
	public double getY();
	public int getHealth();
	public String getName();
	public void setPosition(double x, double y);
	public void setHealth(int h);
}
