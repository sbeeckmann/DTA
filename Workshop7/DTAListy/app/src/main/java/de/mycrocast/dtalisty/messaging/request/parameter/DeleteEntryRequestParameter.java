package de.mycrocast.dtalisty.messaging.request.parameter;

public class DeleteEntryRequestParameter {

    private long entryId;
    private long entryHolderId;

    public DeleteEntryRequestParameter(long entryId, long entryHolderId) {
        this.entryId = entryId;
        this.entryHolderId = entryHolderId;
    }

    public long getEntryId() {
        return this.entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getEntryHolderId() {
        return this.entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }
}
