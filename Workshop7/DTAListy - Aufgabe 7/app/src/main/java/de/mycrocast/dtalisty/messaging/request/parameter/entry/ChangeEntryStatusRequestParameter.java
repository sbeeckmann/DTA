package de.mycrocast.dtalisty.messaging.request.parameter.entry;

public class ChangeEntryStatusRequestParameter {
    private long entryId;
    private boolean active;

    public ChangeEntryStatusRequestParameter(long entryId, boolean active) {
        this.entryId = entryId;
        this.active = active;
    }

    public long getEntryId() {
        return this.entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
