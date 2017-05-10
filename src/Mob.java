
public interface Mob {
	public void init(); //called once at game start
	public void update(); //called 60 times a second
	
	public double getX();
	public double getY();
}
