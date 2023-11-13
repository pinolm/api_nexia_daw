package cat.nexia.spring.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.Authentication;
import cat.nexia.spring.dto.request.MissatgeRequestDto;
import cat.nexia.spring.dto.response.MissatgeDetailResponseDto;
import cat.nexia.spring.dto.response.MissatgeResponseDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.models.User;
import cat.nexia.spring.service.MissatgeService;
import cat.nexia.spring.service.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/missatge")
public class MissatgeController {

    private final MissatgeService missatgeService;

    private final UserService userService;

    public MissatgeController(MissatgeService missatgeService, UserService userService) {
        this.missatgeService = missatgeService;
        this.userService = userService;
    }

    /**
     * Gets the list of all messages.
     *
     * This endpoint allows users to get a list of all available messages.
     * Permission of any user is required to access this resource.
     *
     * @return ResponseEntity with a list of MissatgeResponseDto objects in the
     *         response body.
     */
    @GetMapping("/list")
    @PreAuthorize("permitAll()")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<List<MissatgeResponseDto>> getAllMissatges() {
        List<Missatge> missatges = missatgeService.getAllMissatges();

        List<MissatgeResponseDto> responseDtoList = missatges.stream()
                .map(missatge -> new MissatgeResponseDto(
                        missatge.getId(),
                        missatge.getUser().getId(),
                        missatge.getUser().getUsername(),
                        missatge.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }

    /**
     * Gets a message by its ID.
     *
     * This endpoint allows users to get details of a specific message
     * identified by your ID. If the message exists, a successful response is
     * returned with
     * message details. If the message is not found, a response is returned
     * 404 (Not Found) with an error message.
     *
     * @param id The ID of the message to retrieve.
     * @return ResponseEntity with a MissatgeDetailResponseDto object in the
     *         response body
     *         if the message is found. If the message does not exist, a response is
     *         returned with
     *         status 404 and a map containing an error message.
     */
    @GetMapping("/getById/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> getMissatgeById(@PathVariable Long id) {
        Missatge missatge = missatgeService.getMissatgeById(id);

        if (missatge != null) {
            String username = userService.getUsuariById(missatge.getUser().getId()).getUsername();

            MissatgeDetailResponseDto responseDto = new MissatgeDetailResponseDto(
                    missatge.getId(),
                    missatge.getUser().getId(),
                    username,
                    missatge.getContent(),
                    missatge.getCreatedAt());

            return ResponseEntity.ok(responseDto);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("Error", "No se encontró el mensaje con el ID proporcionado.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Create a new message. Authentication required and the current user is
     * obtained from the provided Authentication object.
     *
     * @param missatgeRequest The message information to create, provided in the
     *                        request body.
     * @param ucBuilder       Used to construct the response URI indicating
     *                        successful creation of the message.
     * @param authentication  Authentication object that represents the
     *                        authentication information of the current user.
     * @return ResponseEntity with a successful response and a
     *         MissatgeSimpleResponseDto object if the message is created
     *         successfully.
     *         If there is a problem with authentication, an unauthorized response
     *         is returned with an error message.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> createMissatge(@RequestBody MissatgeRequestDto missatgeRequest,
            UriComponentsBuilder ucBuilder, Authentication authentication) {

        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userService.findByUsername(authentication.getName());
        }

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MissatgeSimpleResponseDto("Error al obtener el usuario actual."));
        }

        Missatge missatge = new Missatge(currentUser, missatgeRequest.getContent());

        missatgeService.createMissatge(missatge);

        return ResponseEntity.created(ucBuilder.path("/api/missatges/{id}").buildAndExpand(missatge.getId()).toUri())
                .body(new MissatgeSimpleResponseDto("Mensaje creado exitosamente!"));
    }

    /**
     * Updates the content of a message identified by its ID.
     * If the message exists and the current user has the necessary permissions (is
     * the owner of the message or is a administrator),
     * the content of the message is updated and a successful response is returned.
     * If the message is not found with the provided ID, a
     * 404 (Not Found) response with an error message.
     *
     * @param id                The ID of the message to update.
     * @param updatedMessageDto The MissatgeRequestDto object containing the new
     *                          message content, provided in the request body.
     * @param authentication    Authentication object that represents the
     *                          information
     *                          Authentication of the current user.
     * @return ResponseEntity with a Map containing a success message if the
     *         message is updated successfully,
     *         or an error message if the message with the given ID is not found.
     */
    @PutMapping("/update/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<Map<String, String>> updateMissatge(@PathVariable Long id, @RequestBody MissatgeRequestDto updatedMessageDto,
            Authentication authentication) {

        Missatge existingMissatge = missatgeService.getMissatgeById(id);

        if (existingMissatge != null) {

            String currentUsername = authentication.getName();

            if (!currentUsername.equals(existingMissatge.getUser().getUsername()) && !isAdmin(authentication)) {

                Map<String, String> response = new HashMap<>();
                response.put("Error", "No tienes permisos para actualizar este mensaje.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            existingMissatge.setContent(updatedMessageDto.getContent());
            missatgeService.createMissatge(existingMissatge);

            Map<String, String> response = new HashMap<>();
            response.put("message", "El mensaje con ID " + id + " ha sido actualizado correctamente.");
            return ResponseEntity.ok(response);
        } else {

            Map<String, String> response = new HashMap<>();
            response.put("Error", "No se encontró el mensaje con el ID proporcionado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Delete a message by its ID.
     *
     * A valid message ID is expected as part of the URL. If the message exists, it
     * is deleted and returned.
     * a successful response indicating that the message has been successfully
     * deleted. If the message is not found
     * with the given ID, a NOT_FOUND response is returned with an error message.
     *
     * @param id The ID of the message to delete, provided as part of the URL.
     * @return ResponseEntity with a Map containing a success message if the message
     *         is successfully deleted,
     *         or an error message if the message with the given ID is not found.
     */
    @DeleteMapping("/deleteById/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<Map<String, String>> deleteMissatge(@PathVariable Long id) {
        Missatge missatge = missatgeService.getMissatgeById(id);

        if (missatge != null) {
            missatgeService.deleteMissatge(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "El mensaje con ID " + id + " ha sido eliminado correctamente.");
            return ResponseEntity.ok(response);

        } else {
            Map<String, String> response = new HashMap<>();
            response.put("Error", "No se encontró el mensaje con el ID proporcionado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
