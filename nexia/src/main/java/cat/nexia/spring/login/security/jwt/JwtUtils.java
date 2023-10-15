package cat.nexia.spring.login.security.jwt;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import cat.nexia.spring.login.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;

/**
 * Utility class for managing JWT tokens in security application.
 * Allows you to generate, validate and obtain JWT token information, as well as
 * manage
 * JWT related cookies.
 */
@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${nexia.app.jwtSecret}")
  private String jwtSecret;

  @Value("${nexia.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${nexia.app.jwtCookieName}")
  private String jwtCookie;

  /**
   * Gets the JWT token from the cookies of an HTTP request.
   *
   * @param request The HTTP request.
   * @return The JWT token as a string, or null if not found.
   */
  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  /**
   * Generates a JWT cookie from a UserDetailsImpl object.
   *
   * @param userPrincipal The UserDetailsImpl of the authenticated user.
   * @return A ResponseCookie containing the JWT token.
   */
  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true)
        .build();
    return cookie;
  }

  /**
   * Gets an empty JWT cookie, used to delete the JWT cookie.
   *
   * @return A ResponseCookie with a null value.
   */
  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  /**
   * Gets the username from a JWT token.
   *
   * @param token The JWT token to parse.
   * @return The username contained in the token.
   */
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Validates a JWT token.
   *
   * @param authToken The JWT token to validate.
   * @return true if the token is valid, false if it is not.
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  /**
   * Generates a JWT token from the given username.
   *
   * @param username The username for which the token is generated.
   * @return The JWT token generated as a string.
   */
  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }
}
