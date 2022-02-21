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
			case "help" -> {
				if (tokens.length == 1) {
					if (!ClientMain.getConnection().isConnected()) {
						System.out.println("conn, connect <address> -> connects to the specified address");
						System.out.println("help -> shows a list of available commands");
						System.out.println("help <command> -> shows help for the specified command");
					}
					else if (!UserLogManager.isLogged()) {
						System.out.println("log, login <username> -> logs the user into the server");
					}
					else {
						System.out.println("calc, calculate <a> <operand> <b> -> computes a calculation");
						System.out.println("irpef, aliquots <value> -> computes the IRPEF aliquots of the value");
						System.out.println("guess, gtn <...> -> handles 'guess the value' game");
					}

					System.out.println("stop -> stops the program");
					System.out.println("help -> shows a list of available commands");
					System.out.println("help <command> -> shows help for the specified command");
				}
				else {
					System.err.println("help command with parameter not implemented yet");
				}
			}
			case "stop" -> {
				if (ClientMain.getConnection().isConnected()) {
					Message stopMessage = new Message(Message.STOP_MESSAGE, UserLogManager.getUsername(), 0, "");
					ClientMain.getConnection().send(stopMessage);
				}
				ClientMain.stop();
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
					String jsonSubMessage = JsonSerializer.serialize(new Message.StringMessage(username));
					Message message = new Message(Message.LOGIN_TYPE, username, 0, jsonSubMessage);
					ClientMain.getConnection().send(message);
				}
				catch (JsonProcessingException e) {
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
					String jsonSubMessage = JsonSerializer.serialize(new Message.CalculationMessage(operand, a, b));
					Message message =
							new Message(Message.CALCULATE_TYPE, UserLogManager.getUsername(), 1, jsonSubMessage);
					ClientMain.getConnection().send(message);
				}
				catch (NumberFormatException e) {
					System.err.println("The numbers inserted are not valid");
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			case "irpef", "aliquot" -> {
				if (invalidFullCheck(2, tokens.length, "irpef <value>")) break;

				try {
					double value = Double.parseDouble(tokens[1]);
					String jsonSubMessage = JsonSerializer.serialize(new Message.DoubleMessage(value));
					Message message =
							new Message(Message.ALIQUOT_CALC_TYPE, UserLogManager.getUsername(), 2, jsonSubMessage);
					ClientMain.getConnection().send(message);
				}
				catch (NumberFormatException e) {
					System.err.println("The value inserted is not valid");
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			case "guess", "gtn" -> {
				if (invalidFullCheck(2, tokens.length, "guess <option> or guess <value>")) break;

				if (tokens[1].equalsIgnoreCase("start")) {
					Message message =
							new Message(Message.START_GUESS_THE_NUMBER_TYPE, UserLogManager.getUsername(), 3, "");
					ClientMain.getConnection().send(message);
				}
				else {
					try {
						int value = Integer.parseInt(tokens[1]);
						String subMessageJson = JsonSerializer.serialize(new Message.IntMessage(value));
						Message message =
								new Message(Message.GUESS_THE_NUMBER_NUM_TYPE, UserLogManager.getUsername(), 3,
										subMessageJson);
						ClientMain.getConnection().send(message);
					}
					catch (NumberFormatException e) {
						System.err.println("The value inserted is not valid");
					}
					catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
			default -> System.err.println("Unknown command. Type help for a list of available commands");
		}
	}

	/**
	 * Checks for the connection, the login and the value of parameters
	 *
	 * @param expected      The expected value of parameters
	 * @param actual        The actual value of parameters
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
			System.err.println("You must connect to a server with the conn or connect command before using that " +
					"command");
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
}
