package cat.nexia.spring.login.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reserva")
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "id_pista")
    private Pista pista;

    @ManyToOne
    @JoinColumn(name = "id_horari")
    private Horari horari;

    @ManyToOne
    @JoinColumn(name = "id_usuari")
    private User user;

    @Column(name = "dia")
    private Date dia;

    public Reserva() {
    }

    public Reserva(Pista pista, Horari horari, User user, Date dia) {
        this.pista = pista;
        this.horari = horari;
        this.user = user;
        this.dia = dia;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
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

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }
}
