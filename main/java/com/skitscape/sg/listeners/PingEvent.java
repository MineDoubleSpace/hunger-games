package com.skitscape.sg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.skitscape.sg.Core;

public class PingEvent implements Listener{
	
	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		event.setMaxPlayers(Core.get().getMaxPlayers());
	}

}
