package cat.nexia.spring.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.nexia.spring.dto.request.CrearRespostaRequestDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.dto.response.RespostaResponseDto;
import cat.nexia.spring.models.Resposta;
import cat.nexia.spring.service.RespostaService;

/**
 * Controlador que gestiona les operacions relacionades amb les respostes als
 * missatges.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/resposta")
public class RespostaController {

    private final RespostaService respostaService;

    private static final String ERROR_CREATE_RESPONSE = "Error al crear la resposta.";
    private static final String RESPONSE_CREATED_SUCCESSFULLY = "Resposta creada exitosament.";

    /**
     * Constructor que injecta el servei necessari pel controlador.
     *
     * @param respostaService Servei per gestionar les operacions amb les respostes.
     */
    @Autowired
    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    /**
     * Crea una nova resposta associada a un missatge.
     *
     * @param crearRespostaRequest Objecte DTO que conté la informació necessària
     *                             per crear la resposta.
     * @return ResponseEntity amb un objecte MissatgeSimpleResponseDto indicant si
     *         la resposta es crea amb èxit o hi ha errors.
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearRespuesta(@RequestBody CrearRespostaRequestDto crearRespostaRequest) {
        Resposta novaResposta = respostaService.crearRespuesta(
                crearRespostaRequest.getMissatgeId(),
                crearRespostaRequest.getUserId(),
                crearRespostaRequest.getContent());

        if (novaResposta == null) {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto(ERROR_CREATE_RESPONSE));
        }

        return ResponseEntity.ok(new MissatgeSimpleResponseDto(RESPONSE_CREATED_SUCCESSFULLY));
    }

    /**
     * Obté les respostes associades a un missatge pel seu ID.
     *
     * @param idMissatge L'ID del missatge del qual es volen obtenir les respostes.
     * @return ResponseEntity amb un JSON que conté la llista de respostes o un
     *         missatge indicant que no hi ha respostes.
     */
    @GetMapping("/findByIdMissatge/{idMissatge}")
    public ResponseEntity<?> findByIdMissatge(@PathVariable Long idMissatge) {
        List<Resposta> respostes = respostaService.obtenerRespuestasPorMissatge(idMissatge);

        if (respostes.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No hi ha respostes associades a aquest missatge.");
            return ResponseEntity.ok(response);
        }

        List<RespostaResponseDto> respostaDtoList = respostes.stream()
                .map(resposta -> new RespostaResponseDto(
                        resposta.getId(),
                        resposta.getUser().getId(),
                        resposta.getUser().getUsername(),
                        resposta.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respostaDtoList);
    }

}
