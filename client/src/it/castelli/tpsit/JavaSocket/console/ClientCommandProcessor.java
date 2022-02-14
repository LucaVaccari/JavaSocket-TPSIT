package it.castelli.tpsit.JavaSocket.console;

import it.castelli.tpsit.JavaSocket.ClientMain;

public class ClientCommandProcessor extends CommandProcessor {
    @Override
    public void handleCommand(String inputLine) {
        String[] tokens = inputLine.strip().split(" ");
        String command = tokens[0];
        switch (command.toLowerCase()) {
            case "stop":
                ClientMain.stop();
                break;
        }
    }
}
