package cat.nexia.spring.models.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ReservaDto {

    private Long idReserva;

    private int numPista;

    private String iniHora;

    private String fiHora;

    private String name;

    private String email;

    private LocalDate dia;

    private String info;

    public ReservaDto(Long idReserva, int numPista, String iniHora, String fiHora, String name, String email, LocalDate dia) {
        this.idReserva = idReserva;
        this.numPista = numPista;
        this.iniHora = iniHora;
        this.fiHora = fiHora;
        this.name = name;
        this.email = email;
        this.dia = dia;
    }

    public ReservaDto() {
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public int getNumPista() {
        return numPista;
    }

    public void setNumPista(int numPista) {
        this.numPista = numPista;
    }

    public String getIniHora() {
        return iniHora;
    }

    public void setIniHora(String iniHora) {
        this.iniHora = iniHora;
    }

    public String getFiHora() {
        return fiHora;
    }

    public void setFiHora(String fiHora) {
        this.fiHora = fiHora;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaDto that = (ReservaDto) o;
        return numPista == that.numPista && Objects.equals(iniHora, that.iniHora) && Objects.equals(fiHora, that.fiHora) && Objects.equals(dia, that.dia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numPista, iniHora, fiHora, dia);
    }

    @Override
    public String toString() {
        return "ReservaDto{" +
                "idReserva=" + idReserva +
                ", numPista=" + numPista +
                ", horaInici='" + iniHora + '\'' +
                ", horaFi='" + fiHora + '\'' +
                ", userName='" + name + '\'' +
                ", userEmail='" + email + '\'' +
                ", diaReserva=" + dia +
                ", info=" + info +
                '}';
    }
}
