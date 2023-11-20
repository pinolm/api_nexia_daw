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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador per gestionar les operacions CRUD relacionades amb els usuaris.
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

    // Constants per als rols d'autorització i missatges d'error
    private static final String AUTHORIZATION_ROLES = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')";
    private static final String ERROR_USER_NOT_AUTHORIZED = "No té permisos per accedir a la llista d'usuaris.";

    private static final String ERROR_USER_NOT_FOUND = "Usuari no trobat. Si us plau, introdueixi un ID vàlid.";
    private static final String ERROR_USERNAME_IN_USE = "Error: El nom d'usuari ja està en ús!";
    private static final String ERROR_EMAIL_IN_USE = "Error: El correu electrònic ja està en ús!";

    private static final String ERROR_USER_DELETE_NOT_FOUND = "Error: Usuari no trobat.";
    private static final String ERROR_ROLE_NOT_FOUND = "Error: Rol no trobat.";
    private static final String ERROR_USER_NOT_EXIST = "Error: L'usuari no existeix.";

    private static final String SUCCESS_USER_CREATED = "Usuari creat amb èxit!";
    private static final String SUCCESS_USER_DELETED = "Usuari eliminat amb èxit.";
    private static final String SUCCESS_USER_UPDATED = "Usuari actualitzat amb èxit.";

    /**
     * Obté una llista d'usuaris.
     *
     * Aquest endpoint permet als usuaris amb rols d'usuari o administrador obtenir
     * una llista d'usuaris registrats al sistema.
     * Segons els permisos de l'usuari que fa la sol·licitud, es mostraran tots els
     * usuaris (si ets administrador) o només els detalls del teu propi perfil (si
     * ets un usuari regular).
     * Si l'usuari no té els permisos adequats per accedir a la llista d'usuaris,
     * es retornarà un codi d'estat 401 (No autoritzat) amb un missatge d'error.
     *
     * @return Un objecte ResponseEntity que conté una llista d'usuaris registrats o
     *         un missatge d'error en cas de permisos insuficients.
     */
    @GetMapping("/list")
    @PreAuthorize(AUTHORIZATION_ROLES)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> userList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (hasRoleAdmin(authentication)) {
            List<User> users = userRepository.findAll();
            List<UserListResponseDto> userResponses = users.stream()
                    .map(this::mapUserToUserListResponseDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResponses);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_AUTHORIZED));
        }
    }

    /**
     * Obté els detalls d'un usuari pel seu ID.
     *
     * @param userId L'ID de l'usuari a trobar.
     * @return ResponseEntity amb els detalls de l'usuari si es troba o un missatge
     *         d'error si no es troba.
     */
    @GetMapping("/findById/{userId}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? ResponseEntity.ok(mapUserToUserListResponseDto(user))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_FOUND));
    }

    /**
     * Obté informació sobre un usuari pel seu nom d'usuari.
     *
     * Aquest endpoint permet als usuaris amb rols d'administrador o usuari obtenir
     * informació detallada d'un usuari específic a través del seu nom d'usuari.
     * Si l'usuari que fa la sol·licitud no té els permisos adequats per accedir a
     * aquesta informació, es retornarà un codi d'estat 401 (No autoritzat)
     * juntament amb un missatge d'error.
     *
     * @param username El nom d'usuari de l'usuari a consultar.
     * @return Un objecte ResponseEntity amb la informació de l'usuari si es troba,
     *         o un missatge d'error si no es troba o si l'usuari no té permisos
     *         per accedir a aquesta informació.
     */
    @GetMapping("/findByUsername/{username}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserListResponseDto response = mapUserToUserListResponseDto(user);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_FOUND));
        }
    }

    /**
     * Crea un nou usuari al sistema.
     *
     * Aquest endpoint permet als usuaris amb rols d'administrador o usuari crear un
     * nou usuari al sistema.
     * Si l'usuari que fa la sol·licitud no té els permisos adequats per
     * crear un nou usuari, es retornarà
     * un codi d'estat 401 (No autoritzat) juntament amb un missatge d'error. A més,
     * es validarà que el nom d'usuari i el correu electrònic no estiguin en ús.
     * Si ja existeixen, es retornarà un codi d'estat 400 (Petició incorrecta).
     * amb un missatge d'error corresponent.
     *
     * @param createUserRequest Detalls de l'usuari a crear.
     * @param ucBuilder         El generador d'URI per a la ubicació del nou usuari.
     * @return Un objecte ResponseEntity que indica si l'usuari s'ha creat
     *         correctament o si hi ha hagut un error.
     */
    @PostMapping("/create")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto createUserRequest,
            UriComponentsBuilder ucBuilder) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_USERNAME_IN_USE));
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_EMAIL_IN_USE));
        }

        User user = createUserFromRequest(createUserRequest);
        userRepository.save(user);

        return ResponseEntity.created(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri())
                .body(new MissatgeSimpleResponseDto(SUCCESS_USER_CREATED));
    }

    /**
     * Elimina un usuari pel seu nom d'usuari.
     *
     * Aquest endpoint permet als usuaris amb rols d'administrador o usuari eliminar
     * un usuari existent al sistema especificant el seu nom d'usuari.
     * Si l'usuari que fa la sol·licitud no té els permisos adequats per eliminar
     * un usuari, es retornarà un codi d'estat 401 (No autoritzat) juntament amb un
     * missatge d'error.
     * Si l'usuari amb el nom especificat no es troba al sistema, es retornarà un
     * codi d'estat 400 (Petició incorrecta) amb un missatge d'error corresponent.
     *
     * @param username El nom d'usuari de l'usuari a eliminar.
     * @return Un objecte ResponseEntity que indica si l'usuari s'ha eliminat
     *         correctament o si hi ha hagut un error.
     */
    @DeleteMapping("/deleteByUsername/{username}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.ok(new MissatgeSimpleResponseDto(SUCCESS_USER_DELETED));
        } else {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_USER_DELETE_NOT_FOUND));
        }
    }

    /**
     * Elimina un usuari pel seu identificador (ID).
     *
     * Aquest endpoint permet als usuaris amb rols d'administrador o usuari eliminar
     * un usuari existent al sistema especificant el seu ID.
     * Si l'usuari que fa * la sol·licitud no té els permisos adequats per eliminar
     * un usuari, es retornarà un codi d'estat 401 (No autoritzat) juntament amb
     * un missatge d'error.
     * Si l'usuari amb l'ID especificat no és es troba al sistema, es
     * retornarà un codi d'estat 404 (No trobat) amb un missatge d'error
     * corresponent.
     *
     * @param userId L'ID de l'usuari a eliminar.
     * @return Un objecte ResponseEntity que indica si l'usuari s'ha eliminat
     *         correctament o si hi ha hagut un error.
     */
    @DeleteMapping("/deleteById/{userId}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<?> deleteById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.ok(new MissatgeSimpleResponseDto(SUCCESS_USER_DELETED));
        } else {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_USER_DELETE_NOT_FOUND));
        }
    }

    /**
     * Actualitza la informació d'un usuari existent pel seu identificador (ID).
     *
     * Aquest endpoint permet als usuaris amb rols d'usuari o administrador
     * actualitzar la informació d'un usuari existent especificant el seu ID i
     * proporcionant els detalls de l'actualització al cos de la sol·licitud.
     * Si l'usuari que realitza la sol·licitud no té els permisos adequats per
     * actualitzar un usuari, es retornarà un codi d'estat 401 (No autoritzat)
     * juntament amb un missatge d'error.
     * Si l'usuari amb l'ID especificat no es troba al sistema, es retornarà
     * un codi d'estat 400 (Petició incorrecta) juntament amb un missatge d'error
     * corresponent.
     *
     * @param userId            L'ID de l'usuari a actualitzar.
     * @param updateUserRequest Un objecte que conté els detalls de l'actualització,
     *                          com el nom, cognom, número de telèfon, adreça,
     *                          etc.
     * @return Un objecte ResponseEntity que indica si l'usuari s'ha actualitzat
     *         correctament o si ha ocorregut un error.
     */
    @PutMapping("/update/{userId}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
            @RequestBody UpdateUserRequestDto updateUserRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_EXIST));
        }
        updateUserDetails(user, updateUserRequest);
        userRepository.save(user);
        return ResponseEntity.ok(new MissatgeSimpleResponseDto(SUCCESS_USER_UPDATED));
    }

    /**
     * Comprova si l'usuari autenticat té el rol d'administrador (ROLE_ADMIN).
     *
     * @param authentication L'objecte d'autenticació que representa la informació
     *                       d'autenticació de l'usuari.
     * @return true si l'usuari té el rol d'administrador (ROLE_ADMIN), sinó
     *         retorna false.
     */
    private boolean hasRoleAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ERole.ROLE_ADMIN.name()));
    }

    /**
     * Mapa un objecte User a un objecte UserListResponseDto.
     *
     * @param user L'usuari a mapar.
     * @return Un objecte UserListResponseDto mapejat des de l'usuari.
     */

    private UserListResponseDto mapUserToUserListResponseDto(User user) {
        return new UserListResponseDto(
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
    }

    /**
     * Crea un objecte User a partir de les dades de la sol·licitud.
     *
     * @param createUserRequest Detalls de l'usuari a crear.
     * @return Un objecte User creat a partir de les dades de la sol·licitud.
     */
    private User createUserFromRequest(CreateUserRequestDto createUserRequest) {
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
            Set<Role> roles = createUserRequest.getRole().stream()
                    .map(this::getRoleByName)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return user;
    }

    /**
     * Obté un objecte Role a partir del nom del rol.
     *
     * @param roleName Nom del rol.
     * @return Un objecte Role trobat pel nom o llança una excepció si no es troba.
     */
    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(ERole.valueOf(roleName.toUpperCase()))
                .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND));
    }

    private void updateUserDetails(User user, UpdateUserRequestDto updateUserRequest) {
        user.setEmail(updateUserRequest.getEmail());
        user.setName(updateUserRequest.getName());
        user.setSurname(updateUserRequest.getSurname());
        user.setNumber(updateUserRequest.getNumber());
        user.setAddress(updateUserRequest.getAddress());
        user.setCity(updateUserRequest.getCity());
        user.setCountry(updateUserRequest.getCountry());
        user.setPostalCode(updateUserRequest.getPostalCode());
        user.setGender(updateUserRequest.getGender());
        if (updateUserRequest.getPassword() != null) {
            String encryptedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
            user.setPassword(encryptedPassword);
        }
    }

}
