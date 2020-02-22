package me.alan.mistery.entity;

import java.awt.Graphics2D;

import me.alan.mistery.rendering.Animation;
import me.alan.mistery.rendering.Texture;
import me.alan.mistery.world.TileMap;

public class MiniBoss extends Enemy {


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

    public MiniBoss(double x, double y, TileMap tileMap) {
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
		
		attackAnimation = new Animation(5, new Texture(new Texture("PlayerSheetEx"), 5, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 4, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 3, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 2, 9, 128, 64),
				new Texture(new Texture("PlayerSheetEx"), 1, 9, 128, 64));
		
		alive = true;
		dx = -1;
		cwidth = texture.getWidth();
		cheight = texture.getHeight();
		damage = 1;
		startX = 0;
		hitPoints = 10;
		chargeTime = 0;
	}
   
    @Override
    /*
     * if the enemy is alive, it checks multiple stuff:
     * if it is charging(his type of attack) he increases his speed to 5 and keeps his current direction
     * the enemy checks for collision in his own moveEnemy method
     * if it is moving to the left or right it sets multiple boolean fields accordingly: movingRight, moving, movingLeft
     * if the enemy is moving to the left or right his animation will be set accordingly
     * 
     * @see me.alan.mistery.entity.Mob#tick()
     */
    public void tick() {
    	if(alive){

			if (flinching) {
				long elapsed = (System.nanoTime() - flinchTime) / 1000000;
				if (elapsed > 1000) {
					flinching = false;
				}
			}
			if(charging){
				dx = dx/Math.abs(dx) * 5 ;
			}else dx = dx/Math.abs(dx) * 1;
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
		} else if (dx < 0) {
			movingLeft = true;
			moving = true;
			movingRight = false;
		} 
		if (movingLeft) {
			leftAnimation.animate();

		} else if (movingRight) {
			rightAnimation.animate();
		}

    	}
    }
    
    
    @Override
    /*
     * creating temporary boolean fields for horizontal and vertical collision:
     * horiz -> calling getTileCollisionEnemy(would work for player too but i never tried it and dont want to run into bugs) and adding 
     * the vektor coordinates x, y, x + dx(destination on x-layer)[destination from the current x position], y(on horizontal layer there is no y destination)
     * and boolean vertical = false(check tilecollision method). for the size we add texture.getWidth.
     * startX which was set to 0 increases/decreases based on destinationX (dx) (startX might be a missleading name but w/e)
     * distanceToP is the distance from the enemy to the player. currentX is the enemys current position on the x-layer which is updated in the tick method
     * playerX is essentially the same as currentX but instead it calculates the players current x position
     * if the player is near the enemy charging becomes true and it will trigger the charge in the tick method, otherwise if the player is not near the enemy
     * charging becomes false and the enemy doesnt attack
     * if there is no horizontal collision dx is added to x, same with vertical collision, dy and y, and the position is updated
     * if the enemy collides with an solid tile or moves a set ammount of distance it changes directions.
     * @see me.alan.mistery.entity.Mob#moveEnemy()
     */
    public void moveEnemy() {
		boolean horiz = getTileCollisionEnemy(texture.getWidth(), x, y, x + dx, y, false);
		boolean vert = getTileCollisionEnemy(texture.getWidth(), x, y, x, y + dy, true);
		startX += dx;
		distanceToP = currentX - playerX;
		if((distanceToP <= 300 && distanceToP >= 0) || (distanceToP >= -300 && distanceToP <= 0)){
			charging = true;
		}else charging = false;
		if(!horiz) x += dx;
		if(!vert) y += dy;

		if(horiz || startX <= -512 || (startX <= 50 && startX >= 10)){
				dx = dx * -1;
			}

		
		
    }

	@Override
	/*
	 * 
	 * @see me.alan.mistery.entity.Mob#render(java.awt.Graphics2D, int, int)
	 */
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
		else if(charging){
			chargeTexture.render(g, offsetX, offsetY);
		}
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

