package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class RemoteCalculatorMessageHandler {
    public static void handle(Message message) {
        try {
            if (Message.GENERIC_TYPE.equals(message.getType())) {
                System.out.println("The result of the calculation is: " +
                        message.getContent(Message.StringMessage.class).value());
            } else {
                System.err.println(
                        "(Program log) Error in remote calculator message, unsupported message type: " + message.getType());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
