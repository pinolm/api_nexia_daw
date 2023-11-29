package cat.nexia.spring.models;

import javax.persistence.*;
import java.util.Objects;
/**
 Mòdel entitat ReservaTipus corresponent a taula reserva_tipus
 */
@Entity
@Table(name = "reserva_tipus", schema = "nexia", catalog = "nexia")

public class ReservaTipus {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "tipus", nullable = false)
    private int tipus;
    @Basic
    @Column(name = "descripcio", length = 50)
    private String descripcio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTipus() {
        return tipus;
    }

    public void setTipus(int tipus) {
        this.tipus = tipus;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }


    public ReservaTipus() {
    }

    public ReservaTipus(int tipus, String descripcio) {
        this.tipus = tipus;
        this.descripcio = descripcio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaTipus that = (ReservaTipus) o;
        return tipus == that.tipus && Objects.equals(id, that.id) && Objects.equals(descripcio, that.descripcio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipus, descripcio);
    }
}
