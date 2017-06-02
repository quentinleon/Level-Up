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
		Scanner fileInput = null;
		
		if(g.player == null){
			g.player = new Player(g);
		}
		
		try{
			//load map and player position
			img = ImageIO.read(new File("levels/" + level +"/map.png"));
			processImage(img, g);
			
			//load enemies
			g.enemies = new ArrayList<Mob>();
			fileInput = new Scanner(new File("levels/" + level +"/enemies.dat"));
			
			while(fileInput.hasNext()){
				String line = fileInput.nextLine();
				if(line.length() > 0 && line.charAt(0) != '#'){
					processLine(line, g);
				}
			}
			return true;
		} catch(IOException e){
			System.out.println("Could not load level -" + level + "!");
			return false;
		} finally {
			try{
				fileInput.close();
			} catch (Exception e) {

			}
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
		} else if(input.charAt(0) == 'e'){
			double x = Double.parseDouble(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			double y = Double.parseDouble(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			String type = input.substring(input.indexOf(')') + 1);
			Enemy enemy = new Enemy(g, g.player, type);
			enemy.setPosition(x,y);
			g.enemies.add(enemy);
		}
	
	}
	
	private static void processImage(BufferedImage img, Game g){
		int width = img.getWidth();
		int height = img.getHeight();
		
		g.map = new TileMap(width,height);
		
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	        	Color tileColor = new Color(img.getRGB(x, y), true);
	        		        	
	        	double tileHeight = tileColor.equals(Color.WHITE) ? Game.WALL_HEIGHT : 0;
	        	TileType type = tileColor.equals(Color.WHITE) ? TileType.stone : TileType.floor;
	        	
	        	if(tileHeight == 0 && tileColor.getGreen() == 255){
	        		g.player.setPosition(x, y);
	        		if(g.getLevel() != Game.START_LEVEL){
	        			type = TileType.trapDoor;
	        		}
	        	}
	        	
	        	if(tileHeight == 0 && tileColor.getRed() == 255){
	        		type = TileType.ladder;
	        		tileHeight = Game.WALL_HEIGHT;
	        		g.player.setExit(x, y);
	        	}
	        	
	        	//if we're on ground level, custom loading parameters
	        	if(g.getLevel() == 0 && type != TileType.trapDoor){
	        		type = tileColor.equals(Color.WHITE) ? TileType.sand : TileType.grass;
	        		tileHeight = 0;
	        	}

	        	g.map.setTile(x, y, type, tileHeight);

	        }
	    }
	}
	
}
