import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Tile {
	
	water("water", false),
	stone("stone", true),
	grass("grass", true),
	sand("sand", true),
	gravel("gravel", true);
	
	//private final String name;
	private final boolean traversable;
	private BufferedImage img;
	
	Tile(String name, boolean traversable){
		this.traversable = traversable;
		
		img = null;
		try{
			img = ImageIO.read(new File("resources/tiles/"+name+".png"));
		} catch(IOException e){
			System.out.println("Could not load " + name + " sprite!");
		}
	}
	
	public BufferedImage getImage() {
		return img;
	}
	
	public boolean isTraversable(){
		return traversable;
	}
}
