package test.server.threads;

import test.common.entities.User;
import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.socket.ServerSocketWorker;
import test.server.util.CommandManager;
import test.server.util.PasswordEncoder;
import test.server.util.RoutesCollection;

import java.util.concurrent.Executor;

public class RequestHandler implements Runnable {
    private Request acceptedRequest;
    private DBWorker dbWorker;
    private RoutesCollection routesCollection;
    private CommandManager commandManager;
    private Executor fixedThreadPool;
    private ServerSocketWorker serverSocketWorker;


    public RequestHandler(Request request, DBWorker dbWorker, RoutesCollection routesCollection, CommandManager commandManager, Executor fixed, ServerSocketWorker serverSocketWorker) {
        this.acceptedRequest = request;
        this.dbWorker = dbWorker;
        this.routesCollection = routesCollection;
        this.commandManager = commandManager;
        this.fixedThreadPool = fixed;
        this.serverSocketWorker = serverSocketWorker;
    }


    @Override
    public void run() {
        System.out.println("Запрос типа : " + acceptedRequest.getType() + "от " + acceptedRequest.getUser().getLogin() + " был принят");
        try {
            if (acceptedRequest.getType().equals("registration")) {
                User user = acceptedRequest.getUser();
                user.setPassword(PasswordEncoder.shaEncode(user.getPassword()));
                dbWorker.addUser(user);
                Response response = new Response(user.getName() + " ,Вы успешно зарегистрированы", true);
                SenderThread sender = new SenderThread(response, serverSocketWorker);
                fixedThreadPool.execute(sender);

            } else if (acceptedRequest.getType().equals("login")) {
                User user = acceptedRequest.getUser();
                user.setPassword(PasswordEncoder.shaEncode(user.getPassword()));
                if (dbWorker.isLoggedIn(user)) {
                    Response response = new Response("Добро пожаловать, " + user.getLogin(), true);
                    SenderThread sender = new SenderThread(response, serverSocketWorker);
                    fixedThreadPool.execute(sender);
                }
            } else if (acceptedRequest.getType().equals("command")) {
                User user = acceptedRequest.getUser();
                user.setPassword(PasswordEncoder.shaEncode(user.getPassword()));
                Response responseToSend;
                if (dbWorker.isLoggedIn(user)) {
                    routesCollection.sort();
                    System.out.println("Сервер выполняет команду: " + acceptedRequest.getCommandName());
                    responseToSend = commandManager.executeClientCommand(acceptedRequest);
                } else {
                    responseToSend = new Response("Вы не авторизованы");
                }
                SenderThread sender = new SenderThread(responseToSend, serverSocketWorker);
                fixedThreadPool.execute(sender);
            }
        } catch (UserAlreadyExistsException e) {
            System.out.println("Зарегистрированный пользователь пытался зарегистрироваться");
            Response response = new Response(e.getMessage(), false);
            SenderThread sender = new SenderThread(response, serverSocketWorker);
            fixedThreadPool.execute(sender);

        } catch (IncorrectUserDataException e) {
            System.out.println("Пользователь ввел неправильные данные");
            Response response = new Response(e.getMessage(), false);
            SenderThread sender = new SenderThread(response, serverSocketWorker);
            fixedThreadPool.execute(sender);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
