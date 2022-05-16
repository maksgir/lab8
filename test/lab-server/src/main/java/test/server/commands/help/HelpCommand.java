package test.server.commands.help;

import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;
import test.common.abstractions.AbstractCommand;
import test.common.util.Request;
import test.common.util.Response;

import java.util.HashMap;

public class HelpCommand extends AbstractCommand {
    private final HashMap<String, AbstractCommand> availableCommands;

    public HelpCommand(HashMap<String, AbstractCommand> availableCommands) {
        super("help", 0, "показывает список доступных команд");
        this.availableCommands = availableCommands;
    }

    @Override
    public Response executeClientCommand(Request request) {
        StringBuilder sb = new StringBuilder();
        for (AbstractCommand command : availableCommands.values()) {
            sb.append(command.toString()).append("\n");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        return new Response(("Доступные команды:\n") + sb);
    }
}
