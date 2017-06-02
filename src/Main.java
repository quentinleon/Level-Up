import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;

public class Main {
	
	public static void main(String args[]){
		Game game = new Game();
		JFrame frame = new JFrame("Level-Up"); //Create a new window named "Level-Up"
		frame.setSize(new Dimension(800,600)); //Set it's dimension
		frame.setLocation(new Point(200,200)); //Set it's position
		frame.setContentPane(game.renderer); //Make the game the frame content
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Enable the close button
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH); //maximize frame
		frame.setVisible(true); //Show the frame
	}
	
}
	