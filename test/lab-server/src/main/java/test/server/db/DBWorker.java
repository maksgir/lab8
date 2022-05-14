package test.server.db;

import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.server.entities.User;

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
}
