package com.skitscape.sg;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.skitscape.sg.SPlayer.PlayerStatus;
import com.skitscape.sg.game.GameState;
import com.skitscape.sg.game.GameState.GameStatus;
import com.skitscape.sg.game.GameTimer;
import com.skitscape.sg.listeners.PlayerListener;
import com.skitscape.sg.maps.Map;
import com.skitscape.sg.maps.MapConfig;
import com.skitscape.sg.util.Log;

public class Core extends JavaPlugin {

	//Game instance 
	private static Core instance;
	public static boolean debug = true;

	//Game info
	private int maxPlayers;
	private int minPlayers;
	private int joinedMaxPlayers;
	private Location minLoc;
	private Location maxLoc;

	private static final String prefix = ChatColor.BLUE + "[" + ChatColor.GREEN + "Survival Games" + ChatColor.BLUE + "]" + ChatColor.RESET;

	private GameTimer timer;

	private Map map;

	@Override
	public void onEnable() {
		instance = this;

		GameState.setStatus(GameStatus.LOADING);

		registerEvents();

		reload();

		map = new Map();
		map.load("map.zip");

		//Start game tasks
		timer = new GameTimer();
		timer.startTask();

		registerPlayers();

		GameState.setStatus(GameStatus.WAITING);
	}

	public void reload() {
		saveDefaultConfig();
		reloadConfig();
		//temp one for testing
		minPlayers = getConfig().getInt("min-players", 3);
	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	//debug method.
	public void registerPlayers() {
		for (Player pl : getServer().getOnlinePlayers()) {
			new SPlayer(pl, PlayerStatus.PLAYER);
		}
	}

	public MapConfig getMapConfig() {
		return this.map.getConfig();
	}

	public WorldEditPlugin getWorldEdit() {
		Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (pl instanceof WorldEditPlugin) {
			return (WorldEditPlugin) pl;
		}
		Log.log(Level.WARNING, "World edit was not found on the server. Shutting down server. Please install WorldEdit and restart the server.");
		getServer().shutdown();
		return null;
	}

	public Location getMaxLoc() {
		return maxLoc;
	}

	public void setMaxLoc(Location maxLoc) {
		this.maxLoc = maxLoc;
	}

	public Location getMinLoc() {
		return minLoc;
	}

	public void setMinLoc(Location minLoc) {
		this.minLoc = minLoc;
	}

	public void setMaxPlayers(int max) {
		this.maxPlayers = max;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

	public int getJoinedMaxPlayers() {
		return joinedMaxPlayers;
	}

	public void setJoinedMaxPlayers(int joinedMaxPlayers) {
		this.joinedMaxPlayers = joinedMaxPlayers;
	}

	public Map getMap() {
		return this.map;
	}
	
	public static String getPrefix() {
		return prefix + " ";
	}

	public static Core get() {
		return instance;
	}

}
