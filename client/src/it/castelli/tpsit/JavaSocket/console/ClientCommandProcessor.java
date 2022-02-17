package it.castelli.tpsit.JavaSocket.console;

import it.castelli.tpsit.JavaSocket.ClientMain;
import it.castelli.tpsit.JavaSocket.networking.message.CalculateMessage;
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
			case "stop" -> ClientMain.stop();
			case "conn", "connect" -> {
				ClientMain.getConnection().setServerAddress(tokens[1]);
				ClientMain.getConnection().start();
			}
			case "calc", "calculate" -> {
				if (tokens.length == 4) {
					char operand;
					float a, b;
					a = Float.parseFloat(tokens[1]);
					operand = tokens[2].charAt(0);
					b = Float.parseFloat(tokens[2]);

					try {
						String jsonSubMessage = JsonSerializer.serialize(new CalculateMessage(operand, a, b));
						Message message = new Message("calculate", 1, jsonSubMessage);
						String jsonMessage = JsonSerializer.serialize(message);
						ClientMain.getConnection().send(jsonMessage);
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
