package cat.nexia.spring.dto.response;

import java.time.LocalTime;

public class AllReservasByDiaResponse {

    private Long idUsuario;
    private Long idReserva;
    private Long idHorario;
    private LocalTime horaInicio;
    private String username;

    public AllReservasByDiaResponse(Long idUsuario, Long idReserva, Long idHorario, LocalTime horaInicio, String username) {
        this.idUsuario = idUsuario;
        this.idReserva = idReserva;
        this.idHorario = idHorario;
        this.horaInicio = horaInicio;
        this.username = username;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

}
