package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.UserLogManager;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class GenericMessageHandler {
    public static void handle(Message message) {
        try {
            switch (message.getType()) {
                case Message.GENERIC_TYPE -> {
                    Message.GenericMessage genericMessage = message.getContent(Message.GenericMessage.class);
                    System.out.println(genericMessage.message());
                }
                case Message.LOGIN_TYPE -> {
                    Message.LoginMessage loginMessage = message.getContent(Message.LoginMessage.class);
                    UserLogManager.login();
                    UserLogManager.setUsername(loginMessage.username());
                    System.out.println("Logged in successfully with username " + loginMessage.username());
                }
                case Message.ERROR_MESSAGE -> {
                    Message.GenericMessage genericMessage = message.getContent(Message.GenericMessage.class);
                    System.err.println("Error from the server: " + genericMessage.message());
                }
                case Message.STOP_MESSAGE -> {
                    System.out.println("The server is stopping");
                    UserLogManager.logout();
                }
                default -> System.err.println(
                        "(Program log) Error in generic message, unsupported message type: " + message.getType());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
