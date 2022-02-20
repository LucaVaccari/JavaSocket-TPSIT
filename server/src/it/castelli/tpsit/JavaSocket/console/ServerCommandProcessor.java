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
			case "enable":
			case "disable":
				// TODO: implement enable/disable
			case "stop":
				try {
					String subMessageJson = JsonSerializer.serialize(new Message.GenericMessage("Server stopped"));
					Message stopMessage = new Message(Message.STOP_MESSAGE, "", 0, subMessageJson);
					ServerMain.getConnectionManager().broadcast(JsonSerializer.serialize(stopMessage));
					ServerMain.stop();
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			case "help":
				// TODO: implement help
			case "kick":
				// TODO: implement kick
			case "broadcast":
				// TODO: implement broadcast
				break;
		}
	}
}
