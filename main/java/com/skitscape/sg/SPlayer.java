package com.skitscape.sg;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.skitscape.sg.game.GameState;
import com.skitscape.sg.game.GameState.GameStatus;

public class SPlayer {

	private static List<SPlayer> players = Lists.newArrayList();

	//player info
	private Player player;
	private PlayerStatus status;

	public SPlayer(Player player, PlayerStatus status) {
		this.player = player;
		this.status = status;
		players.add(this);
	}
	
	public void lobby() {
		
	}

	public void RemovePlayer(SPlayer p) {
		if (players.contains(p)) players.remove(p);
	}

	public PlayerStatus getStatus() {
		return this.status;
	}

	public void sendMessage(String msg) {
		this.player.sendMessage(msg);
	}
	
	public void makeSpectator() {
		this.status = PlayerStatus.SPECTATOR;
		this.player.setFlying(true);
		
		//hide sepectator 
		for (SPlayer sp : getPlayers()) {
			if (sp.status == PlayerStatus.PLAYER) {
				sp.player.hidePlayer(this.player);
			}
		}
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
	
	public static void sendMessageAll(String msg) {
		for (SPlayer sp : getPlayers()) {
			sp.sendMessage(msg);
		}
	}
	
	public static List<SPlayer> getPlayers() {
		return players;
	}

	public enum PlayerStatus {
		PLAYER, SPECTATOR
	}

}
