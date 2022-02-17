package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.CalculateMessage;
import it.castelli.tpsit.JavaSocket.networking.message.GenericMessage;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

public class RemoteCalculatorMessageHandler {
    public static void handle(Message message, ClientConnection clientConnection) {
        switch (message.getType()) {
            case Message.CALCULATE_TYPE -> {
                try {
                    CalculateMessage calculateMessage = message.getContent(CalculateMessage.class);
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
                        Message newMessage = new Message(Message.GENERIC_TYPE, clientConnection.getUsername(), 1, jsonSubMessage);
                        clientConnection.send(JsonSerializer.serialize(newMessage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    // TODO: handle error
                }
            }
            default -> System.err.println("(Program log) Error in remote calculator message, unsupported message type: " + message.getType());
        }
    }
}
