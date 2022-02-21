package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class IRPEFAliquotsMessageHandler {
    public static void handle(Message message) {
        try {
            if (Message.ALIQUOT_CALC_TYPE.equals(message.getType())) {
                Message.DoubleMessage aliquotMessage = message.getContent(Message.DoubleMessage.class);
                System.out.println("The IRPEF aliquot value is: " + aliquotMessage.value());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
