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
}
