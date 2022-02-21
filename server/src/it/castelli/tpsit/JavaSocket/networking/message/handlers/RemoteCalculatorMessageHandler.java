package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class RemoteCalculatorMessageHandler {
    public static void handle(Message message, ClientConnection clientConnection) {
        if (Message.CALCULATE_TYPE.equals(message.getType())) {
            try {
                Message.CalculationMessage calculateMessage = message.getContent(Message.CalculationMessage.class);
                double result = switch (calculateMessage.operation()) {
                    case '+' -> calculateMessage.a() + calculateMessage.b();
                    case '-' -> calculateMessage.a() - calculateMessage.b();
                    case '*' -> calculateMessage.a() * calculateMessage.b();
                    case '/' -> calculateMessage.a() / calculateMessage.b();
                    case '^' -> Math.pow(calculateMessage.a(), calculateMessage.b());
                    default -> throw new IllegalStateException("Unexpected value: " + calculateMessage.operation());
                };

                String jsonSubMessage =
                        JsonSerializer.serialize(new Message.StringMessage(String.valueOf(result)));
                Message newMessage =
                        new Message(Message.GENERIC_TYPE, clientConnection.getUsername(), 1, jsonSubMessage);
                clientConnection.send(newMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println(
                    "(Program log) Error in remote calculator message, unsupported message type: " + message.getType());
        }
    }
}
