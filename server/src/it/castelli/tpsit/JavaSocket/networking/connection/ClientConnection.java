package it.castelli.tpsit.JavaSocket.networking.connection;

import it.castelli.tpsit.JavaSocket.ServerMain;
import it.castelli.tpsit.JavaSocket.networking.message.Message;
import it.castelli.tpsit.JavaSocket.networking.message.handlers.GenericMessageHandler;
import it.castelli.tpsit.JavaSocket.networking.message.handlers.RemoteCalculatorMessageHandler;
import it.castelli.tpsit.JavaSocket.serialization.JsonSerializer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handles a connection with a single client (input and output)
 */
public class ClientConnection extends Thread {
	private final Socket socket;
	private String username;
	private BufferedWriter writer;

	public ClientConnection(Socket socket) {
		this.socket = socket;
		start();
	}

	@Override
	public void run() {
		try {
			System.out.println(
					"New active connection at " + socket.getInetAddress().getHostName() + " on " + socket.getPort());
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			while (true) {
				// TODO: handle socket exception
				try {
					String input = reader.readLine();
					Message message = JsonSerializer.deserialize(input, Message.class);
					switch (message.getService()) {
						case 0 -> GenericMessageHandler.handle(message, this);
						case 1 -> RemoteCalculatorMessageHandler.handle(message, this);
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
				catch (SocketException exception) {
					System.out.println(username + " disconnected");
					interrupt();
					return;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the client
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
			// TODO: handle send error
		}
	}

	@Override
	public void interrupt() {
		try {
			ServerMain.getConnectionManager().removeConnection(this);
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			// TODO: handle error
		}
		super.interrupt();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
