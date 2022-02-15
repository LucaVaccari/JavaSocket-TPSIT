package it.castelli.tpsit.JavaSocket.networking.connection;

import it.castelli.tpsit.JavaSocket.GlobalData;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

import java.io.*;
import java.net.Socket;

/**
 * Handles the connection with the server (input and output)
 */
public class Connection extends Thread {
    private String serverAddress = "localhost";
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, GlobalData.PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected with " + serverAddress + " on port " + GlobalData.PORT);
        } catch (IOException e) {
            System.err.println("Error while connecting to the server: invalid address or server unreachable");
        }
        // TODO: find a way to not use while(true)
        while (true) {
            try {
                String jsonMessage = reader.readLine();
                Message message = JsonSerializer.deserialize(jsonMessage, Message.class);
                // TODO: handle message
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message to the server
     * @param json The json message
     */
    public void send(String json) {
        try {
            writer.write(json);
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
        }
        super.interrupt();
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
