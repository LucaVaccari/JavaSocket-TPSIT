package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class GuessTheNumberMessageHandler {
    public static void handle(Message message) {
        try {
            switch (message.getType()) {
                case Message.GENERIC_TYPE -> {
                    Message.GenericMessage genericMessage = message.getContent(Message.GenericMessage.class);
                    System.out.println(genericMessage.message());
                }
                case Message.ERROR_MESSAGE -> {
                    Message.GenericMessage genericMessage = message.getContent(Message.GenericMessage.class);
                    System.err.println(genericMessage.message());
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
