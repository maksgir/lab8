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
import test.client.util.ClientSocketWorker;
import test.common.entities.User;
import test.common.util.Request;
import test.common.util.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController {

    private ClientSocketWorker clientSocketWorker;

    public WelcomeController(ClientSocketWorker cSW) {
        this.clientSocketWorker = cSW;
    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginButton;

    @FXML
    private TextField login_filed;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button registrationButton;

    @FXML
    void initialize() {

    }

    @FXML
    void signIn() {
        try {
            String login = login_filed.getText().trim();
            if (login.isEmpty()) {
                throw new IllegalArgumentException("Заполните поле логин");
            }
            String password = password_field.getText().trim();
            if (password.isEmpty()) {
                throw new IllegalArgumentException("Заполните поле пароль");
            }

            Request request = new Request("login", new User(login, password));

            if (sendRequest(request)) {
                Response response = receiveResponse();
                if (!response.isSuccessful()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.getMessageToResponse());
                    alert.showAndWait();
                } else {
                    openHome();
                }
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            //System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    @FXML
    void signUp() {
        try {
            registrationButton.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/registration.fxml"));
            fxmlLoader.setControllerFactory(controllerClass -> new RegistrationController(clientSocketWorker));

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();


        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при переходе к регистрации");
            alert.showAndWait();
        }

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

    void openHome() throws IOException {
        loginButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/home.fxml"));


        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


}
