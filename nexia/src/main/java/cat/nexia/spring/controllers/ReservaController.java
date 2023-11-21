package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.response.AllReservesResponseDto;
import cat.nexia.spring.dto.response.ReservaDto;
import cat.nexia.spring.mail.SendMail;
import cat.nexia.spring.mail.StringMails;
import cat.nexia.spring.models.Reserva;
import cat.nexia.spring.models.mapper.AllReservaMapper;
import cat.nexia.spring.models.mapper.ReservaMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador que gestiona les operacions relacionades amb les reserves.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private SendMail sendMail;

    public static final String EMAIL = ""; // *****************************************************
    private static final String EMAIL_RESERVA_SUCCESS_SUBJECT = "Nexia Pàdel: RESERVA";
    private static final String EMAIL_DELETE_SUBJECT = "TEST"; // *****************************************************

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_PATTERN_ERROR = "PATRÓ DE DATA INCORRECTE: {date}\r" +
            "El patró correcte de la data és: " + DATE_PATTERN;
    private static final String NO_RESERVATIONS = "No hi ha reserves per al dia: {date}";
    private static final String LIMIT_EXCEEDED = "Límit de reserves per dia superat. " +
            "Només es pot realitzar una reserva per dia";
    private static final String USER_HAS_NO_RESERVATIONS = "L'usuari: {userId} no té reserves";
    private static final String ERROR_GETTING_RESERVA = "Error al recuperar la reserva";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NexiaEnum.DATA_TIME_FORMAT.getPhrase());

    /**
     * Recupera i retorna una llista de reserves per a la data especificada.
     *
     * @param dia La data en format "yyyy-MM-dd" per a la qual es recuperaran les
     *            reserves.
     * @return ResponseEntity amb la llista de reserves en el format
     *         AllReservasByDiaResponse si es troben reserves,
     *         o un missatge d'error si no hi ha reserves o el format de la data és
     *         incorrecte.
     */
    @GetMapping("/findByDia/{dia}")
    public ResponseEntity<Object> findReservaByDia(@PathVariable("dia") String dia) {
        boolean isValid = GenericValidator.isDate(dia, DATE_PATTERN, true);
        if (!isValid) {
            return buildErrorResponse(DATE_PATTERN_ERROR.replace("{date}", dia), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            LocalDate localDate = LocalDate.parse(dia, formatter);
            List<AllReservesResponseDto> reservaList = reservaService.findReservaByDia(localDate);
            return new ResponseEntity<>(reservaList, HttpStatus.OK);

        }
    }

    /**
     * Recupera totes les reserves disponibles.
     *
     * @return ResponseEntity amb la llista de reserves amb l'estat HttpStatus.OK si
     *         l'operació és exitosa.
     *         En cas d'error, retorna un missatge d'error amb l'estat
     *         HttpStatus.INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/findAll")
    public ResponseEntity<Object> findAllReserves() {
        try {
            List<AllReservesResponseDto> reserves = reservaService.findAll();
            return new ResponseEntity<>(reserves, HttpStatus.OK);
        } catch (Exception e) {
            return buildErrorResponse("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cerca totes les reserves.
     *
     * @param reserva informació com a:
     *                "idPista": 2,
     *                "idHorari": 6,
     *                "idUsuari": 5,
     *                "dia": "2023-11-01"
     * @return Una resposta que conté la informació de la reserva si es troba
     * @exception Exception: si l'excepció és de tipus PSQLException, retorna:
     *                       ERROR: clau duplicada
     *                       viola la restricció d'unicitat «reserva_unique»
     */
    @PostMapping("/createReserva")
    public ResponseEntity<Object> createReserva(@RequestBody Reserva reserva) {
        // comprovar si l'usuari té més d'una reserva per dia, si és així, no pot
        // realitzar la reserva
        int numReserves = reservaService.countReservaByDiaAndByUser(reserva);
        if (numReserves > 0) {
            return buildErrorResponse(LIMIT_EXCEEDED, HttpStatus.OK);
        }

        try {
            reservaService.guardarReserva(reserva);
            Reserva guardada = reservaService.findReservaByIdPistaAndIdHorariAndDia(reserva);
            if (reserva != null) {
                // SI GUARDA, ENVIAR EMAIL AMB CONFIRMACIÓ. Cos de l'email millorat per Laura GC
                String cosEmail = StringMails.cosEmailReservaMillorat(guardada);
                sendMail.sendEmailHtml(guardada.getUser().getEmail(), null, null, EMAIL_RESERVA_SUCCESS_SUBJECT,
                        cosEmail);
                ReservaDto reservaDto = ReservaMapper.toReservaDto(guardada);
                return new ResponseEntity<>(reservaDto, HttpStatus.OK);
            } else {
                return buildErrorResponse(ERROR_GETTING_RESERVA, HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            return handleException(e);
        }
    }

    /**
     * Cerca una reserva per dia.
     *
     * @param idReserva L'ID de la reserva a cercar.
     * @return Una resposta que conté la informació de la reserva si es troba,
     *         o un missatge d'error si la reserva no existeix.
     */
    @GetMapping("/reservaById/{idReserva}")
    public ResponseEntity<Object> reservaById(@PathVariable("idReserva") Long idReserva) {
        try {
            AllReservesResponseDto reservesResponseDto = AllReservaMapper
                    .toReservaDto(reservaService.findReservaById(idReserva));
            return new ResponseEntity<>(reservesResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    /**
     * Cerca les reserves d'un usuari pel seu identificador.
     *
     * @param idUser L'ID de l'usuari per al qual es volen recuperar les reserves.
     * @return ResponseEntity que conté una llista de reserves
     *         (AllReservesResponseDto) si l'usuari té reserves,
     *         o un missatge indicant que l'usuari no té reserves si la llista és
     *         buida.
     *         En cas d'error, es retorna una resposta amb el missatge d'error i
     *         l'estat INTERNAL_SERVER_ERROR.
     */
    @GetMapping("/reservaByUser/{idUsuari}")
    public ResponseEntity<Object> reservaByUserId(@PathVariable("idUsuari") Long idUser) {
        try {
            List<AllReservesResponseDto> reserves = reservaService.findReservaByUserId(idUser);
            if (reserves.isEmpty()) {
                return buildSuccessResponse(USER_HAS_NO_RESERVATIONS.replace("{userId}", idUser.toString()),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(reserves, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    /**
     * Elimina una reserva pel seu identificador.
     *
     * @param idReserva L'ID de la reserva que es vol eliminar.
     * @return ResponseEntity que conté la informació de la reserva eliminada
     *         (ReservaDto) si s'elimina amb èxit,
     *         o un missatge d'error si la reserva no es troba o hi ha un problema
     *         amb l'eliminació.
     *         En cas d'èxit, s'envia un correu electrònic de confirmació amb el cos
     *         de l'email millorat.
     */

    @DeleteMapping("/deleteReserva/{idReserva}")
    public ResponseEntity<Object> deleteReservaById(@PathVariable("idReserva") Long idReserva) {
        try {
            Reserva reserva = reservaService.findReservaById(idReserva);
            ReservaDto reservaDto = ReservaMapper.toReservaDto(reserva);
            if (reservaDto != null) {
                reservaDto.setInfo(NexiaEnum.RESERVA_DELETE_INFO.getPhrase());
                reservaService.eliminarReservaById(idReserva);
                // SI ELIMINA, ENVIAR EMAIL AMB CONFIRMACIÓ
                sendMail.sendEmailHtml(EMAIL, null, null, EMAIL_DELETE_SUBJECT, StringMails.mailReserva);
                return new ResponseEntity<>(reservaDto, HttpStatus.OK);
            } else {
                return buildErrorResponse(NexiaEnum.ID_ERROR.getPhrase() + idReserva, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return handleException(e);
        }

    }

    /**
     * Construeix i retorna una ResponseEntity amb un cos que conté un missatge
     * d'error.
     *
     * @param message El missatge a incloure en la resposta.
     * @param status  L'estat HTTP de la resposta.
     * @return Una ResponseEntity amb el cos del mapa que conté la clau i el
     *         missatge d'error, amb l'estat HTTP indicat.
     */
    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        return buildResponse("error", message, status);
    }

    /**
     * Construeix i retorna una ResponseEntity amb un cos que conté un missatge
     * d'èxit.
     *
     * @param message El missatge d'èxit a incloure en la resposta.
     * @param status  L'estat HTTP de la resposta.
     * @return Una ResponseEntity amb el cos del mapa que conté la clau "message" i
     *         el
     *         missatge d'èxit, amb l'estat HTTP indicat.
     */
    private ResponseEntity<Object> buildSuccessResponse(String message, HttpStatus status) {
        return buildResponse("message", message, status);
    }

    /**
     * Construeix i retorna una ResponseEntity amb un cos que conté un missatge
     * genèric.
     *
     * @param key     La clau associada al missatge.
     * @param message El missatge a incloure en la resposta.
     * @param status  L'estat HTTP de la resposta.
     * @return Una ResponseEntity amb el cos del mapa que conté la clau i el
     *         missatge, amb l'estat HTTP indicat.
     */
    private ResponseEntity<Object> buildResponse(String key, String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put(key, message);
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Maneja les excepcions i construeix una ResponseEntity amb el missatge d'error
     * corresponent.
     *
     * @param e Excepció a gestionar.
     * @return Una ResponseEntity amb el missatge d'error i l'estat HTTP intern
     *         corresponent.
     */
    private ResponseEntity<Object> handleException(Exception e) {
        if (NexiaUtils.psqlException(e) != null) {
            return buildErrorResponse(NexiaUtils.psqlException(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
