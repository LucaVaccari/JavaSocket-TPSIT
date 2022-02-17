package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.CalculateMessage;
import it.castelli.tpsit.JavaSocket.networking.message.GenericMessage;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class RemoteCalculatorMessageHandler {
	public static void handle(CalculateMessage calculateMessage, ClientConnection clientConnection) {
		double result = switch (calculateMessage.operation()) {
			case '+' -> calculateMessage.a() + calculateMessage.b();
			case '-' -> calculateMessage.a() - calculateMessage.b();
			case '*' -> calculateMessage.a() * calculateMessage.b();
			case '/' -> calculateMessage.a() / calculateMessage.b();
			case '^' -> Math.pow(calculateMessage.a(), calculateMessage.b());
			default -> throw new IllegalStateException("Unexpected value: " + calculateMessage.operation());
		};

		try {
			String jsonSubMessage = JsonSerializer.serialize(new GenericMessage("Result: " + result));
			Message message = new Message("generic", 1, jsonSubMessage);
			String jsonMessage = JsonSerializer.serialize(message);
			clientConnection.send(jsonMessage);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
