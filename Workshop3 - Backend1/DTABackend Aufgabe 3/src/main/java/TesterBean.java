import entry.data.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@LocalBean
@Singleton
@Startup
public class TesterBean {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void atStartUp() {

        List<Entry> entries = this.em.createQuery("Select e from Entry e", Entry.class).getResultList();

        System.out.println("Found entries: " + entries.size());
        if (entries.isEmpty()) {
            System.out.println("Creating new active entry");

            this.createEntry("Entry1", true, Entry.Status.ACTIVE, Entry.Priority.MEDIUM);
        }

        List<Entry> doneEntries = this.em.createNamedQuery(Entry.NQ_GET_BY_STATUS, Entry.class)
                .setParameter("status", Entry.Status.DONE)
                .getResultList();

        System.out.println("Found done entries: " + doneEntries.size());
        if (doneEntries.isEmpty()) {
            this.createEntry("This is a done Entry", false, Entry.Status.DONE, Entry.Priority.MEDIUM);
        }

        List<Entry> entriesWithoutPriority = this.em.createQuery("Select e from Entry e where e.priority is null", Entry.class).getResultList();
        for (Entry entry : entriesWithoutPriority) {
            entry.setPriority(Entry.Priority.MEDIUM);
        }

        List<Entry> highPrioryEntries = this.em.createNamedQuery(Entry.NQ_GET_BY_PRIORITY, Entry.class)
                .setParameter("priority", Entry.Priority.HIGH).getResultList();

        if (highPrioryEntries.isEmpty()) {
            createEntry("This is a high priority entry", true, Entry.Status.ACTIVE, Entry.Priority.HIGH);
        }

        List<Entry> lowPriorityEntries = this.em.createNamedQuery(Entry.NQ_GET_BY_PRIORITY, Entry.class)
                .setParameter("priority", Entry.Priority.LOW).getResultList();

        if (lowPriorityEntries.isEmpty()) {
            createEntry("This is a low priority entry", true, Entry.Status.ACTIVE, Entry.Priority.LOW);
        }
    }

    private void createEntry(String name, boolean isActive, Entry.Status status, Entry.Priority priority) {
        Entry entry = new Entry();
        entry.setCreationTime(LocalDateTime.now().minusDays(2));
        entry.setName(name);
        entry.setStatus(status);
        entry.setActive(isActive);
        entry.setPriority(priority);

        this.em.persist(entry);
    }
}
