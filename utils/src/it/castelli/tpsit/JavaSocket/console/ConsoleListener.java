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
		System.out.println("Welcome.\nSoftware by Luca Vaccari.");
		// TODO: find a way to not use while(true)
		while (true) {
			String input = scanner.nextLine();
			CommandProcessor.getInstance().handleCommand(input);
		}
	}

	@Override
	public void interrupt() {
		System.out.println("Interrupting...");
		super.interrupt();
	}
}
