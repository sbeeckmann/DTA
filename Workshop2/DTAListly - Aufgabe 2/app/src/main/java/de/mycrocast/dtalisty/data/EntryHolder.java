package de.mycrocast.dtalisty.data;

import java.util.List;

public class EntryHolder {

    private String name;
    private long creationTime;
    private List<Entry> entries;

    public EntryHolder(String name) {
        this.name = name;
        this.creationTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
