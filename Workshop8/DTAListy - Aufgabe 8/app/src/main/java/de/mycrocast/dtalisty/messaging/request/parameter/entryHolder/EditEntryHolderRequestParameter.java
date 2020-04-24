package de.mycrocast.dtalisty.messaging.request.parameter.entryHolder;

public class EditEntryHolderRequestParameter {

    private long entryHolderId;
    private String name;

    public EditEntryHolderRequestParameter(long entryHolderId, String name) {
        this.entryHolderId = entryHolderId;
        this.name = name;
    }

    public long getEntryHolderId() {
        return this.entryHolderId;
    }

    public void setEntryHolderId(long entryHolderId) {
        this.entryHolderId = entryHolderId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
