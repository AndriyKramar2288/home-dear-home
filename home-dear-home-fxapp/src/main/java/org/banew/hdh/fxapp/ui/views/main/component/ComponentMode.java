package org.banew.hdh.fxapp.ui.views.main.component;

public interface ComponentMode {
    // Викликається при старті режиму
    void init(ComponentInfo ui, String locationId);

    // Реакції на кнопки (за замовчуванням нічого не роблять, щоб не дублювати код)
    default void onEditClicked(ComponentInfo ui) {}
    default void onSaveClicked(ComponentInfo ui) {}
    default void onCancelClicked(ComponentInfo ui) {}
    default void onToggleChanged(ComponentInfo ui) {
        ui.renderRadioArguments(); // Оновлює аргументи при перемиканні радіокнопок
    }
}
