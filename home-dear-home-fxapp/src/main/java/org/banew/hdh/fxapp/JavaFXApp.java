package org.banew.hdh.fxapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * JavaFX App
 */
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
        Platform.setImplicitExit(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        scene = new Scene(loadFXML("primary"), 640, 480);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();

        stage.iconifiedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue && stage.isMaximized()) {
                resizeAsOpen(stage);
            }
        });
        ResizeHelper.addResizeListener(stage);
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

        var resource = getClass().getClassLoader().getResource("views/" + fxml + ".fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader.load();
    }

    public static void resizeAsOpen(Stage stage) {
        Platform.runLater(() -> {
            Rectangle2D bounds = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight())
                    .get(0).getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        });
    }
}