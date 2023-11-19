package cat.nexia.spring.dto.response;

public class MissatgeResponseDto {
    
    private Long messageId;
    private Long userId;
    private String username;
    private String content;

    public MissatgeResponseDto(Long messageId, Long userId, String username, String content) {
        this.messageId = messageId;
        this.userId = userId;
        this.username = username;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
}
