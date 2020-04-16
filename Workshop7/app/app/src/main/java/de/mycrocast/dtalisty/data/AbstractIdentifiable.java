package de.mycrocast.dtalisty.data;

public abstract class AbstractIdentifiable {
    private long id;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
