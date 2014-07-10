package com.egs.dungeon;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;


public class MainComponent extends Canvas implements Runnable{
	
	private Random random;
	private Game game;
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private boolean running = false;
	
	private int fps = 0;
	private int ups = 0;
	
	private long seed;
	
	public MainComponent(){
		random = new Random();
		seed = random.nextLong();
		game = new Game(seed, this);
		
	}
	
	public static void main(String[] args){
		MainComponent main = new MainComponent();
		Dimension size = new Dimension(WIDTH, HEIGHT);
		main.setPreferredSize(size);
		main.setMinimumSize(size);
		main.setMinimumSize(size);
		
		JFrame frame = new JFrame("Dungeon Crawler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(main);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		main.start();
	}
	
	public void start(){
		running = true;
		new Thread(this, "main").start();
	}
	
	public void stop(){
		running = false;
	}
	
	public void run(){
		double nsPerFrame = 1000000000.0 / 60.0;
		double unprocessedTime = 0;
		double maxSkipFrames = 10;
		
		long lastTime = System.nanoTime();
		long lastFrameTime = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		while(running){
			long now = System.nanoTime();
			double passedTime = (now - lastTime) / nsPerFrame;
			lastTime = now;
			
			if(passedTime < -maxSkipFrames) passedTime = -maxSkipFrames;
			if(passedTime > maxSkipFrames) passedTime = maxSkipFrames;
			
			unprocessedTime += passedTime;
			
			boolean render = false;
			while(unprocessedTime > 1){
				unprocessedTime -= 1;
				update();
				updates++;
				render = true;
			}
			
			if(render){
				render();
				frames++;
			}
			
			if(System.currentTimeMillis() > lastFrameTime + 1000){
				fps = frames;
				ups = updates;
				System.out.println(fps + "fps, " + ups + "ups");
				lastFrameTime += 1000;
				frames = 0;
				updates = 0;
			}
		}
	}
		
	public void update(){
		game.update();
	}
	
	public void render(){		
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(new Color(25, 25, 25));
		g.fillRect(0, 0, WIDTH + 2, HEIGHT + 2);

		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.setColor(Color.WHITE);
		
		game.render(g);
		
		g.dispose();
		bs.show();
	}
}
