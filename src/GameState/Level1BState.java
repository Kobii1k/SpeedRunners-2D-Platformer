package GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import entity.Enemy;
import entity.EnemyProjectile;
import entity.EnergyParticle;
import entity.Explosion;
import entity.HUD;
import entity.Player;
import entity.PlayerSave;
import entity.Teleport;
import entity.Title;
import entity.Enemies.Gazer;
import entity.Enemies.GelPop;

public class Level1BState extends GameState {

	private Background city1;
	private Background city2;
	private Background sky;
	private Background montagnes;

	private Player player;
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyProjectile> eprojectiles;
	private ArrayList<EnergyParticle> energyParticles;
	private ArrayList<Explosion> explosions;

	private HUD hud;
	private BufferedImage hageonText;
	private Title title;
	private Title subtitle;
	private Teleport teleport;

	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;
	private boolean eventFinish;
	private boolean eventDead;
	private boolean eventQuake;

	public Level1BState(GameStateManager gsm) {
		super(gsm);
		init();
	}

	public void init() {

		city1 = new Background("/Backgrounds/0.gif", 0.2);
		city2 = new Background("/Backgrounds/1.gif", 0.3);
		sky = new Background("/Backgrounds/2.gif", 0);
		montagnes = new Background("/Backgrounds/3.gif", 0.1);

		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap("/Maps/level1b.map");
		tileMap.setPosition(140, 0);
		tileMap.setTween(1);

		player = new Player(tileMap);
		player.setPosition(300, 131);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());

		enemies = new ArrayList<Enemy>();
		eprojectiles = new ArrayList<EnemyProjectile>();
		populateEnemies();
		energyParticles = new ArrayList<EnergyParticle>();

		player.init(enemies, energyParticles);

		explosions = new ArrayList<Explosion>();

		hud = new HUD(player);

		try {
			hageonText = ImageIO.read(getClass().getResourceAsStream("/HUD/HageonTemple.gif"));
			title = new Title(hageonText.getSubimage(0, 0, 178, 20));
			title.sety(60);
			subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
			subtitle.sety(85);
		} catch (Exception e) {
			e.printStackTrace();
		}

		teleport = new Teleport(tileMap);
		teleport.setPosition(2850, 371);

		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();

	}

	private void populateEnemies() {
		enemies.clear();
		GelPop gp;
		Gazer g;

		gp = new GelPop(tileMap, player);
		gp.setPosition(750, 100);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(900, 150);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(1320, 250);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(1570, 160);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(1590, 160);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(2600, 370);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(2620, 370);
		enemies.add(gp);
		gp = new GelPop(tileMap, player);
		gp.setPosition(2640, 370);
		enemies.add(gp);

		g = new Gazer(tileMap);
		g.setPosition(904, 130);
		enemies.add(g);
		g = new Gazer(tileMap);
		g.setPosition(1080, 270);
		enemies.add(g);
		g = new Gazer(tileMap);
		g.setPosition(1200, 270);
		enemies.add(g);
		g = new Gazer(tileMap);
		g.setPosition(1704, 300);
		enemies.add(g);

	}

	public void update() {


		handleInput();

		if (teleport.contains(player)) {
			eventFinish = blockInput = true;
		}

		if (eventStart)
			eventStart();
		if (eventDead)
			eventDead();
		if (eventQuake)
			eventQuake();
		if (eventFinish)
			eventFinish();

		if (title != null) {
			title.update();
			if (title.shouldRemove())
				title = null;
		}
		if (subtitle != null) {
			subtitle.update();
			if (subtitle.shouldRemove())
				subtitle = null;
		}

		city1.setPosition(tileMap.getx(), tileMap.gety());
		city2.setPosition(tileMap.getx(), tileMap.gety());
		montagnes.setPosition(tileMap.getx(), tileMap.gety());

		player.update();
		if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}

		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		tileMap.fixBounds();


		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
			}
		}


		for (int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile ep = eprojectiles.get(i);
			ep.update();
			if (ep.shouldRemove()) {
				eprojectiles.remove(i);
				i--;
			}
		}

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}


		teleport.update();

	}

	public void draw(Graphics2D g) {


		sky.draw(g);
		montagnes.draw(g);
		city1.draw(g);
		city2.draw(g);


		tileMap.draw(g);


		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		for (int i = 0; i < eprojectiles.size(); i++) {
			eprojectiles.get(i).draw(g);
		}


		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		player.draw(g);
		teleport.draw(g);
		hud.draw(g);


		if (title != null)
			title.draw(g);
		if (subtitle != null)
			subtitle.draw(g);


		g.setColor(java.awt.Color.BLACK);
		for (int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}

	}

	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE))
			gsm.setPaused(true);
		if (blockInput || player.getHealth() == 0)
			return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
		player.setDashing(Keys.keyState[Keys.BUTTON2]);
		if (Keys.isPressed(Keys.BUTTON3))
			player.setAttacking();
		if (Keys.isPressed(Keys.BUTTON4))
			player.setCharging();
	}

//Evenements
	// reset level
	private void reset() {
		player.loseLife();
		player.reset();
		player.setPosition(300, 131);
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		eventStart = true;
		eventStart();
		title = new Title(hageonText.getSubimage(0, 0, 178, 20));
		title.sety(60);
		subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
		subtitle.sety(85);
	}

	// début level
	private void eventStart() {
		eventCount++;
		if (eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if (eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if (eventCount == 30)
			title.begin();
		if (eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			subtitle.begin();
			tb.clear();
		}
	}

	// joueur est mort
	private void eventDead() {
		eventCount++;
		if (eventCount == 1)
			player.setDead();
		if (eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if (eventCount >= 120) {
			if (player.getLives() == 0) {
				gsm.setState(GameStateManager.MENUSTATE);
			} else {
				eventDead = blockInput = false;
				eventCount = 0;
				reset();
			}
		}
	}

	// earthquake => inutilisé
	private void eventQuake() {
		eventCount++;
		if (eventCount == 1) {
			player.stop();
			player.setPosition(2175, player.gety());
		}
		if (eventCount == 60) {
			player.setEmote(Player.CONFUSED);
		}
		if (eventCount == 120)
			player.setEmote(Player.NONE);
		if (eventCount == 150)
		if (eventCount == 180)
			player.setEmote(Player.SURPRISED);
		if (eventCount == 300) {
			player.setEmote(Player.NONE);
			eventQuake = blockInput = false;
			eventCount = 0;
		}
	}

	// level terminé
	private void eventFinish() {
		eventCount++;
		if (eventCount == 1) {
			player.setTeleporting(true);
			player.stop();
		} else if (eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if (eventCount == 180) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			gsm.setState(GameStateManager.LEVEL1CSTATE);
		}
	}
}