package test.server.commands.exit;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;

public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", 0, "завершает работу с клиентом");
    }

    @Override
    public Response executeClientCommand(Request request) {
        return null;
    }
}