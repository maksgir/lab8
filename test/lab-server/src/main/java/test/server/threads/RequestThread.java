package test.server.threads;

import test.common.entities.User;
import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.socket.ServerSocketWorker;
import test.server.util.CommandManager;
import test.server.util.RoutesCollection;
import test.server.util.ServerConfig;

import java.io.IOException;

public class RequestThread extends Thread {

    private final ServerSocketWorker serverSocketWorker;
    private final DBWorker dbWorker;
    private CommandManager commandManager;
    private RoutesCollection routesCollection;


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

                    System.out.println("Запрос типа : " + acceptedRequest.getType() + " был принят");

                    if (acceptedRequest.getType().equals("registration")) {
                        User user = acceptedRequest.getUser();
                        dbWorker.addUser(user);
                        Response response = new Response(user.getName() + " ,Вы успешно зарегистрированы", true);
                        serverSocketWorker.sendResponse(response);
                    } else if (acceptedRequest.getType().equals("login")) {
                        User user = acceptedRequest.getUser();
                        if (dbWorker.isLoggedIn(user)) {
                            Response response = new Response("Добро пожаловать, " + user.getLogin(), true);
                            serverSocketWorker.sendResponse(response);
                        }
                    } else if (acceptedRequest.getType().equals("command")) {
                        routesCollection.sort();
                        System.out.println("Сервер выполняет команду: " + acceptedRequest.getCommandName());
                        Response responseToSend = commandManager.executeClientCommand(acceptedRequest);
                        serverSocketWorker.sendResponse(responseToSend);
                    }


                }
            } catch (ClassNotFoundException e) {
                System.out.println("An error occurred while deserializing the request, try again");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (UserAlreadyExistsException e) {
                System.out.println("Зарегистрированный пользователь пытался зарегистрироваться");
                Response response = new Response(e.getMessage(), false);
                try {
                    serverSocketWorker.sendResponse(response);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (IncorrectUserDataException e) {
                System.out.println("Пользователь ввел неправильные данные");
                Response response = new Response(e.getMessage(), false);
                try {
                    serverSocketWorker.sendResponse(response);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            serverSocketWorker.stopServer();
        } catch (IOException e) {
            System.out.println("An error occurred during stopping the server");
        }
    }
}