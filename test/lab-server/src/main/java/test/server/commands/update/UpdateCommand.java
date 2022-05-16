package test.server.commands.update;

import test.common.abstractions.AbstractCommand;
import test.common.exceptions.IDNotFoundException;
import test.common.exceptions.NotAnOwnerException;
import test.common.exceptions.WrongArgException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.util.RoutesCollection;

public class UpdateCommand extends AbstractCommand {


    private final RoutesCollection collectionInWork;
    private final DBWorker db;

    public UpdateCommand(RoutesCollection collectionManager, DBWorker db) {
        super("update", 1,
                "обновляет элемент коллекции, ID которого равен заданному",
                "id");
        this.collectionInWork = collectionManager;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            db.updateById(request.getRoute(), (int) request.getNumber(), request.getUser());
            collectionInWork.updateById((int) request.getNumber(), request.getRoute());
            return new Response(("Элемент с ID= " + request.getNumber() + " был обновлен"));
        } catch (IDNotFoundException | NotAnOwnerException | WrongArgException e) {
            return new Response((e.getMessage()));
        }
    }
}