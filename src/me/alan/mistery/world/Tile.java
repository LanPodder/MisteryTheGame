package me.alan.mistery.world;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import me.alan.mistery.entity.TestEnemy;
import me.alan.mistery.rendering.Texture;

public class Tile {
	
	private static final Texture terrain = new Texture("TerrainTexture");
	
	protected int x, y;
	protected Texture sprite;
	protected boolean solid;
	protected boolean deadly;
	protected int id;

	private static final Map<Integer, Tile> tileMap = new HashMap<Integer, Tile>();
	
	public static final Tile grassDirt = new Tile(0xFFD63C3C, new Texture(terrain, 1, 1, 64), false);
	public static final Tile dirt = new Tile(0xFFFF0000, new Texture(terrain, 1, 2, 64), false);
	public static final Tile leftEdgeFloatingGrass = new Tile(0xFF600505, new Texture(terrain, 1, 4, 64), false);
	public static final Tile rightEdgeFloatingGrass = new Tile(0xFF8D1313, new Texture(terrain, 3, 4, 64), false);
	public static final Tile centerFloatingGrass = new Tile(0xFFF38D3B, new Texture(terrain, 2, 4, 64), false);
	public static final Tile rightGrassEdge = new Tile(0xFF3E1B00, new Texture(terrain, 3, 1, 64), false);
	public static final Tile rightGrassCorner = new Tile(0xFF733C12, new Texture(terrain, 2, 1, 64), false);
	public static final Tile leftGrassCorner = new Tile(0xFFFF59FD, new Texture(terrain, 2, 2, 64), false);
	public static final Tile leftGrassEdge = new Tile(0xFFA419A2, new Texture(terrain, 4, 1, 64), false);
	public static final Tile grassConnectorRight = new Tile(0xFF425941, new Texture(terrain, 2, 3, 64), false);
	public static final Tile grassConnectorLeft = new Tile(0xFF25F4C2, new Texture(terrain, 1, 3, 64), false);
	public static final Tile spikes = new Tile(0xFF2AA4C2, new Texture(terrain, 4, 3, 64), true);
	public static final Tile stoneSurface = new Tile(0xFF7C7B7A, new Texture(terrain, 3, 2, 64), false);
	public static final Tile stoneBlock = new Tile(0xFF2D2D2D, new Texture(terrain, 3, 3, 64), false);
	public static final Tile runicStone = new Tile(0xFFDF8F3F, new Texture(terrain, 4, 2, 64), false);
	public static final Tile pillarDetails = new Tile(0xFFC13B3B, new Texture(terrain, 4, 4, 64), false);
	public static final Tile pillarClean = new Tile(0xFF176B15, new Texture(terrain, 5, 4, 64), false);
	public static final Tile pillarBroken = new Tile(0xFFE8E632, new Texture(terrain, 5, 3, 64), false);
	public static final Tile pillarDetailsDown = new Tile(0xFF3C1616, new Texture(terrain, 6, 2, 64), false);
	public static final Tile pillarBrokenDown = new Tile(0xFF886FA7, new Texture(terrain, 6, 4, 64), false);
	
	

	
	private Tile(int id, Texture sprite, boolean deadly){
		this.id = id;
		this.sprite = sprite;
		solid = true;
		tileMap.put(id, this);
		this.deadly = deadly;
	}
	
	
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isDeadly(){
		return deadly;
	}
	
	public void render(Graphics2D g, int x, int y){
		sprite.render(g, x, y);
		
	}


	public static Tile getFromID(int id){
		return tileMap.get(id);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	/*
	g.setColor(Color.RED);
	g.draw(getTop());
	g.setColor(Color.BLUE);
	g.draw(getBottom());
	g.setColor(Color.MAGENTA);
	g.draw(getLeft());
	g.setColor(Color.ORANGE);
	g.draw(getRight());

}

public Rectangle getBounds(){
	return new Rectangle((int)x, (int)y,
			sprite.getWidth(), sprite.getHeight());
}

public Rectangle getTop(){
	return new Rectangle((int)x , (int)y, 
			sprite.getWidth() - 4 , 4);
}

public Rectangle getBottom(){
	return new Rectangle((int)x + 6, (int)y + sprite.getHeight() - 4,
			sprite.getWidth() - 6, 4);
}

public Rectangle getLeft(){
	return new Rectangle((int)x + sprite.getWidth() - 4, (int)y + 6,
			4, sprite.getHeight() - 6);
}
public Rectangle getRight(){
	return new Rectangle((int)x, (int)y + 6 ,
			4, sprite.getHeight() - 6);
}
*/
}
