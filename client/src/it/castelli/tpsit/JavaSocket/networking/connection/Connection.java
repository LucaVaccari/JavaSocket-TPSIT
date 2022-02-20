package it.castelli.tpsit.JavaSocket.networking.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.GlobalData;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.networking.message.handlers.*;
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
    private static boolean connected = false;

    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, GlobalData.PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected with " + serverAddress + " on port " + GlobalData.PORT);
            connected = true;
        } catch (IOException e) {
            System.err.println("Error while connecting to the server: invalid address or server unreachable");
        }
        // TODO: find a way to not use while(true)
        while (true) {
            try {
                String jsonMessage = reader.readLine();
                Message message = JsonSerializer.deserialize(jsonMessage, Message.class);
                if (message != null) {
                    switch (message.getService()) {
                        case 0 -> GenericMessageHandler.handle(message);
                        case 1 -> RemoteCalculatorMessageHandler.handle(message);
                        case 2 -> IRPEFAliquotsMessageHandler.handle(message);
                        case 3 -> GuessTheNumberMessageHandler.handle(message);
                        case 4 -> HangmanMessageHandler.handle(message);
                        case 5 -> AreaCalculatorMessageHandler.handle(message);
                        case 6 -> ECommerceMessageHandler.handle(message);
                        case 7 -> PARLANewsMessageHandler.handle(message);
                        case 8 -> AuctionMessageHandler.handle(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from the server");
                connected = false;
                interrupt();
                return;
            }
        }
    }

    /**
     * Sends a message to the server
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
     * Converts a message in json and sends it to the server
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
            connected = false;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.interrupt();
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public boolean isConnected() {
        return connected;
    }
}
