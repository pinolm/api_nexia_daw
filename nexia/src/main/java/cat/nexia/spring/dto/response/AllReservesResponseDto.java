package cat.nexia.spring.dto.response;

import java.time.LocalDate;

public class AllReservesResponseDto {

    private LocalDate dia;
    private Long idReserva;

    private PistaDto pista;
    private HorariDto horari;
    private UserSimpleDto user;

    public AllReservesResponseDto() {
    }

    public AllReservesResponseDto(LocalDate dia, Long idReserva, PistaDto pista, HorariDto horari, UserSimpleDto user) {
        this.dia = dia;
        this.idReserva = idReserva;
        this.horari = horari;
        this.pista = pista;
        this.user = user;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public PistaDto getPista() {
        return pista;
    }

    public void setPista(PistaDto pista) {
        this.pista = pista;
    }

    public HorariDto getHorari() {
        return horari;
    }

    public void setHorari(HorariDto horari) {
        this.horari = horari;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public UserSimpleDto getUser() {
        return user;
    }

    public void setUser(UserSimpleDto user) {
        this.user = user;
    }

}
