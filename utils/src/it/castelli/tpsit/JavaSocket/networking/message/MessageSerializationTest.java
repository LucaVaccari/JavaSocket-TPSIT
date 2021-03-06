package it.castelli.tpsit.JavaSocket.networking.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageSerializationTest {

    @Test
    void serializeMessage() {
        try {
            Message.StringMessage stringMessage = new Message.StringMessage("hello");
            Message message = new Message("generic", "", 0, JsonSerializer.serialize(stringMessage));
            assertEquals("{\"type\":\"generic\",\"username\":\"\",\"service\":0,\"serializedContend\":\"{message:\"hello\"}\"}", JsonSerializer.serialize(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}