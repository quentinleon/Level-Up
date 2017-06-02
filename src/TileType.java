import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.Random;

public enum TileType {
	
	stone("stone"),
	grass("grass"),
	sand( "sand"),
	gravel("gravel"),
	wall("wall"),
	cobbleStone("cobblestone"),
	none(null);
	
	private final String name;
	private BufferedImage img;
	
	TileType(String name){
		this.name = name;
		
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
	
	private static final TileType[] VALUES = values();
	private static final int SIZE = VALUES.length - 1;
	private static final Random RANDOM = new Random();
	
	public static TileType getRandomTile(){
		return VALUES[RANDOM.nextInt(SIZE)];
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return getName();
	}
	
	public static TileType fromString(String s){
		for(TileType t: TileType.values()){
			if(t.name().equals(s)){
				return t;
			}
		}
		return none;
	}
	
	public static TileType fromColor(Color s){
		//TODO from color
		return stone;
	}
}
