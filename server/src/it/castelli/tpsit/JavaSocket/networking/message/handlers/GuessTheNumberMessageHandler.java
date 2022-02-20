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

                    String subMessageJson = JsonSerializer.serialize(new Message.GenericMessage("Game started. Try to guess!"));
                    Message responseMessage = new Message(Message.GENERIC_TYPE, message.getUsername(), 3, subMessageJson);
                    connection.send(responseMessage);

                    System.out.println("Starting a guess the number game with " + message.getUsername());
                }
                case Message.GUESS_THE_NUMBER_NUM_TYPE -> {
                    if (!userInfoMap.containsKey(message.getUsername())) {
                        String subMessageJson = JsonSerializer.serialize(
                                new Message.GenericMessage("You must start a game (guess start) before guessing a number"));
                        Message responseMessage = new Message(Message.ERROR_MESSAGE, message.getUsername(), 3, subMessageJson);
                        connection.send(responseMessage);
                        break;
                    }
                    Message.GuessTheNumberMessage guessTheNumberMessage = message.getContent(Message.GuessTheNumberMessage.class);
                    userInfoMap.get(message.getUsername()).tries++;
                    int numberToGuess = userInfoMap.get(message.getUsername()).numberToGuess;
                    String response;
                    if (guessTheNumberMessage.number() > numberToGuess)
                        response = "Try a smaller number";
                    else if (guessTheNumberMessage.number() < numberToGuess)
                        response = "Try a bigger number";
                    else {
                        response = "Correct!";
                        userInfoMap.remove(message.getUsername());
                    }

                    String subMessageJson = JsonSerializer.serialize(new Message.GenericMessage(response));
                    Message responseMessage = new Message(Message.GENERIC_TYPE, message.getUsername(), 3, subMessageJson);
                    connection.send(responseMessage);
                }
                default -> System.err.println(
                        "(Program log) Error in guess the number message, unsupported message type: " + message.getType());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
