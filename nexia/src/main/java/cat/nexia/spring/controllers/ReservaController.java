package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.response.AllReservasByDiaResponse;
import cat.nexia.spring.dto.response.AllReservasResponseDto;
import cat.nexia.spring.dto.response.MessageResponseDto;
import cat.nexia.spring.dto.response.ReservaDto;
import cat.nexia.spring.mail.SendMail;
import cat.nexia.spring.mail.StringMails;
import cat.nexia.spring.models.Reserva;
import cat.nexia.spring.service.ReservaService;
import cat.nexia.spring.utils.NexiaEnum;
import cat.nexia.spring.utils.NexiaUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private SendMail sendMail;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NexiaEnum.DATA_TIME_FORMAT.getPhrase());

    /**
     * Retrieves and returns a list of reservations for the specified date.
     *
     * @param day The date in "yyyy-MM-dd" format for which reservations will be
     *            retrieved.
     * @return ResponseEntity with the list of reservations in
     *         AllReservasByDiaResponse format if reservations are found, or an
     *         error message if there are no reservations or the date format is
     *         incorrect.
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
                // Mapeja las reservas al DTO AllReservasByDiaResponse
                List<AllReservasByDiaResponse> responseList = reservaList.stream()
                        .map(reserva -> new AllReservasByDiaResponse(
                                reserva.getUser().getId(),
                                reserva.getIdReserva(),
                                reserva.getHorari().getIdHorari(),
                                LocalTime.parse(reserva.getHorari().getIniHora()),
                                reserva.getUser().getUsername()))
                        .collect(Collectors.toList());

                return new ResponseEntity<>(responseList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No existeixen reserves per al dia: " + dia, HttpStatus.OK);
            }
        }
    }

    /**
     * Recovers all available reservations.
     *
     * @return ResponseEntity containing the list of reservations with status
     *         HttpStatus.OK if the operation is successful.
     *         On error, returns an error message with status
     *         HttpStatus.INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/findAll")
    public ResponseEntity<Object> findAllReserves() {
        try {
            List<AllReservasResponseDto> reservas = reservaService.findAll();
            return new ResponseEntity<>(reservas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search for all reservas.
     *
     * @param reserva information like this:
     *                "idPista": 2,
     *                "idHorari": 6,
     *                "idUsuari": 5,
     *                "dia": "2023-11-01"
     * @return A response containing the reserva information if found
     * @exception Exception: si la excepción es de tipoPSQLException devuelve:
     *                       ERROR: llave duplicada
     *                       viola restricción de unicidad «reserva_unique»
     *
     */
    @PostMapping("/createReserva")
    public ResponseEntity<Object> createReserva(@RequestBody Reserva reserva) {
        try {
            reservaService.guardarReserva(reserva);
            Reserva guardada = reservaService.findReservaByIdPistaAndIdHorariAndDia(reserva);
            if (reserva != null) {
                // SI GUARDA, ENVIAR EMAIL AMB CONFIRMACIÓ
                sendMail.sendEmailHtml("correuvostre@hotmail.com", null, null, "TEST", StringMails.mailPedido);
                return new ResponseEntity<>(guardada, HttpStatus.OK);
            } else {
                // SI GUARDA, ENVIAR EMAIL AMB CONFIRMACIÓ
                sendMail.sendEmailHtml("correuvostre@hotmail.com", null, null, "TEST", StringMails.mailPedido);
                return new ResponseEntity<>(reserva, HttpStatus.OK);
            }

        } catch (Exception e) {
            if (NexiaUtils.psqlException(e) != null) {

                return new ResponseEntity<>(new MessageResponseDto(NexiaUtils.psqlException(e)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new MessageResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search for a reserva by day.
     *
     * @param idReserva The if of the reserva to search for.
     * @return A response containing the reserva information if found,
     *         or an error message if the reserva does not exist.
     */
    @GetMapping("/reservaById/{idReserva}")
    public ResponseEntity<Object> reservaById(@PathVariable("idReserva") Long idReserva) {
        try {
            return new ResponseEntity<>(reservaService.findReservaById(idReserva), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteReserva/{idReserva}")
    public ResponseEntity<Object> deleteReservaById(@PathVariable("idReserva") Long idReserva) {
        try {
            ReservaDto reservaDto = reservaService.findReservaById(idReserva);
            if (reservaDto != null) {
                reservaDto.setInfo(NexiaEnum.RESERVA_DELETE_INFO.getPhrase());
                reservaService.eliminarReservaById(idReserva);
                // SI ELIMINA, ENVIAR EMAIL AMB CONFIRMACIÓ
                sendMail.sendEmailHtml("correuvostre@hotmail.com", null, null, "TEST", StringMails.mailPedido);
                return new ResponseEntity<>(reservaDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponseDto(NexiaEnum.ID_ERROR.getPhrase() + idReserva),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
