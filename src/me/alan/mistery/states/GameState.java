package me.alan.mistery.states;

import java.awt.Graphics2D;
import java.util.ArrayList;

import me.alan.mistery.Game;
import me.alan.mistery.entity.Entity;
import me.alan.mistery.world.Tile;
import me.alan.mistery.world.TileMap;

public class GameState implements State {
	
	private ArrayList<Entity> entities;
	private ArrayList<Tile> tiles;
	private TileMap tileMap;

	@Override
	public void init() {
		tileMap = new TileMap("Level1");
	}

	@Override
	public void enter() {
		
	}

	@Override
	public void tick(StateManager stateManager) {
		if(!tileMap.isFinished())
		tileMap.tick();
		else stateManager.setState("menu");
	}

	@Override
	public void render(Graphics2D g) {
		tileMap.render(g, Game.WIDTH, Game.HEIGHT);
	}

	@Override
	public void exit() {
		tileMap.clear();
	}

	@Override
	public String getName() {
		return "lvl1";
	}

}
