package cat.nexia.spring.controllers;

import cat.nexia.spring.models.Reserva;
import cat.nexia.spring.models.dto.ReservaDto;
import cat.nexia.spring.payload.response.MessageResponse;
import cat.nexia.spring.service.ReservaService;
import cat.nexia.spring.utils.NexiaEnum;
import cat.nexia.spring.utils.NexiaUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NexiaEnum.DATA_TIME_FORMAT.getPhrase());

    /**
     * Search for a reserva by day.
     *
     * @param dia The day of the reserva to search for.
     * @return A response containing the reserva information if found,
     * or an error message if the reserva does not exist.
     */
    @GetMapping("/findByDia/{dia}")
    public ResponseEntity<Object> findReservaByDia(@PathVariable("dia") String dia) {
        boolean isValid = GenericValidator.isDate(dia, "yyyy-MM-dd", true);
        if (!isValid) {
            return new ResponseEntity<>("PATRÓ DE DATA INCORRECTE: " + dia +
                    "\r" +
                    "El patró correcte de la data és: \"yyyy-MM-dd\"", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            LocalDate localDate = LocalDate.parse(dia, formatter);
            List<Reserva> reservaList = reservaService.findReservaByDia(localDate);
            if (reservaList != null && !reservaList.isEmpty()) {
                return new ResponseEntity<>(reservaList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No existeixen reserves per al dia: " + dia, HttpStatus.OK);
            }
        }
    }

    /**
     * Search for all reservas.
     *
     *
     * @return A response containing the list of all reserva information if found,
     * or an empty list if the table not contains data.
     */
    @GetMapping("/findAll")
    public ResponseEntity<Object> findAllReserves(){
        try {
            return new ResponseEntity<>(reservaService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search for all reservas.
     *
     * @param reserva information like this:
     *          "idPista": 2,
     *         "idHorari": 6,
     *         "idUsuari": 5,
     *         "dia": "2023-11-01"
     * @return A response containing the reserva information if found
     * @exception Exception: si la excepción es de tipoPSQLException devuelve: ERROR: llave duplicada
     * viola restricción de unicidad «reserva_unique»
     *
     */

    @PostMapping("/guardar")
    public ResponseEntity<Object> guardarReserva(@RequestBody Reserva reserva){
        try {
            reservaService.guardarReserva(reserva);
            Reserva guardada = reservaService.findReservaByIdPistaAndIdHorariAndDia(reserva);
            if(reserva!=null){
                return new ResponseEntity<>(guardada, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(reserva, HttpStatus.OK);
            }
        } catch (Exception e) {
            if(NexiaUtils.psqlException(e) != null){
                return new ResponseEntity<>(new MessageResponse(NexiaUtils.psqlException(e)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new MessageResponse(e.getMessage()) , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search for a reserva by day.
     *
     * @param idReserva The if of the reserva to search for.
     * @return A response containing the reserva information if found,
     * or an error message if the reserva does not exist.
     */
    @GetMapping("/reservaById/{idReserva}")
    public ResponseEntity<Object> reservaById (@PathVariable("idReserva") Long idReserva) {
        try {
            return new ResponseEntity<>(reservaService.findReservaById(idReserva), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping ("/eliminarReserva/{idReserva}")
    public ResponseEntity<Object> eliminarReservaById (@PathVariable("idReserva") Long idReserva) {
        try {
            ReservaDto reservaDto = reservaService.findReservaById(idReserva);
            if (reservaDto != null) {
                reservaDto.setInfo(NexiaEnum.RESERVA_DELETE_INFO.getPhrase());
                reservaService.eliminarReservaById(idReserva);
                return new ResponseEntity<>(reservaDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse(NexiaEnum.ID_ERROR.getPhrase() + idReserva), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }





}
