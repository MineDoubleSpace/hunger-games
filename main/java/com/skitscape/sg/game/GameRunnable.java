package com.skitscape.sg.game;

public class GameRunnable implements Runnable {
	
	private GameTask task;
	
	public GameRunnable(GameTask task) {
		this.task = task;
	}

	public void run() {
		task.onSecond();		
	}

}
