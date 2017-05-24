import java.io.File;
import java.io.IOException;
import java.util.*;

public class MapLoader {
	
	Scanner fileInput = null;
	
	private double x;
	private double y;
	private double height;
	private TileType tileType;
	private Game game;
	
	public MapLoader(Game g){
		game = g;
	}
	
	public void readMap(){
		try{
			fileInput = new Scanner(new File(" "));
			String headerLine = fileInput.nextLine();
			while(fileInput.hasNext()){
				String line = fileInput.nextLine();
				process(line);
				
			}
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}finally {
			try{
				fileInput.close();
			} catch (Exception e) {
				
			}
		}
	}
	

	public void process(String input){
		if(input.charAt(0) == 't'){
			x = Double.parseDouble(input.substring(input.indexOf('(') + 1, input.indexOf(',')));
			y = Double.parseDouble(input.substring(input.indexOf(',') + 1, input.indexOf(':')));
			height = Double.parseDouble(input.substring(input.indexOf(':') + 1, input.indexOf(')')));
			tileType = TileType.fromString(input.substring(input.indexOf(')') + 1));
		}
	
	}
	
}
