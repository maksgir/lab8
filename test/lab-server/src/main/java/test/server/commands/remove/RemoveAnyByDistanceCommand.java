package test.server.commands.remove;


import test.common.abstractions.AbstractCommand;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.exceptions.GroupNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.exceptions.GroupNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class RemoveAnyByDistanceCommand extends AbstractCommand {

    private final RoutesCollection routesCollection;

    public RemoveAnyByDistanceCommand(RoutesCollection routesCollection) {
        super("remove_any_by_distance",
                1,
                "удалить из коллекции один элемент, значение поля distance которого эквивалентно заданному",
                "distance");
        this.routesCollection = routesCollection;
    }



    @Override
    public Response executeClientCommand(Request request) {
        try {
            return new Response(("Маршрут с " + request.getNumber() + " дистанцией был удален"),
                    routesCollection.removeAnyByDistance(request.getNumber()));
        } catch (GroupNotFoundException | CollectionIsEmptyException e) {
            return new Response((e.getMessage()));
        }
    }
}