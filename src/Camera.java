
public class Camera {
	private Mob target;
	private double xPos;
	private double yPos;
	
	private double xOffset;
	private double yOffset;
	
	public Camera (Mob target){
		this.target = target;
	}
	
	public void init(){
		
	}
	
	public void update(){
		xPos = target.getX() + xOffset;
		yPos = target.getY() + yOffset;
	}
	
	public double getX() {
		return xPos;
	}

	public double getY() {
		return yPos;
	}
	
	public void setTarget(Mob target){
		
	}
}
