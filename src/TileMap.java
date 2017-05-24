import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TileMap {
	int width;
	int height;
	private Tile[][] map;
	private Color shadow = new Color(0,0,0,120);
	private Color tempWallColor = new Color(150,150,150);
	
	private double wallHeight = 1.7;
	
	public TileMap(int cols, int rows){
		map = new Tile[cols][rows];
		width = cols;
		height = rows;
	}
	
	public void makeTestMap(){
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if(y == 8 && x % 4 == 2){
						map[x][y] = new Tile(TileType.stone, x/8.0 + .3);
				} else {
					double height = 0;
					TileType type = TileType.sand;
					map[x][y] = new Tile(type,height);
				}
			}
		}
	}
	
	public Tile getTile(int x, int y){
		return map[x][y];
	}
	
	public int[] getSize(){
		int[] size = {map.length, map[0].length};
		return size;
  }
  
  public Tile[][] getMap(){
		return map;
	}
	
	public void drawMap(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		//compute what part of the map needs to be rendered
		int[] xRange = {(int)xOffset, windowWidth};
		int[] yRange = {(int)yOffset, windowHeight};
		
		//don't try to draw what's not there
		if(xRange[0] < 0){xRange[0] = 0;}
		if(xRange[1] >= map.length){xRange[1] = map.length;}
		if(yRange[0] < 0){yRange[0] = 0;}
		if(yRange[1] >= map[0].length){yRange[1] = map[0].length;}
		
		//draw base level
		for(int y = yRange[0]; y < yRange[1]; y++){
			for(int x = xRange[0]; x < xRange[1]; x++){
				//if tile is on base level (0)
				if(map[x][y].getHeight() == 0){
					//get the tile's image
					BufferedImage img = map[x][y].getImage();
					if(img != null){
						//figure out where it should be drawn
						int xPos = (int)((x - xOffset) * Game.UNIT);
						int yPos = (int)((y - yOffset) * Game.UNIT);
						//draw it at the correct position
						g.drawImage(img, xPos, yPos, xPos + Game.UNIT + 1, yPos + Game.UNIT + 1,
								0, 0, img.getWidth(null), img.getHeight(null), null);
					}
				}
			}
		}
	}
	
	public void drawShadows(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		//compute what part of the map needs to be rendered
		int[] xRange = {(int)(xOffset - wallHeight), (int)(windowWidth)};
		int[] yRange = {(int)(yOffset), (int)(windowHeight + (wallHeight * 1.5) + 1)};
		
		//don't try to draw what's not there
		if(xRange[0] < 0){xRange[0] = 0;}
		if(xRange[1] >= map.length){xRange[1] = map.length;}
		if(yRange[0] < 0){yRange[0] = 0;}
		if(yRange[1] >= map[0].length){yRange[1] = map[0].length;}
		
		//draw shadows 
		for(int y = yRange[0]; y < yRange[1]; y++){
			for(int x = xRange[0]; x < xRange[1]; x++){
				//set draw color to transparent black
				g.setColor(shadow);
				//compute the height for each tile
				int height = (int)(map[x][y].getHeight() * Game.UNIT);
				if(height > 0){
					//draw the shadow
					int xPos = (int)((x - xOffset) * Game.UNIT);
					int yPos = (int)((y - yOffset) * Game.UNIT);
					
					//TODO make polygons based on variables, not constants.
					
					//draw topShadow
					if((y - 1) >= yRange[0] && map[x][y-1].getHeight() == 0){
						int[] xPoints = {xPos, xPos + (height), xPos + Game.UNIT + (height), xPos + Game.UNIT, xPos};
						int[] yPoints = {yPos, yPos - (int)(height*1.5), yPos - (int)(height*1.5), yPos, yPos};
						Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(polygon);
					}

					//draw sideShadow
					if((x + 1) < xRange[1] && map[x + 1][y].getHeight() == 0){
						int[] xPoints = {xPos + Game.UNIT, xPos + height + Game.UNIT, xPos + height + Game.UNIT, xPos + Game.UNIT, xPos + Game.UNIT};
						int[] yPoints = {yPos, yPos - (int)(height*1.5), yPos - (int)(height*1.5) + Game.UNIT, yPos + Game.UNIT, yPos};
						Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(polygon);
					}
				}
			}
		}
	}
	
	public void drawWalls(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		//compute what part of the map needs to be rendered
		int[] xRange = {(int)(xOffset), (int)(windowWidth)};
		int[] yRange = {(int)(yOffset), (int)(windowHeight + wallHeight + 1)};
		
		//don't try to draw what's not there
		if(xRange[0] < 0){xRange[0] = 0;}
		if(xRange[1] >= map.length){xRange[1] = map.length;}
		if(yRange[0] < 0){yRange[0] = 0;}
		if(yRange[1] >= map[0].length){yRange[1] = map[0].length;}
		
		for(int y = yRange[0]; y < yRange[1]; y++){
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
						g.fillRect(xPos - 1, (yPos + Game.UNIT) - height, Game.UNIT + 2, height + 1);
						//draw the top of the wall
						g.drawImage(img, xPos - 1, yPos - height, xPos + Game.UNIT + 1, (yPos + Game.UNIT) - height,
								0, 0, img.getWidth(null), img.getHeight(null), null);
					}
				}
			}
		}
	}
}
