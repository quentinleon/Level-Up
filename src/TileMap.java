import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TileMap {
	private Tile[][] map;
	private Color shadow = new Color(0,0,0,100);
	
	public TileMap(int rows, int cols){
		map = new Tile[rows][cols];
		
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if(y == 3){
					map[x][y] = new Tile(TileType.none, 0);
				}
				if(y == 4 && x == 5){
						map[x][y] = new Tile(TileType.stoneWall, 1.7);
				} else {
					double height = 0;
					TileType type = TileType.getRandomTile();
					map[x][y] = new Tile(type,height);
				}
			}
		}
	}
	
	private int margin = 3;
	public void DrawMap(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if((x > xOffset - margin && y > yOffset - margin && x < windowWidth + margin && y < windowHeight + margin)){ //only draw what's on screen	
					
					BufferedImage img = map[x][y].getImage();
					if(img != null){
							
						//convert world us to pixel Game.UNIT;s
						int xPos = (int)((x - xOffset) * Game.UNIT);
						int yPos = (int)((y - yOffset) * Game.UNIT);
						
						int height = (int)(map[x][y].getHeight() * Game.UNIT);
						
						//TODO use different drawImage that doesn't need last 4 params
						
						if(height > 0){
							//draw shadow
							g.setColor(shadow);
							int[] xPoints = {xPos, xPos + (height), xPos + Game.UNIT + (height), xPos + Game.UNIT, xPos};
							int[] yPoints = {yPos, yPos - (int)(height*1.5), yPos - (int)(height*1.5), yPos, yPos};
							Polygon poly = new Polygon(xPoints, yPoints, xPoints.length);
							g.fillPolygon(poly);
							//draw wall
							g.setColor(new Color(150,150,150));
							g.fillRect(xPos, (yPos + Game.UNIT) - height, Game.UNIT, height);
						}
						
						//don't use negative since it draws on tile above it
						if(height < 0){
							g.setColor(new Color(50,50,50));
							g.fillRect(xPos, yPos, Game.UNIT, Math.abs(height));
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
}
