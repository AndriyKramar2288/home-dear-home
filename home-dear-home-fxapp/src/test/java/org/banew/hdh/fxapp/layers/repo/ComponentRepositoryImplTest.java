package org.banew.hdh.fxapp.layers.repo;

import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.fxapp.layers.repo.xml.XmlLocation;
import org.banew.hdh.fxapp.layers.repo.xml.XmlLocationComponent;
import org.banew.hdh.fxapp.layers.repo.xml.XmlStorage;
import org.banew.hdh.fxapp.layers.repo.xml.XmlUserInfo;
import org.banew.hdh.fxapp.layers.services.XmlEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComponentRepositoryImplTest {

    @Mock
    private XmlDataContainer data;
    @Mock
    private XmlEntityMapper mapper;

    @InjectMocks
    private ComponentRepositoryImpl componentRepository;

    private XmlStorage freshStorage;

    @BeforeEach
    void setUp() {
        freshStorage = new XmlStorage();
        freshStorage.setUsers(new ArrayList<>());
        when(data.getXmlStorage()).thenReturn(freshStorage);
    }

    @Test
    @DisplayName("findById(): Знаходить компонент на 3-му рівні вкладеності")
    void shouldFindComponentDeeplyNested() {
        XmlLocationComponent targetComp = new XmlLocationComponent();
        targetComp.setId("comp-001");

        XmlLocation location = new XmlLocation();
        location.setComponents(new ArrayList<>(List.of(targetComp)));

        XmlUserInfo user = new XmlUserInfo();
        user.setLocations(new ArrayList<>(List.of(location)));

        freshStorage.getUsers().add(user);

        ComponentEntity entity = new ComponentEntity();
        entity.setId("comp-001");
        when(mapper.componentXmlToEntity(targetComp)).thenReturn(entity);

        Optional<ComponentEntity> result = componentRepository.findById("comp-001");

        assertTrue(result.isPresent());
        assertEquals("comp-001", result.get().getId());
        verify(mapper, times(1)).componentXmlToEntity(targetComp);
    }

    @Test
    @DisplayName("findById(): Повертає Empty, якщо компонента немає в жодній локації")
    void shouldReturnEmptyWhenComponentNotFound() {
        // Додаємо юзера і локацію, але БЕЗ компонентів
        XmlLocation location = new XmlLocation();
        location.setComponents(new ArrayList<>());
        XmlUserInfo user = new XmlUserInfo();
        user.setLocations(new ArrayList<>(List.of(location)));
        freshStorage.getUsers().add(user);

        Optional<ComponentEntity> result = componentRepository.findById("ghost");

        assertTrue(result.isEmpty());
        verifyNoInteractions(mapper); // Мапер не має викликатись
    }
}