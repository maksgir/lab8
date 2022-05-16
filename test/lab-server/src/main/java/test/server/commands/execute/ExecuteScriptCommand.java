package test.server.commands.execute;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;

import java.util.HashSet;
import java.util.Set;

public class ExecuteScriptCommand extends AbstractCommand {

    private static final Set<String> FILE_HISTORY = new HashSet<>();

    public ExecuteScriptCommand() {
        super("execute_script", 1, "читает и исполняет скрипт из файла", "file name");
    }

    @Override
    public Response executeClientCommand(Request request) {
        return null;
    }
}