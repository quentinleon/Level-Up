import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


 //Dear Mr. B, sorry for making you have to read this mass of rendering code. -QL
 
//Renderer  extends JPanel since it will handle all content drawing
public class Renderer extends JPanel {
	private static final long serialVersionUID = 1L;

	private Game game;
	
	//the color of shadows (alpha channel is the critical one)
	private Color shadow = new Color(0,0,0,120);
			
	//Take a key listener to let the content pane be focusable (clicked on)
	public Renderer(Game game, InputHandler keyListener){
		this.game = game;
		addKeyListener(keyListener);
		setFocusable(true);
	}
	
	//this gets called by renderer.repaint() in game.
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//fill the screen with gray
		g.setColor(new Color(100,100,100));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//get the width and height in tiles of what we're drawing (this is used to only draw what's on screen)
		int drawWidth = (int)(game.camera.getX() + (getWidth()/Game.UNIT) + 2);
		int drawHeight = (int)(game.camera.getY() + (getHeight()/Game.UNIT) + 2);
		
		//call the monster method :D
		//it all has to be in one method, otherwise the way the JPanel works is it won't draw all the methods synchronously
		//this causes weird tearing effects where some objects don't line up with others
		draw(g, game.camera.getX(), game.camera.getY(), drawWidth, drawHeight);
	}
	
	private void draw(Graphics g, double xOffset, double yOffset, int windowWidth, int windowHeight){
		//compute what part of the map needs to be rendered
		int[] xRange = {(int)(xOffset - Game.WALL_HEIGHT), (int)(windowWidth)};
		int[] yRange = {(int)(yOffset), (int)(windowHeight + (Game.WALL_HEIGHT * 1.5) + 1)};
				
		//don't try to draw what's not there (out of bounds)
		if(xRange[0] < 0){xRange[0] = 0;}
		if(xRange[1] >= game.map.getMap().length){xRange[1] = game.map.getMap().length;}
		if(yRange[0] < 0){yRange[0] = 0;}
		if(yRange[1] >= game.map.getMap()[0].length){yRange[1] = game.map.getMap()[0].length;}
		
		//draw base level (tiles of height 0)
		for(int y = yRange[0]; y < yRange[1]; y++){
			for(int x = xRange[0]; x < xRange[1]; x++){
				//if tile is on base level (0)
				if(game.map.getMap()[x][y].getHeight() == 0){
					//get the tile's image
					BufferedImage img = game.map.getMap()[x][y].getImage();
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
			
		final double SHS = 1.2; //shadow height scale (how tall is the shadow)
		final double SWS = .6; //shadow width scale (how wide is the shadow)
		
		//draw shadows 
		for(int y = yRange[0]; y < yRange[1]; y++){
			for(int x = xRange[0]; x < xRange[1]; x++){
				//set draw color to transparent black
				g.setColor(shadow);
				//compute the height for each tile
				int height = (int)(game.map.getMap()[x][y].getHeight() * Game.UNIT);
				if(height > 0){
					//draw the shadow
					int xPos = (int)((x - xOffset) * Game.UNIT);
					int yPos = (int)((y - yOffset) * Game.UNIT);
										
					//draw topShadow
					if((y - 1) >= yRange[0] && game.map.getMap()[x][y-1].getHeight() == 0){
						int[] xPoints = {xPos, xPos + (int)(height*SWS), xPos + Game.UNIT + (int)(height*SWS), xPos + Game.UNIT, xPos};
						int[] yPoints = {yPos, yPos - (int)(height*SHS), yPos - (int)(height*SHS), yPos, yPos};
						Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(polygon);
					}

					//draw sideShadow
					if((x + 1) < xRange[1] && game.map.getMap()[x + 1][y].getHeight() == 0){
						int[] xPoints = {xPos + Game.UNIT, xPos + (int)(height*SWS) + Game.UNIT, xPos + (int)(height*SWS) + Game.UNIT, xPos + Game.UNIT, xPos + Game.UNIT};
						int[] yPoints = {yPos, yPos - (int)(height*SHS), yPos - (int)(height*SHS) + Game.UNIT, yPos + Game.UNIT, yPos};
						Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
						g.fillPolygon(polygon);
					}
				}
			}
		}
		
		//draw walls, mobs, and the player
		//all three of these are done here so that mobs can pass infront of and behind walls
		BufferedImage wall = TileType.wall.getImage();
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
				//compute the height for each tile
				int topHeight = (int)(game.map.getMap()[x][y].getHeight() * Game.UNIT);
				int wallHeight = (int)(game.map.getMap()[x][y].getHeight() + .9);
				if(topHeight > 0){
					BufferedImage img = game.map.getMap()[x][y].getImage();
					if(img != null){
						int xPos = (int)((x - xOffset) * Game.UNIT);
						int yPos = (int)((y - yOffset) * Game.UNIT);
						
						//Fill a rectangle of the correct height with as many wall textures as necessary stacked on top of eachother
						for(int wallSegment = 0; wallSegment < wallHeight; wallSegment++){
							g.drawImage(wall, xPos - 1, yPos - ((wallSegment) * Game.UNIT), xPos + Game.UNIT + 1, (yPos + Game.UNIT) - ((wallSegment) * Game.UNIT),
									0, 0, wall.getWidth(null), wall.getHeight(null), null);
						}
						//draw a ladder on it if it's the exit
						if(game.map.getMap()[x][y].getType() == TileType.ladder){
							for(int wallSegment = 0; wallSegment < wallHeight; wallSegment++){
								g.drawImage(TileType.ladderW.getImage(), xPos - 1, yPos - ((wallSegment) * Game.UNIT), xPos + Game.UNIT + 1, (yPos + Game.UNIT) - ((wallSegment) * Game.UNIT),
										0, 0, wall.getWidth(null), wall.getHeight(null), null);
							}
						}
						
						//draw the top of the wall
						g.drawImage(img, xPos - 1, yPos - topHeight, xPos + Game.UNIT + 1, (yPos + Game.UNIT) - topHeight,
								0, 0, img.getWidth(null), img.getHeight(null), null);
					}
				}
			}
		}
		
		//draw transition cover
		g.setColor(new Color(0,0,0, game.getTransitionAlpha()));
		g.fillRect(0,0, getWidth(), getHeight());
		
		//draw a red screen if the player dies
		if(game.player.isDead()){
			g.setColor(Color.RED);
			g.fillRect(0,0, getWidth(), getHeight());
		}
	}
}
