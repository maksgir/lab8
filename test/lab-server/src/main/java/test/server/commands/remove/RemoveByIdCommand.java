package test.server.commands.remove;

import test.common.abstractions.AbstractCommand;
import test.common.exceptions.IDNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.exceptions.IDNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class RemoveByIdCommand extends AbstractCommand {

    private final RoutesCollection collectionInWork;

    public RemoveByIdCommand(RoutesCollection collectionInWork) {
        super("remove_by_id", 1, "удалить элемент коллекции с заданным ID", "id");
        this.collectionInWork = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            collectionInWork.removeRouteById(request.getNumber());
            return new Response(("Маршрут с ID " + request.getNumber() + " был удален из коллекции"));
        } catch (IDNotFoundException e) {
            return new Response((e.getMessage()));
        }
    }
}