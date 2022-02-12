package TileMap;

import java.awt.image.BufferedImage;

//Bloc de plateforme du jeu, avec int définissant la transparence du bloc dans la map. => sa solidité
public class Tile {
	
	private BufferedImage image;
	private int type;
	
	// differents types de tiles
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	public Tile(BufferedImage image, int type) {	//constructeur
		this.image = image;
		this.type = type;
	}
	
	//getters
	public BufferedImage getImage() { return image; }		
	public int getType() { return type; }
	
}
