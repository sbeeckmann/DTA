package de.mycrocast.dtalisty.messaging.request.parameter.entryHolder;

public class CreateEntryHolderRequestParameter {
    private String name;

    public CreateEntryHolderRequestParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
