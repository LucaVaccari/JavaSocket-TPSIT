package it.castelli.tpsit.JavaSocket.connection;

import it.castelli.tpsit.JavaSocket.GlobalData;

import java.io.*;
import java.net.Socket;

/**
 * Handles the connection with the server (input and output)
 */
public class Connection extends Thread {
	// TODO: put address in file
	public static final String SERVER_ADDRESS = "localhost";
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	public Connection() {
		try {
			socket = new Socket(SERVER_ADDRESS, GlobalData.PORT);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			start();
		}
		catch (IOException e) {
			e.printStackTrace();
			// TODO: handle error
		}
	}

	@Override
	public void run() {
		// TODO: find a way to not use while(true)
		while (true) {
			try {
				String message = reader.readLine();
				// TODO: handle received messages
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(String json) {
		try {
			writer.write(json);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
