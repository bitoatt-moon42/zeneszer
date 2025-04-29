package io.instrumentshop.musical_instrument_shop.rest;

import io.instrumentshop.musical_instrument_shop.model.UserDTO;
import io.instrumentshop.musical_instrument_shop.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserResource {

    private final UserService userService;

    private UserDTO loggedInUser;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping("/{name}/{password}")
    public ResponseEntity<UserDTO> authenticate(@PathVariable(name = "name") final String name, @PathVariable(name = "password") final String password) {
        UserDTO userDTO = userService.authenticate(name, password);
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            this.loggedInUser = userDTO;
            return ResponseEntity.ok(userDTO);
        }
    }

    @GetMapping("/loggedInUser")
    public ResponseEntity<UserDTO> getLoggedInUser() {
        return ResponseEntity.ok(this.loggedInUser);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        this.loggedInUser = null;
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createUser(@RequestBody @Valid final UserDTO userDTO) {
        if (authenticate(userDTO.getName(), userDTO.getPassword()).getStatusCode() != HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        final UUID createdId = userService.create(userDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateUser(@PathVariable(name = "id") final UUID id,
                                           @RequestBody @Valid final UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
