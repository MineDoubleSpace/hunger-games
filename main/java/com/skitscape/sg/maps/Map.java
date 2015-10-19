package com.skitscape.sg.maps;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.skitscape.sg.Core;
import com.skitscape.sg.util.Download;
import com.skitscape.sg.util.Files;
import com.skitscape.sg.util.Log;
import com.skitscape.sg.util.Zip;

public class Map {

	private final String MAP_SERVER = "http://";
	private World w;
	private MapConfig conf;

	public void load(String map) {
		try {
			File copyTo = new File(Core.get().getDataFolder().getParentFile().getAbsoluteFile().getParent() + "/map");
			if (copyTo.exists()) {
				Log.log("--- Deleting existing world ---");
				Bukkit.getServer().unloadWorld("map", false);
			}
			File world = new File(Core.get().getDataFolder().toString() + "/" + "world.zip");
			Download.start(MAP_SERVER + map, world);
			Zip.unzip(world.getAbsolutePath(), Core.get().getDataFolder() + "/" + "extact");

			if (copyTo.exists()) {
				copyTo.delete();
			}
			Log.log("--- Copying downloaded files to world --- ");
			Files.copyFolder(new File(Core.get().getDataFolder().toString() + "/extact/world"), copyTo);
			Log.log("--- Loading new map ---");
			w = Bukkit.getServer().createWorld(new WorldCreator("map"));
			conf = new MapConfig();
			conf.loadConfig(new File(Core.get().getDataFolder() + "/" + "extact/world.yml"));
			Log.log("--- Map has been loaded ---");
			MapChest chest = new MapChest();
			chest.load();
			//Cleaning up
			Log.log("--- Cleaning up ---");
			world.delete();
		} catch (Exception e) {
			Log.log(e);
		}
	}

	public World getWorld() {
		return this.w;
	}

	public MapConfig getConfig() {
		return this.conf;
	}
}
