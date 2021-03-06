package it.castelli.tpsit.JavaSocket.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ServerMain;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class ServerCommandProcessor extends CommandProcessor {
	@Override
	public void init() {
		setInstance(this);
	}

	@Override
	public void handleCommand(String inputLine) {
		String[] tokens = inputLine.strip().split(" ");
		String command = tokens[0];
		switch (command.toLowerCase()) {
			case "enable" -> {
				// TODO: implement enable
			}
			case "disable" -> {
				// TODO: implement disable
			}
			case "stop" -> {
				try {
					String subMessageJson = JsonSerializer.serialize(new Message.StringMessage("Server stopped"));
					Message stopMessage = new Message(Message.STOP_MESSAGE, "", 0, subMessageJson);
					ServerMain.getConnectionManager().broadcast(stopMessage);
					ServerMain.stop();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			case "help" -> {
				if (tokens.length == 1) {
					System.out.println("broadcast <message> -> sends a message to all connected clients");

					System.out.println("stop -> stops the program");
					System.out.println("help -> shows a list of available commands");
					System.out.println("help <command> -> shows help for the specified command");
				} else {
					System.err.println("help command with parameter not implemented yet");
				}
			}
			case "kick" -> {
				// TODO: IT DOES NOT WORK
				if (!invalidSyntaxCheck(2, tokens.length, "kick <username> or kick unlogged")) {
					if (tokens[1].equalsIgnoreCase("unlogged")) {
						ServerMain.getConnectionManager().removeUnloggedConnections(true);
						System.out.println("Disconnecting all the users which are not logged");
					} else {
						ServerMain.getConnectionManager().removeConnection(tokens[1], true);
						System.out.println("Disconnecting " + tokens[1]);
					}
				}
			}
			case "broadcast" -> {
				try {
					tokens[0] = "";
					String stringMessage = "Server message: " + String.join(" ", tokens).strip();
					String subMessageJson =
							JsonSerializer.serialize(new Message.StringMessage(stringMessage));
					Message broadcastMessage = new Message(Message.GENERIC_TYPE, "[server]", 0,
							subMessageJson);
					ServerMain.getConnectionManager().broadcast(broadcastMessage);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			default -> System.err.println("Unknown command. Type help for a list of available commands");
		}
	}
}
