package cat.nexia.spring.dto.response;

public class MissatgeSimpleResponseDto {

	private String message;

	public MissatgeSimpleResponseDto(String message) {
	    this.message = message;
	  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
