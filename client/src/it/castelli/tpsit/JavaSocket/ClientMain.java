package it.castelli.tpsit.JavaSocket;

import it.castelli.tpsit.JavaSocket.console.ClientCommandProcessor;
import it.castelli.tpsit.JavaSocket.console.ConsoleListener;
import it.castelli.tpsit.JavaSocket.networking.connection.Connection;

public class ClientMain {
	private static Connection connection;
	private static final ConsoleListener consoleListener = new ConsoleListener();

	public static void main(String[] args) {
		new ClientCommandProcessor().init();
		consoleListener.start();
	}

	public static Connection getConnection() {
		return connection;
	}

	/**
	 * Generates the Connection thread and starts it
	 */
	public static void generateConnection() {
		connection = new Connection();
		connection.start();
	}

	/**
	 * Stops all the threads
	 */
	public static void stop() {
		connection.interrupt();
		consoleListener.interrupt();
		System.exit(0);
	}
}
