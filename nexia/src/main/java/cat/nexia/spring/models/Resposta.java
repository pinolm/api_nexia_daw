package cat.nexia.spring.models;

import javax.persistence.*;
import java.io.Serializable;
/**
 Mòdel entitat Resposta corresponent a taula respostes
 */
@Entity
@Table(name = "respostes")
public class Resposta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missatge_id")
    private Missatge missatge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "resposta",columnDefinition = "TEXT")
    private String content;

    public Resposta() {
    }

    public Resposta(String content, User user, Missatge missatge) {
        this.content = content;
        this.user = user;
        this.missatge = missatge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Missatge getMissatge() {
        return missatge;
    }

    public void setMissatge(Missatge missatge) {
        this.missatge = missatge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
