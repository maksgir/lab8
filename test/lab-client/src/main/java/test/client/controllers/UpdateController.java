package test.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import test.client.animations.Shake;
import test.client.util.ClientSocketWorker;
import test.client.util.ClientWorker;
import test.client.util.Command;
import test.client.util.RequestCreator;
import test.common.entities.Coordinates;
import test.common.entities.Location;
import test.common.entities.Route;
import test.common.entities.User;
import test.common.util.Request;
import test.common.util.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class UpdateController {
    private ClientWorker clientWorker;
    private Route route;

    public UpdateController(ClientWorker clientWorker, Route route) {
        this.clientWorker = clientWorker;
        this.route = route;
    }

    @FXML
    private TextField name_field;

    @FXML
    private TextField curr_x;

    @FXML
    private TextField curr_y;

    @FXML
    private TextField distance_field;

    @FXML
    private Button updateButton;

    @FXML
    private TextField from_name;

    @FXML
    private TextField from_x;

    @FXML
    private TextField from_y;

    @FXML
    private TextField to_name;

    @FXML
    private TextField to_x;

    @FXML
    private TextField to_y;

    @FXML
    public void initialize() {
        setFields();
    }

    @FXML
    void updateRoute() {
        Route generatedRoute = generateRoute();
        Command command = new Command(this.clientWorker.getUser(), "update", this.route.getId(), generatedRoute);
        Request request = RequestCreator.createCommandWithIdAndRouteRequest(command);

        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            System.out.println(response.getMessageToResponse());
            updateButton.getScene().getWindow().hide();
        }
    }

    private void setFields() {
        name_field.setText(this.route.getName());
        distance_field.setText(this.route.getDistance() + "");
        curr_x.setText(this.route.getCoordinates().getX() + "");
        curr_y.setText(this.route.getCoordinates().getY() + "");
        from_name.setText(this.route.getFrom().getName());
        from_x.setText(this.route.getFrom().getX() + "");
        from_y.setText(this.route.getFrom().getY() + "");
        to_name.setText(this.route.getTo().getName());
        to_x.setText(this.route.getTo().getX() + "");
        to_y.setText(this.route.getTo().getY() + "");
    }


    private Route generateRoute() {
        try {
            String name = getRouteName();
            long distance = getDistance();
            Coordinates coordinates = generateCoordinates();

            Location from = generateLocationFrom();

            Location to = generateLocationTo();

            System.out.println(name);
            System.out.println(distance);
            System.out.println(coordinates);
            System.out.println(from);

            Route route = new Route();
            route.setCreationDate();
            route.setName(name);
            route.setDistance(distance);
            route.setFrom(from);
            route.setTo(to);
            route.setCoordinates(coordinates);
            route.setOwner(this.clientWorker.getUser().getLogin());
            return route;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());

        }
    }

    private Location generateLocationTo() {
        String name = getLocationToName();
        double x = getToX();
        long y = getToY();
        return new Location(x, y, name);
    }


    private Location generateLocationFrom() {
        String name = getLocationFromName();
        double x = getFromX();
        long y = getFromY();
        return new Location(x, y, name);

    }

    private long getFromY() {
        String from_y_str = from_y.getText().trim();
        if (from_y_str.isEmpty()) {
            Shake nameShake = new Shake(from_y);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле Y локации отправления");
        }

        long fromY = Long.parseLong(from_y_str);
        if (fromY < 0 || fromY > 1000) {
            Shake nameShake = new Shake(from_y);
            nameShake.playAnim();
            throw new IllegalArgumentException("Поле X вне допустимого значения ");
        }
        return fromY;
    }

    private double getFromX() {
        String from_x_str = from_x.getText().trim();
        if (from_x_str.isEmpty()) {
            Shake nameShake = new Shake(from_x);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле X локации отправления");
        }
        double fromX = Double.parseDouble(from_x_str);
        if (fromX < 0 || fromX > 1000) {
            Shake nameShake = new Shake(from_x);
            nameShake.playAnim();
            throw new IllegalArgumentException("Поле X вне допустимого значения ");
        }
        return fromX;
    }

    private String getLocationFromName() {
        String from_name_str = from_name.getText().trim();
        if (from_name_str.isEmpty()) {
            Shake nameShake = new Shake(from_name);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле название локации отправления");
        }
        return from_name_str;
    }

    private String getLocationToName() {
        String to_name_str = to_name.getText().trim();
        if (to_name_str.isEmpty()) {
            Shake nameShake = new Shake(to_name);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле название локации отправления");
        }
        return to_name_str;
    }

    private double getToX() {
        String to_x_str = to_x.getText().trim();
        if (to_x_str.isEmpty()) {
            Shake nameShake = new Shake(to_x);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле X локации отправления");
        }
        double toX = Double.parseDouble(to_x_str);
        if (toX < 0 || toX > 1000) {
            Shake nameShake = new Shake(to_x);
            nameShake.playAnim();
            throw new IllegalArgumentException("Поле X вне допустимого значения ");
        }
        return toX;
    }

    private long getToY() {
        String to_y_str = to_y.getText().trim();
        if (to_y_str.isEmpty()) {
            Shake nameShake = new Shake(to_y);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле Y локации отправления");
        }

        long toY = Long.parseLong(to_y_str);
        if (toY < 0 || toY > 1000) {
            Shake nameShake = new Shake(to_y);
            nameShake.playAnim();
            throw new IllegalArgumentException("Поле X вне допустимого значения ");
        }
        return toY;
    }


    private Coordinates generateCoordinates() {
        int x = getCurrX();
        int y = getCurrY();
        return new Coordinates(x, y);
    }

    private int getCurrX() {
        String curr_x_str = curr_x.getText().trim();
        if (curr_x_str.isEmpty()) {
            Shake nameShake = new Shake(curr_x);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле текущего Х");
        }
        int currX = Integer.parseInt(curr_x_str);
        if (currX < 0 || currX > 1000) {
            Shake nameShake = new Shake(curr_x);
            nameShake.playAnim();
            throw new IllegalArgumentException("Поле Х вне допустимого значения");
        }
        return currX;
    }

    private int getCurrY() {
        String curr_y_str = curr_y.getText().trim();
        if (curr_y_str.isEmpty()) {
            Shake nameShake = new Shake(curr_y);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле текущего Y");
        }
        int currY = Integer.parseInt(curr_y_str);
        if (currY < 0 || currY > 1000) {
            Shake nameShake = new Shake(curr_y);
            nameShake.playAnim();
            throw new IllegalArgumentException("Поле Y вне допустимого значения");
        }
        return currY;
    }

    private String getRouteName() {
        String name = name_field.getText().trim();
        if (name.isEmpty()) {
            Shake nameShake = new Shake(name_field);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле имя");
        }
        return name;
    }

    private long getDistance() {
        String distanceStr = distance_field.getText().trim();
        if (distanceStr.isEmpty()) {
            Shake nameShake = new Shake(distance_field);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле длина");
        }

        long distance = Long.parseLong(distanceStr);
        if (distance < 1) {
            Shake nameShake = new Shake(distance_field);
            nameShake.playAnim();
            throw new IllegalArgumentException("Длина должна быть больше 1");
        }
        return distance;
    }


}
