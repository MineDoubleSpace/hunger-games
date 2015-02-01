package com.skitscape.sg.game;

public class GameState {
	
	private static GameStatus status;
	
	public static GameStatus getStatus() {
		return status;
	}
	
	public static void setStatus(GameStatus s) {
		status = s;
	}
	
	public static boolean isLoading() {
		return status == GameStatus.LOADING;
	}
	
	public static boolean isWaiting() {
		return status == GameStatus.WAITING || status == GameStatus.STARTING;
	}
	
	public static boolean hasStarted() {
		return status == GameStatus.STARTED;
	}
	
	public static boolean isEnding() {
		return status == GameStatus.ENDING;
	}
	
	public static boolean isRestarting() {
		return status == GameStatus.RESTARTING;
	}
	

	public enum GameStatus {
		LOADING, WAITING, STARTING, STARTED, ENDING, RESTARTING
	}
	
}
