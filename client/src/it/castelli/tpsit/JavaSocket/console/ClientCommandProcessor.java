package it.castelli.tpsit.JavaSocket.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ClientMain;
import it.castelli.tpsit.JavaSocket.networking.connection.UserLogManager;
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
				try {
					Message stopMessage = new Message(Message.STOP_MESSAGE, UserLogManager.getUsername(), 0, "");
					ClientMain.getConnection().send(JsonSerializer.serialize(stopMessage));
					ClientMain.stop();
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			case "conn", "connect" -> {
				if (tokens.length != 2) {
					System.err.println("Wrong syntax. Correct use: conn <address>");
				}
				else {
					ClientMain.getConnection().setServerAddress(tokens[1]);
					ClientMain.getConnection().start();
				}
			}
			case "log", "login" -> {
				try {
					if (UserLogManager.isLogged()) {
						System.err.println("You already logged in.");
						break;
					}
					if (tokens.length != 2) {
						System.err.println("Wrong syntax. Correct use: " +
								"log <username> (the username cannot contain spaces)");
					}
					else {
						// TODO: validate username
						String jsonSubMessage = JsonSerializer.serialize(new Message.LoginMessage(tokens[1]));
						Message message =
								new Message(Message.LOGIN_TYPE, tokens[1], 0, jsonSubMessage);
						String json = JsonSerializer.serialize(message);
						ClientMain.getConnection().send(json);
					}
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			case "calc", "calculate" -> {
				if (!ClientMain.getConnection().isConnected()) {
					System.err.println(
							"You must connect to a server with the conn or connect command before using the" +
									" calculator");
					break;
				}
				if (!UserLogManager.isLogged()) {
					System.err.println("You must login with the log or login command before using the calculator");
					break;
				}
				if (tokens.length != 4) {
					System.err.println("Wrong syntax. Correct use: calc <a> <operand> <b>");
				}
				else {
					char operand;
					float a, b;
					try {
						a = Float.parseFloat(tokens[1]);
						operand = tokens[2].charAt(0);
						b = Float.parseFloat(tokens[3]);
						String jsonSubMessage = JsonSerializer.serialize(new Message.CalculateMessage(operand, a, b));
						Message message =
								new Message(Message.CALCULATE_TYPE, UserLogManager.getUsername(), 1, jsonSubMessage);
						ClientMain.getConnection().send(JsonSerializer.serialize(message));
					}
					catch (NumberFormatException e) {
						System.err.println("The numbers inserted are not valid");
					}
					catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
			case "irpef", "aliquot" -> {
				if (!ClientMain.getConnection().isConnected()) {
					System.err.println(
							"You must connect to a server with the conn or connect command before using the" +
									" IRPEF aliquot calculator");
					break;
				}
				if (!UserLogManager.isLogged()) {
					System.err.println("You must login with the log or login command before using the IRPEF aliquot " +
							"calculator");
					break;
				}
				if (tokens.length != 2) {
					System.err.println("Wrong syntax. Correct use: irpef <value>");
				}
				else {
					try {
						double value = Double.parseDouble(tokens[1]);
						String jsonSubMessage = JsonSerializer.serialize(new Message.AliquotMessage(value));
						Message message =
								new Message(Message.ALIQUOT_CALC_TYPE, UserLogManager.getUsername(), 2,
										jsonSubMessage);
						ClientMain.getConnection().send(JsonSerializer.serialize(message));
					}
					catch (NumberFormatException e) {
						System.err.println("The number inserted is not valid");
					}
					catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
			default -> System.err.println("Unknown command. Type help for a list of available commands");
		}
	}
}
