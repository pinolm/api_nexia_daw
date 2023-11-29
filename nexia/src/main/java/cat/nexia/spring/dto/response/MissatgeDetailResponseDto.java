package cat.nexia.spring.dto.response;

import java.util.Date;

/**
 * POJO de la clase MissatgeDetailResponseDto
 */

public class MissatgeDetailResponseDto {

    private Long messageId;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private Date createdAt;
    private int numRespostes;
    private boolean estat;

    public MissatgeDetailResponseDto(Long messageId, Long userId, String username, String title, String content, Date createdAt, int numRespostes, boolean estat) {
        this.messageId = messageId;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.numRespostes = numRespostes;
        this.estat = estat;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getRespuestaCount() {
        return numRespostes;
    }

    public void setRespuestaCount(int numRespostes) {
        this.numRespostes = numRespostes;
    }

    public boolean isEstat() {
        return estat;
    }

    public void setEstat(boolean estat) {
        this.estat = estat;
    }

}
