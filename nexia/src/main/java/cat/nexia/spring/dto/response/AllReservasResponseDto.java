package cat.nexia.spring.dto.response;

import cat.nexia.spring.models.Pista;
import cat.nexia.spring.models.Horari;
import java.time.LocalDate;

public class AllReservasResponseDto {
    
    private Long idReserva;
    private LocalDate dia;
    private Pista pista;
    private Horari horari;
    private UserSimpleDto user;

    public AllReservasResponseDto() {
    }

    public AllReservasResponseDto(Long idReserva, LocalDate dia, Pista pista, Horari horari, UserSimpleDto user) {
        this.idReserva = idReserva;
        this.dia = dia;
        this.pista = pista;
        this.horari = horari;
        this.user = user;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public Pista getPista() {
        return pista;
    }

    public void setPista(Pista pista) {
        this.pista = pista;
    }

    public Horari getHorari() {
        return horari;
    }

    public void setHorari(Horari horari) {
        this.horari = horari;
    }

    public UserSimpleDto getUser() {
        return user;
    }

    public void setUser(UserSimpleDto user) {
        this.user = user;
    }

}
