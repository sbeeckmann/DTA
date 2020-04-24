package entry.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = EntryHolder.NQ_GET_ALL, query = "Select eh from EntryHolder eh")
})
@Entity
public class EntryHolder {

    public static final String NQ_GET_ALL = "entryholder.get.all";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Entry> entries = new ArrayList<>();

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

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addEntry(Entry toAdd) {
        this.entries.add(toAdd);
    }

    public void removeEntry(Entry toRemove) {
        this.entries.remove(toRemove);
    }
}