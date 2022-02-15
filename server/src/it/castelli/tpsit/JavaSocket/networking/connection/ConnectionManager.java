package it.castelli.tpsit.JavaSocket.networking.connection;

import it.castelli.tpsit.JavaSocket.GlobalData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the connections with the clients
 */
public class ConnectionManager extends Thread {
    private final ArrayList<ClientConnection> connections = new ArrayList<>();

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(GlobalData.PORT);
            System.out.println("Server listening on " + GlobalData.PORT);
            // TODO: find a way to not use while(true)
            while (true) {
                Socket newClientSocket = serverSocket.accept();
                connections.add(new ClientConnection(newClientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle connection error
        }
    }

    @Override
    public void interrupt() {
        for (var connection : connections) {
            connection.interrupt();
        }
        super.interrupt();
    }
}
