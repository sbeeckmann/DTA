package de.mycrocast.dtalisty.data;

import java.util.ArrayList;
import java.util.List;

public class EntryHolder {
    private long id;
    private String name;
    private List<Entry> entries;

    public EntryHolder(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public EntryHolder() {

    }

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
}
