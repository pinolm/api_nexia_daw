package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.CrearRespuestaRequestDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.models.Respuesta;
import cat.nexia.spring.service.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/respuesta")
public class RespuestaController {

    private final RespuestaService respuestaService;

    @Autowired
    public RespuestaController(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearRespuesta(@RequestBody CrearRespuestaRequestDto crearRespuestaRequest) {
        Respuesta nuevaRespuesta = respuestaService.crearRespuesta(
                crearRespuestaRequest.getMissatgeId(),
                crearRespuestaRequest.getUserId(),
                crearRespuestaRequest.getContenido());

        if (nuevaRespuesta == null) {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto("Error al crear la respuesta."));
        }

        return ResponseEntity.ok(new MissatgeSimpleResponseDto("Respuesta creada exitosamente."));
    }

}
