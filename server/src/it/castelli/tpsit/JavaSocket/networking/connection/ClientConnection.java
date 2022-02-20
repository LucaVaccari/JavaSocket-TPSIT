package it.castelli.tpsit.JavaSocket.networking.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ServerMain;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.networking.message.handlers.*;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handles a connection with a single client (input and output)
 */
public class ClientConnection extends Thread {
    private final Socket socket;
    private String username;
    private BufferedWriter writer;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        try {
            System.out.println(
                    "New active connection at " + socket.getInetAddress().getHostName() + " on " + socket.getPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                try {
                    String input = reader.readLine();
                    Message message = JsonSerializer.deserialize(input, Message.class);
                    switch (message.getService()) {
                        case 0 -> GenericMessageHandler.handle(message, this);
                        case 1 -> RemoteCalculatorMessageHandler.handle(message, this);
                        case 2 -> IRPEFAliquotsMessageHandler.handle(message, this);
                        case 3 -> GuessTheNumberMessageHandler.handle(message, this);
                        case 4 -> HangmanMessageHandler.handle(message, this);
                        case 5 -> AreaCalculatorMessageHandler.handle(message, this);
                        case 6 -> ECommerceMessageHandler.handle(message, this);
                        case 7 -> PARLANewsMessageHandler.handle(message, this);
                        case 8 -> AuctionMessageHandler.handle(message, this);
                    }
                } catch (SocketException exception) {
                    System.out.println(username + " disconnected");
                    interrupt();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the client
     *
     * @param message The json message
     */
    public void send(String message) {
        try {
            writer.write(message + (message.endsWith("\n") ? "" : "\n"));
            writer.flush();
        } catch (IOException e) {
            interrupt();
        }
    }

    /**
     * Converts a message into json, then sends it to the client
     *
     * @param message The message to send
     */
    public void send(Message message) {
        try {
            send(JsonSerializer.serialize(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        try {
            ServerMain.getConnectionManager().removeConnection(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.interrupt();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
