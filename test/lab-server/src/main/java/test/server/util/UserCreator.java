package test.server.util;

import test.common.util.Request;
import test.server.entities.User;

import java.time.ZonedDateTime;

public class UserCreator {
    public static User createUser(Request request){
        String name = request.getName();
        String login = request.getLogin();
        String password = request.getPassword();
        ZonedDateTime zdt = request.getData();
        return new User(name, login, password, zdt);
    }

    public static User createSmplUser(Request request){
        String login = request.getLogin();
        String password = request.getPassword();
        return new User(login, password);
    }
}
