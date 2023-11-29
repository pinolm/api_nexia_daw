package cat.nexia.spring.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
/**
 Mòdel entitat Manteniment corresponent a taula manteniment
 */
@Entity
@Table(name = "manteniment", uniqueConstraints = { @UniqueConstraint(columnNames = { "dia", "id_pista" }) })
public class Manteniment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manteniment")
    private Long idManteniment;

    @Column(name = "dia")
    private LocalDate dia;

    @Column(name = "id_pista")
    private Long idPista;

    @ManyToOne
    @JoinColumn(name = "id_pista", insertable = false, updatable = false)
    private Pista pista;

    public Manteniment() {
    }

    public Manteniment(LocalDate dia, Long idPista, Pista pista) {
        this.dia = dia;
        this.idPista = idPista;
        this.pista = pista;
    }

    public Long getIdManteniment() {
        return idManteniment;
    }

    public void setIdManteniment(Long idManteniment) {
        this.idManteniment = idManteniment;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public Long getIdPista() {
        return idPista;
    }

    public void setIdPista(Long idPista) {
        this.idPista = idPista;
    }

    public Pista getPista() {
        return pista;
    }

    public void setPista(Pista pista) {
        this.pista = pista;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manteniment that = (Manteniment) o;
        return Objects.equals(idManteniment, that.idManteniment) && Objects.equals(dia, that.dia) && Objects.equals(idPista, that.idPista) && Objects.equals(pista, that.pista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idManteniment, dia, idPista, pista);
    }
}
