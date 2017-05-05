import java.awt.Graphics;
import javax.swing.JPanel;

public class Game extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final int TILE_SIZE = 32; //the pixel size of each tile image
	public static final double TILE_SCALE = 3; //the factor to multiply the size with
	public static final int UNIT = (int)(TILE_SIZE * TILE_SCALE); //Factor to multiply world coordinates into screenspace pixel coordinates

	public TileMap map;
	public InputHandler input;
	
	public Game () {
		map = new TileMap(new int[][] {
				{0,1,2,3,0,1,2,3,0},
				{0,1,2,3,0,1,2,3,0},
				{0,1,2,3,0,1,2,3,0},
				{0,1,2,3,0,1,2,3,0},
				{0,1,2,3,0,1,2,3,0},
				{0,1,2,3,0,1,2,3,0}, 
				{0,1,2,3,0,1,2,3,0},
		});
		
		run();
	}
	
	public void run(){
		/*init();
		while(true){
			update();
		}*/
	}
	
	public void init() {
		input = new InputHandler(this);
	}
	
	public void update(){
		
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.DrawMap(g, 0, 0);
	}
}
