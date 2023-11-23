package cat.nexia.spring.dto.response;

public class MissatgeResponseDto {

    private Long messageId;
    private Long userId;
    private String username;
    private String title;
    private String content;

    public MissatgeResponseDto(Long messageId, Long userId, String username, String title, String content) {
        this.messageId = messageId;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
