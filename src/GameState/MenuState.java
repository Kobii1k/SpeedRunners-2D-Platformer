package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import Handlers.Keys;
import entity.PlayerSave;

//Menu Entree de jeu
public class MenuState extends GameState {
	
	private Image head;
	private ImageIcon head2;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Quit"
	};
	
	private Font font;
	private Font font2;
	
	private ImageIcon icofond;
	private Image title;
	
	public MenuState(GameStateManager gsm) {
		
		super(gsm);
		
		try {
								
			head2 = new ImageIcon(getClass().getResource("/logo.png"));
			this.head = this.head2.getImage();
			
			//fonts
			font = new Font("Tahoma", Font.BOLD, 25);
			font2 = new Font("Palatino Linotype", Font.PLAIN, 16);
			icofond = new ImageIcon(getClass().getResource("/fond.jpg"));
			this.title = this.icofond.getImage();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	public void update() {handleInput();}
	
	public void draw(Graphics2D g) {
		
		//background
		g.drawImage(title, 0, 0, null);
		
		//menu options
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Start", 448, 420);
		g.drawString("Quit", 448, 465);
		
		//tete flottante => abandonné
		if(currentChoice == 0) g.drawImage(head, 405, 395, null);
		else if(currentChoice == 1) g.drawImage(head, 405, 440, null);
		
		g.setFont(font2);
		g.drawString("v1.0.1", 5, 530);
		
	}
	
	private void select() { //selection des boutons menu
		if(currentChoice == 0) {
			PlayerSave.init();
			gsm.setState(GameStateManager.LEVEL1ASTATE);
		}
		else if(currentChoice == 1) {
			System.exit(0);
		}
	}
	
	public void handleInput() { //nouvelle selection des boutons du clavier
		if(Keys.isPressed(Keys.ENTER)) select();
		if(Keys.isPressed(Keys.UP)) {
			if(currentChoice > 0) {
				currentChoice--;
			}
		}
		if(Keys.isPressed(Keys.DOWN)) {
			if(currentChoice < options.length - 1) {
				currentChoice++;
			}
		}
	}
	
}










