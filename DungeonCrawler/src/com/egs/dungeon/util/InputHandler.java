package com.egs.dungeon.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import com.egs.dungeon.MainComponent;

public class InputHandler implements KeyListener{
	
	public InputHandler(MainComponent main){
		main.addKeyListener(this);
	}
	
	public class Key{
//		public int numPressed;
//		public boolean pressed = false;

		public int presses, absorbs;
		public boolean down, pressed;
		
		public Key(){
			keys.add(this);
		}
		
		public void toggle(boolean pressed){
			if(pressed != down) down = pressed;
			if(pressed) presses++;
		}
		
		public void update(){
			if(absorbs < presses){
				absorbs++;
				pressed = true;
			}else pressed = false;
			
		}		
//		public int getNumPressed(){
//			return presses;
//		}
//		
//		public boolean isPressed(){
//			return pressed;
//		}
//		
//		public void toggle(boolean isPressed){
//			pressed = isPressed;
//			if(pressed){
//				numPressed++;
//			}
//		}
	}
	
	public List<Key> keys = new ArrayList<Key>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	
	public void update(){
		for(Key key : keys) key.update();
	}
	
	public void release(){
		for(int i = 0; i < keys.size(); i++){
			keys.get(i).pressed = false;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) up.toggle(isPressed);
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) down.toggle(isPressed);
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) left.toggle(isPressed);
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) right.toggle(isPressed);
	}
}