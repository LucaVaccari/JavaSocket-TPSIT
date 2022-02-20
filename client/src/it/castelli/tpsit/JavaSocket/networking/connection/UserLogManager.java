package it.castelli.tpsit.JavaSocket.networking.connection;

public class UserLogManager {
	private static boolean logged = false;
	private static String username = "";

	public static boolean isLogged() {
		return logged;
	}

	public static void login() {
		logged = true;
	}

	public static void logout() {
		logged = false;
		username = "";
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		UserLogManager.username = username;
	}
}
