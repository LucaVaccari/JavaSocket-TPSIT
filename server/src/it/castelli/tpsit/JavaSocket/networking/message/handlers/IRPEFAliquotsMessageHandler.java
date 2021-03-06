package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class IRPEFAliquotsMessageHandler {
	public static void handle(Message message, ClientConnection connection) {
		if (Message.ALIQUOT_CALC_TYPE.equals(message.getType())) {
			try {
				Message.DoubleMessage aliquotMessage = message.getContent(Message.DoubleMessage.class);
				double result = 0;
				double value = aliquotMessage.value();

				double aliquot;

				// TODO: THIS CALCULATION IS WRONG
				if (value > 50000) {
					aliquot = value * 43 / 100;
					value -= 50000;
					result += aliquot;
				}
				if (value > 28000) {
					aliquot = value * 35 / 100;
					value -= 28000;
					result += aliquot;
				}
				if (value > 15000) {
					aliquot = value * 25 / 100;
					value -= 15000;
					result += aliquot;
				}
				if (value < 15000) {
					aliquot = value * 23 / 100;
					result += aliquot;
				}

				Message.DoubleMessage resultMessage = new Message.DoubleMessage(result);
				Message newMessage = new Message(Message.ALIQUOT_CALC_TYPE, connection.getUsername(), 2,
						JsonSerializer.serialize(resultMessage));
				connection.send(newMessage);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println(
					"(Program log) Error in IRPEF aliquot message, unsupported message type: " + message.getType());
		}
	}
}
