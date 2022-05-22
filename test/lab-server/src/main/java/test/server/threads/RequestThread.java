package test.server.threads;

import test.common.util.Request;
import test.server.db.DBWorker;
import test.server.socket.ServerSocketWorker;
import test.server.util.CommandManager;
import test.server.util.RoutesCollection;
import test.server.util.ServerConfig;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RequestThread extends Thread {

    private final ServerSocketWorker serverSocketWorker;
    private final DBWorker dbWorker;
    private CommandManager commandManager;
    private RoutesCollection routesCollection;
    private Executor fixedThreadPool = Executors.newFixedThreadPool(6);


    public RequestThread(ServerSocketWorker serverSocketWorker, DBWorker dbWorker, CommandManager commandManage, RoutesCollection routesCollection) {
        this.serverSocketWorker = serverSocketWorker;
        this.dbWorker = dbWorker;
        this.commandManager = commandManage;
        this.routesCollection = routesCollection;
    }

    @Override
    public void run() {
        while (ServerConfig.getRunning()) {
            try {
                Request acceptedRequest = serverSocketWorker.listenForRequest();
                if (acceptedRequest != null) {
                    new Thread(new RequestHandler(acceptedRequest, dbWorker, routesCollection, commandManager, fixedThreadPool, serverSocketWorker)).start();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("An error occurred while deserializing the request, try again");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            serverSocketWorker.stopServer();
        } catch (IOException e) {
            System.out.println("An error occurred during stopping the server");
        }
    }
}