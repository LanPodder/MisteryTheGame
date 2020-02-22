package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class Cookie extends Entity {
	
	private Player player;
	private boolean alive;

	public Cookie(double x, double y, TileMap tilem) {
		super(new Texture("HitPoints"), x, y, tilem);
		alive = true;
		damage = -1;
	}

	@Override
	public void tick() {
		if(alive){
			
		}
	}
	
	@Override
	public void render(Graphics2D g, int offsetX, int offsetY){
		super.render(g, offsetX, offsetY);
	}
	
	public boolean isAlive() {
		return alive;
	}
	

	@Override
	public void kill(int damage) {
		if(alive){
		player.hitPoints += damage;
		alive = false;
		}
	}
	
	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public boolean isFalling() {
		return false;
	}

	@Override
	public void setCanJump(boolean b) {
		
	}

	@Override
	public void setVelocityY(double i) {
		
	}
	
	
}
