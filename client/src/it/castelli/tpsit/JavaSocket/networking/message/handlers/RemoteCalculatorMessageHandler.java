package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.GenericMessage;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class RemoteCalculatorMessageHandler {
	public static void handle(Message message) {
		switch (message.getType()) {
			case Message.GENERIC_TYPE -> {
				try {
					System.out.println(
							"The result of the calculation is: " + message.getContent(GenericMessage.class).message());
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
					// TODO: handle error
				}
			}
			default -> System.err.println(
					"(Program log) Error in remote calculator message, unsupported message type: " + message.getType());
		}
	}
}
