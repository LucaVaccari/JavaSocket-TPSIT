package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.UserLogManager;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class GenericMessageHandler {
    public static void handle(Message message) {
        try {
            switch (message.getType()) {
                case Message.GENERIC_TYPE -> {
                    Message.StringMessage stringMessage = message.getContent(Message.StringMessage.class);
                    System.out.println(stringMessage.value());
                }
                case Message.LOGIN_TYPE -> {
                    Message.StringMessage loginMessage = message.getContent(Message.StringMessage.class);
                    UserLogManager.login();
                    UserLogManager.setUsername(loginMessage.value());
                    System.out.println("Logged in successfully with username " + loginMessage.value());
                }
                case Message.ERROR_MESSAGE -> {
                    Message.StringMessage stringMessage = message.getContent(Message.StringMessage.class);
                    System.err.println("Error from the server: " + stringMessage.value());
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
