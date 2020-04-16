package messaging.requests;


import entry.data.Entry;

public class UpdateEntryRequest {

    private Entry.Status status;
    private long entryId;

    public Entry.Status getStatus() {
        return status;
    }

    public void setStatus(Entry.Status status) {
        this.status = status;
    }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }
}
