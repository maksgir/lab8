package test.server.db;

import test.common.entities.Route;
import test.common.exceptions.AddRouteToDbException;
import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.server.entities.Person;
import test.common.entities.User;

import java.util.List;

public interface InterfaceDB {
    void addUser(User user) throws UserAlreadyExistsException;
    boolean checkLogin(User user) throws IncorrectUserDataException;
    void addRoute(Route route, User user) throws AddRouteToDbException;
}
