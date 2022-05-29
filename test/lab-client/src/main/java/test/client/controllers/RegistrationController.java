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
import java.time.ZonedDateTime;

public class RegistrationController {
    private ClientWorker clientWorker;

    private User user;

    public RegistrationController(ClientWorker clientWorker) {
        this.clientWorker = clientWorker;
    }


    @FXML
    private Button signUpButton;

    @FXML
    private TextField login_filed;

    @FXML
    private Tooltip login_tooltip;

    @FXML
    private TextField name_field;

    @FXML
    private Tooltip name_tooltip;

    @FXML
    private PasswordField password_field;

    @FXML
    private Tooltip password_tooltip;

    @FXML
    private PasswordField password_field1;

    @FXML
    private Tooltip password1_tooltip;

    @FXML
    void initialize() {

    }

    @FXML
    void signUp() {
        try {
            this.user = readUserData();

            Request request = RequestCreator.createRegistrationRequest(this.user);

            if (clientWorker.sendRequest(request)) {
                Response response = clientWorker.receiveResponse();
                if (!response.isSuccessful()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.getMessageToResponse());
                    alert.showAndWait();
                    openWelcome();
                } else {
                    clientWorker.setUser(user);
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


        signUpButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/welcome.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new WelcomeController(clientWorker));


        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    void openHome() throws IOException {
        signUpButton.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/samples/home_table.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new HomeController("table", clientWorker));

        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private User readUserData(){
        String name = name_field.getText().trim();
        if (name.isEmpty()) {
            Shake nameShake = new Shake(name_field);
            nameShake.playAnim();
            throw new IllegalArgumentException("Заполните поле имя");
        }
        String login = login_filed.getText().trim();
        if (login.isEmpty()) {
            Shake loginShake = new Shake(login_filed);
            loginShake.playAnim();
            throw new IllegalArgumentException("Заполните поле логин");
        }
        String password1 = password_field.getText().trim();
        String password2 = password_field1.getText().trim();
        if (!password1.equals(password2)) {
            Shake passwordShake = new Shake(password_field);
            Shake password1Shake = new Shake(password_field1);
            passwordShake.playAnim();
            password1Shake.playAnim();
            throw new IllegalArgumentException("Введенные пароли не совпадают");
        } else if (password1.isEmpty()) {
            Shake passwordShake = new Shake(password_field);
            passwordShake.playAnim();
            throw new IllegalArgumentException("Заполните поле пароль");
        }

        System.out.println(name + " " + login + " " + password1);

        ZonedDateTime zdt = ZonedDateTime.now();

        return new User(name, login, password1, zdt);
    }




}
