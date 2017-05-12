import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TileMap {
	private Tile[][] map;
	private Color shadow = new Color(0,0,0,120);
	private Color tempWallColor = new Color(150,150,150);
	
	private boolean drawHeight = true;
	private boolean shadows = true;
	
	public TileMap(int rows, int cols){
		map = new Tile[rows][cols];
		
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if(y == 15 && x % 4 == 2){
						map[x][y] = new Tile(TileType.stone, 1.7);
				} else {
					double height = 0;
					TileType type = TileType.sand;
					map[x][y] = new Tile(type,height);
				}
			}
		}
	}
	
	private int margin = 3;
	public void DrawMap(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		//compute what part of the map needs to be rendered
		int[] xRange = {(int)xOffset - margin, windowWidth + margin};
		int[] yRange = {(int)yOffset - margin, windowHeight + margin};
		
		//don't try to draw what's not there
		if(xRange[0] < 0){xRange[0] = 0;}
		if(xRange[1] > map.length){xRange[0] = map.length;}
		if(yRange[0] < 0){yRange[0] = 0;}
		if(yRange[1] > map[0].length){yRange[0] = map[0].length;}
		
		//draw base level
		for(int y = yRange[0]; y < yRange[1]; y++){
			for(int x = xRange[0]; x < xRange[1]; x++){
				//if tile is on base level (0)
				if(map[x][y].getHeight() == 0 || !drawHeight){
					//get the tile's image
					BufferedImage img = map[x][y].getImage();
					if(img != null){
						//figure out where it should be drawn
						int xPos = (int)((x - xOffset) * Game.UNIT);
						int yPos = (int)((y - yOffset) * Game.UNIT);
						//draw it at the correct position
						g.drawImage(img, xPos, yPos, xPos + Game.UNIT, yPos + Game.UNIT,
								0, 0, img.getWidth(null), img.getHeight(null), null);
					}
				}
			}
		}
		
		//draw mobs
		//draw objects
		
		//draw shadows 
		for(int y = yRange[0]; y < yRange[1]; y++){
			if(!shadows){break;};
			for(int x = xRange[0]; x < xRange[1]; x++){
				//set draw color to transparent black
				g.setColor(shadow);
				//compute the height for each tile
				int height = (int)(map[x][y].getHeight() * Game.UNIT);
				if(height > 0){
					//draw the shadow
					int xPos = (int)((x - xOffset) * Game.UNIT);
					int yPos = (int)((y - yOffset) * Game.UNIT);
					
					//TODO make polygons based on variables, not constants
					
					//draw topShadow
					if((y - 1) >= yRange[0] && map[x][y-1].getHeight() == 0){
						int[] xPoints = {xPos, xPos + (height), xPos + Game.UNIT + (height), xPos + Game.UNIT, xPos};
						int[] yPoints = {yPos, yPos - (int)(height*1.5), yPos - (int)(height*1.5), yPos, yPos};
						Polygon poly = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(poly);
					}

					//draw sideShadow
					if((x + 1) < xRange[1] && map[x + 1][y].getHeight() == 0){
						int[] xPoints2 = {xPos + Game.UNIT, xPos + height + Game.UNIT, xPos + height + Game.UNIT, xPos + Game.UNIT, xPos + Game.UNIT};
						int[] yPoints2 = {yPos, yPos - (int)(height*1.5), yPos - (int)(height*1.5) + Game.UNIT, yPos + Game.UNIT, yPos};
						Polygon sideShadow = new Polygon(xPoints2, yPoints2, xPoints2.length);
						g.fillPolygon(sideShadow);
					}
				}
			}
		}
		
		//draw walls and top levels
		for(int y = yRange[0]; y < yRange[1]; y++){
			if(!drawHeight){break;};
			for(int x = xRange[0]; x < xRange[1]; x++){
				//set draw color to transparent black
				g.setColor(tempWallColor);
				//compute the height for each tile
				int height = (int)(map[x][y].getHeight() * Game.UNIT);
				if(height > 0){
					BufferedImage img = map[x][y].getImage();
					if(img != null){
						int xPos = (int)((x - xOffset) * Game.UNIT);
						int yPos = (int)((y - yOffset) * Game.UNIT);
						//draw wall
						g.fillRect(xPos, (yPos + Game.UNIT) - height, Game.UNIT, height);
						//draw the top of the wall
						g.drawImage(img, xPos, yPos - height, xPos + Game.UNIT, (yPos + Game.UNIT) - height,
								0, 0, img.getWidth(null), img.getHeight(null), null);
					}
				}
			}
		}
		
		//draw lights?
		
	}
}
