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
	
	public void DrawMap(Graphics g){
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[0].length; x++){
				
				int index = map[y][x];
				int yOffset = 0;
				
				if(index > (tileSheet.getWidth() / Engine.TILE_WIDTH) - 1){
					yOffset ++;
					index = index - (tileSheet.getWidth() / Engine.TILE_WIDTH);
				}
				
				g.drawImage(tileSheet, 
						x * Engine.TILE_WIDTH,
						y * Engine.TILE_HEIGHT,
						(x * Engine.TILE_WIDTH) + Engine.TILE_WIDTH,
						(y * Engine.TILE_HEIGHT) + Engine.TILE_HEIGHT,
						index * Engine.TILE_WIDTH,
						yOffset * Engine.TILE_HEIGHT,
						(index * Engine.TILE_WIDTH) + Engine.TILE_WIDTH,
						(yOffset * Engine.TILE_HEIGHT) + Engine.TILE_HEIGHT,
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
