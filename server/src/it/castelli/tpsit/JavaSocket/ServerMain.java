package it.castelli.tpsit.JavaSocket;

import it.castelli.tpsit.JavaSocket.connection.ConnectionManager;

public class ServerMain {
	private static ConnectionManager connectionManager;

	public static void main(String[] args) {
		connectionManager = new ConnectionManager();
		connectionManager.start();
	}

	public static ConnectionManager getConnectionManager() {
		return connectionManager;
	}
}
