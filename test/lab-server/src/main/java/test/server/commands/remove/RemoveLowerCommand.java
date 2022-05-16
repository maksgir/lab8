package test.server.commands.remove;


import test.common.abstractions.AbstractCommand;
import test.common.entities.Route;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.entities.Route;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

import java.util.List;
import java.util.Set;

public class RemoveLowerCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;

    public RemoveLowerCommand(RoutesCollection routesCollection) {
        super("remove_lower", 0, "удалить из коллекции все элементы, меньшие, чем заданный");
        this.routesCollection = routesCollection;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            List<Route> res = routesCollection.removeIfLower(request.getRoute());
            if (res.isEmpty()) {
                return new Response(("Ни один элемент не был удален из коллекцию"));
            } else {
                return new Response(("Этот элемент был удален из коллекции:"), res);
            }
        } catch (CollectionIsEmptyException e) {
            return new Response((e.getMessage()));
        }
    }
}