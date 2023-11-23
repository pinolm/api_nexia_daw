package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.MissatgeRequestDto;
import cat.nexia.spring.dto.response.MissatgeDetailResponseDto;
import cat.nexia.spring.dto.response.MissatgeResponseDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.models.User;
import cat.nexia.spring.service.MissatgeService;
import cat.nexia.spring.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador que gestiona els missatges i les operacions relacionades.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/missatge")
public class MissatgeController {

    // Serveis
    private final MissatgeService missatgeService;
    private final UserService userService;

    // Constants per als missatges d'error i d'èxit
    private static final String ERROR_MESSAGE_NOT_FOUND = "No s'ha trobat el missatge amb l'ID proporcionat.";
    private static final String ERROR_MESSAGE_UPDATE_PERMISSION = "No tens permisos per actualitzar aquest missatge.";
    private static final String ERROR_MESSAGE_GETTING_CURRENT_USER = "Error al obtenir l'usuari actual.";

    private static final String SUCCESS_MESSAGE_CREATE = "Missatge creat amb èxit!";
    private static final String SUCCESS_MESSAGE_UPDATE = "El missatge amb ID %d s'ha actualitzat correctament.";
    private static final String SUCCESS_MESSAGE_DELETE = "El missatge amb ID %d s'ha eliminat correctament.";

    // Altres constants
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    /**
     * Constructor que injecta els serveis necessaris pel controlador.
     *
     * @param missatgeService Servei per gestionar les operacions amb missatges.
     * @param userService     Servei per gestionar les operacions amb usuaris.
     */

    public MissatgeController(MissatgeService missatgeService, UserService userService) {
        this.missatgeService = missatgeService;
        this.userService = userService;
    }

    /**
     * Obté la llista de tots els missatges.
     *
     * Aquest endpoint permet als usuaris obtenir una llista de tots els missatges
     * disponibles.
     * Es requereix el permís de qualsevol usuari per accedir a aquest recurs.
     *
     * @return ResponseEntity amb una llista d'objectes MissatgeResponseDto al cos
     *         de la resposta.
     */
    @GetMapping("/list")
    @PreAuthorize("permitAll()")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<List<MissatgeDetailResponseDto>> getAllMissatges() {
        List<Missatge> missatges = missatgeService.getAllMissatges();
        List<MissatgeDetailResponseDto> responseDtoList = convertToResponseDtoList(missatges);
        return ResponseEntity.ok(responseDtoList);

    }

    /**
     * Obté els detalls d'un missatge pel seu ID.
     *
     * Aquest endpoint permet als usuaris obtenir els detalls d'un missatge
     * específic identificat pel seu ID.
     * Si el missatge existeix, es retorna una resposta reeixida amb els detalls del
     * missatge.
     * Si el missatge no es troba, es retorna una resposta 404 (No trobat) amb un
     * missatge d'error.
     *
     * @param id L'ID del missatge a recuperar.
     * @return ResponseEntity amb un objecte MissatgeDetailResponseDto al cos de la
     *         resposta si es troba el missatge.
     *         Si el missatge no existeix, es retorna una resposta amb l'estat 404 i
     *         un mapa que conté un missatge d'error.
     */
    @GetMapping("/getById/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> getMissatgeById(@PathVariable Long id) {
        Missatge missatge = missatgeService.getMissatgeById(id);
        return (missatge != null)
                ? ResponseEntity.ok(convertToDetailResponseDto(missatge))
                : buildErrorResponse(ERROR_MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    /**
     * Crea un nou missatge.
     *
     * L'autenticació és necessària i l'usuari actual s'obté de l'objecte
     * Authentication proporcionat.
     *
     * @param missatgeRequest La informació del missatge a crear, proporcionada al
     *                        cos de la sol·licitud.
     * @param ucBuilder       S'utilitza per construir la URI de resposta que indica
     *                        la creació reeixida del missatge.
     * @param authentication  Objecte Authentication que representa la informació
     *                        d'autenticació de l'usuari actual.
     * @return ResponseEntity amb una resposta reeixida i un objecte
     *         MissatgeSimpleResponseDto si el missatge es crea amb èxit.
     *         Si hi ha un problema amb l'autenticació, es retorna una resposta no
     *         autoritzada amb un missatge d'error.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('" + ROLE_ADMIN + "') or hasRole('" + ROLE_USER + "')")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> createMissatge(@RequestBody MissatgeRequestDto missatgeRequest,
            UriComponentsBuilder ucBuilder, Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MissatgeSimpleResponseDto(ERROR_MESSAGE_GETTING_CURRENT_USER));
        }

        Missatge missatge = new Missatge(currentUser, missatgeRequest.getTitle(), missatgeRequest.getContent());
        missatgeService.createMissatge(missatge);

        // Retorna una resposta CREATED amb un missatge d'èxit
        return buildSuccessResponse(SUCCESS_MESSAGE_CREATE, HttpStatus.CREATED);

    }

    /**
     * Actualitza el contingut d'un missatge identificat pel seu ID. Si el missatge
     * existeix i l'usuari actual té els permisos necessaris (és el propietari del
     * missatge o és un administrador), es
     * actualitza el contingut del missatge i es retorna una resposta reeixida. Si
     * el
     * missatge no es troba amb l'ID proporcionat,
     * es retorna una resposta 404 (No trobat) amb un missatge d'error.
     *
     * @param id                L'ID del missatge a actualitzar.
     * @param updatedMessageDto El missatge MissatgeRequestDto que conté el nou
     *                          contingut del missatge, proporcionat al cos de la
     *                          sol·licitud.
     * @param authentication    Objecte Authentication que representa la informació
     *                          d'autenticació de l'usuari actual.
     * @return ResponseEntity amb un mapa que conté un missatge d'èxit si el
     *         missatge
     *         s'actualitza correctament, o un missatge d'error si no es troba el
     *         missatge amb l'ID proporcionat.
     */
    @PutMapping("/update/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<Map<String, String>> updateMissatge(@PathVariable Long id,
            @RequestBody MissatgeRequestDto updatedMessageDto,
            Authentication authentication) {

        Missatge existingMissatge = missatgeService.getMissatgeById(id);

        if (existingMissatge != null) {
            String currentUsername = authentication.getName();

            if (!currentUsername.equals(existingMissatge.getUser().getUsername()) && !isAdmin(authentication)) {
                return buildErrorResponse(ERROR_MESSAGE_UPDATE_PERMISSION, HttpStatus.FORBIDDEN);
            }
            updateMissatgeContentAndTitle(existingMissatge, updatedMessageDto);
            missatgeService.createMissatge(existingMissatge);
            return buildSuccessResponse(String.format(SUCCESS_MESSAGE_UPDATE, id), HttpStatus.OK);
        } else {
            return buildErrorResponse(ERROR_MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un missatge pel seu ID.
     *
     * Espera un ID de missatge vàlid com a part de la URL. Si el missatge existeix,
     * s'elimina i es retorna una resposta
     * reeixida que indica que el missatge s'ha eliminat correctament. Si no es
     * troba el missatge amb l'ID proporcionat,
     * es retorna una resposta NOT_FOUND amb un missatge d'error.
     *
     * @param id L'ID del missatge a eliminar, proporcionat com a part de la URL.
     * @return ResponseEntity amb un mapa que conté un missatge d'èxit si el
     *         missatge
     *         s'elimina correctament, o un missatge d'error si no es troba el
     *         missatge amb l'ID proporcionat.
     */
    @DeleteMapping("/deleteById/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<Map<String, String>> deleteMissatge(@PathVariable Long id) {
        Missatge missatge = missatgeService.getMissatgeById(id);

        if (missatge != null) {
            missatgeService.deleteMissatge(id);

            return buildSuccessResponse(String.format(SUCCESS_MESSAGE_DELETE, id), HttpStatus.OK);

        } else {

            return buildErrorResponse(ERROR_MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND);

        }
    }

    /**
     * Obté l'usuari actual a partir de l'objecte Authentication.
     *
     * @param authentication Objecte Authentication que representa la informació
     *                       d'autenticació de l'usuari actual.
     * @return L'usuari actual o null si no s'ha autenticat correctament.
     */
    private User getCurrentUser(Authentication authentication) {
        return (authentication != null && authentication.isAuthenticated())
                ? userService.findByUsername(authentication.getName())
                : null;
    }

    /**
     * Verifica si l'usuari té el rol d'administrador.
     *
     * @param authentication Objecte Authentication que representa la informació
     *                       d'autenticació de l'usuari actual.
     * @return Cert si l'usuari té el rol d'administrador; fals en cas contrari.
     */

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN));
    }

    /**
     * Construeix i retorna una ResponseEntity amb un cos que conté un missatge.
     *
     * Aquest mètode pren una clau (`key`), un missatge (`message`) i
     * un estat HTTP (`status`) per construir una resposta consistent.
     * El cos de la resposta és un mapa amb la clau proporcionada
     * i el missatge associat (ja sigui d'error o d'èxit).
     * L'estat HTTP indica l'estat de la resposta.
     *
     * @param key     La clau associada al missatge (pot ser "error" o "message").
     * @param message El missatge a incloure en la resposta.
     * @param status  L'estat HTTP de la resposta.
     * @return Una ResponseEntity amb el cos del mapa que conté la clau i el
     *         missatge, amb l'estat HTTP indicat.
     */
    private ResponseEntity<Map<String, String>> buildResponse(String key, String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put(key, message);
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Construeix i retorna una ResponseEntity amb un cos que conté un missatge
     * d'error.
     *
     * Aquest mètode utilitza el mètode buildResponse per construir una resposta
     * amb la clau "error" i el missatge d'error associat.
     *
     * @param message El missatge d'error a incloure en la resposta.
     * @param status  L'estat HTTP de la resposta.
     * @return Una ResponseEntity amb el cos del mapa que conté la clau "error" i el
     *         missatge d'error, amb l'estat HTTP indicat.
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        return buildResponse("error", message, status);
    }

    /**
     * Construeix i retorna una ResponseEntity amb un cos que conté un missatge
     * d'èxit.
     *
     * Aquest mètode utilitza el mètode buildResponse per construir una resposta
     * amb la clau "message" i el missatge d'èxit associat.
     *
     * @param message El missatge d'èxit a incloure en la resposta.
     * @param status  L'estat HTTP de la resposta.
     * @return Una ResponseEntity amb el cos del mapa que conté la clau "message" i
     *         el
     *         missatge d'èxit, amb l'estat HTTP indicat.
     */
    private ResponseEntity<Map<String, String>> buildSuccessResponse(String message, HttpStatus status) {
        return buildResponse("message", message, status);
    }

    /**
     * Converteix una llista de missatges a una llista de DTO de resposta.
     *
     * @param missatges Llista de missatges a convertir.
     * @return Llista de DTO de resposta de missatge.
     */
    private List<MissatgeDetailResponseDto> convertToResponseDtoList(List<Missatge> missatges) {
        return missatges.stream()
                .map(this::convertToDetailResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Converteix un missatge a un DTO de resposta.
     *
     * @param missatge Missatge a convertir.
     * @return DTO de resposta de missatge.
     */
    private MissatgeResponseDto convertToResponseDto(Missatge missatge) {
        return new MissatgeResponseDto(
                missatge.getId(),
                missatge.getUser().getId(),
                missatge.getUser().getUsername(),
                missatge.getContent(),
                missatge.getTitle());
    }

    /**
     * Converteix un missatge a un DTO de resposta detallat.
     *
     * @param missatge Missatge a convertir.
     * @return DTO de resposta detallat de missatge.
     */
    private MissatgeDetailResponseDto convertToDetailResponseDto(Missatge missatge) {
      //  String username = userService.getUsuariById(missatge.getUser().getId()).getUsername();
        return new MissatgeDetailResponseDto(
                missatge.getId(),
                missatge.getUser().getId(),
                 missatge.getUser().getUsername(),
                missatge.getContent(),
                missatge.getTitle(),
                missatge.getCreatedAt());
    }

    /**
     * Actualitza el contingut i el títol d'un missatge amb les dades proporcionades
     * en un DTO.
     *
     * @param missatge          Missatge a actualitzar.
     * @param updatedMessageDto Dades de missatge actualitzades.
     */
    private void updateMissatgeContentAndTitle(Missatge missatge, MissatgeRequestDto updatedMessageDto) {
        missatge.setContent(updatedMessageDto.getContent());
        missatge.setTitle(updatedMessageDto.getTitle());
    }

}
