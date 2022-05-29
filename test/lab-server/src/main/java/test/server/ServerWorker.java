package test.server;

import test.common.exceptions.WrongArgException;
import test.server.commands.add.AddCommand;
import test.server.commands.add.AddIfMinCommand;
import test.server.commands.clear.ClearCommand;
import test.server.commands.execute.ExecuteScriptCommand;
import test.server.commands.exit.ExitCommand;
import test.server.commands.filter.FilterByDistanceCommand;
import test.server.commands.filter.FilterGreaterThanDistanceCommand;
import test.server.commands.help.HelpCommand;
import test.server.commands.info.InfoCommand;
import test.server.commands.remove.RemoveAnyByDistanceCommand;
import test.server.commands.remove.RemoveByIdCommand;
import test.server.commands.remove.RemoveHeadCommand;
import test.server.commands.remove.RemoveLowerCommand;
import test.server.commands.serverCommands.ServerExitCommand;
import test.server.commands.serverCommands.ServerHelpCommand;
import test.server.commands.show.ShowCommand;
import test.server.commands.update.UpdateCommand;
import test.server.db.DBWorker;
import test.server.db.ImplDB;
import test.server.socket.ServerSocketWorker;
import test.server.threads.ConsoleThread;
import test.server.threads.RequestThread;
import test.server.util.CommandManager;
import test.server.util.RoutesCollection;
import test.server.util.ServerCommandListener;
import test.server.util.ServerConfig;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Scanner;

public class ServerWorker {
    private final Scanner scanner = new Scanner(System.in);
    private ServerSocketWorker serverSocketWorker;
    private final ServerCommandListener serverCommandListener = new ServerCommandListener(scanner);
    private DBWorker dbWorker;
    private CommandManager commandManager;
    private RoutesCollection routesCollection = new RoutesCollection();


    public ServerWorker() {

    }


    public void startServerWorker() {

        authorizeSocket();
        authorizeDb();
        authorizeCollection();
        authorizeCommandManager();


        System.out.println("Добро пожаловать на сервер. Введите HELP чтобы увидеть список доступных команд");

        RequestThread requestThread = new RequestThread(serverSocketWorker, dbWorker, commandManager, routesCollection);
        ConsoleThread consoleThread = new ConsoleThread(serverCommandListener, commandManager);
        requestThread.start();
        consoleThread.start();
    }

    private void authorizeDb() {
        dbWorker = new DBWorker(new ImplDB());
    }

    private void authorizeCommandManager() {
        commandManager = new CommandManager(
                new HelpCommand(ServerConfig.getClientAvailableCommands()),
                new InfoCommand(routesCollection),
                new ShowCommand(routesCollection),
                new AddCommand(routesCollection, dbWorker),
                new UpdateCommand(routesCollection, dbWorker),
                new RemoveByIdCommand(routesCollection, dbWorker),
                new ClearCommand(routesCollection, dbWorker),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new RemoveHeadCommand(routesCollection, dbWorker),
                new AddIfMinCommand(routesCollection, dbWorker),
                new RemoveLowerCommand(routesCollection, dbWorker),
                new RemoveAnyByDistanceCommand(routesCollection, dbWorker),
                new FilterByDistanceCommand(routesCollection),
                new FilterGreaterThanDistanceCommand(routesCollection),

                new ServerHelpCommand(ServerConfig.getServerAvailableCommands()),
                new ServerExitCommand(scanner, routesCollection));
    }

    private void authorizeCollection() {
        try {
            routesCollection.setRoutes(new ArrayDeque<>(dbWorker.getAllRoutes()));
            routesCollection.sort();
        } catch (WrongArgException e) {
            e.printStackTrace();
        }

    }

    private void authorizeSocket() {
        try{
            serverSocketWorker = new ServerSocketWorker();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
