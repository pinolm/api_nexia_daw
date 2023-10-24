package cat.nexia.spring.login.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pista")
public class Pista {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


