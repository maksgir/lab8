package test.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import test.client.controllers.RegistrationController;
import test.client.controllers.WelcomeController;
import test.client.util.ClientSocketWorker;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private ClientSocketWorker clientSocketWorker;


    @Override
    public void start(Stage stage) throws IOException {
        clientSocketWorker = new ClientSocketWorker();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/samples/welcome.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new WelcomeController(clientSocketWorker));

        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Welcome Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
