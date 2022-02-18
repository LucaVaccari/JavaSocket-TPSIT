package it.castelli.tpsit.JavaSocket.networking.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

/**
 * Serialized message flowing through the network
 */
public class Message {
	public static final String GENERIC_TYPE = "generic";
	public static final String LOGIN_TYPE = "login";
	public static final String CALCULATE_TYPE = "calculate";

	private final String type;
	private final String username;
	private final int service;
	private final String serializedContent;

	public Message(String type, String username, int service, String serializedSubclass) {
		this.type = type;
		this.username = username;
		this.service = service;
		this.serializedContent = serializedSubclass;
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
}
