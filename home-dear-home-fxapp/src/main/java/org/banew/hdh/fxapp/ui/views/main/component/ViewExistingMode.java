package org.banew.hdh.fxapp.ui.views.main.component;

import org.banew.hdh.core.api.layers.services.LocationService;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;

public class ViewExistingMode implements ComponentMode {
    private final LocationComponentDto info;
    private final LocationService locationService;

    public ViewExistingMode(LocationComponentDto info, LocationService locationService) {
        this.info = info;
        this.locationService = locationService;
    }

    @Override
    public void init(ComponentInfo ui, String locationId) {
        ui.resetUI();
        ui.getLockOutside().accept(false);

        ui.getComponentNameBox().setVisible(true);
        ui.getComponentNameBox().setManaged(true);
        ui.getComponentName().setText(info.name());
        ui.getComponentClass().setText(info.fullClassName());

        if (info.classAttributes() != null) {
            ui.getComponentDescription().setText(info.classAttributes().description());
            ui.refreshArguments(info.classAttributes(), info.properties(), false);
        } else {
            ui.hideDynamicAreas();
        }

        ui.setupButtons(true, false); // Показуємо "Олівець", ховаємо "Зберегти"
    }

    @Override
    public void onEditClicked(ComponentInfo ui) {
        // ПЕРЕХІД СТАНУ: Перемикаємось на редагування
        ui.setMode(new EditExistingMode(info, locationService));
    }
}