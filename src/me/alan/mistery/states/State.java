package me.alan.mistery.states;

import java.awt.Graphics2D;

public interface State {
	
	//whenever the game starts and add state to the statemanager it calls init
	
	public void init();
	public void enter();
	public void tick(StateManager stateManager);
	public void render(Graphics2D g);
	//not exit game, exit state!
	public void exit();
	public String getName();
	
}
