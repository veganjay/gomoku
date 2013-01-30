package com.veganjay.game.gomoku;

public class Logger {

	public static final String DEBUG_PROPERTY = "gomoku.debug";
	
	private boolean enabled = false;
	private String  component = "logger";	
	
	public Logger(String component) {
		String gameDebug = System.getProperty(DEBUG_PROPERTY);
		
		if (gameDebug != null && gameDebug.equals("true")) {
			enabled = true;
		}
		this.component = component;
	}
	
	public void debug(String msg) {
		if (enabled) {
			System.out.print(component);
			System.out.print(":");
			System.out.println(msg);			
		}
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
}
