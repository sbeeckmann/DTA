package messaging.requests;


import entry.data.Entry;

public class UpdateEntryRequest {

    private Entry.Priority priority;
    private String name;
    private long entryId;

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public Entry.Priority getPriority() {
        return priority;
    }

    public void setPriority(Entry.Priority priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
