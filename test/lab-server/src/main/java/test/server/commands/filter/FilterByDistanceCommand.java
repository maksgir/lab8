package test.server.commands.filter;

import test.common.abstractions.AbstractCommand;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.exceptions.GroupNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class FilterByDistanceCommand extends AbstractCommand {
    private final RoutesCollection routesCollection;

    public FilterByDistanceCommand(RoutesCollection routesCollection) {
        super("filter_by_distance",
                1,
                "вывести элементы, значение поля distance которых равно заданному",
                "distance");
        this.routesCollection = routesCollection;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            return new Response(("Маршруты с дистанцией равной  " + request.getNumber()),
                    routesCollection.filterByDistance(request.getNumber()));
        } catch (Exception e) {
            return new Response((e.getMessage()));
        }
    }

}
