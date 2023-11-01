package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.LoginRequestDto;
import cat.nexia.spring.dto.request.RegisterRequestDto;
import cat.nexia.spring.dto.response.MessageResponseDto;
import cat.nexia.spring.dto.response.UserInfoResponseDto;
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
  * User authentication and registration handler provides entry points
  * to authenticate users, register new users and log out of the application.
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

  /**
    * Authenticates the user and issues a JWT token for subsequent sessions.
    *
    * @param loginRequest The login details provided by the user.
    * @return ResponseEntity with a JWT token and details of the authenticated user.
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
    * Register a new user in the system with specific roles and permissions.
    *
    * @param signUpRequest The registration data provided by the new user.
    * @return ResponseEntity with a success or error message.
    */
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername().toLowerCase())) {
      return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername().toLowerCase(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponseDto("The user has been created successfully."));
  }

  /**
    * Logs out a user, invalidating the JWT token.
    *
    * @return ResponseEntity with a success message.
    */
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponseDto("the user has successfully logged out"));
  }
}
