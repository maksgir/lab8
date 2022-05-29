package test.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import test.client.controllers.WelcomeController;
import test.client.util.ClientSocketWorker;
import test.client.util.ClientWorker;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        ClientWorker clientWorker = new ClientWorker();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/samples/welcome.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new WelcomeController(clientWorker));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Welcome Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
