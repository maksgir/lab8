package test.server.db;

import test.common.entities.Coordinates;
import test.common.entities.Location;
import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
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
    public boolean checkOwner(User user, int routeId) throws NotAnOwnerException {
        String login = user.getLogin();

        String sql = "SELECT * FROM routes WHERE owner IN (SELECT id FROM users WHERE login = ?);";
        boolean flag = false;

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, login);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                if (id == routeId) {
                    flag = true;
                    break;
                }
            }

            connection.commit();
            if (!flag) {
                throw new NotAnOwnerException("");
            }
            return flag;

        } catch (SQLException | NotAnOwnerException throwables) {
            System.out.println(throwables.getMessage());
            throw new NotAnOwnerException("Вы не являетесь владельцем данного маршрута");
        }

    }

    @Override
    public int addRoute(Route route, User user) throws AddRouteToDbException {
        try (Connection connection = getConnection()) {
            String name = route.getName();
            int coord = createCoordinates(route.getCoordinates());
            Date data = Date.valueOf(route.getCreationDate().toLocalDate());
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
            return findMaxRouteId();
        } catch (SQLException | WrongArgException throwables) {
            System.out.println(throwables.getMessage());
            throw new AddRouteToDbException("Ошибка добавления маршрута в БД");
        }


    }

    @Override
    public void updateById(Route route, int id) throws WrongArgException {
        Coordinates coordinates = route.getCoordinates();
        Location from = route.getFrom();
        Location to = route.getTo();
        String name = route.getName();
        long distance = route.getDistance();
        Date date = Date.valueOf(route.getCreationDate().toLocalDate());

        String sql = "SELECT * FROM routes WHERE id = ?;";

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            rs.next();

            int coordId = rs.getInt("coordinates");
            updateCoordinatesById(coordinates, coordId);

            int fromId = rs.getInt("locationFrom");
            updateLocationsById(from, fromId);

            int toId = rs.getInt("locationTo");
            updateLocationsById(to, toId);
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка обновления маршрута по ID");
        }

        String sql1 = "UPDATE routes SET name = ?, creationDate = ?, distance = ? WHERE id = ?;";

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql1);
            stm.setString(1, name);
            stm.setDate(2, date);
            stm.setLong(3, distance);
            stm.setInt(4, id);

            stm.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка обновления маршрута по ID");
        }


    }

    @Override
    public void removeById(int id) throws WrongArgException {

        String sql = "SELECT * FROM routes WHERE id = ?;";

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            rs.next();

            int coordId = rs.getInt("coordinates");
            deleteCoordinate(coordId);

            int fromId = rs.getInt("locationFrom");
            deleteLocation(fromId);

            int toId = rs.getInt("locationTo");
            deleteLocation(toId);
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка обновления маршрута по ID");
        }

        String sql1 = "DELETE FROM routes WHERE id = ?;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql1);
            stm.setInt(1, id);

            stm.executeUpdate();
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка удаления маршрута по ID");
        }
    }

    @Override
    public void clear(User user) throws WrongArgException {
        int owner = findUser(user.getLogin());

        String sql = "SELECT id FROM routes WHERE owner = ?;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, owner);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                removeById(id);

            }
            connection.commit();
        } catch (SQLException | WrongArgException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка удаления маршрутов");
        }
    }

    @Override
    public List<Route> getAllRoutes() throws WrongArgException {
        String sql = "SELECT * FROM routes;";
        List<Route> routes = new ArrayList<>();

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Route route = new Route();

                String name = rs.getString("name");
                Date data = rs.getDate("creationDate");
                long distance = rs.getLong("distance");
                int id = rs.getInt("id");

                int coordId = rs.getInt("coordinates");
                int fromId = rs.getInt("locationFrom");
                int toId = rs.getInt("locationTo");
                int ownerId = rs.getInt("owner");

                Coordinates coordinates = getCoordinates(coordId);
                Location from = getLocation(fromId);
                Location to = getLocation(toId);
                String owner = getOwner(ownerId);

                route.setId(id);
                route.setName(name);
                route.setCreationDate(data.toLocalDate().atStartOfDay(ZoneId.systemDefault()));
                route.setDistance(distance);
                route.setCoordinates(coordinates);
                route.setFrom(from);
                route.setTo(to);
                route.setOwner(owner);
                routes.add(route);

                System.out.println("Прочитали из БД маршрут\n" + route + "\n\n\n");

            }
            connection.commit();
        } catch (SQLException | WrongArgException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка чтения маршрута с БД");
        }
        return routes;

    }

    private void updateCoordinatesById(Coordinates coordinates, int id) throws WrongArgException {
        int x = coordinates.getX();
        long y = coordinates.getY();

        String sql = "UPDATE coordinates SET x = ?, y = ? WHERE id = ?;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, x);
            stm.setLong(2, y);
            stm.setInt(3, id);


            stm.executeUpdate();
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка обновления значения координаты по ID");
        }
    }

    private void updateLocationsById(Location location, int id) throws WrongArgException {
        double x = location.getX();
        long y = location.getY();
        String name = location.getName();

        String sql = "UPDATE locations SET x = ?, y = ?, name = ? WHERE id = ?;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setDouble(1, x);
            stm.setLong(2, y);
            stm.setString(3, name);
            stm.setInt(4, id);


            stm.executeUpdate();
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка обновления значения локации по ID");
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

    private Coordinates getCoordinates(int id) throws WrongArgException {
        String sql = "SELECT * FROM coordinates WHERE id = ?;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int x = rs.getInt("x");
            long y = rs.getLong("y");
            connection.commit();
            return new Coordinates(x, y);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в поиске координаты по заданному ID");
        }

    }

    private Location    getLocation(int id) throws WrongArgException {
        String sql = "SELECT * FROM locations WHERE id = ?;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            rs.next();
            double x = rs.getDouble("x");
            long y = rs.getLong("y");
            String name = rs.getString("name");
            connection.commit();
            return new Location(x, y, name);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в поиске локации по заданному ID");
        }
    }

    private String getOwner(int id) throws WrongArgException {
        String sql = "SELECT * FROM users WHERE id = ?;";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            rs.next();

            String login = rs.getString("login");
            connection.commit();
            return login;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в логина пользователя по заданному ID");
        }
    }

    private void deleteCoordinate(int id) throws WrongArgException {
        String sql1 = "DELETE FROM coordinates WHERE id = ?;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql1);
            stm.setInt(1, id);

            stm.executeUpdate();
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка удаления координаты по ID");
        }
    }

    private void deleteLocation(int id) throws WrongArgException {
        String sql1 = "DELETE FROM locations WHERE id = ?;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql1);
            stm.setInt(1, id);

            stm.executeUpdate();
            connection.commit();

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка удаления локации по ID");
        }
    }

    private int findMaxRouteId() throws WrongArgException {
        String sql = "SELECT MAX(id) FROM routes;";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.next();

            int id = rs.getInt(1);
            connection.commit();
            return id;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throw new WrongArgException("Ошибка в поиске ID маршрута");
        }
    }

    @Override
    public void createTables(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        while (reader.ready()) {
            builder.append(reader.readLine());
        }
        String sql = builder.toString();

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.executeQuery();
            connection.commit();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }


}
