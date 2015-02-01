package com.skitscape.sg.game;

import java.util.Arrays;

import org.bukkit.ChatColor;

import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.game.GameState.GameStatus;

public class GameTask {
	int starting = 30;
	int gametime = 600;
	
	public void onSecond() {
		//Skip all tasks if game is still loading
		if (GameState.isLoading()) return;
		if (GameState.isWaiting()) {
			if (Core.getInstance().getMinPlayers() <= SPlayer.waitingCount()) {
				starting(starting);
				starting--;
				if (starting == 1) {
					start();
				}
			}
		}if (GameState.hasStarted()) {
			if (SPlayer.playingCount() != 1) {
				gametime--;
			}else {
				//We have a winner!
				ending();
			}
		}
	}
	
	
	public void start() {
		GameState.setStatus(GameStatus.STARTED);
	}
	
	public void starting(int i) {
		GameState.setStatus(GameStatus.STARTING);
		int[] in = new int[] {30, 20, 10, 5, 4, 3, 2, 1};
		if (Arrays.asList(in).contains(i)) {
			SPlayer.sendMessageAll(ChatColor.GREEN + "Match starting in " + ChatColor.RED + i + " seconds");
		}
	}
	
	public void ending() {
		
	}
	
}
