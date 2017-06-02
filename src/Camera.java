//Camera follows a target with offsets. These offsets can be used to center the target or pan the camera about the target.

public class Camera {
	private Mob target;
	private double xPos;
	private double yPos;
	
	private double xOffset;
	private double yOffset;
	
	public Camera (Mob target){
		this.target = target;
	}
	
	//Set the camera's position to the target's position plus offsets
	//(Called 60 times a second from Game update loop)
	public void update(){
		xPos = target.getX() - xOffset;
		yPos = target.getY() - yOffset;
	}
	
	public double getX() {
		return xPos;
	}

	public double getY() {
		return yPos;
	}
	
	public void setTarget(Mob target){
		this.target = target;
	}
	
	public void setXOffset(double xOffset) {
		this.xOffset = xOffset;
	}	
	
	public void setYOffset(double yOffset) {
		this.yOffset = yOffset;
	}
}
