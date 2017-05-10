import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TileMap {
	private Tile[][] map;
	
	public TileMap(int rows, int cols){
		map = new Tile[rows][cols];
		
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[0].length; x++){
				map[y][x] = new Tile(TileType.getRandomTile());
			}
		}
	}
	
	public void DrawMap(Graphics g, double xOffset, double yOffset){
				
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[0].length; x++){
				
				BufferedImage img = map[y][x].getImage();
						
				//convert world us to pixel Game.UNIT;s
				int xPos = (int)((x + xOffset) * Game.UNIT);
				int yPos = (int)((y + yOffset) * Game.UNIT);
				
				//TODO use different drawImage that doesn't need last 4 params
				g.drawImage(img, 
						xPos, 
						yPos, 
						xPos + Game.UNIT, 
						yPos + Game.UNIT,
						0,
						0,
						img.getWidth(),
						img.getHeight(),
						null);
				
			}
		}
	}
}
