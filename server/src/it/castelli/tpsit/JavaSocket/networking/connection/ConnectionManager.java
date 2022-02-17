package it.castelli.tpsit.JavaSocket.networking.connection;

import it.castelli.tpsit.JavaSocket.GlobalData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles the connections with the clients
 */
public class ConnectionManager extends Thread {
    private final ArrayList<ClientConnection> unloggedConnections = new ArrayList<>();
    private final HashMap<String, ClientConnection> loggedConnections = new HashMap<>();

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
            // TODO: handle connection error
        }
    }

    /**
     * Sends a message to a specific user
     *
     * @param username The username of the user
     * @param message  The message to send
     */
    public void send(String username, String message) {
        ClientConnection clientConnection = loggedConnections.get(username);
        if (clientConnection == null) throw new IllegalArgumentException("Invalid username");
        clientConnection.send(message);
    }

    /**
     * Sends a message to every connected client
     *
     * @param message The message to send
     */
    public void broadcast(String message) {
        for (var client : loggedConnections.values()) client.send(message);
    }

    /**
     * Register a new connection as "logged" (with a username)
     * Removes the previous anonymous one
     *
     * @param username   The unique username of the connection
     * @param connection The connection to register
     */
    public void logConnection(String username, ClientConnection connection) {
        if (loggedConnections.containsKey(username))
            throw new IllegalArgumentException("Trying to add an already logged user");
        else {
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
        super.interrupt();
    }
}
