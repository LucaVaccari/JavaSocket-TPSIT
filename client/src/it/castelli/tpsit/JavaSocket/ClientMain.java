package it.castelli.tpsit.JavaSocket;

import it.castelli.tpsit.JavaSocket.connection.Connection;

public class ClientMain {
	private static Connection connection;

	public static void main(String[] args) {
		connection = new Connection();
		connection.start();
	}

	public static Connection getConnection() {
		return connection;
	}
}
