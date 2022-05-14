package test.server.db;

import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.server.entities.Person;
import test.server.entities.User;

import java.util.List;

public interface InterfaceDB {
    void addUser(User user) throws UserAlreadyExistsException;
    boolean checkLogin(User user) throws IncorrectUserDataException;
    void addPerson(String name, int age);
    List<Person> getAllPeople();
    void deleteAllPeople();
}
