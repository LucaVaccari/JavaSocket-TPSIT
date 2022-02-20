package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class RemoteCalculatorMessageHandler {
	public static void handle(Message message) {
		if (Message.GENERIC_TYPE.equals(message.getType())) {
			try {
				System.out.println("The result of the calculation is: " +
						message.getContent(Message.GenericMessage.class).message());
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println(
					"(Program log) Error in remote calculator message, unsupported message type: " + message.getType());
		}
	}
}
