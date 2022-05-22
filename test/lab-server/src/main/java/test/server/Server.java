package test.server;

/**
 * Класс - сервер, принимает запросы от клиентов и отдает данные
 */
public class Server {
    public static void main(String[] args) {
        ServerWorker serverWorker;
        if (args.length!=0) {
            serverWorker = new ServerWorker(args[0]);
        } else {
            serverWorker = new ServerWorker();
        }

        serverWorker.startServerWorker();


    }
}