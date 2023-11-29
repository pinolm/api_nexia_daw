package cat.nexia.spring.models;

import javax.persistence.*;
import java.io.Serializable;
/**
 Mòdel entitat Horari corresponent a taula Horari
 */
@Entity
@Table(name = "horari")
public class Horari implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horari")
    private Long idHorari;

    @Column(name = "ini_hora")
    private String iniHora;

    @Column(name = "fi_hora")
    private String fiHora;

    @Column(name = "estat")
    private int estat;

    public Horari() {
    }

    public Horari(String iniHora, String fiHora, int estat) {
        this.iniHora = iniHora;
        this.fiHora = fiHora;
        this.estat = estat;
    }

    public Long getIdHorari() {
        return idHorari;
    }

    public void setIdHorari(Long idHorari) {
        this.idHorari = idHorari;
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

    public int getEstat() {
        return estat;
    }

    public void setEstat(int estat) {
        this.estat = estat;
    }
    
}
