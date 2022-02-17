package it.castelli.tpsit.JavaSocket.networking.message;

/**
 * Represents a generic message with a string as a parameter. Could be sent both by the client and the server.
 * The outcome of receiving it will depend on the service number:
 * <p>
 * 0. Sent both by the client and the sever. Will just print the message on the console.
 * 1. Sent by the server as the result of the calculation.
 * <p>
 * All the others service are currently unsupported, or they don't use this message.
 */
public record GenericMessage(String message) {
}
