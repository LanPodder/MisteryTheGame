package me.alan.mistery.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import me.alan.mistery.Game;
import me.alan.mistery.input.KeyInput;
import me.alan.mistery.input.MouseInput;
import me.alan.mistery.rendering.UI.Button;
import me.alan.mistery.utils.manage.Fonts;

public class MenuState implements State {

	private Button[] options;
	private int currentSelection;
	
	@Override
	public void init(){
        options = new Button[3];
        options[0] = new Button("Play", 200 + 0 * 80,
                new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
                Color.WHITE, Color.YELLOW);
        options[1] = new Button("Options", 200 + 1 * 80,
                new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
                Color.WHITE, Color.YELLOW);
        options[2] = new Button("Exit", 200 + 2 * 80,
                new Font("Arial", Font.PLAIN, 32), new Font("Arial", Font.BOLD, 48),
                Color.WHITE, Color.YELLOW);
	}
	
	@Override
	public void enter(){
		
	}
	
	@Override
	public void tick(StateManager stateManager){
		
		
		if(KeyInput.wasPressed(KeyEvent.VK_UP) || KeyInput.wasPressed(KeyEvent.VK_W)){
			currentSelection--;
			if(currentSelection < 0) currentSelection = options.length - 1;
		}
		if(KeyInput.wasPressed(KeyEvent.VK_DOWN) || KeyInput.wasPressed(KeyEvent.VK_S)){
			currentSelection++;
			if(currentSelection >= options.length) currentSelection = 0;
		}
		boolean clicked = false;
		for(int i = 0; i< options.length; i++){
			//checking if mouse is inside the button
			if(options[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY()))){
				currentSelection = i;
				clicked = MouseInput.wasPressed(MouseEvent.BUTTON1);
			}
		}
		
		if(clicked || KeyInput.wasPressed(KeyEvent.VK_ENTER)) 
			select(stateManager);
	}
	
	private void select(StateManager stateManager){
		switch(currentSelection){
		case 0:
			stateManager.setState("lvl1");
			break;
		case 1:
			System.out.println("Options");
			break;
		case 2:
			System.out.println("Exit");
			Game.INSTANCE.stop();
			break;
		}
	}
	
	public void render(Graphics2D g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		Fonts.drawString(g, new Font("Arial", Font.BOLD, 72), Color.ORANGE, Game.TITLE, 80);
		
		for(int i=0; i < options.length; i++){
			if(i == currentSelection)
				options[i].setSelected(true);
			else options[i].setSelected(false);
			
			options[i].render(g);
		}
	}
	
	@Override
	public void exit(){
		
	}
	
	public String getName(){
		return "menu";
	}
}
