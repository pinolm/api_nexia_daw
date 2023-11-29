package cat.nexia.spring.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 Mòdel entitat Missatge corresponent a taula missatge
 */
@Entity
@Table(name = "missatges")
public class Missatge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_missatge")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuari", nullable = false)
    private User user;

    @OneToMany(mappedBy = "missatge", cascade = CascadeType.REMOVE)
    private List<Resposta> respostes;

    @Column(name = "titol_missatge")
    private String title;

    @Column(name = "missatge", nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "estat", nullable = false)
    private boolean estat;

    public Missatge() {
        this.createdAt = new Date();
    }

    public Missatge(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = new Date();
        this.estat = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Resposta> getRespostes() {
        return respostes;
    }
    
    public void setRespostes(List<Resposta> respostes) {
        this.respostes = respostes;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEstat() {
        return estat;
    }

    public void setEstat(boolean estat) {
        this.estat = estat;
    }

}
