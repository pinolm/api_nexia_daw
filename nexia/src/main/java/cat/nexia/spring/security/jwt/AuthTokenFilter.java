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
 * Filtre d'autenticació JWT que intercepta les sol·licituds entrants i les comprova
 * la presència
 * d'un testimoni JWT vàlid a les galetes. Si es troba un testimoni vàlid, autentica l'usuari
 * en el context de la seguretat.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  /**
   * Mètode que s'executa automàticament per processar i verificar les sol·licituds entrants
   * presència
   * d'un testimoni JWT vàlid a les galetes. Si es troba un testimoni vàlid, autentica
   * l'usuari en el context de seguretat de les aplicacions.
   *
   * @param request La sol·licitud HTTP entrant.
   * @param response La resposta HTTP que cal generar.
   * @param filterChain Cadena de filtres per continuar processant la sol·licitud.
   * @throws ServletException Si es produeix un error en processar la sol·licitud.
   * @throws IOException Si es produeix un error d'I/O durant el processament del fitxer
   * de petició.
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
   * Analitza un testimoni JWT des d'una capçalera de sol·licitud HTTP.
   *
   * @param request La sol·licitud HTTP entrant de la qual serà el testimoni JWT extret.
   * @return El testimoni JWT contingut a la capçalera del tipus "Autorització".
   * "Portador (Bearer)", o nul si no es troba cap testimoni vàlid.
   */
  private String parseJwt(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring(7);
    }
    return null;
  }

}
