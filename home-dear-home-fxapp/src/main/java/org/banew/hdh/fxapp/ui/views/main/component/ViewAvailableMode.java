package org.banew.hdh.fxapp.ui.views.main.component;

import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import java.util.Map;

public class ViewAvailableMode implements ComponentMode {
    private final AvailableComponent available;

    public ViewAvailableMode(AvailableComponent available) {
        this.available = available;
    }

    @Override
    public void init(ComponentInfo ui, String locationId) {
        ui.resetUI();
        ui.getLockOutside().accept(false); // Знімаємо блокування ззовні

        ui.getComponentNameBox().setVisible(false);
        ui.getComponentNameBox().setManaged(false);
        ui.getComponentClass().setText(available.fullClassName());

        if (available.attributes() != null) {
            ui.getComponentDescription().setText(available.attributes().description());
            // Передаємо порожню мапу, бо це лише шаблон, і false (не редагується)
            ui.refreshArguments(available.attributes(), Map.of(), false);
        } else {
            ui.getComponentName().setText("--NO INFO--");
            ui.hideDynamicAreas();
        }

        ui.setupButtons(false, false); // Жодних кнопок "Зберегти" чи "Редагувати"
    }
}