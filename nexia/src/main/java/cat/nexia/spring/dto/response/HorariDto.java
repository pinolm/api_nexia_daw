package cat.nexia.spring.dto.response;

/**
 * POJO de la clase HorariDto
 */
public class HorariDto {

    private String iniHora;

    private String fiHora;

    public HorariDto(String iniHora, String fiHora) {
        this.iniHora = iniHora;
        this.fiHora = fiHora;
    }

    public HorariDto() {
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
}
