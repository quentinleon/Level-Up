import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class MapLoader {		
	
	public static boolean loadSave(String path, Game g){
		System.out.println("Loading " + path);
		Scanner fileInput = null;
		try{
			fileInput = new Scanner(new File("saves/" + path));
			while(fileInput.hasNext()){
				String line = fileInput.nextLine();
				if(line.length() > 0 && line.charAt(0) != '#'){
					processLine(line, g);
				}
			}
			return true;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}finally {
			try{
				fileInput.close();
			} catch (Exception e) {

			}
		}
	}
	
	public static boolean loadLevel(String level, Game g){
		System.out.println("Loading level -" + level);
		BufferedImage tiles = null;
		BufferedImage zones = null;
		try{
			tiles = ImageIO.read(new File("levels/t" + level +".png"));
			zones = ImageIO.read(new File("levels/d" + level +".png"));
			processImage(tiles, zones, g);
			return true;
		} catch(IOException e){
			System.out.println("Could not load level -" + level + "!");
			return false;
		}
	}

	private static void processLine(String input, Game g){
		if(input.charAt(0) == '*'){
			int width = Integer.parseInt(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			int height = Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			g.map = new TileMap(width,height);
		}
		else if(input.charAt(0) == 't'){
			int x = Integer.parseInt(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			int y = Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf(':')));
			double height = Double.parseDouble(input.substring(input.indexOf(':') + 1, input.indexOf(')')));
			TileType tileType = TileType.fromString(input.substring(input.indexOf(')') + 1));
			g.map.setTile(x, y, tileType, height);
		}
		else if(input.charAt(0) == 'p'){
			double x = Double.parseDouble(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			double y = Double.parseDouble(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			g.player = new Player(g, x, y);
		}
	
	}
	
	private static final double WALL_HEIGHT = 1.7;
	private static void processImage(BufferedImage tiles, BufferedImage data, Game g){
		int width = tiles.getWidth();
		int height = tiles.getHeight();
		
		g.map = new TileMap(width,height);
		g.player = new Player(g);
		
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	        	Color t = new Color(tiles.getRGB(x, y), true);
	        	Color d = new Color(data.getRGB(x, y), true);
	        		        	
	        	double tileHeight = d.equals(Color.WHITE) ? WALL_HEIGHT : 0;
	        	g.map.setTile(x, y, TileType.fromColor(t), tileHeight);
	        	
	        	if(tileHeight == 0 && d.getGreen() == 255){
	        		g.player.setPosition(x, y);;
	        	}

	        }
	    }
	}
	
}
