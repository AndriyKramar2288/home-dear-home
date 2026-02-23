package org.banew.hdh.fxapp.layers.repo;

import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.fxapp.layers.repo.xml.XmlLocation;
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
class LocationRepositoryImplTest {

    @Mock
    private XmlDataContainer data; // Використовуємо ТІЛЬКИ одне поле data!
    @Mock
    private XmlEntityMapper mapper;

    @InjectMocks
    private LocationRepositoryImpl locationRepository;

    private XmlStorage freshStorage;
    private XmlUserInfo targetOwner;

    @BeforeEach
    void setUp() {
        freshStorage = new XmlStorage();
        freshStorage.setUsers(new ArrayList<>());

        targetOwner = new XmlUserInfo();
        targetOwner.setId("owner-123");
        targetOwner.setLocations(new ArrayList<>());
        freshStorage.getUsers().add(targetOwner);

        lenient().when(data.getXmlStorage()).thenReturn(freshStorage);
    }

    @Test
    @DisplayName("save(): Нова локація генерує ID і додається в список власника")
    void shouldSaveNewLocation() {
        UserEntity ownerEntity = new UserEntity();
        ownerEntity.setId("owner-123");

        LocationEntity newLoc = new LocationEntity();
        newLoc.setOwner(ownerEntity); // Без ID

        XmlLocation xmlLoc = new XmlLocation();
        when(mapper.locationEntityToXml(newLoc)).thenReturn(xmlLoc);

        locationRepository.save(newLoc);

        assertAll(
                () -> assertNotNull(newLoc.getId(), "UUID згенеровано"),
                () -> assertEquals(1, targetOwner.getLocations().size(), "Додано до власника"),
                () -> assertTrue(targetOwner.getLocations().contains(xmlLoc))
        );
    }

    @Test
    @DisplayName("findByUser(): Повертає незалежну копію списку локацій юзера")
    void shouldFindLocationsByUser() {
        LocationEntity loc1 = new LocationEntity();
        LocationEntity loc2 = new LocationEntity();

        UserEntity user = new UserEntity();
        user.setLocations(List.of(loc1, loc2));

        List<LocationEntity> result = locationRepository.findByUser(user);

        assertEquals(2, result.size());
        // Перевіряємо, чи це копія, спробувавши її змінити (UnsupportedOperationException)
        assertThrows(UnsupportedOperationException.class, () -> result.add(new LocationEntity()));
    }

    @Test
    @DisplayName("findByUser(): Повертає пустий список, якщо юзер null")
    void shouldReturnEmptyListIfUserIsNull() {
        List<LocationEntity> result = locationRepository.findByUser(null);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findById(): Знаходить локацію серед усіх юзерів")
    void shouldFindByIdDeepSearch() {
        XmlLocation targetLoc = new XmlLocation();
        targetLoc.setId("loc-777");
        targetOwner.getLocations().add(targetLoc);

        LocationEntity entity = new LocationEntity();
        entity.setId("loc-777");
        when(mapper.locationXmlToEntity(targetLoc)).thenReturn(entity);

        Optional<LocationEntity> result = locationRepository.findById("loc-777");

        assertTrue(result.isPresent());
        assertEquals("loc-777", result.get().getId());
    }

    @Test
    @DisplayName("deleteById(): Видаляє локацію за ID")
    void shouldDeleteById() {
        XmlLocation locToKeep = new XmlLocation(); locToKeep.setId("keep-me");
        XmlLocation locToDelete = new XmlLocation(); locToDelete.setId("delete-me");

        targetOwner.getLocations().addAll(List.of(locToKeep, locToDelete));

        locationRepository.deleteById("delete-me");

        assertEquals(1, targetOwner.getLocations().size());
        assertEquals("keep-me", targetOwner.getLocations().get(0).getId());
    }
}