import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.Random;

public enum TileType {
	
	//water("water", false),
	stone("stone", true),
	grass("grass", true),
	sand("sand", true),
	gravel("gravel", true),
	
	stoneWall("stoneWall", false);
	
	//private final String name;
	private final boolean traversable;
	private BufferedImage img;
	
	TileType(String name, boolean traversable){
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
	
	private static final TileType[] VALUES = values();
	private static final int SIZE = VALUES.length;
	private static final Random RANDOM = new Random();
	
	public static TileType getRandomTile(){
		return VALUES[RANDOM.nextInt(SIZE)];
	}
}
