package com.skitscape.sg;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.skitscape.sg.SPlayer.PlayerStatus;
import com.skitscape.sg.game.GameTimer;
import com.skitscape.sg.listeners.PlayerListener;
import com.skitscape.sg.maps.Map;
import com.skitscape.sg.maps.MapConfig;

public class Core extends JavaPlugin {

	//Game instance 
	private static Core instance;
	public static boolean debug = true;

	//Game info
	private int maxPlayers;
	private int minPlayers;
	private int joinedMaxPlayers;

	private GameTimer timer;

	private Map map;

	@Override
	public void onEnable() {
		instance = this;

		registerEvents();

		reload();

		map = new Map();
		map.load("map.zip");

		//Start game tasks
		timer = new GameTimer();
		timer.startTask();

		registerPlayers();
	}

	public void reload() {
		saveDefaultConfig();
		reloadConfig();
		//temp one for testing
		minPlayers = getConfig().getInt("min-players", 2);
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
	
	public MapConfig getMapConfig() {
		return this.map.getConfig();
	}

	public static Core get() {
		return instance;
	}

}
