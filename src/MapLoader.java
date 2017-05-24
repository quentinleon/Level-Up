import java.io.File;
import java.io.IOException;
import java.util.*;

public class MapLoader {		
	
	public static boolean loadSave(String path, Game g){
		Scanner fileInput = null;
		try{
			fileInput = new Scanner(new File("saves/" + path));
			
			String headerLine = fileInput.nextLine();
			int width = Integer.parseInt(headerLine.substring(headerLine.indexOf('(') + 1, headerLine.indexOf(',')));
			int height = Integer.parseInt(headerLine.substring(headerLine.indexOf(',') + 1, headerLine.indexOf(')')));
			g.map = new TileMap(width,height);
			
			while(fileInput.hasNext()){
				String line = fileInput.nextLine();
				process(line, g);
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
	

	private static void process(String input, Game g){
		if(input.length() > 0 && input.charAt(0) == 't'){
			int x = Integer.parseInt(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			int y = Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf(':')));
			double height = Double.parseDouble(input.substring(input.indexOf(':') + 1, input.indexOf(')')));
			TileType tileType = TileType.fromString(input.substring(input.indexOf(')') + 1));
			g.map.setTile(x, y, tileType, height);
		}
		if(input.length() > 0 && input.charAt(0) == 'p'){
			double x = Double.parseDouble(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			double y = Double.parseDouble(input.substring(input.indexOf(',') + 1, input.indexOf(')')));
			g.player = new Player(g, x, y);
		}
	
	}
	
}
