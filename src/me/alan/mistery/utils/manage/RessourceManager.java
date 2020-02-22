package me.alan.mistery.utils.manage;

public abstract class RessourceManager {
	
	
	protected int count = 1;
	
	public void addReference(){
		count++;
	}
	
	public boolean removeRef(){
		count--;
		return count == 0;
	}
}
