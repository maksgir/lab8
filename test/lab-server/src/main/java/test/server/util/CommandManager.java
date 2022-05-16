package test.server.util;

import test.common.abstractions.AbstractCommand;
import test.common.abstractions.AbstractServerCommand;
import test.common.util.Request;
import test.common.util.Response;

public class CommandManager {

    public CommandManager(AbstractCommand helpClientCommand,
                          AbstractCommand infoCommand,
                          AbstractCommand showCommand,
                          AbstractCommand addCommand,
                          AbstractCommand updateCommand,
                          AbstractCommand removeByIdCommand,
                          AbstractCommand clearCommand,
                          AbstractCommand executeScriptCommand,
                          AbstractCommand exitCommand,
                          AbstractCommand removeHeadCommand,
                          AbstractCommand addIfMinCommand,
                          AbstractCommand removeLowerCommand,
                          AbstractCommand removeAnyByDistanceCommand,
                          AbstractCommand filterByDistanceCommand,
                          AbstractCommand filterGreaterThanDistance,
                          AbstractServerCommand serverHelpCommand,
                          AbstractServerCommand serverExitCommand) {

        ServerConfig.pushClientCommand(helpClientCommand.getName(), helpClientCommand);
        ServerConfig.pushClientCommand(infoCommand.getName(), infoCommand);
        ServerConfig.pushClientCommand(showCommand.getName(), showCommand);
        ServerConfig.pushClientCommand(addCommand.getName(), addCommand);
        ServerConfig.pushClientCommand(updateCommand.getName(), updateCommand);
        ServerConfig.pushClientCommand(removeByIdCommand.getName(), removeByIdCommand);
        ServerConfig.pushClientCommand(clearCommand.getName(), clearCommand);
        ServerConfig.pushClientCommand(executeScriptCommand.getName(), executeScriptCommand);
        ServerConfig.pushClientCommand(exitCommand.getName(), exitCommand);
        ServerConfig.pushClientCommand(removeHeadCommand.getName(), removeHeadCommand);
        ServerConfig.pushClientCommand(addIfMinCommand.getName(), addIfMinCommand);
        ServerConfig.pushClientCommand(removeLowerCommand.getName(), removeLowerCommand);
        ServerConfig.pushClientCommand(removeAnyByDistanceCommand.getName(), removeAnyByDistanceCommand);
        ServerConfig.pushClientCommand(filterByDistanceCommand.getName(), filterByDistanceCommand);
        ServerConfig.pushClientCommand(filterGreaterThanDistance.getName(), filterGreaterThanDistance);

        ServerConfig.pushServerCommand(serverHelpCommand.getName(), serverHelpCommand);
        ServerConfig.pushServerCommand(serverExitCommand.getName(), serverExitCommand);

    }

    public Response executeClientCommand(Request request) {
        return ServerConfig.getClientAvailableCommands().get(request.getCommandName()).executeClientCommand(request);
    }

    public String executeServerCommand(String commandName) {
        if (ServerConfig.getServerAvailableCommands().containsKey(commandName)) {
            return ServerConfig.getServerAvailableCommands().get(commandName).executeServerCommand();
        } else {
            return ("Нет такой команды, введите HELP, чтобы увидеть список доступных");
        }
    }
}