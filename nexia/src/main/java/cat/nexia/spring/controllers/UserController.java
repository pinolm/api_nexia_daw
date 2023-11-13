package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.CreateUserRequestDto;
import cat.nexia.spring.dto.request.UpdateUserRequestDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.dto.response.UserListResponseDto;
import cat.nexia.spring.models.ERole;
import cat.nexia.spring.models.Role;
import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.RoleRepository;
import cat.nexia.spring.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller to manage CRUD operations related to users.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Gets a list of users.
     *
     * This endpoint allows users with user or administrator roles to obtain a list
     * of users registered on the system.
     * Depending on the permissions of the user making the request, all users will
     * be shown (if you are an administrator) or only
     * your own profile details (if you are a regular user). If the user does not
     * have the appropriate permissions to access the list
     * of users, a 401 (Unauthorized) status code will be returned along with an
     * error message.
     *
     * @return A ResponseEntity object containing a list of registered users or an
     *         error message in case of missing permissions.
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> userList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (hasRoleAdmin(authentication)) {
            List<User> users = userRepository.findAll();

            List<UserListResponseDto> userResponses = users.stream()
                    .map(user -> new UserListResponseDto(
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

            return ResponseEntity.ok(userResponses);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MissatgeSimpleResponseDto("No tiene permisos para acceder a la lista de usuarios."));
        }
    }

    /**
     * Checks if the authenticated user has the administrator role (ROLE_ADMIN).
     *
     * @param authentication The Authentication object that represents the user's
     *                       authentication information.
     * @return true if the user has the administrator role (ROLE_ADMIN), otherwise
     *         returns false.
     */
    private boolean hasRoleAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Obtain the details of a user for your ID.
     *
     * @param userId The ID of the user that is nearby.
     * @return ResponseEntity with user details if it is troba or a missatge
     *         d'error if it is not troba.
     */
    @GetMapping("/findById/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MissatgeSimpleResponseDto("Usuario no encontrado. Por favor, introduzca un ID válido."));
        }

        UserListResponseDto response = new UserListResponseDto(
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
     * Gets information about a user by their username.
     *
     * This endpoint allows users with administrator or user roles to obtain
     * detailed information
     * from a specific user through their username. If the user making the request
     * does not have the appropriate permissions
     * To access this information, a 401 (Unauthorized) status code will be returned
     * along with an error message.
     *
     * @param username The username of the user to query.
     * @return A ResponseEntity object with the user information if found, or an
     *         error message if not found
     *         or if the user does not have permissions to access this information.
     */
    @GetMapping("/findByUsername/{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            UserListResponseDto response = new UserListResponseDto(
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
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MissatgeSimpleResponseDto(
                            "Usuario no encontrado. Por favor, introduzca un nombre de usuario válido."));
        }

    }

    /**
     * Create a new user in the system.
     *
     * This endpoint allows users with administrator or user roles to create a new
     * user on the system.
     * If the user making the request does not have the appropriate permissions to
     * create a new user, it will be returned
     * a 401 (Unauthorized) status code along with an error message. Additionally,
     * it will be validated that the username
     * and email are not in use. If they already exist, a status code 400 (Bad
     * Request) will be returned.
     * with a corresponding error message.
     *
     * @param createUserRequest The details of the user to create.
     * @param ucBuilder         The URI generator for the new user's location.
     * @return A ResponseEntity object indicating whether the user was created
     *         successfully or if an error occurred.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto createUserRequest,
            UriComponentsBuilder ucBuilder) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto("Error: El nombre de usuario ya está en uso!"));
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto("Error: El correo electrónico ya está en uso!"));
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
                .body(new MissatgeSimpleResponseDto("Usuario creado exitosamente!"));
    }

    /**
     * Delete a user by username.
     *
     * This endpoint allows users with administrator or user roles to delete an
     * existing user on the system
     * specifying your username. If the user making the request does not have the
     * appropriate permissions to delete
     * a user, a 401 (Unauthorized) status code will be returned along with an error
     * message. If the user with the name
     * If the specified user is not found in the system, a 400 (Bad Request) status
     * code will be returned with a
     * corresponding error message.
     *
     * @param username The username of the user to be deleted.
     * @return A ResponseEntity object indicating whether the user was deleted
     *         successfully or if an error occurred.
     */
    @DeleteMapping("/deleteByUsername/{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto("Error: Usuario no encontrado."));
        }

        userRepository.delete(user);

        return ResponseEntity.ok(new MissatgeSimpleResponseDto("Usuario eliminado exitosamente."));
    }

    /**
     * Deletes a user by their identifier (ID).
     *
     * This endpoint allows users with administrator or user roles to delete an
     * existing user on the system
     * specifying your ID. If the user making the request does not have the
     * appropriate permissions to delete a user,
     * will return a 401 (Unauthorized) status code along with an error message. If
     * the user with the specified ID is not
     * is found on the system, a 404 (Not Found) status code will be returned with a
     * corresponding error message.
     *
     * @param userId The ID of the user to delete.
     * @return A ResponseEntity object indicating whether the user was deleted
     *         successfully or if an error occurred.
     */
    @DeleteMapping("/deleteById/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> deleteById(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MissatgeSimpleResponseDto("Usuario no encontrado. Por favor, introduzca un ID válido."));
        }

        userRepository.delete(user);

        return ResponseEntity.ok(new MissatgeSimpleResponseDto("Usuario eliminado exitosamente."));
    }

    /**
     * Updates the information of an existing user by their identifier (ID).
     *
     * This endpoint allows users with user or administrator roles to update
     * information for an existing user
     * specifying your ID and providing the update details in the request body. If
     * the user who performs
     * the request does not have the appropriate permissions to update a user, a 401
     * (Unauthorized) status code will be returned
     * along with an error message. If the user with the specified ID is not in the
     * system, a login code will be returned.
     * status 400 (Bad Request) with a corresponding error message.
     *
     * @param userId            The ID of the user to update.
     * @param updateUserRequest An object containing the update details, such as
     *                          first name, last name, phone number, address, etc.
     * @return A ResponseEntity object indicating whether the user was updated
     *         successfully or if an error occurred.
     */
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
            @RequestBody UpdateUserRequestDto updateUserRequest) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto("Error: El usuario no existe."));
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

        return ResponseEntity.ok(new MissatgeSimpleResponseDto("Usuario actualizado exitosamente."));
    }

}
