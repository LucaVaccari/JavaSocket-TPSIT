package it.castelli.tpsit.JavaSocket.console;

import it.castelli.tpsit.JavaSocket.ServerMain;

public class ServerCommandProcessor extends CommandProcessor {
    @Override
    public void init() {
        setInstance(this);
    }

    @Override
    public void handleCommand(String inputLine) {
        String[] tokens = inputLine.strip().split(" ");
        String command = tokens[0];
        switch (command.toLowerCase()) {
            case "enable":
            case "disable":
                // TODO: implement enable/disable
            case "stop":
                ServerMain.stop();
            case "help":
                // TODO: implement help
            case "kick":
                // TODO: implement kick
            case "broadcast":
                // TODO: implement broadcast
                break;
        }
    }
}
