import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Renderer extends JPanel {
	private static final long serialVersionUID = 1L;

	private Tile[][] map;	
	private Game game;
	
	private Color shadow = new Color(0,0,0,120);
	private Color tempWallColor = new Color(150,150,150); //TODO replace with wall texture :D
	
	private double wallHeight = 1.7;
	
	private boolean debug = true;
	
	public Renderer(Game game, InputHandler keyListener){
		this.game = game;
		map = game.map.getMap();
		addKeyListener(keyListener);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(new Color(100,100,100));
		
		g.fillRect(0, 0, getWidth(), getHeight());
		int drawWidth = (int)(game.camera.getX() + (getWidth()/Game.UNIT) + 2);
		int drawHeight = (int)(game.camera.getY() + (getHeight()/Game.UNIT) + 2);
		
		draw(g, game.camera.getX(), game.camera.getY(), drawWidth, drawHeight);
	}
	
	private void draw(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		//compute what part of the map needs to be rendered
		int[] xRange = {(int)(xOffset - wallHeight), (int)(windowWidth)};
		int[] yRange = {(int)(yOffset), (int)(windowHeight + (wallHeight * 1.5) + 1)};
				
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
							if(game.debug){
								g.setColor(Color.RED);
								g.fillOval(xPos-2, yPos-2, 4, 4);
							}
						}
					}
				}
			}
			
		final double SHS = 1.2; //shadow height scale
		final double SWS = .6; //shadow width scale
		
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
										
					//draw topShadow
					if((y - 1) >= yRange[0] && map[x][y-1].getHeight() == 0){
						int[] xPoints = {xPos, xPos + (int)(height*SWS), xPos + Game.UNIT + (int)(height*SWS), xPos + Game.UNIT, xPos};
						int[] yPoints = {yPos, yPos - (int)(height*SHS), yPos - (int)(height*SHS), yPos, yPos};
						Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(polygon);
					}

					//draw sideShadow
					if((x + 1) < xRange[1] && map[x + 1][y].getHeight() == 0){
						int[] xPoints = {xPos + Game.UNIT, xPos + (int)(height*SWS) + Game.UNIT, xPos + (int)(height*SWS) + Game.UNIT, xPos + Game.UNIT, xPos + Game.UNIT};
						int[] yPoints = {yPos, yPos - (int)(height*SHS), yPos - (int)(height*SHS) + Game.UNIT, yPos + Game.UNIT, yPos};
						Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(polygon);
					}
				}
			}
		}
		
		//draw walls and mobs?
		for(int y = yRange[0]; y < yRange[1]; y++){
			for(int x = xRange[0]; x < xRange[1]; x++){
				for(Mob mob : game.enemies){
					if((int)(mob.getY()) == y){
						mob.draw(g, game.camera);
					}
				}
				if((int)(game.player.getY()) == y){
					game.player.draw(g, game.camera);
				}
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
