package cat.nexia.spring.models;

import javax.persistence.*;
import java.io.Serializable;
/**
 Mòdel entitat Pista corresponent a taula pista
 */
@Entity
@Table(name = "pista")
public class Pista implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pista")
    private Long id;

    @Column(name = "num_pista")
    private Integer numPista;

    @Column(name = "estat")
    private Integer estat;

    public Pista() {
    }

    public Pista(Integer numPista, Integer estat) {
        this.numPista = numPista;
        this.estat = estat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumPista() {
        return numPista;
    }

    public void setNumPista(Integer numPista) {
        this.numPista = numPista;
    }

    public Integer getEstat() {
        return estat;
    }

    public void setEstat(Integer estat) {
        this.estat = estat;
    }

}


