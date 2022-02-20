package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class IRPEFAliquotsMessageHandler {
	public static void handle(Message message, ClientConnection connection) {
		if (Message.ALIQUOT_CALC_TYPE.equals(message.getType())) {
			try {
				Message.AliquotMessage aliquotMessage = message.getContent(Message.AliquotMessage.class);
				double result = 0;
				double value = aliquotMessage.value();

				// TODO: fix algorithm
				while (value > 0) {
					double aliquot;
					if (value > 50000) {
						aliquot = value * 43 / 100;
					}
					else if (value > 28000) {
						aliquot = value * 35 / 100;
					}
					else if (value > 15000) {
						aliquot = value * 25 / 100;
					}
					else {
						aliquot = value * 23 / 100;
						result += aliquot;
						break;
					}
					result += aliquot;
					value -= aliquot;
				}

				Message.AliquotMessage resultMessage = new Message.AliquotMessage(result);
				Message newMessage = new Message(Message.ALIQUOT_CALC_TYPE, connection.getUsername(), 2,
						JsonSerializer.serialize(resultMessage));
				connection.send(JsonSerializer.serialize(newMessage));
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println(
					"(Program log) Error in IRPEF aliquot message, unsupported message type: " + message.getType());
		}
	}
}
