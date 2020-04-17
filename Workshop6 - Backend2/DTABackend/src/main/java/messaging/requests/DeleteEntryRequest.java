package messaging.requests;

public class DeleteEntryRequest {

    private long entryId;
    private long entryHolderId;

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getEntryHolderId() {
        return entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }
}
