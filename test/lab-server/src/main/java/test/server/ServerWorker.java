package test.server;

import test.server.db.DBWorker;
import test.server.db.ImplDB;
import test.server.socket.ServerSocketWorker;
import test.server.threads.RequestThread;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerWorker {
    private final Scanner scanner = new Scanner(System.in);
    private final int maxPort = 65535;
    private ServerSocketWorker serverSocketWorker;
    private DBWorker dbWorker;

    public void startServerWorker(){
        authorizeDb();
        inputPort();
        System.out.println("Добро пожаловать на сервер. ");
        RequestThread requestThread = new RequestThread(serverSocketWorker, dbWorker);
        requestThread.start();
    }

    private void authorizeDb(){
        dbWorker = new DBWorker(new ImplDB());
    }

    private void inputPort() {
        System.out.println("Вы хотите использовать дефолтный порт? [y/n]");
        try {
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                System.out.println("Введите номер порта (1-65535)");
                String port = scanner.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= maxPort) {
                        serverSocketWorker = new ServerSocketWorker(portInt);
                    } else {
                        System.out.println("Номер порта не подходит под критерии,попробуйте еще раз");
                        inputPort();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка ввода числа, попробуйте еще раз");
                    inputPort();
                }
            } else if ("y".equals(s)) {
                serverSocketWorker = new ServerSocketWorker();
            } else {
                System.out.println("Вы введи невалидный символ, попробуйте еще раз");
                inputPort();
            }
        } catch (NoSuchElementException | IOException e) {
            System.out.println("Введен неверный символ, отключение от сервера");
            System.exit(1);
        }
    }
}
