import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Tile type is a collection of "static objects" (an enum), one for every different type of tile
//this is useful so that we don't need to have the same tile texture saved in memory for each stone tile
//we can save just one copy of the stone texture and access it from here
public enum TileType {
	
	stone("stone"),
	grass("grass"),
	sand( "sand"),
	gravel("gravel"),
	wall("wall"),
	floor("cobblestone"),
	trapDoor("trapDoor"),
	ladder("ladder"),
	ladderW("ladderW");
	
	private final String name;
	private BufferedImage img;
	
	//each tile type has a "name" (filepath)
	TileType(String name){
		this.name = name;
		
		//load the image associated with the tileType
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
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return getName();
	}
	
	//return the tileType from a string
	public static TileType fromString(String s){
		for(TileType t: TileType.values()){
			if(t.name().equals(s)){
				return t;
			}
		}
		return null;
	}
}
