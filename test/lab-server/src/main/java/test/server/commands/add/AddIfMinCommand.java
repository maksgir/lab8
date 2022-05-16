package test.server.commands.add;


import test.common.abstractions.AbstractCommand;
import test.common.exceptions.GroupNotMinException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.exceptions.GroupNotMinException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class AddIfMinCommand extends AbstractCommand {
    private final RoutesCollection routesCollection;

    public AddIfMinCommand(RoutesCollection routesCollection) {
        super("add_if_min", 0, "добавляет новый элемент в коллекцию, если его значение меньше наименьшего элемента");
        this.routesCollection = routesCollection;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            routesCollection.addIfMin(request.getRoute());
            return new Response(("Новый элемент был успешно добавлен"), request.getRoute());
        } catch (GroupNotMinException e) {
            return new Response((e.getMessage()));
        }
    }
}