package test.server.commands.add;


import test.common.abstractions.AbstractCommand;
import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.AddRouteToDbException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.db.DBWorker;
import test.server.util.RoutesCollection;

public class AddCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;
    private final DBWorker db;

    public AddCommand(RoutesCollection collectionInWork, DBWorker db) {
        super("add", 0, "добавить новый элемент в коллекцию");
        this.routesCollection = collectionInWork;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        Route route = request.getRoute();
        User user = request.getUser();
        try {
            db.addRoute(route, user);
            routesCollection.addRoute(route);
            return new Response(("Новый элемент был успешно добавлен!"), request.getRoute());

        } catch (AddRouteToDbException e) {
            return new Response(e.getMessage());
        }


    }
}