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

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        return buildResponse("error", message, status);
    }

    private ResponseEntity<Object> buildResponse(String key, String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put(key, message);
        return ResponseEntity.status(status).body(response);
    }


}
