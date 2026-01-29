package org.banew.hdh.fxapp;
import javafx.css.PseudoClass;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeHelper {
    private static double x, y;
    private static final int BORDER = 8; // Ширина зони ресайзу

    public static void addResizeListener(Stage stage) {
        Scene scene = stage.getScene();

        scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {

            if (stage.isMaximized()) return;

            double mouseX = e.getX();
            double mouseY = e.getY();
            double width = stage.getWidth();
            double height = stage.getHeight();

            Cursor cursor = Cursor.DEFAULT;
            boolean left = mouseX < BORDER;
            boolean right = mouseX > width - BORDER;
            boolean top = mouseY < BORDER;
            boolean bottom = mouseY > height - BORDER;

            if (left && top) cursor = Cursor.NW_RESIZE;
            else if (left && bottom) cursor = Cursor.SW_RESIZE;
            else if (right && top) cursor = Cursor.NE_RESIZE;
            else if (right && bottom) cursor = Cursor.SE_RESIZE;
            else if (left) cursor = Cursor.W_RESIZE;
            else if (right) cursor = Cursor.E_RESIZE;
            else if (top) cursor = Cursor.N_RESIZE;
            else if (bottom) cursor = Cursor.S_RESIZE;

            scene.setCursor(cursor);
        });

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            x = e.getScreenX();
            y = e.getScreenY();
        });

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {

            if (scene.getCursor() == Cursor.DEFAULT || stage.isMaximized()) return;

            double deltaX = e.getScreenX() - x;
            double deltaY = e.getScreenY() - y;
            Cursor c = scene.getCursor();

            if (c == Cursor.E_RESIZE || c == Cursor.NE_RESIZE || c == Cursor.SE_RESIZE) {
                stage.setWidth(Math.max(stage.getMinWidth(), stage.getWidth() + deltaX));
            } else if (c == Cursor.W_RESIZE || c == Cursor.NW_RESIZE || c == Cursor.SW_RESIZE) {
                double oldW = stage.getWidth();
                stage.setWidth(Math.max(stage.getMinWidth(), stage.getWidth() - deltaX));
                if (stage.getWidth() != oldW) stage.setX(stage.getX() + deltaX);
            }

            if (c == Cursor.S_RESIZE || c == Cursor.SE_RESIZE || c == Cursor.SW_RESIZE) {
                stage.setHeight(Math.max(stage.getMinHeight(), stage.getHeight() + deltaY));
            } else if (c == Cursor.N_RESIZE || c == Cursor.NW_RESIZE || c == Cursor.NE_RESIZE) {
                double oldH = stage.getHeight();
                stage.setHeight(Math.max(stage.getMinHeight(), stage.getHeight() - deltaY));
                if (stage.getHeight() != oldH) stage.setY(stage.getY() + deltaY);
            }

            x = e.getScreenX();
            y = e.getScreenY();
        });
    }
}