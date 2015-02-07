package com.skitscape.sg.game;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.game.GameState.GameStatus;
import com.skitscape.sg.util.Log;

public class GameTask {
	int STARTING_GAME = 20;
	int GAME_TIME = 1200;
	int STARTING_FINAL = 60;
	int LIGHTNING = 20;
	int ENDING = 30;
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
		if (GameState.isEnding()) {
			if (ENDING != 0) {
				timedMessage(ENDING, new int[] { 20, 10, 5, 4, 3, 2, 1 }, "&aGame ending in &c%time% &aseconds");
				firework();
				if (ENDING == 1) {
					GameState.setStatus(GameStatus.RESTARTING);
					SPlayer.kickAll();
				}
				ENDING--;
			}
		}
		if (GameState.isRestarting()) {
			restart();
		}
	}

	private void restart() {
		Bukkit.getServer().shutdown();
		//TODO clean up map, extract location, world.zip
		//TODO check for update and update if available
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
		SPlayer.sendMessageAll("&9&l" + SPlayer.getWinner().getName() + "&f&a has won survival games!");

	}

	private void firework() {
		Firework firework = (Firework) Core.get().getMap().getWorld().spawn(SPlayer.getWinner().getPlayer().getLocation().add(0, 2, 0), Firework.class);

		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(Type.CREEPER).withColor(Color.GREEN).withFade(Color.BLUE).build());
		meta.setPower(0);
		firework.setFireworkMeta(meta);
	}

}
