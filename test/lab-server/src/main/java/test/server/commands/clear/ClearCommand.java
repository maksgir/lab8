package test.server.commands.clear;

import test.common.abstractions.AbstractCommand;
import test.common.exceptions.WrongArgException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.util.RoutesCollection;

public class ClearCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;
    private DBWorker db;

    public ClearCommand(RoutesCollection collectionInWork, DBWorker db) {
        super("clear", 0, "очищает коллекцию");
        this.routesCollection = collectionInWork;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            db.clear(request.getUser());
            routesCollection.clearCollection(request.getUser());
            return new Response(("Все ваши маршруты удалены"));

        } catch (WrongArgException e) {
            e.printStackTrace();
            return new Response((e.getMessage()));
        }


    }
}