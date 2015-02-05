package com.skitscape.sg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.SPlayer.PlayerStatus;
import com.skitscape.sg.game.GameState;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (GameState.isWaiting() || SPlayer.playingCount() < Core.get().getMaxPlayers()) {
			SPlayer sp = new SPlayer(event.getPlayer(), PlayerStatus.PLAYER);
			event.getPlayer().teleport(Core.get().getMap().getConfig().getNextAvailable(sp));
		} else {
			SPlayer sp = new SPlayer(event.getPlayer(), PlayerStatus.SPECTATOR);
			sp.makeSpectator();
		}
	}

	@EventHandler
	public void OnPVP(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			SPlayer sp = SPlayer.getSPlayer((Player) event.getDamager());
			if (sp.getStatus() == PlayerStatus.SPECTATOR) {
				event.setCancelled(true);
			}
		}
		if (event.getEntity() instanceof Player) {
			if (SPlayer.getSPlayer((Player) event.getEntity()).getStatus() == PlayerStatus.SPECTATOR) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		SPlayer sp = SPlayer.getSPlayer(e.getPlayer());
		if (sp.getStatus() != PlayerStatus.ADMIN) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		SPlayer sp = SPlayer.getSPlayer(event.getPlayer());
		if (GameState.isWaiting()) {
			Core.get().getMapConfig().clearSpawn(sp.getSpawnid());
		}
		sp.remove();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (GameState.isWaiting()) {
			if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
				e.getPlayer().teleport(e.getFrom());
			}
		}
	}

}
