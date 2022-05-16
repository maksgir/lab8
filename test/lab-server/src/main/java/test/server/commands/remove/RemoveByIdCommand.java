package test.server.commands.remove;

import test.common.abstractions.AbstractCommand;
import test.common.entities.User;
import test.common.exceptions.IDNotFoundException;
import test.common.exceptions.NotAnOwnerException;
import test.common.exceptions.WrongArgException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.exceptions.IDNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class RemoveByIdCommand extends AbstractCommand {

    private final RoutesCollection collectionInWork;
    private final DBWorker db;

    public RemoveByIdCommand(RoutesCollection collectionInWork, DBWorker db) {
        super("remove_by_id", 1, "удалить элемент коллекции с заданным ID", "id");
        this.collectionInWork = collectionInWork;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        int id = (int)request.getNumber();
        User user = request.getUser();
        try {
            db.removeById(id, user);
            collectionInWork.removeRouteById(id);
            return new Response(("Маршрут с ID " + id + " был удален из коллекции"));
        } catch (IDNotFoundException | NotAnOwnerException | WrongArgException e) {
            return new Response((e.getMessage()));
        }
    }
}