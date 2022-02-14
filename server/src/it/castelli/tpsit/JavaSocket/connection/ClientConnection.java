package it.castelli.tpsit.JavaSocket.connection;

import java.io.*;
import java.net.Socket;

/**
 * Handles a connection with a single client (input and output)
 */
public class ClientConnection extends Thread {
    private final Socket socket;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        try {
            System.out.println("New active connection at " + socket.getInetAddress().getHostName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // TODO: receive client messages
            // TODO: handle client messages
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle error
        }
        super.interrupt();
    }
}
