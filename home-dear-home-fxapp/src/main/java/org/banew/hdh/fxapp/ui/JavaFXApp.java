package org.banew.hdh.fxapp.ui;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.banew.hdh.fxapp.SpringBootApp;
import org.banew.hdh.fxapp.ui.views.Wrap;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * JavaFX App
 */
public class JavaFXApp extends Application {

    private static ApplicationContext context;
    private static JavaFXApp INSTANCE;

    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 600;

    @Getter
    private Scene scene;
    private Stage stage;
    private HostServices hostServices;

    @Override
    public void init() {
        INSTANCE = this;
        hostServices = getHostServices();
        context = SpringApplication.run(SpringBootApp.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        Font.loadFont(getClass().getResourceAsStream("/views/assets/styles/Gugi-Regular.ttf"), 60);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        scene = new Scene(resolveSceneRoot("primary", null), MIN_WIDTH, MIN_HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        // narrow-mode listener
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            scene.getRoot().pseudoClassStateChanged(MyStyles.NARROW_MODE, newVal.doubleValue() < 1400);
        });

        stage.setScene(scene);
        maximize();
        stage.show();

        stage.iconifiedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue && stage.isMaximized()) {
                resizeAsOpen(stage);
            }
        });
        ResizeHelper.addResizeListener(stage);
    }

    public void changeScene(String fxml) {
        changeScene(fxml, null);
    }

    public void changeScene(String fxml, Consumer<?> sceneControllerUpdater) {
        Platform.runLater(() -> {
            try {
                scene.setRoot(resolveSceneRoot(fxml, sceneControllerUpdater));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Parent resolveSceneRoot(String fxml, Consumer<?> sceneControllerUpdater) throws IOException {
        var loader = getLoader("wrap");
        Parent node = loader.load();
        Wrap controller = loader.getController();

        var sceneLoader = getLoader(fxml);
        Parent sceneNode = sceneLoader.load();

        if (sceneControllerUpdater != null) {
            sceneControllerUpdater.accept(sceneLoader.getController());
        }

        controller.setContent(sceneNode);
        return node;
    }

    public void minimize() {
        stage.setIconified(true);
        scene.setFill(stage.isMaximized() ? Color.BLACK : Color.TRANSPARENT);
    }

    public boolean maximize() {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            JavaFXApp.resizeAsOpen(stage);
            stage.setMaximized(true);
        }

        scene.setFill(stage.isMaximized() ? Color.BLACK : Color.TRANSPARENT);
        return stage.isMaximized();
    }

    public static JavaFXApp getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Application has not been initialized!");
        }
        else {
            return INSTANCE;
        }
    }

    public Node getControlledNode(String fxml, Consumer<?> sceneControllerUpdater) {
        try {
            var loader = getLoader(fxml);
            Node node = loader.load();
            if (sceneControllerUpdater != null) {
                sceneControllerUpdater.accept(loader.getController());
            }
            return node;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FXMLLoader getLoader(String fxml) {
        var resource = getClass().getResource("/views/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader;
    }

    private static void resizeAsOpen(Stage stage) {
        Platform.runLater(() -> {
            Rectangle2D bounds = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight())
                    .get(0).getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        });
    }

    public String getAppVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        return (version != null) ? version : "Dev Mode";
    }

    public void openWebpage(String url) {
        if (hostServices != null) {
            hostServices.showDocument(url);
        }
    }
}