package test.server.db;

import test.common.entities.Route;
import test.common.exceptions.*;
import test.common.entities.User;

import java.io.IOException;
import java.util.List;

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

    public int addRoute(Route route, User user) throws AddRouteToDbException {
        return db.addRoute(route, user);
    }

    public void updateById(Route route, int id, User user) throws NotAnOwnerException, WrongArgException {
        if (db.checkOwner(user, id)){
            db.updateById(route, id);
        }

    }

    public boolean checkAccess(User user, int id) throws NotAnOwnerException {
        return db.checkOwner(user, id);
    }

    public void removeById(int id, User user) throws NotAnOwnerException, WrongArgException {
        if (db.checkOwner(user, id)){
            db.removeById(id);
        }
    }

    public void clear(User user) throws WrongArgException {
        db.clear(user);
    }

    public List<Route> getAllRoutes() throws WrongArgException {
        return db.getAllRoutes();
    }

    public void createTables(String file) throws IOException {
        db.createTables(file);
    }

    public User readUserInfo(String login,String password) throws WrongArgException {
        return db.readUserInfo(login, password);
    }
}
