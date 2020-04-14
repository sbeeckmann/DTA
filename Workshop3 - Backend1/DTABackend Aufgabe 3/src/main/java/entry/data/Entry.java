package entry.data;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Entry.NQ_GET_BY_STATUS, query = "Select e from Entry e where e.status = :status"),
        @NamedQuery(name = Entry.NQ_GET_BY_PRIORITY, query = "Select e from Entry e where e.priority = :priority")
})
@Entity
public class Entry {

    public static final String NQ_GET_BY_STATUS = "entry.get.by.status";
    public static final String NQ_GET_BY_PRIORITY = "entry.get.by.priority";

    public enum Status {
        ACTIVE,
        DONE,
        DELETED
    }

    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
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

    @Enumerated(EnumType.STRING)
    private Priority priority;

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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
