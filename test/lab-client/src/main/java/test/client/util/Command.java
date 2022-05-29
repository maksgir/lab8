package test.client.util;

import test.common.entities.Route;
import test.common.entities.User;

public class Command {
    private String name;

    private User user;

    private Route route;

    private int id;

    private long distance;

    public Command(User user, String name, Route route) {
        this.user = user;
        this.name = name;
        this.route = route;
    }

    public Command(User user, String name, int id) {
        this.user = user;
        this.name = name;
        this.id = id;
    }

    public Command(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Command(User user, String name,  int id, Route route) {
        this.name = name;
        this.user = user;
        this.route = route;
        this.id = id;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public Command(User user, String name, long distance) {
        this.name = name;
        this.user = user;
        this.distance = distance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
