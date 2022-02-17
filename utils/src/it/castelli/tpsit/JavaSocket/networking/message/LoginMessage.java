package it.castelli.tpsit.JavaSocket.networking.message;

/**
 * If sent by the client it represents a request of registering a new user.
 * If sent by the server it confirms the registration of the user.
 * <p>
 * Registrations are valid only for one session. If the user quits it must register again.
 */
public record LoginMessage(String username) {
}
