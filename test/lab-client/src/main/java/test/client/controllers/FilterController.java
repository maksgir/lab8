package test.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Set;

public class FilterController extends VBox {

    @FXML
    private ChoiceBox<Long> distanceChoice;

    @FXML
    private Button filterButton;

    @FXML
    private ChoiceBox<String> fromChoice;

    @FXML
    private ChoiceBox<String> nameChoice;

    @FXML
    private ChoiceBox<String> signChoice;

    @FXML
    private ChoiceBox<String> toChoice;

    private Set<String> names;

    private Set<Long> distances;

    private Set<String> froms;

    private Set<String> tos;

    private String[] signs = {">", "<", "=", ">=", "<="};

    private String name;
    private String sign;
    private Long distance;
    private String from;
    private String to;

    public FilterController(Set<String> names, Set<Long> distances, Set<String> froms, Set<String> tos) {
        this.names = names;
        this.distances = distances;
        this.froms = froms;
        this.tos = tos;


    }

    @FXML
    void initialize() {
        initSigns();
        initNames();
        initDistance();
        initFroms();
        initTos();
    }


    @FXML
    void returnFilterValues(ActionEvent event) {
        this.name = this.nameChoice.getValue();
        this.sign = this.signChoice.getValue();
        this.distance = this.distanceChoice.getValue();
        this.from = this.fromChoice.getValue();
        this.to = this.toChoice.getValue();
        filterButton.getScene().getWindow().hide();
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    public Long getDistance() {
        return distance;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    private void initSigns() {
        this.signChoice.getItems().addAll(this.signs);
    }

    private void initNames() {
        this.nameChoice.getItems().addAll(this.names);
    }

    private void initDistance() {
        this.distanceChoice.getItems().addAll(this.distances);
    }

    private void initFroms() {
        this.fromChoice.getItems().addAll(this.froms);
    }

    private void initTos() {
        this.toChoice.getItems().addAll(this.tos);
    }

}
