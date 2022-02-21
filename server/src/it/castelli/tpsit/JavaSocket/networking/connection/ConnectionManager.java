package it.castelli.tpsit.JavaSocket.networking.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.GlobalData;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Handles the connections with the clients
 */
public class ConnectionManager extends Thread {
	private final ConcurrentLinkedQueue<ClientConnection> unloggedConnections = new ConcurrentLinkedQueue<>();
	private final ConcurrentHashMap<String, ClientConnection> loggedConnections = new ConcurrentHashMap<>();

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(GlobalData.PORT);
			System.out.println("Server listening on " + GlobalData.PORT);
			// TODO: find a way to not use while(true)
			while (true) {
				Socket newClientSocket = serverSocket.accept();
				unloggedConnections.add(new ClientConnection(newClientSocket));
			}
		} catch (IOException e) {
			e.printStackTrace();
			interrupt();
		}
	}

	/**
	 * Converts a message to json, then sends it to a specific user
	 *
	 * @param username The username of the user
	 * @param message  The message to send
	 */
	public void send(String username, Message message) {
		ClientConnection clientConnection = loggedConnections.get(username);
		if (clientConnection == null) throw new IllegalArgumentException("Invalid username");
		clientConnection.send(message);
	}

	/**
	 * Converts a message to json, then sends it to every connected client
	 *
	 * @param message The message to send
	 */
	public void broadcast(Message message) {
		for (var client : loggedConnections.values()) client.send(message);
		for (var client : unloggedConnections) client.send(message);
	}

	/**
	 * Register a new connection as "logged" (with a username) Removes the previous anonymous one
	 *
	 * @param username   The unique username of the connection
	 * @param connection The connection to register
	 */
	public void logConnection(String username, ClientConnection connection) {
		if (loggedConnections.containsKey(username)) {
			throw new IllegalArgumentException("Trying to add an already logged user");
		} else {
			loggedConnections.put(username, connection);
			connection.setUsername(username);
			unloggedConnections.remove(connection);
		}
	}

	@Override
	public void interrupt() {
		for (var connection : unloggedConnections) {
			connection.interrupt();
		}
		unloggedConnections.clear();
		loggedConnections.clear();
		super.interrupt();
	}

	/**
	 * Disconnects a specific connection
	 *
	 * @param username The username of the connection
	 */
	public void removeConnection(String username) {
		loggedConnections.get(username).interrupt();
		loggedConnections.remove(username);
	}

	public void removeConnection(String username, boolean sendMessage) {
		if (!loggedConnections.containsKey(username))
			System.err.println("The username is not registered");
		try {
			if (sendMessage) {
				String jsonSubMessage = JsonSerializer.serialize(new Message.StringMessage("Disconnecting"));
				Message stopMessage = new Message(Message.STOP_MESSAGE, "", 0, jsonSubMessage);
				loggedConnections.get(username).send(stopMessage);
			}
			removeConnection(username);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Disconnects a specific connection
	 *
	 * @param connection The connection to stop
	 */
	public void removeConnection(ClientConnection connection) {
		unloggedConnections.remove(connection);
		loggedConnections.values().remove(connection);
	}

	/**
	 * Disconnects a specific connection
	 *
	 * @param connection  The connection to stop
	 * @param sendMessage Whether to send a stop message or not
	 */
	public void removeConnection(ClientConnection connection, boolean sendMessage) {
		try {
			if (sendMessage) {
				String jsonSubMessage = JsonSerializer.serialize(new Message.StringMessage("Disconnecting"));
				Message stopMessage = new Message(Message.STOP_MESSAGE, "", 0, jsonSubMessage);
				connection.send(stopMessage);
			}
			connection.interrupt();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all the unlogged connections
	 */
	public void removeUnloggedConnections() {
		for (var connection : unloggedConnections) {
			connection.interrupt();
		}
	}

	/**
	 * Removes all the unlogged connections
	 *
	 * @param sendMessage Whether to send a stop message or not
	 */
	public void removeUnloggedConnections(boolean sendMessage) {
		if (sendMessage) {
			for (var connection : unloggedConnections) {
				removeConnection(connection, true);
			}
		} else {
			removeUnloggedConnections();
		}
	}
}
