import entry.data.Entry;
import entry.data.EntryHolder;

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
            Entry entry = new Entry();
            entry.setCreationTime(LocalDateTime.now());
            entry.setName("Entry1");
            entry.setStatus(Entry.Status.ACTIVE);
            entry.setActive(true);

            this.em.persist(entry);
        }

        List<Entry> doneEntries = this.em.createNamedQuery(Entry.NQ_GET_BY_STATUS, Entry.class)
                .setParameter("status", Entry.Status.DONE)
                .getResultList();

        System.out.println("Found done entries: " + doneEntries.size());
        if (doneEntries.isEmpty()) {
            System.out.println("Creating new done entry 2 days ago");
            Entry doneEntry = new Entry();
            doneEntry.setCreationTime(LocalDateTime.now().minusDays(2));
            doneEntry.setName("This is a done entry");
            doneEntry.setStatus(Entry.Status.DONE);
            doneEntry.setActive(false);

            this.em.persist(doneEntry);
        }

        List<Entry> totalEntries = this.em.createQuery("Select e from Entry e", Entry.class).getResultList();

        List<EntryHolder> holders = this.em.createQuery("Select eh from EntryHolder eh", EntryHolder.class).getResultList();
        if (holders.isEmpty()) {
            EntryHolder holder = new EntryHolder();
            holder.setName("Default List");
            holder.setEntries(totalEntries);

            this.em.persist(holder);
        }
    }
}
