package de.mycrocast.dtalisty.messaging.request;

public class DeleteEntryRequestParameter {

    private long entryId;

    public DeleteEntryRequestParameter(long entryId) {
        this.entryId = entryId;
    }

    public long getEntryId() {
        return this.entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }
}
