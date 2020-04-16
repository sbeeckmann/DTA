package messaging.requests;

import entry.data.Entry;

public class CreateEntryRequest {

    private long entryHolderId;
    private String name;
    private Entry.Priority priority;

    public long getEntryHolderId() {
        return entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entry.Priority getPriority() {
        return priority;
    }

    public void setPriority(Entry.Priority priority) {
        this.priority = priority;
    }
}
