package test.server.db;

import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.server.entities.Person;
import test.server.entities.User;

import java.sql.*;
import java.util.List;

public class ImplDB implements InterfaceDB {
    private String user = "postgres";
    private String password = "347599";
    private String url = "jdbc:postgresql://127.0.0.1:5433/stud";
    private String driver = "org.postgresql.Driver";

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public ImplDB() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        String name = user.getName();
        String login = user.getLogin();
        String password = user.getPassword();
        Date data = Date.valueOf(user.getRegistrationDate().toLocalDate());
        String sql = "INSERT INTO users(name, login, password, reg_date) VALUES(?, ?, ?, ?);";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, name);
            stm.setString(2, login);
            stm.setString(3, password);
            stm.setDate(4, data);
            stm.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new UserAlreadyExistsException("Пользователь с таким логином уже зарегистрирован");
        }

    }

    @Override
    public boolean checkLogin(User user) throws IncorrectUserDataException {

        String login = user.getLogin();
        String password = user.getPassword();

        String sql = "SELECT COUNT(*) FROM users WHERE login = ? AND password = ?;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, login);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            connection.commit();
            if (count != 1) {
                throw new IncorrectUserDataException("Пользователя с такими данными нет");
            }
            return true;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new IncorrectUserDataException("Ошибка при вводе логина или пароля");
        }
    }

    @Override
    public void addPerson(String name, int age) {
        String sql = "INSERT INTO people(name, age) VALUES(?, ?);";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, name);
            stm.setInt(2, age);
            stm.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Person> getAllPeople() {
        return null;
    }

    @Override
    public void deleteAllPeople() {

    }
}
