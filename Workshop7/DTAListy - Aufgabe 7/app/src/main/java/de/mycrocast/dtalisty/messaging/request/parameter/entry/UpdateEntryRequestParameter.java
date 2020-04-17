package de.mycrocast.dtalisty.messaging.request.parameter.entry;

import de.mycrocast.dtalisty.data.Entry;

public class UpdateEntryRequestParameter {

    private long entryId;
    private String name;
    private Entry.Priority priority;

    public UpdateEntryRequestParameter(long entryId, String name, Entry.Priority priority) {
        this.entryId = entryId;
        this.name = name;
        this.priority = priority;
    }

    public long getEntryId() {
        return this.entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entry.Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Entry.Priority priority) {
        this.priority = priority;
    }
}
