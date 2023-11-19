package cat.nexia.spring.dto.response;

import java.util.Date;

public class MissatgeDetailResponseDto {

    private Long messageId;
    private Long userId;
    private String username;
    private String content;
    private Date createdAt;

    public MissatgeDetailResponseDto(Long messageId, Long userId, String username, String content, Date createdAt) {
        this.messageId = messageId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
