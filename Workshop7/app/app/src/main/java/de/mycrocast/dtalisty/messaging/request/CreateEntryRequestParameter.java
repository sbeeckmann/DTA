package de.mycrocast.dtalisty.messaging.request;

import de.mycrocast.dtalisty.data.Entry;

public class CreateEntryRequestParameter {

    private long entryHolderId;
    private String name;
    private Entry.Priority priority;

    public CreateEntryRequestParameter(long entryHolderId, String name, Entry.Priority priority) {
        this.entryHolderId = entryHolderId;
        this.name = name;
        this.priority = priority;
    }

    public long getEntryHolderId() {
        return this.entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
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