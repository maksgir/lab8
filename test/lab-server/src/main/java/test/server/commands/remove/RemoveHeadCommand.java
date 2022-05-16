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


public class RemoveHeadCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;
    private final DBWorker db;

    public RemoveHeadCommand(RoutesCollection routesCollection, DBWorker db) {
        super("remove_head", 0, "возвращает первый элемент коллекции и удаляет его");
        this.routesCollection = routesCollection;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            Route res = routesCollection.showHead();
            if (res == null) {
                return new Response("Ни один элемент не был удален");
            } else {
                int id = (int)res.getId();
                User user = request.getUser();
                try {
                    db.removeById(id, user);
                    routesCollection.removeRouteById(id);
                    return new Response(("Маршрут с ID " + id + " был удален из коллекции"));
                } catch (IDNotFoundException | NotAnOwnerException | WrongArgException e) {
                    return new Response((e.getMessage()));
                }
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}