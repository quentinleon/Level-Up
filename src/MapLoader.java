import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class MapLoader {		
	
	//Load a text based save file into the game from a path
	public static boolean loadSave(String path, Game g){
		System.out.println("Loading " + path);
		Scanner fileInput = null;
		try{
			//open a file reader at the selected path
			fileInput = new Scanner(new File("saves/" + path));
			while(fileInput.hasNext()){
				String line = fileInput.nextLine();
				if(line.length() > 0 && line.charAt(0) != '#'){
					//process each line that doesn't start with a '#'
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
				//close our file reader
				fileInput.close();
			} catch (Exception e) {

			}
		}
	}
	
	//Load a level (an image and enemy data) into the game
	public static boolean loadLevel(String level, Game g){
		System.out.println("Loading level -" + level);
		BufferedImage img = null;
		Scanner fileInput = null;
		
		//create a player if we don't gave one
		if(g.player == null){
			g.player = new Player(g);
		}
		
		//TEMP, reset player health before boss room
		if(g.getLevel() == 1){
			g.player.resetHealth();
		}
		
		try{
			//load map, player start, and exit
			g.enemies = new ArrayList<Mob>(); //temp
			img = ImageIO.read(new File("levels/" + level +"/map.png"));
			processImage(img, g);
			
			//load enemies
			//g.enemies = new ArrayList<Mob>();
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

	//process a line of text and build the corresponing object
	private static void processLine(String input, Game g){
		//lines deliniated with an asterix are the size of the map
		if(input.charAt(0) == '*'){
			int width = Integer.parseInt(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			int height = Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			g.map = new TileMap(width,height);
		}
		//lines starting with a t are tiles
		else if(input.charAt(0) == 't'){
			int x = Integer.parseInt(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			int y = Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf(':')));
			double height = Double.parseDouble(input.substring(input.indexOf(':') + 1, input.indexOf(')')));
			TileType tileType = TileType.fromString(input.substring(input.indexOf(')') + 1));
			g.map.setTile(x, y, tileType, height);
		}
		//p is the player
		else if(input.charAt(0) == 'p'){
			double x = Double.parseDouble(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			double y = Double.parseDouble(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			g.player = new Player(g, x, y);
		}
		//e is the enemies
		else if(input.charAt(0) == 'e'){
			double x = Double.parseDouble(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			double y = Double.parseDouble(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			String type = input.substring(input.indexOf(')') + 1);
			Enemy enemy = new Enemy(g, g.player, type);
			enemy.setPosition(x,y);
			g.enemies.add(enemy);
		}
	
	}
	
	//process and image and build a map from it
	private static void processImage(BufferedImage img, Game g){
		int width = img.getWidth();
		int height = img.getHeight();
		
		g.map = new TileMap(width,height);
		
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	        	//get the color from one pixel at a time
	        	Color tileColor = new Color(img.getRGB(x, y), true);
	        		   
	        	//set the height to wall height if the pixel is white, otherwise set it to zero
	        	double tileHeight = tileColor.equals(Color.WHITE) ? Game.WALL_HEIGHT : 0;
	        	//set the tile type to stone if the pixel is white, otherwise set it to floor material
	        	TileType type = tileColor.equals(Color.WHITE) ? TileType.stone : TileType.floor;
	        	
	        	//if the pixel is on ground level and green, that's the player's start point
	        	if(tileHeight == 0 && tileColor.getGreen() == 255){
	        		g.player.setPosition(x, y);
	        		//if it's not the first level, show a trapDoor
	        		if(g.getLevel() != Game.START_LEVEL){
	        			type = TileType.trapDoor;
	        		}
	        	}
	        	
	        	//if the pixel is on ground level and red, that's the exit from the level
	        	if(tileHeight == 0 && tileColor.getRed() == 255){
	        		type = TileType.ladder;
	        		tileHeight = Game.WALL_HEIGHT;
	        		g.player.setExit(x, y);
	        	}
	        	
	        	//if the pixel is on ground level and blue, spawn an enemy (Enemy takes care of setting it's difficulty based on level)
	        	if(tileHeight == 0 && tileColor.getBlue() == 255){
	        		Enemy e = new Enemy(g, g.player);
	        		e.setPosition(x, y);
	        		g.enemies.add(e);
	        	}
	        	
	        	//if we're on level 0 (finished the dungeon), load grass and sand instead of stones
	        	if(g.getLevel() == 0 && type != TileType.trapDoor){
	        		type = tileColor.equals(Color.WHITE) ? TileType.sand : TileType.grass;
	        		tileHeight = 0;
	        	}

	        	//finally set the tile
	        	g.map.setTile(x, y, type, tileHeight);

	        }
	    }
	}
	
}
