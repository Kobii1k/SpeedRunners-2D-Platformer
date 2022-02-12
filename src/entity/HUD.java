package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	
	private BufferedImage heart;
	private BufferedImage life;
	
	public HUD(Player p) {
		player = p;
		try {
			BufferedImage image = ImageIO.read( //on récup l'image HUD général
				getClass().getResourceAsStream(
					"/HUD/Hud.gif"
				)
			);
			heart = image.getSubimage(0, 0, 13, 12); //on récupère une image précise dans un zone de l'image séléctionné avec les coordonnées
			life = image.getSubimage(0, 12, 12, 11); // ''
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//affichage pour chaque lives (3) et chaque health (5)
	public void draw(Graphics2D g) {
		for(int i = 0; i < player.getHealth(); i++) {
			g.drawImage(heart, 10 + i * 15, 10, null);
		}
		for(int i = 0; i < player.getLives(); i++) {
			g.drawImage(life, 10 + i * 15, 25, null);
		}
		g.setColor(java.awt.Color.WHITE);
		g.drawString(player.getTimeToString(), 460, 15);
	}
	
}













