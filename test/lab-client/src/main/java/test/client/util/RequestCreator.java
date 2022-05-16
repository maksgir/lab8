package test.client.util;

import test.common.entities.Route;
import test.common.exceptions.WrongAmountOfArgsException;
import test.common.exceptions.WrongArgException;
import test.common.util.Request;

public class RequestCreator {
    public Request createRequestOfCommand(CommandToSend command) {
        String name = command.getCommandName();
        Request request;
        if (AvailableCommands.COMMANDS_WITHOUT_ARGS.contains(name)) {
            request = createRequestWithoutArgs(command);
        } else if (AvailableCommands.COMMANDS_WITH_ID_ARG.contains(name)) {
            request = createRequestWithID(command);
        } else if (AvailableCommands.COMMANDS_WITH_DISTANCE_ARG.contains(name)) {
            request = createRequestWithDistance(command);
        } else if (AvailableCommands.COMMANDS_WITH_ROUTE_ARG.contains(name)) {
            request = createRequestWithRoute(command);
        } else if (AvailableCommands.COMMANDS_WITH_ROUTE_ID_ARGS.contains(name)) {
            request = createRequestWithRouteID(command);
        } else {
            System.out.println("Нет такой команды среди доступных\n Введите HELP, чтоб посмотреть список доступных команд");
            request = null;
        }
        return request;
    }

    private Request createRequestWithoutArgs(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            return new Request("command", command.getCommandName());
        } catch (WrongAmountOfArgsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Request createRequestWithID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long id = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "ID должен быть больше единицы",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            return new Request("command", command.getCommandName(), id);
        } catch (WrongAmountOfArgsException | WrongArgException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка типа данных");
            return null;
        }
    }

    private Request createRequestWithDistance(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long distance = CommandValidators.validateArg(arg -> ((long) arg) > 1,
                    "Длина маршрута должна быть больше 1",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            return new Request("command", command.getCommandName(), distance);
        } catch (WrongAmountOfArgsException | WrongArgException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка типа данных");
            return null;
        }
    }

    private Request createRequestWithRoute(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            RouteGenerator generator = new RouteGenerator();
            generator.setVariables();
            Route route = generator.getGeneratedRoute();

            return new Request("command", command.getCommandName(), route);
        } catch (WrongAmountOfArgsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Request createRequestWithRouteID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long id = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "ID должен быть больше единицы",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            RouteGenerator generator = new RouteGenerator();
            generator.setVariables();
            Route route = generator.getGeneratedRoute();
            return new Request("command", command.getCommandName(), id, route);
        } catch (WrongAmountOfArgsException | WrongArgException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка типа данных");
            return null;
        }
    }

}
