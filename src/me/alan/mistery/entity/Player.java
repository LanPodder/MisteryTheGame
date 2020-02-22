package me.alan.mistery.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import me.alan.mistery.input.KeyInput;
import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class Player extends Mob {

	// Combat
	public int hitPoints = 3;
	public char state;
	public boolean flinching = false;
	public long flinchTime;
	public long time;
	public Rectangle rect;
	private boolean attacking;
	private int attackDamage;
	private int attackRange;
	private long attackDebugDelay;

	// Animations
	private Animation rightAnimation;
	private Animation leftAnimation;
	private boolean movingRight;
	private boolean move;
	private boolean movingLeft;
	private boolean facingRight;
	private Texture facingLeftTexture;
	private Texture facingRightTexture;
	private boolean facingLeft;
	private int notMoved;
	private Animation attackingLeftAnimation;
	private Animation attackingRightAnimation;
	private boolean attacked;
	private Texture attackLeftTexture;
	private Texture attackRightTexture;
	private Animation idleAnimation;
	private Animation jumpAnimation;
	private Animation idleAnimationLeft;

	public Player(double x, double y, TileMap state) {
		super(new Texture(new Texture("PlayerSheetEx"), 2, 4, 64), x, y, state);
		
		//moving animations
		rightAnimation = new Animation(5, new Texture(new Texture("PlayerSheetEx"), 1, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 5, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 6, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 7, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 8, 2, 64));
		leftAnimation = new Animation(5, new Texture(new Texture("PlayerSheetEx"), 1, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 5, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 6, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 7, 8, 64),
				new Texture(new Texture("PlayerSheetEx"), 8, 8, 64));
		
		//idle animations
		idleAnimation = new Animation(25, new Texture(new Texture("PlayerSheetEx"), 1, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 1, 64));
		idleAnimationLeft = new Animation(25, new Texture(new Texture("PlayerSheetEx"), 3, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 1, 64));
		//jumpAnimation TODO
		jumpAnimation = new Animation(5, new Texture(new Texture("PlayerSheetEx"), 3, 1, 64));

		//attacking animations
		attackingLeftAnimation = new Animation(5, new Texture(new Texture("PlayerSheetEx"), 5, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 1, 9, 128, 64));
		attackingRightAnimation = new Animation(5, new Texture(new Texture("PlayerSheetEx"), 1, 7, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 7, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 7, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 7, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 5, 7, 128, 64));
		
		//collisionbox
		cwidth = texture.getWidth();
		cheight = texture.getHeight();
		attackDamage = 2;
		attackRange = texture.getWidth() * 2;

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
			if (KeyInput.isDown(KeyEvent.VK_W))
				jump(15);
			// if(KeyInput.isDown(KeyEvent.VK_S)) dy = 4;
			if (KeyInput.isDown(KeyEvent.VK_A) && !attacking)
				dx = -6;
			if (KeyInput.isDown(KeyEvent.VK_D) && !attacking)
				dx = 6;
			if (KeyInput.isDown(KeyEvent.VK_F) && !moving) {
				System.out.println("attack");
				attacking = true;
				
				}

			// if(KeyInput.wasReleased(KeyEvent.VK_W) ||
			// KeyInput.wasReleased(KeyEvent.VK_S))
			// dy = 0;
			if (KeyInput.wasReleased(KeyEvent.VK_A) || KeyInput.wasReleased(KeyEvent.VK_D))
				dx = 0;
			super.tick();
			if(dy > 0) falling = true;
			else if(dy <= 0) falling = false;
			if(!ground){
				jumpAnimation.animate();
			}
			if(notMoved == 0){
				idleAnimation.animate();
			}
			//checking which direction we move
			if (dx > 0) {
				movingRight = true;
				moving = true;
				movingLeft = false;
				facingRight = true;
				facingLeft = false;
			} else if (dx < 0) {
				movingLeft = true;
				moving = true;
				movingRight = false;
				facingLeft = true;
				facingRight = false;
			} 
			
			//animation based on direction
			if (movingLeft && !attacking) {
				leftAnimation.animate();

			} else if (movingRight && !attacking) {
				rightAnimation.animate();
			} 
			
			//attacking animation
			if(!moving && facingLeft && attacking){
				if(attackingLeftAnimation.playedOnce) attacking = false;
				attackingLeftAnimation.animate();
			} else if(attacking && facingRight && !moving){
				if(attackingRightAnimation.playedOnce) attacking = false;
				attackingRightAnimation.animate();
			}else 
				//idle animations
				if(!moving && facingRight && !attacking){
				idleAnimation.animate();
			}else if(!moving && facingLeft && !attacking){
				idleAnimationLeft.animate();
			}

		}
	}

		

	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {

		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}
		//hitbox width becomes 
		cwidth = texture.getWidth();
		
		if(notMoved == 0 && !attacking){
			facingRight = true;
			idleAnimation.render(g, x+ offsetX, y + offsetY);
		}
		
		//check attack, cant attack when moving
		if(!moving && facingLeft && attacking){
			attackingLeftAnimation.render(g, x + offsetX - texture.getWidth(), y + offsetY);
			cwidth = texture.getWidth() * -2;
		} else if(!moving && facingRight && attacking){
			attackingRightAnimation.render(g, x + offsetX, y + offsetY);
			cwidth = texture.getWidth() * 2;
			
			
		}else
		// when standing sill and last moved in right direction
		if (!moving && facingRight && !attacking) {
			idleAnimation.render(g, x+ offsetX, y + offsetY);
		}else if(!moving && facingLeft && !attacking){
			idleAnimationLeft.render(g, x + offsetX, y + offsetY);
		}
		// righ moving animation
		else if (movingRight && !attacking) {
			if (notMoved <= 10)
				notMoved += 1;
			rightAnimation.render(g, x + offsetX, y + offsetY);
		}
		// left moving animation
		else if (movingLeft && !attacking) {
			if (notMoved <= 10)
				notMoved += 1;
			leftAnimation.render(g, x + offsetX, y + offsetY);
		}else if(!ground){
			jumpAnimation.render(g, x + offsetX, y + offsetY);
		}
		}
	
	/*	public void draw(Graphics2D g) {
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(), //	public BufferedImage getImage() { return frames[currentFrame]; }
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}*/
	

	public void kill(int damage) {
		//blinking effect
		int oldHitpoints = hitPoints;
		if (flinching) {
			return;
		}
		//reduce the mobile entity's hitpoints by damage of the enemy object
		hitPoints -= damage;
		if (hitPoints < 0)
			hitPoints = 0;
		if (hitPoints == 0)
			alive = false;
		
		//flinching becomes true upon taking damage
		flinching = true;
		flinchTime = System.nanoTime();

	}
	
	

	public void checkAttack(ArrayList<Enemy> enemies) {

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);

			// attack
			if (attacking) {
					if (isIn(e)) {
						e.kill(attackDamage);
					}
			}
			if (isIn(e) && !attacking) {
				kill(e.getDamage());
			}
			}
	}

			
			
	public boolean isAlive() {
		return alive;
	}
	
	public void reset() {
		hitPoints = 3;
		facingRight = true;
	}

	
	public boolean setAlive(boolean alive) {
		return this.alive = alive;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public int getDamage() {
		return attackDamage;
	}

}
