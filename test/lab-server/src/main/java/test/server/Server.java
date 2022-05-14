package test.server;

/**
 * Класс - сервер, принимает запросы от клиентов и отдает данные
 */
public class Server {
    public static void main(String[] args) {
        //InterfaceDB db = new ImplDB();
        //db.addPerson("hui", 12);
        //db.addPerson("negr", 122);

        ServerWorker serverWorker = new ServerWorker();
        serverWorker.startServerWorker();


    }
}