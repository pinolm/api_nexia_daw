package cat.nexia.spring.security.jwt;

import cat.nexia.spring.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT authentication filter that intercepts incoming requests and checks for
 * presence
 * of a valid JWT token in cookies. If a valid token is found, authenticate the
 * user
 * in the context of security.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  /**
   * Method that runs automatically to process incoming requests and verify
   * presence
   * of a valid JWT token in cookies. If a valid token is found, authenticates the
   * user in the context
   * Application security.
   *
   * @param request     The incoming HTTP request.
   * @param response    The HTTP response to generate.
   * @param filterChain Filter chain to continue processing the request.
   * @throws ServletException If an error occurs while processing the request.
   * @throws IOException      If an I/O error occurs during processing of the
   *                          request.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
            null,
            userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Parses a JWT token from an HTTP request header.
   *
   * @param request The incoming HTTP request from which the JWT token will be
   *                extracted.
   * @return The JWT token contained in the "Authorization" header of type
   *         "Bearer", or null if no valid token is found.
   */
  private String parseJwt(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring(7);
    }
    return null;
  }

}
