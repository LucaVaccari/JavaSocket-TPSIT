package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class GuessTheNumberMessageHandler {
	public static void handle(Message message) {
		try {
			switch (message.getType()) {
				case Message.GENERIC_TYPE -> {
					Message.StringMessage stringMessage = message.getContent(Message.StringMessage.class);
					System.out.println(stringMessage.value());
				}
				case Message.ERROR_MESSAGE -> {
					Message.StringMessage stringMessage = message.getContent(Message.StringMessage.class);
					System.err.println(stringMessage.value());
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
