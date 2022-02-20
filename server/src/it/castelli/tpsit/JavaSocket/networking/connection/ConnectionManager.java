package it.castelli.tpsit.JavaSocket.networking.connection;

import it.castelli.tpsit.JavaSocket.GlobalData;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

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

    public void removeConnection(String username) {
        loggedConnections.get(username).interrupt();
        loggedConnections.remove(username);
    }

    public void removeConnection(ClientConnection connection) {
        unloggedConnections.remove(connection);
        loggedConnections.values().remove(connection);
    }
}
