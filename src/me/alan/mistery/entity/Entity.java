package me.alan.mistery.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.Tile;
import me.alan.mistery.world.TileMap;

//not gonna use entities just costomize
public abstract class Entity {
	
	
	
	protected Texture texture;
	protected TileMap tilemap;
	
	//position and vector
	protected double x, y;
	protected double dy, dx;	
	//Collision box
	protected int cwidth;
	protected int cheight;
	//collision
	protected double xdest;
	protected double ydest;
	protected int currRow;
	protected int currCol;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected int mapWidth;

	protected boolean falling;
	protected int tileSize;

	
	//combat
	protected int damage;
	
	public Entity(Texture texture, double x, double y, TileMap tilem) {
		super();
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.tilemap = tilem;
		tilemap.addEntity(this);
	}
	
	//Entities will have different behavior
	public abstract void tick();
	
	//not all entities will get damaged but it makes it easier to programm
	public abstract void kill(int damage);
	
	public boolean intersectsEntity(Entity e){
		Rectangle r1 = getRectangle();
		Rectangle r2 = e.getRectangle();
		
		return r1.intersects(r2);
		
		
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(
				(int)x - cwidth, (int)y - cheight, cwidth, cheight
				);
	}
	
	public void render(Graphics2D g, int offsetX, int offsetY){
		texture.render(g, x + offsetX, y + offsetY);

	}
	
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	public int getCwidth() {
		return cwidth;
	}

	public int getCheight() {
		return cheight;
	}
	
	public int getDamage() {
		return damage;
	}

	public abstract boolean isFalling();

	public abstract void setCanJump(boolean b);

	public abstract void setVelocityY(double i);

	public abstract boolean isAlive();
	
	
	// xR 1 > xL 2
	// xL 1 < xR 2
	// yL 1 > yU 2
	// yU 1 < yL 2
	
    public boolean isIn(Entity e) {
        if (( e.x + e.cwidth > x) && 
        		(e.x < x + cwidth) && 
        		(e.y + e.cheight > y) && 
        		(e.y < y + cheight))
        {
            return true;
        } else
        {
            return false;
        }

    }
    

	
}
