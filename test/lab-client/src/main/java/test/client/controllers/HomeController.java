package test.client.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import test.client.animations.Shake;
import test.client.util.ClientWorker;
import test.client.util.Command;
import test.client.util.RequestCreator;
import test.client.util.RouteFilter;
import test.common.entities.Route;
import test.common.util.Request;
import test.common.util.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeController extends StackPane {

    private ClientWorker clientWorker;

    private String scene;


    @FXML
    TableView<Route> table;

    @FXML
    TableColumn<Route, Integer> id;
    @FXML
    TableColumn<Route, String> name;
    @FXML
    TableColumn<Route, Long> distance;
    @FXML
    TableColumn<Route, Integer> curr_x;
    @FXML
    TableColumn<Route, Long> curr_y;
    @FXML
    TableColumn<Route, String> from_name;
    @FXML
    TableColumn<Route, Double> from_x;
    @FXML
    TableColumn<Route, Long> from_y;
    @FXML
    TableColumn<Route, String> to_name;
    @FXML
    TableColumn<Route, Double> to_x;
    @FXML
    TableColumn<Route, Long> to_y;
    @FXML
    TableColumn<Route, String> creation_date;
    @FXML
    TableColumn<Route, String> owner;

    ObservableList<Route> items;

    @FXML
    private Label user_label;

    @FXML
    private Tooltip user_tooltip;

    @FXML
    private Label countLabel;

    @FXML
    private TextField distanceField;

    @FXML
    private Button removeButton;

    @FXML
    private Button updateButton;

    @FXML
    private RadioButton filterRadioButton;

    public HomeController(String scene, ClientWorker clientWorker) {

        this.scene = scene;
        this.clientWorker = clientWorker;
    }

    @FXML
    public void initialize() {
        initUserLabel();
        if (scene.equals("table")) {
            initTableView(readCurrentRoutes());
        }
        if (scene.equals("map")) {

        }


    }

    @FXML
    public void filter() {
        if (readCurrentRoutes() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "В данный момент маршрутов нет");
            alert.showAndWait();
        } else {


            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/filter.fxml"));
                FilterController filterController = new FilterController(readCurrentRoutesNames(),
                        readCurrentRoutesDistances(),
                        readCurrentRoutesFroms(), readCurrentRoutesTos());

                fxmlLoader.setControllerFactory(controllerClass -> filterController);
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();
                showFilteredRoutes(filterController.getName(), filterController.getDistance(), filterController.getSign(),
                        filterController.getFrom(), filterController.getTo());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClick() {
        activeButtons();
    }

    @FXML
    void addRoute() {
        add("add");
    }

    @FXML
    void addIfMin() {
        add("add_if_min");
    }

    @FXML
    void add(String type) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/add_route.fxml"));
            fxmlLoader.setControllerFactory(controllerClass -> new AddController(clientWorker, type));

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            initTableView(readCurrentRoutes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeRoute() {
        int id = table.getSelectionModel().getSelectedItem().getId();
        Command command = new Command(clientWorker.getUser(), "remove_by_id", id);
        Request request = RequestCreator.createCommandWithIdRequest(command);
        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            if (response.isSuccessful()) {
                disableButtons();
                initTableView(readCurrentRoutes());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, response.getMessageToResponse());
                alert.showAndWait();
            }
            System.out.println(response.getMessageToResponse());

        }

    }

    @FXML
    void removeHead() {
        Command command = new Command(this.clientWorker.getUser(), "remove_head");
        Request request = RequestCreator.createCommandWithOutArgs(command);

        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            System.out.println(response.getMessageToResponse());

        }
        initTableView(readCurrentRoutes());
    }

    @FXML
    void removeLower() {
        String distance_str = distanceField.getText().trim();
        if (distance_str.isEmpty()) {
            Shake nameShake = new Shake(distanceField);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле длина");
        }

        long distance = Long.parseLong(distance_str);
        if (distance < 1) {
            Shake nameShake = new Shake(distanceField);
            nameShake.playAnim();
            throw new IllegalArgumentException("Длина должна быть больше 1");
        }
        Command command = new Command(this.clientWorker.getUser(), "remove_lower", distance);
        Request request = RequestCreator.createCommandWithDistance(command);

        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            System.out.println(response.getMessageToResponse());

        }
        initTableView(readCurrentRoutes());

    }


    @FXML
    void updateRoute() {
        Route route = table.getSelectionModel().getSelectedItem();

        if (checkAccess(route)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/update_route.fxml"));
                fxmlLoader.setControllerFactory(controllerClass -> new UpdateController(clientWorker, route));

                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();
                initTableView(readCurrentRoutes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(0);
        }
        disableButtons();


    }

    @FXML
    void clearRoutes() {
        Command command = new Command(this.clientWorker.getUser(), "clear");
        Request request = RequestCreator.createCommandWithOutArgs(command);

        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            System.out.println(response.getMessageToResponse());
        }
        initTableView(readCurrentRoutes());


    }

    private boolean checkAccess(Route route) {
        Request request = RequestCreator.createCheckAccessRequest(this.clientWorker.getUser(), route.getId());
        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            System.out.println(response.getMessageToResponse());
            return response.isSuccessful();
        }
        throw new IllegalArgumentException("Ошибка отправления реквеста");

    }

    @FXML
    void goToTable(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/home_table.fxml"));
            fxmlLoader.setControllerFactory(controllerClass -> new HomeController("table", clientWorker));


            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToMap(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/home_map.fxml"));
            fxmlLoader.setControllerFactory(controllerClass -> new HomeController("map", clientWorker));


            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void unselectFilterButton() {
        filterRadioButton.setSelected(false);
        filterRadioButton.setDisable(true);
        initTableView(readCurrentRoutes());
    }

    private List<Route> readCurrentRoutes() {
        Command command = new Command(clientWorker.getUser(), "show");
        Request request = RequestCreator.createCommandWithOutArgs(command);
        clientWorker.sendRequest(request);
        Response response = clientWorker.receiveResponse();
        return response.getCollectionToResponse();
    }

    private Set<String> readCurrentRoutesNames() {
        Set<String> names = new HashSet<>();
        for (Route r : readCurrentRoutes()) {
            names.add(r.getName());
        }
        return names;
    }

    private Set<Long> readCurrentRoutesDistances() {
        Set<Long> distances = new HashSet<>();
        for (Route r : readCurrentRoutes()) {
            distances.add(r.getDistance());
        }
        return distances;
    }

    private Set<String> readCurrentRoutesFroms() {
        Set<String> froms = new HashSet<>();
        for (Route r : readCurrentRoutes()) {
            froms.add(r.getFrom().getName());
        }
        return froms;
    }

    private Set<String> readCurrentRoutesTos() {
        Set<String> tos = new HashSet<>();
        for (Route r : readCurrentRoutes()) {
            tos.add(r.getTo().getName());
        }
        return tos;
    }

    void showFilteredRoutes(String name, Long distance, String sign, String from, String to) {
        List<Route> allRoutes = readCurrentRoutes();
        RouteFilter filter = new RouteFilter(allRoutes, name, distance, sign, from, to);
        List<Route> filteredList = filter.filterRoutes();
        initTableView(filteredList);
        filterRadioButton.setDisable(false);
        filterRadioButton.setSelected(true);

    }


    private void initUserLabel() {
        user_label.setText("Пользователь: " + clientWorker.getUser().getName());

        user_tooltip.setText("Логин: " + clientWorker.getUser().getLogin() + "\n" + "Дата регистрации: " + clientWorker.getUser().getStringRegistrationDate());
    }

    private void initTableView(List<Route> routes) {
        if (routes == null) {
            initCountLabel(0);
        } else {

            items = FXCollections.observableArrayList(routes);
            initCountLabel(items.size());

            table.itemsProperty().setValue(items);


            id.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
            name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
            distance.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getDistance()).asObject());
            curr_x.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getCoordinates().getX()).asObject());
            curr_y.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getCoordinates().getY()).asObject());
            from_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFrom().getName()));
            from_x.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getFrom().getX()).asObject());
            from_y.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getFrom().getY()).asObject());
            to_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTo().getName()));
            to_x.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getTo().getX()).asObject());
            to_y.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getTo().getY()).asObject());
            creation_date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStringOfCreationDate()));
            owner.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOwner()));
        }
    }

    private void initCountLabel(int n) {
        this.countLabel.setText("Количество элементов: " + n);
    }

    private void activeButtons() {
        removeButton.setDisable(false);
        updateButton.setDisable(false);
    }

    private void disableButtons() {
        this.removeButton.setDisable(true);
        updateButton.setDisable(true);
    }


}
