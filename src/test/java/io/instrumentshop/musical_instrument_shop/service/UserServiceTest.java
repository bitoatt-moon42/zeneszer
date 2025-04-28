package io.instrumentshop.musical_instrument_shop.service;

import io.instrumentshop.musical_instrument_shop.domain.User;
import io.instrumentshop.musical_instrument_shop.model.UserDTO;
import io.instrumentshop.musical_instrument_shop.repos.UserRepository;
import io.instrumentshop.musical_instrument_shop.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testAuthenticate_validCredentials() {
        User user = createSampleUser();
        when(userRepository.findByNameAndPassword("john_doe", "password123"))
            .thenReturn(user);

        UserDTO result = userService.authenticate("john_doe", "password123");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("john_doe");
    }

    @Test
    void testAuthenticate_invalidCredentials() {
        when(userRepository.findByNameAndPassword("wrong_user", "wrong_pass"))
            .thenReturn(null);

        UserDTO result = userService.authenticate("wrong_user", "wrong_pass");

        assertThat(result).isNull();
    }

    @Test
    void testGet_existingUser() {
        User user = createSampleUser();
        when(userRepository.findById(any()))
            .thenReturn(Optional.of(user));

        UserDTO result = userService.get(UUID.randomUUID());

        assertThat(result.getName()).isEqualTo("john_doe");
    }

    @Test
    void testGet_nonExistingUser() {
        when(userRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.get(UUID.randomUUID()));
    }

    @Test
    void testCreate_savesUser() {
        User user = createSampleUser();
        when(userRepository.save(any()))
            .thenReturn(user);

        UserDTO userDTO = createSampleUserDTO();
        UUID id = userService.create(userDTO);

        assertThat(id).isNotNull();
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testUpdate_existingUser() {
        User user = createSampleUser();
        when(userRepository.findById(any()))
            .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
            .thenReturn(user);

        UserDTO userDTO = createSampleUserDTO();
        userService.update(UUID.randomUUID(), userDTO);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testUpdate_nonExistingUser() {
        when(userRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(UUID.randomUUID(), createSampleUserDTO()));
    }

    @Test
    void testDelete_callsRepositoryDelete() {
        UUID id = UUID.randomUUID();

        userService.delete(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    private User createSampleUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("john_doe");
        user.setPassword("password123");
        return user;
    }

    private UserDTO createSampleUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setName("john_doe");
        dto.setPassword("password123");
        return dto;
    }
}
