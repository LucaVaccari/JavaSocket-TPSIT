package it.castelli.tpsit.JavaSocket.networking.message;

/**
 * Sent by the client. Represents the request of making a calculation.
 */
public record CalculateMessage(char operation, double a, double b) {
}
