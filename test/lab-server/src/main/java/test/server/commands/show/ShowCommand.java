package test.server.commands.show;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class ShowCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;

    public ShowCommand(RoutesCollection collectionInWork) {
        super("show", 0, "отображает все элемент коллекции и информацию о них");
        this.routesCollection = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        if (routesCollection.getListOfRoutes().isEmpty()) {
            return new Response("Коллекция пустая");
        } else {
            return new Response(("Элементы коллекции:"), routesCollection.getListOfRoutes());
        }
    }
}