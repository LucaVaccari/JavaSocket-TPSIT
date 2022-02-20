package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class IRPEFAliquotsMessageHandler {
	public static void handle(Message message) {
		if (Message.ALIQUOT_CALC_TYPE.equals(message.getType())) {
			try {
				Message.AliquotMessage aliquotMessage = message.getContent(Message.AliquotMessage.class);
				System.out.println("The IRPEF aliquot value is: " + aliquotMessage.value());
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
}
