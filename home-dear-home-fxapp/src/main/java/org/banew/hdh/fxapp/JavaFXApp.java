package org.banew.hdh.fxapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * JavaFX App
 */
@SpringBootApplication
public class JavaFXApp extends Application {

    private static ApplicationContext context;
    private static JavaFXApp INSTANCE;

    private Scene scene;

    @Override
    public void init() throws Exception {
        INSTANCE = this;
        context = SpringApplication.run(SpringBootApp.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) {
        Platform.runLater(() -> {
            try {
                scene.setRoot(loadFXML(fxml));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static JavaFXApp getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Application has not been initialized!");
        }
        else {
            return INSTANCE;
        }
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader.load();
    }
}