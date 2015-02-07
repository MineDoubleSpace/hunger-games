package com.skitscape.sg.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.SPlayer.PlayerStatus;
import com.skitscape.sg.game.GameState;
import com.skitscape.sg.util.StringUtil;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (GameState.isLoading()) {
			event.getPlayer().kickPlayer("Server still loading!");
		}
		if (GameState.isWaiting() || SPlayer.waitingCount() < Core.get().getMaxPlayers()) {
			SPlayer sp = new SPlayer(event.getPlayer(), PlayerStatus.PLAYER);
			event.setJoinMessage(StringUtil.format("&6%s &ahas joined the game. [&6%s/%s&9]", sp.getName(), SPlayer.waitingCount(), Core.get().getMaxPlayers()));
			event.getPlayer().teleport(Core.get().getMap().getConfig().getNextAvailable(sp));
		} else {
			SPlayer sp = new SPlayer(event.getPlayer(), PlayerStatus.SPECTATOR);
			sp.makeSpectator();
		}
	}

	@EventHandler
	public void OnPVP(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			SPlayer sp = SPlayer.getSPlayer((Player) event.getDamager());
			if (sp.getStatus() == PlayerStatus.SPECTATOR) {
				event.setCancelled(true);
			}
		}
		//TODO prevent spectator from damaging entities
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		SPlayer sp = SPlayer.getSPlayer(e.getPlayer());
		if (sp.getStatus() != PlayerStatus.ADMIN) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		SPlayer sp = SPlayer.getSPlayer(e.getPlayer());
		if (sp.getStatus() != PlayerStatus.ADMIN) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		SPlayer sp = SPlayer.getSPlayer(e.getEntity());
		if (GameState.isOn()) {
			sp.setDeathLocation(e.getEntity().getLocation());
			sp.makeSpectator();
			sp.sendMessage(ChatColor.RED + "Ouch!");
			e.setDeathMessage(StringUtil.format("&6%s &4was killed by &6%s.", e.getEntity().getName(), e.getEntity().getKiller().getName()));
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(SPlayer.getSPlayer(event.getPlayer()).getDeathLocation());
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		SPlayer sp = SPlayer.getSPlayer(event.getPlayer());
		if (GameState.isWaiting()) {
			Core.get().getMapConfig().clearSpawn(sp.getSpawnid());
		}
		sp.remove();
		event.setQuitMessage(StringUtil.format("&6%s &cleft this awesome game.", event.getPlayer().getName()));
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (GameState.isWaiting()) {
			Location spawn = SPlayer.getSPlayer(e.getPlayer()).getSpawn();
			if (spawn.getBlockX() != e.getTo().getBlockX() || spawn.getBlockY() != e.getTo().getBlockY() || spawn.getBlockZ() != e.getTo().getBlockZ()) {
				e.getPlayer().teleport(spawn);
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (SPlayer.getSPlayer(event.getPlayer()).getStatus() == PlayerStatus.SPECTATOR) {
			event.setCancelled(true);
		}
	}

}
