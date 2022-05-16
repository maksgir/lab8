package test.server.commands.add;


import test.common.abstractions.AbstractCommand;
import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.AddRouteToDbException;
import test.common.exceptions.GroupNotMinException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.util.RoutesCollection;

public class AddIfMinCommand extends AbstractCommand {
    private final RoutesCollection routesCollection;
    private final DBWorker db;

    public AddIfMinCommand(RoutesCollection routesCollection, DBWorker db) {
        super("add_if_min", 0, "добавляет новый элемент в коллекцию, если его значение меньше наименьшего элемента");
        this.routesCollection = routesCollection;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        Route route = request.getRoute();
        User user = request.getUser();
        route.setOwner(user.getLogin());
        try {
            if (routesCollection.checkIfMin(request.getRoute())) {
                int id = db.addRoute(route, user);
                route.setId(id);
                routesCollection.addRoute(route);
                return new Response(("Новый элемент был успешно добавлен!"), request.getRoute());
            } else {
                return new Response("");
            }
        } catch (AddRouteToDbException | GroupNotMinException e) {
            return new Response((e.getMessage()));
        }
    }
}