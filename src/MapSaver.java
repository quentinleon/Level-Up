import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

public class MapSaver {
		//t(xPos,yPos:height)tileType
	public static void save(String path, Game game){
		System.out.println("Saving " + path);
		String nl = System.getProperty("line.separator"); //get system newline
	
		BufferedWriter writer = null;
		
		try{
			File file = new File("saves/" + path);
			writer = new BufferedWriter(new FileWriter(file));
			String str;
			
			writer.write("#this is a comment");
			
			TileMap map = game.map;
			Player player = game.player;
			ArrayList<Mob> enemies = game.enemies;

			//save map size
			int[] mapSize = map.getSize();
			writer.write("# map size");
			str = "*(" + mapSize[0] + "," + mapSize[1] + ")" + nl;
			writer.write(str + nl);
			
			//save the player
			writer.write("# player");
			str = "p(" + player.getX() + "," + player.getY() + ")" + nl;
			writer.write(str + nl);
			
			//TODO save mobs
			writer.write("# mobs" + nl);
			for(Mob enemy : enemies){
				str = "p(" + enemy.getX() + "," + enemy.getY() + ")" + enemy.getName() + nl;
				writer.write(str);
			}
			writer.write(nl);
			
			//save the tiles
			writer.write("# tiles");
			for(int y = 0; y < map.height; y++){
				for(int x = 0; x < map.width; x++){
					Tile t = map.getTile(x, y);
					str = "t(" + x + "," + y + ":" + t.getHeight() +")" + t.getType() + nl;
					writer.write(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				writer.close();
			} catch (Exception e) {
				
			}
		}
	}
}
