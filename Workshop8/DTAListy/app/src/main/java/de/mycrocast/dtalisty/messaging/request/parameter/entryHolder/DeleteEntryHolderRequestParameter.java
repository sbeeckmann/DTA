package de.mycrocast.dtalisty.messaging.request.parameter.entryHolder;

public class DeleteEntryHolderRequestParameter {

    private long entryHolderId;

    public DeleteEntryHolderRequestParameter(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }

    public long getEntryHolderId() {
        return this.entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }
}
