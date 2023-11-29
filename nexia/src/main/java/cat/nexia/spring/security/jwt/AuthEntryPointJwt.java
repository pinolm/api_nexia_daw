package cat.nexia.spring.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Punt d'entrada personalitzat per a l'autenticació. Aquesta classe s'encarrega del maneig
 * de respostes
 * en cas d'autenticació fallida o no autoritzada. Genera una resposta JSON
 * indicant la manca d'autorització i proporciona detalls sobre l'error.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  /**
   * Mètode que s'executa quan es troba una sol·licitud no autoritzada o
   * l'autenticació falla.
   *
   * @param request La sol·licitud HTTP entrant.
   * @param response La resposta HTTP a generar.
   * @param authException Excepció d'autenticació que activa l'esdeveniment.
   * @throws IOException Si es produeix un error d'I/O mentre es genera el fitxer
   * de resposta.
   * @throws ServletException Si es produeix un error en processar la sol·licitud.
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }

}
