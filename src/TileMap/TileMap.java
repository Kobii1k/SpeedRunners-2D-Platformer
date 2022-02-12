package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {

	// positions
	private double x;
	private double y;

	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	private double tween; // permet de suivre le personnage avec la caméra avec un mouvement plus lisse

	// map
	private int[][] map;
	private int tileSize;
	private int numLignes;
	private int numColonnes;

	//dimensions
	private int width;
	private int height;

	//set
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;

	//ne déssiner que les tiles qui sont présentes sur l'écran pour
	// éviter de surcharger la mémoire.
	private int lignesOffset; // à partir d'ou on commence à charger les tiles
	private int colonnesOffset;
	private int numLignesToDraw; // combien on doit en charger sur l'écran
	private int numColonnesToDraw;

	// effects
	private boolean shaking;
	private int intensity;

	public TileMap(int tileSize) { // constructeur
		this.tileSize = tileSize;
		numLignesToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColonnesToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}

	public void loadTiles(String s) { // charge le tileset dans la mémoire

		try {

			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];

			BufferedImage subimage;
			for (int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);										//définit que les tiles présents sur la seconde ligne de l'image sont normaux
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);										//définit que les tiles présents sur la seconde ligne de l'image sont bloqués (collision activée)
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadMap(String s) { // charge le filemap dans la mémoire

		try {

			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));		//on charge le fichier texte de la map

			numColonnes = Integer.parseInt(br.readLine());							//définit que la premiere ligne est le nmbre de colonnes
			numLignes = Integer.parseInt(br.readLine());							//définit que la seconde ligne est le nmbre de lignes
			map = new int[numColonnes][numColonnes];								//définit que le reste du text est simplement la map
			width = numColonnes * tileSize;
			height = numLignes * tileSize;

			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;

			String delims = "\\s+";													//définit le délimiteur à un espace
			for (int row = 0; row < numLignes; row++) {								
				String line = br.readLine();
				String[] tokens = line.split(delims);								//on partage le texte de notre array en "tokens" avec le délimiteur (ici un espace) pour le lire
				for (int col = 0; col < numColonnes; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);					//on charge completement la map.
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getTileSize() {
		return tileSize;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNumRows() {
		return numLignes;
	}

	public int getNumCols() {
		return numColonnes;
	}

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	public void setTween(double d) {	

		tween = d;
	}

	public void setBounds(int i1, int i2, int i3, int i4) {
		xmin = GamePanel.WIDTH - i1;
		ymin = GamePanel.WIDTH - i2;
		xmax = i3;
		ymax = i4;
	}

	public void setPosition(double x, double y) {	// permet de suivre le personnage avec la caméra avec un mouvement plus lisse

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		fixBounds();								//on fixe la position pour s'assurez que les limites ne sont pas dépassées

		colonnesOffset = (int) -this.x / tileSize;
		lignesOffset = (int) -this.y / tileSize;

	}

	public void fixBounds() {
		if (x < xmin)
			x = xmin;
		if (y < ymin)
			y = ymin;
		if (x > xmax)
			x = xmax;
		if (y > ymax)
			y = ymax;
	}

	public void draw(Graphics2D g) {

		for (int row = lignesOffset; row < lignesOffset + numLignesToDraw; row++) {

			if (row >= numLignes)
				break;																		//on arrette d'afficher de nouveaux items si il n'y a plus rien à afficher

			for (int col = colonnesOffset; col < colonnesOffset + numColonnesToDraw; col++) {

				if (col >= numColonnes)
					break;																	//same
				if (map[row][col] == 0)
					continue;

				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;

				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
			}
		}
	}
}