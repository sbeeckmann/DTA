package de.mycrocast.dtalisty.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryHolderManager {

    public interface EntryHolderChangeListener {
        void onEntryHolderAdded(EntryHolder entryHolder);

        void onEntryHolderUpdated(EntryHolder entryHolder);

        void onEntryHolderDeleted(EntryHolder entryHolder);
    }

    private final Map<Long, EntryHolder> entryHolders;
    private final List<EntryHolderChangeListener> changeListeners;

    public EntryHolderManager() {
        this.entryHolders = new HashMap<>();
        this.changeListeners = new ArrayList<>();
    }

    public EntryHolder findById(long entryHolderId) {
        return this.entryHolders.get(entryHolderId);
    }

    public void setEntryHolders(List<EntryHolder> entryHolderList) {
        this.entryHolders.clear();

        if (entryHolderList != null) {
            for (EntryHolder entryHolder : entryHolderList) {
                this.add(entryHolder);
            }
        }
    }

    public List<EntryHolder> getEntryHolders() {
        return new ArrayList<>(this.entryHolders.values());
    }

    public void add(EntryHolder entryHolder) {
        if (entryHolder != null) {
            this.entryHolders.put(entryHolder.getId(), entryHolder);
            for (EntryHolderChangeListener listener : this.changeListeners) {
                listener.onEntryHolderAdded(entryHolder);
            }
        }
    }

    public void update(EntryHolder entryHolder) {
        if (entryHolder != null) {
            // is in our map already a EntryHolder with the same id as the entryHolder to update
            EntryHolder previous = this.entryHolders.put(entryHolder.getId(), entryHolder);
            if (previous == null) {
                // no there is not -> we added the entryHolder
                for (EntryHolderChangeListener listener : this.changeListeners) {
                    listener.onEntryHolderAdded(entryHolder);
                }
            } else {
                // yes there is -> we updated the previous to entryHandler
                for (EntryHolderChangeListener listener : this.changeListeners) {
                    listener.onEntryHolderUpdated(entryHolder);
                }
            }
        }
    }

    public void remove(EntryHolder entryHolder) {
        if (entryHolder != null) {
            this.entryHolders.remove(entryHolder.getId());
            for (EntryHolderChangeListener listener : this.changeListeners) {
                listener.onEntryHolderDeleted(entryHolder);
            }
        }
    }

    public void addChangeListener(EntryHolderChangeListener changeListener) {
        if (changeListener != null && !this.changeListeners.contains(changeListener)) {
            this.changeListeners.add(changeListener);
        }
    }

    public void removeChangeListener(EntryHolderChangeListener changeListener) {
        if (changeListener != null) {
            this.changeListeners.remove(changeListener);
        }
    }
}
