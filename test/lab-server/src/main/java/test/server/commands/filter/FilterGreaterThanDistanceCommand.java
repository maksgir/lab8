package test.server.commands.filter;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class FilterGreaterThanDistanceCommand extends AbstractCommand {
    private final RoutesCollection routesCollection;

    public FilterGreaterThanDistanceCommand(RoutesCollection routesCollection) {
        super("filter_greater_than_distance",
                1,
                " вывести элементы, значение поля distance которых больше заданного",
                "distance");
        this.routesCollection = routesCollection;
    }
    @Override
    public Response executeClientCommand(Request request) {
        try {
            return new Response(("Маршруты с дистанцией больше чем  " + request.getNumber()),
                    routesCollection.filterGreaterByDistance(request.getNumber()));
        } catch (Exception e) {
            return new Response((e.getMessage()));
        }
    }
}
