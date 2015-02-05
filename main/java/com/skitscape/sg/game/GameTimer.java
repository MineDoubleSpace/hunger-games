package com.skitscape.sg.game;

import com.skitscape.sg.Core;

public class GameTimer {

	public void startTask() {
		GameTask task = new GameTask();
		GameRunnable runnable = new GameRunnable(task);
		Core.get().getServer().getScheduler().scheduleSyncRepeatingTask(Core.get(), runnable, 20L, 20L);
	}
}
