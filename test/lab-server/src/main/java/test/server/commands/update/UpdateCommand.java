package test.server.commands.update;

import test.common.abstractions.AbstractCommand;
import test.common.exceptions.IDNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;
import test.common.abstractions.AbstractCommand;
import test.common.exceptions.IDNotFoundException;
import test.common.util.Request;
import test.common.util.Response;
import test.server.util.RoutesCollection;

public class UpdateCommand extends AbstractCommand {


    private final RoutesCollection collectionInWork;

    public UpdateCommand(RoutesCollection collectionManager) {
        super("update", 1,
                "обновляет элемент коллекции, ID которого равен заданному",
                "id");
        this.collectionInWork = collectionManager;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            collectionInWork.updateById(request.getNumber(), request.getRoute());
            return new Response(("Element with ID " + request.getNumber() + " was updated"));
        } catch (IDNotFoundException e) {
            return new Response((e.getMessage()));
        }
    }
}