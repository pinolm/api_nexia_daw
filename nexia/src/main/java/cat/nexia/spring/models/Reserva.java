package cat.nexia.spring.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "reserva" /*, uniqueConstraints={@UniqueConstraint(columnNames={"id_pista","id_horari","dia"})}*/)
//@SequenceGenerator(name = "reserva_id_seq", sequenceName = "reserva_id_seq", allocationSize = 1)
public class Reserva implements Serializable  {

    private static final long serialVersionUID = -5144613821559613440L;
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserva_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY/*, generator = "reserva_id_seq"*/)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_pista")
    private Long idPista;

    @Column(name = "id_horari")
    private Long idHorari;

    @Column(name = "id_usuari")
    private Long idUsuari;

    @Column(name = "dia")
    private LocalDate dia;


    @ManyToOne
    @JoinColumn(name = "id_pista", insertable = false, updatable = false)
    private Pista pista;

    @ManyToOne
    @JoinColumn(name = "id_horari", insertable = false, updatable = false)
    private Horari horari;

    @ManyToOne
    @JoinColumn(name = "id_usuari", insertable = false, updatable = false)
    private User user;


    public Reserva() {
    }

    public Reserva(Pista pista, Horari horari, User user, LocalDate dia) {
        this.pista = pista;
        this.horari = horari;
        this.user = user;
        this.dia = dia;
    }

    public Reserva(LocalDate dia, Long idPista, Long idHora, Long idUsuari) {
        this.dia = dia;
        this.idPista = idPista;
        this.idHorari = idHora;
        this.idUsuari = idUsuari;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdPista() {
        return idPista;
    }

    public void setIdPista(Long idPista) {
        this.idPista = idPista;
    }

    public Long getIdHorari() {
        return idHorari;
    }

    public void setIdHorari(Long idHorari) {
        this.idHorari = idHorari;
    }

    public Long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Long idUsuari) {
        this.idUsuari = idUsuari;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(idReserva, reserva.idReserva) && Objects.equals(idPista, reserva.idPista) && Objects.equals(idHorari, reserva.idHorari) && Objects.equals(idUsuari, reserva.idUsuari) && Objects.equals(dia, reserva.dia) && Objects.equals(pista, reserva.pista) && Objects.equals(horari, reserva.horari) && Objects.equals(user, reserva.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva, idPista, idHorari, idUsuari, dia, pista, horari, user);
    }

    @Override
    public String toString() {
        return "Reserva {" +
                "idReserva=" + idReserva +
                ", idPista=" + idPista +
                ", idHorari=" + idHorari +
                ", idUsuari=" + idUsuari +
                ", dia=" + dia +
                ", pista=" + pista +
                ", horari=" + horari +
                ", user=" + user +
                '}';
    }
}
