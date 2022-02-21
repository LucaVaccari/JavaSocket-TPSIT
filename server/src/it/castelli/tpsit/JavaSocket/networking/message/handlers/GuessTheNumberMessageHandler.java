package it.castelli.tpsit.JavaSocket.networking.message.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.networking.connection.ClientConnection;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GuessTheNumberMessageHandler {
	private static class UserInfo {
		public int tries, numberToGuess;

		public UserInfo(int tries, int numberToGuess) {
			this.tries = tries;
			this.numberToGuess = numberToGuess;
		}
	}

	private final static ConcurrentHashMap<String, UserInfo> userInfoMap = new ConcurrentHashMap<>();

	public static void handle(Message message, ClientConnection connection) {
		try {
			switch (message.getType()) {
				case Message.START_GUESS_THE_NUMBER_TYPE -> {
					// TODO : add parameters to the start of the game
					userInfoMap.put(message.getUsername(), new UserInfo(0, new Random().nextInt(100) + 1));

					String subMessageJson =
							JsonSerializer.serialize(new Message.StringMessage("Game started. Try to guess!"));
					Message responseMessage =
							new Message(Message.GENERIC_TYPE, message.getUsername(), 3, subMessageJson);
					connection.send(responseMessage);

					System.out.println("Starting a guess the value game with " + message.getUsername());
				}
				case Message.GUESS_THE_NUMBER_NUM_TYPE -> {
					if (!userInfoMap.containsKey(message.getUsername())) {
						String subMessageJson = JsonSerializer.serialize(
								new Message.StringMessage("You must start a game (guess start) before guessing a " +
										"value"));
						Message responseMessage =
								new Message(Message.ERROR_MESSAGE, message.getUsername(), 3, subMessageJson);
						connection.send(responseMessage);
						break;
					}
					Message.IntMessage guessTheNumberMessage =
							message.getContent(Message.IntMessage.class);
					userInfoMap.get(message.getUsername()).tries++;
					int numberToGuess = userInfoMap.get(message.getUsername()).numberToGuess;
					String response;
					if (guessTheNumberMessage.value() > numberToGuess)
						response = "Try a smaller value";
					else if (guessTheNumberMessage.value() < numberToGuess)
						response = "Try a bigger value";
					else {
						response = "Correct! Guessed in " + userInfoMap.get(message.getUsername()).tries + " attempts";
						userInfoMap.remove(message.getUsername());
					}

					String subMessageJson = JsonSerializer.serialize(new Message.StringMessage(response));
					Message responseMessage =
							new Message(Message.GENERIC_TYPE, message.getUsername(), 3, subMessageJson);
					connection.send(responseMessage);
				}
				default -> System.err.println(
						"(Program log) Error in guess the value message, unsupported message type: " +
								message.getType());
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
