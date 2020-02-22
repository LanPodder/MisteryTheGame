package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.Game;
import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.Tile;
import me.alan.mistery.world.TileMap;

public abstract class Mob extends Entity{
	
	protected double dx, dy;
	protected double gravity;
	protected boolean falling = true;
	protected boolean ground;
	protected double maxDY;
	protected Animation animate;
	protected boolean moving = false;
	protected int doubleJump = 0;
	public boolean alive = true;

	public Mob(Texture texture, double x, double y, TileMap state){
		super(texture, x, y, state);
		//falling acceleration
		gravity = 0.5;
		//max falling speed
		maxDY = 12;
	}
	
	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {
		if(alive){
		
		super.render(g, offsetX, offsetY);
		}
	}
	
	
	@Override
	public void tick(){
		if(dy > 0) falling = true;
		else if(dy <= 0) falling = false;
		move();
		fall();
		if(dx == 0) moving = false;
		}
	
	
	
	public void move(){
		boolean horiz = tilemap.getTileCollision(texture.getWidth(), x, y, x + dx, y, false);
		boolean vert = tilemap.getTileCollision(texture.getWidth(), x, y, x, y + dy, true);
		if(!horiz) x += dx;
		if(!vert) y += dy;
	}
	
	public void moveEnemy(){
		boolean horiz = getTileCollisionEnemy(texture.getWidth(), x, y, x + dx, y, false);
		boolean vert = getTileCollisionEnemy(texture.getWidth(), x, y, x, y + dy, true);
		if(!horiz) x += dx;
		if(!vert) y += dy;
	}
	
	public boolean getTileCollisionEnemy(int size, double oldX, double oldY, double newX, double newY, boolean vert) {
		double fromX = Math.min(oldX, newX);
		double fromY = Math.min(oldY, newY);
		double toX = Math.max(oldX, newX);
		double toY = Math.max(oldY, newY);
		mapWidth = tilemap.getWidth();

		int fromTileX = tilemap.pixelsToTiles((int) fromX);
		int fromTileY = tilemap.pixelsToTiles((int) fromY);
		int toTileX = tilemap.pixelsToTiles((int) toX + size - 1);
		int toTileY = tilemap.pixelsToTiles((int) toY + size - 1);

		for (int y = fromTileY; y <= toTileY; y++) {
			for (int x = fromTileX; x <= toTileX; x++) {
				if (x < 0 || x >= mapWidth || (tilemap.getTile(x, y) != null && tilemap.getTile(x, y).isSolid())) {
					if(tilemap.getTile(x, y).isDeadly()){
						kill(10);
					}
					if (vert) {	
						if (isFalling()) {
							setY(tilemap.tilesToPixels(y) - size);
							setCanJump(true);
						} else
							setY(tilemap.tilesToPixels(y + 1));
						setVelocityY(0);
					}

					return true;
				}
			}
		}

		return false;

	}
	
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
	

	
	/*
	protected boolean hasVerticalColision(){
		//together with the move method, which is run in the tick method,
		//it will constantly check if there are any horizontal colisions
		for(int i = 0; i < state.getTiles().size(); i++){
			//getting all tiles, added in the GameState class
			Tile t = state.getTiles().get(i);
			//intersects checks if 2 rectangles(from the java class) are within each other
			//dy > 0 checks if player y is greater than the top y of tile
			if(getBounds().intersects(t.getTop()) && dy > 0){
				ground = true;
				falling = false;
				dy = 0;
				return true;
			}else falling = true;
			if(getBounds().intersects(t.getBottom()) && dy < 0){
				dy = 0;
				return true;
			}
			}
		return false;
			
		}

	
	protected boolean hasHorizontalColision(){
		for(int i = 0; i < state.getTiles().size(); i++){
			Tile t = state.getTiles().get(i);
			if(getBounds().intersects(t.getRight()) && dx > 0){
				dx = 0;
				return true;
			}
			if(getBounds().intersects(t.getLeft()) && dx < 0){
				dx = 0;
				return true;
			}
			}
		return false;
	}
	*/
	
	protected void fall(){
			dy += gravity;
			//speed cap
			if(dy > maxDY) dy = maxDY;
		}
	
	protected void jump(double jumpacceleration){
		
		doubleJump++;
		if(ground){
			dy -= jumpacceleration;
			ground = false;
		}
	}
	
	public void setCanJump(boolean canJump) {
		this.ground = canJump;

	}
	
	public void setVelocityY(double dy){
		this.dy = dy;
	}
	
	public boolean isFalling() {
		return falling;
	}
	
	public boolean isMovingLeft(){
		return dx > 0;
	}

	public boolean isMovingRight(){
		return dx < 0;
	}
	

	public boolean isMoving() {
		return moving;
	}
	
	public boolean isAlive(){
		return alive;
	}

}
