import java.awt.Graphics;
import javax.swing.JPanel;

public class Engine extends JPanel {

	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;

	private TileMap map;
	
	public Engine () {
		map = new TileMap(new int[][] {
				{1,1,1,1,1,1,}	
		});
	}
	
	@Override public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.DrawMap(g);
	}
}
