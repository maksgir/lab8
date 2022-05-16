package test.server.commands.serverCommands;

import test.common.abstractions.AbstractServerCommand;

import test.server.util.ServerConfig;
import test.common.abstractions.AbstractServerCommand;
import test.server.util.RoutesCollection;
import test.server.util.ServerConfig;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerExitCommand extends AbstractServerCommand {
    private final Scanner scanner;

    private final RoutesCollection routesCollection;

    public ServerExitCommand(Scanner scanner,  RoutesCollection routesCollection) {
        super("exit", "заглушить работу сервера");
        this.scanner = scanner;
        this.routesCollection = routesCollection;
    }

    @Override
    public String executeServerCommand() {
        ServerConfig.toggleRun();
        return ("Заглушение сервера...");
    }

}