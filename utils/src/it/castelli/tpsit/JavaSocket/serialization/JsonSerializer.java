package it.castelli.tpsit.JavaSocket.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts any object to json string and viceversa
 */
public final class JsonSerializer {
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Converts an object of type T into a json string
	 *
	 * @param obj The instance of the object to serialize
	 * @param <T> The type of the object to serialize
	 * @return The converted json string
	 * @throws JsonProcessingException If the conversion throws an error
	 */
	public static <T> String serialize(T obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	/**
	 * Converts a json string into an object of type T
	 *
	 * @param json   The json string
	 * @param tClass The class of the object when deserialized
	 * @param <T>    The type of the object when deserialized
	 * @return The deserialized object
	 * @throws JsonProcessingException If the conversion throws an error
	 */
	public static <T> T deserialize(String json, Class<T> tClass) throws JsonProcessingException {
		return mapper.readValue(json, tClass);
	}
}
