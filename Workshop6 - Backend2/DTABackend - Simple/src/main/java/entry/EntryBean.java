package entry;

import entry.data.Entry;
import entry.data.EntryHolder;
import messaging.requests.*;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.StringWriter;
import java.util.List;

@Stateless
@LocalBean
public class EntryBean {

    @PersistenceContext
    private EntityManager em;

    public String onCreateEntry(CreateEntryRequest request) {
        EntryHolder entryHolder = this.em.find(EntryHolder.class, request.getEntryHolderId());
        if (entryHolder == null) {
            return this.buildErrorResponse("Wrong id, holder not found"); // --> TODO enum
        }

        Entry entry = new Entry();
        entry.setName(request.getName());
        entry.setStatus(Entry.Status.ACTIVE);
        entry.setCreationTime(System.currentTimeMillis());
        entry.setPriority(request.getPriority());
        entry.setActive(true);
        this.em.persist(entry);

        entryHolder.addEntry(entry);

        return this.buildJsonStringFromEntry(entry);
    }

    public String onUpdateEntry(UpdateEntryRequest updateEntryRequest) {
        Entry entry = this.em.find(Entry.class, updateEntryRequest.getEntryId());
        if (entry == null) {
            return this.buildErrorResponse("Entry not found");
        }

        entry.setPriority(updateEntryRequest.getPriority());
        entry.setName(updateEntryRequest.getName());

        return this.buildJsonStringFromEntry(entry);
    }

    public String onDeleteEntry(DeleteEntryRequest deleteEntryRequest) {
        Entry entry = this.em.find(Entry.class, deleteEntryRequest.getEntryId());
        if (entry == null) {
            return this.buildErrorResponse("Entry not found");
        }

        EntryHolder entryHolder = this.em.find(EntryHolder.class, deleteEntryRequest.getEntryHolderId());
        if (entryHolder != null) {
            entryHolder.removeEntry(entry);
        }
        this.em.remove(entry);

        return this.createEmtpyJsonObject();
    }

    public String onGetEntries() {
        List<EntryHolder> holders = this.em.createNamedQuery(EntryHolder.NQ_GET_ALL, EntryHolder.class).getResultList();
        JsonArrayBuilder entryHolderArray = Json.createArrayBuilder();

        for (EntryHolder holder : holders) {
            entryHolderArray.add(this.buildEntryHolderJson(holder));
        }

        return entryHolderArray.build().toString();
    }

    public String onCreateEntryHolder(CreateEntryHolderRequest request) {
        EntryHolder entryHolder = new EntryHolder();
        entryHolder.setName(request.getName());

        this.em.persist(entryHolder);
        return this.buildEntryHolderJson(entryHolder).toString();
    }

    public String onDeleteEntryHolder(DeleteEntryHolderRequest deleteEntryHolderRequest) {
        EntryHolder entryHolder = this.em.find(EntryHolder.class, deleteEntryHolderRequest.getEntryHolderId());
        if (entryHolder == null) {
            return this.buildErrorResponse("Entryholder not found");
        }
        for (Entry entry : entryHolder.getEntries()) {
            this.em.remove(entry);
        }
        this.em.remove(entryHolder);

        return this.createEmtpyJsonObject();
    }

    public String onChangeEntryStatusRequest(ChangeEntryStatusRequest request) {
        Entry entry = this.em.find(Entry.class, request.getEntryId());
        if (entry == null) {
            return this.buildErrorResponse("Entry not found");
        }
        entry.setActive(request.isActive());
        return this.buildJsonStringFromEntry(entry);
    }

    public String onEditEntryHolderRequest(EditEntryHolderRequest editEntryHolderRequest) {
        EntryHolder eh = this.em.find(EntryHolder.class, editEntryHolderRequest.getEntryHolderId());
        if (eh == null) {
            return this.buildErrorResponse("EntryHolder not found");
        }
        eh.setName(editEntryHolderRequest.getName());
        return this.buildEntryHolderJson(eh).toString();
    }

    private String buildJsonStringFromEntry(Entry entry) {
        JsonObject entryJson = Json.createObjectBuilder()
                .add("id", entry.getId())
                .add("creationTime", entry.getCreationTime())
                .add("name", entry.getName())
                .add("status", entry.getStatus() != null ? entry.getStatus().toString() : "")
                .add("priority", entry.getPriority().toString() != null ? entry.getPriority().toString() : "")
                .build();

        return entryJson.toString();
    }

    private JsonObject buildEntryHolderJson(EntryHolder entryHolder) {
        List<Entry> entries = entryHolder.getEntries();
        JsonArrayBuilder entryArray = Json.createArrayBuilder();
        if (entryHolder.getEntries() != null) {
            for (Entry entry : entries) {
                entryArray.add(Json.createObjectBuilder()
                        .add("id", entry.getId())
                        .add("creationTime", entry.getCreationTime())
                        .add("name", entry.getName())
                        .add("status", entry.getStatus() != null ? entry.getStatus().toString() : "")
                        .add("priority", entry.getPriority().toString() != null ? entry.getPriority().toString() : ""));
            }
        }
        return Json.createObjectBuilder()
                .add("name", entryHolder.getName())
                .add("id", entryHolder.getId())
                .add("entries", entryArray)
                .build();
    }

    private String buildErrorResponse(String error) {
        JsonObject errorJson = Json.createObjectBuilder().add("error", error).build();
        return errorJson.toString();
    }

    private String createEmtpyJsonObject() {
        JsonObject emptyJson = Json.createObjectBuilder().build();

        return emptyJson.toString();
    }
}
