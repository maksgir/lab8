package test.server.threads;

import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.entities.User;
import test.server.socket.ServerSocketWorker;
import test.server.util.UserCreator;

import java.io.IOException;

public class RequestThread extends Thread {

    private final ServerSocketWorker serverSocketWorker;
    private final DBWorker dbWorker;
    private boolean run = true;


    public RequestThread(ServerSocketWorker serverSocketWorker, DBWorker dbWorker) {
        this.serverSocketWorker = serverSocketWorker;
        this.dbWorker = dbWorker;
    }

    @Override
    public void run() {
        while (run) {
            try {
                Request acceptedRequest = serverSocketWorker.listenForRequest();
                if (acceptedRequest != null) {

                    System.out.println("Запрос на : " + acceptedRequest.getType() + " был принят");
                    System.out.println(acceptedRequest.getLogin());
                    System.out.println(acceptedRequest.getPassword());
                    if (acceptedRequest.getType().equals("registration")) {
                        User user = UserCreator.createUser(acceptedRequest);
                        dbWorker.addUser(user);
                        Response response = new Response(user.getName() + " ,Вы успешно зарегистрированы", true);
                        serverSocketWorker.sendResponse(response);
                    } else if (acceptedRequest.getType().equals("login")){
                        User user = UserCreator.createSmplUser(acceptedRequest);
                        if (dbWorker.isLoggedIn(user)){
                            Response response = new Response("Добро пожаловать, " + user.getLogin(), true);
                            serverSocketWorker.sendResponse(response);
                        }
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