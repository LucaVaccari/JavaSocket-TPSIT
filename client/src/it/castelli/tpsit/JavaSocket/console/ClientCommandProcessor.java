package it.castelli.tpsit.JavaSocket.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.castelli.tpsit.JavaSocket.ClientMain;
import it.castelli.tpsit.JavaSocket.networking.connection.UserLogManager;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

/**
 * Handles the commands inserted on the client console
 */
public class ClientCommandProcessor extends CommandProcessor {
    @Override
    public void handleCommand(String inputLine) {
        String[] tokens = inputLine.strip().split(" ");
        String command = tokens[0];

        switch (command.toLowerCase()) {
            case "stop" -> {
                try {
                    if (ClientMain.getConnection().isConnected()) {
                        Message stopMessage = new Message(Message.STOP_MESSAGE, UserLogManager.getUsername(), 0, "");
                        ClientMain.getConnection().send(JsonSerializer.serialize(stopMessage));
                    }
                    ClientMain.stop();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            case "conn", "connect" -> {
                if (invalidSyntaxCheck(2, tokens.length, "conn <address>")) break;
                ClientMain.getConnection().setServerAddress(tokens[1]);
                ClientMain.getConnection().start();
            }
            case "log", "login" -> {
                try {
                    if (invalidConnectionCheck()) break;
                    if (invalidSyntaxCheck(2, tokens.length,
                            "log <username> (the username cannot contain spaces)"))
                        break;

                    String username = tokens[1];
                    String jsonSubMessage = JsonSerializer.serialize(new Message.LoginMessage(username));
                    Message message = new Message(Message.LOGIN_TYPE, username, 0, jsonSubMessage);
                    String json = JsonSerializer.serialize(message);
                    ClientMain.getConnection().send(json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            case "calc", "calculate" -> {
                if (invalidFullCheck(4, tokens.length, "calc <a> <operand> <b>")) break;

                char operand;
                float a, b;
                try {
                    a = Float.parseFloat(tokens[1]);
                    operand = tokens[2].charAt(0);
                    b = Float.parseFloat(tokens[3]);
                    String jsonSubMessage = JsonSerializer.serialize(new Message.CalculateMessage(operand, a, b));
                    Message message =
                            new Message(Message.CALCULATE_TYPE, UserLogManager.getUsername(), 1, jsonSubMessage);
                    ClientMain.getConnection().send(JsonSerializer.serialize(message));
                } catch (NumberFormatException e) {
                    System.err.println("The numbers inserted are not valid");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            case "irpef", "aliquot" -> {
                if (invalidFullCheck(2, tokens.length, "irpef <value>")) break;

                try {
                    double value = Double.parseDouble(tokens[1]);
                    String jsonSubMessage = JsonSerializer.serialize(new Message.AliquotMessage(value));
                    Message message =
                            new Message(Message.ALIQUOT_CALC_TYPE, UserLogManager.getUsername(), 2,
                                    jsonSubMessage);
                    ClientMain.getConnection().send(JsonSerializer.serialize(message));
                } catch (NumberFormatException e) {
                    System.err.println("The number inserted is not valid");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            default -> System.err.println("Unknown command. Type help for a list of available commands");
        }
    }

    /**
     * Checks for the connection, the login and the number of parameters
     *
     * @param expected      The expected number of parameters
     * @param actual        The actual number of parameters
     * @param correctSyntax The syntax template to print when expect and actual don't match
     * @return whether all the checks are passed
     */
    private boolean invalidFullCheck(int expected, int actual, String correctSyntax) {
        return invalidConnectionCheck() || !loginCheck() || invalidSyntaxCheck(expected, actual, correctSyntax);
    }

    /**
     * Checks if the client is connected to the server and prints an error if not
     *
     * @return whether it is connected or not
     */
    private boolean invalidConnectionCheck() {
        if (!ClientMain.getConnection().isConnected()) {
            System.err.println("You must connect to a server with the conn or connect command before using that command");
            return true;
        }
        return false;
    }

    /**
     * Checks if the client is logged in to the server and prints an error if not
     *
     * @return whether it is logged or not
     */
    private boolean loginCheck() {
        if (!UserLogManager.isLogged()) {
            System.err.println("You must login with the log or login command before using that command");
            return false;
        }
        return true;
    }

    /**
     * Check if the number of parameters in a command line is correct
     *
     * @param expected      The expected number of parameters
     * @param actual        The actual number of parameters
     * @param correctSyntax The syntax template to print when expect and actual don't match
     * @return whether the number of parameters is correct or not
     */
    private boolean invalidSyntaxCheck(int expected, int actual, String correctSyntax) {
        if (expected != actual) {
            System.err.println("Wrong syntax. Correct use: " + correctSyntax);
            return true;
        }
        return false;
    }
}
