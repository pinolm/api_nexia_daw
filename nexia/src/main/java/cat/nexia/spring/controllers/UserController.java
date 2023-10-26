package cat.nexia.spring.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cat.nexia.spring.models.ERole;
import cat.nexia.spring.models.Role;
import cat.nexia.spring.payload.request.UpdateUserRequest;
import cat.nexia.spring.payload.response.MessageResponse;
import cat.nexia.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;

import cat.nexia.spring.payload.response.UserListResponse;
import cat.nexia.spring.repository.RoleRepository;
import cat.nexia.spring.models.User;
import cat.nexia.spring.payload.request.CreateUserRequest;

/**
 * Controller to manage CRUD operations related to users.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Gets the list of users available in the system.
     *
     * @return A list of {@link UserListResponse} objects with the users
     *         information.
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<UserListResponse> userList() {
        List<User> users = userRepository.findAll();

        List<UserListResponse> userResponses = users.stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getNumber(),
                        user.getAddress(),
                        user.getCity(),
                        user.getCountry(),
                        user.getPostalCode(),
                        user.getGender(),
                        user.getName(),
                        user.getSurname()))
                .collect(Collectors.toList());

        return userResponses;
    }

    /**
     * Search for a user by their ID.
     *
     * @param userId The ID of the user to search for.
     * @return A response containing the user information if found,
     *         or an error message if the user does not exist.
     */
    @GetMapping("/findById/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado. Por favor, introduzca un ID válido."));
        }

        UserListResponse response = new UserListResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNumber(),
                user.getAddress(),
                user.getCity(),
                user.getCountry(),
                user.getPostalCode(),
                user.getGender(),
                user.getName(),
                user.getSurname());

        return ResponseEntity.ok(response);
    }

    /**
     * Search for a user by their username.
     *
     * @param username The username to search for.
     * @return A response containing the user information if found,
     *         or an error message if the user does not exist.
     */
    @GetMapping("/findByUsername/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(
                            "Usuario no encontrado. Por favor, introduzca un nombre de usuario válido."));
        }

        UserListResponse response = new UserListResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNumber(),
                user.getAddress(),
                user.getCity(),
                user.getCountry(),
                user.getPostalCode(),
                user.getGender(),
                user.getName(),
                user.getSurname());

        return ResponseEntity.ok(response);
    }

    /**
     * Create a new user in the system.
     *
     * @param createUserRequest The details of the user to create.
     * @param ucBuilder         The URI builder for the new user's location.
     * @return A response indicating whether the user was created successfully or
     *         whether an error occurred.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest,
            UriComponentsBuilder ucBuilder) {

        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario ya está en uso!"));
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El correo electrónico ya está en uso!"));
        }

        User user = new User(createUserRequest.getUsername(), createUserRequest.getEmail(),
                passwordEncoder.encode(createUserRequest.getPassword()));

        user.setNumber(createUserRequest.getNumber());
        user.setAddress(createUserRequest.getAddress());
        user.setCity(createUserRequest.getCity());
        user.setCountry(createUserRequest.getCountry());
        user.setPostalCode(createUserRequest.getPostalCode());
        user.setGender(createUserRequest.getGender());
        user.setName(createUserRequest.getName());
        user.setSurname(createUserRequest.getSurname());

        if (createUserRequest.getRole() != null) {
            Set<Role> roles = new HashSet<>();
            createUserRequest.getRole().forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                    roles.add(userRole);
                }
            });
            user.setRoles(roles);
        }

        userRepository.save(user);

        return ResponseEntity.created(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri())
                .body(new MessageResponse("Usuario creado exitosamente!"));
    }

    /**
     * Removes a user from the system by username.
     *
     * @param username The username of the user to delete.
     * @return A response indicating whether the user was removed successfully or if
     *         an error occurred.
     */
    @DeleteMapping("/deleteByUsername/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Usuario no encontrado."));
        }

        userRepository.delete(user);

        return ResponseEntity.ok(new MessageResponse("Usuario eliminado exitosamente."));
    }

    /**
     * Delete a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return A response indicating whether the deletion was successful or if an
     *         error occurred.
     */
    @DeleteMapping("/deleteById/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Usuario no encontrado. Por favor, introduzca un ID válido."));
        }

        userRepository.delete(user);

        return ResponseEntity.ok(new MessageResponse("Usuario eliminado exitosamente."));
    }

    /**
     * Actualiza los detalles de un usuario en el sistema.
     *
     * @param userId            El ID del usuario que se actualizará.
     * @param updateUserRequest Los datos actualizados del usuario.
     * @return Una respuesta que indica si la actualización se realizó con éxito o
     *         si ocurrió un error.
     */
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El usuario no existe."));
        }

        if (updateUserRequest.getName() != null) {
            user.setName(updateUserRequest.getName());
        }

        if (updateUserRequest.getSurname() != null) {
            user.setSurname(updateUserRequest.getSurname());
        }

        if (updateUserRequest.getNumber() != null) {
            user.setNumber(updateUserRequest.getNumber());
        }

        if (updateUserRequest.getAddress() != null) {
            user.setAddress(updateUserRequest.getAddress());
        }

        if (updateUserRequest.getCity() != null) {
            user.setCity(updateUserRequest.getCity());
        }

        if (updateUserRequest.getCountry() != null) {
            user.setCountry(updateUserRequest.getCountry());
        }

        if (updateUserRequest.getPostalCode() != null) {
            user.setPostalCode(updateUserRequest.getPostalCode());
        }

        if (updateUserRequest.getGender() != null) {
            user.setGender(updateUserRequest.getGender());
        }

        if (updateUserRequest.getPassword() != null) {
            // Encriptar la nueva contraseña y guardarla
            String encryptedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
            user.setPassword(encryptedPassword);
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario actualizado exitosamente."));
    }

}
