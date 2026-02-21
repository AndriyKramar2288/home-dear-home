package org.banew.hdh.fxapp.ui.views.main.component;

import org.banew.hdh.core.api.layers.services.LocationService;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;

import java.util.HashMap;
import java.util.Map;

public class EditExistingMode implements ComponentMode {
    private final LocationComponentDto originalInfo;
    private final Map<String, String> editingProperties;
    private final LocationService locationService;
    private String locationId;

    public EditExistingMode(LocationComponentDto info, LocationService locationService) {
        this.originalInfo = info;
        // Робимо робочу копію властивостей для редагування
        this.editingProperties = new HashMap<>(info.properties());
        this.locationService = locationService;
    }

    @Override
    public void init(ComponentInfo ui, String locationId) {
        this.locationId = locationId;

        ui.getLockOutside().accept(true); // БЛОКУЄМО КЛІКИ ЗОВНІ!

        ui.getComponentName().setText(originalInfo.name());
        ui.getComponentClass().setText(originalInfo.fullClassName());

        if (originalInfo.classAttributes() != null) {
            ui.getComponentDescription().setText(originalInfo.classAttributes().description());
            // Передаємо нашу КОПІЮ мапи і true (дозволяємо редагування)
            ui.refreshArguments(originalInfo.classAttributes(), editingProperties, true);
        }

        ui.setupButtons(false, true); // Ховаємо "Олівець", показуємо "Зберегти" і "Скасувати"
    }

    @Override
    public void onSaveClicked(ComponentInfo ui) {
        // ТУТ БУДЕ ТВОЯ ЛОГІКА ЗБЕРЕЖЕННЯ (виклик сервісу і т.д.)
        System.out.println("Збережено властивості: " + editingProperties);

        locationService.getLocationById(locationId).ifPresent(location -> {
            var newComponentsList = location.components().stream()
                    .map(dto -> {
                        if (dto.id().equals(originalInfo.id())) {
                            return new LocationComponentDto(originalInfo.id(),
                                    originalInfo.fullClassName(),
                                    originalInfo.classAttributes(),
                                    originalInfo.name(),
                                    editingProperties);
                        }
                        else {
                            return dto;
                        }
                    })
                    .toList();

            locationService.updateLocation(locationId, new LocationDto(locationId,
                    location.name(),
                    location.description(),
                    newComponentsList,
                    location.actions()));
        });

        // Після збереження (або імітації) повертаємось у режим перегляду
        ui.setMode(new ViewExistingMode(originalInfo, locationService));
    }

    @Override
    public void onCancelClicked(ComponentInfo ui) {
        // Відміняємо зміни, повертаємось у режим перегляду
        ui.setMode(new ViewExistingMode(originalInfo, locationService));
    }
}
