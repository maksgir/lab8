package test.server;

/**
 * Класс - сервер, принимает запросы от клиентов и отдает данные
 */
public class Server {
    public static void main(String[] args) {

        ServerWorker serverWorker = new ServerWorker();
        serverWorker.startServerWorker();


    }
}