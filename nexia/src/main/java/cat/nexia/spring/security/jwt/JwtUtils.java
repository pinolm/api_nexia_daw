package cat.nexia.spring.security.jwt;

import cat.nexia.spring.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Classe d'utilitat per gestionar fitxes JWT a l'aplicació de seguretat.
 * Permet generar, validar i obtenir informació de token JWT, així com
 * gestionar galetes relacionades amb JWT.
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
   * Obté el token JWT de les galetes d'una sol·licitud HTTP.
   *
   * @param request La sol·licitud HTTP.
   * @return El token JWT com a cadena, o nul si no es troba.
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
   * Genera una galeta JWT a partir de les dades de l'usuari proporcionades.
   *
   * @param userPrincipal L'objecte UserDetailsImpl de l'usuari autenticat.
   * @return Una ResponseCookie que conté el token JWT.
   */
  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
  }

  /**
   * Obté una galeta JWT buida, utilitzada per eliminar la galeta JWT.
   *
   * @return Una ResponseCookie amb un valor nul.
   */
  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  /**
   * Obté el nom d'usuari d'un token JWT.
   *
   * @param token El token JWT per analitzar.
   * @return El nom d'usuari contingut al token.
   */
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Valida un token JWT.
   *
   * @param authToken El token JWT a validar.
   * @return true si el token és vàlid, false si no ho és.
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
   * Genera un token JWT a partir del nom d'usuari donat.
   *
   * @param username d'usuari El nom d'usuari pel qual es genera el token.
   * @return El token JWT generat com a cadena.
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
