package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.LoginRequestDto;
import cat.nexia.spring.dto.request.RegisterRequestDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.dto.response.UserInfoResponseDto;
import cat.nexia.spring.mail.SendMail;
import cat.nexia.spring.mail.StringMails;
import cat.nexia.spring.models.ERole;
import cat.nexia.spring.models.Role;
import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.RoleRepository;
import cat.nexia.spring.repository.UserRepository;
import cat.nexia.spring.security.jwt.JwtUtils;
import cat.nexia.spring.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador per gestionar l'autenticació i el registre d'usuaris,
 * proporciona punts d'entrada per autenticar usuaris, registrar nous usuaris
 * i tancar la sessió de l'aplicació.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  private SendMail sendMail;

  public static final String ROLE_ADMIN = "admin";
  public static final String ROLE_MODERATOR = "mod";
  public static final String ROLE_USER = "user";

  public static final String ERROR_USERNAME_IN_USE = "Error: El nom d'usuari ja està en ús!";
  public static final String ERROR_EMAIL_IN_USE = "Error: El correu electrònic ja està en ús!";
  public static final String ERROR_ROLE_NOT_FOUND = "Error: Rol no trobat.";

  public static final String SUCCESSFUL_USER_CREATION = "L'usuari amb ID %d s'ha creat amb èxit.";
  public static final String SUCCESSFUL_LOGOUT = "L'usuari ha tancat la sessió amb èxit.";

  /**
   * Autentica l'usuari i emet un token JWT per a les sessions següents.
   *
   * @param loginRequest Les dades d'inici de sessió proporcionades per l'usuari.
   * @return ResponseEntity amb un token JWT i detalls de l'usuari autenticat.
   */
  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername().toLowerCase(),
            loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    String jwtTokenValue = jwtCookie.getValue();
    UserInfoResponseDto userInfoResponse = new UserInfoResponseDto(userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles, jwtTokenValue);
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(userInfoResponse);
  }

  /**
   * Registra un nou usuari al sistema amb rols i permisos específics.
   * Un cop registrat s'envia un correu al nou usuari.
   *
   * @param signUpRequest Les dades de registre proporcionades pel nou usuari.
   * @return ResponseEntity amb un missatge d'èxit o d'error.
   */
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto signUpRequest) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername().toLowerCase()))) {
      return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_USERNAME_IN_USE));
    }

    /*if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
      return ResponseEntity.badRequest()
          .body(new MissatgeSimpleResponseDto(ERROR_EMAIL_IN_USE));
    }*/

    // Crea el compte del nou usuari
    User user = new User(signUpRequest.getUsername().toLowerCase(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));
    user.setRoles(getRolesFromRequest(signUpRequest));

    User userGuardat = userRepository.save(user);
    String cosEmail = StringMails.cosEmailBenvinguda(userGuardat.getId(), userGuardat.getUsername(),signUpRequest.getPassword());
    sendMail.sendEmailHtml(userGuardat.getEmail(),null,null,"TEST Benvinguda",cosEmail);
    return ResponseEntity.ok(new MissatgeSimpleResponseDto(String.format(SUCCESSFUL_USER_CREATION, user.getId())));
  }

  /**
   * Tanca la sessió d'un usuari, invalidant el token JWT.
   *
   * @return ResponseEntity amb un missatge d'èxit.
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MissatgeSimpleResponseDto(SUCCESSFUL_LOGOUT));
  }

  /**
   * Obtenir el rol a partir del nom del rol.
   *
   * @param roleName Nom del rol.
   * @return Rol corresponent al nom especificat.
   * @throws IllegalArgumentException Si el rol no es troba o és desconegut.
   */
  private Role getRoleByName(String roleName) {
    switch (roleName.toLowerCase()) {
      case ROLE_ADMIN:
        return roleRepository.findByName(ERole.ROLE_ADMIN)
            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND + ": " + roleName));
      case ROLE_MODERATOR:
        return roleRepository.findByName(ERole.ROLE_MODERATOR)
            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND + ": " + roleName));
      case ROLE_USER:
        return roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND + ": " + roleName));
      default:
        throw new IllegalArgumentException("Rol desconegut: " + roleName);
    }
  }

  /**
   * Obté el conjunt de rols a partir de les dades de registre.
   *
   * @param signUpRequest Dades de registre del nou usuari.
   * @return Conjunt de rols associats a l'usuari.
   */
  private Set<Role> getRolesFromRequest(RegisterRequestDto signUpRequest) {
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null || strRoles.isEmpty()) {
      roles.add(getRoleByName(ROLE_USER));
    } else {
      strRoles.forEach(role -> roles.add(getRoleByName(role)));
    }

    return roles;
  }

}
