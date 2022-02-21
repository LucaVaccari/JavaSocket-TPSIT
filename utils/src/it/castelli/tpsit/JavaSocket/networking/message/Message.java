package it.castelli.tpsit.JavaSocket.networking.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

/**
 * Serialized message flowing through the network. It contains public records which are used to store data about each
 * message.
 */
public class Message {
	public record StringMessage(String value) {
	}

	public record DoubleMessage(double value) {
	}

	public record IntMessage(int value) {
	}

	public record CalculationMessage(char operation, double a, double b) {
	}

	// GENERIC MESSAGES
	public static final String GENERIC_TYPE = "generic";
	public static final String ERROR_MESSAGE = "error";
	public static final String LOGIN_TYPE = "login";
	public static final String STOP_MESSAGE = "stop";
	// CALCULATOR MESSAGES
	public static final String CALCULATE_TYPE = "calculate";
	// IRPEF ALIQUOTS MESSAGES
	public static final String ALIQUOT_CALC_TYPE = "aliquot";
	// GUESS THE NUMBER MESSAGES
	public static final String START_GUESS_THE_NUMBER_TYPE = "start_guess_the_number";
	public static final String GUESS_THE_NUMBER_NUM_TYPE = "num_guess_the_number";

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
	 * Getter for the service value: - 0: generic - 1: remote calculator - 2: IRPEF aliquots - 3: guess the value - 4:
	 * hangman - 5: area calculator - 6: e-commerce - 7: PARLA news - 8: auction
	 *
	 * @return The service value
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
