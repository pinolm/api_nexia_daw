package cat.nexia.spring.dto.request;

public class MissatgeRequestDto {
    
    private String content;
    private String titulo;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
