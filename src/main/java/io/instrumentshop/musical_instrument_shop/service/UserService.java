package io.instrumentshop.musical_instrument_shop.service;

import io.instrumentshop.musical_instrument_shop.domain.User;
import io.instrumentshop.musical_instrument_shop.model.UserDTO;
import io.instrumentshop.musical_instrument_shop.repos.UserRepository;
import io.instrumentshop.musical_instrument_shop.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO authenticate(String username, String password) {
        User user = userRepository.findByNameAndPassword(username, password);
        if (user == null) {
            return null;
        } else {
            return mapToDTO(user, new UserDTO());
        }
    }

    public UserDTO get(final UUID id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final UUID id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    private void mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
    }
}
