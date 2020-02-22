package me.alan.mistery.rendering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation {
	
	//how many frames have been rendered
	private int count;
	//make sure when rendering it doesn't animate too fast/slow
	private int index;
	private int speed;
	private int numFrames;
	private Texture currentFrame;
	private Texture[] frames;
	private BufferedImage[] bufferedFrames;
	private boolean firstFramePlayed;
	public boolean playedOnce;
	
	// "..." dont need to initialise Textures all the time
	public Animation(int speed, Texture... frames){
		this.speed = speed;
		this.frames = frames;
		this.numFrames = frames.length;
		playedOnce = false;
	}
	
	//switch from current frame to the next one
	private void nextFrame(){
		//using index for frame and then add 1 to index
		currentFrame = frames[index++];
		//reseting the array of frames so that we don't go out of the array
		if(index >= numFrames){
			playedOnce = true;
			index = 0;
		}
		
	}
	public BufferedImage getImage() { return bufferedFrames[index]; }
	
	public void animate(){
		playedOnce = false;
		if(!firstFramePlayed)
			nextFrame();
		firstFramePlayed = true;
		//add 1 to count and check. keeping track of animation speed
		count++;
		if(count > speed){
			count = 0;
			nextFrame();
			
		}		
	}
	
	public void render(Graphics2D g, double x, double y){
		//avoiding errors with rendering null
		if(currentFrame != null){
			currentFrame.render(g, x, y);
			
		}
	}
	
}
