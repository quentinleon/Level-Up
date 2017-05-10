import java.awt.image.BufferedImage;

public class Tile {
	private TileType type;
	private boolean traversable;
	private Interactive interactive;
	
	public Tile(TileType type){
		this.type = type;
		traversable = type.isTraversable();
	}
	
	public void interact(){
		interactive.interact();
	}
	
	public void setType(TileType type){
		this.type = type;
	}
	
	public BufferedImage getImage() {
		return type.getImage();
	}
	
	public boolean isTraversable(){
		return traversable;
	}
	
}
