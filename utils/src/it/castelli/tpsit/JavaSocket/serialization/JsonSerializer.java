package it.castelli.tpsit.JavaSocket.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Converts any object to json string and viceversa
 */
public final class JsonSerializer {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static <T> String serialize(T obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	// TODO: make it work
	public static <T> T deserialize(String json) throws JsonProcessingException {
		return mapper.readValue(json, new TypeReference<T>() {});
	}
}
