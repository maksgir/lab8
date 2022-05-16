package test.server.db;

import test.common.entities.Route;
import test.common.exceptions.AddRouteToDbException;
import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.common.entities.User;

public class DBWorker {
    private InterfaceDB db;

    public DBWorker(InterfaceDB db) {
        this.db = db;
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        db.addUser(user);
    }
    public boolean isLoggedIn(User user) throws IncorrectUserDataException {
        return db.checkLogin(user);
    }

    public void addRoute(Route route, User user) throws AddRouteToDbException {
        db.addRoute(route, user);
    }
}
