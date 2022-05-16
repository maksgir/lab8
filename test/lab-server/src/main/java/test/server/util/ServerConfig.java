package test.server.util;


import test.common.abstractions.AbstractCommand;
import test.common.abstractions.AbstractServerCommand;

import java.util.HashMap;

public final class ServerConfig {

    private static final HashMap<String, AbstractCommand> CLIENT_AVAILABLE_COMMANDS = new HashMap<>();
    private static final HashMap<String, AbstractServerCommand> SERVER_AVAILABLE_COMMANDS = new HashMap<>();
    private static boolean isRunning = true;

    private ServerConfig() {
    }

    public static void pushClientCommand(String name, AbstractCommand command){
        CLIENT_AVAILABLE_COMMANDS.put(name, command);
    }

    public static void pushServerCommand(String name, AbstractServerCommand command){
        SERVER_AVAILABLE_COMMANDS.put(name, command);
    }



    public static boolean getRunning() {
        return isRunning;
    }

    public static void toggleRun() {
        isRunning = !isRunning;
    }


    public static HashMap<String, AbstractCommand> getClientAvailableCommands() {
        return CLIENT_AVAILABLE_COMMANDS;
    }
    public static HashMap<String, AbstractServerCommand> getServerAvailableCommands() {
        return SERVER_AVAILABLE_COMMANDS;
    }


}