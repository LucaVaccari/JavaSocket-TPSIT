package it.castelli.tpsit.JavaSocket.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonSerializerTest {
    @Test
    void serialize() {
        try {
            String json = JsonSerializer.serialize(new TestClass(1, 2));
            assertEquals("{\"a\":1,\"b\":2}", json.strip().toLowerCase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deserialize() {
        try {
            TestClass testClass = JsonSerializer.deserialize("{\"a\":1,\"b\":2}", TestClass.class);
            assertEquals(new TestClass(1, 2), testClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    static class TestClass {
        public int a;
        public int b;

        public TestClass(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public TestClass() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestClass testClass = (TestClass) o;
            return a == testClass.a && b == testClass.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
}