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
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("levels/" + level +"/map.png"));
			processImage(img, g);
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
	private static void processImage(BufferedImage img, Game g){
		int width = img.getWidth();
		int height = img.getHeight();
		
		g.map = new TileMap(width,height);
		g.player = new Player(g);
		
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	        	Color tileColor = new Color(img.getRGB(x, y), true);
	        		        	
	        	double tileHeight = tileColor.equals(Color.WHITE) ? WALL_HEIGHT : 0;
	        	g.map.setTile(x, y, TileType.fromColor(tileColor), tileHeight);
	        	
	        	if(tileHeight == 0 && tileColor.getGreen() == 255){
	        		g.player.setPosition(x, y);;
	        	}

	        }
	    }
	}
	
}
