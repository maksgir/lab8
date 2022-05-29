package test.client.util;

import test.common.entities.User;
import test.common.util.Request;

public class RequestCreator {
    public static Request createLoginRequest(User user) {
        return new Request("login", user);
    }

    public static Request createRegistrationRequest(User user) {
        return new Request("registration", user);
    }

    public static Request createCheckAccessRequest(User user, int id){
        return new Request("check", user, id);
    }

    public static Request createCommandWithRouteRequest(Command command) {
        return new Request("command", command.getUser(), command.getName(), command.getRoute());
    }

    public static Request createCommandWithIdRequest(Command command){
        return new Request("command", command.getUser(), command.getName(), command.getId());
    }

    public static Request createCommandWithOutArgs(Command command){
        return new Request("command", command.getUser(), command.getName());
    }

    public static Request createCommandWithDistance(Command command){
        return new Request("command", command.getUser(), command.getName(), command.getDistance());
    }

    public static Request createCommandWithIdAndRouteRequest(Command command){
        return new Request("command", command.getUser(), command.getName(), command.getRoute(), command.getId());
    }
}
