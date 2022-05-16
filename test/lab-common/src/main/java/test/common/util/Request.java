package test.common.util;

import test.common.entities.Route;
import test.common.entities.User;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Request implements Serializable {

    private String type;

    private User user;



    private String commandName;
    private long number;
    private Route route;


    public Request(String type, String command, long number){
        this.type = type;
        this.commandName = command;
        this.number = number;
    }
    public Request(String type, String command){
        this.type = type;
        this.commandName = command;
    }

    public Request(String type, String command, Route route){
        this.type = type;
        this.commandName = command;
        this.route = route;
    }


    public Request(String type, User user){
        this.type = type;
        this.user = user;
    }

    public Request(String type, String commandName, long id, Route generatedRoute) {
        this.type = type;
        this.commandName = commandName;
        this.number = id;
        this.route = generatedRoute;
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