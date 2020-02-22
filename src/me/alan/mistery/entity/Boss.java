package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class Boss extends Enemy {


    //animations
	protected Animation animateEn;
	private Animation animateEnRight;
	private Animation animateEnLeft;
	private Animation leftAnimation;
	private Animation rightAnimation;


	//movement
	private boolean facingRight;
	private boolean facingLeft;
	private double startX;
	private boolean movingLeft;
	private boolean movingRight;
	private boolean move;
	protected boolean falling = true; 
    protected boolean jumping = false;
    protected boolean moving = false;
    protected double maxDY;
    protected double gravity;
	
	//combat
	protected int hitPoints = 3;
	protected char state;
	protected boolean deadly = true;
	private double distanceToP;
	private double currentX;
	private double playerX;
	protected boolean charging;
	private long flinchTime;
	private boolean flinching;
	protected boolean attacking;
	protected long chargeTime;
	protected Animation attackAnimation;
	protected Texture chargeTexture;
	private long chargeDelay;
	private Animation attackingLeftAnimation;
	private Animation attackingRightAnimation;

    public Boss(double x, double y, TileMap tileMap) {
		super(new Texture(new Texture("PlayerSheetEx"), 1, 1, 64), x, y, tileMap);
		
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
		
		chargeTexture = new Texture(new Texture("PlayerSheetEx"), 4, 1, 64);
		
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
		
		alive = true;
		dx = -1;
		cwidth = texture.getWidth();
		cheight = texture.getHeight();
		damage = 1;
		startX = 0;
		hitPoints = 4;
		chargeTime = 0;
	}
   
    /**
     * TODO boss attacks
     */
    @Override
    public void tick() {
    	if(alive){

			if (flinching) {
				long elapsed = (System.nanoTime() - flinchTime) / 1000000;
				if (elapsed > 1000) {
					flinching = false;
				}
			}
			playerX = tilemap.getPlayer().getX();
			currentX = this.x;
		if(dy > 0) falling = true;
		else if(dy < 0) falling = false;
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
	/*	}else if(!moving && facingLeft && attacking){
			if(attackingLeftAnimation.playedOnce) attacking = false;
			attackingLeftAnimation.animate();
		} else if(attacking && facingRight && !moving){
			if(attackingRightAnimation.playedOnce) attacking = false;
			attackingRightAnimation.animate();

    	}*/
    	}else {
    		tilemap.setFinished(true);;
    	}
    }
    }
    
    
    @Override
    public void moveEnemy() {
		boolean horiz = getTileCollisionEnemy(texture.getWidth(), x, y, x + dx, y, false);
		boolean vert = getTileCollisionEnemy(texture.getWidth(), x, y, x, y + dy, true);
		startX += dx;
		
		//get distance from enemy to player if player is close enough enemy does something
		distanceToP = currentX - playerX;
		if((distanceToP <= 150 && distanceToP >= 0) || (distanceToP >= -150 && distanceToP <= 0)){
			attacking = true;
		}else attacking = false;
		if(!horiz) x += dx;
		if(!vert) y += dy;

		//if enemy moves a fixed ammount of distance or collides on the x-layer he changes direction
		if(horiz || startX <= -512 || (startX <= 50 && startX >= 10)){
				dx = dx * -1;
			}

		
		
    }

	@Override
	public void render(Graphics2D g, int offsetX, int offsetY) {
		if(alive){
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			//blinking effect
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}
		if(movingRight)
			rightAnimation.render(g, x + offsetX, y + offsetY);
		else if(movingLeft)
			leftAnimation.render(g, x + offsetX, y + offsetY);
		}
		}

	
	
	public void kill(int damage){
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

	
	public int getDamage() {
		return damage;
	}

}

