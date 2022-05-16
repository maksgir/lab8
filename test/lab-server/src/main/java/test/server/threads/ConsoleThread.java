package test.server.threads;

import test.server.util.CommandManager;
import test.server.util.ServerCommandListener;
import test.server.util.ServerConfig;

public class ConsoleThread extends Thread {

    private final ServerCommandListener serverCommandListener;
    private final CommandManager commandManager;

    public ConsoleThread(ServerCommandListener serverCommandListener, CommandManager commandManager) {
        this.serverCommandListener = serverCommandListener;
        this.commandManager = commandManager;
    }

    @Override
    public void run() {
        while (ServerConfig.getRunning()) {
            String command = serverCommandListener.readCommand();
            System.out.println(commandManager.executeServerCommand(command));
        }
    }

}