package it.castelli.tpsit.JavaSocket.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonSerializerTest {
	static class TestClass {
		public int a;
		public int b;

		public TestClass(int a, int b) {
			this.a = a;
			this.b = b;
		}
	}

	@Test
	void serialize() {
		try {
			String json = JsonSerializer.serialize(new TestClass(1, 2));
			assertEquals("{\"a\":1,\"b\":2}", json.strip().toLowerCase());
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Test
	void deserialize() {
		try {
			TestClass testClass = JsonSerializer.deserialize("{\"a\":1,\"b\":2}");
			assertEquals(new TestClass(1, 2), testClass);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}