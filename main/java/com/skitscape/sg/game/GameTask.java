package com.skitscape.sg.game;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;

import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.game.GameState.GameStatus;
import com.skitscape.sg.util.Log;

public class GameTask {
	int STARTING_GAME = 5;
	int GAME_TIME = 1200;
	int STARTING_FINAL = 60;
	int LIGHTNING = 20;
	boolean isLIGHTNING = false;

	public void onSecond() {
		//Skip all tasks if game is still loading
		if (GameState.isLoading()) return;
		if (GameState.isWaiting()) {
			if (Core.get().getMinPlayers() <= SPlayer.waitingCount()) {
				GameState.setStatus(GameStatus.STARTING);
			}
		}

		if (GameState.isStarting()) {
			starting(STARTING_GAME);
			if (STARTING_GAME == 1) {
				start();
			}
			STARTING_GAME--;
		}

		if (GameState.isOn()) {
			if (SPlayer.playingCount() == 1) {
				ending();
			}
		}
		if (GameState.hasStarted()) {
			if (GAME_TIME <= 60 || ((SPlayer.playingCount() * 100) / Core.get().getJoinedMaxPlayers()) <= 13) {
				//Starting death match
				GameState.setStatus(GameStatus.FINAL);
			}
			if (isLIGHTNING) {
				if (LIGHTNING == 0) {
					SPlayer.lightning();
					LIGHTNING = 20;
				}
				LIGHTNING--;
			} else if (((SPlayer.playingCount() * 100) / Core.get().getJoinedMaxPlayers()) <= 30) {
				isLIGHTNING = true;
				SPlayer.lightning();
			}
			GAME_TIME--;

		}
		if (GameState.isFinal()) {
			timedMessage(STARTING_FINAL, new int[] { 60, 30, 10, 5, 4, 3, 2, 1 }, ChatColor.DARK_RED + "Death match starting in %time% seconds!");

			if (STARTING_FINAL == 1) {
				deathMatch();
			}
			STARTING_FINAL--;
		}
	}

	public void start() {
		Core.get().setJoinedMaxPlayers(SPlayer.waitingCount());
		SPlayer.sendMessageAll(ChatColor.GREEN + "" + ChatColor.BOLD + "Match started!");
		GameState.setStatus(GameStatus.STARTED);
	}

	public void starting(int i) {
		GameState.setStatus(GameStatus.STARTING);
		int[] in = new int[] { 30, 20, 10, 5, 4, 3, 2, 1 };
		timedMessage(i, in, ChatColor.GREEN + "Match starting in " + ChatColor.RED + "%time%" + ChatColor.GREEN + " seconds");
	}

	public void deathMatch() {
		//Teleport players to center

	}

	public void timedMessage(int i, int[] in, String message) {
		if (ArrayUtils.contains(in, i)) {
			String msg = message.replaceAll("%time%", Integer.toString(i));
			SPlayer.sendMessageAll(msg);
			Log.log(msg);
		}
	}

	public void ending() {
		GameState.setStatus(GameStatus.ENDING);
		//End the game and do something with winner
	}

}
