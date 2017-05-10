import java.awt.image.BufferedImage;

public class Tile {
	private TileType type;
	private double height;
	private boolean traversable;
	private Interactive interactive;
	
	public Tile(TileType type){
		this.type = type;
		traversable = type.isTraversable();
		height = 0;
	}
	
	public Tile(TileType type, double height){
		this.type = type;
		this.traversable = type.isTraversable();
		this.height = height;
	}
	
	public void interact(){
		interactive.interact();
	}
	
	public void setType(TileType type){
		this.type = type;
	}
	
	public void setHeight(double height){
		this.height = height;
	}
	
	public double getHeight(){
		return height;
	}
	
	public BufferedImage getImage() {
		return type.getImage();
	}
	
	public boolean isTraversable(){
		return traversable;
	}
	
}
