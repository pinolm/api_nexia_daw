package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.request.CrearRespostaRequestDto;
import cat.nexia.spring.dto.response.MissatgeSimpleResponseDto;
import cat.nexia.spring.models.Resposta;
import cat.nexia.spring.service.RespostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/respuesta")
public class RespuestaController {

    @Autowired
    private RespostaService respuestaService;


    @PostMapping("/crear")
    public ResponseEntity<?> crearRespuesta(@RequestBody CrearRespostaRequestDto crearRespuestaRequest) {
        Resposta nuevaRespuesta = respuestaService.crearRespuesta(
                crearRespuestaRequest.getMissatgeId(),
                crearRespuestaRequest.getUserId(),
                crearRespuestaRequest.getContent());

        if (nuevaRespuesta == null) {
            return ResponseEntity.badRequest().body(new MissatgeSimpleResponseDto("Error al crear la respuesta."));
        }

        return ResponseEntity.ok(new MissatgeSimpleResponseDto("Respuesta creada exitosamente."));
    }

}
