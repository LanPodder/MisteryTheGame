package me.alan.mistery.entity;

import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public abstract class Enemy extends Mob{

	public Enemy(Texture texture, double x, double y, TileMap state) {
		super(texture, x, y, state);
	}

	@Override
	public abstract void kill(int damage);

}
