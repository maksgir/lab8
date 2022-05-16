package test.server.commands.info;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class InfoCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;

    public InfoCommand(RoutesCollection collectionInWork) {
        super("info", 0, "отображает информацию о коллекции");
        this.routesCollection = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return new Response(("Информация о коллекции:\n") + routesCollection.returnInfo());
    }
}