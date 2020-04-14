package entry.data;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Entry.NQ_GET_BY_STATUS, query = "Select e from Entry e where e.status = :status")
})
@Entity
public class Entry {

    public static final String NQ_GET_BY_STATUS = "entry.get.by.status";

    public enum Status {
        ACTIVE,
        DONE,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private LocalDateTime creationTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
