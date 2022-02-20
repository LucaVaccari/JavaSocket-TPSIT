package it.castelli.tpsit.JavaSocket.networking.connection;

import it.castelli.tpsit.JavaSocket.GlobalData;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.networking.message.handlers.GenericMessageHandler;
import it.castelli.tpsit.JavaSocket.networking.message.handlers.RemoteCalculatorMessageHandler;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

import java.io.*;
import java.net.Socket;

/**
 * Handles the connection with the server (input and output)
 */
public class Connection extends Thread {
	private String serverAddress = "localhost";
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private boolean connected = false;

	@Override
	public void run() {
		try {
			socket = new Socket(serverAddress, GlobalData.PORT);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("Connected with " + serverAddress + " on port " + GlobalData.PORT);
			connected = true;
		}
		catch (IOException e) {
			System.err.println("Error while connecting to the server: invalid address or server unreachable");
		}
		// TODO: find a way to not use while(true)
		while (true) {
			try {
				String jsonMessage = reader.readLine();
				Message message = JsonSerializer.deserialize(jsonMessage, Message.class);
				switch (message.getService()) {
					case 0 -> GenericMessageHandler.handle(message);
					case 1 -> RemoteCalculatorMessageHandler.handle(message);
					case 2 -> {
					}
					case 3 -> {
					}
					case 4 -> {
					}
					case 5 -> {
					}
					case 6 -> {
					}
					case 7 -> {
					}
					case 8 -> {
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sends a message to the server
	 *
	 * @param message The json message
	 */
	public void send(String message) {
		try {
			writer.write(message + (message.endsWith("\n") ? "" : "\n"));
			writer.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			// TODO: handle error
		}
	}

	@Override
	public void interrupt() {
		try {
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public boolean isConnected() {
		return connected;
	}
}
