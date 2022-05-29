package test.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import test.client.animations.Shake;
import test.client.util.ClientSocketWorker;
import test.client.util.ClientWorker;
import test.client.util.RequestCreator;
import test.common.entities.User;
import test.common.util.Request;
import test.common.util.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class WelcomeController {
    private ClientWorker clientWorker;

    private User user;



    public WelcomeController(ClientWorker clientWorker) {
        this.clientWorker = clientWorker;
    }

    @FXML
    private Button loginButton;


    @FXML
    private TextField login_filed;

    @FXML
    private Tooltip loginTooltip;

    @FXML
    private PasswordField password_field;

    @FXML
    private Tooltip passwordTooltip;

    @FXML
    private Button registrationButton;


    @FXML
    void initialize() {

    }

    @FXML
    void signIn() {
        try {
            this.user = readUser();
            Request request = RequestCreator.createLoginRequest(this.user);

            if (clientWorker.sendRequest(request)) {
                Response response = clientWorker.receiveResponse();
                if (!response.isSuccessful()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.getMessageToResponse());
                    alert.showAndWait();
                } else {
                    completeUser();
                    clientWorker.setUser(this.user);
                    openHome();
                }
            }
        } catch (IOException e) {
            //Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            //alert.showAndWait();
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void signUp() {
        try {
            registrationButton.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/registration.fxml"));
            fxmlLoader.setControllerFactory(controllerClass -> new RegistrationController(clientWorker));

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();


        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при переходе к регистрации");
            alert.showAndWait();
        }

    }

    private User readUser(){
        String login = login_filed.getText().trim();
        if (login.isEmpty()) {
            Shake loginShake = new Shake(login_filed);
            loginShake.playAnim();

            throw new IllegalArgumentException("Заполните поле логин");
        }
        String password = password_field.getText().trim();
        if (password.isEmpty()) {
            Shake passwordShake = new Shake(password_field);
            passwordShake.playAnim();
            throw new IllegalArgumentException("Заполните поле пароль");
        }
        return new User(login, password);
    }


    void openHome() throws IOException  {
        loginButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/home_table.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new HomeController("table", clientWorker));


        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void completeUser() {
        Request request = new Request("userInfo", user);
        if (clientWorker.sendRequest(request)) {
            Response response = clientWorker.receiveResponse();
            this.user = response.getUser();
            System.out.println(user);
        }

    }


}
