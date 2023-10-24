package cat.nexia.spring.login.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "horari")
public class Horari {

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
