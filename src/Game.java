import java.awt.Graphics;
import javax.swing.JPanel;

public class Game extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final int TILE_SIZE = 32; //the pixel size of each tile image
	public static final int TILE_SCALE = 3; //the factor to multiply the size with
	public static final int UNIT = TILE_SIZE * TILE_SCALE; //Factor to multiply world coordinates into screenspace pixel coordinates

	private TileMap map;
	
	public Game () {
		map = new TileMap(new int[][] {
				{0,1,2,3,0,1,2,3}	
		});
	}
	
	@Override public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.DrawMap(g, 0, 0);
	}
}
