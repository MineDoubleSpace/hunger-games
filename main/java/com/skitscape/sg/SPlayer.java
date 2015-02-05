package com.skitscape.sg;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.skitscape.sg.game.GameState;
import com.skitscape.sg.game.GameState.GameStatus;

public class SPlayer {

	private static List<SPlayer> players = Lists.newArrayList();

	//player info
	private Player player;
	private PlayerStatus status;
	private int spawnid;
	private Location spawn;
	private Location deathLocation;

	public SPlayer(Player player, PlayerStatus status) {
		this.player = player;
		this.status = status;
		players.add(this);
	}

	public void remove() {
		players.remove(this);
	}

	public void sendMessage(String msg) {
		this.player.sendMessage(msg);
	}

	public void makeSpectator() {
		this.status = PlayerStatus.SPECTATOR;
		this.player.setAllowFlight(true);
		this.player.setFlying(true);

		//hide sepectator 
		for (SPlayer sp : getSPlayers()) {
			if (sp.status == PlayerStatus.PLAYER) {
				sp.player.hidePlayer(this.player);
			}
		}
	}

	public void setStatus(PlayerStatus s) {
		this.status = s;
	}

	public static SPlayer getSPlayer(Player p) {
		for (SPlayer sp : players) {
			if (sp.player.getUniqueId() == p.getUniqueId()) return sp;
		}
		return null;
	}

	public static int waitingCount() {
		return players.size();
	}

	public static List<SPlayer> getSPlayers() {
		return players;
	}

	public static int playingCount() {
		if (GameState.getStatus() == GameStatus.STARTED) {
			int count = 0;
			for (SPlayer sp : players) {
				if (sp.status == PlayerStatus.PLAYER) count++;
			}
			return count;
		}
		return 0;
	}

	public static void lightning() {
		for (SPlayer sp : players) {
			if (sp.getStatus() == PlayerStatus.PLAYER) {
				Core.get().getMap().getWorld().strikeLightningEffect(sp.getPlayer().getLocation());
			}
		}
	}

	public static void sendMessageAll(String msg) {
		for (SPlayer sp : getSPlayers()) {
			sp.sendMessage(msg);
		}
	}

	public PlayerStatus getStatus() {
		return this.status;
	}

	public Player getPlayer() {
		return this.player;
	}

	public int getSpawnid() {
		return spawnid;
	}

	public void setSpawnid(int spawnid) {
		this.spawnid = spawnid;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public Location getDeathLocation() {
		return deathLocation;
	}

	public void setDeathLocation(Location death) {
		this.deathLocation = death;
	}

	public enum PlayerStatus {
		PLAYER, SPECTATOR, ADMIN
	}

}
