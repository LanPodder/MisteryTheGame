package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class Jumper extends Enemy {
	
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

	public Jumper(double x, double y, TileMap state) {
		super(new Texture(new Texture("PlayerSheetEx"), 1, 1, 64), x, y, state);

		rightAnimation = new Animation(10, new Texture(new Texture("PlayerSheetEx"), 1, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 1, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 2, 64));
		leftAnimation = new Animation(10, new Texture(new Texture("PlayerSheetEx"), 1, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 1, 2, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 1, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 1, 64));
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
				fall();
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
				if (movingLeft) {
					leftAnimation.animate();

				} else if (movingRight) {
					rightAnimation.animate();
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
			if (movingRight)
				rightAnimation.render(g, x + offsetX, y + offsetY);
			else if (movingLeft)
				leftAnimation.render(g, x + offsetX, y + offsetY);
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
		if(distanceToP <= 200 && distanceToP >= - 200){
			jump(15);
			dx = dx/Math.abs(dx) * 3;
		}else dx = dx/Math.abs(dx) * 1;
		if(horiz || startX <= -512 || (startX <= 50 && startX >= 2)){
				dx = dx * -1;
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
