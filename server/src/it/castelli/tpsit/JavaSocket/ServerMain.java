package it.castelli.tpsit.JavaSocket;

import it.castelli.tpsit.JavaSocket.networking.connection.ConnectionManager;
import it.castelli.tpsit.JavaSocket.console.ConsoleListener;
import it.castelli.tpsit.JavaSocket.console.ServerCommandProcessor;

public class ServerMain {
    private static final ConnectionManager connectionManager = new ConnectionManager();
    private static final ConsoleListener consoleListener = new ConsoleListener();

    public static void main(String[] args) {
        connectionManager.start();
        new ServerCommandProcessor().init();
        consoleListener.start();
    }

    public static void stop() {
        connectionManager.interrupt();
        consoleListener.interrupt();
        System.exit(0);
    }

    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
