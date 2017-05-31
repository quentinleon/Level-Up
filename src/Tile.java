import java.awt.image.BufferedImage;

public class Tile {
	private TileType type;
	private double height;
	
	public Tile(TileType type){
		this.type = type;
		height = 0;
	}
	
	public Tile(TileType type, double height){
		this.type = type;
		this.height = height;
	}
	
	public void setType(TileType type){
		this.type = type;
	}
	
	public void addHeight(double added){
		height += added;
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
	
	public TileType getType(){
		return type;
	}	
}
