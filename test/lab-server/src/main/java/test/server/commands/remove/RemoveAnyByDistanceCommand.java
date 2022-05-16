package test.server.commands.remove;


import test.common.abstractions.AbstractCommand;
import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.IDNotFoundException;
import test.common.exceptions.NotAnOwnerException;
import test.common.exceptions.WrongArgException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.util.RoutesCollection;

public class RemoveAnyByDistanceCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;
    private DBWorker db;

    public RemoveAnyByDistanceCommand(RoutesCollection routesCollection, DBWorker db) {
        super("remove_any_by_distance",
                1,
                "удалить из коллекции один элемент, значение поля distance которого эквивалентно заданному",
                "distance");
        this.routesCollection = routesCollection;
        this.db = db;
    }


    @Override
    public Response executeClientCommand(Request request) {
        Route routeToDel = routesCollection.AnyEqDistance(request.getNumber());
        if (routeToDel == null) {
            return new Response("Не нашлось маршрутов с указанной дистанцией");
        } else {
            User user = request.getUser();
            int id = routeToDel.getId();
            try {
                db.removeById(id, user);
                routesCollection.removeRouteById(id);
                return new Response(("Маршрут с ID " + id + " был удален из коллекции"));
            } catch (IDNotFoundException | NotAnOwnerException | WrongArgException e) {
                return new Response((e.getMessage()));
            }
        }

    }
}