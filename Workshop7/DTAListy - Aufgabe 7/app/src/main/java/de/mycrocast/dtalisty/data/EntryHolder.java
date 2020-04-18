package de.mycrocast.dtalisty.data;

import java.util.ArrayList;
import java.util.List;

public class EntryHolder extends AbstractIdentifiable {

    private static final int INVALID_POSITION = -1;

    public interface EntryChangeListener {
        void onEntryAdded(Entry entry);

        void onEntryUpdated(Entry entry);

        void onEntryDeleted(Entry entry);
    }

    private String name;
    private List<Entry> entries;
    private final List<EntryChangeListener> changeListeners;

    public EntryHolder(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
        this.changeListeners = new ArrayList<>();
    }

    public EntryHolder() {
        this.entries = new ArrayList<>();
        this.changeListeners = new ArrayList<>();
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

    public void add(Entry entry) {
        if (entry != null) {
            this.entries.add(entry);
            for (EntryChangeListener listener : this.changeListeners) {
                listener.onEntryAdded(entry);
            }
        }
    }

    public void update(Entry entry) {
        if (entry != null) {
            // is in our map already the same entry.we want to update?
            int position = this.entries.indexOf(entry);
            if (position == INVALID_POSITION) {
                // no there is not -> we need to add the entry
                this.add(entry);
            } else {
                // yes there is -> we need to update the entry
                this.entries.set(position, entry);
                for (EntryChangeListener listener : this.changeListeners) {
                    listener.onEntryUpdated(entry);
                }
            }
        }
    }

    public void remove(Entry entry) {
        if (entry != null) {
            this.entries.remove(entry);
            for (EntryChangeListener listener : this.changeListeners) {
                listener.onEntryDeleted(entry);
            }
        }
    }

    public void addChangeListener(EntryChangeListener changeListener) {
        if (changeListener != null && !this.changeListeners.contains(changeListener)) {
            this.changeListeners.add(changeListener);
        }
    }

    public void removeChangeListener(EntryChangeListener changeListener) {
        if (changeListener != null) {
            this.changeListeners.remove(changeListener);
        }
    }
}
