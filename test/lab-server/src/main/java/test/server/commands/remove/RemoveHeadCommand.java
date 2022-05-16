package test.server.commands.remove;

import test.common.abstractions.AbstractCommand;
import test.common.entities.Route;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;


public class RemoveHeadCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;

    public RemoveHeadCommand(RoutesCollection routesCollection) {
        super("remove_head", 0, "возвращает первый элемент коллекции и удаляет его");
        this.routesCollection = routesCollection;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            Route res = routesCollection.removeHead();
            if (res == null) {
                return new Response("Ни один элемент не был удален");
            } else {
                return new Response(("Данный элемент был удален из коллеккции:"), res);
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}