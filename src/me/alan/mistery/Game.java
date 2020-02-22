package me.alan.mistery;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import me.alan.mistery.input.KeyInput;
import me.alan.mistery.input.MouseInput;
import me.alan.mistery.states.GameState;
import me.alan.mistery.states.MenuState;
import me.alan.mistery.states.StateManager;

public class Game extends Canvas implements Runnable {

	/**
	 * Project: 2D Game
	 * 
	 */
	private static final long serialVersionUID = -2398476706239860109L;
    private static JFrame frame = new JFrame();
	public static final int WIDTH = 1280;
    public static final int HEIGHT = WIDTH / 16 * 9;
    public static final String TITLE = "Mistery";
    private static Game game = new Game();
	//not running by default
    private boolean running = false;
    //when started make "void run" act as a secondary main method
    private Thread thread;
    private StateManager stateManager;
    
    public static Game INSTANCE;
    
    public Game(){
    	addKeyListener(new KeyInput());
    	MouseInput mi = new MouseInput();
    	addMouseListener(mi);
    	addMouseMotionListener(mi);
    	stateManager = new StateManager();
    	
    	stateManager.addState(new MenuState());
    	stateManager.addState(new GameState());
    	
    	INSTANCE = this;
    }
    
    
    /*
     * just returns game. literally.
     */
    public static Game getInstance(){
    	return game;
    }
    
    
    
    public void tick(){
    	stateManager.tick();
    	}
    
    
    /**
     * 
     */
    public void render(){
    	BufferStrategy bs = this.getBufferStrategy();
    	if(bs == null){
    		createBufferStrategy(3);
    		return;
    	}
    	Graphics g = bs.getDrawGraphics();
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.translate(-6, -28);
    	//-------------------------------
    	stateManager.render(g2d);
    	//-------------------------------

    	g.dispose();
    	bs.show();
    }
    
    /**
     * sets running to true if it is not running already and creates and starts a new Thread. Thread makes "run" act as a secondary main method
     * 
     */
    private void start(){
    	if(running)
    		return;
    	else
    		running = true;
    	thread = new Thread(this);
    	thread.start();
    }
    
    /**
     * sets running to false if it is not already false.
     */
    public void stop() {
        if (!running) return;
        running = false;
    }
    
    /**
     * implements run from java.lang.Runnable
     * creates and infinite loop which can only be interrupted by pressing "x" or selecting "Exit" in the main menu
     * 
     */
    @Override
	public void run() {
    	System.out.println("running");
    	//variables to time and slow down the game
    	
    	long lastTime = System.nanoTime();
    	//60 updates per second
    	final double numTicks = 60.0;
    	//convert into seconds
    	double nanosPerTick = 1000000000 / numTicks;
    	//unprocessed time
    	double delta = 0.0;
    	int frames = 0;
    	int ticks = 0;
    	long timer = System.currentTimeMillis();
    	boolean canRender = false;
    	
    	while(running){
    		long currentTime = System.nanoTime();
    		delta +=(currentTime - lastTime) / nanosPerTick;
    		lastTime = currentTime;
    		
    		if(delta >= 1.0){
    			tick();
    			KeyInput.update();
    			MouseInput.update();
    			ticks++;
    			delta--;
    			//fps cap
    			canRender = true;
    		}else canRender = false;
    		
    		
    		
    		try{
    			Thread.sleep(1);
    		} catch(InterruptedException e) {
    			e.printStackTrace();
    		}
    		
    		if(canRender){
    			frames++;
    			render();
    		}
    		
    		if(System.currentTimeMillis() - timer > 1000){
    			timer+=1000;
    			System.out.println(ticks + "Tickss, FPS: " + frames);;
    			frame.setTitle(TITLE + "      Ticks: " + ticks + "   FPS: "+ frames); 
    			ticks = 0;
    			frames = 0;
    		}
    	}
        System.exit(0);
	}
    
    /**
     * creates new JFrame and sets some default values like default close operation or resizable = true
     * starts the game (start() method from above) -> game loop from run()
     * @param args
     */
	public static void main(String args[]){
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setTitle(TITLE);
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		//Window in Center of Screen
		frame.setLocationRelativeTo(null);
		//anti bug
		frame.setResizable(false);
		//you can see the window
		frame.setVisible(true);
		//creating final JFrame
		frame.pack();
		game.start();
		
	}
}
