import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MapSaver {
		//t(xPos,yPos:height)tileType
	public static void save(String path, Game game){
		String nl = System.getProperty("line.separator"); //get system newline
	
		BufferedWriter writer = null;
		
		try{
			File file = new File(path);
			writer = new BufferedWriter(new FileWriter(file));
			
			TileMap map = game.map;
			Player player = game.player;
			
			String str;
			str = "p(" + player.getX() + "," + player.getY() + ")" + nl;
			writer.write(str + nl);
			
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
