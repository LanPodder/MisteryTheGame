package me.alan.mistery.world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import me.alan.mistery.entity.Boss;
import me.alan.mistery.entity.Cookie;
import me.alan.mistery.entity.DeadlyBat;
import me.alan.mistery.entity.Enemy;
import me.alan.mistery.entity.Entity;
import me.alan.mistery.entity.Jumper;
import me.alan.mistery.entity.MiniBoss;
import me.alan.mistery.entity.Player;
import me.alan.mistery.entity.Slugger;
import me.alan.mistery.entity.TestEnemy;
import me.alan.mistery.rendering.HUD;
import me.alan.mistery.rendering.Paralax;
import me.alan.mistery.rendering.ParalaxEngine;
import me.alan.mistery.rendering.Texture;

public class TileMap {

	private static final int TILE_SIZE = 64;
	private static final int TILE_SIZE_BITS = 6; // 2*bits = Tile_Size

	private Tile[] tiles;
	private String name;
	private int width, height;

	private Player player;
	private ParalaxEngine parallaxEngine;

	private Graphics2D g;
	private HUD hud;
	private Cookie cookie;
	public boolean finished;

	private ArrayList<Entity> entities;
	private ArrayList<Enemy> enemies;
	
	
	//Enemies
	private Jumper jumper;
	private TestEnemy mob;
	private MiniBoss mboss;
	private DeadlyBat bat;
	private Slugger slugger;
	private Boss boss;

	public TileMap(String name) {
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		//mob = new TestEnemy(1500, 3000, this);
		//addEntity(mob);
		Paralax back = new Paralax(new Texture("background_back"), (int)((16*0.25) * 0.15));
		Paralax mid = new Paralax(new Texture("background_mid"), (int)((16*0.25) * 0.4));
		Paralax front = new Paralax(new Texture("background_front"), (int)((16*0.25) * 0.7));
		this.parallaxEngine = new ParalaxEngine(back, mid, front);
		load(name);
	}

	public static int pixelsToTiles(int pixels) {
		return pixels >> TILE_SIZE_BITS; // floor(pixels / TILE_SIZE) the actual
											// way doesnt give negative
	}

	public static int tilesToPixels(int tiles) {
		return tiles << TILE_SIZE_BITS; // tiles * TILE_SIZE
	}

    public void render(Graphics2D g, int screenWidth, int screenHeight) {
        int mapWidth = tilesToPixels(width);
        int mapHeight = tilesToPixels(height);
        int offsetX = (int) (screenWidth / 2 - player.getX() - TILE_SIZE / 2);
        int offsetY = (int) (screenHeight / 2 - player.getY() - TILE_SIZE / 2);
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, screenHeight - mapHeight);

        int firstX = pixelsToTiles(-offsetX);
        int lastX = firstX + pixelsToTiles(screenWidth) + 1;
        int firstY = pixelsToTiles(-offsetY);
        int lastY = firstY + pixelsToTiles(screenHeight) + 1;
        
        parallaxEngine.render(g);

        for (int y = firstY; y <= lastY; y++) {
            for (int x = firstX; x <= lastX; x++) {
                Tile t = getTile(x, y);
                if (t != null) t.render(g, tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY);
            }
        }

        for (int i = 0; i < entities.size(); i++)
            entities.get(i).render(g, offsetX, offsetY);
        for (int i = 0; i < enemies.size(); i++)
            enemies.get(i).render(g, offsetX, offsetY);
        player.render(g, offsetX, offsetY);
        
        hud.render(g);
    }

	// oldx and oldy = current position
	public boolean getTileCollision(int size, double oldX, double oldY, double newX, double newY, boolean vert) {
		double fromX = Math.min(oldX, newX);
		double fromY = Math.min(oldY, newY);
		double toX = Math.max(oldX, newX);
		double toY = Math.max(oldY, newY);

		int fromTileX = pixelsToTiles((int) fromX);
		int fromTileY = pixelsToTiles((int) fromY);
		int toTileX = pixelsToTiles((int) toX + size - 1);
		int toTileY = pixelsToTiles((int) toY + size - 1);

		for (int y = fromTileY; y <= toTileY; y++) {
			for (int x = fromTileX; x <= toTileX; x++) {
				if (x < 0 || x >= width || (getTile(x, y) != null && getTile(x, y).isSolid())) {
					if(getTile(x, y).isDeadly()){
						player.kill(10);
					}
					if (vert) {
						if (player.isFalling()) {
							player.setY(tilesToPixels(y) - size);
							player.setCanJump(true);
							
						} else
							player.setY(tilesToPixels(y + 1));
						player.setVelocityY(0);
					}

					return true;
					
				}
			}
		}

		return false;

	}
	
	/*public boolean getTileCollisionEnemy(int size, double oldX, double oldY, double newX, double newY, boolean vert) {
		double fromX = Math.min(oldX, newX);
		double fromY = Math.min(oldY, newY);
		double toX = Math.max(oldX, newX);
		double toY = Math.max(oldY, newY);

		int fromTileX = pixelsToTiles((int) fromX);
		int fromTileY = pixelsToTiles((int) fromY);
		int toTileX = pixelsToTiles((int) toX + size - 1);
		int toTileY = pixelsToTiles((int) toY + size - 1);

		for (int y = fromTileY; y <= toTileY; y++) {
			for (int x = fromTileX; x <= toTileX; x++) {
				if (x < 0 || x >= width || (getTile(x, y) != null && getTile(x, y).isSolid())) {
					if(getTile(x, y).isDeadly()){
						mob.kill(10);
					}
					if (vert) {	
						if (mob.isFalling()) {
							mob.setY(tilesToPixels(y) - size);
							mob.setCanJump(true);
						} else
							mob.setY(tilesToPixels(y + 1));
						mob.setVelocityY(0);
					}

					return true;
				}
			}
		}

		return false;

	}
	*/
	public boolean getTileCollisionMiniBoss(int size, double oldX, double oldY, double newX, double newY, boolean vert) {
		double fromX = Math.min(oldX, newX);
		double fromY = Math.min(oldY, newY);
		double toX = Math.max(oldX, newX);
		double toY = Math.max(oldY, newY);

		int fromTileX = pixelsToTiles((int) fromX);
		int fromTileY = pixelsToTiles((int) fromY);
		int toTileX = pixelsToTiles((int) toX + size - 1);
		int toTileY = pixelsToTiles((int) toY + size - 1);

		for (int y = fromTileY; y <= toTileY; y++) {
			for (int x = fromTileX; x <= toTileX; x++) {
				if (x < 0 || x >= width || (getTile(x, y) != null && getTile(x, y).isSolid())) {
					if(getTile(x, y).isDeadly()){
						mboss.kill(10);
					}
					if (vert) {	
						if (mboss.isFalling()) {
							mboss.setY(tilesToPixels(y) - size);
							mboss.setCanJump(true);
						} else
							mboss.setY(tilesToPixels(y + 1));
						mboss.setVelocityY(0);
					}

					return true;
				}
			}
		}

		return false;

	}

	public boolean getTileCollisionEntity(int size, double oldX, double oldY, double newX, double newY, boolean vert) {
		double fromX = Math.min(oldX, newX);
		double fromY = Math.min(oldY, newY);
		double toX = Math.max(oldX, newX);
		double toY = Math.max(oldY, newY);

		int fromTileX = pixelsToTiles((int) fromX);
		int fromTileY = pixelsToTiles((int) fromY);
		int toTileX = pixelsToTiles((int) toX + size - 1);
		int toTileY = pixelsToTiles((int) toY + size - 1);
		for (int i = 0; i < enemies.size(); i++){
		for (int y = fromTileY; y <= toTileY; y++) {
			for (int x = fromTileX; x <= toTileX; x++) {
				if (x < 0 || x >= width || (getTile(x, y) != null && getTile(x, y).isSolid())) {
					if(getTile(x, y).isDeadly()){
						enemies.get(i).kill(10);
					}
					if (vert) {	
						if (enemies.get(i).isFalling()) {
							enemies.get(i).setY(tilesToPixels(y) - size);
							enemies.get(i).setCanJump(true);
						} else
							enemies.get(i).setY(tilesToPixels(y + 1));
						enemies.get(i).setVelocityY(0);
					}

					return true;
				}
			}
		}
		}

		return false;

	}
	

	
	public void tick() {
		if(player.isAlive()){
        if (player.isMovingLeft())
            parallaxEngine.setLeft();
        else if (player.isMovingRight())
            parallaxEngine.setRight();
        if (player.isMoving())
            parallaxEngine.move();
		for (int i = 0; i < entities.size(); i++){
			
			entities.get(i).tick();
			
		}
		
		
		for (int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			e.tick();
			if(!e.isAlive()){
				enemies.remove(i);
				i--;
			}
		}
		player.tick();
		player.checkAttack(enemies);
		

	//	if(cookie.isAlive())
		//	cookie.tick();
		}else load(name);
		
		
	}

	public void setTile(int x, int y, Tile tile) {
		tiles[x + y * width] = tile; //
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		return tiles[x + y * width];
	}

	private void load(String name) {
		clear();
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("stuff/Levels/" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Point of the code: reading an image with differently colored pixels
		// and replace them with sprites
		this.name = name; // replacing name variable with the level name
		this.width = image.getWidth();
		this.height = image.getHeight();
		tiles = new Tile[width * height];
		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int id = pixels[x + y * width];
				if (id == 0xFF0000FF){
					player = new Player(tilesToPixels(x), tilesToPixels(y), this);
				addEntity(player);
				player.reset();
				player.setAlive(true);
				}
				else if (Tile.getFromID(id) != null) {
					setTile(x, y, Tile.getFromID(id));
					
				}
			if(id == 0xFF0FFFFF){
					mob = new TestEnemy(tilesToPixels(x), tilesToPixels(y), this);
					enemies.add(mob);
					
			}
			if(id == 0xFFDF3FD9){
				mboss = new MiniBoss(tilesToPixels(x), tilesToPixels(y), this);
				enemies.add(mboss);
			}
			if(id == 0xFFC9985C){
				jumper = new Jumper(tilesToPixels(x), tilesToPixels(y), this);
				enemies.add(jumper);
			}
			if(id == 0xFF571857){
				bat = new DeadlyBat(tilesToPixels(x), tilesToPixels(y), this);
				enemies.add(bat);
			}
			if(id == 0xFF1A3286){
				boss = new Boss(tilesToPixels(x), tilesToPixels(y), this);
				enemies.add(boss);
			}
			if(id == 0xFF6BE9D3){
				slugger = new Slugger(tilesToPixels(x), tilesToPixels(y), this);
				enemies.add(slugger);
			}
			

				
			}
		}
		hud = new HUD(player);
	}

	public void addEntity(Entity e) {
		if (!(e instanceof Player))
			entities.add(e);
	}

	public void removeEntity(Entity e) {
		if (!(e instanceof Player))
			entities.remove(e);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public Player getPlayer() {
		return player;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void clear(){
		entities.clear();
		enemies.clear();
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	

}
