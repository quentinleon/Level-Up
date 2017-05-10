import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class TileMap {
	private Tile[][] map;
	
	public TileMap(int rows, int cols){
		map = new Tile[rows][cols];
		
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if(y == 3 || y == 4){
					map[x][y] = new Tile(TileType.stoneWall, .7);
				} else {
					double height = 0;
					TileType type = TileType.getRandomTile();
					if(type == TileType.water){
						height = -.1;
					}
					map[x][y] = new Tile(type,height);
				}
			}
		}
	}
	
	int margin = 2;
	public void DrawMap(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if((x > xOffset - margin && y > yOffset - margin && x < windowWidth + margin && y < windowHeight + margin)){ //only draw what's on screen							
					//convert world us to pixel Game.UNIT;s
					int xPos = (int)((x - xOffset) * Game.UNIT);
					int yPos = (int)((y - yOffset) * Game.UNIT);
					
					int height = (int)(map[x][y].getHeight() * Game.UNIT);
					Image img = map[x][y].getImage();
					
					//TODO use different drawImage that doesn't need last 4 params
					
					if(height > 0){
						g.setColor(new Color(50,50,50));
						g.fillRect(xPos, (yPos + Game.UNIT) - height, Game.UNIT, height);
					}
					if(height < 0){
						int h = Math.abs(height);
						g.setColor(new Color(50,50,50));
						g.fillRect(xPos, yPos, Game.UNIT, h);
					}
					
					g.drawImage(img, 
							xPos, 
							yPos - height, 
							xPos + Game.UNIT, 
							(yPos + Game.UNIT) - height,
							0,
							0,
							img.getWidth(null),
							img.getHeight(null),
							null);
				}
			}
		}
	}
}
