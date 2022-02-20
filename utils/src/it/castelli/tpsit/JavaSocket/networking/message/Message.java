package it.castelli.tpsit.JavaSocket.networking.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

/**
 * Serialized message flowing through the network.
 * It contains public records which are used to store data about each message.
 */
public class Message {
    /**
     * Represents a generic message with a string as a parameter. Could be sent both by the client and the server. The
     * outcome of receiving it will depend on the service number:
     * <p>
     * 0. Sent both by the client and the sever. Will just print the message on the console. 1. Sent by the server as the
     * result of the calculation.
     * <p>
     * All the others service are currently unsupported, or they don't use this message.
     */
    @JsonSerialize
    public record GenericMessage(String message) {
    }

    /**
     * If sent by the client it represents a request of registering a new user. If sent by the server it confirms the
     * registration of the user.
     * <p>
     * Registrations are valid only for one session. If the user quits it must register again.
     */
    @JsonSerialize
    public record LoginMessage(String username) {
    }

    /**
     * Sent by the client. Represents the request of making a calculation.
     */
    @JsonSerialize
    public record CalculateMessage(char operation, double a, double b) {
    }

    public static final String GENERIC_TYPE = "generic";
    public static final String LOGIN_TYPE = "login";
    public static final String CALCULATE_TYPE = "calculate";

    private final String type;
    private final String username;
    private final int service;
    private final String serializedContent;

    public Message() {
        type = "";
        username = "";
        service = 0;
        serializedContent = "{}";
    }

    public Message(String type, String username, int service, String serializedContent) {
        this.type = type;
        this.username = username;
        this.service = service;
        this.serializedContent = serializedContent;
    }

    /**
     * Get the actual content of the message
     *
     * @param type The class type of the content
     * @param <T>  The type of the content
     * @return The deserialized message content
     * @throws JsonProcessingException If there's an error during deserialization
     */
    public <T> T getContent(Class<T> type) throws JsonProcessingException {
        return JsonSerializer.deserialize(serializedContent, type);
    }

    /**
     * Getter for the service number: - 0: generic - 1: remote calculator - 2: IRPEF aliquots - 3: guess the number
     * - 4:
     * hangman - 5: area calculator - 6: e-commerce - 7: PARLA news - 8: auction
     *
     * @return The service number
     */
    public int getService() {
        return service;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getSerializedContent() {
        return serializedContent;
    }
}
