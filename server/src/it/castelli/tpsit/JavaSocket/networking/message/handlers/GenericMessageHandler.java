package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ServerMain;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class GenericMessageHandler {
    public static void handle(Message message, ClientConnection clientConnection) {
        switch (message.getType()) {
            case Message.LOGIN_TYPE -> {
                try {
                    Message.StringMessage loginMessage = message.getContent(Message.StringMessage.class);
                    String username = loginMessage.value();
                    try {
                        ServerMain.getConnectionManager().logConnection(username, clientConnection);
                        ServerMain.getConnectionManager().send(username, message);
                        System.out.println("New user logged: " + username);
                    } catch (IllegalArgumentException e) {
                        String jsonErrorSubMessage = JsonSerializer.serialize(new Message.StringMessage(
                                "The username " + username + " is already logged. Try a different username"));
                        Message errorMessage = new Message(Message.ERROR_MESSAGE, username, 0, jsonErrorSubMessage);
                        clientConnection.send(errorMessage);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            case Message.STOP_MESSAGE -> ServerMain.getConnectionManager().removeConnection(message.getUsername());
            default -> System.err.println(
                    "(Program log) Error in generic message, unsupported message type: " + message.getType());
        }
    }
}
