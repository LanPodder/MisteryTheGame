package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class TestEnemy extends Enemy {

	protected double maxDY;
	protected double gravity;
	protected int hitPoints = 3;
	protected char state;
	protected boolean deadly = true;
	protected Animation animateEn;
	private long flinchTime;
	private boolean flinching;
	private boolean movingLeft;
	private boolean movingRight;
	protected boolean move;
	private Player player;
	private Animation leftAnimation;
	private Animation rightAnimation;
	protected boolean facingRight;
	protected boolean facingLeft;

	public TestEnemy(double x, double y, TileMap tileMap) {
		super(new Texture(new Texture("PlayerSheetEx"), 1, 1, 64), x, y, tileMap);

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
		dx = 2;
		cwidth = texture.getWidth();
		cheight = texture.getHeight();
		damage = 1;
		//falling acceleration
		gravity = 0.2;
		//max falling speed
		maxDY = 12;
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
	public void moveEnemy() {
		boolean horiz = getTileCollisionEnemy(texture.getWidth(), x, y, x + dx, y, false);
		boolean vert = getTileCollisionEnemy(texture.getWidth(), x, y, x, y + dy, true);
		if (!horiz)
			x += dx;
		if (!vert)
			y += dy;
		if (horiz)
			dx = dx * -1;

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

	public boolean isAlive() {
		return alive;
	}

	public boolean isDeadly() {
		return deadly;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return super.getX();
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return super.getY();
	}

	public Player getPlayer() {
		return player;
	}

	public int getDamage() {
		return damage;
	}

}
