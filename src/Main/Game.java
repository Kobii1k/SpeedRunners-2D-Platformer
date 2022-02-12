package Main;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

//Classe main, lancement au boot, fenetre et Frame de jeu.
public class Game {
	
	public static Image icone;
	public static ImageIcon icone2;
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Speedrunners");
		

		
		window.add(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setUndecorated(true);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
