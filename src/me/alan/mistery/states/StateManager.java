package me.alan.mistery.states;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class StateManager {

	
	private Map<String, State> map;
	private State CurrentState;
	
	public StateManager(){
		map = new HashMap<String, State>();
	}
	
	public void addState(State state){
		map.put(state.getName().toUpperCase(), state);
		state.init();
		if(CurrentState == null){
			state.enter();
			CurrentState = state;
		}
	}
	
	public void setState(String name){
		State state = map.get(name.toUpperCase());
		if(state == null){
			System.err.println("State <" + name + "> does not exist!!!!!!");
			return;
		}
		CurrentState.exit();
		state.enter();
		CurrentState = state;
	}
	
	public void tick(){
		CurrentState.tick(this);
		
	}
	
	public void render(Graphics2D g){
		CurrentState.render(g);
	}
}
