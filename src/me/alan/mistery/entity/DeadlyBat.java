package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class DeadlyBat extends Enemy{
		protected int hitPoints;
		protected char state;
		protected boolean deadly = true;
		protected Animation animateEn;
		private Animation leftAnimation;
		private Animation rightAnimation;
		protected boolean facingRight;
		protected boolean facingLeft;
		
		//movement
		private int startX;
		private int startY;
		private boolean movingLeft;
		private boolean movingRight;
		protected double maxDY;
		protected double gravity;
		protected boolean move;

		//combat
		private double playerX;
		private double currentX;
		protected double distanceToP;
		private long flinchTime;
		private boolean flinching;
		private Animation moveAnimation;


		public DeadlyBat(double x, double y, TileMap state) {
			super(new Texture(new Texture("DeadlyBatSprite"), 1, 1, 64), x, y, state);

			moveAnimation = new Animation(10, new Texture(new Texture("DeadlyBatSprite"), 1, 1, 64),
					new Texture(new Texture("DeadlyBatSprite"), 2, 1, 64),
					new Texture(new Texture("DeadlyBatSprite"), 3, 1, 64),
					new Texture(new Texture("DeadlyBatSprite"), 4, 1, 64),
					new Texture(new Texture("DeadlyBatSprite"), 5, 1, 64),
					new Texture(new Texture("DeadlyBatSprite"), 6, 1, 64));
			

			alive = true;
	
			dx = 1;
			cwidth = texture.getWidth();
			cheight = texture.getHeight();
			damage = 1;
			//falling acceleration
			gravity = 0.5;
			//max falling speed
			maxDY = 12;
			startX = 0;
			startY = 0;
			hitPoints = 5;
		}
		
		@Override
		public void tick() {
				if (alive) {
					if (flinching) {
						long elapsed = (System.nanoTime() - flinchTime) / 1000000;
						if (elapsed > 1000) {
							flinching = false;
						}
					}
					
					playerX = tilemap.getPlayer().getX();
					currentX = this.x;
					if (dy > 0)
						falling = true;
					else if (dy <= 0)
						falling = false;
					moveEnemy();
					if(dy != 0 || dx != 0){
						moving = true;
					}
					if (moving){
						moveAnimation.animate();
					}
					}
				}
			

		
		@Override
		public void render(Graphics2D g, int offsetX, int offsetY) {
			if (alive) {
				if (flinching) {
					long elapsed = (System.nanoTime() - flinchTime) / 1000000;
					if (elapsed / 100 % 2 == 0) {
						return;
					}
				}
				if (moving){
					moveAnimation.render(g, x + offsetX, y + offsetY);
				}
			}
		}
		
		@Override
		public void moveEnemy() {
			boolean horiz = getTileCollisionEnemy(texture.getWidth(), x, y, x + dx, y, false);
			boolean vert = getTileCollisionEnemy(texture.getWidth(), x, y, x, y + dy, true);

			startX += dx;
			distanceToP = currentX - playerX;
			
			if(!horiz) x += dx;
			if(!vert) y += dy;
			if(horiz || startX <= -512 || (startX <= 50 && startX >= 2)){
					dx = dx * -1;
				}
			if(vert || startY >= 300 || (startY >= -50 && startY <= -2)){
				dy = dy * -1;
			}
		}
		
		
		@Override
		public void kill(int damage) {
			int oldHitpoints = hitPoints;
			if (flinching) {
				return;
			}
			hitPoints -= damage;
			if (hitPoints < 0)
				hitPoints = 0;
			if (hitPoints == 0)
				alive = false;
			flinching = true;
			flinchTime = System.nanoTime();		
		}
		
}


