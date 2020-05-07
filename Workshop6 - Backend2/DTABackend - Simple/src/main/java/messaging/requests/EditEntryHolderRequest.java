package messaging.requests;

public class EditEntryHolderRequest {

    private long entryHolderId;
    private String name;

    public long getEntryHolderId() {
        return entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
