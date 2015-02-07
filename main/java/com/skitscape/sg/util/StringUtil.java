package com.skitscape.sg.util;

import org.bukkit.ChatColor;

import com.skitscape.sg.Core;

public class StringUtil {
	
	public static String format (String str, Object... args) {
		return ChatColor.translateAlternateColorCodes('&', String.format(Core.getPrefix() + str, args));
	}

}
