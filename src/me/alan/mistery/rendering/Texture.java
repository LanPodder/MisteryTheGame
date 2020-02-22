package me.alan.mistery.rendering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


public class Texture {
	
	//helps with memory saving 
	private final static Map<String, BufferedImage> textMap = new HashMap<String, BufferedImage>();
	private BufferedImage image;
	private String fileName;
	private int width, height;
	
	
	/**
	 * constructor for texture:
	 * fileName field is set to the input nameFile
	 * temporary BufferedImage oldTexture equals the name in the HashMap
	 * if oldTexture is null it means the texture has not been loaded and it loads it in a try{}catch{} to prevent errors when the texture file does not exist
	 * then when it is loaded it is put in the HashMap.
	 * width and height is set to the width and height of the image
	 * @param nameFile
	 */
	public Texture(String nameFile){
		this.fileName = nameFile;
		BufferedImage oldTexture = textMap.get(nameFile);
		if(oldTexture != null){
			this.image = oldTexture;
		}else{
			try{
				System.out.println("Loading Texture : " + nameFile);
				// get from default path
				this.image = ImageIO.read(new File("stuff/Textures/" + nameFile + ".png"));
				textMap.put(nameFile, image);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	/**
	 * width and height is set accordingly to the input, temporary String key and temp. BufferedImage old in order to compare the image with existing versions
	 * if there are no existing versions of the current image [if(old != null)]
	 * @param spriteSheet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Texture(Texture spriteSheet, int x, int y, int width, int height){
		this.width = width;
		this.height = height;
		//spriteSheet array selection thing (?)
		String key = spriteSheet.fileName + "_" + x + "_" + y;
		BufferedImage old = textMap.get(key);
		if(old != null) this.image = old;
		else this.image = spriteSheet.image.getSubimage(x * width - width,
								y * height - height, width, height);
	}
	
	public Texture(Texture spriteSheet, int x, int y, int size){
		this(spriteSheet, x, y, size, size);
	}
	
	
	public void render(Graphics2D g, double x, double y){
		g.drawImage(image, (int) x, (int) y, null);
		
	}
	
	public BufferedImage getImage() { return image; }
	
	public void render(Graphics2D g, int destX1, int destX2, int srcX1, int srcX2, int y){
		g.drawImage(image, destX1, y, destX2, y + height, srcX1, 0, srcX2, height, null);
	}
	

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}
