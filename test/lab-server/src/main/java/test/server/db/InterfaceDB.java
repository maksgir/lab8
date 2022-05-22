package test.server.db;

import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.*;

import java.io.IOException;
import java.util.List;

public interface InterfaceDB {

    void addUser(User user) throws UserAlreadyExistsException;

    boolean checkLogin(User user) throws IncorrectUserDataException;

    boolean checkOwner(User user, int routeId) throws NotAnOwnerException;

    int addRoute(Route route, User user) throws AddRouteToDbException;

    void updateById(Route route, int id) throws WrongArgException;

    void removeById(int id) throws WrongArgException;

    void clear(User user) throws WrongArgException;

    List<Route> getAllRoutes() throws WrongArgException;

    void createTables(String file) throws IOException;
}
