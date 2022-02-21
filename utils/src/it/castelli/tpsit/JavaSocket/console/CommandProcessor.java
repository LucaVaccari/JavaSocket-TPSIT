package it.castelli.tpsit.JavaSocket.console;

/**
 * Its subclasses will process the user commands inserted in the console. It implements the singleton pattern. Before
 * using the methods of this class init() should be called.
 */
public abstract class CommandProcessor {
	/**
	 * The singleton instance
	 */
	private static CommandProcessor instance;

	/**
	 * Singleton getter
	 *
	 * @return The singleton instance
	 */
	public static CommandProcessor getInstance() {
		return instance;
	}

	/**
	 * Singleton setter
	 *
	 * @param instance The new instance
	 */
	protected static void setInstance(CommandProcessor instance) {
		CommandProcessor.instance = instance;
	}

	/**
	 * Initiates the singleton instance. Should be called before using this class.
	 */
	public void init() {
		setInstance(this);
	}

	/**
	 * Handles a command inserted in the console
	 *
	 * @param inputLine The full line of the command inserted
	 */
	public abstract void handleCommand(String inputLine);

	/**
	 * Check if the value of parameters in a command line is correct
	 *
	 * @param expected      The expected value of parameters
	 * @param actual        The actual value of parameters
	 * @param correctSyntax The syntax template to print when expect and actual don't match
	 * @return whether the value of parameters is correct or not
	 */
	protected boolean invalidSyntaxCheck(int expected, int actual, String correctSyntax) {
		if (expected != actual) {
			System.err.println("Wrong syntax. Correct use: " + correctSyntax);
			return true;
		}
		return false;
	}
}
