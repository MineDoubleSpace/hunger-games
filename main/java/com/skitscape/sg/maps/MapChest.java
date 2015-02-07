package com.skitscape.sg.maps;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.skitscape.sg.Core;
import com.skitscape.sg.util.Log;

public class MapChest {

	World w = Core.get().getMap().getWorld();
	ArrayList<Block> chests = new ArrayList<Block>();

	public void load() {
		World world = Core.get().getMap().getWorld();

		CuboidSelection sel = new CuboidSelection(world, Core.get().getMaxLoc(), Core.get().getMinLoc());

		if (sel instanceof CuboidSelection) {
			Vector min = sel.getNativeMinimumPoint();
			Vector max = sel.getNativeMaximumPoint();
			Log.debug("Min: " + sel.getNativeMinimumPoint());
			Log.debug("Max: " + sel.getNativeMaximumPoint());
			for (int x = min.getBlockX(); x <= max.getBlockX(); x = x + 1) {
				for (int y = min.getBlockY(); y <= max.getBlockY(); y = y + 1) {
					for (int z = min.getBlockZ(); z <= max.getBlockZ(); z = z + 1) {
						Location b = new Location(world, x, y, z);
						if (b.getBlock().getType() == Material.CHEST) {
							chests.add(b.getBlock());
						}
					}
				}
			}
			Log.debug(ChatColor.AQUA + "Chests found: " + chests.size());
			for (Block b : chests) {
				if (b.getState() instanceof Chest) {
					Chest ch = (Chest) b.getState();
					ch.getInventory().clear();
					Random rn = new Random();
					for (int num = rn.nextInt(3) + 1; num > 0; num--) {
						int chance = rn.nextInt(8);
						switch (chance) {
						case 0:
							ch.getInventory().addItem(new ItemStack(Material.APPLE, 3));
							break;
						case 1:
							ch.getInventory().addItem(new ItemStack(Material.WOOD_SWORD, 1));
							break;
						case 2:
							ch.getInventory().addItem(new ItemStack(Material.STONE_SWORD, 1));
							break;
						case 3:
							ch.getInventory().addItem(new ItemStack(Material.POISONOUS_POTATO, 5));
							break;
						case 4:
							ch.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 5));
							break;
						case 5:
							ch.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
							break;

						case 6:
							ch.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS, 1));
							break;
						case 7:
							ch.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET, 1));
							break;
						}
					}

				}
			}
		} else {
			Log.debug(ChatColor.DARK_RED + "Invalid Selection!");
		}
	}
}
