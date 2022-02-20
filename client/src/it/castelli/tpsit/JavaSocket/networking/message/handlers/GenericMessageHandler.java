package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.UserLogManager;
import it.castelli.tpsit.JavaSocket.networking.message.Message;

public class GenericMessageHandler {
	public static void handle(Message message) {
		switch (message.getType()) {
			case Message.LOGIN_TYPE -> {
				try {
					Message.LoginMessage loginMessage = message.getContent(Message.LoginMessage.class);
					UserLogManager.login();
					UserLogManager.setUsername(loginMessage.username());
					System.out.println("Logged in successfully with username " + loginMessage.username());
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
					// TODO: handle error
				}
			}
			case Message.ERROR_MESSAGE -> {
				try {
					Message.GenericMessage genericMessage = message.getContent(Message.GenericMessage.class);
					System.err.println("Error from the server: " + genericMessage.message());
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			default -> System.err.println(
					"(Program log) Error in generic message, unsupported message type: " + message.getType());
		}
	}
}
