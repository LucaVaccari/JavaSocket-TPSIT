package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ServerMain;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.LoginMessage;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class GenericMessageHandler {
    public static void handle(Message message, ClientConnection clientConnection) {
        switch (message.getType()) {
            case Message.LOGIN_TYPE -> {
                try {
                    LoginMessage loginMessage = message.getContent(LoginMessage.class);
                    ServerMain.getConnectionManager().logConnection(loginMessage.username(), clientConnection);
                    ServerMain.getConnectionManager().send(loginMessage.username(), JsonSerializer.serialize(message));
                    System.out.println("New user logged: " + loginMessage.username());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            default -> System.err.println("(Program log) Error in generic message, unsupported message type: " + message.getType());
        }
    }
}
