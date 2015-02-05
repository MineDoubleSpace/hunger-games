package com.skitscape.sg.maps;

import java.io.File;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;
import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.util.Log;

public class MapConfig {

	private FileConfiguration mapConf;
	private File mapFile;

	private List<Integer> spawns = Lists.newArrayList();

	public void loadConfig(File file) {
		mapFile = file;
		mapConf = YamlConfiguration.loadConfiguration(mapFile);
		//load the spawn points
		Core.get().setMaxPlayers(mapConf.getConfigurationSection("spawns").getKeys(false).size());
		Log.debug("Max: " + Core.get().getMaxPlayers());
	}
	
	/*
	 * spawns:
	 *   spawn1:
	 *     x: 100
	 *     y: 100
	 *     z: 100
	 *     yaw: 100
	 *     pitch: 100 
	 */
	
	public Location getNextAvailable(SPlayer sp) {
		int id = 0;
		for (int i = 1; i <= Core.get().getMaxPlayers(); i++) {
			if (!spawns.contains(i)) {
				id = i;
				break;
			}
		}
		
		spawns.add(id);
		sp.setSpawnid(id);
		Location loc = getSpawnLocation(id);
		sp.setSpawn(loc);

		return loc;
		
	}
	
	public void clearSpawn(int id) {
		spawns.remove((Integer) id);
	}
	
	private Location getSpawnLocation(int id) {
		String path = "spawns.spawn" + id + "."; 
		double x = getConfig().getDouble(path + "x");
		double y = (double) getConfig().getDouble(path + "y");
		double z = (double) getConfig().getDouble(path + "z");
		float pitch = (float) getConfig().getDouble(path + "pitch");
		float yaw = (float) getConfig().getDouble(path + "yaw");		
		return new Location(Core.get().getMap().getWorld(), x, y, z, yaw, pitch);
	}

	public FileConfiguration getConfig() {
		return mapConf;
	}

}
