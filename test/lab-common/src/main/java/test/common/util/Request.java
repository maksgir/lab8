package test.common.util;

import test.common.entities.Route;
import test.common.entities.User;

import java.io.Serializable;

public class Request implements Serializable {

    private String type;

    private User user;


    private String commandName;
    private long number;
    private Route route;


    public Request(String type, User user) {
        this.type = type;
        this.user = user;
    }

    public Request(String type, User user, String command) {
        this.type = type;
        this.user = user;
        this.commandName = command;
    }

    public Request(String type, User user, String command, Route route) {
        this.type = type;
        this.user = user;
        this.commandName = command;
        this.route = route;
    }

    public Request(String type, User user, String command, int id) {
        this.type = type;
        this.user = user;
        this.commandName = command;
        this.number = id;
    }

    public Request(String type, User user, String command, long distance) {
        this.type = type;
        this.user = user;
        this.commandName = command;
        this.number = distance;
    }


    public Request(String type, User user, String command, Route route, int id) {
        this.type = type;
        this.user = user;
        this.commandName = command;
        this.route = route;
        this.number = id;
    }

    public Request(String type, User user, int id) {
        this.type = type;
        this.user = user;
        this.number = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}