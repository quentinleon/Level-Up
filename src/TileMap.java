import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileMap {
	private int[][] map;
	private BufferedImage tileSheet;
	
	public TileMap(int[][] existingMap){
		map = new int[existingMap.length][existingMap[0].length];
		
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[0].length; x++){
				map[y][x] = existingMap[y][x];
			}
		}
		
		tileSheet = LoadTileSheet("tileSheet.jpg");
		
	}
	
	public void DrawMap(Graphics g, double xOffset, double yOffset){
				
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[0].length; x++){
				
				//BufferedImage img = <get the tile img>;
				
				int index = map[y][x];
				int yIndex = 0;
				
				if(index > (tileSheet.getWidth() / Game.TILE_SIZE) - 1){
					yIndex ++;
					index = index - (tileSheet.getWidth() / Game.TILE_SIZE);
				}
						
				//convert world us to pixel Game.UNIT;s
				int xPos = (int)((x + xOffset) * Game.UNIT);
				int yPos = (int)((y + yOffset) * Game.UNIT);
				
				g.drawImage(tileSheet, 
						xPos, 
						yPos, 
						xPos + Game.UNIT, 
						yPos + Game.UNIT,
						index * Game.TILE_SIZE,
						yIndex * Game.TILE_SIZE,
						(index * Game.TILE_SIZE) + Game.TILE_SIZE,
						(yIndex * Game.TILE_SIZE) + Game.TILE_SIZE,
						null);
				
			}
		}
	}
	
	public BufferedImage LoadTileSheet(String fileName){
		BufferedImage img = null;
		
		try{
			img = ImageIO.read(new File(fileName));
		} catch(IOException e){
			System.out.println("Could not load TileSheet!");
		}
		
		return img;
	}
	
}
