package com.skitscape.sg.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.skitscape.sg.Core;

public class Log {

	private static Logger log;

	private static boolean debug = true;

	static {
		log = Core.get().getLogger();
	}

	public static void log(Level lvl, String msg) {
		log.log(lvl, msg);
	}

	public static void log(String msg) {
		log(Level.INFO, msg);
	}

	public static void log(Exception e) {
		log(Level.WARNING, e.getMessage());
		if (debug) e.printStackTrace();
	}

	public static void debug(String msg) {
		if (debug) log("[DEBUG] " + msg);
	}

	public static void log(String log, String debug) {
		log(log);
		if (Log.debug) log(debug);
	}

	public static void setDebugging(boolean debug) {
		Log.debug = debug;
	}

}
