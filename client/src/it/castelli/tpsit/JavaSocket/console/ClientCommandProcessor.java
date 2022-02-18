package it.castelli.tpsit.JavaSocket.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ClientMain;
import it.castelli.tpsit.JavaSocket.networking.connection.UserLogManager;
import it.castelli.tpsit.JavaSocket.networking.message.CalculateMessage;
import it.castelli.tpsit.JavaSocket.networking.message.LoginMessage;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

/**
 * Handles the commands inserted on the client console
 */
public class ClientCommandProcessor extends CommandProcessor {
	@Override
	public void handleCommand(String inputLine) {
		String[] tokens = inputLine.strip().split(" ");
		String command = tokens[0];
		switch (command.toLowerCase()) {
			case "stop" -> {
				// TODO: send stop message
				ClientMain.stop();
			}
			case "conn", "connect" -> {
				ClientMain.getConnection().setServerAddress(tokens[1]);
				ClientMain.getConnection().start();
			}
			case "log", "login" -> {
				try {
					if (tokens.length != 2) {
						System.err.println("Wrong syntax. Correct use: log <username>");
					}
					else {
						// TODO: validate username
						String jsonSubMessage = JsonSerializer.serialize(new LoginMessage(tokens[1]));
						Message message =
								new Message(Message.LOGIN_TYPE, UserLogManager.getUsername(), 0, jsonSubMessage);
						ClientMain.getConnection().send(JsonSerializer.serialize(message));
					}
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			case "calc", "calculate" -> {
				if (!UserLogManager.isLogged()) {
					System.err.println("You must login with the log or login command before using the calculator");
					break;
				}
				if (tokens.length == 4) {
					char operand;
					float a, b;
					a = Float.parseFloat(tokens[1]);
					operand = tokens[2].charAt(0);
					b = Float.parseFloat(tokens[2]);

					try {
						String jsonSubMessage = JsonSerializer.serialize(new CalculateMessage(operand, a, b));
						Message message =
								new Message(Message.CALCULATE_TYPE, UserLogManager.getUsername(), 1, jsonSubMessage);
						ClientMain.getConnection().send(JsonSerializer.serialize(message));
					}
					catch (com.fasterxml.jackson.core.JsonProcessingException e) {
						e.printStackTrace();
					}
				}
				else {
					System.err.println("Wrong syntax. Correct use: calc <a> <operand> <b>");
				}
			}
		}
	}
}
