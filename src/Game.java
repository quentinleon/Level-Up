import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;

public class Game implements Runnable {	
	public static final int TILE_SIZE = 16; //the pixel size of each tile image
	public static final double TILE_SCALE = 5; //the factor to multiply the size with
	public static final int UNIT = (int)(TILE_SIZE * TILE_SCALE); //Factor to multiply world coordinates into screenspace pixel coordinates
	public static final double WALL_HEIGHT = 1.3;
	public static final int START_LEVEL = 20;
	
	public TileMap map;
	public Player player;
	public ArrayList<Mob> enemies;
	public Camera camera;
	public Renderer renderer;
	public boolean debug = false;
	
	private int blackAlpha = 500;
	private boolean loadingNext = false;
	
	private int level = START_LEVEL;
	private int[] goal;
	
	public Game () {
		//if we can't load the map, load a default map
		if(MapLoader.loadLevel(Integer.toString(level), this) == false){
			enemies = new ArrayList<Mob>();
			map = new TileMap(100,100);	
			map.makeTestMap();
			
			player = new Player(this, 10, 5);
	
			Enemy e1 = new Enemy(this, player, "fireMonster", 1, 1, 3);
			Enemy e2 = new Enemy(this, player, "piskel", 1, 1, 4);
			enemies.add(e1);
			enemies.add(e2);
		}
		
		//need to do this in game	
		renderer = new Renderer(this, player.input.getKeyListener());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		camera = new Camera(player);
		camera.setXOffset((screenSize.getWidth()/2) / (Game.UNIT) - (.5));
		camera.setYOffset((screenSize.getHeight()/2) / Game.UNIT - (.5));
		
		new Thread(this).start();
	}
	
	public void run(){
		while(true){
			update();
		}
	}
	
	public void loadNextLevel(){
		if(!loadingNext){
			level--;
			loadingNext = true;
		}
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getTransitionAlpha(){
		return blackAlpha <= 255 ? blackAlpha : 255;
	}
	
	long lastTime = System.currentTimeMillis();
	double counter = 0;
	
	public void update(){
		long nowTime = System.currentTimeMillis();
		counter += (nowTime - lastTime);
		lastTime = nowTime;
		
		if(counter >= 1000/60){
			counter = 0;
			player.update();
			camera.update();
			for(Mob mob : enemies){
				mob.update();
			}
			
			if(loadingNext){
				blackAlpha += 255/60;
				if(blackAlpha >= 255){
					if(!MapLoader.loadLevel(Integer.toString(level), this)){
						System.out.println("Failed to load level -" + level);
					}
					loadingNext = false;
				}
			} else {
				blackAlpha -= 255/60;
				if(blackAlpha < 0){
					blackAlpha = 0;
				}
			}
			
		}
		
		renderer.repaint();
	}
}
