package com.skitscape.sg;

import org.bukkit.plugin.java.JavaPlugin;

import com.skitscape.sg.game.GameTimer;

public class Core extends JavaPlugin {
	
	//Game instance 
	private static Core instance;
	public static boolean debug = true;
	
	//Game settings
	private int maxPlayers;
	private int minPlayers;
	
	@Override
	public void onEnable() {
		instance = this;
		reload();
		
		//Start game tasks
		GameTimer timer = new GameTimer();
		timer.startTask();
	}


	public void reload() {
		saveDefaultConfig();
		reloadConfig();
		
		
		/*
		 * 
		 * spawns:
		 *   spawn1:
		 *     x: 1 
		 * 
		 */

		minPlayers = getConfig().getInt("min-players", 5);
	}
	
	public static Core getInstance() {
		return instance;
	}
	
	
	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getMinPlayers() {
		return minPlayers;
	}
	
	

}
