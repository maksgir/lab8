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

import java.util.List;

public class RemoveLowerCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;
    private DBWorker db;

    public RemoveLowerCommand(RoutesCollection routesCollection, DBWorker db) {
        super("remove_lower", 0, "удалить из коллекции все элементы, меньшие, чем заданный");
        this.routesCollection = routesCollection;
        this.db = db;
    }

    @Override
    public Response executeClientCommand(Request request) {
        List<Route> routeList = routesCollection.checkLower(request.getRoute());
        if (routeList.isEmpty()) {
            return new Response(("Нет маршрутов меньших, чем заданный"));
        }
        boolean flag = false;
        for (Route r : routeList) {
            int id = (int) r.getId();
            User user = request.getUser();
            try {
                db.removeById(id, user);
                routesCollection.removeRouteById(id);
                flag = true;

            } catch (IDNotFoundException | NotAnOwnerException | WrongArgException e) {

            }
        }
        if (flag) {
            return new Response(("Ваши маршруты, меньшие чем заданный, были удалены"));
        } else {
            return new Response(("Ко всем меньшим маршрутам, чем заданный, у вас нет прав"));
        }
    }
}