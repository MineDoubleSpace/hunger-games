package com.skitscape.sg.game;

import java.util.HashMap;

import org.bukkit.Location;

import com.skitscape.sg.Core;

public class GameSpawn {

	//True if available
	static HashMap<String, Boolean> spawns = new HashMap<String, Boolean>();

	public static void LoadSpawns() {

	}

	public static Location nextAvailableSpawn() {
		for (String spawn : spawns.keySet()) {
			if (spawns.get(spawn)) return formatString(spawn);
		}
		return formatString((String) spawns.keySet().toArray()[0]);
	}

	public static void setSpawnAvailable(Location l) {
		spawns.replace(formatLocation(l), true);
	}

	/*
	 * String format for locations X Y Z Pitch Yaw
	 */
	private static String formatLocation(Location l) {
		String loc = l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ() + " " + l.getPitch() + " " + l.getYaw();
		return loc;
	}

	private static Location formatString(String s) {
		String[] str = s.split(" ");
		int x = Integer.valueOf(str[0]);
		int y = Integer.valueOf(str[2]);
		int z = Integer.valueOf(str[3]);
		float yaw = Float.valueOf(str[5]);
		float pitch = Float.valueOf(str[4]);

		return new Location(Core.get().getServer().getWorld("world"), x, y, z, yaw, pitch);
	}

}
