package cat.nexia.spring.dto.request;

import javax.validation.constraints.NotBlank;

public class RespostaRequestDto {
    
    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
