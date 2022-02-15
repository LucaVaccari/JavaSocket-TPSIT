package it.castelli.tpsit.JavaSocket;

import it.castelli.tpsit.JavaSocket.networking.connection.Connection;
import it.castelli.tpsit.JavaSocket.console.ClientCommandProcessor;
import it.castelli.tpsit.JavaSocket.console.ConsoleListener;

public class ClientMain {
    private static final Connection connection = new Connection();
    private static final ConsoleListener consoleListener = new ConsoleListener();

    public static void main(String[] args) {
        new ClientCommandProcessor().init();
        consoleListener.start();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void stop() {
        connection.interrupt();
        consoleListener.interrupt();
        System.exit(0);
    }
}
