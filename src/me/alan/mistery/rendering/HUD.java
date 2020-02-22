package me.alan.mistery.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import me.alan.mistery.entity.Player;

public class HUD {
	
	private Player player;
	private Font font;
	private Texture texture;
	private Color c;
	private Rectangle healthBarRing;
	private Rectangle healthBarFill;
	
	public HUD(Player p){
		
		player = p;
		try {
			texture = new Texture("HitPoints");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = new Font("Arial", Font.BOLD, 32);

	}
	
	public void render(Graphics2D g){
		texture.render(g, 10, 40);
		g.setColor(Color.RED);
		g.setFont(font);
		//draws "x" and the number of current hitpoints of the player next to the hitpoints texture(the heart icon in this case)
		g.drawString("x" + player.getHitPoints(), texture.getWidth() + 20, 50 + texture.getHeight() / 2);
		
		//creating rectangles for the hpbar below the heart icon
		//displays
		healthBarRing = new Rectangle(20, 160, 100, 20);
		healthBarFill = new Rectangle(20, 160, 33 * player.getHitPoints(), 20);
		g.draw(healthBarRing);
		g.draw(healthBarFill);
		g.fill(healthBarFill);
	}
}
