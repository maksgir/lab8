package test.server.db;

import test.common.entities.Coordinates;
import test.common.entities.Location;
import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.AddRouteToDbException;
import test.common.exceptions.IncorrectUserDataException;
import test.common.exceptions.UserAlreadyExistsException;
import test.common.exceptions.WrongArgException;

import java.sql.*;
import java.time.ZonedDateTime;

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
    public void addRoute(Route route, User user) throws AddRouteToDbException {
        try (Connection connection = getConnection()) {
            String name = route.getName();
            int coord = createCoordinates(route.getCoordinates());
            Date data = Date.valueOf(ZonedDateTime.now().toLocalDate());
            int from = createLocation(route.getFrom());
            int to = createLocation(route.getTo());
            long distance = route.getDistance();
            int owner = findUser(user.getLogin());
            String sql = "INSERT INTO routes(name, coordinates, creationDate, locationFrom," +
                    " locationTo, distance, owner) VALUES(?, ?, ?, ?, ?, ?, ?);";
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, name);
            stm.setInt(2, coord);
            stm.setDate(3, data);
            stm.setInt(4, from);
            stm.setInt(5, to);
            stm.setLong(6, distance);
            stm.setInt(7, owner);

            stm.executeUpdate();
            connection.commit();
        } catch (SQLException | WrongArgException throwables) {
            System.out.println(throwables.getMessage());
            throw new AddRouteToDbException("Ошибка добавления маршрута в БД");
        }



    }

    private int createCoordinates(Coordinates coordinates) throws WrongArgException {
        int x = coordinates.getX();
        long y = coordinates.getY();
        String sql = "INSERT INTO coordinates(x, y) VALUES(?, ?);";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, x);
            stm.setLong(2, y);
            stm.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в создании координат");
        }

        int id = 0;

        String sql1 = "SELECT MAX(id) FROM coordinates;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql1);
            if (rs.next()) {
                id = rs.getInt(1);
            }
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в поиске ID координаты");
        }
        return id;


    }

    private int createLocation(Location location) throws WrongArgException {

        double x = location.getX();
        long y = location.getY();
        String name = location.getName();

        String sql = "INSERT INTO locations(x, y, name) VALUES(?, ?, ?);";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setDouble(1, x);
            stm.setLong(2, y);
            stm.setString(3, name);
            stm.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в создании локации");
        }

        int id = 0;

        String sql1 = "SELECT MAX(id) FROM locations;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql1);
            if (rs.next()) {
                id = rs.getInt(1);
            }
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в поиске ID координаты");
        }
        return id;
    }

    private int findUser(String login) throws WrongArgException {
        String sql = "SELECT id FROM users WHERE login = ?;";
        int id;
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, login);
            ResultSet rs = stm.executeQuery();
            rs.next();
            id = rs.getInt(1);
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в поиске ID владельца маршрута");
        }
        return id;
    }


}
