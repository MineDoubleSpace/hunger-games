package com.skitscape.sg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.skitscape.sg.Core;
import com.skitscape.sg.SPlayer;
import com.skitscape.sg.SPlayer.PlayerStatus;
import com.skitscape.sg.game.GameState;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (GameState.isWaiting() || SPlayer.playingCount() < Core.getInstance().getMaxPlayers()) {
			SPlayer sp = new SPlayer(event.getPlayer(), PlayerStatus.PLAYER);
			sp.lobby();
		}else {
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
		} if (event.getEntity() instanceof Player) {
			if (SPlayer.getSPlayer((Player) event.getEntity()).getStatus() == PlayerStatus.SPECTATOR) {
				event.setCancelled(true);
			}
		}
	}

}
