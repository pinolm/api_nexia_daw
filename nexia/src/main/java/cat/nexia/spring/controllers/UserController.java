package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.CreateUserRequestDto;
import cat.nexia.spring.dto.request.UpdateUserRequestDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.dto.response.ResponseMessage;
import cat.nexia.spring.dto.response.UserListResponseDto;
import cat.nexia.spring.mail.SendMail;
import cat.nexia.spring.mail.StringMails;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
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

    @Autowired
    private SendMail sendMail;

    // Constants per als rols d'autorització i missatges d'error
    private static final String AUTHORIZATION_ROLES = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')";
    private static final String ERROR_USER_NOT_AUTHORIZED = "No té permisos per accedir a la llista d'usuaris.";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MODERATOR = "mod";
    public static final String ROLE_USER = "user";

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
     * <p>
     * Aquest endpoint permet als usuaris amb rols d'usuari o administrador obtenir
     * una llista d'usuaris registrats al sistema.
     * Segons els permisos de l'usuari que fa la sol·licitud, es mostraran tots els
     * usuaris (si ets administrador) o només els detalls del teu propi perfil (si
     * ets un usuari regular).
     * Si l'usuari no té els permisos adequats per accedir a la llista d'usuaris,
     * es retornarà un codi d'estat 401 (No autoritzat) amb un missatge d'error.
     *
     * @return Un objecte ResponseEntity que conté una llista d'usuaris registrats o
     * un missatge d'error en cas de permisos insuficients.
     */
    @GetMapping("/list")
    @PreAuthorize(AUTHORIZATION_ROLES)
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
                            user.getSurname(),
                            user.getImage()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userResponses);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_AUTHORIZED));
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
     * Obté els detalls d'un usuari pel seu ID.
     *
     * @param userId L'ID de l'usuari a trobar.
     * @return ResponseEntity amb els detalls de l'usuari si es troba o un missatge
     * d'error si no es troba.
     */
    @GetMapping("/findById/{userId}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_FOUND));
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
                user.getSurname(),
                user.getImage())
        ;

        return ResponseEntity.ok(response);
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
                    user.getSurname(),
                    user.getImage());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MissatgeSimpleResponseDto(
                            ERROR_USER_NOT_FOUND));
        }
    }

    /**
     * Crea un nou usuari al sistema.
     * <p>
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
     * correctament o si hi ha hagut un error.
     */
    @PostMapping("/create")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto createUserRequest,
            UriComponentsBuilder ucBuilder) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto(ERROR_USERNAME_IN_USE));
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto(ERROR_EMAIL_IN_USE));
        }

        User user = new User(createUserRequest.getUsername().toLowerCase(), createUserRequest.getEmail(),
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
        String cosEmail = StringMails.cosEmailBenvinguda(user.getId(),user.getUsername(),createUserRequest.getPassword());
        sendMail.sendEmailHtml(user.getEmail(),null,null,"Benvingut/da a Nèxia", cosEmail );

        return ResponseEntity.created(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri())
                .body(new MissatgeSimpleResponseDto(SUCCESS_USER_CREATED));
    }

    /**
     * Elimina un usuari pel seu nom d'usuari.
     * <p>
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
     * correctament o si hi ha hagut un error.
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
     * <p>
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
     * correctament o si hi ha hagut un error.
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
     * <p>
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
     * correctament o si ha ocorregut un error.
     */
    @PutMapping("/update/{userId}")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
            @RequestBody UpdateUserRequestDto updateUserRequest)  {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MissatgeSimpleResponseDto(ERROR_USER_NOT_EXIST));
        }

        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
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
            String encryptedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
            user.setPassword(encryptedPassword);
        }
        if (updateUserRequest.getImage() != null){
            String file = updateUserRequest.getImage();
            if (!file.isEmpty()){
                user.setImage(file);
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MissatgeSimpleResponseDto(SUCCESS_USER_UPDATED));
    }

    /**
     * Consulta l'existència d'un usuari per l'adreça electrònica.
     *
     * @param email L'adreça de correu electrònic de l'usuari a verificar.
     * @return ResponseEntity amb un map que conté la clau "Existeix" i un valor
     * booleà que indica si el correu electrònic existeix a la base de dades.
     */
    @GetMapping("/findUserEmail/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Existeix:", emailExists);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualitzar la imatge d'un usuari. Guardar a base de dades.
     *
     * @param file Fitxer amb la imatge.
     * @param userId id de l'usuari.
     * @return Resposta si s'ha actualitzat la imatge .
     * @throws Exception no s'ha pogut guardar la imatge.
     */

    @PostMapping("/uploadImage")
    @PreAuthorize(AUTHORIZATION_ROLES)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userId") String userId ) {
        String message = "";
        try {
            if (!file.isEmpty()){
                String capcalera = "data:image;base64,";
                String encodedString = capcalera.concat(Base64.getEncoder().encodeToString(file.getBytes()));
                userRepository.updateUserById( encodedString, Long.parseLong(userId));
            } else {
                message = "Uploaded the file is empty: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }



}
