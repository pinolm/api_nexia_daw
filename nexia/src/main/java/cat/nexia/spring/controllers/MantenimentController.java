package cat.nexia.spring.controllers;

import cat.nexia.spring.models.Manteniment;
import cat.nexia.spring.service.MantenimentService;
import cat.nexia.spring.utils.NexiaEnum;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador del mòdul manteniment de pista. Sols es pot accedir
 * des de rol ADMIN
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/manteniment")
public class MantenimentController {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_PATTERN_ERROR = "PATRÓ DE DATA INCORRECTE: {date}\r" +
            "El patró correcte de la data és: " + DATE_PATTERN;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NexiaEnum.DATA_TIME_FORMAT.getPhrase());

    @Autowired
    private MantenimentService mantenimentService;

    /**
     * Crear un manteniment en la taula de bbdd
     * @param dia data del manteniment
     * @param pista id de la pista en manteniment
     * @return retorna un missatge i el codi de httpStatus
     */

    @PostMapping("/create")
    public ResponseEntity<Object> createManteniment(@RequestParam(value = "dia") String dia,
                                                    @RequestParam(value = "pista") String pista) {

        boolean isValid = GenericValidator.isDate(dia, DATE_PATTERN, true);
        if (!isValid) {
            return buildErrorResponse(DATE_PATTERN_ERROR.replace("{date}", dia), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            LocalDate localDate = LocalDate.parse(dia, formatter);
            mantenimentService.guardarManteniment(localDate, Long.parseLong(pista));
            return new ResponseEntity<>("manteniment creat", HttpStatus.OK);
        }
    }

    /**
     * Obtenir el manteniment d'un dia
     * @param dia data que es vol consulta
     * @return llista dels manteniments. Existiran com a màxim 2 manteniments x dia, de la pista 1 i la 2.
     */

    @GetMapping("/findByDia/{dia}")
    public ResponseEntity<Object> findMantenimentByDia(@PathVariable("dia") String dia){
        boolean isValid = GenericValidator.isDate(dia, DATE_PATTERN, true);
        if (!isValid) {
            return buildErrorResponse(DATE_PATTERN_ERROR.replace("{date}", dia), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            LocalDate localDate = LocalDate.parse(dia, formatter);
            List<Manteniment> allMantenimentByDia = mantenimentService.findMantenimentByDia(localDate);
            if (allMantenimentByDia.isEmpty()){
                return new ResponseEntity<>("llista buida", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(allMantenimentByDia, HttpStatus.OK);
            }
        }

    }

    /**
     * Mètode per construir una resposta d'error
     * @param message missatge a retornar
     * @param status HttpStatus
     * @return resposta d'error
     */

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        return buildResponse("error", message, status);
    }

    /**
     * Mètode per construir una resposta amb èxit del procès
     * @param message missatge a retornar
     * @param status HttpStatus
     * @return resposta d'exit
     */

    private ResponseEntity<Object> buildResponse(String key, String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put(key, message);
        return ResponseEntity.status(status).body(response);
    }


}
