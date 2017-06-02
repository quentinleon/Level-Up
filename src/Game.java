import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Dimension;

public class Game implements Runnable {	
	public static final int TILE_SIZE = 16; //the pixel size of each tile image
	public static final double TILE_SCALE = 5; //the factor to multiply the size with
	public static final int UNIT = (int)(TILE_SIZE * TILE_SCALE); //Factor to multiply world coordinates into screenspace pixel coordinates
	public static final double WALL_HEIGHT = 1.3; //Height of all walls
	public static final int START_LEVEL = 5; //The level we start on
	
	public TileMap map; 
	public Player player;
	public ArrayList<Mob> enemies;
	public Camera camera;
	public Renderer renderer;
	public boolean debug = false; //Should we draw debug functions on the screen? (colliders, health bars, stats)
	
	private int blackAlpha = 500; //The alpha channel (opacity) of the transition screen
	private boolean loadingNext = false; //are we loading the next level?
	
	private int level; //the level we are currently on
	
	public Game () {
		//create all our objects used on a per level basis
		restart();
		
		//create objects that are used on all levels
		renderer = new Renderer(this, player.getInput().getKeyListener());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		camera = new Camera(player);
		camera.setXOffset((screenSize.getWidth()/2) / (Game.UNIT) - (.5));
		camera.setYOffset((screenSize.getHeight()/2) / Game.UNIT - (.5));
		
		//start a threaded process (runs the run() method)
		new Thread(this).start();
	}
	
	//reset the game and loads into the first level
	public void restart(){
		level = START_LEVEL;
		blackAlpha = 500;
		
		if(player != null){
			player.resetHealth();
		}
		
		MapLoader.loadLevel(Integer.toString(level), this);
	}
	
	//Main game loop
	public void run(){
		while(true){
			update();
		}
	}
	
	public int getLevel(){
		return level;
	}
	
	//set state to loading so that the fade animation plays
	public void loadNextLevel(){
		if(!loadingNext){
			level--;
			loadingNext = true;
		}
	}

	//get the opacity of the fade screen (clamped to 255 (max value))
	public int getTransitionAlpha(){
		return blackAlpha <= 255 ? blackAlpha : 255;
	}
	
	
	long lastTime = System.currentTimeMillis();
	double counter = 0;
	
	//running as fast as possible
	public void update(){
		long nowTime = System.currentTimeMillis();
		counter += (nowTime - lastTime);
		lastTime = nowTime;
		
		//use System time to run all object updates 60 times a second
		if(counter >= 1000/60){
			counter = 0;
			player.update();
			camera.update();
			for(Mob mob : enemies){
				mob.update();
			}
			
			//if we are loading the next level
			if(loadingNext){
				//increase the alpha until the screen is fully opaque
				blackAlpha += 255/60;
				if(blackAlpha >= 255){
					//then load the level
					if(!MapLoader.loadLevel(Integer.toString(level), this)){
						System.out.println("Failed to load level -" + level);
					}
					//set the state to no longer loading the level
					loadingNext = false;
				}
			} else {
				//this will cause the alpha to decrease until it is fully transparent
				blackAlpha -= 255/60;
				if(blackAlpha < 0){
					blackAlpha = 0;
				}
			}
			
		}
		
		//render call
		renderer.repaint();
	}
}
