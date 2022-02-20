package it.castelli.tpsit.JavaSocket.console;

import java.util.Scanner;

/**
 * Reads input from the console after being started.
 */
public class ConsoleListener extends Thread {
	private final Scanner scanner;

	public ConsoleListener() {
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void run() {
		System.out.println("Welcome.\nSoftware by Luca Vaccari.\nPress enter to start");
		// TODO: find a way to not use while(true)
		while (true) {
			// TODO: try to not skip the first line
			String input = scanner.nextLine();
			CommandProcessor.getInstance().handleCommand(input);
		}
	}

	@Override
	public void interrupt() {
		System.out.print("Interrupting...");
		super.interrupt();
	}
}
