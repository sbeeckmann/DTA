package de.mycrocast.dtalisty.data;

import java.util.ArrayList;
import java.util.List;

public class EntryHolder extends AbstractIdentifiable {

    private String name;
    private List<Entry> entries;

    public EntryHolder(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public EntryHolder() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
