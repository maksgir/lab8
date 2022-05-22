package test.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import test.client.Application;
import test.client.util.ClientSocketWorker;
import test.common.entities.User;
import test.common.util.Request;
import test.common.util.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class RegistrationController {

    private final ClientSocketWorker clientSocketWorker;

    public RegistrationController(ClientSocketWorker clientSocketWorker) {
        this.clientSocketWorker = clientSocketWorker;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button SingUpButton;

    @FXML
    private TextField login_filed;

    @FXML
    private TextField name_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private PasswordField password_field1;

    @FXML
    void initialize() {

    }

    @FXML
    void signUp() {
        try {
            String name = name_field.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Заполните поле имя");
            }
            String login = login_filed.getText().trim();
            if (login.isEmpty()) {
                throw new IllegalArgumentException("Заполните поле логин");
            }
            String password1 = password_field.getText().trim();
            String password2 = password_field1.getText().trim();
            if (!password1.equals(password2)) {
                throw new IllegalArgumentException("Введенные пароли не совпадают");
            } else if (password1.isEmpty()) {
                throw new IllegalArgumentException("Заполните поле пароль");
            }

            System.out.println(name + " " + login + " " + password1);

            ZonedDateTime zdt = ZonedDateTime.now();

            Request request = new Request("registration", new User(name, login, password1, zdt));

            if (sendRequest(request)) {
                Response response = receiveResponse();
                if (!response.isSuccessful()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.getMessageToResponse());
                    alert.showAndWait();
                    openWelcome();
                } else {
                    openHome();
                }
            }
        } catch (IllegalArgumentException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }



    void openWelcome() throws IOException {


        SingUpButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/welcome.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new WelcomeController(clientSocketWorker));


        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    void openHome() throws IOException {
        SingUpButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/home.fxml"));


        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private boolean sendRequest(Request request) {
        if (request != null) {
            try {
                clientSocketWorker.sendRequest(request);
                return true;
            } catch (IOException e) {
                throw new IllegalArgumentException("Возникла ошибка сериализации данных");

            }
        } else {
            return false;
        }
    }

    private Response receiveResponse() {
        try {
            Response response = clientSocketWorker.receiveResponse();
            return response;
        } catch (SocketTimeoutException e) {
            throw new IllegalArgumentException("Время ожидания отклика от сервера превышено, попробуйте позже");
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка получения ответа от сервера");

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Ответ от сервера пришел поврежденный");

        }

    }


}
