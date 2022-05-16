package test.server.commands.clear;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class ClearCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;

    public ClearCommand(RoutesCollection collectionInWork) {
        super("clear", 0, "очищает коллекцию");
        this.routesCollection = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        if (routesCollection.getListOfRoutes().isEmpty()) {
            return new Response(("Коллекция уже пуста"));
        } else {
            routesCollection.clearCollection();
            return new Response(("Коллекция очищена"));
        }
    }
}