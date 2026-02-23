package org.banew.hdh.fxapp.layers.repo;

import org.banew.hdh.core.api.layers.data.entities.UserEntity;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private XmlDataContainer data;
    @Mock
    private XmlEntityMapper mapper;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private XmlStorage freshStorage;

    @BeforeEach
    void setUp() {
        // ІЗОЛЯЦІЯ: Абсолютно нове сховище для кожного тесту
        freshStorage = new XmlStorage();
        freshStorage.setUsers(new ArrayList<>());
        when(data.getXmlStorage()).thenReturn(freshStorage);
    }

    @Test
    @DisplayName("save(): Повинен згенерувати UUID для нового юзера і додати в базу")
    void shouldGenerateIdAndSaveNewUser() {
        UserEntity newUser = new UserEntity();
        newUser.setUsername("Banyak");
        // ID спеціально не задаємо (null)

        XmlUserInfo mappedXml = new XmlUserInfo();
        mappedXml.setUsername("Banyak");
        when(mapper.userEntityToXml(newUser)).thenReturn(mappedXml);

        userRepository.save(newUser);

        assertAll(
                () -> assertNotNull(newUser.getId(), "UUID мав бути згенерований"),
                () -> assertEquals(1, freshStorage.getUsers().size()),
                () -> assertEquals("Banyak", freshStorage.getUsers().get(0).getUsername())
        );
    }

    @Test
    @DisplayName("save(): Повинен оновити існуючого юзера без дублювання")
    void shouldUpdateExistingUser() {
        XmlUserInfo existing = new XmlUserInfo();
        existing.setId("user-1");
        existing.setUsername("OldName");
        freshStorage.getUsers().add(existing);

        UserEntity toUpdate = new UserEntity();
        toUpdate.setId("user-1");
        toUpdate.setUsername("NewName");

        XmlUserInfo updatedXml = new XmlUserInfo();
        updatedXml.setId("user-1");
        updatedXml.setUsername("NewName");

        when(mapper.userEntityToXml(toUpdate)).thenReturn(updatedXml);

        userRepository.save(toUpdate);

        assertAll(
                () -> assertEquals(1, freshStorage.getUsers().size(), "Дублікатів бути не повинно"),
                () -> assertEquals("NewName", freshStorage.getUsers().get(0).getUsername())
        );
    }

    @Test
    @DisplayName("findByUsername(): Повинен знайти всіх юзерів з однаковим іменем")
    void shouldFindAllUsersByUsername() {
        XmlUserInfo u1 = new XmlUserInfo(); u1.setUsername("admin");
        XmlUserInfo u2 = new XmlUserInfo(); u2.setUsername("guest");
        XmlUserInfo u3 = new XmlUserInfo(); u3.setUsername("admin");
        freshStorage.getUsers().addAll(List.of(u1, u2, u3));

        UserEntity e1 = new UserEntity(); e1.setUsername("admin");
        UserEntity e3 = new UserEntity(); e3.setUsername("admin");

        when(mapper.userXmlToEntity(u1)).thenReturn(e1);
        when(mapper.userXmlToEntity(u3)).thenReturn(e3);

        List<UserEntity> result = userRepository.findByUsername("admin");

        assertEquals(2, result.size());
        verify(mapper, times(2)).userXmlToEntity(any(XmlUserInfo.class));
    }

    @Test
    @DisplayName("findByLastTimeLoginAfterThan(): Повинен знайти юзера з найстарішим логіном після вказаної дати")
    void shouldFindCorrectUserByLastLogin() {
        LocalDateTime threshold = LocalDateTime.of(2026, 1, 1, 0, 0);

        XmlUserInfo u1 = new XmlUserInfo(); // Занадто старий, ігноруємо
        u1.setLastTimeLogin(threshold.minusDays(5));

        XmlUserInfo u2 = new XmlUserInfo(); // Підходить (найстаріший з тих, що після)
        u2.setLastTimeLogin(threshold.plusDays(2));

        XmlUserInfo u3 = new XmlUserInfo(); // Підходить, але він новіший за u2
        u3.setLastTimeLogin(threshold.plusDays(10));

        freshStorage.getUsers().addAll(List.of(u1, u2, u3));

        UserEntity e2 = new UserEntity(); e2.setLastTimeLogin(threshold.plusDays(2));
        UserEntity e3 = new UserEntity(); e3.setLastTimeLogin(threshold.plusDays(10));

        // Мапер викликається тільки для тих, хто пройшов фільтр
        when(mapper.userXmlToEntity(u2)).thenReturn(e2);
        when(mapper.userXmlToEntity(u3)).thenReturn(e3);

        Optional<UserEntity> result = userRepository.findByLastTimeLoginAfterThan(threshold);

        assertTrue(result.isPresent());
        assertEquals(threshold.plusDays(2), result.get().getLastTimeLogin(), "Мав обрати u2 через Comparator.comparing");
    }

    @Test
    @DisplayName("findById(): Повинен знайти юзера")
    void shouldFindById() {
        XmlUserInfo u1 = new XmlUserInfo(); u1.setId("id-99");
        freshStorage.getUsers().add(u1);

        UserEntity e1 = new UserEntity(); e1.setId("id-99");
        when(mapper.userXmlToEntity(u1)).thenReturn(e1);

        Optional<UserEntity> result = userRepository.findById("id-99");

        assertTrue(result.isPresent());
        assertEquals("id-99", result.get().getId());
    }
}