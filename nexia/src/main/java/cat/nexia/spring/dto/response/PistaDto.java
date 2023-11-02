package cat.nexia.spring.dto.response;

public class PistaDto {

    private Integer numPista;

    public PistaDto(Integer numPista) {
        this.numPista = numPista;
    }

    public Integer getNumPista() {
        return numPista;
    }

    public void setNumPista(Integer numPista) {
        this.numPista = numPista;
    }
}
